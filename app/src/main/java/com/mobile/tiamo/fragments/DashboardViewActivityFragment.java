package com.mobile.tiamo.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mobile.tiamo.MainActivity;
import com.mobile.tiamo.R;
import com.mobile.tiamo.adapters.ActivityModelItem;
import com.mobile.tiamo.adapters.DailyActivityHobbyModelItem;
import com.mobile.tiamo.adapters.DashboardActivityAdapter;
import com.mobile.tiamo.dao.ActivitiesModel;
import com.mobile.tiamo.dao.DailyActivityHobbyModel;
import com.mobile.tiamo.dao.SQLiteDatabase;
import com.mobile.tiamo.dao.TiamoDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DashboardViewActivityFragment extends Fragment {

    private static View view;
    private static TiamoDatabase db;
    private static List<ActivityModelItem> activityModelItems = null;
    private static List<DailyActivityHobbyModelItem> activityHobbyModelItems = null;

    private static void getActivityList() {
        activityModelItems = new ArrayList<>();
        // get Activity
        List<ActivitiesModel> scheduleList = db.activitiesModelDao().getAll();
        if (scheduleList.size() > 0) {
            for (int i = 0; i < scheduleList.size(); i++) {
                ActivityModelItem model = new ActivityModelItem();
                model.setUid(scheduleList.get(i).getUid());
                model.setTitle(scheduleList.get(i).getTitle());
                model.setHours(scheduleList.get(i).getHours());
                model.setMinutes(scheduleList.get(i).getMinutes());

                for (DailyActivityHobbyModelItem item : activityHobbyModelItems) {
                    if (item.getTitle().equals(model.getTitle())) {

                        int activityHrs = item.getHours();
                        int acitivityMins = item.getMinutes();

                        int totalActivityHrs = model.getHourPractice();
                        int totalActivityMins = model.getMinutePractice();

                        totalActivityHrs = totalActivityHrs + activityHrs;
                        totalActivityMins = totalActivityMins + acitivityMins;

                        if (totalActivityMins > 59) {
                            totalActivityMins = totalActivityMins - 60;
                            totalActivityHrs = totalActivityHrs + 1;
                        }
                        model.setHourPractice(totalActivityHrs);
                        model.setMinutePractice(totalActivityMins);
                    }
                }
                activityModelItems.add(model);
            }
        }
    }

    private static void getActivityProgress() {
        activityHobbyModelItems = new ArrayList<>();
        // get Activity Events
        List<DailyActivityHobbyModel> activityProgressListAll = db.dailyActivityHobbyModelDao().getAll();
        List<DailyActivityHobbyModel> activityProgressList = new ArrayList<>();
        List<String> currentWeek = getCurrentWeek("dd-MM-yyyy");

        // Filter for the current week
        for (DailyActivityHobbyModel activityProgress : activityProgressListAll) {
            if (currentWeek.contains(activityProgress.getDateCreated())) {
                activityProgressList.add(activityProgress);
            }
        }

        if (activityProgressList.size() > 0) {
            for (int i = 0; i < activityProgressList.size(); i++) {
                DailyActivityHobbyModelItem model = new DailyActivityHobbyModelItem();
                model.setUid(activityProgressList.get(i).getUid());
                model.setTitle(activityProgressList.get(i).getTitle());
                model.setHours(activityProgressList.get(i).getHours());
                model.setMinutes(activityProgressList.get(i).getMinutes());
                model.setDateCreated(activityProgressList.get(i).getDateCreated());
                activityHobbyModelItems.add(model);
            }
        }
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard, container,false);
        MainActivity.textToolbar.setText(R.string.title_dashboard);
        db = SQLiteDatabase.getTiamoDatabase(getContext());

        GetListDailyActivityAsync getListDailyActivityAsync = new GetListDailyActivityAsync();
        getListDailyActivityAsync.execute();
        return view;
    }

    private class GetListDailyActivityAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            getActivityProgress();
            getActivityList();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ListView dListView = (ListView) view.findViewById(R.id.listView_dashboard);
            DashboardActivityAdapter dashboardAdapter = new DashboardActivityAdapter(getContext(), R.layout.adapter_dashboard, activityModelItems, activityHobbyModelItems);
            dListView.setAdapter(dashboardAdapter);
        }
    }

    private static List<String> getCurrentWeek(String dateFormat) {
        // Get the current week dates
        DateFormat format = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        List<String> days = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            days.add(format.format(calendar.getTime()));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return days;
    }
}
