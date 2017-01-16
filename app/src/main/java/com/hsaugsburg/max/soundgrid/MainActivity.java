package com.hsaugsburg.max.soundgrid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.hsaugsburg.max.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startPlayer(View view) {
        int id = view.getId();
        String ourId = getResources().getResourceEntryName(id);
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra(EXTRA_MESSAGE, ourId);
        startActivity(intent);
    }
}
