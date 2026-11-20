package com.example.mycycletest

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.sp
import mycycletest.app.shared.generated.resources.Res
import org.jetbrains.compose.resources.decodeToImageBitmap

@Composable
fun rememberImageBitmap(assetPath: String?): ImageBitmap? {
    var bitmap by remember(assetPath) { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(assetPath) {
        bitmap = null
        if (!assetPath.isNullOrBlank()) {
            try {
                val fileName = assetPath.substringAfterLast("/")
                val bytes = Res.readBytes("drawable/$fileName")
                bitmap = bytes.decodeToImageBitmap()
            } catch (_: Throwable) {
                bitmap = null
            }
        }
    }

    return bitmap
}

@Composable
fun AssetImage(
    assetPath: String?,
    contentDescription: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit
) {
    val bitmap = rememberImageBitmap(assetPath)
    if (bitmap != null) {
        Image(
            painter = remember(bitmap) { BitmapPainter(bitmap) },
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale
        )
    } else {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text("No preview", color = Color.Gray, fontSize = 12.sp)
        }
    }
}
