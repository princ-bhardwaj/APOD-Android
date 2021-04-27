package com.example.apod.utils

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.URL

object ImageStorage {

    val POD_FILENAME = "apod.jpg"
    val POD_DIRECTORY = "apod"

    fun saveToSdCard(context: Context, urlString: String, filename: String) : MutableLiveData<Uri?> {
        var saveImageUri = MutableLiveData<Uri?>()

        // async task to get / download bitmap from url
        val result: Deferred<Bitmap?> = GlobalScope.async {
            URL(urlString).toBitmap()
        }

        GlobalScope.launch(Dispatchers.Main) {
            // get the downloaded bitmap
            val bitmap: Bitmap? = result.await()
            // if downloaded then saved it to internal storage
            bitmap?.apply {
                // get saved bitmap internal storage uri
                val savedUri: Uri? = saveToInternalStorage(context, filename)
                saveImageUri.value = savedUri
            }
        }
        return  saveImageUri
    }

    // extension function to get / download bitmap from url
    private fun URL.toBitmap(): Bitmap? {
        return try {
            BitmapFactory.decodeStream(openStream())
        } catch (e: IOException) {
            null
        }
    }


    // extension function to save an image to internal storage
    private fun Bitmap.saveToInternalStorage(context: Context, filename: String): Uri? {
        // get the context wrapper instance
        val wrapper = ContextWrapper(context)

        // initializing a new file
        // bellow line return a directory in internal storage
        var file = wrapper.getDir(POD_DIRECTORY, Context.MODE_PRIVATE)

        // create a file to save the image
        file = File(file, filename)

        return try {
            // get the file output stream
            val stream: OutputStream = FileOutputStream(file)

            // compress bitmap
            compress(Bitmap.CompressFormat.JPEG, 100, stream)

            // flush the stream
            stream.flush()

            // close stream
            stream.close()

            // return the saved image uri
            Uri.parse(file.absolutePath)
        } catch (e: IOException) { // catch the exception
            e.printStackTrace()
            null
        }
    }


    fun getImageUri(context: Context?, fileName: String): Uri {
        // get the context wrapper instance
        val wrapper = ContextWrapper(context)

        // initializing a new file
        // bellow line return a directory in internal storage
        var file = wrapper.getDir(POD_DIRECTORY, Context.MODE_PRIVATE)

        // create a file to save the image
        file = File(file, fileName)

        return Uri.parse(file.absolutePath)
    }

}

