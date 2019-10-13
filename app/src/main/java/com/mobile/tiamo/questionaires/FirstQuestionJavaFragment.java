package com.mobile.tiamo.questionaires;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.mcsoft.timerangepickerdialog.RangeTimePickerDialog;
import com.mobile.tiamo.R;

import java.util.ArrayList;
import java.util.List;

public class FirstQuestionJavaFragment extends Fragment {

    public static String timeRangeStart = "";
    public static String timeRangeEnd = "";
    public static List<String> days = new ArrayList<String>();
    public static String commuting = "";
    private TextView timeStart, timeEnd;
    private NumberPicker npMin, npHour;
    private int commute_minutes, commute_hours;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_question_1, container,false);
        LinearLayout setWorkingTime = view.findViewById(R.id.questionaries_working_time);
        timeStart = view.findViewById(R.id.timestart);
        timeEnd = view.findViewById(R.id.timeend);
        ChipGroup chipGroup = view.findViewById(R.id.chipFirstGroup);
        npMin = view.findViewById(R.id.numberPickerMin_q1);
        npHour = view.findViewById(R.id.numberPickerHour_q1);
        setupNumberPickers();


        for(int i = 0 ; i < chipGroup.getChildCount() ; i++){
            final Chip chip = (Chip) chipGroup.getChildAt(i);
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        days.add(chip.getText().toString());
                    }else{
                        days.remove(chip.getText().toString());
                    }

                }
            });
        }

        setWorkingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectStartTimeQuestion();
            }
        });


        return view;
    }

    private void setupNumberPickers() {
        npHour.setMinValue(0);
        npHour.setMaxValue(2);
        npHour.setValue(0);

        String[] mins5 = {"00", "05", "10", "15", "20", "25", "30", "35", "40",
                "45", "50", "55"};
        npMin.setMinValue(0);
        npMin.setMaxValue(mins5.length - 1);
        npMin.setValue(4);
        npMin.setDisplayedValues(mins5);

        npMin.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //Display the newly selected number from picker
                commute_minutes = newVal * 5;
            }
        });
        npHour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //Display the newly selected number from picker
                commute_hours = newVal;
            }
        });
    }


    public void selectStartTimeQuestion(){
        RangeTimePickerDialog dialog = new RangeTimePickerDialog();
        dialog.newInstance();
        dialog.setRadiusDialog(20); // Set radius of dialog (default is 50)
        dialog.setIs24HourView(true); // Indicates if the format should be 24 hours
        dialog.setColorBackgroundHeader(R.color.colorPrimary); // Set Color of Background header dialog
        dialog.setColorTextButton(R.color.white); // Set Text color of button
        dialog.setColorBackgroundTimePickerHeader(R.color.colorPrimaryDark);
        FragmentManager fragmentManager = getActivity().getFragmentManager();
        dialog.setTargetFragment(this,202);
        dialog.show(fragmentManager,"");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 202)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                if (data.getExtras().containsKey(RangeTimePickerDialog.HOUR_START))
                {
                    String s_hourStart="", s_hourEnd="", s_minuteStart="", s_minuteEnd="";
                    int hourStart = data.getExtras().getInt(RangeTimePickerDialog.HOUR_START);
                    int hourEnd = data.getExtras().getInt(RangeTimePickerDialog.HOUR_END);
                    int minuteStart = data.getExtras().getInt(RangeTimePickerDialog.MINUTE_START);
                    int minuteEnd = data.getExtras().getInt(RangeTimePickerDialog.MINUTE_END);
                    // Use the returned value
                    if(hourStart <9){
                        s_hourStart = "0"+hourStart;
                    }else{
                        s_hourStart = hourStart +"";
                    }
                    if(hourEnd < 9){
                        s_hourEnd = "0" + hourEnd;
                    }else{
                        s_hourEnd = hourEnd+"";
                    }
                    if(minuteStart < 9){
                        s_minuteStart = "0" + minuteStart;
                    }else{
                        s_minuteStart = minuteStart + "";
                    }
                    if(minuteEnd < 9){
                        s_minuteEnd = "0" + minuteEnd;
                    }else{
                        s_minuteEnd = minuteEnd + "";
                    }
                    timeRangeStart = s_hourStart+":"+s_minuteStart;
                    timeRangeStart = timeRangeStart.trim();
                    timeRangeEnd = s_hourEnd+":"+s_minuteEnd;
                    timeRangeEnd = timeRangeEnd.trim();
                    timeStart.setText("From: "+timeRangeStart);
                    timeEnd.setText("To: "+timeRangeEnd);
                }
            }
        }
    }
}
