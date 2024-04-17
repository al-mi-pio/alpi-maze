package com.thepantoster.alpimaze

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var myMaze:Maze=Maze(20,20,1)
        myMaze.generateMaze()
        myMaze.mazeLayout.forEach {


            println(it.joinToString().replace("wall","#").replace("floor"," ").replace("shortPathE"," ").replace("shortPathS"," ").replace("shortPath","O").replace("start","S").replace("end","E"))


        }
        println("-------------------------")

    }
}