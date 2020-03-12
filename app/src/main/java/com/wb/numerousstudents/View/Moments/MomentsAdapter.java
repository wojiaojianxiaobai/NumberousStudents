package com.wb.numerousstudents.View.Moments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wb.numerousstudents.R;

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
        holder.userNameTextView.setText(momentsItem.getName());
    }

    @Override
    public int getItemCount() {
        return mMomentsItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTextView = itemView.findViewById(R.id.moments_item_user_name);

        }
    }
}
