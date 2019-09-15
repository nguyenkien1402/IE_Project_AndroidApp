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

import com.github.mikephil.charting.charts.LineChart;
import com.mobile.tiamo.R;
import com.mobile.tiamo.adapters.ActivityModelItem;
import com.mobile.tiamo.adapters.DashboardActivityAdapter;
import com.mobile.tiamo.dao.ActivitiesModel;
import com.mobile.tiamo.dao.SQLiteDatabase;
import com.mobile.tiamo.dao.TiamoDatabase;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private static View view;
    private static TiamoDatabase db;
    private static List<ActivityModelItem> activityModelItems = null;
    private LineChart chart;

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
                activityModelItems.add(model);
            }
        }
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard, container,false);

        db = SQLiteDatabase.getTiamoDatabase(getContext());

        GetListDailyActivityAsync getListDailyActivityAsync = new GetListDailyActivityAsync();
        getListDailyActivityAsync.execute();
        return view;
    }

    private class GetListDailyActivityAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            getActivityList();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ListView dListView = (ListView) view.findViewById(R.id.listView_dashboard);
            DashboardActivityAdapter dashboardAdapter = new DashboardActivityAdapter(getContext(), R.layout.adapter_dashboard, activityModelItems);
            dListView.setAdapter(dashboardAdapter);
        }
    }
}
