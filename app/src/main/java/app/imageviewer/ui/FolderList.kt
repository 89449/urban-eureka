package app.imageviewer.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import app.imageviewer.data.Folder
import app.imageviewer.data.Query

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderList(
	onFolderClick: (Long) -> Unit
) {
	val context = LocalContext.current
	var folders by remember { mutableStateOf<List<Folder>>(emptyList()) }
	
	LaunchedEffect(Unit) {
		folders = Query(context).getFolders()
	}
	
	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text("Folders") }
			)
		}
	) {
		LazyColumn(
			contentPadding = it
		) {
			items(folders) {
				ListItem(
					headlineContent = { Text(it.name) },
					modifier = Modifier
						.clickable {
							onFolderClick(it.id)
						}
				)
			}
		}
	}
}