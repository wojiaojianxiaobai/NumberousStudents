package com.wb.numerousstudents.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.wb.numerousstudents.R;

public class FindItem extends RelativeLayout {
    public FindItem(Context context) {
        this(context,null);
    }

    public FindItem(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public FindItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

    }

}
