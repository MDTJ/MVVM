package com.mtt.lib.base_module.widget;

import android.graphics.Typeface;
import android.util.SparseArray;
import android.widget.TextView;

/**
 * Created by mtt on 2018/4/10.
 */

public class ViewStyle {
    public static final int FONT_DEFAULT = 0;
    public static final int FONT_PINGFANG_REGULAR = 1;
    public static final int FONT_PINGFANG_MEDIUM = 2;
    public static final int FONT_PINGFANG_BOLD = 3;
    public static Typeface m_typefaces = null;
    public static Typeface b_typefaces = null;

    private static SparseArray<Typeface> fonts = new SparseArray<>(2);

    public static Typeface getTypeface(int type) {
        if (type == FONT_DEFAULT) {
            return Typeface.DEFAULT;
        }
        Typeface font = fonts.get(type);
        if (font == null) {

        }
        return font != null ? font : Typeface.DEFAULT;
    }

    public static Typeface getPFR() {
        return getTypeface(FONT_PINGFANG_REGULAR);
    }

    public static Typeface getFPM() {
        return getTypeface(FONT_PINGFANG_MEDIUM);
    }

    public static void setTypeface(TextView tv, int typeface) {
//        if (typeface == FONT_PINGFANG_MEDIUM) {
//            if(m_typefaces==null){
//                m_typefaces = Typeface.createFromAsset(tv.getContext().getAssets(), "PingFangMedium.ttf");
//            }
//            tv.setTypeface(m_typefaces);
//
//        } else if (typeface == FONT_PINGFANG_BOLD) {
//            if(b_typefaces==null){
//                b_typefaces= Typeface.createFromAsset(tv.getContext().getAssets(), "PingFangBold.ttf");
//            }
//            tv.setTypeface(b_typefaces);
//        }

    }
}
