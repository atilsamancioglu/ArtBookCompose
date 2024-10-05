package com.atilsamancioglu.artbookcompose.screens

import android.Manifest
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.atilsamancioglu.artbookcompose.R
import com.atilsamancioglu.artbookcompose.model.Art

@Composable
fun DetailScreen(
    art: Art?,
    deleteFunction : () -> Unit
    ) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                text = art?.artName ?: "",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(2.dp),
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            val imageBitmap = art?.image?.let { imageData ->
                BitmapFactory.decodeByteArray(imageData, 0, imageData.size)?.asImageBitmap()
            }

            Image(
                bitmap = imageBitmap ?: ImageBitmap.imageResource(id = R.drawable.selectimage),
                contentDescription = art?.artName ?: "Select an image",
                modifier = Modifier
                    .padding(16.dp)
                    .size(300.dp, 200.dp)
            )

            Text(
                text = art?.artistName ?: "",
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.padding(2.dp),
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center

            )

            Text(
                text = art?.year ?: "",
                style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.padding(2.dp),
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center

            )

            Button(onClick = {
                deleteFunction()
            }) {
                Text(text = "Delete")
            }

        }
    }
    }

