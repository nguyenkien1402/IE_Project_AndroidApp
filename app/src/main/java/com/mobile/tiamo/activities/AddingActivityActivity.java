package com.mobile.tiamo.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobile.tiamo.R;
import com.mobile.tiamo.adapters.ActivityModelItem;
import com.mobile.tiamo.dao.ActivitiesModel;
import com.mobile.tiamo.dao.SQLiteDatabase;
import com.mobile.tiamo.dao.TiamoDatabase;
import com.mobile.tiamo.utilities.OtherUtilities;

import java.util.ArrayList;
import java.util.List;


/**
  * This activity is used to add the daily activity
  * Trigger from fab button in the homescreen
 */
public class AddingActivityActivity extends AppCompatActivity {

    public static int CODE_RESULT = 2;
    private static String[] chipSuggestion = null;
    private TypedArray chipSugesstionIcon = null;
    private ChipGroup chipCurrentGroup;
    private ChipGroup chipSuggesstionGroup;
    private View popupInputDialogView, popupInputHobby;
    private Button btnAdd, btnCancel,btnInputAdd, btnInputCancel;
    private TimePicker timePicker;
    private TiamoDatabase db;
    private FloatingActionButton fab;
    private EditText edInputActivity;
    private List<ActivitiesModel> activitiesModels;
    private TextView title_suggesstion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_hobby);
        initComponent();
        fabButtonAction();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.adding_hobby_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.adding_hobby_save){
            // save the selection to the database
            SaveHobbiesToDatabaseAsync saveHobbiesToDatabaseAsync = new SaveHobbiesToDatabaseAsync();
            saveHobbiesToDatabaseAsync.execute();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SaveHobbiesToDatabaseAsync saveHobbiesToDatabaseAsync = new SaveHobbiesToDatabaseAsync();
        saveHobbiesToDatabaseAsync.execute();
    }

    /*
      Save new activity to SQL database
      Using AsynTask to do in background
     */
    private class SaveHobbiesToDatabaseAsync extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            // delete all the current hobbies in database
            db.activitiesModelDao().deleteAll();
            // add the new database
            db.activitiesModelDao().insertAll(activitiesModels);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // firstly, convert data into the activity model item
            List<ActivityModelItem> activityModelItems = new ArrayList<ActivityModelItem>();
            for(int i = 0 ; i < activitiesModels.size() ; i++){
                ActivityModelItem item = new ActivityModelItem();
                item.setTitle(activitiesModels.get(i).getTitle());
                item.setHours(activitiesModels.get(i).getHours());
                item.setMinutes(activitiesModels.get(i).getMinutes());
//                item.setUid(activityModelItems.get(i).getUid());
                activityModelItems.add(item);
            }
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra("hobbies", (ArrayList<? extends Parcelable>) activityModelItems);
            setResult(CODE_RESULT,intent);
            finish();
        }
    }


    /*
      This function to initialize the component of the activity
     */
    private void initComponent() {
        // init database
        db = SQLiteDatabase.getTiamoDatabase(this);
        chipSuggestion = getResources().getStringArray(R.array.chip_suggestion);
        chipSugesstionIcon = getResources().obtainTypedArray(R.array.chip_icons);
        chipCurrentGroup = findViewById(R.id.adding_current_chip);
        chipSuggesstionGroup = findViewById(R.id.adding_suggestion_chip);
        fab = findViewById(R.id.fab_adding_activity);
        Toolbar toolbar =  (Toolbar) findViewById(R.id.toolbar_adding_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Adding Activities");

        activitiesModels = new ArrayList<ActivitiesModel>();
        // Fill down the chip suggestion.
        fillDownChipSuggestion();
        // Fill down the current chip
        GetCurrentChipAsync getCurrentChipAsync = new GetCurrentChipAsync();
        getCurrentChipAsync.execute();
    }


    /*
      Initialize the fac button action to add more activity if
      User don't find
     */
    private void fabButtonAction(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddingActivityActivity.this);
                alertDialogBuilder.setCancelable(false);
                // Init popup dialog view and it's ui controls.
                initPopupInputActivity();
                alertDialogBuilder.setView(popupInputHobby);
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                final ActivitiesModel model = new ActivitiesModel();

                btnInputAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        /*
                          Get the information from the activity
                          Save into database
                         */
                        if(!edInputActivity.getText().toString().trim().equals("") &&
                            edInputActivity.getText() != null){
                            model.setHours(timePicker.getHour());
                            model.setMinutes(timePicker.getMinute());
                            String activity = edInputActivity.getText().toString();
                            model.setTitle(activity);
                            addingNewChip(model,-1);
                            alertDialog.dismiss();
                        }else{
                            edInputActivity.setError("Please Enter Title for Activity");
                        }


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
    }

    /*
      Get the data of activity and fill on the chip
     */
    private class GetCurrentChipAsync extends AsyncTask<Void, Void, List<ActivitiesModel>>{
        @Override
        protected List<ActivitiesModel> doInBackground(Void... voids) {
            return db.activitiesModelDao().getAll();
        }

        @Override
        protected void onPostExecute(List<ActivitiesModel> result) {
            super.onPostExecute(result);
            activitiesModels = result;
            fillDownCurrentChip(result);

        }
    }

    /*
      Initialize the current activity that user have
     */
    private void fillDownCurrentChip(List<ActivitiesModel> list){
        LayoutInflater inflater = LayoutInflater.from(this);
        for(int i = 0 ; i < list.size() ; i++){
            Chip chip = (Chip) inflater.inflate(R.layout.item_chip,chipCurrentGroup,false);
            chip.setChipIcon(getResources().getDrawable(OtherUtilities.getIcon(list.get(i).getTitle())));
            if(list.get(i).getHours() == 0) {
                String text = list.get(i).getTitle() + " (" + list.get(i).getMinutes() + " minutes)";
                chip.setText(text);
            }else if(list.get(i).getMinutes() == 0) {
                String text = list.get(i).getTitle() + " (" + list.get(i).getHours() + " hours)";
                chip.setText(text);
            }else{
                String text = list.get(i).getTitle() + " (" + list.get(i).getHours() + " hours, "+list.get(i).getMinutes() +" minutes)";
                chip.setText(text);
            }
            chip.setChecked(true);
            chipCurrentGroup.addView(chip);
        }
        for(int i = 0 ; i < chipCurrentGroup.getChildCount(); i++){
            final Chip c = (Chip) chipCurrentGroup.getChildAt(i);
            c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddingActivityActivity.this);
                        alertDialogBuilder.setCancelable(false);
                        // Init popup dialog view and it's ui controls.
                        initPopupViewControls();
                        title_suggesstion.setText(OtherUtilities.suggestion(c.getText().toString()));
                        alertDialogBuilder.setView(popupInputDialogView);
                        final AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        btnAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int hour = timePicker.getHour();
                                int minute = timePicker.getMinute();
                                ActivitiesModel a = new ActivitiesModel();
                                a.setTitle(c.getText().toString());
                                a.setMinutes(minute);
                                a.setHours(hour);
                                activitiesModels.add(a);
                                if(minute == 0){
                                    String text = c.getText().toString() +" ("+hour+" hours)";
                                    c.setText(text);
                                }else if(hour == 0){
                                    String text = c.getText().toString() + " (" +minute+" minutes)";
                                    c.setText(text);
                                }else{
                                    String text = c.getText().toString() + " ("+hour+" hours, "+minute+" minutes)";
                                    c.setText(text);
                                }
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
                        String match = c.getText().toString().split("\\(")[0].trim();
                        removeCorrespondingViewInSuggestion(match);
                        for(int i = 0 ; i < activitiesModels.size() ; i++){
                            if(match.equals(activitiesModels.get(i).getTitle().trim())){
                                activitiesModels.remove(i);
                                break;
                            }
                        }
                        chipCurrentGroup.removeView(c);
                    }
                }
            });
        }
    }

    /*
     Make sure when one chip is remove from suggestion
     It is also being removed from the current chip.
     */
    private void removeCorrespondingViewInSuggestion(String match) {
        Log.d("TAG","Remove view at suggestion");
        for(int i = 0 ; i < chipSuggesstionGroup.getChildCount(); i++){
            Chip c = (Chip) chipSuggesstionGroup.getChildAt(i);
            if(match.equals(c.getText())){
                c.setChecked(false);
            }
        }
    }

    /*
     Make sure when one chip is removing from current chip
     If it exists in suggesstion chip, it will be removed as well.
     */
    private void removeCorrespondingViewInCurrent(String match){
        Log.d("TAG","Remove view at current");
        for(int i = 0 ; i < activitiesModels.size() ; i++){
            if(match.equals(activitiesModels.get(i).getTitle().trim())){
                activitiesModels.remove(i);
                break;
            }
        }
        for(int i = 0 ; i < chipCurrentGroup.getChildCount(); i++){
            Chip c = (Chip) chipCurrentGroup.getChildAt(i);
            if(match.equals(c.getText().toString().split("\\(")[0].trim())){
                chipCurrentGroup.removeView(c);
                break;
            }
        }
    }

    /*
     This is used to initialize the suggestion chip tab
     Under the current chip
     */
    private void fillDownChipSuggestion() {
        LayoutInflater inflater = LayoutInflater.from(this);
        for(int i = 0 ; i < chipSuggestion.length ; i++){
            Chip chip = (Chip) inflater.inflate(R.layout.item_chip,chipSuggesstionGroup,false);
            chip.setText(chipSuggestion[i]);
            chip.setChipIcon(getResources().getDrawable(chipSugesstionIcon.getResourceId(i,0)));
            chipSuggesstionGroup.addView(chip);
        }
        for(int i = 0 ; i < chipSuggesstionGroup.getChildCount(); i++){
            final Chip c = (Chip) chipSuggesstionGroup.getChildAt(i);
            final int finalI = i;
            c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddingActivityActivity.this);
                        alertDialogBuilder.setCancelable(false);
                        // Init popup dialog view and it's ui controls.
                        initPopupViewControls();
                        title_suggesstion.setText(OtherUtilities.suggestion(c.getText().toString()));
                        alertDialogBuilder.setView(popupInputDialogView);
                        final AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();

                        btnAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int hour = timePicker.getHour();
                                int minute = timePicker.getMinute();
                                ActivitiesModel a = new ActivitiesModel();
                                a.setTitle(c.getText().toString());
                                a.setMinutes(minute);
                                a.setHours(hour);
//                                if(minute == 0){
//                                    String text = c.getText().toString() +" ("+hour+" hours)";
//                                    c.setText(text);
//                                }else if(hour == 0){
//                                    String text = c.getText().toString() + " (" +minute+" minutes)";
//                                    c.setText(text);
//                                }else{
//                                    String text = c.getText().toString() + " ("+hour+" hours, "+minute+" minutes)";
//                                    c.setText(text);
//                                }
                                addingNewChip(a, finalI);
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
                        String match = c.getText().toString().split("\\(")[0].trim();
                        c.setText(match);
                        removeCorrespondingViewInCurrent(match);
                        chipCurrentGroup.removeView(c);
                        for(int i = 0 ; i < activitiesModels.size() ; i++){
                            if(match.equals(activitiesModels.get(i).getTitle().trim())){
                                activitiesModels.remove(i);
                                break;
                            }
                        }
                    }
                }
            });
        }
    }

    /*
     Handle adding new chip from fab button
     Pretty much the same when fill down the current chip and suggesstion chip
     Need to add action for each new chip as well.
     */
    public void addingNewChip(ActivitiesModel model,int i){
        activitiesModels.add(model);
        LayoutInflater inflater = LayoutInflater.from(this);
        final Chip c = (Chip) inflater.inflate(R.layout.item_chip,chipCurrentGroup,false);
        if(i!=-1){
            c.setChipIcon(getResources().getDrawable(chipSugesstionIcon.getResourceId(i,0)));
        }else{
            c.setChipIcon(getResources().getDrawable(R.drawable.default_activity));
        }
        int hour = model.getHours();
        int minute = model.getMinutes();
        if(minute == 0){
            String text = model.getTitle() +" ("+hour+" hours)";
            c.setText(text);
        }else if(hour == 0){
            String text = model.getTitle() + " (" +minute+" minutes)";
            c.setText(text);
        }else{
            String text = model.getTitle() + " ("+hour+" hours, "+minute+" minutes)";
            c.setText(text);
        }
        c.setChecked(true);
        chipCurrentGroup.addView(c);
        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddingActivityActivity.this);
                    alertDialogBuilder.setCancelable(false);
                    // Init popup dialog view and it's ui controls.
                    initPopupViewControls();
                    title_suggesstion.setText(OtherUtilities.suggestion(c.getText().toString()));
                    alertDialogBuilder.setView(popupInputDialogView);
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    final ActivitiesModel model = new ActivitiesModel();
                    model.setTitle(c.getText().toString());

                    btnAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int hour = timePicker.getHour();
                            int minute = timePicker.getMinute();
                            ActivitiesModel a = new ActivitiesModel();
                            a.setTitle(c.getText().toString());
                            a.setMinutes(minute);
                            a.setHours(hour);
                            activitiesModels.add(a);
                            if(minute == 0){
                                String text = c.getText().toString() +" ("+hour+" hours)";
                                c.setText(text);
                            }else if(hour == 0){
                                String text = c.getText().toString() + " (" +minute+" minutes)";
                                c.setText(text);
                            }else{
                                String text = c.getText().toString() + " ("+hour+" hours, "+minute+" minutes)";
                                c.setText(text);
                            }
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
                }
                else{
                    String match = c.getText().toString().split("\\(")[0].trim();
                    removeCorrespondingViewInSuggestion(match);
                    for(int i = 0 ; i < activitiesModels.size() ; i++){
                        if(match.equals(activitiesModels.get(i).getTitle().trim())){
                            activitiesModels.remove(i);
                            break;
                        }
                    }
                    chipCurrentGroup.removeView(c);

                }
            }
        });
    }


    /*
     Initialize the popup of adding new chip
     */
    public void initPopupViewControls(){
        // Get layout inflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(AddingActivityActivity.this);
        // Inflate the popup dialog from a layout xml file.
        popupInputDialogView = layoutInflater.inflate(R.layout.popup_hour_task, null);
        title_suggesstion = popupInputDialogView.findViewById(R.id.title_suggesstion);
        btnCancel = popupInputDialogView.findViewById(R.id.popup_btn_cancel_q);
        btnAdd = popupInputDialogView.findViewById(R.id.popup_btn_add_q);
        timePicker = popupInputDialogView.findViewById(R.id.q3_time_picker_2);
        timePicker.setIs24HourView(true);
        timePicker.setHour(1);
        timePicker.setMinute(0);
    }

    public void initPopupInputActivity(){
        LayoutInflater layoutInflaterInput = LayoutInflater.from(AddingActivityActivity.this);
        popupInputHobby = layoutInflaterInput.inflate(R.layout.popup_input_hobby, null);
        edInputActivity = popupInputHobby.findViewById(R.id.input_your_activity);
        btnInputAdd = popupInputHobby.findViewById(R.id.popup_btn_add_hobby);
        btnInputCancel = popupInputHobby.findViewById(R.id.popup_btn_cancel_hobby);
        timePicker = popupInputHobby.findViewById(R.id.q3_time_picker);
        timePicker.setIs24HourView(true);
        timePicker.setHour(1);
        timePicker.setMinute(0);
    }
}
