package org.cadnusdevs.sandroid.jwcongregationasignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.main.MainFragment

class MainActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}