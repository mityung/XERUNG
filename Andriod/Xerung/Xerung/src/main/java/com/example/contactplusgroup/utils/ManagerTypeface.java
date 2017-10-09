package com.example.contactplusgroup.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.SparseArray;

/**
 * ManagerTypeface Set the Font
 * @author micat
 *
 */
public class ManagerTypeface
{
    private static final SparseArray<Typeface> typefacesCache = new SparseArray<>();

    public static Typeface getTypeface(Context context, int typeface)
    {
        synchronized(typefacesCache)
        {
            if(typefacesCache.indexOfKey(typeface) < 0)
            {
                typefacesCache.put(typeface, FactoryTypeface.createTypeface(context, typeface));
            }

            return typefacesCache.get(typeface);
        }
    }
}
