package aleksey.vasiliev.wolfandsheep.ais

import aleksey.vasiliev.wolfandsheep.chesspieces.Sheep
import aleksey.vasiliev.wolfandsheep.chesspieces.Wolf
import aleksey.vasiliev.wolfandsheep.helpers.Graph
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.graph
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.playerWon
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.setScreen
import aleksey.vasiliev.wolfandsheep.screens.TheEnd

class WolfAI(private val sheep: Sheep, private val wolves: MutableList<Wolf>) : AI {
    override fun move() {
        val wolvesPositions = wolves.map { it.node }
        val sheepTurns = graph.optionsForMinimax(sheep.node).filter { it !in wolvesPositions }
        wolvesWon(sheepTurns)
        val wolvesTurns = mutableListOf<List<Graph.Node>>()
        wolvesPositions.forEach {
            val toAdd = graph.optionsForMinimaxWolves(it).filter { one -> one !in wolvesPositions && one != sheep.node }
            wolvesTurns.add(toAdd)
        }
        val costs = MutableList<MutableList<Int>>(wolvesPositions.size) { mutableListOf() }
        for (index in wolvesTurns.indices) {
            for (innerIndex in wolvesTurns[index].indices) {
                costs[index].add(0)
                val combination = (wolvesPositions.take(index) + wolvesTurns[index][innerIndex] + wolvesPositions.subList(index, wolvesTurns.size)).toSet()
                val visited = mutableSetOf<Graph.Node>()
                val toVisit = mutableSetOf(sheep.node)
                countCost(visited, toVisit, combination, costs[index], innerIndex, sheep, 1)
            }
        }
        var maximum = Int.MIN_VALUE
        var currentIndex = -1 to -1
        for (index in wolvesTurns.indices) {
            for (innerIndex in wolvesTurns[index].indices) {
                if (costs[index][innerIndex] > maximum) {
                    maximum = costs[index][innerIndex]
                    currentIndex = index to innerIndex
                }
            }
        }
        if (currentIndex.first == -1 && currentIndex.second == -1) {
            playerWon = true
            setScreen(TheEnd())
            return
        }
        wolves[currentIndex.first].move(wolvesTurns[currentIndex.first][currentIndex.second])
        playerWon()
    }

    private fun countCost(visited: MutableSet<Graph.Node>,
                          toVisit: MutableSet<Graph.Node>,
                          wolvesPositions: Set<Graph.Node>,
                          costs: MutableList<Int>,
                          innerIndex: Int,
                          sheep: Sheep,
                          depth: Int) {
        visited.addAll(toVisit)
        val turns = mutableSetOf<Graph.Node>()
        toVisit.forEach {
            turns.addAll(graph.optionsForMinimax(it))
        }
        val found = turns.filter { one -> one !in wolvesPositions && one !in visited}.toMutableSet()
        if (found.isEmpty()) return
        costs[innerIndex] += found.size * depth
        val newDepth = depth + 1
        return countCost(visited, found, wolvesPositions, costs, innerIndex, sheep, newDepth)
    }

    // у овечки не осталось путей
    private fun wolvesWon(turns: List<Graph.Node>) {
        if (turns.isEmpty()) {
            playerWon = false
            setScreen(TheEnd())
        }
    }

    // овечка дошла до конца
    private fun playerWon() {
        if (sheep.node.coordinates.first == 7) {
            playerWon = true
            setScreen(TheEnd())
        }
    }
}