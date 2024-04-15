package com.example.unsplashdemo

import android.app.Activity
import android.content.Context
import android.os.Environment
import java.io.File

class FileCache(context: Context) {
    private var cacheDir: File? = null

    init {
        //Find the dir to save cached images
        val sharedPref = (context as Activity).getPreferences(Context.MODE_PRIVATE)
        cacheDir =
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString() + "/TempImages/"
            ) else File(context.getCacheDir().toString() + "/")
        //        if (sharedPref.getBoolean("isFirstTime", true)) {
//            //first time user
//            if (new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/TempImages/").exists()) {
//                clear();
//            }
//        }
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
        //        File[] files = cacheDir.listFiles();
//        assert files != null;
//
//        if (files == null)
//            return;
//        for (File f : files)
//            f.delete();
    }
}
