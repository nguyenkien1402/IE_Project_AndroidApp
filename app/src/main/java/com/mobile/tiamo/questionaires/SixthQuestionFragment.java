package com.mobile.tiamo.questionaires;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mobile.tiamo.R;

public class SixthQuestionFragment extends Fragment {
//    public static EditText day_per_week, hour_per_day;
    RadioGroup radioGroup;
    LinearLayout ln;
    public static Spinner spinnerHour,spinnerDay;
    public String[] days = {"1 day","2 days","3 days","4 days","5 days","6 days","7 days"};
    public String[] hours = {"1 hours","2 hours","3 hours","4 hours","5 hours","6 hours","7 hours",
                    "8 hours","9 hours","10 hours","11 hours","12 hours","13 hours","14 hours",
                    "15 hours","16 hours","17 hours","18 hours","19 hours","20 hours",
                    "21 hours","22 hours","23 hours","24 hours"};
    public static String daySelected, hourSelected;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_question_6,container,false);
//        day_per_week = view.findViewById(R.id.day_per_week);
//        hour_per_day = view.findViewById(R.id.hour_per_day);
        radioGroup = view.findViewById(R.id.radioGroup);
        ln = view.findViewById(R.id.view_if_yes);
        spinnerDay = view.findViewById(R.id.spinner_day);
        spinnerHour = view.findViewById(R.id.spinner_hour);

        // Adapter
        ArrayAdapter<String> adaperDays = new ArrayAdapter<String>(getContext(),R.layout.item_spinner,days);
        adaperDays.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDay.setAdapter(adaperDays);

        ArrayAdapter<String> adapterHours = new ArrayAdapter<String>(getContext(),R.layout.item_spinner,hours);
        adapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHour.setAdapter(adapterHours);

        spinnerDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                daySelected = days[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerHour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hourSelected = hours[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radio_no){
                    // do nothing
                    ln.setVisibility(View.GONE);
                }
                if(checkedId == R.id.radio_yes){
                    ln.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }

    public static SixthQuestionFragment newInstance(String text){
        SixthQuestionFragment f = new SixthQuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putString("msg",text);
        f.setArguments(bundle);
        return f;
    }
}
