package com.example.simplechatsb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.sendbird.android.SendBirdException;
import com.sendbird.syncmanager.SendBirdSyncManager;
import com.sendbird.syncmanager.handler.CompletionHandler;

public class MainMenuActivity extends AppCompatActivity {
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        userID = getIntent().getStringExtra("userID");
        Log.d("App", "Initialize test");

        initializeSyncManager();

        LinearLayout start_group_channel_list_activity = findViewById(R.id.start_group_channel_list_activity);
        start_group_channel_list_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, ChannelListActivity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("channelType", Constants.groupChannelType);
                startActivity(intent);
            }
        });

        LinearLayout start_open_channel_list_activity = findViewById(R.id.start_open_channel_list_activity);
        start_open_channel_list_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, ChannelListActivity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("channelType", Constants.openChannelType);
                startActivity(intent);
            }
        });
    }

    protected void initializeSyncManager() {
        Log.d("App", "Initialize Sync Manager");
        SendBirdSyncManager.Options options = new SendBirdSyncManager.Options.Builder()
                .setMessageResendPolicy(SendBirdSyncManager.MessageResendPolicy.AUTOMATIC)
                .setAutomaticMessageResendRetryCount(5).build();

        SendBirdSyncManager.setup(getApplicationContext(), userID, options, new CompletionHandler() {
            @Override
            public void onCompleted(SendBirdException e) {
                if (e != null) {
                    // Error handling
                    return;
                }
            }
        });
    }

}
