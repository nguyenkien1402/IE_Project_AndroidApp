package com.mobile.tiamo.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.badoualy.datepicker.DatePickerTimeline;
import com.mobile.tiamo.MainActivity;
import com.mobile.tiamo.R;
import com.mobile.tiamo.activities.AddingActivityActivity;
import com.mobile.tiamo.activities.AddingRoutineActivity;
import com.mobile.tiamo.activities.WeeklyCalendarViewActivity;
import com.mobile.tiamo.adapters.ActivityModelItem;
import com.mobile.tiamo.adapters.DailyActivityAdapter;
import com.mobile.tiamo.adapters.DailyRoutineItem;
import com.mobile.tiamo.adapters.HomeListDailyActivityAdapter;
import com.mobile.tiamo.dao.ActivitiesModel;
import com.mobile.tiamo.dao.DailyActivityHobbyModel;
import com.mobile.tiamo.dao.DailyRoutine;
import com.mobile.tiamo.dao.SQLiteDatabase;
import com.mobile.tiamo.dao.Schedule;
import com.mobile.tiamo.dao.TiamoDatabase;
import com.mobile.tiamo.utilities.DateUtilities;
import com.mobile.tiamo.utilities.LocalNotifications;
import com.mobile.tiamo.utilities.Messages;
import com.mobile.tiamo.utilities.SavingDataSharePreference;
import com.mobile.tiamo.utilities.ShowcaseViews;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a home fragment
 * Show the list of the routine activity and the list of the daily activity
 */
public class HomeFragment extends Fragment {
    public static HomeListDailyActivityAdapter homeListDailyActivityAdapter = null;

