package com.thepantoster.alpimaze

import kotlin.random.Random


class Maze(h:Int,w:Int,minLen:Int) {
    var height:Int=h
    var width:Int=w
    var startPosition = arrayOf(0,0)
    var endPosition = arrayOf(0,1)
    var length=minLen
    var mazeLayout = Array(h) { Array<BlockType>(w) { BlockType.floor } }

    fun generateMaze(){
        val sides= arrayOf(arrayOf(0,Random.nextInt(height-2)+1), arrayOf(width-1,Random.nextInt(height-2)+1), arrayOf(Random.nextInt(width-2)+1,0), arrayOf(Random.nextInt(width-2)+1,height-1))
        startPosition=sides[Random.nextInt(4)]
        endPosition=sides[Random.nextInt(4)]


        mazeLayout[startPosition[1]][startPosition[0]]
        mazeLayout[endPosition[1]][endPosition[0]]
    }

}