package aleksey.vasiliev.wolfandsheep.chesspieces

import aleksey.vasiliev.wolfandsheep.helpers.Configuration
import aleksey.vasiliev.wolfandsheep.helpers.Graph
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.PNG
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.assetManager
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.configuration
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.graph
import com.badlogic.gdx.graphics.Texture

class Sheep : ChessPiece() {
    override lateinit var node: Graph.Node

    override var touched: Boolean = false

    override val texture: Texture = assetManager["sheep$PNG", Texture::class.java]

    init {
        val row = if (configuration == Configuration.SHEEP) 0 else 7
        val column = (0..3).random()
        node = graph.nodes.first { it.coordinates.first == row && it.coordinates.second == column }
    }
}