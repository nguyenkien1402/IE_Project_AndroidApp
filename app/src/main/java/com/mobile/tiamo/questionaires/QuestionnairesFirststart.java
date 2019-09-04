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

import com.google.android.material.snackbar.Snackbar;
import com.mobile.tiamo.R;
import com.mobile.tiamo.utilities.SavingDataSharePreference;

import java.util.ArrayList;
import java.util.List;

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
        final View parentLayout = findViewById(android.R.id.content);
        final Button btnNext = findViewById(R.id.btnQNext);
        Button btnBack = findViewById(R.id.btnQBack);
        Toast.makeText(this,"Number of page:"+viewPager.getAdapter().getCount(),Toast.LENGTH_LONG).show();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tab 1
                if(viewPager.getCurrentItem() == 0){
                    String workingStartTime = FirstQuestionFragment.Companion.getTimeRangeStart();
                    String workingEndTime = FirstQuestionFragment.Companion.getTimeRangeEnd();
                    ArrayList<String> workingDay = FirstQuestionFragment.Companion.getDay();
                    if(workingStartTime == null || workingDay.size() == 0 || workingEndTime == null){
                        Snackbar.make(parentLayout,"You need to fill the data",Snackbar.LENGTH_SHORT).show();
                    }else{
                        viewPager.setCurrentItem(getItem(+1),true);
                        return;
                    }
                }

                // Tab 2
                if(viewPager.getCurrentItem() == 1){
                    if(SecondQuestionFragment.editText.getText().toString() == null){
                        Snackbar.make(parentLayout,"Cannot leave a blank",Snackbar.LENGTH_SHORT).show();
                    }else{
                        viewPager.setCurrentItem(getItem(+1),true);
                        return;
                    }
                }

                // Tab 3
                if(viewPager.getCurrentItem() == 2){
                    viewPager.setCurrentItem(getItem(+1),true);
                    return;
                }

                // Tab 4 is about the gyming time
                if(viewPager.getCurrentItem() == 3){
                    if(SixthQuestionFragment.checkYes == true){
                        if(SixthQuestionFragment.daySelected.equals("") || SixthQuestionFragment.hourSelected.equals("")){
                            Snackbar.make(parentLayout,"Cannot leave a blank",Snackbar.LENGTH_SHORT).show();
                        }else{
                            viewPager.setCurrentItem(getItem(+1),true);
                        }
                    }
                    return;
                }

                // Tab 4
                if(viewPager.getCurrentItem() == 4){
//                    List<String> hobbies = FourthQuestionFragment.listHobbies;
//                    if(hobbies.size() == 0){
//                        Snackbar.make(parentLayout,"You need select hobbies",Snackbar.LENGTH_SHORT).show();
//                    }else{
                    btnNext.setVisibility(View.GONE);
                    viewPager.setCurrentItem(getItem(+1),true);
                    return;
//                    }
                }

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnNext.setVisibility(View.VISIBLE);
                viewPager.setCurrentItem(getItem(-1),true);
            }
        });
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }
}
