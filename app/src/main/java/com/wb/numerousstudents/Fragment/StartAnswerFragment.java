package com.wb.numerousstudents.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wb.numerousstudents.Answer.AnswerActivity;
import com.wb.numerousstudents.Answer.CommonAdapter;
import com.wb.numerousstudents.Answer.DataAnswer;
import com.wb.numerousstudents.Answer.StartStudyActivity;
import com.wb.numerousstudents.Answer.Utils;
import com.wb.numerousstudents.Answer.ViewHolder;
import com.wb.numerousstudents.R;

import java.util.ArrayList;

public class StartAnswerFragment extends Fragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    private TextView tv_content;
    private ListView lv_list;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_answer,null);
        TextView startAnswer = view.findViewById(R.id.tv_begin);
        tv_content = view.findViewById(R.id.tv_content);
        lv_list = view.findViewById(R.id.lv_list);

        startAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AnswerActivity.class);
                startActivityForResult(intent, 1001);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == 1002) {
            if (data == null) {
                tv_content.setVisibility(View.GONE);
                return;
            }
            tv_content.setVisibility(View.VISIBLE);
            ArrayList<DataAnswer> list = data.getParcelableArrayListExtra("result");
            int trueNum = 0;
            int falseNum = 0;
            int noNum = 0;
            for (DataAnswer dataAnswer : list) {
                if (dataAnswer.getIs_select_postion() == dataAnswer.getIs_ture()) {
                    trueNum++;
                } else if (dataAnswer.getIs_select_postion() != 0) {
                    falseNum++;
                }else {
                    noNum++;
                }
            }
            tv_content.setText("本轮答题成绩：\n"
                    + "正确：" +trueNum  + "道\n"
                    + "错误：" + falseNum  + "道\n"
                    + "未答：" +noNum  + "道\n"
                    + "成绩：" +trueNum * 10  + "分");

            lv_list.setAdapter(new CommonAdapter<DataAnswer>(getActivity(),list,R.layout.item) {
                @Override
                public void convert(ViewHolder helper, DataAnswer item, int position) {
                    helper.setText(R.id.tv_item, Utils.getContent(item,position));
                }
            });
        }else {
            tv_content.setVisibility(View.GONE);
        }
    }
}
