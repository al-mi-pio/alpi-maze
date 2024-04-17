package com.thepantoster.alpimaze

import java.lang.IndexOutOfBoundsException
import kotlin.random.Random


class Maze(h:Int,w:Int,minLen:Int) {
    var height:Int=h
    var width:Int=w
    var startPosition = arrayOf(0,0)
    var endPosition = arrayOf(0,1)
    var length=minLen
    var mazeLayout = Array(h) { Array<BlockType>(w) { BlockType.wall } }

    fun generateMaze(){
        val sides= arrayOf(arrayOf(0,Random.nextInt(height-2)+1), arrayOf(width-1,Random.nextInt(height-2)+1), arrayOf(Random.nextInt(width-2)+1,0), arrayOf(Random.nextInt(width-2)+1,height-1))
        startPosition=sides[Random.nextInt(4)]
        endPosition=sides[Random.nextInt(4)]

        carvePath(startPosition[1],endPosition[0])
        mazeLayout[startPosition[1]][startPosition[0]]=BlockType.start
        mazeLayout[endPosition[1]][endPosition[0]]=BlockType.end
    }
    //1=floor
    //0=wall
    private fun carvePath(positionY:Int,positionX: Int){
        val directions= arrayOf(arrayOf(0,1), arrayOf(1,0), arrayOf(-1,0), arrayOf(0,-1))
        directions.shuffle()
        directions.forEach {
            var directionY=it[0]
            var directionX=it[1]
            if (positionX + directionX < width - 1 && positionX + directionX > 0 && positionY + directionY < height - 1 && positionY + directionY > 0 && mazeLayout[positionY + directionY][positionX + directionX] != BlockType.floor){
                try {


                    if (mazeLayout[positionY + directionY * 2][positionX + directionX * 2] != BlockType.floor && (mazeLayout[positionY + directionX][positionX + directionX] != BlockType.floor || directionX == 0) && (mazeLayout[positionY + directionY][positionX + directionY] != BlockType.floor || directionY == 0)) {


                        mazeLayout[positionY + directionY][positionX + directionX] = BlockType.floor

                        carvePath(positionY + directionY, positionX + directionX)
                    }
                }catch (e:IndexOutOfBoundsException) {
                    //pass
                }
            }

        }
    }
}