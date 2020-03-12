package com.wb.numerousstudents.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.socks.library.KLog;
import com.wb.numerousstudents.R;
import com.wb.numerousstudents.Utils.RecyclerViewSpacesItemDecoration;
import com.wb.numerousstudents.View.Moments.MomentsAdapter;
import com.wb.numerousstudents.View.Moments.MomentsItem;

import java.util.ArrayList;
import java.util.List;

public class MomentsFragment extends Fragment {
    private static boolean DEBUG = true;
    private List<MomentsItem> mMomentsItemList = new ArrayList<>();
    private RecyclerView mMomentsRecyclerView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moments,null);
        initMoments(view);
        return view;
    }

    private void initMoments(View view){
        initDate();
        initView(view);
    }

    private void initDate(){
        mMomentsItemList.clear();
        for (int i = 0;i < 10;i++){
            MomentsItem momentsItem = new MomentsItem("测试" + i,i);
            mMomentsItemList.add(momentsItem);
        }
    }

    private void initView(View view){
        mMomentsRecyclerView = view.findViewById(R.id.moments_content_RecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mMomentsRecyclerView.setLayoutManager(linearLayoutManager);
        mMomentsRecyclerView.addItemDecoration(new RecyclerViewSpacesItemDecoration(3));
        MomentsAdapter momentsAdapter = new MomentsAdapter(mMomentsItemList);
        mMomentsRecyclerView.setAdapter(momentsAdapter);
        if (DEBUG)
        KLog.v("wb.z :" + mMomentsItemList.toString());
    }
}