    private View view;
    private TiamoDatabase db;
    private List<DailyRoutineItem> datasets = null;
    private List<ActivityModelItem> activityModelItems = null;
    private ListView listViewRoutine, listViewActivity;
    private static DailyActivityAdapter adapter = null;
    private static DatePickerTimeline timeline;
    private static String TAG="HomeFragment";
    private FloatingActionButton btnAddingRoutine, btnAddingActivity;
    private FloatingActionsMenu fAM;
    private View popupInputDialogView, popupRoutine;
    private Button btnAdd, btnCancel, btnRoutineYes, btnRoutineNo;
    private NumberPicker npMin, npHour;
    private LocalNotifications localNotifications;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container,false);
        initComponent();
        localNotifications = new LocalNotifications(getActivity());
        // Load the data
        GetAllDailyActivityAysnc getAllDailyActivityAysnc = new GetAllDailyActivityAysnc();
        getAllDailyActivityAysnc.execute();

        actionButtonFromListAndTimeline();

        return view;
    }

    /**
     * Handle all of the action from user
     * Also handle the loading data from the time range
     */
    private void actionButtonFromListAndTimeline() {
        listViewRoutine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        listViewActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                String currentDate = DateUtilities.getCurrentDateInString().trim();
                String selectedDate = MainActivity.textToolbar.getText().toString().trim();
                if(currentDate.equals(selectedDate)){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setCancelable(false);
                    // Init popup dialog view and it's ui controls.
                    initPopupViewControls(activityModelItems.get(position).getTitle());
                    alertDialogBuilder.setView(popupInputDialogView);
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                    npMin.setValue(activityModelItems.get(position).getMinutePractice() / 5);
                    npHour.setValue(activityModelItems.get(position).getHourPractice());


                    btnAdd.setOnClickListener(new View.OnClickListener() {
                        // Adding the new daily activity hobby model to database
                        @Override
                        public void onClick(View v) {
                            DailyActivityHobbyModel dailyActivityHobbyModel = new DailyActivityHobbyModel();
                            dailyActivityHobbyModel.setTitle(activityModelItems.get(position).getTitle());
                            dailyActivityHobbyModel.setDateCreated(DateUtilities.getCurrentDateInString());
                            dailyActivityHobbyModel.setHours(npHour.getValue());
                            dailyActivityHobbyModel.setMinutes(npMin.getValue() * 5);
                            dailyActivityHobbyModel.setUid(activityModelItems.get(position).getUid());
//                            Log.d(TAG,activityModelItems.get(position).getTitle()+"-"+activityModelItems.get(position).getUid());
                            SaveHobbiesActivityAsync saveHobbiesActivityAsync = new SaveHobbiesActivityAsync();
                            saveHobbiesActivityAsync.execute(dailyActivityHobbyModel);
                            int hour = npHour.getValue();
                            int minute = npMin.getValue() * 5;
                            activityModelItems.get(position).setHourPractice(hour);
                            activityModelItems.get(position).setMinutePractice(minute);
                            alertDialog.cancel();
                        }
                    });
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.cancel();
                        }
                    });
                }
                else{
                    Toast.makeText(getActivity(),"Not Today", Toast.LENGTH_LONG).show();
                }
            }
        });

        listViewRoutine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if(!datasets.get(position).getTitle().equals("Sleeping")){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setCancelable(false);
                    // Init popup dialog view and it's ui controls.
                    initPopupViewRoutine(datasets.get(position));
                    alertDialogBuilder.setView(popupRoutine);
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                    btnRoutineYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GetDailyActivityAsync getDailyActivityAsync = new GetDailyActivityAsync();
                            getDailyActivityAsync.execute(datasets.get(position).getUid());
                            alertDialog.cancel();
                        }
                    });

                    btnRoutineNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.cancel();
                        }
                    });
                }

            }
        });

        // When user select the date from the timeline
        // Populate the list with the new data
        timeline.setOnDateSelectedListener(new DatePickerTimeline.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day, int index) {
                // make some small adjustment before of the selected day
                month = month + 1;
                String days = "",months="";
                if(day <= 9){
                    days = "0"+day;
                }else{
                    days = day + "";
                }
                if(month <= 9){
                    months = "0" + month;
                }else{
                    months = month + "";
                }
                String selectedDate = days +"-"+months+"-"+year;
                MainActivity.textToolbar.setText(selectedDate);
                // Get the data from selected date
                GetDailyActivitiesFromSelectedDate getDailyActivitiesFromSelectedDate = new GetDailyActivitiesFromSelectedDate();
                getDailyActivitiesFromSelectedDate.execute(selectedDate);
            }
        });

        // action when click adding routine button
        btnAddingRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAM.collapse();
                Intent intent = new Intent(getActivity(), AddingRoutineActivity.class);
                startActivityForResult(intent,AddingRoutineActivity.CODE_RESULT);
            }
        });

        // action when click adding activity button
        btnAddingActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAM.collapse();
                Intent intent = new Intent(getActivity(), AddingActivityActivity.class);
                startActivityForResult(intent,AddingActivityActivity.CODE_RESULT);
            }
        });
    }

    /**
     * Init the main component of the fragment
     */
    private void initComponent() {
        listViewRoutine = (ListView) view.findViewById(R.id.home_list_routine);
        listViewActivity = (ListView) view.findViewById(R.id.home_list_activity);
        timeline = view.findViewById(R.id.timeline);
        datasets = new ArrayList<DailyRoutineItem>();
        activityModelItems = new ArrayList<ActivityModelItem>();
        btnAddingActivity = (FloatingActionButton) view.findViewById(R.id.home_action_activity);
        btnAddingRoutine = (FloatingActionButton) view.findViewById(R.id.home_action_routine);
        fAM = (FloatingActionsMenu) view.findViewById(R.id.multiple_actions);
        db = SQLiteDatabase.getTiamoDatabase(getContext());
        MainActivity.textToolbar.setText(DateUtilities.getCurrentDateInString());
    }

    private void saveActivity(String title, int hour) {
        AddActivityModelAsyn addActivityModelAsyn = new AddActivityModelAsyn();
        addActivityModelAsyn.execute(title,hour+"");
    }

    // Adding Activity Model AsyncTask
    private class AddActivityModelAsyn extends AsyncTask<String,Void, ActivitiesModel>{
        @Override
        protected ActivitiesModel doInBackground(String... strings) {
            int hour = Integer.parseInt(strings[1]);
            ActivitiesModel activitiesModel = new ActivitiesModel(strings[0],hour);
            long uid = db.activitiesModelDao().insert(activitiesModel);
            activitiesModel.setUid(uid);
            return activitiesModel;
        }

        @Override
        protected void onPostExecute(ActivitiesModel s) {
            super.onPostExecute(s);
            ActivityModelItem activityModelItem = new ActivityModelItem();
            if(s.getTitle() != null){
                activityModelItem.setTitle(s.getTitle());
            }
            activityModelItem.setDayPerWeek(s.getDayPerWeek());
            activityModelItem.setHours(s.getHours());
            activityModelItem.setIsHighPriority(s.getIsHighPriority());
            activityModelItem.setUid(s.getUid());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.home_menu_calenar){
            Intent intent = new Intent(getActivity(), WeeklyCalendarViewActivity.class);
            intent.putParcelableArrayListExtra("routine", (ArrayList<? extends Parcelable>) datasets);
            intent.putParcelableArrayListExtra("activity", (ArrayList<? extends Parcelable>) activityModelItems);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == DatePickerFragment.REQUEST_CODE && resultCode == Activity.RESULT_OK){
            String selectedDate = data.getStringExtra("selectedDate");
            int year = Integer.parseInt(selectedDate.split("-")[2]);
            int month = Integer.parseInt(selectedDate.split("-")[1]);
            int day = Integer.parseInt(selectedDate.split("-")[0]);
            timeline.setSelectedDate(year,month-1, day);
            MainActivity.textToolbar.setText(selectedDate);
            GetDailyActivitiesFromSelectedDate getDailyActivitiesFromSelectedDate = new GetDailyActivitiesFromSelectedDate();
            getDailyActivitiesFromSelectedDate.execute(selectedDate);
        }

        if(requestCode == AddingActivityActivity.CODE_RESULT && resultCode == Activity.RESULT_OK){
            Log.d("TAG","Back From Activity");
            GetAllDailyActivityAysnc getAllDailyActivityAysnc = new GetAllDailyActivityAysnc();
            getAllDailyActivityAysnc.execute();
        }
        if(requestCode == AddingRoutineActivity.CODE_RESULT && resultCode == Activity.RESULT_OK){
            Log.d("TAG","Result");
            GetAllDailyActivityResultAysnc getAllDailyActivityAysnc = new GetAllDailyActivityResultAysnc();
            getAllDailyActivityAysnc.execute();
        }
    }

    /*
     Handle the action when user click to the list of the item
     */
    private class GetDailyActivityAsync extends AsyncTask<Long, Void, Void> {
        ProgressDialog dialog ;
        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getContext());
            dialog.setMessage("Updating");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Long... voids) {
            // Change into database

            db.dailyActivitiesDao().updateIsDone(voids[0],1);
//            DailyRoutine dailyRoutine = db.dailyActivitiesDao().getDailyActivityById(voids[0]);
            return null;
        }



        @Override
        protected void onPostExecute(Void dailyActivities) {
            super.onPostExecute(dailyActivities);
            dialog.dismiss();
        }
    }
    // Get the list of daily activity from seleted date
    // The date is from time range
    private class GetDailyActivitiesFromSelectedDate extends AsyncTask<String,Void,List<DailyRoutine>>{

        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setTitle("Loading");
            dialog.show();

        }

        @Override
        protected List<DailyRoutine> doInBackground(String... strings) {
            String currentDate = DateUtilities.getCurrentDateInString();
            String selectedDate = strings[0];
            List<DailyRoutine> list = new ArrayList<DailyRoutine>();
            activityModelItems.clear();
            String abbDay = DateUtilities.getDayInAbbBySelectedDate(selectedDate);
            // if currentDate less than selectedDate, then check the schedule from schedule
            // and fill the list item without saving to database
            if(DateUtilities.stringToDate(selectedDate).equals(DateUtilities.stringToDate(currentDate))){
                datasets = getDailyActivityList(currentDate);
                return null;
            }

            // If the selected date if before the current date
            // Not allow to modify the data
            if(DateUtilities.stringToDate(selectedDate).before(DateUtilities.stringToDate(currentDate))){
                if(db.dailyActivityHobbyModelDao().getDailyActivityHobbyByDate(selectedDate).size() > 0) {
                    List<DailyActivityHobbyModel> dailyActivityHobbyModels = db.dailyActivityHobbyModelDao().getDailyActivityHobbyByDate(selectedDate);
                    for(int i = 0 ; i < dailyActivityHobbyModels.size() ; i++){
                        ActivityModelItem model = new ActivityModelItem();
                        model.setUid(dailyActivityHobbyModels.get(i).getUid());
                        model.setTitle(dailyActivityHobbyModels.get(i).getTitle());
                        model.setHourPractice(dailyActivityHobbyModels.get(i).getHours());
                        model.setMinutePractice(dailyActivityHobbyModels.get(i).getMinutes());
                        activityModelItems.add(model);
                    }
                }
                list = db.dailyActivitiesDao().getDailyActivities(selectedDate);
                return list;
            }

            // If the selected date if after the current date
            // Not allow to modify data as well.
            if(DateUtilities.stringToDate(selectedDate).after(DateUtilities.stringToDate(currentDate))){
                List<Schedule> listSchedules = db.scheduleDao().getAll();
                for(int i = 0 ; i < listSchedules.size() ; i++){
                    if(listSchedules.get(i).getSpecificDay().contains(abbDay)){
                        DailyRoutine d = new DailyRoutine();
                        d.setIsDone(0);
                        d.setScheduleId(listSchedules.get(i).getUid());
                        d.setTitle(listSchedules.get(i).getTitle());
                        d.setHours(listSchedules.get(i).getTimeStart() + " - " + listSchedules.get(i).getTimeEnd());
                        d.setTimeStart(listSchedules.get(i).getTimeStart());
                        d.setTimeEnd(listSchedules.get(i).getTimeEnd());
                        list.add(d);
                    }
                }

                if(db.activitiesModelDao().getAll().size() > 0) {
                    List<ActivitiesModel> scheduleList = db.activitiesModelDao().getAll();
                    for (int i = 0; i < scheduleList.size(); i++) {
                        ActivityModelItem model = new ActivityModelItem();
                        model.setUid(scheduleList.get(i).getUid());
                        model.setTitle(scheduleList.get(i).getTitle());
                        model.setHours(scheduleList.get(i).getHours());
                        model.setMinutes(scheduleList.get(i).getMinutes());
                        activityModelItems.add(model);
                    }
                }

                return list;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<DailyRoutine> dailyActivities) {
            super.onPostExecute(dailyActivities);
            // if the dailyactivity list is null
            // Then adjust the current list by using the setDynamicHeight
            if(dailyActivities == null){
                adapter = new DailyActivityAdapter(datasets, getActivity());
                listViewRoutine.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                homeListDailyActivityAdapter = new HomeListDailyActivityAdapter(activityModelItems, getActivity());
                listViewActivity.setAdapter(homeListDailyActivityAdapter);
                homeListDailyActivityAdapter.notifyDataSetChanged();

                setDynamicHeight(listViewActivity);
                setDynamicHeight(listViewRoutine);
                dialog.dismiss();
                return;
            }
            // if the list is not null
            if(dailyActivities.size() > 0){
                Log.d(TAG,"There is value");
                datasets.clear();
                // Show to the list
                for(int i = 0 ; i < dailyActivities.size(); i++){
                    DailyRoutineItem model = new DailyRoutineItem();
                    model.setIsDone(dailyActivities.get(i).getIsDone());
                    model.setTitle(dailyActivities.get(i).getTitle());
                    model.setHours(dailyActivities.get(i).getHours());
                    model.setUid(dailyActivities.get(i).getUid());
                    datasets.add(model);
                }
                // Update the adapter
                adapter.notifyDataSetChanged();
                homeListDailyActivityAdapter.notifyDataSetChanged();
                setDynamicHeight(listViewRoutine);
                setDynamicHeight(listViewActivity);
            }else{
                activityModelItems.clear();
                datasets.clear();
                if(adapter != null){
                    adapter.notifyDataSetChanged();
                }
                if(homeListDailyActivityAdapter != null) homeListDailyActivityAdapter.notifyDataSetChanged();
                setDynamicHeight(listViewRoutine);
                setDynamicHeight(listViewActivity);
            }
            dialog.dismiss();
        }
    }

    /**
     * This AsyncTask is used to get the activity result from the current date
     */
    private class GetAllDailyActivityResultAysnc extends AsyncTask<Void, Void, List<DailyRoutineItem>>{

        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setTitle("Update");
            dialog.show();
        }

        @Override
        protected List<DailyRoutineItem> doInBackground(Void... voids) {
            String currentDate = DateUtilities.getCurrentDateInString();
            return getDailyActivityList(currentDate);
        }

        @Override
        protected void onPostExecute(List<DailyRoutineItem> dailyActivityItems) {
            super.onPostExecute(dailyActivityItems);
            if(dailyActivityItems.size() > 0){
                datasets.clear();
                adapter.clear();
                adapter.notifyDataSetChanged();
                datasets = dailyActivityItems;
                adapter = new DailyActivityAdapter(datasets, getActivity());
                listViewRoutine.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                setDynamicHeight(listViewRoutine);

            }else{
            }
            dialog.dismiss();
        }
    }

    public void initPopupViewControls(String name) {
        // Get layout inflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        // Inflate the popup dialog from a layout xml file.
        popupInputDialogView = layoutInflater.inflate(R.layout.popup_hour_task, null);
        TextView txt = popupInputDialogView.findViewById(R.id.title_suggesstion);
        txt.setText("How much time did you put in " + name + " today?");
        btnCancel = popupInputDialogView.findViewById(R.id.popup_btn_cancel_q);
        btnAdd = popupInputDialogView.findViewById(R.id.popup_btn_add_q);

        npMin = popupInputDialogView.findViewById(R.id.numberPickerMin);
        npHour = popupInputDialogView.findViewById(R.id.numberPickerHour);
        setupNumberPickers();
    }


    private List<DailyRoutineItem> getDailyActivityList(String currentDate){
        /**
         * A function to return the list of the daily routine item
         * Params:
         *      currentDate: self-explained
         * result:
         *      none
         */
        List<DailyRoutineItem> dailyActivityItems = new ArrayList<DailyRoutineItem>();
        List<DailyRoutine> dailyRoutineLists = new ArrayList<DailyRoutine>();

        // if the daily activity of the current date is null
        // then get current activity and routine from database
        // and add those data in the daily data
        if(db.dailyActivitiesDao().getDailyActivities(currentDate).size() == 0){
            // show up all the daily activities to show in the fragment
            String currentDayAbb = DateUtilities.getCurrentDayInAbb();
            List<Schedule> scheduleList = new ArrayList<Schedule>();
            List<Schedule> all = db.scheduleDao().getAll();
            for(int i = 0 ; i < all.size() ; i++){
                if(all.get(i).getSpecificDay().contains(currentDayAbb)){
                    scheduleList.add(all.get(i));
                }
            }
            if(scheduleList != null){
                for(int i = 0 ; i < scheduleList.size(); i++){
                    // Add Daily Activity to database
                    DailyRoutine dailyRoutine = new DailyRoutine();
                    dailyRoutine.setIsDone(0);
                    dailyRoutine.setHours(scheduleList.get(i).getTimeStart() + " - " + scheduleList.get(i).getTimeEnd());
                    dailyRoutine.setTitle(scheduleList.get(i).getTitle());
                    dailyRoutine.setTimeStart(scheduleList.get(i).getTimeStart());
                    dailyRoutine.setTimeEnd(scheduleList.get(i).getTimeEnd());
                    dailyRoutine.setScheduleId(scheduleList.get(i).getUid());
                    dailyRoutine.setDate(currentDate);
                    dailyRoutine.setDayOperation(scheduleList.get(i).getSpecificDay());
                    long uid = db.dailyActivitiesDao().insert(dailyRoutine);
                    dailyRoutine.setUid(uid);
                    dailyRoutineLists.add(dailyRoutine);

                    DailyRoutineItem dailyActivityItem = new DailyRoutineItem();
                    dailyActivityItem.setIsDone(0);
                    dailyActivityItem.setHours(scheduleList.get(i).getTimeStart() + " - " + scheduleList.get(i).getTimeEnd());
                    dailyActivityItem.setTitle(scheduleList.get(i).getTitle());
                    dailyActivityItem.setUid(dailyRoutine.getUid());
                    dailyActivityItems.add(dailyActivityItem);
                }
            }else{
                return null;
            }
        }else{
            // If the data is already exist
            // First, check if something new has added by today
            // Then add the new data to the daily data
            List<Schedule> addRecently = db.scheduleDao().getListAddByDate(currentDate);
            List<Schedule> newList = new ArrayList<Schedule>();
            String dayAbb = DateUtilities.getCurrentDayInAbb();

            for(int i = 0 ; i < addRecently.size() ; i++){
                if(addRecently.get(i).getSpecificDay().contains(dayAbb)){
                    newList.add(addRecently.get(i));
                }else{
                }
            }
            for(int i = 0 ; i < newList.size() ; i++){
                if(db.dailyActivitiesDao().checkIfAlreadyExistDailyActivity(newList.get(i).getUid()) == null){
                    // add to database
                    DailyRoutine dailyRoutine = new DailyRoutine();
                    dailyRoutine.setIsDone(0);
                    dailyRoutine.setHours(newList.get(i).getTimeStart() + " - " + newList.get(i).getTimeEnd());
                    dailyRoutine.setTitle(newList.get(i).getTitle());
                    dailyRoutine.setTimeStart(newList.get(i).getTimeStart());
                    dailyRoutine.setTimeEnd(newList.get(i).getTimeEnd());
                    dailyRoutine.setScheduleId(newList.get(i).getUid());
                    dailyRoutine.setDate(currentDate);
                    dailyRoutine.setIsStorage(0);
                    dailyRoutine.setDayOperation(newList.get(i).getSpecificDay());
                    long uid = db.dailyActivitiesDao().insert(dailyRoutine);
                    dailyRoutine.setUid(uid);
                    dailyRoutineLists.add(dailyRoutine);
                }
            }

            dailyRoutineLists = db.dailyActivitiesDao().getDailyActivities(currentDate);

            for(int i = 0; i < dailyRoutineLists.size(); i++){
                DailyRoutineItem model = new DailyRoutineItem();
                model.setIsDone(dailyRoutineLists.get(i).getIsDone());
                model.setTitle(dailyRoutineLists.get(i).getTitle());
                model.setHours(dailyRoutineLists.get(i).getHours());
                model.setUid(dailyRoutineLists.get(i).getUid());
                dailyActivityItems.add(model);
            }
        }
        localNotifications.scheduleNotification(dailyRoutineLists);

        // get Hobbies activity
        // First, get the data for current day
        activityModelItems.clear();
        List<ActivitiesModel> hobbyActivities = db.activitiesModelDao().getAll();
        List<DailyActivityHobbyModel> dailyActivityHobbyModels;
        if(db.dailyActivityHobbyModelDao().getDailyActivityHobbyByDate(currentDate).size() > 0){
            dailyActivityHobbyModels = db.dailyActivityHobbyModelDao().getDailyActivityHobbyByDate(currentDate);
            // First, loop all the hobby activities list
            // Then, loop all the daily activities hobby list
            // Check if some of them is already contain the data, the populate the item with information
            for(int i = 0 ; i < hobbyActivities.size() ; i++){
                ActivityModelItem model = new ActivityModelItem();
                model.setTitle(hobbyActivities.get(i).getTitle());
                model.setHours(hobbyActivities.get(i).getHours());
                model.setMinutes(hobbyActivities.get(i).getMinutes());
                for(int j = 0 ; j < dailyActivityHobbyModels.size() ; j++){
                    if(hobbyActivities.get(i).getTitle().equals(dailyActivityHobbyModels.get(j).getTitle())){
                        model.setHourPractice(dailyActivityHobbyModels.get(j).getHours());
                        model.setMinutePractice(dailyActivityHobbyModels.get(j).getMinutes());
                        model.setUid(dailyActivityHobbyModels.get(j).getUid());
                        break;
                    }else{

                    }
                }
                activityModelItems.add(model);
            }
        }else{
            Log.d("TAG","No recording activity");
            // load all the data from activity model dao
            List<DailyActivityHobbyModel> list = new ArrayList<DailyActivityHobbyModel>();
            if(db.activitiesModelDao().getAll().size() > 0) {
                for (int i = 0; i < hobbyActivities.size(); i++) {
                    DailyActivityHobbyModel m = new DailyActivityHobbyModel();
                    m.setTitle(hobbyActivities.get(i).getTitle());
                    m.setDateCreated(DateUtilities.getCurrentDateInString());
//                    ActivityModelItem model = new ActivityModelItem();
//                    model.setUid(hobbyActivities.get(i).getUid());
//                    model.setTitle(hobbyActivities.get(i).getTitle());
//                    model.setHours(hobbyActivities.get(i).getHours());
//                    model.setMinutes(hobbyActivities.get(i).getMinutes());
//                    activityModelItems.add(model);
                    list.add(m);
                }
            }
            db.dailyActivityHobbyModelDao().insertAll(list);
            list = db.dailyActivityHobbyModelDao().getDailyActivityHobbyByDate(DateUtilities.getCurrentDateInString());
            for(int i = 0 ; i < list.size() ; i++){
                ActivityModelItem model = new ActivityModelItem();
                model.setTitle(list.get(i).getTitle());
                model.setUid(list.get(i).getUid());
                activityModelItems.add(model);
            }
        }

        return dailyActivityItems;
    }

    private class SaveHobbiesActivityAsync extends AsyncTask<DailyActivityHobbyModel, Void, Void>{

        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setTitle("Update");
            dialog.show();
        }

        @Override
        protected Void doInBackground(DailyActivityHobbyModel... dailyActivityHobbyModels) {
            // First check if there is no database contain the same information.
            if(db.dailyActivityHobbyModelDao().checkIfExists(dailyActivityHobbyModels[0].getTitle(),
                    DateUtilities.getCurrentDateInString()).size() > 0){
                // call update
                db.dailyActivityHobbyModelDao().update(dailyActivityHobbyModels[0]);
            }else{
                db.dailyActivityHobbyModelDao().insert(dailyActivityHobbyModels[0]);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            homeListDailyActivityAdapter.notifyDataSetChanged();
            dialog.dismiss();
        }
    }

    private void setupNumberPickers() {
        npHour.setMinValue(0);
        npHour.setMaxValue(12);
        npHour.setValue(1);

        String[] mins5 = {"00", "05", "10", "15", "20", "25", "30", "35", "40",
                "45", "50", "55"};
        npMin.setMinValue(0);
        npMin.setMaxValue(mins5.length - 1);
        npMin.setValue(0);
        npMin.setDisplayedValues(mins5);
    }

    private void generateShowcaseView() {

        new ShowcaseViews.Builder(getActivity())
                .setStyle(R.style.CustomShowcaseTheme2)
                .setShotMode(ShowcaseViews.SHOT_MODE_MULTIPLE)
                .addView(new ShowcaseViews.ViewProperties(
                        R.id.tv1,
                        "Routine",
                        "These are events that you must complete on specific days.")
                )
                .addView(new ShowcaseViews.ViewProperties(
                        R.id.tv2,
                        "Activity",
                        "These are extra curricular activities that you may want to introduce to your daily life.")
                )
                .addView(new ShowcaseViews.ViewProperties(
                        R.id.home_action_routine,
                        "You can add new Routines and Activities",
                        "")
                )

                .setListener(new ShowcaseViews.ShowcaseViewsListener() {
                    @Override
                    public void onShowcaseStart() {
                        // do some stuff before first target shows
                    }

                    @Override
                    public void onShowcaseEnd(boolean hadViews) {
                        // do some stuff after last target shows
                    }
                })
                .show();
    }

    private void initPopupViewRoutine(DailyRoutineItem dailyRoutineItem){
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        // Inflate the popup dialog from a layout xml file.
        popupRoutine = layoutInflater.inflate(R.layout.popup_routine_detail, null);
        TextView txt = popupRoutine.findViewById(R.id.popup_routine_title);
        txt.setText("Have you finished "+dailyRoutineItem.getTitle()+"?");
        btnRoutineYes = popupRoutine.findViewById(R.id.popup_routine_yes);
        btnRoutineNo = popupRoutine.findViewById(R.id.popup_routine_no);
    }

    public static void setDynamicHeight(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        //check adapter if null
        if (adapter == null) {
            return;
        }
        int height = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            height += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
        layoutParams.height = height + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(layoutParams);
        listView.requestLayout();
    }

    /**
     * This AsyncTask is used to get the activity result from the current date
     */
    private class GetAllDailyActivityAysnc extends AsyncTask<Void, Void, List<DailyRoutineItem>> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setTitle("Load Data");
        }

        @Override
        protected List<DailyRoutineItem> doInBackground(Void... voids) {
            String currentDate = DateUtilities.getCurrentDateInString();
            return getDailyActivityList(currentDate);
        }

        @Override
        protected void onPostExecute(List<DailyRoutineItem> dailyActivityItems) {
            super.onPostExecute(dailyActivityItems);
            if (dailyActivityItems.size() > 0) {
                datasets = dailyActivityItems;
                adapter = new DailyActivityAdapter(datasets, getActivity());
                listViewRoutine.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                homeListDailyActivityAdapter = new HomeListDailyActivityAdapter(activityModelItems, getActivity());
                listViewActivity.setAdapter(homeListDailyActivityAdapter);
                homeListDailyActivityAdapter.notifyDataSetChanged();

                setDynamicHeight(listViewActivity);
                setDynamicHeight(listViewRoutine);
            } else {
                if (activityModelItems.size() > 0) {
                    homeListDailyActivityAdapter = new HomeListDailyActivityAdapter(activityModelItems, getActivity());
                    listViewActivity.setAdapter(homeListDailyActivityAdapter);
                    homeListDailyActivityAdapter.notifyDataSetChanged();
                    setDynamicHeight(listViewActivity);
                }
            }

            // Generate ShowcaseView only for the first time
            if (SavingDataSharePreference.getDataInt(getContext(), Messages.LOCAL_DATA, Messages.FLAG_TUTORIAL_HOME) == 0) {
                SavingDataSharePreference.savingLocalData(getContext(), Messages.LOCAL_DATA, Messages.FLAG_TUTORIAL_HOME, 1);
                generateShowcaseView();
            }
            pd.dismiss();

        }
    }
}
