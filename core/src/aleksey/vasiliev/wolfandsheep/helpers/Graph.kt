package aleksey.vasiliev.wolfandsheep.helpers

import aleksey.vasiliev.wolfandsheep.chesspieces.ChessPiece
import aleksey.vasiliev.wolfandsheep.chesspieces.Sheep
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.cellsAmount

/* Граф, необходимый для реализации логики по перемещению фигур, ходов искусственного интеллекта,
представленный списком смежностей.
*/
class Graph {
    val nodes = mutableSetOf<Node>()

    // Вершина графа
    inner class Node(val coordinates: Pair<Int, Int>) {
        /* Методы для получения соседних вершин с данной. Названы по аналогии со сторонами света,
        если бы сверху доски был север, снизу юг, справа - восток, слева - запад.
         */
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
                nodes.first { it.coordinates == coordinates.first - 1 to coordinates.second - 1 }
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

        // Методы для удобства сравнения вершин
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

    // Заполнение списка смежностей.
    fun create() {
        for (row in 0 until cellsAmount) {
            for (column in 0 until cellsAmount) {
                nodes.add(Node(row to column))
            }
        }
    }

    // Возможные ходы для шахматной фигуры, данный метод предназначен для пользователя.
    fun options(chosenOne: ChessPiece): Set<Graph.Node> {
        val node = chosenOne.node
        return if (chosenOne is Sheep) {
            listOfNotNull(node.ne(), node.nw(), node.se(), node.sw()).toSet()
        } else {
            listOfNotNull(node.ne(), node.nw()).toSet()
        }
    }

    // Соседи данной вершины, данные методы предназначены для искуственного интеллекта.
    fun optionsForSheep(node: Node): MutableList<Graph.Node> = listOfNotNull(node.ne(), node.nw()).toMutableList()

    fun options(node: Node): MutableList<Graph.Node> = listOfNotNull(node.sw(), node.se(), node.ne(), node.nw()).toMutableList()

    fun optionsForWolf(node: Node): List<Graph.Node> = listOfNotNull(node.sw(), node.se()).toList()
}