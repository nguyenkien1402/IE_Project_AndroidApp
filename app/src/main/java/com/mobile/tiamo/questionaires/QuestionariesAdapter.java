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
//            case 0: return new FirstQuestionFragment();
            case 0: return new FirstQuestionFragment();
            case 1: return SecondQuestionFragment.newInstance("Second Fragment");
            case 2: return new ThirdQuestionFragment();
            case 3: return SixthQuestionFragment.newInstance("Sixth Fragment");
            case 4: return FourthQuestionFragment.newInstance("Fourth Fragment");
            case 5: return FifthQuestionFragment.newInstance("Fifth Fragment");
            default: return FifthQuestionFragment.newInstance("First Fragment, Default");
        }
    }

    @Override
    public int getCount() {
        return 6;
    }
}
