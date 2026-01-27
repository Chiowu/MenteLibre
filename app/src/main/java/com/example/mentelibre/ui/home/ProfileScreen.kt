package com.example.mentelibre.ui.profile

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.example.mentelibre.data.profile.ProfileDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

fun cropToCircle(context: Context, uri: Uri): Uri {
    // Leer bitmap original
    var source = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)

    // --- CORREGIR ORIENTACIÓN EXIF ---
    val inputStream = context.contentResolver.openInputStream(uri)
    val exif = androidx.exifinterface.media.ExifInterface(inputStream!!)

    val orientation = exif.getAttributeInt(
        androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION,
        androidx.exifinterface.media.ExifInterface.ORIENTATION_NORMAL
    )

    val rotation = when (orientation) {
        androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_90 -> 90f
        androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_180 -> 180f
        androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_270 -> 270f
        else -> 0f
    }

    if (rotation != 0f) {
        val matrix = Matrix()
        matrix.postRotate(rotation)
        source = Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    // --- RECORTE A CUADRADO ---
    val size = minOf(source.width, source.height)
    val x = (source.width - size) / 2
    val y = (source.height - size) / 2

    val square = Bitmap.createBitmap(source, x, y, size, size)

    // --- CREAR BITMAP CIRCULAR ---
    val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)

    val paint = Paint().apply { isAntiAlias = true }
    val rect = Rect(0, 0, size, size)
    val rectF = RectF(rect)

    canvas.drawARGB(0, 0, 0, 0)
    canvas.drawOval(rectF, paint)

    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(square, rect, rect, paint)

    // --- GUARDAR ARCHIVO ---
    val file = File(context.filesDir, "profile_image.png")
    FileOutputStream(file).use {
        output.compress(Bitmap.CompressFormat.PNG, 100, it)
    }
    return Uri.fromFile(file)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = remember { ProfileDataStore(context) }

    // ---------- ESTADOS ----------
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }

    var userName by remember { mutableStateOf("") }
    var userEmail by remember { mutableStateOf("") }
    var userPhone by remember { mutableStateOf("") }

    var isEditingProfile by remember { mutableStateOf(false) }
    var showPicker by remember { mutableStateOf(false) }
    val appBarColor = Color(0xFF4A3F6B)

    // ---------- OBSERVAR DATOS ----------
    // Profile image se observa directamente desde DataStore
    val profileImageUri by dataStore.profileImageUri.collectAsState(initial = null)

    // Otros datos
    LaunchedEffect(Unit) {
        dataStore.userName.collect { userName = it ?: "" }
    }
    LaunchedEffect(Unit) {
        dataStore.userEmail.collect { userEmail = it ?: "" }
    }
    LaunchedEffect(Unit) {
        dataStore.userPhone.collect { userPhone = it ?: "" }
    }

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                scope.launch {
                    val circularUri = cropToCircle(context, it)
                    dataStore.saveProfileImage(circularUri.toString())
                }
            }
        }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && cameraImageUri != null) {
                scope.launch {
                    val circularUri = cropToCircle(context, cameraImageUri!!)
                    dataStore.saveProfileImage(circularUri.toString())
                }
            }
        }

    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it && cameraImageUri != null) cameraLauncher.launch(cameraImageUri!!)
        }

    val galleryPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) galleryLauncher.launch("image/*")
        }

    // ---------- FONDO ----------
    val bg = Brush.verticalGradient(
        listOf(
            Color(0xFFFFE6EC),
            Color(0xFFF6D8E8),
            Color(0xFFEADCF5)
        )
    )
    val forceReload = remember(profileImageUri) { System.currentTimeMillis() }

    // ---------- UI ----------
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Perfil",
                        fontSize = 29.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = appBarColor
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = appBarColor,
                            modifier = Modifier.size(29.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bg)
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(19.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(1.dp))

            // FOTO
            Box(
                modifier = Modifier.size(180.dp),
                contentAlignment = Alignment.Center
            ) {

                // FOTO PERFIL
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color(0xFFDCEFEA), CircleShape)
                        .border(3.dp, Color(0xFF4A3F6B), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (profileImageUri != null) {
                        AsyncImage(
                            model = profileImageUri.toString() + "?t=$forceReload",
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                        )
                    } else {
                        Text(
                            "Tocar para\nagregar foto",
                            fontSize = 14.sp,
                            color = Color.DarkGray,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }

                // BOTÓN CÁMARA
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .align(Alignment.BottomCenter)
                        .offset(y = 10.dp)
                        .background(Color.White, CircleShape)
                        .border(2.dp, Color(0xFF4A3F6B), CircleShape)
                        .clickable { showPicker = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Cambiar foto",
                        tint = Color(0xFF4A3F6B),
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(Modifier.height(44.dp))

            // ---------- PERFIL ----------
            Card(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFEADCF8) // mismo color que HomeMiniCard
                ),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {

                    if (isEditingProfile) {

                        OutlinedTextField(
                            value = userName,
                            onValueChange = { userName = it },
                            label = { Text("Apodo favorito") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(14.dp))

                        OutlinedTextField(
                            value = userEmail,
                            onValueChange = { userEmail = it },
                            label = { Text("Correo electrónico") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(14.dp))

                        OutlinedTextField(
                            value = userPhone,
                            onValueChange = { userPhone = it },
                            label = { Text("Teléfono") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(20.dp))

                        Button(
                            onClick = {
                                scope.launch {
                                    dataStore.saveUserName(userName)
                                    dataStore.saveUserEmail(userEmail)
                                    dataStore.saveUserPhone(userPhone)
                                    isEditingProfile = false
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Guardar cambios")
                        }

                    } else {

                        Text(
                            text = userName.ifBlank { "Tu apodo" },
                            fontSize = 22.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF4A3F6B)
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = userEmail.ifBlank { "correo@ejemplo.com" },
                            fontSize = 15.sp,
                            color = Color.DarkGray
                        )

                        Spacer(Modifier.height(6.dp))

                        Text(
                            text = userPhone.ifBlank { "Teléfono no agregado" },
                            fontSize = 15.sp,
                            color = Color.DarkGray
                        )

                        Spacer(Modifier.height(16.dp))

                        Text(
                            text = "Editar perfil",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF4A3F6B),
                            modifier = Modifier.clickable {
                                isEditingProfile = true
                            }
                        )
                    }
                }
            }


            // ---------- DIALOGO FOTO ----------
            if (showPicker) {
                AlertDialog(
                    onDismissRequest = { showPicker = false },
                    title = { Text("Foto de perfil") },
                    text = { Text("Selecciona una opción") },
                    confirmButton = {
                        TextButton(onClick = {
                            showPicker = false
                            val permission =
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                                    Manifest.permission.READ_MEDIA_IMAGES
                                else Manifest.permission.READ_EXTERNAL_STORAGE

                            if (ContextCompat.checkSelfPermission(
                                    context,
                                    permission
                                ) == PackageManager.PERMISSION_GRANTED
                            ) galleryLauncher.launch("image/*")
                            else galleryPermissionLauncher.launch(permission)
                        }) { Text("Galería") }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            showPicker = false
                            val file = File(
                                context.cacheDir,
                                "camera_${System.currentTimeMillis()}.jpg"
                            )
                            cameraImageUri = FileProvider.getUriForFile(
                                context,
                                "com.example.mentelibre.fileprovider",
                                file
                            )

                            if (ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CAMERA
                                ) == PackageManager.PERMISSION_GRANTED
                            ) cameraLauncher.launch(cameraImageUri!!)
                            else cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }) { Text("Cámara") }
                    }
                )
            }
        }
    }
}