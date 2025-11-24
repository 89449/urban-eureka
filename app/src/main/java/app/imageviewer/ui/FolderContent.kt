package app.imageviewer.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import app.imageviewer.data.Images
import app.imageviewer.data.Query

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderContent(
	folderId: Long,
	folderName: String,
	onImageClick: (Long) -> Unit
) {
	val context = LocalContext.current
	var images by remember { mutableStateOf<List<Images>>(emptyList()) }
	
	LaunchedEffect(Unit) {
		images = Query(context).getImages(folderId)
	}
	
	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text(folderName) }
			)
		}
	) {
		LazyVerticalGrid(
			columns = GridCells.Fixed(3),
			contentPadding = it
		) {
			items(images) {
				AsyncImage(
					model = it.uri,
					contentScale = ContentScale.Crop,
					contentDescription = null,
					modifier = Modifier
						.aspectRatio(1f)
						.clickable {
							onImageClick(it.id)
						}
				)
			}
		}
	}
	
}