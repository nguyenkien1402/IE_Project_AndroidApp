//package com.mobile.tiamo.questionaires;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import com.appsci.sleep.timepicker.SleepTimePicker;
//import com.jakewharton.threetenabp.AndroidThreeTen;
//import com.mobile.tiamo.R;
//
//import org.threeten.bp.LocalTime;
//
//import timber.log.Timber;
//
//public class ThirdQuestionFragment extends Fragment {
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.viewpager_question_3,container,false);
//        AndroidThreeTen.init(getActivity());
//
//        return view;
//    }
//
//    public static ThirdQuestionFragment newInstance(String text){
//        ThirdQuestionFragment f = new ThirdQuestionFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("msg",text);
//        f.setArguments(bundle);
//        return f;
//    }
//}
