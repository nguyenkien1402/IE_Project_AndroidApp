package com.mobile.tiamo.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.github.badoualy.datepicker.DatePickerTimeline;
import com.mobile.tiamo.MainActivity;
import com.mobile.tiamo.R;
import com.mobile.tiamo.adapters.DailyActivityAdapter;
import com.mobile.tiamo.adapters.DailyActivityItem;
import com.mobile.tiamo.dao.DailyActivities;
import com.mobile.tiamo.dao.SQLiteDatabase;
import com.mobile.tiamo.dao.Schedule;
import com.mobile.tiamo.dao.TiamoDatabase;
import com.mobile.tiamo.utilities.DateUtilities;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    View view;
    TiamoDatabase db;
    List<DailyActivityItem> datasets = null;
    ListView listView;
    private static DailyActivityAdapter adapter;
    DatePickerTimeline timeline;
    private static String TAG="HomeFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_home, container,false);
        listView = (ListView) view.findViewById(R.id.home_list);
        timeline = view.findViewById(R.id.timeline);
        datasets = new ArrayList<DailyActivityItem>();
        db = SQLiteDatabase.getTiamoDatabase(getContext());
        MainActivity.textToolbar.setText(DateUtilities.getCurrentDateInString());
        GetAllDailyActivityAysnc getAllDailyActivityAysnc = new GetAllDailyActivityAysnc();
        getAllDailyActivityAysnc.execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = datasets.get(position).getTitle() + " " + datasets.get(position).getUid();
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
        return view;
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
            FragmentManager fm = ((AppCompatActivity)getActivity()).getSupportFragmentManager();
            AppCompatDialogFragment dateFragment = new DatePickerFragment();
            dateFragment.setTargetFragment(HomeFragment.this,DatePickerFragment.REQUEST_CODE);
            dateFragment.show(fm,"datePicker");
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
    }

    private class GetDailyActivitiesFromSelectedDate extends AsyncTask<String,Void,List<DailyActivities>>{
        @Override
        protected List<DailyActivities> doInBackground(String... strings) {
            List<DailyActivities> list = db.dailyActivitiesDao().getDailyActivities(strings[0]);
            return list;
        }

        @Override
        protected void onPostExecute(List<DailyActivities> dailyActivities) {
            super.onPostExecute(dailyActivities);
            datasets.clear();
            if(dailyActivities.size() > 0){
                // Show to the list
                for(int i = 0 ; i < dailyActivities.size(); i++){
                    DailyActivityItem model = new DailyActivityItem();
                    model.setIsDone(dailyActivities.get(i).getIsDone());
                    model.setTitle(dailyActivities.get(i).getTitle());
                    model.setHours(dailyActivities.get(i).getHours());
                    model.setUid(dailyActivities.get(i).getUid());
                    datasets.add(model);
                }
                adapter.notifyDataSetChanged();
            }else{
                datasets.clear();
                adapter.notifyDataSetChanged();
                Toast.makeText(getActivity(),"Kinda null",Toast.LENGTH_LONG).show();
            }
        }
    }


    private class GetAllDailyActivityAysnc extends AsyncTask<Void, Void, List<DailyActivityItem>>{
        @Override
        protected List<DailyActivityItem> doInBackground(Void... voids) {
            String currentDate = DateUtilities.getCurrentDateInString();
            Log.d("Current Date 2:",currentDate);
            datasets = getDailyActivityList(currentDate);
            return datasets;
        }

        @Override
        protected void onPostExecute(List<DailyActivityItem> dailyActivityItems) {
            if(dailyActivityItems.size() > 0){
                adapter = new DailyActivityAdapter(datasets, getActivity());
                listView.setAdapter(adapter);
            }else{
                Toast.makeText(getActivity(),"Null cmnr",Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(dailyActivityItems);
        }
    }

    public List<DailyActivityItem> getDailyActivityList(String currentDate){
        List<DailyActivityItem> dailyActivityItems = new ArrayList<DailyActivityItem>();
        List<DailyActivities> dailyActivitiesLists = new ArrayList<DailyActivities>();
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
                    DailyActivities dailyActivities = new DailyActivities();
                    dailyActivities.setIsDone(0);
                    dailyActivities.setHours(scheduleList.get(i).getTimeStart() + " - " + scheduleList.get(i).getTimeEnd());
                    dailyActivities.setTitle(scheduleList.get(i).getTitle());
                    dailyActivities.setTimeStart(scheduleList.get(i).getTimeStart());
                    dailyActivities.setTimeEnd(scheduleList.get(i).getTimeEnd());
                    dailyActivities.setScheduleId(scheduleList.get(i).getUid());
                    dailyActivities.setDate(currentDate);
                    long uid = db.dailyActivitiesDao().insert(dailyActivities);
                    dailyActivities.setUid(uid);
                    dailyActivitiesLists.add(dailyActivities);

                    DailyActivityItem dailyActivityItem = new DailyActivityItem();
                    dailyActivityItem.setIsDone(0);
                    dailyActivityItem.setHours(scheduleList.get(i).getTimeStart() + " - " + scheduleList.get(i).getTimeEnd());
                    dailyActivityItem.setTitle(scheduleList.get(i).getTitle());
                    dailyActivityItem.setUid(dailyActivities.getUid());
                    dailyActivityItems.add(dailyActivityItem);
                }
            }else{
                return null;
            }
            return dailyActivityItems;
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
                    DailyActivities dailyActivities = new DailyActivities();
                    dailyActivities.setIsDone(0);
                    dailyActivities.setHours(newList.get(i).getTimeStart() + " - " + newList.get(i).getTimeEnd());
                    dailyActivities.setTitle(newList.get(i).getTitle());
                    dailyActivities.setTimeStart(newList.get(i).getTimeStart());
                    dailyActivities.setTimeEnd(newList.get(i).getTimeEnd());
                    dailyActivities.setScheduleId(newList.get(i).getUid());
                    dailyActivities.setDate(currentDate);
                    long uid = db.dailyActivitiesDao().insert(dailyActivities);
                    dailyActivities.setUid(uid);
                    dailyActivitiesLists.add(dailyActivities);
                }
            }

            List<DailyActivities> dailyActivitiesList = db.dailyActivitiesDao().getDailyActivities(currentDate);
            for(int i = 0 ; i < dailyActivitiesList.size(); i++){
                DailyActivityItem model = new DailyActivityItem();
                model.setIsDone(dailyActivitiesList.get(i).getIsDone());
                model.setTitle(dailyActivitiesList.get(i).getTitle());
                model.setHours(dailyActivitiesList.get(i).getHours());
                model.setUid(dailyActivitiesList.get(i).getUid());
                dailyActivityItems.add(model);
            }
            return dailyActivityItems;
        }
    }
}
