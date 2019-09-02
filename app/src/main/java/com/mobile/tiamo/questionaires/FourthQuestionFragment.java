package com.mobile.tiamo.questionaires;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.mobile.tiamo.R;

import java.util.ArrayList;
import java.util.List;

public class FourthQuestionFragment extends Fragment {

    String[] s = {"Reading","Exercising","Hiking","Playing Football","Climbing"};
    public static List<String> listHobbies = new ArrayList<String>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_question_4,container,false);
        ChipGroup chipGroup = (ChipGroup) view.findViewById(R.id.chip_hobby);
        for(int i = 0 ; i < s.length ; i++){
            Chip chip = (Chip) inflater.inflate(R.layout.item_chip,chipGroup,false);
            chip.setText(s[i]);
            chipGroup.addView(chip);
        }

        for(int i = 0 ; i < chipGroup.getChildCount(); i++){
            final Chip c = (Chip) chipGroup.getChildAt(i);
            c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        listHobbies.add(c.getText().toString());
                    }else{
                        listHobbies.remove(c.getText().toString());
                    }
                }
            });

        }
        return view;
    }

    public static FourthQuestionFragment newInstance(String text){
        FourthQuestionFragment f = new FourthQuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putString("msg",text);
        f.setArguments(bundle);
        return f;
    }
}
