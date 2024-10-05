package com.atilsamancioglu.artbookcompose.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.atilsamancioglu.artbookcompose.R
import com.atilsamancioglu.artbookcompose.model.Art

@Composable
fun AddArtScreen(saveFunction: (art: Art) -> Unit) {

    val artName = remember {
        mutableStateOf("")
    }

    val artistName = remember {
        mutableStateOf("")
    }

    val artYear = remember {
        mutableStateOf("")
    }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current


    Box(modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                ImagePicker(onImageSelected = { uri ->
                    selectedImageUri = uri
                })
                TextField(value = artName.value, placeholder = {
                    Text("Enter Art Name")
                }, onValueChange = {
                    artName.value = it
                },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent
                    ))

                TextField(value = artistName.value, placeholder = {
                    Text("Enter Artist Name")
                }, onValueChange = {
                    artistName.value = it
                },
                    colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent
                ))

                TextField(value = artYear.value, placeholder = {
                    Text("Enter Art Year")
                }, onValueChange = {
                    artYear.value = it
                },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent
                    ))

                Button(onClick = {

                    // Convert the selected URI into a ByteArray if it's not null
                    val imageByteArray = selectedImageUri?.let { uriToByteArray(context, it) } ?: ByteArray(0)

                    val artToInsert = Art(artName = artName.value,
                        artistName = artistName.value,
                        year = artYear.value,
                        image = imageByteArray)
                    saveFunction(artToInsert)
                }) {
                    Text("Save")
                }

            }
        }

}

@Composable
fun ImagePicker(onImageSelected: (Uri?) -> Unit) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    // Determine the permission based on the Android version
    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    // Launchers for gallery intent and permission request
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            galleryLauncher.launch("image/*") // Open gallery if permission is granted
        } else {
            Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display selected image if available, otherwise show default image
        selectedImageUri?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Selected Image",
                modifier = Modifier
                    .size(300.dp, 200.dp)
                    .padding(16.dp)
            )
            onImageSelected(it)  // Notify parent composable of the selected image

        } ?: Image(
            painter = painterResource(id = R.drawable.selectimage),
            contentDescription = "Select an image",
            modifier = Modifier
                .size(300.dp, 200.dp)
                .padding(16.dp)
                .clickable {
                    // Check if permission is already granted, else request permission
                    if (ContextCompat.checkSelfPermission(
                            context,
                            permission
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        galleryLauncher.launch("image/*") // Launch gallery if permission already granted
                    } else {
                        permissionLauncher.launch(permission) // Request permission
                    }
                }

        )
    }
}

fun uriToByteArray(context: Context, uri: Uri): ByteArray? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        inputStream?.readBytes() // Convert to ByteArray
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}