package com.example.simplechatsb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sendbird.android.OpenChannel;

import java.util.List;

public class OpenChannelListAdapter extends RecyclerView.Adapter<OpenChannelListAdapter.OpenChannelListViewHolder> {
    private List<OpenChannel> openChannelList;
    private String mChannelType;

    public class OpenChannelListViewHolder extends RecyclerView.ViewHolder{
        public TextView openChannelNameTextView;
        public Button openChannelButton;
        private String channelUrl;

        public OpenChannelListViewHolder(LinearLayout v) {
            super(v);
            openChannelNameTextView = itemView.findViewById(R.id.channel_url_text_view);
            openChannelButton = itemView.findViewById(R.id.join_channel_button);
        }
    }

    public OpenChannelListAdapter(List<OpenChannel> list, String channelType) {
        openChannelList = list;
        mChannelType = channelType;
    }

    @Override
    public OpenChannelListViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.channel_item, parent, false);
        OpenChannelListViewHolder vh = new OpenChannelListViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(OpenChannelListViewHolder holder, int position) {
        holder.channelUrl = openChannelList.get(position).getUrl();
        holder.openChannelNameTextView.setText(holder.channelUrl);

        final String channelUrl = holder.channelUrl;
        holder.openChannelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v ) {
                Context context = v.getContext();
                Intent intent = new Intent(context, ChatActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("channelUrl", channelUrl);
                bundle.putString("channelType", mChannelType);
                intent.putExtras(bundle);

                context.startActivity(intent);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return openChannelList.size();
    }
}