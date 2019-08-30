package com.mobile.tiamo.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mobile.tiamo.R;
import com.mobile.tiamo.adapters.DailyActivityAdapter;
import com.mobile.tiamo.adapters.DailyActivityItem;
import com.mobile.tiamo.dao.DailyActivities;
import com.mobile.tiamo.dao.SQLiteDatabase;
import com.mobile.tiamo.dao.Schedule;
import com.mobile.tiamo.dao.TiamoDatabase;
import com.mobile.tiamo.utilities.DateUtilities;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    View view;
    TiamoDatabase db;
    List<DailyActivityItem> datasets;
    ListView listView;
    private static DailyActivityAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        String currentDate = DateUtilities.getCurrentDateInString();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container,false);
        listView = (ListView) view.findViewById(R.id.home_list);
        datasets = new ArrayList<DailyActivityItem>();
        db = SQLiteDatabase.getTiamoDatabase(getContext());
        GetAllDailyActivityAysnc getAllDailyActivityAysnc = new GetAllDailyActivityAysnc();
        getAllDailyActivityAysnc.execute();
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
        if(itemId == R.id.home_menu_add){
            Toast.makeText(getActivity(),"Menu Activate", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
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
            if(dailyActivityItems != null){
                Toast.makeText(getActivity(),"Not null",Toast.LENGTH_SHORT).show();
                adapter = new DailyActivityAdapter(dailyActivityItems, getActivity().getApplicationContext());
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
        if(db.dailyActivitiesDao().getDailyActivities(currentDate) != null){
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
                    DailyActivityItem dailyActivityItem = new DailyActivityItem();
                    dailyActivityItem.setIsDone(0);
                    dailyActivityItem.setHours(scheduleList.get(i).getTimeStart() + " - " + scheduleList.get(i).getTimeEnd());
                    dailyActivityItem.setTitle(scheduleList.get(i).getTitle());
                    dailyActivityItems.add(dailyActivityItem);

                    // Add Daily Activity to database
                    DailyActivities dailyActivities = new DailyActivities();
                    dailyActivities.setIsDone(0);
                    dailyActivities.setHours(scheduleList.get(i).getTimeStart() + " - " + scheduleList.get(i).getTimeEnd());
                    dailyActivities.setTitle(scheduleList.get(i).getTitle());
                    dailyActivities.setTimeStart(scheduleList.get(i).getTimeStart());
                    dailyActivities.setTimeEnd(scheduleList.get(i).getTimeEnd());
                    dailyActivities.setDate(currentDate);
                    dailyActivitiesLists.add(dailyActivities);
                    db.dailyActivitiesDao().insertAll(dailyActivitiesLists);
                }
            }else{
                return null;
            }
            return dailyActivityItems;
        }else{
            List<DailyActivities> dailyActivitiesList = db.dailyActivitiesDao().getDailyActivities(currentDate);
            for(int i = 0 ; i < dailyActivitiesList.size(); i++){
                DailyActivityItem model = new DailyActivityItem();
                model.setIsDone(dailyActivitiesList.get(i).getIsDone());
                model.setTitle(dailyActivitiesList.get(i).getTitle());
                model.setHours(dailyActivitiesList.get(i).getHours());
                dailyActivityItems.add(model);
            }
            return dailyActivityItems;
        }
    }
}
