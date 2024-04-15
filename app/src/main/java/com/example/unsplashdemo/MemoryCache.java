package com.example.unsplashdemo;

import android.graphics.Bitmap;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

public class MemoryCache {
    private final Map<String, SoftReference<Bitmap>> cache=Collections.synchronizedMap(new HashMap<>());

    public Bitmap get(String id){
        if(!cache.containsKey(id)) {
            return null;
        }
        SoftReference<Bitmap> ref=cache.get(id);
        if (ref != null) {
            return ref.get();
        }
        return null;
    }

    public void put(String id, Bitmap bitmap){
        cache.put(id, new SoftReference<>(bitmap));
    }

    public void clear() {
        cache.clear();
    }
}
