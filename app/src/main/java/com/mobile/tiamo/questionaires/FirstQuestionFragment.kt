package com.mobile.tiamo.questionaires

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.mobile.tiamo.R
import me.adawoud.bottomsheettimepicker.BottomSheetTimeRangePicker
import me.adawoud.bottomsheettimepicker.OnTimeRangeSelectedListener

class FirstQuestionFragment : Fragment(),OnTimeRangeSelectedListener{
    private val tagBottomSheetTimeRangePicker = "tagBottomSheetTimeRangePicker"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.viewpager_question_1,container,false)
        val setWorkingTime = view.findViewById<TextView>(R.id.questionaries_working_time)
        setWorkingTime.setOnClickListener{
            BottomSheetTimeRangePicker
                    .newInstance(this, DateFormat.is24HourFormat(activity))
                    .show(activity?.supportFragmentManager, tagBottomSheetTimeRangePicker)
        }
        return view;
    }

    override fun onTimeRangeSelected(startHour: Int, startMinute: Int, endHour: Int, endMinute: Int) {
        var startHourString = startHour.toString()
        var startMinuteString = startMinute.toString()
        var endHourString = endHour.toString()
        var endMinuteString = endMinute.toString()
        when {
            startHour < 9 -> startHourString = startHour.toString().prependZero()
            startMinute < 9 -> startMinuteString = startMinute.toString().prependZero()
            endHour < 9 -> endHourString = endHour.toString().prependZero()
            endMinute < 9 -> endMinuteString = endMinute.toString().prependZero()
        }

        val tvTimeRange = view?.findViewById<TextView>(R.id.questionaries_time_setting)
        if (tvTimeRange != null) {
            tvTimeRange.text = getString(
                    R.string.chosen_time_range,
                    startHourString,
                    startMinuteString,
                    endHourString,
                    endMinuteString
            )
        }
    }
    private fun String.prependZero(): String {
        return "0".plus(this)
    }

}