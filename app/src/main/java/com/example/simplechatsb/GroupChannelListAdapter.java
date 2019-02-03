package com.example.simplechatsb;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sendbird.android.GroupChannel;

import java.util.List;

public class GroupChannelListAdapter extends RecyclerView.Adapter<GroupChannelListAdapter.GroupChannelListViewHolder> {
    private List<GroupChannel> mList;

    public static class GroupChannelListViewHolder extends RecyclerView.ViewHolder {
        public TextView groupChannelNameTextView;
        public Button groupChannelButton;
        public GroupChannelListViewHolder(LinearLayout v) {
            super(v);
            groupChannelNameTextView = itemView.findViewById(R.id.group_channel_url_text_view);
            groupChannelButton = itemView.findViewById(R.id.join_group_channel_button);
        }
    }

    public GroupChannelListAdapter(List<GroupChannel> list) {
        mList = list;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public GroupChannelListViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_channel_item, parent, false);
        GroupChannelListViewHolder vh = new GroupChannelListViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(GroupChannelListViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.groupChannelNameTextView.setText(mList.get(position).getUrl());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mList.size();
    }
}