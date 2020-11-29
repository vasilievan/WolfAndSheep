package aleksey.vasiliev.wolfandsheep.chesspieces

import aleksey.vasiliev.wolfandsheep.helpers.Configuration
import aleksey.vasiliev.wolfandsheep.helpers.Graph
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.PNG
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.assetManager
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.configuration
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.graph
import com.badlogic.gdx.graphics.Texture

// Класс, описывающий волка как шахматную фигуру.
// Положение на доске определяется номером волка (т.к. их 4).
class Wolf(wolfNumber: Int): ChessPiece() {
    override lateinit var node: Graph.Node

    // Привязка текстуры к объекту.
    override val texture: Texture = assetManager["wolf$PNG", Texture::class.java]

    init {
        // В зависимости от текущей конфигурации волк либо в начале доски (пользователь играет за волков),
        // либо в конце. Это сделано для удобства пользователя.
        val row = if (configuration == Configuration.WOLF) 0 else 7
        node = graph.nodes.first { it.coordinates.first == row && it.coordinates.second == wolfNumber }
    }
}