package com.modosmart.agileiot.modosmartagileiot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

import com.modosmart.agileiot.modosmartagileiot.R;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        CardView room1 = findViewById(R.id.room1Card);
        CardView room2 = findViewById(R.id.room2Card);

        room1.setOnClickListener(this);
        room2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.room1Card) {
            Intent readingsIntent = new Intent(MainActivity.this, ReadingsActivity.class);
            startActivity(readingsIntent);
        } else if (view.getId() == R.id.room2Card) {
            Intent readingsIntent = new Intent(MainActivity.this, ReadingsActivity.class);
            startActivity(readingsIntent);
        }
    }
}
