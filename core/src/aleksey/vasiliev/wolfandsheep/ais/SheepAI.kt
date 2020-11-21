package aleksey.vasiliev.wolfandsheep.ais

import aleksey.vasiliev.wolfandsheep.chesspieces.Sheep
import aleksey.vasiliev.wolfandsheep.chesspieces.Wolf
import aleksey.vasiliev.wolfandsheep.helpers.Graph
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.graph
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.playerWon
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.setScreen
import aleksey.vasiliev.wolfandsheep.screens.TheEnd

class SheepAI(private val sheep: Sheep, private val wolves: MutableSet<Wolf>): AI {
    init {
        move()
    }

    override fun move() {
        val wolvesPositions = wolves.map { it.node }.toSet()
        val turns = graph.optionsForMinimax(sheep.node).filter { it !in wolvesPositions }
        playerWon(turns)
        val costs = IntArray(turns.size)
        val ends = IntArray(turns.size) { Int.MAX_VALUE }
        for (index in turns.indices) {
            val visited = mutableSetOf<Graph.Node>()
            val toVisit = mutableSetOf(turns[index])
            toVisit.add(sheep.node)
            countCost(visited, toVisit, wolvesPositions, costs, index, sheep, ends, 1)
        }
        val shortest = ends.min()
        // если край доски достижим
        if (shortest != Int.MAX_VALUE && shortest != null) {
            // случай, когда овечка в 1 ходе от выигрыша
            if (ends.any { it == 1 }) {
                val theTurn = turns.firstOrNull {
                    graph.optionsForMinimax(it).any {
                        one -> one !in wolvesPositions && one.coordinates.first == 0
                    }
                } ?: turns.first { it.coordinates.first == 0 && it !in wolvesPositions }
                sheep.move(theTurn)
            } else {
                sheep.move(turns[ends.indexOfFirst { it == shortest }])
            }
        } else {
            // иначе выбраю вариант с наименьшей стоимостью
            val minimal = costs.min() ?: playerWon(turns)
            val index = costs.indexOfFirst { it == minimal }
            if (index == -1) {
                playerWon = true
                setScreen(TheEnd())
                return
            }
            sheep.move(turns[index])
        }
        sheepWon()
    }

    private fun sheepWon() {
        if (sheep.node.coordinates.first == 0) {
            playerWon = false
            setScreen(TheEnd())
        }
    }

    private fun playerWon(turns: List<Graph.Node>) {
        if (turns.isEmpty()) {
            playerWon = true
            setScreen(TheEnd())
            return
        }
    }

    // граф заполняется стоимостями.
    private fun countCost(visited: MutableSet<Graph.Node>,
                          toVisit: MutableSet<Graph.Node>,
                          wolvesPositions: Set<Graph.Node>,
                          costs: IntArray,
                          index: Int,
                          sheep: Sheep,
                          ends: IntArray,
                          depth: Int) {
        visited.addAll(toVisit)
        val turns = mutableSetOf<Graph.Node>()
        toVisit.forEach {
            turns.addAll(graph.optionsForMinimax(it))
        }
        val found = turns.filter { one -> one !in wolvesPositions && one !in visited}.toMutableSet()
        if (found.isEmpty()) return
        costs[index] += found.size * depth
        if (found.any { it.coordinates.first == 0 } && depth < ends[index]) ends[index] = depth
        val newDepth = depth + 1
        return countCost(visited, found, wolvesPositions, costs, index, sheep, ends, newDepth)
    }
}

