package com.example.unsplashdemo.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.activity.ComponentActivity
import com.example.unsplashdemo.adapter.PhotoAdapter
import com.example.unsplashdemo.service.RetrofitClient
import com.example.unsplashdemo.model.UnsplashPhoto
import com.example.unsplashdemo.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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
        setContentView(binding.root);
        adapter = PhotoAdapter(
            this@MainActivity,
            photos
        )
        binding.gridView.adapter = adapter
        fetchPhotos();
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

    private fun fetchPhotos() {
        /* GlobalScope.launch(Dispatchers.IO) {
             val retrofit: Retrofit = Builder()
                 .baseUrl("https://api.unsplash.com/")
                 .addConverterFactory(GsonConverterFactory.create())
                 .build()
             val unsplashApi: UnsplashApi = retrofit.create(UnsplashApi::class.java)
             Log.w("msg", "page== $page")
             val call: Call<List<Photo>> = unsplashApi.getPhotos(
                 "_VJU5P1X9x0c__AyPUptOUksNVLeQrjqjnsNz9kLj8A",
                 page, perPage
             )
             call.enqueue(object : Callback<List<Photo>> {
                 override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                     if (binding.relLoading.visibility == View.VISIBLE) {
                         binding.relLoading.visibility = View.GONE
                     }
                     if (!response.isSuccessful()) {
                         Log.e("API_CALL", "Failed to fetch photos: " + response.code())
                         return
                     }
                     response.body()?.let { photos.addAll(it) };
                     withContext(Dispatchers.Main) {
                         page++;
                         adapter.notifyDataSetChanged();
                     }


                 }

                 override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                     Log.e("API_CALL", "Failed to fetch photos: " + t.message)
                 }

             })
         }*/
        isLoading = true
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val newPhotos = RetrofitClient.unsplashApi.getPhotos(
                    "_VJU5P1X9x0c__AyPUptOUksNVLeQrjqjnsNz9kLj8A", page, perPage
                )
                photos.addAll(newPhotos)
                withContext(Dispatchers.Main) {
                    if (binding.relLoading.visibility == View.VISIBLE) {
                        binding.relLoading.visibility = View.GONE
                    }
                    adapter.notifyDataSetChanged()
                    page++
                    isLoading = false
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Failed to fetch photos: ${e.message}")
                isLoading = false
            }
        }

    }

}
