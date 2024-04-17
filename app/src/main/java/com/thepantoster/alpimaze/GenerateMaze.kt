package com.thepantoster.alpimaze


import kotlin.random.Random

class GenerateMaze(h:Int,w:Int) {
    var height:Int=h
    var width:Int=w
    var startPosition = arrayOf(0,0)
    var endPosition = arrayOf(0,0)
    var mazeLayout = Array(h) { Array<BlockType>(w) { BlockType.floor } }

}