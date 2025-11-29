package app.imageviewer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Color
import coil.compose.AsyncImage
import app.imageviewer.data.Images
import app.imageviewer.data.Query
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Fullscreen(
	imageId: Long,
	folderId: Long
) {
	val context = LocalContext.current
	var images by remember { mutableStateOf<List<Images>>(emptyList()) }
	
	LaunchedEffect(Unit) {
		images = Query(context).getImages(folderId)
	}
	
	var isToolbarVisible by remember { mutableStateOf(true) }
	
	Box{
		if(images.isNotEmpty()) {
			val startingIndex = images.indexOfFirst { it.id == imageId }
	        val pagerState = rememberPagerState( initialPage = if (startingIndex == -1) 0 else startingIndex ) {
	            images.size
	        }
	        val currentItem = images[pagerState.currentPage]
	        
	        HorizontalPager(state = pagerState) { page ->
	        	val item = images[page]
	        	val zoomState = rememberZoomState(maxScale = 15f)
	        	
	        	LaunchedEffect(zoomState.scale) {
    	            isToolbarVisible = zoomState.scale <= 1.0f
    	        }
    	        
                AsyncImage(
                    model = item.uri,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .zoomable(zoomState = zoomState, onTap = { isToolbarVisible = !isToolbarVisible} ),
                    contentScale = ContentScale.Fit
                )
	        }
			if(isToolbarVisible) {
				TopAppBar(
					title = { Text("${currentItem.name}") },
					modifier = Modifier.align(Alignment.TopCenter)
				)
			}
		}
	}
}