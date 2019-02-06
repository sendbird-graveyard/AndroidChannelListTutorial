package com.example.simplechatsb;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.sendbird.android.GroupChannel;
import com.sendbird.android.GroupChannelListQuery;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;

import java.security.acl.Group;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    final static String APP_ID = "2686B0E8-CBD6-43EF-98C7-2D321A920418"; // Sync Server App
    String USER_ID = "204ea859-18c0-482c-8729-bd8c264b8705"; // Rommel
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_channel_list);
        USER_ID = getIntent().getStringExtra("userID");
        Log.d("debug", "userID = " + USER_ID);
        init_sendbird();
    }

    @Override
    protected void onResume() {
        super.onResume();
        connect_sendbird();
        get_group_channels();
    }

    protected void get_group_channels() {
        GroupChannelListQuery channelListQuery = GroupChannel.createMyGroupChannelListQuery();
        channelListQuery.setIncludeEmpty(true);
        List<String> customTypeList = Arrays.asList("SimpleGroupChatSB");
        channelListQuery.setCustomTypesFilter(customTypeList);
        channelListQuery.next(new GroupChannelListQuery.GroupChannelListQueryResultHandler() {
            @Override
            public void onResult(List<GroupChannel> list, SendBirdException e) {
                if (e != null) {    // Error.
                    return;
                }

                populate_group_channel_list(list);
            }
        });

    }

    protected void populate_group_channel_list(List<GroupChannel> list) {
        RecyclerView rvGroupChannelList = (RecyclerView) findViewById(R.id.groupChannelListRecyclerView);

        // Create adapter passing in the sample user data
        GroupChannelListAdapter adapter = new GroupChannelListAdapter(list);
        // Attach the adapter to the recyclerview to populate items
        rvGroupChannelList.setAdapter(adapter);
        // Set layout manager to position the items
        rvGroupChannelList.setLayoutManager(new LinearLayoutManager(this));
        // That's all!
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
        SendBird.init(APP_ID, this);

    }
}
