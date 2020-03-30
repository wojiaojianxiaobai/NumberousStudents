package com.wb.numerousstudents.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.socks.library.KLog;
import com.wb.numerousstudents.Activity.MomentsDetailedActivity;
import com.wb.numerousstudents.R;
import com.wb.numerousstudents.Utils.DebugUtil;
import com.wb.numerousstudents.Utils.MyOKhttpUtil;
import com.wb.numerousstudents.Utils.RecyclerViewSpacesItemDecoration;
import com.wb.numerousstudents.View.Moments.MomentsAdapter;
import com.wb.numerousstudents.View.Moments.MomentsItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;

public class MomentsFragment extends Fragment {
    private static boolean DEBUG = true;
    private List<MomentsItem> mMomentsItemList = new ArrayList<>();
    private RecyclerView mMomentsRecyclerView;
    private ImageView addNewMoment;
    private SwipeRefreshLayout mRefreshMomentsLayout;

    private static final int REFRESH_MOMENTS = 1;
    private static final int REFRESH_FAIL = 2;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int code = msg.arg1;
            switch (code){
                case REFRESH_MOMENTS:{
                    mMomentsAdapter.refreshData(mMomentsItemList);
                    mRefreshMomentsLayout.setRefreshing(false);
                    break;
                }
                case REFRESH_FAIL:{
                    mRefreshMomentsLayout.setRefreshing(false);
                    Toast.makeText(getActivity(),"网络繁忙",Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        DebugUtil.debug("onAttach");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moments,null);
        initMoments(view);
        DebugUtil.debug("onCreateView");
        return view;
    }

    private void initMoments(View view){
        initDate();
        initView(view);
    }

    private void initDate(){
        String uri = "http://175.24.23.24:8080/readMoment";
        FormBody.Builder formBody = new FormBody.Builder();
        MyOKhttpUtil.getInstance().get(uri,formBody);
        MyOKhttpUtil.getInstance().setMyOKHttpUtilListener(new MyOKhttpUtil.ResponseListener() {
            @Override
            public void findOnSuccess(String response) {
                DebugUtil.debug(response);
                if (!response.equals("")){
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        mMomentsItemList.clear();
                        for (int i = 0; i < jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            MomentsItem momentsItem = new MomentsItem();
                            momentsItem.setMomentId(jsonObject.getString("mMomentId"));
                            momentsItem.setUserName(jsonObject.getString("mUserName"));
                            momentsItem.setMomentTittle(jsonObject.getString("mMomentTitle"));
                            momentsItem.setContent(jsonObject.getString("mMomentContent"));
                            momentsItem.setmMomentNickName(jsonObject.getString("mUserNickName"));
                            momentsItem.setmMomentPicturePath(jsonObject.getString("mMomentPicturePath"));
                            momentsItem.setMomentTime(jsonObject.getString("mMomentTime"));
                            mMomentsItemList.add(momentsItem);


                        }
                        Message message = handler.obtainMessage();
                        message.arg1 = REFRESH_MOMENTS;
                        handler.sendMessage(message);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void findOnFail(String response) {
                DebugUtil.debug("网络错误");
                Message message = handler.obtainMessage();
                message.arg1 = REFRESH_FAIL;
                handler.sendMessage(message);
            }
        });


    }
    private MomentsAdapter mMomentsAdapter;
    private void initView(View view){
        mRefreshMomentsLayout = view.findViewById(R.id.refresh_moments_swipe_refresh_layout);
        mRefreshMomentsLayout.setColorSchemeResources(R.color.blue);
        mRefreshMomentsLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initDate();
            }
        });
        mMomentsRecyclerView = view.findViewById(R.id.moments_content_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mMomentsRecyclerView.setLayoutManager(linearLayoutManager);
        mMomentsRecyclerView.addItemDecoration(new RecyclerViewSpacesItemDecoration(3));
        mMomentsAdapter = new MomentsAdapter(mMomentsItemList,getContext());
        mMomentsRecyclerView.setAdapter(mMomentsAdapter);
        if (DEBUG)
        KLog.v("wb.z :" + mMomentsItemList.toString());
        addNewMoment = view.findViewById(R.id.iv_moments_add_new_moment);
        addNewMoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MomentsDetailedActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DebugUtil.debug("onCreateMomentsFragment");
    }

}
