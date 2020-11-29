package aleksey.vasiliev.wolfandsheep.ais
import aleksey.vasiliev.wolfandsheep.chesspieces.Sheep
import aleksey.vasiliev.wolfandsheep.chesspieces.Wolf
import aleksey.vasiliev.wolfandsheep.helpers.Configuration
import aleksey.vasiliev.wolfandsheep.helpers.Graph
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.configuration
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.graph

class WolfAI(private val sheep: Sheep, private val wolves: MutableList<Wolf>) : AI {
    override fun move() {
        val maxOrMin = IntArray(1)
        if (configuration == Configuration.WOLF) maxOrMin[0] = Int.MIN_VALUE else Int.MAX_VALUE
        val outerIndex = IntArray(1)
        val turnIndex = IntArray(1)
        val wolvesPositions = wolves.map { it.node }
        val wolvesTurns = mutableListOf<List<Graph.Node>>()
        wolvesPositions.forEach {
            val possibleTurns = graph.optionsForMinimaxWolves(it).filter { one -> one !in wolvesPositions && one != sheep.node }
            wolvesTurns.add(possibleTurns)
        }
        runMinimax(Configuration.WOLF, 0, wolvesPositions, sheep.node, maxOrMin, outerIndex, turnIndex)
        wolves[outerIndex[0]].move(wolvesTurns[outerIndex[0]][turnIndex[0]])
    }

    private fun runMinimax(type: Configuration, depth: Int, wolvesPositions: List<Graph.Node>, sheep: Graph.Node, maxOrMin: IntArray, outerIndex: IntArray, turnIndex: IntArray) {
        if (type == Configuration.WOLF) {
            if (depth == 0) {
                for (index in wolvesPositions.indices) {
                    val possibleTurns = graph.optionsForMinimaxWolves(wolvesPositions[index]).filter { it !in wolvesPositions && it != sheep }
                    for (innerIndex in possibleTurns.indices) {
                        val newPosition = wolvesPositions.take(index) + possibleTurns[innerIndex] + wolvesPositions.subList(index + 1, 4) + listOf(graph.Node(index to innerIndex))
                        runMinimax(Configuration.SHEEP, depth + 1, newPosition, sheep, maxOrMin, outerIndex, turnIndex)
                    }
                }
            }
            for (index in 0..wolvesPositions.size - 2) {
                val possibleTurns = graph.optionsForMinimaxWolves(wolvesPositions[index]).filter { it !in wolvesPositions && it != sheep }
                for (innerIndex in possibleTurns.indices) {
                    val newPosition = wolvesPositions.take(index) + possibleTurns[innerIndex] + wolvesPositions.subList(index + 1, 4) + wolvesPositions.last()
                    if (depth < 6) {
                        runMinimax(Configuration.SHEEP, depth + 1, newPosition, sheep, maxOrMin, outerIndex, turnIndex)
                    } else {
                        val result = countCost(mutableSetOf(), listOf(sheep), newPosition.subList(0, 4), 0, 1)
                        if (result > maxOrMin[0]) {
                            maxOrMin[0] = result
                            outerIndex[0] = wolvesPositions.last().coordinates.first
                            turnIndex[0] = wolvesPositions.last().coordinates.second
                        }
                    }
                }
            }
        } else {
            val possibleTurns = graph.optionsForMinimax(sheep).filter { it !in wolvesPositions }
            for (innerIndex in possibleTurns.indices) {
                if (depth < 6) {
                    runMinimax(Configuration.WOLF, depth + 1, wolvesPositions, possibleTurns[innerIndex], maxOrMin, outerIndex, turnIndex)
                } else {
                    val result = countCost(mutableSetOf(), listOf(possibleTurns[innerIndex]), wolvesPositions.subList(0, 4), 0,1)
                    if (result < maxOrMin[0]) {
                        maxOrMin[0] = result
                    }
                }
            }
        }
    }

    private fun countCost(visited: MutableSet<Graph.Node>,
                                  toVisit: List<Graph.Node>,
                                  wolvesPositions: List<Graph.Node>,
                                  cost: Int,
                                  depth: Int): Int {
        visited.addAll(toVisit)
        val turns = mutableSetOf<Graph.Node>()
        toVisit.forEach { turns.addAll(graph.optionsForMinimax(it)) }
        val found = turns.filter { one -> one !in wolvesPositions && one !in visited }
        if (found.isEmpty()) return cost
        return countCost(visited, found, wolvesPositions, cost + found.size * depth, depth + 1)
    }
}