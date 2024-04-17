package com.thepantoster.alpimaze



import kotlin.Error
import kotlin.IndexOutOfBoundsException
import kotlin.random.Random


class Maze(h:Int,w:Int,minLen:Int) {
    var height:Int=h
    var width:Int=w
    var startPosition = arrayOf(0,0)
    var endPosition = arrayOf(0,1)
    var length=minLen
    var mazeLayout = Array(h) { Array<BlockType>(w) { BlockType.wall } }

    fun generateMaze(){
        val sides:Array<Array<Int>> = arrayOf(arrayOf(0,Random.nextInt(height-3)+1), arrayOf(width-1,Random.nextInt(height-3)+1), arrayOf(Random.nextInt(width-3)+1,0), arrayOf(Random.nextInt(width-3)+1,height-1))
        sides.shuffle()
        startPosition=sides[0]
        endPosition=sides[1]

        carvePath(startPosition[1],startPosition[0])
        var shortestPathLength=findShortestPath(startPosition,endPosition)

        while(length>shortestPathLength){
            println("faulty maze "+shortestPathLength)
            mazeLayout = Array(height) { Array<BlockType>(width) { BlockType.wall } }

            sides.shuffle()
            startPosition=sides[0]
            endPosition=sides[1]

            carvePath(startPosition[1],startPosition[0])
            shortestPathLength=findShortestPath(startPosition,endPosition)
        }
        length=shortestPathLength

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
    private fun highlightShortestPath(touchPoint:Array<Int>,movesFromStart:Array<Array<Array<Int>>>,movesFromEnd:Array<Array<Array<Int>>>,gen:Int,pos:Int){
        val directions= arrayOf(arrayOf(0,1), arrayOf(1,0), arrayOf(-1,0), arrayOf(0,-1))
        var touchPointSplitStart:Array<Int> = touchPoint
        var touchPointSplitEnd:Array<Int> = touchPoint
        var generationStart=gen
        var generationEnd=gen-pos


        for(i in 1..gen){

                var g:Boolean=false
                directions.forEach {

                    var dir:Array<Int> =it
                    if (movesFromStart[generationStart].any{it.contentDeepEquals(arrayOf(touchPointSplitStart[0] + dir[0], touchPointSplitStart[1] + dir[1]))} && g==false) {
                        touchPointSplitStart = arrayOf(touchPointSplitStart[0] + it[0], touchPointSplitStart[1] + it[1])
                        mazeLayout[touchPointSplitStart[0]][touchPointSplitStart[1]] = BlockType.shortPath
                        generationStart--
                        g=true
                    }


                }

            g=false
                directions.forEach {
                    var dir:Array<Int> =it
                    if (movesFromEnd[generationEnd].any{it.contentDeepEquals(arrayOf(touchPointSplitEnd[0] + dir[0], touchPointSplitEnd[1] + dir[1]))} && g==false) {
                        touchPointSplitEnd = arrayOf(touchPointSplitEnd[0] + it[0], touchPointSplitEnd[1] + it[1])
                        mazeLayout[touchPointSplitEnd[0]][touchPointSplitEnd[1]] = BlockType.shortPath
                        generationEnd--
                        g=true
                    }

                }

        }
    }
    private fun findShortestPath(start:Array<Int>,end:Array<Int>):Int{
        var movesFromStart:Array<Array<Array<Int>>> = emptyArray()
        var movesFromEnd:Array<Array<Array<Int>>> = emptyArray()
        movesFromStart+= arrayOf(arrayOf(start[1],start[0]))
        movesFromEnd+= arrayOf(arrayOf(end[1],end[0]))
        var generation:Int=0

        for(d in 0..height*width){

            val directions= arrayOf(arrayOf(0,1), arrayOf(1,0), arrayOf(-1,0), arrayOf(0,-1))
            var nextGenerationOfMoves:Array<Array<Int>> = emptyArray()

            for(i in 0..3) {

                movesFromStart[generation].forEach {
                    try {
                        if(mazeLayout[it[0] + directions[i][0]][it[1] + directions[i][1]] == BlockType.floor){
                            nextGenerationOfMoves += arrayOf(it[0] + directions[i][0], it[1] + directions[i][1])
                            mazeLayout[it[0] + directions[i][0]][it[1] + directions[i][1]] = BlockType.shortPathS
                        }else if(mazeLayout[it[0] + directions[i][0]][it[1] + directions[i][1]] == BlockType.shortPathE){
                            mazeLayout[it[0] + directions[i][0]][it[1] + directions[i][1]] = BlockType.shortPath
                            highlightShortestPath(arrayOf(it[0] + directions[i][0],it[1] + directions[i][1]),movesFromStart,movesFromEnd,generation,1)
                            return ((generation + 1) * 2)
                        }
                    }catch(e:IndexOutOfBoundsException){
                        //pass
                    }

                }
            }
            movesFromStart+=nextGenerationOfMoves
            nextGenerationOfMoves= emptyArray()
            for(i in 0..3) {

                movesFromEnd[generation].forEach {
                    try {
                        if(mazeLayout[it[0] + directions[i][0]][it[1] + directions[i][1]] == BlockType.floor){
                            nextGenerationOfMoves += arrayOf(it[0] + directions[i][0], it[1] + directions[i][1])
                            mazeLayout[it[0] + directions[i][0]][it[1] + directions[i][1]] = BlockType.shortPathE
                        }else if(mazeLayout[it[0] + directions[i][0]][it[1] + directions[i][1]] == BlockType.shortPathS){
                            mazeLayout[it[0] + directions[i][0]][it[1] + directions[i][1]] = BlockType.shortPath
                            highlightShortestPath(arrayOf(it[0] + directions[i][0],it[1] + directions[i][1]),movesFromStart,movesFromEnd,generation,0)
                            return ((generation + 1) * 2 -1)
                        }
                    }catch(e:IndexOutOfBoundsException){
                        //pass
                    }

                }
            }
            movesFromEnd+=nextGenerationOfMoves
            generation++
        }
        println(generation)
        return 0
    }


}