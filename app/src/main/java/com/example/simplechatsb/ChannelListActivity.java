package com.example.simplechatsb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.sendbird.android.GroupChannel;
import com.sendbird.android.GroupChannelListQuery;
import com.sendbird.android.OpenChannel;
import com.sendbird.android.OpenChannelListQuery;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.sendbird.syncmanager.ChannelCollection;
import com.sendbird.syncmanager.ChannelEventAction;
import com.sendbird.syncmanager.handler.ChannelCollectionHandler;
import com.sendbird.syncmanager.handler.CompletionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChannelListActivity extends AppCompatActivity {
    String USER_ID;
    String CHANNEL_TYPE;
    ChannelCollection mChannelCollection;

    private GroupChannelListAdapter mGroupChannelListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channel_list);

        USER_ID = getIntent().getStringExtra("userID");
        CHANNEL_TYPE = getIntent().getStringExtra("channelType");
        init_sendbird();

        List<GroupChannel> initialGroupChannelList = new ArrayList<>();
        if (mGroupChannelListAdapter == null) {
            mGroupChannelListAdapter = new GroupChannelListAdapter(initialGroupChannelList, CHANNEL_TYPE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        connect_sendbird();
        switch(CHANNEL_TYPE) {
            case Constants.openChannelType:
                get_open_channels();
                break;
            case Constants.groupChannelType:
                get_group_channels();
                break;
            default:
                Log.e("App", "Invalid Channel Type: " + CHANNEL_TYPE);
                finish();
                break;
        }
    }

    protected void get_group_channels() {
        GroupChannelListQuery channelListQuery = GroupChannel.createMyGroupChannelListQuery();
        channelListQuery.setIncludeEmpty(true);
        channelListQuery.setLimit(100);
        channelListQuery.next(new GroupChannelListQuery.GroupChannelListQueryResultHandler() {
            @Override
            public void onResult(List<GroupChannel> list, SendBirdException e) {
                if (e != null) {    // Error.
                    return;
                }
                populate_group_channel_list(list);
            }
        });

        if (mChannelCollection == null) {
            createGroupChannelCollection(channelListQuery);
        }
    }

    protected void get_open_channels() {
        OpenChannelListQuery channelListQuery = OpenChannel.createOpenChannelListQuery();
        channelListQuery.setLimit(100);
        channelListQuery.next(new OpenChannelListQuery.OpenChannelListQueryResultHandler() {
            @Override
            public void onResult(List<OpenChannel> list, SendBirdException e) {
                if (e != null) {    // Error.
                    return;
                }
                populate_open_channel_list(list);
            }
        });
    }

    ChannelCollectionHandler channelCollectionHandler = new ChannelCollectionHandler() {

        @Override
        public void onChannelEvent(final ChannelCollection channelCollection, final List<GroupChannel> channelList, final ChannelEventAction action) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("App", "GroupChannel Collection run called, action = " + action.toString());

                    switch (action) {
                        case INSERT:
                            mGroupChannelListAdapter.insertChannels(channelList, channelCollection.getQuery().getOrder());
                            break;
                        case UPDATE:
                            mGroupChannelListAdapter.updateChannels(channelList);
                            break;
                        case MOVE:
                            mGroupChannelListAdapter.moveChannels(channelList, channelCollection.getQuery().getOrder());
                            break;
                        case REMOVE:
                            mGroupChannelListAdapter.removeChannels(channelList);
                            break;
                        case CLEAR:
                            mGroupChannelListAdapter.clearChannelList();
                            break;
                    }
                }
            });
        }
    };

    protected void createGroupChannelCollection(GroupChannelListQuery channelListQuery) {
        // Create Channel Collection
        if (mChannelCollection == null) {
            Log.d("App", "Initialize GroupChannel Collection");
            mChannelCollection = new ChannelCollection(channelListQuery);

        }

        mChannelCollection.setCollectionHandler(channelCollectionHandler);
        mChannelCollection.fetch(new CompletionHandler() {
            @Override
            public void onCompleted(SendBirdException e) {
                Log.d("App", "GroupChannel Collection fetch called");

            }
        });
    }

    protected void populate_group_channel_list(List<GroupChannel> list) {
        RecyclerView rvGroupChannelList = findViewById(R.id.channelListRecyclerView);

        Log.d("App", "Creating Group Channel List Adapter");

        mGroupChannelListAdapter.insertChannels(list, mChannelCollection.getQuery().getOrder());
        rvGroupChannelList.setAdapter(mGroupChannelListAdapter);
        rvGroupChannelList.setLayoutManager(new LinearLayoutManager(this));
    }

    protected void populate_open_channel_list(List<OpenChannel> list) {
        RecyclerView rvOpenChannelList = findViewById(R.id.channelListRecyclerView);

        OpenChannelListAdapter mOpenChannelAdapter = new OpenChannelListAdapter(list, CHANNEL_TYPE);
        rvOpenChannelList.setAdapter(mOpenChannelAdapter);
        rvOpenChannelList.setLayoutManager(new LinearLayoutManager(this));
    }

    protected void connect_sendbird() {
        SendBird.connect(USER_ID, new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                if (e != null) {    // Error.
                    return;
                }
            }
        });
    }
    protected void init_sendbird() {
        SendBird.init(LoginActivity.APP_ID, this);
    }
}
