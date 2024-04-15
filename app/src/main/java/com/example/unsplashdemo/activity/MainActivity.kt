package com.example.unsplashdemo.activity

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.activity.ComponentActivity
import com.example.unsplashdemo.FileCache
import com.example.unsplashdemo.adapter.PhotoAdapter
import com.example.unsplashdemo.databinding.ActivityMainBinding
import com.example.unsplashdemo.model.UnsplashPhoto
import com.example.unsplashdemo.service.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.File
import java.io.IOException

class MainActivity : ComponentActivity() {
    private var isLoading: Boolean = false
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PhotoAdapter
    private var photos: MutableList<UnsplashPhoto> = mutableListOf()
    private var page: Int = 1
    private val perPage: Int = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkAppPref()
        setAdapter()
        fetchPhotos()
        gridScrollLister()
    }

    private fun gridScrollLister() {
        binding.gridView.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScrollStateChanged(p0: AbsListView?, p1: Int) {
            }

            override fun onScroll(
                view: AbsListView?,
                firstVisibleItem: Int,
                visibleItemCount: Int,
                totalItemCount: Int
            ) {
                if (firstVisibleItem + visibleItemCount >= totalItemCount && !isLoading) {
                    // Load more photos when reaching the end of the grid
                    binding.relLoading.visibility = View.VISIBLE
                    fetchPhotos();
                }

            }

        })
    }

    private fun setAdapter() {
        adapter = PhotoAdapter(
            this@MainActivity,
            photos
        )
        binding.gridView.adapter = adapter
    }

    private fun checkAppPref() {
        val sharedPref = getPreferences(MODE_PRIVATE)
        val editor = sharedPref.edit()
        if (!sharedPref.getBoolean("isFirstTime", false)) {
            editor.putBoolean(
                "isFirstTime",
                true
            )

        } else {
            editor.putBoolean("isFirstTime", false);
        }
//        var fileCache = FileCache(this@MainActivity)
        val folder = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "/TempImages"
        )
        Log.w("msg", "main_check-- " + folder.exists())
        Log.w("msg", "main_check-- " + (folder.listFiles()?.size ?: 0))
        if (folder.exists()) {
            folder.deleteRecursively()
        }

//        fileCache.clear()
        editor.apply()
    }

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private fun fetchPhotos() {
        isLoading = true
        coroutineScope.launch {
            try {
                val newPhotos = withContext(Dispatchers.IO) {
                    RetrofitClient.unsplashApi.getPhotos(
                        "_VJU5P1X9x0c__AyPUptOUksNVLeQrjqjnsNz9kLj8A", page, perPage
                    )
                }
                photos.addAll(newPhotos)
                if (binding.relLoading.visibility == View.VISIBLE) {
                    binding.relLoading.visibility = View.GONE
                }
                binding.relNoData.visibility = View.GONE
                binding.gridView.visibility = View.VISIBLE
                adapter.notifyDataSetChanged()
                page++
            } catch (e: IOException) {
                Log.e("MainActivity", "IO exception while fetching photos: ${e.message}")
                showError("Failed to fetch photos. Please try again.")
            } catch (e: HttpException) {
                Log.e("MainActivity", "HTTP exception while fetching photos: ${e.message}")
                showError("Failed to fetch photos. Please try again.")
            } catch (e: Exception) {
                Log.e("MainActivity", "Failed to fetch photos: ${e.message}")
                showError("Failed to fetch photos. Please try again.")
            } finally {
                isLoading = false
            }
        }
    }

    private fun showError(message: String) {
        binding.relNoData.visibility = View.VISIBLE
        binding.gridView.visibility = View.GONE
        binding.txtError.text = message
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}
