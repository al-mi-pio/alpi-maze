package com.thepantoster.alpimaze

import android.content.Context
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat


class SingleBlock (context:Context,ts:Int,type:BlockType,maze:Maze,id:Int,rows:Int): AppCompatButton(context) {
    var size:Int=ts
    var bType:BlockType=type
    var X:Int=id%rows
    var Y:Int=(id-X)/rows
    var myMaze:Maze=maze
    val ID:Int= id

    @Override
    fun onClick() {

        when(bType){
            BlockType.wall->{

            }
            BlockType.floor->{
                bType=BlockType.selected
                myMaze.selectedPathList+= arrayOf(Y,X)
                setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.tile_selected))
                println("meow")
                invalidate()

            }
            BlockType.selected->{
                bType=BlockType.floor
                myMaze.selectedPathList.remove(arrayOf(Y,X))
                setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.tile_floor))
                invalidate()
            }
            else->{
                println("meow")
            }
        }
    }

    fun initialize() {
        maxWidth=size
        minWidth=size
        maxHeight=size
        minHeight=size
        id=ID
        tag=Y
        this.setOnClickListener { onClick() }
        if(bType==BlockType.wall){
            background=ContextCompat.getDrawable(context, R.drawable.tile_wall)
        }else {
            background = ContextCompat.getDrawable(context, R.drawable.tile_floor)
        }
            //tile.background = ContextCompat.getDrawable(this, R.drawable.tile_border)
    }
}