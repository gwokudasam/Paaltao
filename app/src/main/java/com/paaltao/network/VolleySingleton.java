package com.paaltao.network;

import android.graphics.Bitmap;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.paaltao.classes.Paaltao;

/**
 * Created by Arindam on 07-Feb-15.
 */
public class VolleySingleton {
    private static VolleySingleton sInstance = null;
    private RequestQueue mRequestQueue;
    private ImageLoader imageLoader;
    private VolleySingleton(){
        mRequestQueue = Volley.newRequestQueue(Paaltao.getAppContext());
        imageLoader = new ImageLoader(mRequestQueue,new ImageLoader.ImageCache() {
            private android.util.LruCache<String,Bitmap> cache = new android.util.LruCache<>((int)(Runtime.getRuntime().maxMemory()/1024/8));
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url,bitmap);
            }
        });

    }
    public static VolleySingleton getsInstance(){
        if (sInstance == null){
            sInstance = new VolleySingleton();
        }
        return  sInstance;

    }

    public RequestQueue getRequestQueue(){
        return mRequestQueue;
    }

    public ImageLoader getImageLoader(){
        return imageLoader;
    }
}
