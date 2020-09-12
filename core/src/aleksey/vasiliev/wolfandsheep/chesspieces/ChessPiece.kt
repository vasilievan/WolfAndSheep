package aleksey.vasiliev.wolfandsheep.chesspieces

import aleksey.vasiliev.wolfandsheep.Board.Companion.beginningHeight
import aleksey.vasiliev.wolfandsheep.Board.Companion.beginningWidth
import aleksey.vasiliev.wolfandsheep.ResourseContainer.cellWidth
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

abstract class ChessPiece {
    abstract val texture: Texture

    abstract val coordinates: Pair<Int, Int>

    fun draw(spriteBatch: SpriteBatch) {
        if (coordinates.first % 2 == 0) {
            spriteBatch.draw(texture, beginningWidth + coordinates.second * cellWidth * 2f,
                    beginningHeight + coordinates.first * cellWidth)
        } else {
            spriteBatch.draw(texture, beginningWidth + (coordinates.second + 0.5f) * cellWidth * 2f,
                    beginningHeight + coordinates.first * cellWidth)
        }

    }

    fun dispose() {
        texture.dispose()
    }
}