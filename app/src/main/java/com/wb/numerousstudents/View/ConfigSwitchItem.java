package com.wb.numerousstudents.View;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.socks.library.KLog;
import com.wb.numerousstudents.Config.Config;
import com.wb.numerousstudents.R;

public class ConfigSwitchItem extends RelativeLayout {
    private static final boolean DEBUG = false;

    private TextView mTitleView;
    private Switch mSummaryView;
    private Context mContext;
    private View mView;

    private String mTitle;
    private boolean mSummary;

    public ConfigSwitchItem(Context context) {
        this(context,null);
    }

    public ConfigSwitchItem(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public ConfigSwitchItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

        TypedArray s = context.obtainStyledAttributes(attrs, R.styleable.ConfigSwitchItem);
        mTitle = s.getString(R.styleable.ConfigSwitchItem_item_switch_title);
//        mSummary = s.getBoolean(R.styleable.ConfigSwitchItem_item_switch_summary,false);
        mSummary = Config.getInstance().isProcessServer();
        if (DEBUG){
            KLog.v("wb.z :mTitle" + mTitle);
        }
        init();
        s.recycle();
    }

    private void init(){
        mView = View.inflate(mContext,R.layout.config_switch_item,this);
        initButton();
    }

    private void initButton(){
        mTitleView = mView.findViewById(R.id.item_tittle);
        mSummaryView = mView.findViewById(R.id.item_summary);
        mTitleView.setText(mTitle);
        mSummaryView.setChecked(mSummary);
    }

    public void setTitle(String title){
        mTitle = title;
        mTitleView.setText(title);
    }
    public void setSummary(boolean summary){
        mSummary = summary;
        mSummaryView.setSelected(summary);
        Config.getInstance().setProcessServerState(summary);
    }

    public boolean getSummary(){
        return mSummary;
    }

    public void changeState(){
        KLog.v("changeState");
        if (mSummary){
            mSummaryView.setChecked(false);
            mSummary = false;
        }else {
            mSummaryView.setChecked(true);
            mSummary = true;
        }
        Config.getInstance().setProcessServerState(mSummary);
    }
}
