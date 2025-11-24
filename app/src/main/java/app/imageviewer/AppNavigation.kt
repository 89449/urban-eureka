package app.imageviewer

import androidx.compose.runtime.Composable 
import androidx.navigation.compose.NavHost 
import androidx.navigation.compose.composable 
import androidx.navigation.compose.rememberNavController 
import androidx.compose.animation.EnterTransition 
import androidx.compose.animation.ExitTransition 

import app.imageviewer.ui.FolderList
import app.imageviewer.ui.FolderContent
import app.imageviewer.ui.Fullscreen

@Composable
fun AppNavigation() {
	val navController = rememberNavController()
	NavHost(
		startDestination = "folder_list",
		navController = navController,
		enterTransition = { EnterTransition.None },
		exitTransition = { ExitTransition.None }
	) {
		composable(route = "folder_list") {
			FolderList(
				onFolderClick = { folderId ->
					navController.navigate("folder_content/$folderId")
				}
			)
		}
		composable(route = "folder_content/{folderId}") {
			val folderId = it.arguments!!.getString("folderId")!!.toLong()
			FolderContent(
				folderId = folderId,
				onImageClick = { imageId ->
					navController.navigate("fullscreen/$imageId/$folderId")
				}
			)
		}
		composable(route = "fullscreen/{imageId}/{folderId}") {
			val imageId = it.arguments!!.getString("imageId")!!.toLong()
			val folderId = it.arguments!!.getString("folderId")!!.toLong()
			Fullscreen(
				imageId = imageId,
				folderId = folderId
			)
		}
	}
}