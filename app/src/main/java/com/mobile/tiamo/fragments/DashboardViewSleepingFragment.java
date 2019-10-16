package com.mobile.tiamo.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.mobile.tiamo.R;
import com.mobile.tiamo.activities.AddingActivityActivity;
import com.mobile.tiamo.activities.MoodAndMovieTypeActivity;
import com.mobile.tiamo.activities.SearchMoviesActivity;
import com.mobile.tiamo.adapters.ActivityModelItem;
import com.mobile.tiamo.adapters.DashboardSleepingAdapter;
import com.mobile.tiamo.adapters.DashboardSleepingItem;
import com.mobile.tiamo.dao.SQLiteDatabase;
import com.mobile.tiamo.dao.SleepingModel;
import com.mobile.tiamo.dao.TiamoDatabase;
import com.mobile.tiamo.utilities.DateUtilities;
import com.mobile.tiamo.utilities.OtherUtilities;

import org.threeten.bp.Duration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
    Sleeping Fragment of the dashboard
 **/
public class DashboardViewSleepingFragment extends Fragment {
    private static TiamoDatabase db;
    private static List<ActivityModelItem> activityModelItems = null;
    View view;
    ListView lv;
    DashboardSleepingAdapter adapter;
    List<DashboardSleepingItem> listItems = null;
    private LinearLayout sleepingMood;
    private ImageView btnHappy, btnNeutral, btnTired,imvMoodView;
    private View popupView;
    private TextView tvToday, tvAvgSleepingToday, tvInbed, tvWakeup, tvYesterday;
    private View mv;
    private Button changeEmotion;


