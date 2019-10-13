package com.mobile.tiamo.questionaires;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.mobile.tiamo.R;

import java.util.List;

/**
 * A full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class QuestionnairesFirststart extends AppCompatActivity {

    NoSwipeableViewpager viewPager;
    LinearLayout main_view_pager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaires_firststart);
        viewPager = (NoSwipeableViewpager) findViewById(R.id.questionaries_viewpager);
        main_view_pager = (LinearLayout) findViewById(R.id.main_view_pager);
        viewPager.setAdapter(new QuestionariesAdapter(getFragmentManager()));
        viewPager.setSwipeLocked(true);
        final View parentLayout = findViewById(android.R.id.content);
        final Button btnNext = findViewById(R.id.btnQNext);
        final Button btnBack = findViewById(R.id.btnQBack);
        btnBack.setVisibility(View.GONE);
//        viewPager.invalidate();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tab 1
                if(viewPager.getCurrentItem() == 0){

                    String workingStartTime = FirstQuestionJavaFragment.timeRangeStart;
                    String workingEndTime = FirstQuestionJavaFragment.timeRangeEnd;
                    List<String> workingDay = FirstQuestionJavaFragment.days;
                    if(workingStartTime == null || workingDay.size() == 0 || workingEndTime == null){
                        Snackbar.make(parentLayout,"You need to select the working day and working time",Snackbar.LENGTH_SHORT).show();
                    }else{
                        btnBack.setVisibility(View.VISIBLE);
                        viewPager.setCurrentItem(getItem(+1),true);
                        return;
                    }
                }

                // Tab 2
                if(viewPager.getCurrentItem() == 1){
                    viewPager.setCurrentItem(getItem(+1),true);
                    return;
                }


                // Tab 3
                if(viewPager.getCurrentItem() == 2){
                    btnNext.setVisibility(View.GONE);
                    viewPager.setCurrentItem(getItem(+1),true);
                    return;
                }

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewPager.getCurrentItem()-1==0){
                    btnBack.setVisibility(View.GONE);
                }
                btnNext.setVisibility(View.VISIBLE);
                viewPager.setCurrentItem(getItem(-1),true);
            }
        });
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }
}
