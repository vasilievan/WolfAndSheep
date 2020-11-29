package aleksey.vasiliev.wolfandsheep.chesspieces

import aleksey.vasiliev.wolfandsheep.helpers.Configuration
import aleksey.vasiliev.wolfandsheep.helpers.Graph
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.PNG
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.assetManager
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.configuration
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.graph
import com.badlogic.gdx.graphics.Texture

// Класс, описывающий овечку как шахматную фигуру.
class Sheep : ChessPiece() {
    override lateinit var node: Graph.Node

    // Привязка текстуры к объекту.
    override val texture: Texture = assetManager["sheep$PNG", Texture::class.java]

    init {
        // В зависимости от текущей конфигурации овечка либо в начале доски (пользователь играет за овечку),
        // либо в конце. Это сделано для удобства пользователя.
        val row = if (configuration == Configuration.SHEEP) 0 else 7
        // Положение в ряду выбирается случаным образом.
        val column = (0..3).random()
        node = graph.nodes.first { it.coordinates.first == row && it.coordinates.second == column }
    }
}