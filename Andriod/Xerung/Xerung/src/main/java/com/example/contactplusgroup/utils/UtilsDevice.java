package com.example.contactplusgroup.utils;

import android.content.Context;
import android.util.DisplayMetrics;
/**
 * UtilsDevice
 * @author micat
 *
 */
public class UtilsDevice
{
    /**
     * Returns the screen width in pixels
     *
     * @param context is the context to get the resources
     *
     * @return the screen width in pixels
     */
    public static int getScreenWidth(Context context)
    {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();

        return metrics.widthPixels;
    }
}
