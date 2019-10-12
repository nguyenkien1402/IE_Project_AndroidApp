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
import android.widget.TextView;
import android.widget.TimePicker;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_question_1, container,false);
        LinearLayout setWorkingTime = view.findViewById(R.id.questionaries_working_time);
        timeStart = view.findViewById(R.id.timestart);
        timeEnd = view.findViewById(R.id.timeend);
        ChipGroup chipGroup = view.findViewById(R.id.chipFirstGroup);
        TimePicker timePicker = view.findViewById(R.id.q1_timepicker);
        timePicker.setIs24HourView(true);
        timePicker.setHour(0);
        timePicker.setMinute(20);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                String s_hourOfDay, s_minutes;
                if(hourOfDay < 9) {
                    s_hourOfDay = "0" + hourOfDay;
                }else {
                    s_hourOfDay = hourOfDay + "";
                }
                if(minute < 9) {
                    s_minutes = "0" + minute;
                }else {
                    s_minutes = minute + "";
                }
                commuting = s_hourOfDay + ":" + s_minutes;
            }
        });
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
