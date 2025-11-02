package com.example.quickchat.ui.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

object FileUtils {
    const val PICTURES = "Pictures"
    const val DOCUMENTS = "Documents"

    fun openFile(
        context: Context,
        uri: Uri,
    ) {
        val intent =
            Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, getMimeType(uri))
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "No app found to open this file", Toast.LENGTH_SHORT).show()
        }
    }

    // Get MIME type from URI
    fun getMimeType(uri: Uri): String {
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(
            MimeTypeMap.getFileExtensionFromUrl(uri.toString()),
        ) ?: "application/octet-stream"
    }

    // Helper function to check if a file is an image
    fun isImageFile(
        context: Context,
        uri: Uri,
    ): Boolean {
        val contentResolver = context.contentResolver
        val mimeType =
            MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri))
        return mimeType != null &&
            listOf(
                "jpg",
                "jpeg",
                "png",
                "gif",
                "webp",
            ).contains(mimeType.lowercase())
    }

    fun createFilesDirectory(
        context: Context,
        directoryName: String,
    ): String {
        val folder = context.filesDir
        val dir = File(folder, directoryName)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir.absolutePath
    }

    fun createFileFromUri(
        context: Context,
        directoryName: String,
        uri: Uri,
    ): File? {
        val dir = File(directoryName)
        val fileName = getFileName(context, uri)
        val destinationFile = fileName?.let { File(dir, it) }

        destinationFile?.let {
            context.contentResolver.openInputStream(uri)?.use { input ->
                it.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }

        Log.d("FileUtils", "File created at: ${destinationFile?.path}")
        return destinationFile
    }

    fun getFileName(
        context: Context,
        uri: Uri,
    ): String {
        var name: String? = null
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index != -1) {
                    name = cursor.getString(index)
                }
            }
        }
        return name ?: "temp_file"
    }

    fun getUriFromFile(
        context: Context,
        file: File,
    ): Uri? {
        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    }

    fun deleteFileFromUri(uri: Uri) {
        val file = uri.path?.let { File(it) }
        file?.let {
            if (it.exists()) {
                it.delete()
            }
        }
    }

    suspend fun saveFileToStorage(
        fileUrl: String,
        fileName: String,
        directoryName: String,
    ): String? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(fileUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.doInput = true
                connection.connect()

                val inputStream = connection.inputStream

                // Step 1: Determine Save Directory
                val directory = File(directoryName)
                if (!directory.exists()) directory.mkdirs()

                // Step 2: Create a File to Save
                val file = File(directory, fileName)
                val outputStream = FileOutputStream(file)

                // Step 3: Write Data
                inputStream.use { input ->
                    outputStream.use { output ->
                        input.copyTo(output)
                    }
                }

                Log.d("FileUtils", "File saved: ${file.absolutePath}")
                return@withContext file.absolutePath
            } catch (e: Exception) {
                Log.e("FileUtils", "Error saving file", e)
                return@withContext null
            }
        }
    }
}