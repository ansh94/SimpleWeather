package com.anshdeep.simpleweather.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.FrameLayout
import com.anshdeep.simpleweather.R
import com.anshdeep.simpleweather.ui.home.HomeFragment
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {

    private var content: FrameLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        content = findViewById(R.id.content)

        if (savedInstanceState == null) {
            val fragment = HomeFragment()
            addFragment(fragment)


        }
    }


    /**
     * add/replace fragment in container [FrameLayout]
     */
    private fun addFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.content, fragment, fragment.javaClass.simpleName)
                .commit()
    }
}
