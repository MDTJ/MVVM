package com.yunda.lib.base_module.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * Created by mtt on 2018/4/10.
 */

public class BaseImageView extends AppCompatImageView {
    public BaseImageView(Context context) {
        this(context, null);
    }

    public BaseImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
