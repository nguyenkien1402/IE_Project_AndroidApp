package com.mobile.tiamo.activities
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.threetenabp.AndroidThreeTen
import com.mobile.tiamo.R
import com.mobile.tiamo.adapters.ActivityModelItem
import com.mobile.tiamo.adapters.DailyRoutineItem
import de.tobiasschuerg.weekview.data.EventConfig
import de.tobiasschuerg.weekview.view.EventView
import de.tobiasschuerg.weekview.view.WeekView
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalTime
import java.util.*

class WeeklyCalendarViewActivity : AppCompatActivity() {

    private val weekView: WeekView by lazy { findViewById<WeekView>(R.id.week_view) }

    private val weekDays = DayOfWeek.values()
            // Filter the weekend
            .filter { it != DayOfWeek.SATURDAY }
            .filter { it != DayOfWeek.SUNDAY }
    private val random = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidThreeTen.init(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weekly_view)

        val config = EventConfig(showSubtitle = false, showTimeEnd = false)
        weekView.eventConfig = config

        // set up the WeekView with the data
        weekView.addEvents(EventCreator.weekData)
//        var routines = intent.getParcelableArrayListExtra<Parcelable>("routine") as List<DailyRoutineItem>
//        var activities = intent.getParcelableArrayListExtra<Parcelable>("activity") as List<ActivityModelItem>
//        for(d in routines){
//            var startTime = LocalTime.of(23 , 0)
//            var endTime = LocalTime.of(23,59)
//            var event = EventCreator.createEvent(d.title,DayOfWeek.MONDAY,startTime, endTime)
//            weekView.addEvent(event)
//        }
        // optional: add an onClickListener for each event
        weekView.setLessonClickListener {
            Toast.makeText(applicationContext, "Removing " + it.event.title, Toast.LENGTH_SHORT).show()
            weekView.removeView(it)
        }

        // optional: register a context menu to each event
        registerForContextMenu(weekView)

        weekView.setOnTouchListener { v, event ->
            when (event.pointerCount) {
                1 -> {
                    v.parent.requestDisallowInterceptTouchEvent(false)
                }
                2 -> {
                    v.parent.requestDisallowInterceptTouchEvent(true)
                }
            }
            false
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo) {
        val (event) = menuInfo as EventView.LessonViewContextInfo
        menu.setHeaderTitle(event.title)
        menu.add("First Option")
        menu.add("Second Option")
        menu.add("Third Option")
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add("Add").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        menu.add("Clear").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.title) {
            "Add" -> addRandomItem()
            "Clear" -> removeAllEvents()
        }
        return true
    }

    private fun removeAllEvents() {
        Log.i(TAG, "removeAllEvents()")
        weekView.removeViews(1, weekView.childCount - 1)
    }

    private fun addRandomItem() {
        Log.i(TAG, "addRandomItem()")
        val newEvent = EventCreator.createRandomEvent()
        weekView.addEvent(newEvent)
    }

    companion object {
        private const val TAG = "WeeklyCalendarViewActivity"
    }
}
