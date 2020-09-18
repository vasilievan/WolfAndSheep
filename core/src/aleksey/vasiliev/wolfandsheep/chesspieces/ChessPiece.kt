package aleksey.vasiliev.wolfandsheep.chesspieces

import aleksey.vasiliev.wolfandsheep.helpers.Board.Companion.beginningHeight
import aleksey.vasiliev.wolfandsheep.helpers.Board.Companion.beginningWidth
import aleksey.vasiliev.wolfandsheep.helpers.Graph
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.cellWidth
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

abstract class ChessPiece {
    companion object {
        fun countTextureCoordinates(node: Graph.Node): Pair<Float, Float> {
            return if (node.coordinates.first % 2 == 0) {
                beginningWidth + node.coordinates.second * cellWidth * 2f to beginningHeight + node.coordinates.first * cellWidth
            } else {
                beginningWidth + (node.coordinates.second + 0.5f) * cellWidth * 2f to beginningHeight + node.coordinates.first * cellWidth
            }
        }
    }

    abstract val texture: Texture

    abstract var node: Graph.Node

    abstract var touched: Boolean

    fun draw(spriteBatch: SpriteBatch) {
        val currentLeftCornerCoordinates = leftCornerCoordinates()
        spriteBatch.draw(texture, currentLeftCornerCoordinates.first, currentLeftCornerCoordinates.second)
    }

    fun isTouched(screenX: Float, screenY: Float) {
        val currentTextureCoordinates = leftCornerCoordinates()
        touched = screenX in currentTextureCoordinates.first..currentTextureCoordinates.first + cellWidth &&
                screenY in currentTextureCoordinates.second..currentTextureCoordinates.second + cellWidth
    }

    fun move(node: Graph.Node) {
        this.node = node
        touched = false
    }

    fun leftCornerCoordinates(): Pair<Float, Float> = countTextureCoordinates(node)

    fun dispose() {
        texture.dispose()
    }
}