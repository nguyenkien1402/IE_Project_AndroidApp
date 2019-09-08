//package com.mobile.tiamo.fragments;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.TableLayout;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
//import androidx.fragment.app.Fragment;
//import androidx.viewpager.widget.ViewPager;
//
//import com.getbase.floatingactionbutton.FloatingActionButton;
//import com.google.android.material.tabs.TabLayout;
//import com.mobile.tiamo.MainActivity;
//import com.mobile.tiamo.R;
//import com.mobile.tiamo.activities.AddingRoutineActivity;
//import com.mobile.tiamo.adapters.ActivityModelItem;
//import com.mobile.tiamo.adapters.ScheduleAdapter;
//import com.mobile.tiamo.adapters.ScheduleItem;
//import com.mobile.tiamo.adapters.ScheduleViewPagerAdapter;
//import com.mobile.tiamo.dao.ActivitiesModel;
//import com.mobile.tiamo.dao.SQLiteDatabase;
//import com.mobile.tiamo.dao.Schedule;
//import com.mobile.tiamo.dao.TiamoDatabase;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ScheduleFragment extends Fragment {
//    TabLayout tabLayout;
//    ViewPager viewPager;
//    View view;
//    FloatingActionButton btnAddingRoutine, btnAddingActivity;
//    TiamoDatabase db;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        setHasOptionsMenu(true);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.fragment_schedule, container, false);
//        btnAddingActivity = (FloatingActionButton) view.findViewById(R.id.action_activity);
//        btnAddingRoutine = (FloatingActionButton) view.findViewById(R.id.action_routine);
//
//        viewPager = (ViewPager) view.findViewById(R.id.schedule_viewpager_1);
//        setupViewPager(viewPager);
//        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(viewPager);
//
//        MainActivity.textToolbar.setText(R.string.app_name);
//
//        db = SQLiteDatabase.getTiamoDatabase(getActivity());
//        tabLayout.setTabTextColors(Color.parseColor("#e3e3e3"), Color.parseColor("#ffffff"));
//        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#ffffff"));
//        tabLayout.setSelectedTabIndicatorHeight((int) (5 * getResources().getDisplayMetrics().density));
//
//        btnAddingRoutine.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), AddingRoutineActivity.class);
//                startActivityForResult(intent,1);
//            }
//        });
//
//        btnAddingActivity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LayoutInflater inflater1 = LayoutInflater.from(getContext());
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
//                alertDialogBuilder.setCancelable(false);
//                // Inflate the popup dialog from a layout xml file.
//                View popupInputDialogView = inflater1.inflate(R.layout.popup_input_activity, null);
//                final EditText edTitle = (EditText) popupInputDialogView.findViewById(R.id.adding_routine_title);
//                final EditText edHour = (EditText) popupInputDialogView.findViewById(R.id.adding_routine_hours);
//                Button btnAdd = popupInputDialogView.findViewById(R.id.btn_adding_routine_add);
//                Button btnCancel = popupInputDialogView.findViewById(R.id.btn_adding_routine_cancel);
//
//                alertDialogBuilder.setView(popupInputDialogView);
//                final AlertDialog alertDialog = alertDialogBuilder.create();
//                alertDialog.show();
//                btnAdd.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // save to database
//                        saveActivity(edTitle.getText().toString(), Integer.parseInt(edHour.getText().toString()));
//                        alertDialog.cancel();
//                    }
//                });
//
//                btnCancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        alertDialog.cancel();
//                    }
//                });
//            }
//        });
//
//        return view;
//    }
//
//    private void setupViewPager(ViewPager viewPager) {
//        Log.d("TAG","Call again");
//        ScheduleViewPagerAdapter adapter = new ScheduleViewPagerAdapter(getChildFragmentManager());
//        adapter.addFragment(new ScheduleRoutinePagerFragment(getContext()),"Routine");
//        adapter.addFragment(new ScheduleActivityPagerFragment(), "Activity");
//        viewPager.setAdapter(adapter);
//    }
//
//    private void saveActivity(String title, int hour) {
//        AddActivityModelAsyn addActivityModelAsyn = new AddActivityModelAsyn();
//        addActivityModelAsyn.execute(title,hour+"");
//    }
//
//    private class AddActivityModelAsyn extends AsyncTask<String,Void,ActivitiesModel>{
//        @Override
//        protected ActivitiesModel doInBackground(String... strings) {
//            int hour = Integer.parseInt(strings[1]);
//            ActivitiesModel activitiesModel = new ActivitiesModel(strings[0],hour);
//            long uid = db.activitiesModelDao().insert(activitiesModel);
//            activitiesModel.setUid(uid);
//            return activitiesModel;
//        }
//
//        @Override
//        protected void onPostExecute(ActivitiesModel s) {
//            super.onPostExecute(s);
//            Toast.makeText(getActivity(),"ID: "+s.getUid() +" - " + s.getTitle(),Toast.LENGTH_LONG).show();
//            ActivityModelItem activityModelItem = new ActivityModelItem();
//            if(s.getTitle() != null){
//                activityModelItem.setTitle(s.getTitle());
//            }
//            activityModelItem.setDayPerWeek(s.getDayPerWeek());
//            activityModelItem.setHours(s.getHours());
//            activityModelItem.setIsHighPriority(s.getIsHighPriority());
//            activityModelItem.setUid(s.getUid());
//            ScheduleActivityPagerFragment.datasets.add(activityModelItem);
//            ScheduleActivityPagerFragment.adapter.notifyDataSetChanged();
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(resultCode == getActivity().RESULT_OK){
//            ScheduleRoutinePagerFragment.datasets.clear();
//            ScheduleRoutinePagerFragment.adapter.clear();
//            ScheduleRoutinePagerFragment.RefreshListScheduleAysnc getAllScheduleAysnc = new ScheduleRoutinePagerFragment.RefreshListScheduleAysnc();
//            getAllScheduleAysnc.execute();
//        }
//    }
//
//
////    @Override
////    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
////        inflater.inflate(R.menu.schedule_menu,menu);
////        super.onCreateOptionsMenu(menu, inflater);
////    }
////
////
////
////    @Override
////    public boolean onOptionsItemSelected(MenuItem item) {
////        int itemId = item.getItemId();
////        if(itemId == R.id.schedule_menu_add){
////            Intent intent = new Intent(getActivity(), AddingRoutineActivity.class);
////            startActivityForResult(intent,1);
////        }
////        return super.onOptionsItemSelected(item);
////    }
//
//
//}
