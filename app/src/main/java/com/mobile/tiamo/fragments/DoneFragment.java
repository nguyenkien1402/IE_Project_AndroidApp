package com.mobile.tiamo.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mobile.tiamo.R;
import com.mobile.tiamo.questionaires.QuestionnairesFirststart;
import com.stephentuso.welcome.WelcomePage;
import com.stephentuso.welcome.WelcomeUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoneFragment extends Fragment implements WelcomePage.OnChangeListener {

    private ViewGroup rootLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_done, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rootLayout = (ViewGroup) view.findViewById(R.id.layout);

        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), QuestionnairesFirststart.class);
                getActivity().finish();
                startActivity(i);
            }
        });
    }

    @Override
    public void onWelcomeScreenPageScrolled(int pageIndex, float offset, int offsetPixels) {
        if (rootLayout != null)
            WelcomeUtils.applyParallaxEffect(rootLayout, true, offsetPixels, 0.3f, 0.2f);
    }

    @Override
    public void onWelcomeScreenPageSelected(int pageIndex, int selectedPageIndex) {
        //Not used
    }

    @Override
    public void onWelcomeScreenPageScrollStateChanged(int pageIndex, int state) {
        //Not used
    }
}
