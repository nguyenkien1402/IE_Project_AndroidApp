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
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.github.badoualy.datepicker.DatePickerTimeline;
import com.google.android.material.chip.ChipGroup;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    private View popupInputDialogView;
    private Button btnAdd, btnCancel;
    private TimePicker timePicker;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_home, container,false);
        initComponent();

        // Load the data
        GetAllDailyActivityAysnc getAllDailyActivityAysnc = new GetAllDailyActivityAysnc();
        getAllDailyActivityAysnc.execute();

        actionButtonFromListAndTimeline();

        return view;
    }

    private void actionButtonFromListAndTimeline() {
        listViewRoutine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = datasets.get(position).getTitle() + " " + datasets.get(position).getUid();
                Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
            }
        });

        listViewActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                String s = activityModelItems.get(position).getTitle() + " " + activityModelItems.get(position).getUid();
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
                        DailyActivityHobbyModel dailyActivityHobbyModel = new DailyActivityHobbyModel();
                        dailyActivityHobbyModel.setTitle(activityModelItems.get(position).getTitle());
                        dailyActivityHobbyModel.setDateCreated(DateUtilities.getCurrentDateInString());
                        dailyActivityHobbyModel.setHours(timePicker.getHour());
                        dailyActivityHobbyModel.setMinutes(timePicker.getMinute());
                        SaveHobbiesActivityAsync saveHobbiesActivityAsync = new SaveHobbiesActivityAsync();
                        saveHobbiesActivityAsync.execute(dailyActivityHobbyModel);
                        int hour = timePicker.getHour();
                        int minute = timePicker.getMinute();
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
                Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
            }
        });

        timeline.setOnDateSelectedListener(new DatePickerTimeline.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day, int index) {
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
                Log.d(TAG,"Date:"+selectedDate);
                GetDailyActivitiesFromSelectedDate getDailyActivitiesFromSelectedDate = new GetDailyActivitiesFromSelectedDate();
                getDailyActivitiesFromSelectedDate.execute(selectedDate);
            }
        });

        btnAddingRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddingRoutineActivity.class);
                startActivityForResult(intent,AddingRoutineActivity.CODE_RESULT);
            }
        });

        btnAddingActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddingActivityActivity.class);
                startActivityForResult(intent,AddingActivityActivity.CODE_RESULT);
            }
        });
    }

    private void initComponent() {
        listViewRoutine = (ListView) view.findViewById(R.id.home_list_routine);
        listViewActivity = (ListView) view.findViewById(R.id.home_list_activity);
        timeline = view.findViewById(R.id.timeline);
        datasets = new ArrayList<DailyRoutineItem>();
        activityModelItems = new ArrayList<ActivityModelItem>();
        btnAddingActivity = (FloatingActionButton) view.findViewById(R.id.home_action_activity);
        btnAddingRoutine = (FloatingActionButton) view.findViewById(R.id.home_action_routine);
        db = SQLiteDatabase.getTiamoDatabase(getContext());
        MainActivity.textToolbar.setText(DateUtilities.getCurrentDateInString());
    }

    private void saveActivity(String title, int hour) {
        AddActivityModelAsyn addActivityModelAsyn = new AddActivityModelAsyn();
        addActivityModelAsyn.execute(title,hour+"");
    }

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
            Toast.makeText(getActivity(),"ID: "+s.getUid() +" - " + s.getTitle(),Toast.LENGTH_LONG).show();
            ActivityModelItem activityModelItem = new ActivityModelItem();
            if(s.getTitle() != null){
                activityModelItem.setTitle(s.getTitle());
            }
            activityModelItem.setDayPerWeek(s.getDayPerWeek());
            activityModelItem.setHours(s.getHours());
            activityModelItem.setIsHighPriority(s.getIsHighPriority());
            activityModelItem.setUid(s.getUid());
            ScheduleActivityPagerFragment.datasets.add(activityModelItem);
            ScheduleActivityPagerFragment.adapter.notifyDataSetChanged();
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
//            FragmentManager fm = ((AppCompatActivity)getActivity()).getSupportFragmentManager();
//            AppCompatDialogFragment dateFragment = new DatePickerFragment();
//            dateFragment.setTargetFragment(HomeFragment.this,DatePickerFragment.REQUEST_CODE);
//            dateFragment.show(fm,"datePicker");
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

        if(requestCode == AddingActivityActivity.CODE_RESULT){
//            activityModelItems.clear();
//            List<ActivityModelItem> a = data.getParcelableArrayListExtra("hobbies");
//            activityModelItems.addAll(a);
//            homeListDailyActivityAdapter.notifyDataSetChanged();
//            setDynamicHeight(listViewActivity);
            GetAllDailyActivityAysnc getAllDailyActivityAysnc = new GetAllDailyActivityAysnc();
            getAllDailyActivityAysnc.execute();
//            Log.d("Array",a.size()+"");
        }
        if(requestCode == AddingRoutineActivity.CODE_RESULT){
            Log.d("TAG","Result");
            GetAllDailyActivityResultAysnc getAllDailyActivityAysnc = new GetAllDailyActivityResultAysnc();
            getAllDailyActivityAysnc.execute();
        }
    }

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
//                List<ActivitiesModel> hobbyActivities = db.activitiesModelDao().getAll();
//                List<DailyActivityHobbyModel> dailyActivityHobbyModels;
//                if(db.dailyActivityHobbyModelDao().getDailyActivityHobbyByDate(currentDate).size() > 0){
//                    dailyActivityHobbyModels = db.dailyActivityHobbyModelDao().getDailyActivityHobbyByDate(currentDate);
//                    // First, loop all the hobby activities list
//                    // Then, loop all the daily activities hobby list
//                    // Check if some of them is already contanin the data, the populate the item with information
//                    for(int i = 0 ; i < hobbyActivities.size() ; i++){
//                        ActivityModelItem model = new ActivityModelItem();
//                        model.setUid(hobbyActivities.get(i).getUid());
//                        model.setTitle(hobbyActivities.get(i).getTitle());
//                        model.setHours(hobbyActivities.get(i).getHours());
//                        model.setMinutes(hobbyActivities.get(i).getMinutes());
//                        for(int j = 0 ; j < dailyActivityHobbyModels.size() ; j++){
//                            if(hobbyActivities.get(i).getTitle().equals(dailyActivityHobbyModels.get(j).getTitle())){
//                                model.setHourPractice(dailyActivityHobbyModels.get(j).getHours());
//                                model.setMinutePractice(dailyActivityHobbyModels.get(j).getMinutes());
//                                activityModelItems.add(model);
//                                break;
//                            }
//                        }
//                    }
//                }
                datasets = getDailyActivityList(currentDate);
                return null;
            }
            if(DateUtilities.stringToDate(selectedDate).before(DateUtilities.stringToDate(currentDate))){
                if(db.dailyActivityHobbyModelDao().getDailyActivityHobbyByDate(currentDate).size() > 0) {
                    List<DailyActivityHobbyModel> dailyActivityHobbyModels = db.dailyActivityHobbyModelDao().getDailyActivityHobbyByDate(currentDate);
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
                Log.d("Before",list.size()+"");
                return list;
            }
            if(DateUtilities.stringToDate(selectedDate).after(DateUtilities.stringToDate(currentDate))){
                Log.d("After","abb:"+abbDay);
                List<Schedule> listSchedules = db.scheduleDao().getAll();
                for(int i = 0 ; i < listSchedules.size() ; i++){
                    if(listSchedules.get(i).getSpecificDay().contains(abbDay)){
                        Log.d("After",listSchedules.get(i).getTitle()+":"+listSchedules.get(i).getSpecificDay());
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
//            adapter.notifyDataSetChanged();
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

    private class GetAllDailyActivityResultAysnc extends AsyncTask<Void, Void, List<DailyRoutineItem>>{
        @Override
        protected List<DailyRoutineItem> doInBackground(Void... voids) {
            String currentDate = DateUtilities.getCurrentDateInString();
            return getDailyActivityList(currentDate);
        }

        @Override
        protected void onPostExecute(List<DailyRoutineItem> dailyActivityItems) {
            if(dailyActivityItems.size() > 0){
                datasets.clear();
                datasets = dailyActivityItems;
                adapter = new DailyActivityAdapter(datasets, getActivity());
                listViewRoutine.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                setDynamicHeight(listViewRoutine);

            }else{
                Toast.makeText(getActivity(),"Null",Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(dailyActivityItems);
        }
    }

    private class GetAllDailyActivityAysnc extends AsyncTask<Void, Void, List<DailyRoutineItem>>{
        @Override
        protected List<DailyRoutineItem> doInBackground(Void... voids) {
            String currentDate = DateUtilities.getCurrentDateInString();
            return getDailyActivityList(currentDate);
        }

        @Override
        protected void onPostExecute(List<DailyRoutineItem> dailyActivityItems) {
            if(dailyActivityItems.size() > 0){
                datasets = dailyActivityItems;
                adapter = new DailyActivityAdapter(datasets, getActivity());
                listViewRoutine.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                homeListDailyActivityAdapter = new HomeListDailyActivityAdapter(activityModelItems, getActivity());
                listViewActivity.setAdapter(homeListDailyActivityAdapter);
                homeListDailyActivityAdapter.notifyDataSetChanged();

                setDynamicHeight(listViewActivity);
                setDynamicHeight(listViewRoutine);

            }else{
                Toast.makeText(getActivity(),"Null",Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(dailyActivityItems);
        }
    }

    public List<DailyRoutineItem> getDailyActivityList(String currentDate){
        List<DailyRoutineItem> dailyActivityItems = new ArrayList<DailyRoutineItem>();
        List<DailyRoutine> dailyRoutineLists = new ArrayList<DailyRoutine>();
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
            // First, check if something new has added by today
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
                    long uid = db.dailyActivitiesDao().insert(dailyRoutine);
                    dailyRoutine.setUid(uid);
                    dailyRoutineLists.add(dailyRoutine);
                }
            }

            List<DailyRoutine> dailyRoutineList = db.dailyActivitiesDao().getDailyActivities(currentDate);
            for(int i = 0; i < dailyRoutineList.size(); i++){
                DailyRoutineItem model = new DailyRoutineItem();
                model.setIsDone(dailyRoutineList.get(i).getIsDone());
                model.setTitle(dailyRoutineList.get(i).getTitle());
                model.setHours(dailyRoutineList.get(i).getHours());
                model.setUid(dailyRoutineList.get(i).getUid());
                dailyActivityItems.add(model);
            }
        }

        // get Hobbies activity
        // First, get the data for current day
        activityModelItems.clear();
        List<ActivitiesModel> hobbyActivities = db.activitiesModelDao().getAll();
        List<DailyActivityHobbyModel> dailyActivityHobbyModels;
        if(db.dailyActivityHobbyModelDao().getDailyActivityHobbyByDate(currentDate).size() > 0){
            dailyActivityHobbyModels = db.dailyActivityHobbyModelDao().getDailyActivityHobbyByDate(currentDate);
            // First, loop all the hobby activities list
            // Then, loop all the daily activities hobby list
            // Check if some of them is already contanin the data, the populate the item with information
            for(int i = 0 ; i < hobbyActivities.size() ; i++){
                ActivityModelItem model = new ActivityModelItem();
                model.setUid(hobbyActivities.get(i).getUid());
                model.setTitle(hobbyActivities.get(i).getTitle());
                model.setHours(hobbyActivities.get(i).getHours());
                model.setMinutes(hobbyActivities.get(i).getMinutes());
                for(int j = 0 ; j < dailyActivityHobbyModels.size() ; j++){
                    if(hobbyActivities.get(i).getTitle().equals(dailyActivityHobbyModels.get(j).getTitle())){
                        model.setHourPractice(dailyActivityHobbyModels.get(j).getHours());
                        model.setMinutePractice(dailyActivityHobbyModels.get(j).getMinutes());
                        break;
                    }
                }
                activityModelItems.add(model);
            }
        }else{
            // load all the data from activity model dao
            if(db.activitiesModelDao().getAll().size() > 0) {
                for (int i = 0; i < hobbyActivities.size(); i++) {
                    ActivityModelItem model = new ActivityModelItem();
                    model.setUid(hobbyActivities.get(i).getUid());
                    model.setTitle(hobbyActivities.get(i).getTitle());
                    model.setHours(hobbyActivities.get(i).getHours());
                    model.setMinutes(hobbyActivities.get(i).getMinutes());
                    activityModelItems.add(model);
                }
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

    public void initPopupViewControls(){
        // Get layout inflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        // Inflate the popup dialog from a layout xml file.
        popupInputDialogView = layoutInflater.inflate(R.layout.popup_hour_task, null);
        btnCancel = popupInputDialogView.findViewById(R.id.popup_btn_cancel_q);
        btnAdd = popupInputDialogView.findViewById(R.id.popup_btn_add_q);
        timePicker = popupInputDialogView.findViewById(R.id.q3_time_picker_2);
        timePicker.setIs24HourView(true);
        timePicker.setHour(1);
        timePicker.setMinute(0);
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
}
