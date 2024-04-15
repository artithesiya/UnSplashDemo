package com.example.unsplashdemo.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.example.unsplashdemo.ImageLoader
import com.example.unsplashdemo.R
import com.example.unsplashdemo.model.UnsplashPhoto
import com.squareup.picasso.Picasso

class PhotoAdapter(private val context: Context, private val photos: List<UnsplashPhoto>) :
    BaseAdapter() {
    override fun getCount(): Int {
        return photos.size
    }

    override fun getItem(position: Int): UnsplashPhoto {
        return photos[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private class ViewHolder {
        lateinit var ivPhoto: ImageView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listitemView = convertView
        val holder: ViewHolder
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(context).inflate(R.layout.photos_item, parent, false)
            holder = ViewHolder()
            holder.ivPhoto = listitemView.findViewById(R.id.ivPhoto)
            listitemView.tag = holder

        } else {
            holder = listitemView.tag as ViewHolder
        }
//        val imageView = listitemView.findViewById<ImageView>(R.id.ivPhoto)
        val imgLoader = ImageLoader(context)
        val loader = R.drawable.loading
        var photo = getItem(position)
//        val (_, urls) = photos[position]
        Log.w("msg", "pos-- " + position + "-getUrls-- " + photo.urls.regular);
        imgLoader.DisplayImage(photo.urls.regular, loader, holder.ivPhoto)
//        Picasso.get().load(photo.urls.regular).placeholder(loader).into(holder.ivPhoto)
        return listitemView!!
    }
}
