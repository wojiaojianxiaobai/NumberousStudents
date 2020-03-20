package com.wb.numerousstudents.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import java.util.zip.Inflater;

public class SetConfigTextItem extends LinearLayout {
    public SetConfigTextItem(Context context) {
        this(context,null);
    }

    public SetConfigTextItem(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SetConfigTextItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context){

    }
}
