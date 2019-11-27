package com.yunda.lib.base_module.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.yunda.lib.base_module.R;

/**
 * Created by mtt on 2018/4/10.
 */

public class BaseTextView extends AppCompatTextView {
    public BaseTextView(Context context) {
        this(context, null);
    }

    public BaseTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public BaseTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        if (attrs == null) return;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BaseTextView);

        switch (a.getInt(R.styleable.BaseTextView_baseTextView_font, 0)) {
            case 1:
                ViewStyle.setTypeface(this, ViewStyle.FONT_PINGFANG_REGULAR);
                break;
            case 2:
                ViewStyle.setTypeface(this, ViewStyle.FONT_PINGFANG_MEDIUM);
                break;
            case 3:
                ViewStyle.setTypeface(this, ViewStyle.FONT_PINGFANG_BOLD);
                break;
        }

        a.recycle();
    }
}