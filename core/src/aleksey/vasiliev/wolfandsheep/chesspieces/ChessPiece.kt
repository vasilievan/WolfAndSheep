package aleksey.vasiliev.wolfandsheep.chesspieces

import aleksey.vasiliev.wolfandsheep.helpers.Board.Companion.beginningHeight
import aleksey.vasiliev.wolfandsheep.helpers.Board.Companion.beginningWidth
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.cellWidth
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

abstract class ChessPiece {
    abstract val texture: Texture

    abstract val coordinates: Pair<Int, Int>

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

    fun leftCornerCoordinates(): Pair<Float, Float> {
        return if (coordinates.first % 2 == 0) {
            beginningWidth + coordinates.second * cellWidth * 2f to beginningHeight + coordinates.first * cellWidth
        } else {
            beginningWidth + (coordinates.second + 0.5f) * cellWidth * 2f to beginningHeight + coordinates.first * cellWidth
        }
    }

    fun dispose() {
        texture.dispose()
    }
}