package com.mobile.tiamo.questionaires

import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import com.mobile.tiamo.R
import me.adawoud.bottomsheettimepicker.BottomSheetTimeRangePicker
import me.adawoud.bottomsheettimepicker.OnTimeRangeSelectedListener

class FirstQuestionFragment : Fragment(),OnTimeRangeSelectedListener{
    private val tagBottomSheetTimeRangePicker = "tagBottomSheetTimeRangePicker"

    companion object{
        var time: String? =  null
        var day = ArrayList<String>()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.viewpager_question_1,container,false)
        val setWorkingTime = view.findViewById<TextView>(R.id.questionaries_working_time)
        val chipGroup = view.findViewById<ChipGroup>(R.id.chipFirstGroup)

        for(i in 0 until chipGroup.childCount){
            val chip = chipGroup.getChildAt(i) as Chip
            chip.setOnCheckedChangeListener{buttonView, isChecked ->
                if(isChecked){
                    day.add(chip.text.toString())
                }else{
                    day.remove(chip.text.toString())
                }
            }
        }

        setWorkingTime.setOnClickListener{
            BottomSheetTimeRangePicker
                    .newInstance(this, DateFormat.is24HourFormat(activity))
                    .show(activity?.supportFragmentManager, tagBottomSheetTimeRangePicker)
        }
        return view
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
            time = tvTimeRange.text.toString()
        }
    }
    private fun String.prependZero(): String {
        return "0".plus(this)
    }

}