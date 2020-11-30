package tests

import aleksey.vasiliev.wolfandsheep.helpers.Configuration
import aleksey.vasiliev.wolfandsheep.helpers.Graph
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.PNG
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.assetManager
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.configuration
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.graph
import com.badlogic.gdx.graphics.Texture

class SheepWrapper(configuration: Configuration, graph: Graph) : ChessPieceWrapper() {
    override lateinit var node: Graph.Node
    init {
        val row = if (configuration == Configuration.SHEEP) 0 else 7
        val column = 2
        node = graph.nodes.first { it.coordinates.first == row && it.coordinates.second == column }
    }
}