package com.mobile.tiamo.questionaires

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.appsci.sleep.timepicker.SleepTimePicker
import com.jakewharton.threetenabp.AndroidThreeTen
import com.mobile.tiamo.R
import org.threeten.bp.Duration
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import timber.log.Timber
import java.util.*
import org.threeten.bp.format.DateTimeFormatter
import org.w3c.dom.Text

class ThirdQuestionFragment : Fragment(){

    companion object{
        var sleepTime: String? = null
        var wakeupTime: String? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.viewpager_question_3,container,false)
        AndroidThreeTen.init(activity)
        val timePicker = view.findViewById<SleepTimePicker>(R.id.timePicker)
        val tvBedTime  = view.findViewById<TextView>(R.id.tvBedTime)
        val tvWakeTime = view.findViewById<TextView>(R.id.tvWakeTime)
        val tvHours = view.findViewById<TextView>(R.id.tvHours)
        val tvMins = view.findViewById<TextView>(R.id.tvMins)
        val llMins = view.findViewById<LinearLayout>(R.id.llMins)
        sleepTime = "23:00"
        wakeupTime = "07:00"
        timePicker.setTime(LocalTime.of(23, 0), LocalTime.of(7, 0))

        timePicker.listener = { bedTime: LocalTime, wakeTime: LocalTime ->
            Timber.d("time changed \nbedtime= $bedTime\nwaketime=$wakeTime")
            val formatter = DateTimeFormatter.ofPattern("hh:mm", Locale.US)
            val anotherFormat = DateTimeFormatter.ofPattern("hh:mm")
            tvBedTime.text = bedTime.format(formatter)
            tvWakeTime.text = wakeTime.format(formatter)
            sleepTime = bedTime.format(anotherFormat)
            wakeupTime = wakeTime.format(anotherFormat)
            val bedDate = bedTime.atDate(LocalDate.now())
            var wakeDate = wakeTime.atDate(LocalDate.now())
            if (bedDate >= wakeDate) wakeDate = wakeDate.plusDays(1)
            val duration = Duration.between(bedDate, wakeDate)
            val hours = duration.toHours()
            val minutes = duration.toMinutes() % 60
            tvHours.text = hours.toString()
            tvMins.text = minutes.toString()
            if (minutes > 0) llMins.visibility = View.VISIBLE else llMins.visibility = View.GONE
        }
        val formatter = DateTimeFormatter.ofPattern("hh:mm", Locale.US)
        tvBedTime.text = timePicker.getBedTime().format(formatter)
        tvWakeTime.text = timePicker.getWakeTime().format(formatter)

        val bedDate = timePicker.getBedTime().atDate(LocalDate.now())
        var wakeDate = timePicker.getWakeTime().atDate(LocalDate.now())
        if (bedDate >= wakeDate) wakeDate = wakeDate.plusDays(1)
        val duration = Duration.between(bedDate, wakeDate)
        val hours = duration.toHours()
        val minutes = duration.toMinutes() % 60
        tvHours.text = hours.toString()
        tvMins.text = minutes.toString()

        if (minutes > 0) llMins.visibility = View.VISIBLE else llMins.visibility = View.GONE
        return view
    }


}