package com.example.simplechatsb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MainMenuActivity extends AppCompatActivity {
    String userID;
    String userNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        userID = getIntent().getStringExtra("userID");
        userNickname = getIntent().getStringExtra("userNickname");
        LinearLayout start_group_channel_list_activity = findViewById(R.id.start_group_channel_list_activity);
        start_group_channel_list_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, ChannelListActivity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("userNickname", userNickname);
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
                intent.putExtra("userNickname", userNickname);
                intent.putExtra("channelType", Constants.openChannelType);
                startActivity(intent);
            }
        });
    }
}
