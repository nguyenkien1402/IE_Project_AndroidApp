package com.mobile.tiamo.questionaires;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.mobile.tiamo.R;
import com.mobile.tiamo.dao.ActivitiesModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FourthQuestionFragment extends Fragment {
    String[] s1 = {"Reading","Exercising","Hiking","Playing Football","Climbing"};
    List<String> s = new ArrayList<String>(Arrays.asList(s1));
    List<ActivitiesModel> activitiesModels = new ArrayList<ActivitiesModel>();
    public static List<String> listHobbies = new ArrayList<String>();
    EditText edHoursPerWeek, edInputActivity;
    View popupInputDialogView, popupInputHobby;
    Button btnAdd, btnCancel, btnAddHobby, btnInputAdd, btnInputCancel;
    ChipGroup chipGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_question_4,container,false);
        chipGroup = (ChipGroup) view.findViewById(R.id.chip_hobby);
        btnAddHobby = (Button) view.findViewById(R.id.btn_add_your_activity);
        doPlayingWithChip(inflater);
        btnAddHobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setCancelable(false);
                // Init popup dialog view and it's ui controls.
                initPopupInputActivity();
                alertDialogBuilder.setView(popupInputHobby);
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                btnInputAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(edInputActivity.getText().toString() != null){
                            String activity = edInputActivity.getText().toString();
                            addingNewChip(inflater,activity);
                        }else{

                        }
                        alertDialog.dismiss();

                    }
                });

                btnInputCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });

        return view;
    }

    private void addingNewChip(LayoutInflater inflater, String text){
        s.add(text);
        final Chip c = (Chip) inflater.inflate(R.layout.item_chip,chipGroup,false);
        c.setText(text);
        chipGroup.addView(c);
        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setCancelable(false);
                    // Init popup dialog view and it's ui controls.
                    initPopupViewControls();
                    alertDialogBuilder.setView(popupInputDialogView);
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    btnAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int hours = Integer.parseInt(edHoursPerWeek.getText().toString());
                            ActivitiesModel model = new ActivitiesModel(c.getText().toString(),hours);
                            activitiesModels.add(model);
                            listHobbies.add(c.getText().toString());
                            c.setText(c.getText().toString() +" ("+hours+" hours)");
                            alertDialog.cancel();
                        }
                    });
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            c.setChecked(false);
                            alertDialog.cancel();
                        }
                    });
                }else{
                    for(int i = 0 ; i < activitiesModels.size() ; i++){
                        if(activitiesModels.get(i).getTitle().equals(c.getText().toString().split("\\(")[0].trim())){
                            activitiesModels.remove(activitiesModels.get(i));
                            c.setText(c.getText().toString().split("\\(")[0].trim());
                            break;
                        }
                    }
                    listHobbies.remove(c.getText().toString());
                }
            }
        });
    }
    private void doPlayingWithChip(LayoutInflater inflater) {
        chipGroup.removeAllViews();
        for(int i = 0 ; i < s.size() ; i++){
            Chip chip = (Chip) inflater.inflate(R.layout.item_chip,chipGroup,false);
            chip.setText(s.get(i));
            chipGroup.addView(chip);
        }
        for(int i = 0 ; i < chipGroup.getChildCount(); i++){
            final Chip c = (Chip) chipGroup.getChildAt(i);
            c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder.setCancelable(false);
                        // Init popup dialog view and it's ui controls.
                        initPopupViewControls();
                        alertDialogBuilder.setView(popupInputDialogView);
                        final AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        btnAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int hours = Integer.parseInt(edHoursPerWeek.getText().toString());
                                ActivitiesModel model = new ActivitiesModel(c.getText().toString(),hours);
                                activitiesModels.add(model);
                                listHobbies.add(c.getText().toString());
                                c.setText(c.getText().toString() +" ("+hours+" hours)");
                                alertDialog.cancel();
                            }
                        });
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                c.setChecked(false);
                                alertDialog.cancel();
                            }
                        });
                    }else{
                        for(int i = 0 ; i < activitiesModels.size() ; i++){
                            if(activitiesModels.get(i).getTitle().equals(c.getText().toString().split("\\(")[0].trim())){
                                activitiesModels.remove(activitiesModels.get(i));
                                c.setText(c.getText().toString().split("\\(")[0].trim());
                                break;
                            }
                        }
                        listHobbies.remove(c.getText().toString());
                    }
                }
            });
        }
    }

    public void initPopupViewControls(){
        // Get layout inflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        // Inflate the popup dialog from a layout xml file.
        popupInputDialogView = layoutInflater.inflate(R.layout.popup_hour_task, null);
        edHoursPerWeek = (EditText) popupInputDialogView.findViewById(R.id.hour_per_day_popup);
        btnCancel = popupInputDialogView.findViewById(R.id.popup_btn_cancel_q);
        btnAdd = popupInputDialogView.findViewById(R.id.popup_btn_add_q);
    }

    public void initPopupInputActivity(){
        LayoutInflater layoutInflaterInput = LayoutInflater.from(getActivity());
        popupInputHobby = layoutInflaterInput.inflate(R.layout.popup_input_hobby, null);
        edInputActivity = popupInputHobby.findViewById(R.id.input_your_activity);
        btnInputAdd = popupInputHobby.findViewById(R.id.popup_btn_add_hobby);
        btnInputCancel = popupInputHobby.findViewById(R.id.popup_btn_cancel_hobby);
    }

    public static FourthQuestionFragment newInstance(String text){
        FourthQuestionFragment f = new FourthQuestionFragment();
        Bundle bundle = new Bundle();
        bundle.putString("msg",text);
        f.setArguments(bundle);
        return f;
    }
}
