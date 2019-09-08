package com.mobile.tiamo.questionaires;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class QuestionariesAdapter extends FragmentPagerAdapter {

    public QuestionariesAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new FirstQuestionFragment();
            case 1: return new SecondQuestionFragment();
            case 2: return ThirdQuestionFragment.newInstance("Third Fragment");
            case 3: return FifthQuestionFragment.newInstance("Fifth Fragment");
            default: return FifthQuestionFragment.newInstance("First Fragment, Default");
        }
    }

    @Override
    public int getCount() {
        return 6;
    }
}
