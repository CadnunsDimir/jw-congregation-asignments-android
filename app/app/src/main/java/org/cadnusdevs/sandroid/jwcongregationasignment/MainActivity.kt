package org.cadnusdevs.sandroid.jwcongregationasignment

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.EditAssignmentsFragment
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.ListBrotherFragment
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.MainFragment
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.QueryViews
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.shared.BaseFragment

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
        q.onClick(R.id.menu_button_brothers) {
            selectFragment(ListBrotherFragment.newInstance())
        }
        q.onClick(R.id.menu_button_assignments) {
            selectFragment(EditAssignmentsFragment())
        }
    }

    private fun selectFragment(fragment: BaseFragment) {
        q.openFragment(fragment,true)
        setButtonColors(fragment)
    }

    private fun setButtonColors(currentFragment: Fragment) {
        val colorUnselected = "#FFBB86FC"
        val colorSelected = "#FF6200EE"

        q.setColor(R.id.menu_button_assignments,
            if(currentFragment is EditAssignmentsFragment) colorSelected else colorUnselected)
        q.setColor(R.id.menu_button_brothers,
            if(currentFragment is ListBrotherFragment) colorSelected else colorUnselected)
    }

    private fun getRootView(): View {
        return findViewById<View?>(android.R.id.content).findViewById(R.id.activity_main_layout_view)
    }
}