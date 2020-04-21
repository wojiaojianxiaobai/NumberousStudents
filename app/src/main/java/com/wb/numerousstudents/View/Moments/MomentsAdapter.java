package com.wb.numerousstudents.View.Moments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.socks.library.KLog;
import com.wb.numerousstudents.R;
import com.wb.numerousstudents.Utils.BitmapCache;

import java.util.List;

public class MomentsAdapter extends RecyclerView.Adapter<MomentsAdapter.ViewHolder> {
    private List<MomentsItem> mMomentsItems;

    private ImageLoader imageLoader;
    private RequestQueue requestQueue;
    public MomentsAdapter(List<MomentsItem> list, Context context){
        mMomentsItems = list;
        requestQueue = Volley.newRequestQueue(context);
        imageLoader = new ImageLoader(requestQueue,new BitmapCache());
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
        String picturePath = momentsItem.getMomentPicturePath();
        if (picturePath.equals("null")){
//            holder.momentsItemPictureImageView.setVisibility(View.GONE);
        }else {
            String thumbnailPicturePath = picturePath.replace("image","thumbnail");
            KLog.v("wb.z MomentPicturePath: " + momentsItem.getMomentPicturePath());
            holder.momentsItemPictureImageView.setTag(position);
            holder.momentsItemPictureImageView.setImageUrl(thumbnailPicturePath,imageLoader);
        }
        holder.momentsItemTimeTextView.setText(momentsItem.getMomentTime());
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

        NetworkImageView momentsItemPictureImageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            momentsItemUserNameTextView = itemView.findViewById(R.id.moments_item_user_name);
            momentsItemTittleTextView = itemView.findViewById(R.id.moments_item_tittle);
            momentsItemContentTextView = itemView.findViewById(R.id.moments_item_content);
            momentsItemPictureImageView = itemView.findViewById(R.id.moments_item_picture);
            momentsItemTimeTextView = itemView.findViewById(R.id.moments_item_time);
        }
    }

    public synchronized void refreshData(List<MomentsItem> newMomentsItems){
//        mMomentsItems.addAll(newMomentsItems);
        notifyDataSetChanged();
        notify();
    }
}
