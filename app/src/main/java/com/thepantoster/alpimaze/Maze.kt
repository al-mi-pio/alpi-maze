package com.thepantoster.alpimaze



import kotlin.IndexOutOfBoundsException
import kotlin.random.Random


class Maze(h:Int,w:Int,minLen:Int) {
    var height:Int=h
    var width:Int=w
    var startPosition = arrayOf(0,0)
    var endPosition = arrayOf(0,1)
    var length=minLen
    var mazeLayout = Array(h) { Array<BlockType>(w) { BlockType.wall } }
    val directions= arrayOf(arrayOf(0,1), arrayOf(1,0), arrayOf(-1,0), arrayOf(0,-1))
    var shortestPathList = mutableListOf<Array<Int>>()
    var selectedPathList= mutableListOf<Array<Int>>()
    var undoHistory= mutableListOf<Array<Int>>()
    fun generateMaze(){
        val sides:Array<Array<Int>> = arrayOf(arrayOf(0,Random.nextInt(height-3)+1), arrayOf(width-1,Random.nextInt(height-3)+1), arrayOf(Random.nextInt(width-3)+1,0), arrayOf(Random.nextInt(width-3)+1,height-1))
        sides.shuffle()
        startPosition=sides[0]
        var currLength=carveShortestPath(startPosition[1],startPosition[0], length )
        while(currLength<length){
            mazeLayout = Array(height) { Array<BlockType>(width) { BlockType.wall } }
            shortestPathList = mutableListOf<Array<Int>>()
            currLength=carveShortestPath(startPosition[1],startPosition[0],length)
        }
        length=currLength
        endPosition=shortestPathList.pop()
        carveFullPath()
        mazeLayout[startPosition[1]][startPosition[0]]=BlockType.start
        mazeLayout[endPosition[0]][endPosition[1]]=BlockType.end
    }
    private fun carveFullPath() {
        var initialStack= mutableListOf<Array<Int>>()
        initialStack.addAll(shortestPathList)
        while (initialStack.isNotEmpty()) {
            val (positionY, positionX) = initialStack.pop()
            directions.shuffle()
            for(i in 0..3) {
                val directionY = directions[i][0]
                val directionX = directions[i][1]
                if (positionX + directionX < width - 1 && positionX + directionX > 0 && positionY + directionY < height - 1 && positionY + directionY > 0 && mazeLayout[positionY + directionY][positionX + directionX] != BlockType.floor) {
                    try {
                        if (mazeLayout[positionY + directionY * 2][positionX + directionX * 2] != BlockType.floor &&
                            (mazeLayout[positionY + directionX][positionX + directionX] != BlockType.floor || directionX == 0) &&
                            (mazeLayout[positionY + directionY][positionX + directionY] != BlockType.floor || directionY == 0) &&
                            (mazeLayout[positionY - directionX][positionX + directionX] != BlockType.floor || directionX == 0) &&
                            (mazeLayout[positionY + directionY][positionX - directionY] != BlockType.floor || directionY == 0)

                        ) {
                            mazeLayout[positionY + directionY][positionX + directionX] = BlockType.floor
                            initialStack.push(arrayOf(positionY + directionY, positionX + directionX))
                            if(i>0){
                                break
                            }
                        }
                    } catch (e: IndexOutOfBoundsException) {
                        //pass
                    }
                }
            }
        }
    }
    private fun <T> MutableList<T>.push(item: T) {
        add(item)
    }
    fun <T> MutableList<T>.pop(): T {
        return removeAt(lastIndex)
    }
    private fun carveShortestPath(y:Int,x:Int,minLen:Int):Int {
        var length: Int = 0
        var positionY: Int = y
        var positionX: Int = x
        tW@while (true) {
            directions.shuffle()
            dFor@for (i in 0..3) {
                val directionY = directions[i][0]
                val directionX = directions[i][1]
                if (positionX + directionX < width - 1 && positionX + directionX > 0 && positionY + directionY < height - 1 && positionY + directionY > 0) {
                    if (mazeLayout[positionY + directionY][positionX + directionX] != BlockType.floor && canItTurn(directionX,directionY,positionX,positionY)) {
                        try {
                            if (mazeLayout[positionY + directionY * 2][positionX + directionX * 2] != BlockType.floor &&
                                (mazeLayout[positionY + directionX][positionX + directionX] != BlockType.floor || directionX == 0) &&
                                (mazeLayout[positionY + directionY][positionX + directionY] != BlockType.floor || directionY == 0) &&
                                (mazeLayout[positionY - directionX][positionX + directionX] != BlockType.floor || directionX == 0) &&
                                (mazeLayout[positionY + directionY][positionX - directionY] != BlockType.floor || directionY == 0)
                            ) {
                                mazeLayout[positionY + directionY][positionX + directionX] = BlockType.floor
                                positionX = positionX + directionX
                                positionY = positionY + directionY
                                shortestPathList.push(arrayOf(positionY , positionX))
                                length++
                                break@dFor
                            }
                        } catch (e: IndexOutOfBoundsException) {
                            //pass
                        }
                    }
                }
                else if(length>minLen){
                    shortestPathList.push(
                        arrayOf(
                            positionY + directionY,
                            positionX + directionX
                        )
                    )
                    break@tW
                }
            if(i==3){
                return 0
            }
            }
        }
        return length
    }
    private fun canItTurn(dx:Int,dy:Int,px:Int,py:Int):Boolean{
        var result:Boolean=true
        var positionX=px
        var positionY=py
        while(true) {
            try {
                if(mazeLayout[positionY+dy][positionX+dx]==BlockType.floor){
                    result=false
                    break
                }
                positionX+=dx
                positionY+=dy
            } catch (e: IndexOutOfBoundsException) {
                break
            }
        }

        return result
    }
    fun checkSolution():Int{
        var lengthOfPath=selectedPathList.size
        if(checkPath(startPosition[1],startPosition[0])) {
            return lengthOfPath
        }
        else {
            return 0
        }
    }
    private  fun checkPath(y:Int,x:Int):Boolean{
        var solved:Boolean = false
        directions.forEach {
            if(endPosition.contentEquals(arrayOf(y+it[0],x+it[1]))){
                return true
            }
            if(containsCoordinates(selectedPathList,arrayOf(y+it[0],x+it[1]))){
                removeCoordinates(selectedPathList,arrayOf(y+it[0],x+it[1]))
                solved=checkPath(y+it[0],x+it[1])
            }
        }
        return solved
    }
    fun removeCoordinates(list: MutableList<Array<Int>>, coordinates: Array<Int>) {
        list.removeIf { it.contentEquals(coordinates) }
    }
    fun containsCoordinates(list: List<Array<Int>>, coordinates: Array<Int>): Boolean {
        return list.any { it.contentEquals(coordinates) }
    }
}