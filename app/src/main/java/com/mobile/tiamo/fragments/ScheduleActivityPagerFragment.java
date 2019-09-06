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

import com.mobile.tiamo.R;
import com.mobile.tiamo.adapters.ActivityModelItem;
import com.mobile.tiamo.adapters.ScheduleActivityAdapter;
import com.mobile.tiamo.adapters.ScheduleAdapter;
import com.mobile.tiamo.adapters.ScheduleItem;
import com.mobile.tiamo.dao.ActivitiesModel;
import com.mobile.tiamo.dao.SQLiteDatabase;
import com.mobile.tiamo.dao.Schedule;
import com.mobile.tiamo.dao.TiamoDatabase;

import java.util.ArrayList;
import java.util.List;

public class ScheduleActivityPagerFragment extends Fragment {

    View view;
    public static List<ActivityModelItem> datasets;
    ListView listView;
    public static TiamoDatabase db;
    public static ScheduleActivityAdapter adapter = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_schedule_activity, container, false);
        listView = (ListView) view.findViewById(R.id.schedule_activity_list);
        datasets = new ArrayList<ActivityModelItem>();

        db = SQLiteDatabase.getTiamoDatabase(getActivity());
        GetAllScheduleAysnc getAllScheduleAysnc = new GetAllScheduleAysnc();
        getAllScheduleAysnc.execute();
        return view;
    }

    public class GetAllScheduleAysnc extends AsyncTask<Void,Void, List<ActivityModelItem>> {
        @Override
        protected List<ActivityModelItem> doInBackground(Void... voids) {
            if(db.activitiesModelDao().getAll().size() > 0){
                List<ActivitiesModel> scheduleList = db.activitiesModelDao().getAll();
                    for(int i = 0 ; i < scheduleList.size() ; i++){
                        ActivityModelItem model = new ActivityModelItem();
                        model.setTitle(scheduleList.get(i).getTitle());
                        model.setHours(scheduleList.get(i).getHours());
                        if(scheduleList.get(i).getTitle().equals("Gym")){
                            model.setDayPerWeek(scheduleList.get(i).getDayPerWeek());
                        }
                        datasets.add(model);

                    }
                return datasets;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<ActivityModelItem> scheduleItems) {
            Log.d("TAG","Get routine data");
            if(scheduleItems.size() > 0 ){
                adapter = new ScheduleActivityAdapter(datasets, getActivity());
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            super.onPostExecute(scheduleItems);
        }
    }



}
