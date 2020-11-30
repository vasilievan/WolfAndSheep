package aleksey.vasiliev.wolfandsheep.chesspieces

import aleksey.vasiliev.wolfandsheep.helpers.Board.Companion.beginningHeight
import aleksey.vasiliev.wolfandsheep.helpers.Board.Companion.beginningWidth
import aleksey.vasiliev.wolfandsheep.helpers.Graph
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.cellWidth
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

// Абстрактный класс, описывающий шахматные фигуры.
abstract class ChessPiece {
    companion object {
        /* Функция, необходимая для обеспечения отрисовки согласно конфигурации
        графа.
        */
        fun countTextureCoordinates(node: Graph.Node): Pair<Float, Float> {
            return if (node.coordinates.first % 2 == 0) {
                beginningWidth + node.coordinates.second * cellWidth * 2f to beginningHeight + node.coordinates.first * cellWidth
            } else {
                beginningWidth + (node.coordinates.second + 0.5f) * cellWidth * 2f to beginningHeight + node.coordinates.first * cellWidth
            }
        }
    }

    // Текстура шахматной фигуры.
    abstract val texture: Texture

    // Соответствующая нода.
    abstract var node: Graph.Node

    // На момент создания объекта, ни одна фигура не выбрана.
    var touched = false

    // Функция отрисовки объекта движком.
    fun draw(spriteBatch: SpriteBatch) {
        val currentLeftCornerCoordinates = countTextureCoordinates(node)
        spriteBatch.draw(texture, currentLeftCornerCoordinates.first, currentLeftCornerCoordinates.second)
    }

    // Проверка на то, дотронулся ли пользователь до заданной фигуры.
    fun isTouched(screenX: Float, screenY: Float) {
        val currentTextureCoordinates = countTextureCoordinates(node)
        touched = screenX in currentTextureCoordinates.first..currentTextureCoordinates.first + cellWidth &&
                screenY in currentTextureCoordinates.second..currentTextureCoordinates.second + cellWidth
    }

    // Передвижение объекта сделанно примитивным образом - меняется текущая нода графа.
    fun move(node: Graph.Node) {
        this.node = node
        touched = false
    }

    // Освобождение графических ресурсов.
    fun dispose() {
        texture.dispose()
    }
}