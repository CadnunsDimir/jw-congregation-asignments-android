package org.cadnusdevs.sandroid.jwcongregationasignment

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.EditAssignmentsFragment
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.ListBrotherFragment
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.MainFragment
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.QueryViews

class MainActivity : AppCompatActivity() {
    private lateinit var q: QueryViews

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setViewConfigs(savedInstanceState)
        setEvents()
    }

    private fun setViewConfigs(savedInstanceState: Bundle?) {
        q = QueryViews(getRootView())
        if (savedInstanceState == null) {
            q.openFragment(MainFragment.newInstance(), true)
        }
    }

    private fun setEvents() {
        val colorSelected = Color.parseColor("#FF6200EE")
        val colorUnselected = Color.parseColor("#FFBB86FC")

        q.onClick(R.id.menu_button_brothers) {
            it.setBackgroundColor(colorSelected)
            q.find<View>(R.id.menu_button_assignments)?.setBackgroundColor(colorUnselected)
            q.openFragment(ListBrotherFragment.newInstance())
        }
        q.onClick(R.id.menu_button_assignments) {
            it.setBackgroundColor(colorSelected)
            q.find<View>(R.id.menu_button_brothers)?.setBackgroundColor(colorUnselected)
            q.openFragment(EditAssignmentsFragment())
        }
    }

    private fun getRootView(): View {
        return findViewById<View?>(android.R.id.content).findViewById(R.id.activity_main_layout_view)
    }
}