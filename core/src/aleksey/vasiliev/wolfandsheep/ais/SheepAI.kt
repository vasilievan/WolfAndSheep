package aleksey.vasiliev.wolfandsheep.ais

import aleksey.vasiliev.wolfandsheep.chesspieces.Sheep
import aleksey.vasiliev.wolfandsheep.chesspieces.Wolf
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.graph
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.playerWon
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.setScreen
import aleksey.vasiliev.wolfandsheep.screens.TheEnd

class SheepAI(private val sheep: Sheep, private val wolves: MutableSet<Wolf>): AI {
    init {
        move()
    }
    override fun move() {
        val wolvesPositions = wolves.map { it.node }
        val possibleTurns = graph.options(sheep).filter { it !in wolvesPositions }
        // 0 or 7
        if (sheep.node.coordinates.first == 0) {
            playerWon = false
            setScreen(TheEnd())
        }
        if (possibleTurns.isEmpty()) {
            playerWon = true
            setScreen(TheEnd())
        } else {
            sheep.move(possibleTurns.first())
        }
    }
}