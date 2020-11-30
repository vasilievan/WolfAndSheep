package tests

import aleksey.vasiliev.wolfandsheep.helpers.Graph
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.graph
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.playerWon
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.setScreen
import aleksey.vasiliev.wolfandsheep.screens.TheEnd

class WolfAIWrapper(private val sheep: SheepWrapper, private val wolves: MutableList<WolfWrapper>) {
    data class Case(val wolvesPosition: List<Graph.Node>, val sheepPosition: Graph.Node, val wolfIndex: Int, val turn: Graph.Node)

    fun move() {
        if (sheep.node.coordinates.first == 7) {
            playerWon = true
            setScreen(TheEnd())
            return
        }
        val wolvesPositions = wolves.map { it.node }
        val isSpecialCase = checkInSpecialCases(wolvesPositions, sheep.node)
        if (isSpecialCase) return
        val wolvesTurns = mutableListOf<List<Graph.Node>>()
        wolvesPositions.forEach {
            val possibleTurns = graph.optionsForWolf(it).filter { one -> one !in wolvesPositions && one != sheep.node }
            wolvesTurns.add(possibleTurns)
        }
        var maximum = Int.MIN_VALUE
        var toGo = -1 to -1
        for (index in wolvesTurns.indices) {
            for (innerIndex in wolvesTurns[index].indices) {
                val position = wolvesPositions.take(index) + wolvesTurns[index][innerIndex] + wolvesPositions.subList(index + 1, 4)
                val cost = countCost(mutableSetOf(), listOf(sheep.node), position, 1)
                if (cost > maximum) {
                    maximum = cost
                    toGo = index to innerIndex
                }
            }
        }
        if (toGo == -1 to -1) {
            playerWon = true
            setScreen(TheEnd())
            return
        }
        wolves[toGo.first].move(wolvesTurns[toGo.first][toGo.second])
        val possibleTurns = graph.options(sheep.node).filter { it !in wolves.map { one -> one.node } }
        if (possibleTurns.isEmpty()) {
            playerWon = false
            setScreen(TheEnd())
        }
    }

    private fun countCost(visited: MutableSet<Graph.Node>,
                          toVisit: List<Graph.Node>,
                          wolvesPositions: List<Graph.Node>,
                          depth: Int): Int {
        visited.addAll(toVisit)
        val turns = mutableSetOf<Graph.Node>()
        toVisit.forEach { turns.addAll(graph.options(it)) }
        val found = turns.filter { one -> one !in wolvesPositions && one !in visited }
        if (found.isEmpty()) return Int.MAX_VALUE
        for (wolf in wolvesPositions) {
            for (sheepTurn in found) {
                if (sheepTurn in graph.optionsForSheep(wolf)) {
                    val possibleMoves = graph.optionsForSheep(sheepTurn).filter { it !in wolvesPositions }
                    if (possibleMoves.isNotEmpty()) return depth
                }
            }
        }
        return countCost(visited, found, wolvesPositions, depth + 1)
    }

