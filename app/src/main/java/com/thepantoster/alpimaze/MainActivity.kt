package com.thepantoster.alpimaze


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import kotlinx.coroutines.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.concurrent.timer


class MainActivity : AppCompatActivity() {
    private var myMaze:Maze?=null
    private var usedSize:Int=0
    private var counter = 0
    private var counterJob: Job? = null
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
    fun onStartGameHandle(view: View){

        setContentView(R.layout.activity_game)


        usedSize = view.tag.toString().toInt()

        myMaze = Maze(usedSize,usedSize,usedSize)



        loadMaze(usedSize, myMaze!!)



    }

    fun loadMaze(size: Int,myMaze:Maze) {
        counter=0
        var rows: Int = size
        var cols: Int = size
        var tileSize: Int = 100
        var ID:Int=0
        val maze: TableLayout =  findViewById(R.id.mazeView)
        var mazeRow: TableRow
        var tile: Button
        println("meow")
        myMaze.generateMaze()
        for (i in 0..<rows){
            mazeRow = TableRow(this)
            mazeRow.setLayoutParams(
                TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.MATCH_PARENT
                )
            )

            for (j in 0..<cols){


                tile = SingleBlock(this,100,myMaze.mazeLayout[i][j],myMaze,ID,size,findViewById(R.id.score))
                ID++
                tile.initialize()
                mazeRow.addView(tile)
            }
            maze.addView(mazeRow, TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT
            )
            )
        }

        startCounter()


    }
    //this will be used on the return in new activity/view
    fun onGoHomeHandle(view: View) {
        counterJob?.cancel()
        setContentView(R.layout.activity_main)
    }
    fun onGoAgainHandle(view:View){
        myMaze = Maze(usedSize,usedSize,usedSize)
        var toRemove:TableLayout=findViewById(R.id.mazeView)
        toRemove.removeAllViews()
        loadMaze(usedSize, myMaze!!)
        counter=0
    }
    fun onDoneHandle(view:View){
        val solved:Int?= myMaze?.checkSolution()
        val shortestPath:Int? =myMaze?.length
        if (solved != null) {
            if(solved>0){
                // implement the showing of new activity
                println("solved meow")
            }else{
                // implement the showing of new activity
                println("FAILURE")
            }
        }
    }
    @SuppressLint("SetTextI18n")
    private fun startCounter() {

        counterJob = CoroutineScope(Dispatchers.Main).launch {
            while (true) {

                counter++
                var secs:String= (counter%60).toString()
                var mins:String= ((counter-counter%60)/60).toString()

                if(secs.length==1){
                    secs="0"+secs
                }
                if(mins.length==1){
                    mins="0"+mins
                }

                findViewById<TextView>(R.id.time).text = mins+":"+secs

                delay(1000)
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()

        counterJob?.cancel()
    }
    fun onHintHandle(view:View){
        // add an ad :D

        myMaze?.shortestPathList?.shuffle()
        for(i:Array<Int> in myMaze?.shortestPathList!!){
            if(!myMaze!!.containsCoordinates(myMaze!!.selectedPathList,i)){
                var hintID:Int=i[0]* myMaze!!.width+i[1]
                findViewById<SingleBlock>(hintID).setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.tile_hinted))
                break
            }
        }
    }
    fun onUndoHandle(view:View){
        try {
            var undo = myMaze?.undoHistory?.last()
            if(undo!=null) {
                if (undo[1] == 1) {
                    findViewById<SingleBlock>(undo[0]).selectClick(true)
                } else {
                    findViewById<SingleBlock>(undo[0]).floorClick(true)
                }
                myMaze?.undoHistory?.remove(undo)
            }
        }catch (e:NoSuchElementException){
            //pass
        }



    }

}