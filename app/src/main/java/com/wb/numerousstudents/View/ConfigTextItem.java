package com.wb.numerousstudents.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.socks.library.KLog;
import com.wb.numerousstudents.R;

public class ConfigTextItem extends RelativeLayout {
    private static final boolean DEBUG = false;

    private TextView mTitleView;
    private TextView mSummaryView;
    private Context mContext;
    private View mView;

    private String mTitle;
    private String mSummary;

    public ConfigTextItem(Context context) {
        this(context,null);
    }

    public ConfigTextItem(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public ConfigTextItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

        TypedArray s = context.obtainStyledAttributes(attrs, R.styleable.ConfigTextItem);
        mTitle = s.getString(R.styleable.ConfigTextItem_item_title);
        mSummary = s.getString(R.styleable.ConfigTextItem_item_summary);
        if (DEBUG){
            KLog.v("wb.z :mTitle" + mTitle);
        }
        init();
        s.recycle();
    }

    private void init(){
        mView = View.inflate(mContext,R.layout.config_item,this);
        initButton();
    }

    private void initButton(){
        mTitleView = mView.findViewById(R.id.item_tittle);
        mSummaryView = mView.findViewById(R.id.item_summary);
        mTitleView.setText(mTitle);
        mSummaryView.setText(mSummary);
    }

    public void setTitle(String title){
        mTitle = title;
        mTitleView.setText(title);
    }
    public void setSummary(String summary){
        mSummary = summary;
        mSummaryView.setText(summary);
    }
}
