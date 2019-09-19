package ru.mapublish.multiplicationtable


import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    var isBackPressed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /**
     * closes app by a double Back press
     */
//    override fun onBackPressed() {
//        if (isBackPressed) {
//            val fragmentManager = supportFragmentManager
//            val startFragment = StartFragment()
//            val fragmentTransaction = fragmentManager.beginTransaction()
//            fragmentTransaction.replace(R.layout.activity_main, startFragment)
//            fragmentTransaction.commit()
//        }
//
//        isBackPressed = true
//        Toast.makeText(this, "Press Back again to exit", Toast.LENGTH_SHORT).show()
//        Handler().postDelayed({ isBackPressed = false }, 2000)
//    }
}
