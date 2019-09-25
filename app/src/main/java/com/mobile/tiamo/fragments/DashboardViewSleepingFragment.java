package com.mobile.tiamo.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.mobile.tiamo.R;
import com.mobile.tiamo.activities.AddingActivityActivity;
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

public class DashboardViewSleepingFragment extends Fragment {
    private static TiamoDatabase db;
    private static List<ActivityModelItem> activityModelItems = null;
    View view;
    ListView lv;
    DashboardSleepingAdapter adapter;
    List<DashboardSleepingItem> listItems = null;
    private Button sleepingMood;
    private ImageView btnHappy, btnNeutral, btnTired,imvMoodView;
    private View popupView;
    private TiamoDatabase db;
    private TextView tvToday, tvAvgSleepingToday, tvInbed, tvWakeup, tvYesterday;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard_sleeping, container, false);
        lv = view.findViewById(R.id.dashboard_sleep_lv);
        listItems = OtherUtilities.getSleepings();
        adapter = new DashboardSleepingAdapter(listItems,getActivity());
        lv.setAdapter(adapter);
        setDynamicHeight(lv);
        adapter.notifyDataSetChanged();

        db = SQLiteDatabase.getTiamoDatabase(getActivity());

        initComponent();
        buttonAction();

        GetSleepingTimeAsync getSleepingTimeAsync = new GetSleepingTimeAsync();
        getSleepingTimeAsync.execute();


        getSleepTimeAsync getListDailyActivityAsync = new getSleepTimeAsync();
        getListDailyActivityAsync.execute();

        return view;
    }

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
            adapter = new DashboardSleepingAdapter(listItems,getActivity());
            lv.setAdapter(adapter);
            setDynamicHeight(lv);
            adapter.notifyDataSetChanged();

            tvYesterday.setText(listItems.get(0).getAvg());


        }
    }

    private void buttonAction(){
        sleepingMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopupSleepMood();
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

        tvToday.setText(DateUtilities.getCurrentDateInString());
        tvInbed.setText("1:15 AM");
        tvWakeup.setText("6:50 AM");
        tvAvgSleepingToday.setText("5h 15m");
    }

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

    private class getSleepTimeAsync extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            List<SleepingModel> sleepingModels = db.sleepingModelDao().getAll();
            return null;
        }
    }

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
                alertDialog.dismiss();
            }
        });

        btnTired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set
                imvMoodView.setImageResource(R.drawable.tired_sleepy);
                alertDialog.dismiss();
            }
        });

        btnNeutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set
                imvMoodView.setImageResource(R.drawable.neutral_64);
                alertDialog.dismiss();
            }
        });

    }

    private List<DashboardSleepingItem> getSleepingTime(){
        List<DashboardSleepingItem> items = new ArrayList<DashboardSleepingItem>();
        try{
            String today = DateUtilities.getCurrentDateInString();
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date currentDate = dateFormat.parse(today);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.DAY_OF_YEAR,-9);
            Date before = calendar.getTime();
            String strBefore = dateFormat.format(before);

            List<SleepingModel> sleepingModels = db.sleepingModelDao().getLastTenDay(strBefore,today);
            String previousDate = "";
            DashboardSleepingItem item = null;
            int avg = 0;
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

        }catch (Exception e){
            Log.d("Dashboard",e.getMessage());
        }
        return items;

    }

}
