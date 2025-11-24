package app.imageviewer.data

import android.content.Context
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class Folder(
	val id: Long,
	val name: String
)

data class Images(
	val uri: Uri,
	val name: String,
	val id: Long
)

class Query(private val context: Context) {
	
	suspend fun getFolders(): List<Folder> = withContext(Dispatchers.IO) {
		var folders = mutableSetOf<Folder>()
		val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
		val projection = arrayOf(MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID)
		val shortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
		
		
		context.contentResolver.query(
			uri,
			projection,
			null,
			null,
			shortOrder
		)?.use {
			val nameCol = it.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
			val idCol = it.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_ID)
			
			while(it.moveToNext()) {
				val name = it.getString(nameCol) ?: "Unknown"
				val id = it.getLong(idCol)
				
				folders.add(Folder(name = name, id = id))
			}
		}
		return@withContext folders.toList()
	}
	
	suspend fun getImages(folderId: Long): List<Images> = withContext(Dispatchers.IO) {
		var images = mutableSetOf<Images>()
		var uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
		val projection = arrayOf(MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media._ID)
		val shortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
		val selection = "${MediaStore.Images.Media.BUCKET_ID} = ?"
		val selectionArgs = arrayOf(folderId.toString())
		
		context.contentResolver.query(
			uri,
			projection,
			selection,
			selectionArgs,
			shortOrder
		)?.use {
		    val nameCol = it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
		    val idCol = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
		    
		    while(it.moveToNext()) {
		        val name = it.getString(nameCol)
		        val id = it.getLong(idCol)
		        val uri = Uri.withAppendedPath(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id.toString()
                )
		        images.add(Images(uri = uri, name = name, id = id))
		    }
		}
		
		return@withContext images.toList()
		
	}
	
}