package com.mobile.tiamo.questionaires;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mobile.tiamo.MainActivity;
import com.mobile.tiamo.R;
import com.mobile.tiamo.activities.AddingScheduleActivity;
import com.mobile.tiamo.utilities.Messages;
import com.mobile.tiamo.utilities.SavingDataSharePreference;

public class FifthQuestionFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_question_5,container,false);

        Button btnAddingRoutine = (Button) view.findViewById(R.id.adding_more_routine);
        Button btnGooApp = (Button) view.findViewById(R.id.go_to_app);

        btnAddingRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SavingDataSharePreference.savingLocalData(getContext(), Messages.LOCAL_DATA,"flagAnswer",1);
                Intent i = new Intent(getActivity(), AddingScheduleActivity.class);
                startActivity(i);
            }
        });

        btnGooApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the data to Sharepreferences first
                SavingDataSharePreference.savingLocalData(getContext(), Messages.LOCAL_DATA,"flagAnswer",1);
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
            }
        });

        return view;
    }

    public static FifthQuestionFragment newInstance(String text){
        FifthQuestionFragment f = new FifthQuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putString("msg",text);
        f.setArguments(bundle);
        return f;
    }
}
