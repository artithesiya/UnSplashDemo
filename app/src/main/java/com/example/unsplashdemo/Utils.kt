package com.example.unsplashdemo

import java.io.InputStream
import java.io.OutputStream

object Utils {
    fun copyStream(`is`: InputStream, os: OutputStream) {
        val bufferSize = 1024
        try {
            val bytes = ByteArray(bufferSize)
            while (true) {
                val count = `is`.read(bytes, 0, bufferSize)
                if (count == -1) break
                os.write(bytes, 0, count)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}
