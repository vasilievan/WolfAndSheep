package aleksey.vasiliev.wolfandsheep.helpers

import aleksey.vasiliev.wolfandsheep.chesspieces.ChessPiece
import aleksey.vasiliev.wolfandsheep.chesspieces.Sheep
import aleksey.vasiliev.wolfandsheep.chesspieces.Wolf
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.cellsAmount
import java.lang.Math.pow
import kotlin.math.pow

class Graph {
    val nodes = mutableSetOf<Node>()
    inner class Node(val coordinates: Pair<Int, Int>) {
        fun ne(): Node? = if (coordinates.first == 7 || coordinates.first % 2 == 1 && coordinates.second == 3) {
             null
        } else {
            if (coordinates.first % 2 == 0) {
                nodes.first { it.coordinates == coordinates.first + 1 to coordinates.second }
            } else {
                nodes.first { it.coordinates == coordinates.first + 1 to coordinates.second + 1 }
            }
        }

        fun sw(): Node? = if (coordinates.first == 0 || coordinates.first % 2 == 0 && coordinates.second == 0) {
            null
        } else {
            if (coordinates.first % 2 == 0) {
                nodes.first { it.coordinates == coordinates.first - 1 to coordinates.second - 1}
            } else {
                nodes.first { it.coordinates == coordinates.first - 1 to coordinates.second }
            }
        }

        fun se(): Node? = if (coordinates.first == 0 || coordinates.first % 2 == 1 && coordinates.second == 3) {
            null
        } else {
            if (coordinates.first % 2 == 0) {
                nodes.first { it.coordinates == coordinates.first - 1 to coordinates.second }
            } else {
                nodes.first { it.coordinates == coordinates.first - 1 to coordinates.second + 1 }
            }
        }


        fun nw(): Node? = if (coordinates.first == 7 || coordinates.first % 2 == 0 && coordinates.second == 0) {
            null
        } else {
            if (coordinates.first % 2 == 0) {
                nodes.first { it.coordinates == coordinates.first + 1 to coordinates.second - 1 }
            } else {
                nodes.first { it.coordinates == coordinates.first + 1 to coordinates.second }
            }
        }

        override fun equals(other: Any?): Boolean {
            return if (other is Node) {
                coordinates.first == other.coordinates.first && coordinates.second == other.coordinates.second
            } else {
                false
            }
        }

        override fun hashCode(): Int {
            return coordinates.hashCode()
        }
    }

    fun create() {
        for (row in 0 until cellsAmount) {
            for (column in 0 until cellsAmount) {
                nodes.add(Node(row to column))
            }
        }
    }

    fun options(chosenOne: ChessPiece): Set<Graph.Node> {
        val node = chosenOne.node
        return if (chosenOne is Sheep) {
            listOfNotNull(node.ne(), node.nw(), node.se(), node.sw()).toSet()
        } else {
            listOfNotNull(node.ne(), node.nw()).toSet()
        }
    }

    fun optionsForMinimax(node: Node): MutableList<Graph.Node> = listOfNotNull(node.sw(), node.se(), node.ne(), node.nw()).toMutableList()
}