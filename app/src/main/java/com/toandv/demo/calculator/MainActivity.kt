package com.toandv.demo.calculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().run {
                replace(R.id.container_keyboard, KeyboardFragment.newInstance())
                replace(R.id.container_result, ResultFragment.newInstance())
                commit()
            }
        }
    }

}
