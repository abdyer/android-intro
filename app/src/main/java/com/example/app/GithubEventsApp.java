package com.example.app;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.app.cache.LruBitmapCache;

/**
 * Created by andy on 3/12/14.
 */
public class GithubEventsApp extends Application {

    private static RequestQueue requestQueue;
    private static LruBitmapCache bitmapCache;
    private static ImageLoader imageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        bitmapCache = new LruBitmapCache(10 * 1024);
        imageLoader = new ImageLoader(requestQueue, bitmapCache);
    }

    public static RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public static ImageLoader getImageLoader() { return imageLoader; }
}
