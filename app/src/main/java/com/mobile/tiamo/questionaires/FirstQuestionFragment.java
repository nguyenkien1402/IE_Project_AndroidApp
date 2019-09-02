//package com.mobile.tiamo.questionaires;
//
//import android.os.Bundle;
//import android.text.format.DateFormat;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import com.mobile.tiamo.R;
//
//import me.adawoud.bottomsheettimepicker.BottomSheetTimeRangePicker;
//
//public class FirstQuestionFragment extends Fragment {
//
//    public static String tagBottomSheetTimeRangePicker = "tagBottomSheetTimeRangePicker"
//    TextView setWorkingTime;
//    TextView workingTime;
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.viewpager_question_1,container,false);
//        setWorkingTime = (TextView) view.findViewById(R.id.questionaries_working_time);
//        workingTime = (TextView) view.findViewById(R.id.questionaries_time_setting);
//
//        setWorkingTime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                BottomSheetTimeRangePicker b = new BottomSheetTimeRangePicker(getActivity().getApplicationContext(),
//                        DateFormat.is24HourFormat(getActivity().getApplicationContext())).
//                        show(getFragmentManager(), tagBottomSheetTimeRangePicker);
//            }
//        });
//        return view;
//    }
//
//    public static FirstQuestionFragment newInstance(String text){
//        FirstQuestionFragment f = new FirstQuestionFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("msg",text);
//        f.setArguments(bundle);
//        return f;
//    }
//}
