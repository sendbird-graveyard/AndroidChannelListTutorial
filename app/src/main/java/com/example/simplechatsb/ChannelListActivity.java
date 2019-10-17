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
import java.util.List;

public class ChannelListActivity extends AppCompatActivity {
    String USER_ID;
    String CHANNEL_TYPE;
    ChannelCollection mChannelCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channel_list);

        USER_ID = getIntent().getStringExtra("userID");
        CHANNEL_TYPE = getIntent().getStringExtra("channelType");
        init_sendbird();
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

        createGroupChannelCollection(channelListQuery);
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

    protected void createGroupChannelCollection(GroupChannelListQuery channelListQuery) {
        // Create Channel Collection
        if (mChannelCollection == null) {
            Log.d("App", "Initialize GroupChannel Collection");
            mChannelCollection = new ChannelCollection(channelListQuery);

        }

        ChannelCollectionHandler channelCollectionHandler = new ChannelCollectionHandler() {

            @Override
            public void onChannelEvent(final ChannelCollection collection, final List<GroupChannel> channels, final ChannelEventAction action) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("App", "GroupChannel Collection run called");
                        switch (action) {
                            case INSERT:
                                break;
                            case UPDATE:
                                break;
                            case MOVE:
                                break;
                            case REMOVE:
                                break;
                            case CLEAR:
                                break;
                        }
                    }
                });
            }
        };

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

        GroupChannelListAdapter adapter = new GroupChannelListAdapter(list, CHANNEL_TYPE);
        rvGroupChannelList.setAdapter(adapter);
        rvGroupChannelList.setLayoutManager(new LinearLayoutManager(this));
    }

    protected void populate_open_channel_list(List<OpenChannel> list) {
        RecyclerView rvOpenChannelList = findViewById(R.id.channelListRecyclerView);

        OpenChannelListAdapter adapter = new OpenChannelListAdapter(list, CHANNEL_TYPE);
        rvOpenChannelList.setAdapter(adapter);
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
