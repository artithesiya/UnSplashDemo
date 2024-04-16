package com.example.unsplashdemo.activity

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
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
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private var isLoading: Boolean = false
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PhotoAdapter
    private var photos: MutableList<UnsplashPhoto> = mutableListOf()
    private var page: Int = 1
    private val perPage: Int = 15

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkAppPref()
        setAdapter()
        fetchPhotos()
        gridScrollLister()
        setReTryClick()
        handleBackPress()
    }

    private fun handleBackPress() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }

        }

        // Add the callback to the OnBackPressedDispatcher
        onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun setReTryClick() {
        binding.btnRetry.setOnClickListener {
            if (isNetworkConnected(this@MainActivity)) {
                fetchPhotos()
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "No network found, please try again later..",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
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
                    fetchPhotos()
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
            editor.putBoolean("isFirstTime", false)
        }
        val fileCache = FileCache(this@MainActivity)
        fileCache.clear()
        editor.apply()
    }

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private fun fetchPhotos() {
        isLoading = true
        if (photos.size == 0) {
            binding.progressBar.visibility = View.VISIBLE
            if (binding.relLoading.visibility == View.VISIBLE) {
                binding.relLoading.visibility = View.GONE
            }
            if (binding.relNoData.visibility == View.VISIBLE) {
                binding.relNoData.visibility = View.GONE
            }
        }
        coroutineScope.launch {
            try {
                val newPhotos = withContext(Dispatchers.IO) {
                    RetrofitClient.unsplashApi.getPhotos(
                        "_VJU5P1X9x0c__AyPUptOUksNVLeQrjqjnsNz9kLj8A", page, perPage
                    )
                }
                photos.addAll(newPhotos)
                adapter.notifyDataSetChanged()
                page++
                if (binding.relLoading.visibility == View.VISIBLE) {
                    binding.relLoading.visibility = View.GONE
                }
                binding.relNoData.visibility = View.GONE
                binding.gridView.visibility = View.VISIBLE
                if (binding.progressBar.visibility == View.VISIBLE) {
                    binding.progressBar.visibility = View.GONE
                }
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
        if (photos.size == 0) {
            binding.relNoData.visibility = View.VISIBLE
            binding.gridView.visibility = View.GONE
            binding.txtError.text = message
        }
        if (binding.progressBar.visibility == View.VISIBLE) {
            binding.progressBar.visibility = View.GONE
        }
        binding.relLoading.visibility = View.GONE
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }

    private fun isNetworkConnected(context: Context): Boolean {
        return (context.getSystemService(CONNECTIVITY_SERVICE) as
                ConnectivityManager).activeNetworkInfo != null
    }

}
