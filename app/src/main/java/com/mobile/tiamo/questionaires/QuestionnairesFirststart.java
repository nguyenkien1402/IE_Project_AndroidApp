package com.mobile.tiamo.questionaires;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mobile.tiamo.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class QuestionnairesFirststart extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout main_view_pager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaires_firststart);
        viewPager = (ViewPager) findViewById(R.id.questionaries_viewpager);
        main_view_pager = (LinearLayout) findViewById(R.id.main_view_pager);
        viewPager.setAdapter(new QuestionariesAdapter(getSupportFragmentManager()));
        Button btnNext = findViewById(R.id.btnQNext);
        Button btnBack = findViewById(R.id.btnQBack);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(getItem(+1),true);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(getItem(-1),true);
            }
        });
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }
}
