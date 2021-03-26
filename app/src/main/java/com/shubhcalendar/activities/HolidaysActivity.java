package com.shubhcalendar.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.shubhcalendar.R;
import com.shubhcalendar.databinding.ActivityHolidaysBinding;
import com.shubhcalendar.databinding.ActivityMainBinding;

public class HolidaysActivity extends AppCompatActivity {

TextView txt_share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holidays);


        txt_share=findViewById(R.id.txt_share);
        txt_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),PickPlanActivity.class));
            }
        });
    }
}