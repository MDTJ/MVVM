package com.mtt.lib.base_module.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import com.mtt.lib.base_module.R;


/**
 * Created by mtt on 2018/4/10.
 */

public class BaseEditText extends AppCompatEditText {
    private int maxLength;

    public BaseEditText(Context context) {
        this(context, null);
    }

    public BaseEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextStyle);
    }

    public BaseEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BaseEditText);
            maxLength = a.getInteger(R.styleable.BaseEditText_baseEditText_maxLength, 100);
            switch (a.getInt(R.styleable.BaseEditText_baseEditText_font, 0)) {
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

        InputFilter[] filters = getFilters();
        int size = 0;
        if (filters != null) {
            size = filters.length;
        }
        boolean hasFilter = false;
        for (int i = 0; i < size; i++) {
            if (filters[i] instanceof InputFilter.LengthFilter) {
                hasFilter = true;
                break;
            }
        }
        if (!hasFilter) {
            InputFilter[] newFilters = new InputFilter[size + 1];
            for (int i = 0; i < size; i++) {
                newFilters[i] = filters[i];
            }
            newFilters[size] = new InputFilter.LengthFilter(maxLength);
            setFilters(newFilters);
        }
    }


}

