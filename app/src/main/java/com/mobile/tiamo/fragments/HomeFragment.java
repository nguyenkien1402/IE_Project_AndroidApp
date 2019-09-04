package com.mobile.tiamo.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.github.badoualy.datepicker.DatePickerTimeline;
import com.mobile.tiamo.MainActivity;
import com.mobile.tiamo.R;
import com.mobile.tiamo.adapters.DailyActivityAdapter;
import com.mobile.tiamo.adapters.DailyActivityItem;
import com.mobile.tiamo.dao.DailyRoutine;
import com.mobile.tiamo.dao.SQLiteDatabase;
import com.mobile.tiamo.dao.Schedule;
import com.mobile.tiamo.dao.TiamoDatabase;
import com.mobile.tiamo.utilities.DateUtilities;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    View view;
    TiamoDatabase db;
    List<DailyActivityItem> datasets = null;
    ListView listView;
    private static DailyActivityAdapter adapter = null;
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

    private class GetDailyActivitiesFromSelectedDate extends AsyncTask<String,Void,List<DailyRoutine>>{
        @Override
        protected List<DailyRoutine> doInBackground(String... strings) {
            String currentDate = DateUtilities.getCurrentDateInString();
            String selectedDate = strings[0];
            List<DailyRoutine> list = new ArrayList<DailyRoutine>();
            String abbDay = DateUtilities.getDayInAbbBySelectedDate(selectedDate);
            // if currentDate less than selectedDate, then check the schedule from schedule
            // and fill the list item without saving to database
            if(DateUtilities.stringToDate(selectedDate).equals(DateUtilities.stringToDate(currentDate)) ||
                DateUtilities.stringToDate(selectedDate).before(DateUtilities.stringToDate(currentDate))){
                list = db.dailyActivitiesDao().getDailyActivities(selectedDate);
                return list;
            }
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
                return list;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<DailyRoutine> dailyActivities) {
            super.onPostExecute(dailyActivities);
            datasets.clear();
//            adapter.notifyDataSetChanged();
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
                if(adapter != null){
                    adapter.notifyDataSetChanged();
                }
                Toast.makeText(getActivity(),"No Data",Toast.LENGTH_LONG).show();
            }
        }
    }


    private class GetAllDailyActivityAysnc extends AsyncTask<Void, Void, List<DailyActivityItem>>{
        @Override
        protected List<DailyActivityItem> doInBackground(Void... voids) {
            String currentDate = DateUtilities.getCurrentDateInString();
            return getDailyActivityList(currentDate);
        }

        @Override
        protected void onPostExecute(List<DailyActivityItem> dailyActivityItems) {
            if(dailyActivityItems.size() > 0){
                datasets = dailyActivityItems;
                adapter = new DailyActivityAdapter(datasets, getActivity());
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }else{
                Toast.makeText(getActivity(),"Null",Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(dailyActivityItems);
        }
    }

    public List<DailyActivityItem> getDailyActivityList(String currentDate){
        List<DailyActivityItem> dailyActivityItems = new ArrayList<DailyActivityItem>();
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

                    DailyActivityItem dailyActivityItem = new DailyActivityItem();
                    dailyActivityItem.setIsDone(0);
                    dailyActivityItem.setHours(scheduleList.get(i).getTimeStart() + " - " + scheduleList.get(i).getTimeEnd());
                    dailyActivityItem.setTitle(scheduleList.get(i).getTitle());
                    dailyActivityItem.setUid(dailyRoutine.getUid());
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
                DailyActivityItem model = new DailyActivityItem();
                model.setIsDone(dailyRoutineList.get(i).getIsDone());
                model.setTitle(dailyRoutineList.get(i).getTitle());
                model.setHours(dailyRoutineList.get(i).getHours());
                model.setUid(dailyRoutineList.get(i).getUid());
                dailyActivityItems.add(model);
            }
            return dailyActivityItems;
        }
    }
}
