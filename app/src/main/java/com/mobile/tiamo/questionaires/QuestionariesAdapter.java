package com.mobile.tiamo.questionaires;



import android.app.Fragment;
import android.app.FragmentManager;

import androidx.legacy.app.FragmentPagerAdapter;


public class QuestionariesAdapter extends FragmentPagerAdapter {


    /**
     * @param fm
     * @deprecated Use {@link androidx.fragment.app.FragmentPagerAdapter} instead.
     */
    public QuestionariesAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new FirstQuestionJavaFragment();
            case 1: return new SecondQuestionFragment();
            case 2: return ThirdQuestionFragment.newInstance("Third Fragment");
            case 3: return FifthQuestionFragment.newInstance("Fifth Fragment");
            default: return FifthQuestionFragment.newInstance("First Fragment, Default");
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
