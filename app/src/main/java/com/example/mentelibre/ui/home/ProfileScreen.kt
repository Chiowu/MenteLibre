package com.example.mentelibre.ui.profile

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.launch
import java.io.File


@Composable
fun ProfileScreen() {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = remember { ProfileDataStore(context) }

    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }
    var userName by remember { mutableStateOf("Nombre Usuario") }
    var isEditingName by remember { mutableStateOf(false) }
    var showPicker by remember { mutableStateOf(false) }

    // Cargar datos guardados
    LaunchedEffect(Unit) {
        dataStore.profileImageUri.collect { uri ->
            profileImageUri = uri?.let { Uri.parse(it) }
        }
    }

    LaunchedEffect(Unit) {
        dataStore.userName.collect { name ->
            userName = name ?: "Nombre Usuario"
        }
    }

    //(recorte)
    val cropLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val resultUri = UCrop.getOutput(result.data!!)
                resultUri?.let {
                    profileImageUri = it
                    scope.launch {
                        dataStore.saveProfileImage(it.toString())
                    }
                }
            }
        }

    // GALERÍA → RECORTE
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                profileImageUri = it
                scope.launch {
                    dataStore.saveProfileImage(it.toString())
                }
            }
        }


    // 📷 CÁMARA REAL → ARCHIVO → RECORTE
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && cameraImageUri != null) {
                val destinationUri = Uri.fromFile(
                    File(context.cacheDir, "cropped_${System.currentTimeMillis()}.jpg")
                )

                val intent = UCrop.of(cameraImageUri!!, destinationUri)
                    .withAspectRatio(1f, 1f)
                    .getIntent(context)

                cropLauncher.launch(intent)
            }
        }

    // PERMISOS
    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted && cameraImageUri != null) {
                cameraLauncher.launch(cameraImageUri!!)
            }
        }

    val galleryPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) galleryLauncher.launch("image/*")
        }

    val bg = Brush.verticalGradient(
        listOf(
            Color(0xFFFFE6EC),
            Color(0xFFF6D8E8),
            Color(0xFFEADCF5)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(24.dp))

        // 👤 FOTO PERFIL
        Box(
            modifier = Modifier
                .size(140.dp)
                .background(Color(0xFFDCEFEA), CircleShape)
                .clickable { showPicker = true },
            contentAlignment = Alignment.Center
        ) {
            if (profileImageUri != null) {
                AsyncImage(
                    model = profileImageUri,
                    contentDescription = "Foto de perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text("Tocar para\nagregar foto", fontSize = 14.sp)
            }
        }

        Spacer(Modifier.height(24.dp))

        // NOMBRE EDITABLE
        if (isEditingName) {
            OutlinedTextField(
                value = userName,
                onValueChange = { userName = it },
                singleLine = true,
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    scope.launch {
                        dataStore.saveUserName(userName)
                        isEditingName = false
                    }
                }
            ) {
                Text("Guardar")
            }
        } else {
            Text(
                text = userName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { isEditingName = true }
            )
        }

        Text(
            text = "correo@ejemplo.com",
            fontSize = 14.sp,
            color = Color.Gray
        )

        // 📋 DIÁLOGO FOTO
        if (showPicker) {
            AlertDialog(
                onDismissRequest = { showPicker = false },
                title = { Text("Foto de perfil") },
                text = { Text("Selecciona una opción") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showPicker = false

                            val galleryPermission =
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    Manifest.permission.READ_MEDIA_IMAGES
                                } else {
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                }

                            when {
                                ContextCompat.checkSelfPermission(
                                    context,
                                    galleryPermission
                                ) == PackageManager.PERMISSION_GRANTED -> {
                                    galleryLauncher.launch("image/*")
                                }
                                else -> {
                                    galleryPermissionLauncher.launch(galleryPermission)
                                }
                            }
                        }
                    ) { Text("Galería") }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showPicker = false

                            val photoFile = File(
                                context.cacheDir,
                                "camera_${System.currentTimeMillis()}.jpg"
                            )

                            cameraImageUri = FileProvider.getUriForFile(
                                context,
                                "com.example.mentelibre.fileprovider",
                                photoFile
                            )

                            when {
                                ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CAMERA
                                ) == PackageManager.PERMISSION_GRANTED -> {
                                    cameraLauncher.launch(cameraImageUri!!)
                                }
                                else -> {
                                    cameraPermissionLauncher.launch(
                                        Manifest.permission.CAMERA
                                    )
                                }
                            }
                        }
                    ) { Text("Cámara") }
                }
            )
        }
    }
}
