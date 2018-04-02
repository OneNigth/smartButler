package com.yj.smartbutler.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by yj on 2018/3/31.
 * 功能 工具类
 *      字体设置
 */

public class UtilTools {
    private static final String FONT_URL = "fonts/FONT.TTF";

    /**
     * 字体设置
     * @param context
     * @param textView
     */
    public static void SetFont(Context context, TextView textView){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),FONT_URL);
        textView.setTypeface(typeface);
    }
}
