package com.example.user.facebookalbums;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by User on 01/11/2017.
 */

public class AlbumImageAdapter extends BaseAdapter {
    private final static String TAG="AlbumImageAdapter";
    private ArrayList<String> photos;
    private Context context;
    private int albumGalleryItemBackground;
    public AlbumImageAdapter(Context context,ArrayList<String> photos){
        this.context=context;
        this.photos=photos;
        TypedArray ta=context.obtainStyledAttributes(R.styleable.AlbumGallery);
        albumGalleryItemBackground=ta.getResourceId(R.styleable.AlbumGallery_android_galleryItemBackground,0);
        ta.recycle();
    }
    @Override
    public int getCount(){
        return(this.photos.size());
    }
    @Override
    public Object getItem(int position){
        return(this.photos.get(position));
    }
    @Override
    public long getItemId(int position){
        return(position);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ImageView imageView=new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setBackgroundResource(albumGalleryItemBackground);
        imageView.setLayoutParams(new Gallery.LayoutParams(300, 300));
        imageView.setTag(photos.get(position));

        //load the image
        String pictureUrl=this.photos.get(position);

        new ImageDownloaderTask(imageView).execute(pictureUrl);
        return(imageView);
    }
}
