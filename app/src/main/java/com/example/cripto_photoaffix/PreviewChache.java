package com.example.cripto_photoaffix;

import android.graphics.Bitmap;
import android.util.LruCache;
import com.example.cripto_photoaffix.Gallery.Media;

public class PreviewChache {
    private LruCache<Media, Bitmap> cache;
    private static PreviewChache instance;

    private PreviewChache() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory())/1024;
        int cacheMemory = maxMemory/10;
        cache = new LruCache<Media, Bitmap>(cacheMemory);
    }

    public static PreviewChache getInstance() {
        if (instance == null)
            instance = new PreviewChache();

        return instance;
    }

    public synchronized void addPreview(Media media, Bitmap bitmap) {
        if (cache.get(media) != null || cache.get(media) != bitmap)
            cache.put(media, bitmap);
    }

    public Bitmap getPreview(Media media) {
        return cache.get(media);
    }
}
