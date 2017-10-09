package com.example.contactplusgroup.utils;

import android.content.Context;
import android.graphics.Typeface;
/**
 * FactoryTypeface declare the static method to set the font type
 * @author micat
 *
 */

public class FactoryTypeface
{
    public static Typeface createTypeface(Context context, int typeface)
    {
        return Typeface.createFromAsset(context.getAssets(), String.format("font/%s.ttf", context.getString(typeface)));
    }
}
