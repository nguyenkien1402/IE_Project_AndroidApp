package com.mobile.tiamo.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import com.mobile.tiamo.adapters.HomeListDailyActivityAdapter;
import com.mobile.tiamo.dao.ActivitiesModel;
import com.mobile.tiamo.dao.DailyActivityHobbyModel;
import com.mobile.tiamo.dao.SQLiteDatabase;
import com.mobile.tiamo.dao.TiamoDatabase;

import java.util.ArrayList;
import java.util.List;

public class DashboardViewActivityFragment extends Fragment {

    private static View view;
    private static TiamoDatabase db;
    private static List<ActivityModelItem> activityModelItems = null;
    private static List<DailyActivityHobbyModelItem> activityHobbyModelItems = null;

    private static void getActivityList() {
        activityModelItems = new ArrayList<>();
        // get Activity
        if (db.activitiesModelDao().getAll().size() > 0) {
            List<ActivitiesModel> scheduleList = db.activitiesModelDao().getAll();
            for (int i = 0; i < scheduleList.size(); i++) {
                ActivityModelItem model = new ActivityModelItem();
                model.setUid(scheduleList.get(i).getUid());
                model.setTitle(scheduleList.get(i).getTitle());
                model.setHours(scheduleList.get(i).getHours());
                model.setMinutes(scheduleList.get(i).getMinutes());

                for (DailyActivityHobbyModelItem item : activityHobbyModelItems) {
                    if (item.getUid() == model.getUid()) {

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
        if (db.dailyActivityHobbyModelDao().getAll().size() > 0) {
            List<DailyActivityHobbyModel> activityProgressList = db.dailyActivityHobbyModelDao().getAll();
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
        MainActivity.textToolbar.setText(R.string.app_name);
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
}
