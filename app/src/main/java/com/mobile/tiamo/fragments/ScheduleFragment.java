package com.mobile.tiamo.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.mobile.tiamo.activities.AddingScheduleActivity;
import com.mobile.tiamo.adapters.ScheduleAdapter;
import com.mobile.tiamo.adapters.ScheduleModel;
import com.mobile.tiamo.dao.SQLiteDatabase;
import com.mobile.tiamo.dao.Schedule;
import com.mobile.tiamo.dao.TiamoDatabase;

import java.util.ArrayList;
import java.util.List;

public class ScheduleFragment extends Fragment {

    View view;
    List<ScheduleModel> datasets;
    ListView listView;
    TiamoDatabase db;
    private static ScheduleAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_schedule, container, false);
        listView = (ListView) view.findViewById(R.id.schedule_list);
        datasets = new ArrayList<ScheduleModel>();
        db = SQLiteDatabase.getTiamoDatabase(getActivity());
        GetAllScheduleAysnc getAllScheduleAysnc = new GetAllScheduleAysnc();
        getAllScheduleAysnc.execute();

        return view;
    }

    private class GetAllScheduleAysnc extends AsyncTask<Void,Void, List<ScheduleModel>>{
        @Override
        protected List<ScheduleModel> doInBackground(Void... voids) {
            if(db.scheduleDao().getAll() != null){
                List<Schedule> scheduleList = db.scheduleDao().getAll();
                if(scheduleList != null){
                    for(int i = 0 ; i < scheduleList.size() ; i++){
                        ScheduleModel model = new ScheduleModel();
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
        protected void onPostExecute(List<ScheduleModel> scheduleModels) {
            adapter = new ScheduleAdapter(scheduleModels, getActivity().getApplicationContext());
            listView.setAdapter(adapter);
            super.onPostExecute(scheduleModels);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.schedule_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.schedule_menu_add){
            Intent intent = new Intent(getActivity(), AddingScheduleActivity.class);
//            startActivity(intent);
            startActivityForResult(intent,1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == getActivity().RESULT_OK){
            datasets.clear();
            adapter.clear();
            GetAllScheduleAysnc getAllScheduleAysnc = new GetAllScheduleAysnc();
            getAllScheduleAysnc.execute();
        }
    }
}
