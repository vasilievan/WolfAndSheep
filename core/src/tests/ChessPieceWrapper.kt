package tests

import aleksey.vasiliev.wolfandsheep.helpers.Graph

abstract class ChessPieceWrapper {
    abstract var node: Graph.Node

    var touched = false

    fun move(node: Graph.Node) {
        this.node = node
        touched = false
    }
}