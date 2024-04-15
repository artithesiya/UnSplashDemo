package com.example.unsplashdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.unsplashdemo.ImageLoader;
import com.example.unsplashdemo.R;
import com.example.unsplashdemo.model.UnsplashPhoto;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PhotoAdapter extends BaseAdapter {
    private List<UnsplashPhoto> photos;
    private Context context;

    public PhotoAdapter(Context context, List<UnsplashPhoto> photos) {
        this.context = context;
        this.photos = photos;
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public Object getItem(int position) {
        return photos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(this.context).inflate(R.layout.photos_item, parent, false);
        }
        ImageView imageView=listitemView.findViewById(R.id.ivPhoto);
        ImageLoader imgLoader = new ImageLoader(this.context);
        int loader = R.mipmap.ic_launcher;

//        ImageView imageView;
//        if (convertView == null) {
//            imageView = new ImageView(context);
//            imageView.setLayoutParams(new GridView.LayoutParams(350, 350));
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        } else {
//            imageView = (ImageView) convertView;
//        }
        UnsplashPhoto photo = photos.get(position);
//        Picasso.get().load(photo.getUrls().getRegular()).into(imageView);
        imgLoader.DisplayImage(photo.getUrls().getRegular(), loader, imageView);
        return listitemView;
    }
}
