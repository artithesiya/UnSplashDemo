package com.example.unsplashdemo

import android.content.Context
import android.os.Environment
import java.io.File

class FileCache(context: Context) {
    private var cacheDir: File? = null

    init {
        //Find the dir to save cached images
        cacheDir =
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString() + "/TempImages/"
            ) else {
                File(context.cacheDir.toString() + "/")
            }
        if (!cacheDir!!.exists()) {
            cacheDir?.mkdirs()
        }
    }

    fun getFile(url: String): File {
        val randomNumber = (0..1000).random()
        val filename = url.hashCode().toString() + randomNumber
        return File(cacheDir, filename)
    }

    fun clear() {
        if (cacheDir!!.isDirectory()) {
            val children = cacheDir!!.list()
            if (children != null) {
                for (child in children) {
                    File(cacheDir, child).delete()
                }
            }
        }
    }
}
