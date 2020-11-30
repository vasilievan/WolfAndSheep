package tests

import aleksey.vasiliev.wolfandsheep.helpers.Configuration
import aleksey.vasiliev.wolfandsheep.helpers.Graph

class WolfWrapper(configuration: Configuration, wolfNumber: Int, graph: Graph) : ChessPieceWrapper() {
    override lateinit var node: Graph.Node
    init {
        val row = if (configuration == Configuration.WOLF) 0 else 7
        node = graph.nodes.first { it.coordinates.first == row && it.coordinates.second == wolfNumber }
    }
}