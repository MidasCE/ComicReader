package com.example.app.main.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.app.R
import com.example.app.main.comic.ComicScreenFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : MainView, AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        val newCurrentFragment = supportFragmentManager.findFragmentByTag("tag")
            ?: ComicScreenFragment()
        transaction.replace(R.id.fragment_container, newCurrentFragment, "tag")
        transaction.commit()
    }
}
