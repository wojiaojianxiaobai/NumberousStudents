package com.wb.numerousstudents.View.Moments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.socks.library.KLog;
import com.wb.numerousstudents.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MomentsAdapter extends RecyclerView.Adapter<MomentsAdapter.ViewHolder> {
    private List<MomentsItem> mMomentsItems;
    public MomentsAdapter(List<MomentsItem> list){
        mMomentsItems = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.moments_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MomentsItem momentsItem = mMomentsItems.get(position);
        holder.momentsItemUserNameTextView.setText(momentsItem.getUserName());
        holder.momentsItemTittleTextView.setText(momentsItem.getMomentTittle());
        holder.momentsItemContentTextView.setText(momentsItem.getContent());
//        holder.momentsItemPictureImageView.setImageURI("缩略图地址");
        holder.momentsItemPictureImageView.setImageResource(R.drawable.time2);
//        holder.momentsItemTimeTextView.setText(momentsItem.getMomentTime());
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日\nHH时mm分ss秒");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日\nHH:mm");
        Date date = new Date();
        String dateString = dateFormat.format(date);
        holder.momentsItemTimeTextView.setText(dateString);
    }

    @Override
    public int getItemCount() {
        return mMomentsItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView momentsItemUserNameTextView;
        TextView momentsItemTittleTextView;
        TextView momentsItemContentTextView;
        TextView momentsItemTimeTextView;

        ImageView momentsItemPictureImageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            momentsItemUserNameTextView = itemView.findViewById(R.id.moments_item_user_name);
            momentsItemTittleTextView = itemView.findViewById(R.id.moments_item_tittle);
            momentsItemContentTextView = itemView.findViewById(R.id.moments_item_content);
            momentsItemPictureImageView = itemView.findViewById(R.id.moments_item_picture);
            momentsItemTimeTextView = itemView.findViewById(R.id.moments_item_time);
        }
    }
}
