package aleksey.vasiliev.wolfandsheep.ais

import aleksey.vasiliev.wolfandsheep.chesspieces.Sheep
import aleksey.vasiliev.wolfandsheep.chesspieces.Wolf
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.graph
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.playerWon
import aleksey.vasiliev.wolfandsheep.screens.TheEnd
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.setScreen

class WolfAI(private val sheep: Sheep, private val wolves: MutableSet<Wolf>) : AI {
    override fun move() {
        val wolf = wolves.first()
        val wolvesPositions = wolves.map { it.node }
        val possibleTurns = graph.aiWolfOptions(wolf).filter { it !in wolvesPositions && it != sheep.node }
        if (sheep.node.coordinates.first == 7) {
            playerWon = true
            setScreen(TheEnd())
        }
        if (possibleTurns.isEmpty()) {
            playerWon = true
            setScreen(TheEnd())
        } else {
            wolf.move(possibleTurns.first())
        }
    }
}