    /*
     Init the view of the fragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard_sleeping, container, false);
        db = SQLiteDatabase.getTiamoDatabase(getActivity());
        initComponent();
        buttonAction();

        // Get the sleeping data from database
        GetSleepingTimeAsync getSleepingTimeAsync = new GetSleepingTimeAsync();
        getSleepingTimeAsync.execute();

        return view;
    }

    /*
     An AsyncTask to get the sleeping time
     */
    private class GetSleepingTimeAsync extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            // Let's get sleeping time in the last 10 day
            listItems = getSleepingTime();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(listItems.size() > 0){
                adapter = new DashboardSleepingAdapter(listItems,getActivity());
                lv.setAdapter(adapter);
                setDynamicHeight(lv);
                adapter.notifyDataSetChanged();
                tvYesterday.setText(listItems.get(0).getAvg());
            }

        }
    }

    private void buttonAction(){
        sleepingMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopupSleepMood();
            }
        });
        changeEmotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopupSleepMood();
            }
        });
        mv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MoodAndMovieTypeActivity.class);
                startActivity(intent);
            }
        });
    }
    private void initComponent(){
        lv = view.findViewById(R.id.dashboard_sleep_lv);
        imvMoodView = view.findViewById(R.id.imvMoodView);
        sleepingMood = view.findViewById(R.id.dashboard_sleeping_add_emotion);
        tvToday = view.findViewById(R.id.today);
        tvAvgSleepingToday = view.findViewById(R.id.today_avg_sleep);
        tvInbed = view.findViewById(R.id.today_inbed);
        tvWakeup = view.findViewById(R.id.today_wakeup);
        tvYesterday = view.findViewById(R.id.avg_yesterday);
        changeEmotion = view.findViewById(R.id.dashboard_sleeping_add_emotion_2);
        mv = view.findViewById(R.id.dashboard_sleeping_mv);
        mv.setVisibility(View.GONE);

        tvToday.setText("To day: "+DateUtilities.getCurrentDateInString());
        tvInbed.setText("No record");
        tvWakeup.setText("No record");
        tvAvgSleepingToday.setText("No record");
    }

    /*
      An utility function to adjust the height of the list
      In the case when the list is putting inside a scrollview
     */
    public static void setDynamicHeight(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        //check adapter if null
        if (adapter == null) {
            return;
        }
        int height = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            height += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
        layoutParams.height = height + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(layoutParams);
        listView.requestLayout();
    }


    /*
      Init the popup of the sleeping mood
     */
    public void initPopupSleepMood(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setCancelable(false);

        LayoutInflater layoutInflaterInput = LayoutInflater.from(getActivity());
        popupView = layoutInflaterInput.inflate(R.layout.popup_sleeping_mood, null);
        btnHappy = popupView.findViewById(R.id.sleep_happy);
        btnNeutral = popupView.findViewById(R.id.sleep_neutral);
        btnTired = popupView.findViewById(R.id.sleep_tired);

        alertDialogBuilder.setView(popupView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        btnHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set
                imvMoodView.setImageResource(R.drawable.happy_64);
                mv.setVisibility(View.GONE);
                alertDialog.dismiss();
            }
        });

        btnTired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set
                imvMoodView.setImageResource(R.drawable.sad_64);
                mv.setVisibility(View.VISIBLE);
                alertDialog.dismiss();
            }
        });

        btnNeutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set
                imvMoodView.setImageResource(R.drawable.neutral_64);
                mv.setVisibility(View.VISIBLE);
                alertDialog.dismiss();
            }
        });

    }

    // Get the data from the database
    // Then populate to the list
    private List<DashboardSleepingItem> getSleepingTime(){
        List<DashboardSleepingItem> items = new ArrayList<DashboardSleepingItem>();
        try{
            // Get the current day
            // and the 10 day before that
            String today = DateUtilities.getCurrentDateInString();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date currentDate = dateFormat.parse(today);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.DAY_OF_YEAR,-9);
            Date before = calendar.getTime();
            String strBefore = dateFormat.format(before);

            // Get the data from database
            // Sleeping data is in the range of the current day and the last ten day
            List<SleepingModel> sleepingModels = db.sleepingModelDao().getLastTenDay(strBefore,today);
//            sleepingModels.addAll(db.sleepingModelDao().getLastTenDay("01-10-2019",today));
            String previousDate = "";
//            List<SleepingModel> models = db.sleepingModelDao().getAll();
//            for(int i = 0 ; i < models.size() ; i++){
//                Log.d("H",models.get(i).getDate());
//            }
            DashboardSleepingItem item = null;
            int avg = 0;

            // get date avg for today

            // Loop all the data and calculate the average sleeping time of each day
            // This should be safe in the database but will do it later
            for(int i = 0 ; i < sleepingModels.size() ; i++){
                String date = sleepingModels.get(i).getDate();
                if(!previousDate.equals(date)){
                    avg = 0;
                    item = new DashboardSleepingItem();
                    previousDate = date;
                    item.setDay(date);
                    item.setInBed(sleepingModels.get(i).getTime());
                    item.setWakeUp(sleepingModels.get(i).getWakeupTime());

                    LocalTime wake = LocalTime.parse(sleepingModels.get(i).getWakeupTime());
                    LocalTime bed = LocalTime.parse(sleepingModels.get(i).getTime());
                    LocalDateTime bedDate = bed.atDate(LocalDate.now());
                    LocalDateTime wakeupDate = wake.atDate(LocalDate.now());
                    if(bedDate.compareTo(wakeupDate)==1) wakeupDate = wakeupDate.plusDays(1);
                    avg += Duration.between(bedDate,wakeupDate).toMinutes();
                    item.setAvg(avg/60 + "h "+avg%60+"m");
                    items.add(item);

                }else{
                    item.setWakeUp(sleepingModels.get(i).getWakeupTime());
                    LocalTime wake = LocalTime.parse(sleepingModels.get(i).getWakeupTime());
                    LocalTime bed = LocalTime.parse(sleepingModels.get(i).getTime());
                    LocalDateTime bedDate = bed.atDate(LocalDate.now());
                    LocalDateTime wakeupDate = wake.atDate(LocalDate.now());
                    if(bedDate.compareTo(wakeupDate)==1) wakeupDate = wakeupDate.plusDays(1);
                    avg += Duration.between(bedDate,wakeupDate).toMinutes();
                    item.setAvg(avg/60 + "h "+avg%60+"m");
                }

            }
            DashboardSleepingItem lastItem = items.get(0);
            Log.d("Sleeping",lastItem.getDay()+":"+lastItem.getInBed());
            tvInbed.setText(lastItem.getInBed());
            tvWakeup.setText(lastItem.getWakeUp());
            tvAvgSleepingToday.setText(lastItem.getAvg());
            items.remove(0);

        }catch (Exception e){
            Log.d("Dashboard",e.getMessage());
        }
        return items;

    }

}
