package com.mobile.tiamo.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.mobile.tiamo.MainActivity;
import com.mobile.tiamo.R;
import com.mobile.tiamo.activities.AddingRoutineActivity;
import com.mobile.tiamo.adapters.ScheduleAdapter;
import com.mobile.tiamo.adapters.ScheduleItem;
import com.mobile.tiamo.dao.SQLiteDatabase;
import com.mobile.tiamo.dao.Schedule;
import com.mobile.tiamo.dao.TiamoDatabase;

import java.util.ArrayList;
import java.util.List;

public class ScheduleRoutinePagerFragment extends Fragment {

    View view;
    public static List<ScheduleItem> datasets;
    static ListView listView;
    public static TiamoDatabase db;
    public static ScheduleAdapter adapter = null;
    private static Context context;


    public ScheduleRoutinePagerFragment(Context context){
        Log.d("TAG","create routine again");


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_schedule_routine, container, false);
        context = getContext();
        listView = (ListView) view.findViewById(R.id.schedule_list);
        datasets = new ArrayList<ScheduleItem>();
        db = SQLiteDatabase.getTiamoDatabase(context);
        GetAllScheduleAysnc getAllScheduleAysnc = new GetAllScheduleAysnc();
        getAllScheduleAysnc.execute();
        return view;
    }

    public class GetAllScheduleAysnc extends AsyncTask<Void,Void, List<ScheduleItem>> {
        ProgressDialog dialog = null;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getContext());
            dialog.setTitle("Update");
            dialog.show();
        }

        @Override
        protected List<ScheduleItem> doInBackground(Void... voids) {
            if(db.scheduleDao().getAll().size() > 0){
                List<Schedule> scheduleList = db.scheduleDao().getAll();
                if(scheduleList != null){
                    for(int i = 0 ; i < scheduleList.size() ; i++){
                        ScheduleItem model = new ScheduleItem();
                        model.setTitle(scheduleList.get(i).getTitle());
                        model.setDays(scheduleList.get(i).getOperationDay());
                        model.setHours(scheduleList.get(i).getTimeStart()+" - " +scheduleList.get(i).getTimeEnd());
                        datasets.add(model);
                    }
                }
                return datasets;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<ScheduleItem> scheduleItems) {
            Log.d("TAG","Get routine data");
            if(scheduleItems.size() > 0 ){
                adapter = new ScheduleAdapter(datasets, getActivity());
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            dialog.dismiss();
            super.onPostExecute(scheduleItems);
        }
    }



    public static class RefreshListScheduleAysnc extends AsyncTask<Void,Void, List<ScheduleItem>> {
        ProgressDialog dialog = null;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setTitle("Update");
            dialog.show();
        }

        @Override
        protected List<ScheduleItem> doInBackground(Void... voids) {
            if(db.scheduleDao().getAll().size() > 0){
                datasets.clear();
                List<Schedule> scheduleList = db.scheduleDao().getAll();
                if(scheduleList != null){
                    for(int i = 0 ; i < scheduleList.size() ; i++){
                        ScheduleItem model = new ScheduleItem();
                        model.setTitle(scheduleList.get(i).getTitle());
                        model.setDays(scheduleList.get(i).getOperationDay());
                        model.setHours(scheduleList.get(i).getTimeStart()+" - " +scheduleList.get(i).getTimeEnd());
                        datasets.add(model);
                    }
                }
                return datasets;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<ScheduleItem> scheduleItems) {
            Log.d("TAG","Get routine data");
            if(scheduleItems.size() > 0 ){
                adapter.notifyDataSetChanged();
            }
            dialog.dismiss();
            super.onPostExecute(scheduleItems);
        }
    }



}
