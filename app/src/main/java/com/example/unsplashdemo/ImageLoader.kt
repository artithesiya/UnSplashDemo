package com.example.unsplashdemo

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.widget.ImageView
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Files
import java.util.Collections
import java.util.WeakHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ImageLoader(context: Context?) {
    var memoryCache = MemoryCache()
    private var fileCache: FileCache
    private val imageViews = Collections.synchronizedMap(WeakHashMap<ImageView, String>())
    private var executorService: ExecutorService
    var stubId = R.drawable.loading

    init {
        fileCache = FileCache(context!!)
        executorService = Executors.newFixedThreadPool(5)
    }

    fun displayImage(url: String, loader: Int, imageView: ImageView) {
        loader.also { stubId = it }
        imageViews[imageView] = url
        val bitmap = memoryCache[url]
        if (bitmap != null) imageView.setImageBitmap(bitmap) else {
            queuePhoto(url, imageView)
            imageView.setImageResource(loader)
        }
    }

    private fun queuePhoto(url: String, imageView: ImageView) {
        val p = PhotoToLoad(url, imageView)
        executorService.submit(PhotosLoader(p))
    }

    private fun getBitmap(url: String): Bitmap? {
        val f = fileCache.getFile(url)
        //from SD cache
        val b = decodeFile(f)
        return b
            ?: try {
                val imageUrl = URL(url)
                val conn = imageUrl.openConnection() as HttpURLConnection
                conn.setConnectTimeout(30000)
                conn.setReadTimeout(30000)
                conn.instanceFollowRedirects = true
                val `is` = conn.inputStream
                val os: OutputStream? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Files.newOutputStream(f.toPath())
                } else {
                    FileOutputStream(f)
                }
                os?.let { Utils.copyStream(`is`, it) }
                os?.close()
                val bitmap: Bitmap? = decodeFile(f)
                bitmap
            } catch (ex: Exception) {
                ex.printStackTrace()
                null
            }
    }

    private fun decodeFile(f: File): Bitmap? {
        try {
            //decode image size
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            BitmapFactory.decodeStream(FileInputStream(f), null, o)
            //Find the correct scale value. It should be the power of 2.
            val requiredSize = 250
            var widthTmp = o.outWidth
            var heightTmp = o.outHeight
            var scale = 1
            while (true) {
                if (widthTmp / 2 < requiredSize || heightTmp / 2 < requiredSize) break
                widthTmp /= 2
                heightTmp /= 2
                scale *= 2
            }

            //decode with inSampleSize
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            return BitmapFactory.decodeStream(FileInputStream(f), null, o2)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    //Task for the queue
    class PhotoToLoad(var url: String, var imageView: ImageView)
    internal inner class PhotosLoader(private var photoToLoad: PhotoToLoad) : Runnable {
        override fun run() {
            if (imageViewReused(photoToLoad)) return
            val bmp = getBitmap(photoToLoad.url)
            bmp?.let { memoryCache.put(photoToLoad.url, it) }
            if (imageViewReused(photoToLoad)) return
            val bd = BitmapDisplayed(bmp, photoToLoad)
            val a = photoToLoad.imageView.context as Activity
            a.runOnUiThread(bd)
        }
    }

    fun imageViewReused(photoToLoad: PhotoToLoad): Boolean {
        val tag = imageViews[photoToLoad.imageView]
        return tag == null || tag != photoToLoad.url
    }

    //Used to display bitmap in the UI thread
    internal inner class BitmapDisplayed(private var bitmap: Bitmap?, private var photoToLoad: PhotoToLoad) :
        Runnable {
        override fun run() {
            Log.w("msg", "bitmap== $bitmap")
            if (imageViewReused(photoToLoad)) return
            if (bitmap != null) photoToLoad.imageView.setImageBitmap(bitmap) else photoToLoad.imageView.setImageResource(
                stubId
            )
        }
    }

    fun clearCache() {
        memoryCache.clear()
        fileCache.clear()
    }
}