    private val specialCases = mutableSetOf(
            Case(listOf(graph.Node(5 to 0), graph.Node(6 to 1), graph.Node(6 to 2), graph.Node(7 to 3)), graph.Node(5 to 3), 3, graph.Node(6 to 3)),
            Case(listOf(graph.Node(5 to 0), graph.Node(5 to 1), graph.Node(6 to 2), graph.Node(6 to 3)), graph.Node(5 to 2), 0, graph.Node(4 to 1)),
            Case(listOf(graph.Node(5 to 0), graph.Node(6 to 1), graph.Node(6 to 2), graph.Node(7 to 3)), graph.Node(5 to 2), 3, graph.Node(6 to 3)),
            Case(listOf(graph.Node(4 to 1), graph.Node(5 to 1), graph.Node(6 to 2), graph.Node(6 to 3)), graph.Node(4 to 2), 3, graph.Node(5 to 2)),
            Case(listOf(graph.Node(4 to 1), graph.Node(5 to 1), graph.Node(6 to 2), graph.Node(5 to 2)), graph.Node(3 to 2), 3, graph.Node(4 to 3)),
            Case(listOf(graph.Node(4 to 1), graph.Node(5 to 1), graph.Node(5 to 2), graph.Node(4 to 3)), graph.Node(3 to 1), 2, graph.Node(4 to 2)),
            Case(listOf(graph.Node(4 to 1), graph.Node(5 to 1), graph.Node(4 to 2), graph.Node(4 to 3)), graph.Node(2 to 1), 0, graph.Node(3 to 0)),
            Case(listOf(graph.Node(4 to 1), graph.Node(5 to 1), graph.Node(6 to 2), graph.Node(5 to 2)), graph.Node(3 to 1), 3, graph.Node(4 to 2)),
            Case(listOf(graph.Node(4 to 1), graph.Node(5 to 1), graph.Node(4 to 2), graph.Node(3 to 2)), graph.Node(1 to 3), 3, graph.Node(2 to 3)),
            Case(listOf(graph.Node(4 to 1), graph.Node(5 to 1), graph.Node(6 to 2), graph.Node(4 to 3)), graph.Node(2 to 1), 0, graph.Node(3 to 0)),
            Case(listOf(graph.Node(3 to 0), graph.Node(5 to 1), graph.Node(6 to 2), graph.Node(4 to 3)), graph.Node(3 to 1), 1, graph.Node(4 to 1)),
            Case(listOf(graph.Node(4 to 1), graph.Node(5 to 1), graph.Node(6 to 2), graph.Node(4 to 2)), graph.Node(2 to 1), 0, graph.Node(3 to 0)),
            Case(listOf(graph.Node(3 to 1), graph.Node(4 to 2), graph.Node(5 to 2), graph.Node(4 to 3)), graph.Node(1 to 1), 0, graph.Node(2 to 1)),
            Case(listOf(graph.Node(2 to 1), graph.Node(4 to 2), graph.Node(5 to 2), graph.Node(4 to 3)), graph.Node(2 to 2), 1, graph.Node(3 to 1)),
            Case(listOf(graph.Node(4 to 1), graph.Node(5 to 1), graph.Node(4 to 2), graph.Node(3 to 2)), graph.Node(1 to 2), 3, graph.Node(2 to 3)),
            Case(listOf(graph.Node(4 to 1), graph.Node(5 to 1), graph.Node(4 to 2), graph.Node(2 to 2)), graph.Node(2 to 1), 0, graph.Node(3 to 0)),
            Case(listOf(graph.Node(3 to 0), graph.Node(0 to 2), graph.Node(2 to 2), graph.Node(2 to 3)), graph.Node(0 to 3), 2, graph.Node(1 to 2)),
            Case(listOf(graph.Node(2 to 2), graph.Node(3 to 2), graph.Node(4 to 3), graph.Node(6 to 3)), graph.Node(0 to 2), 0, graph.Node(1 to 1)),
            Case(listOf(graph.Node(1 to 1), graph.Node(3 to 2), graph.Node(4 to 3), graph.Node(6 to 3)), graph.Node(1 to 2), 1, graph.Node(2 to 2)),
            Case(listOf(graph.Node(2 to 2), graph.Node(2 to 3), graph.Node(4 to 3), graph.Node(5 to 3)), graph.Node(0 to 2), 0, graph.Node(1 to 1))
    )

    private fun checkInSpecialCases(wolvesPosition: List<Graph.Node>, sheepPosition: Graph.Node): Boolean {
        for (case in specialCases) {
            if (case.wolvesPosition.zip(wolvesPosition).all { (first, second) -> first == second && sheepPosition == case.sheepPosition }) {
                wolves[case.wolfIndex].move(case.turn)
                return true
            }
        }
        return false
    }
}

