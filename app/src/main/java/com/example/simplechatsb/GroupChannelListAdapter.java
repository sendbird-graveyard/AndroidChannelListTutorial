package com.example.simplechatsb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sendbird.android.GroupChannel;

import java.util.List;

public class GroupChannelListAdapter extends RecyclerView.Adapter<GroupChannelListAdapter.GroupChannelListViewHolder> {
    private List<GroupChannel> mList;

    public class GroupChannelListViewHolder extends RecyclerView.ViewHolder{
        public TextView groupChannelNameTextView;
        public Button groupChannelButton;
        private String channelUrl;

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
        holder.channelUrl = mList.get(position).getUrl();
        holder.groupChannelNameTextView.setText(holder.channelUrl);

        final String channelUrl = holder.channelUrl;
        holder.groupChannelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v ) {
                Context context = v.getContext();
                Intent intent = new Intent(context, ChatActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("channelUrl", channelUrl);
                intent.putExtras(bundle);

                context.startActivity(intent);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mList.size();
    }
}