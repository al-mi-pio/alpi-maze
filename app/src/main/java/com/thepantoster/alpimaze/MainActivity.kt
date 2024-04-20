package com.thepantoster.alpimaze

import android.os.Bundle
import android.view.View
import kotlinx.coroutines.*
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

    }
    fun onStartGameHandle(view: View) {

        setContentView(R.layout.activity_game)


        val size = view.tag.toString().toInt()
        val myMaze = Maze(2000,2000,2000)

        GlobalScope.launch(Dispatchers.Default) {
            loadMaze(size,myMaze)
        }

    }

    suspend fun loadMaze(size: Int,myMaze:Maze) {




        myMaze.generateMaze()


        withContext(Dispatchers.Main) {
            println("------------8-------------")
            //myMaze.showMaze() - to be implemented
            myMaze.mazeLayout.forEach {
                println(it.joinToString().replace(" ","").replace("wall","#").replace("floor"," ").replace("shortPathE"," ").replace("shortPathS"," ").replace("shortPath","O").replace("start","S").replace("end","E").replace(",",""))
            }
        }
    }
    fun onGoHomeHandle(view: View) {
        setContentView(R.layout.activity_main)
    }
}