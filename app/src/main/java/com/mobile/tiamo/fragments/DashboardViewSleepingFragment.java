package com.mobile.tiamo.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mobile.tiamo.R;
import com.mobile.tiamo.adapters.ActivityModelItem;
import com.mobile.tiamo.adapters.DashboardSleepingAdapter;
import com.mobile.tiamo.adapters.DashboardSleepingItem;
import com.mobile.tiamo.dao.SQLiteDatabase;
import com.mobile.tiamo.dao.SleepingModel;
import com.mobile.tiamo.dao.TiamoDatabase;
import com.mobile.tiamo.utilities.OtherUtilities;

import java.util.List;

public class DashboardViewSleepingFragment extends Fragment {
    private static TiamoDatabase db;
    private static List<ActivityModelItem> activityModelItems = null;
    View view;
    ListView lv;
    DashboardSleepingAdapter adapter;
    List<DashboardSleepingItem> listItems = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        db = SQLiteDatabase.getTiamoDatabase(getContext());
        view = inflater.inflate(R.layout.fragment_dashboard_sleeping, container, false);
        lv = view.findViewById(R.id.dashboard_sleep_lv);
        listItems = OtherUtilities.getSleepings();
        adapter = new DashboardSleepingAdapter(listItems,getActivity());
        lv.setAdapter(adapter);
        setDynamicHeight(lv);
        adapter.notifyDataSetChanged();

        getSleepTimeAsync getListDailyActivityAsync = new getSleepTimeAsync();
        getListDailyActivityAsync.execute();

        return view;
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

}
