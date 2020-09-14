package aleksey.vasiliev.wolfandsheep

import aleksey.vasiliev.wolfandsheep.ResourseContainer.JPG
import aleksey.vasiliev.wolfandsheep.ResourseContainer.assetManager
import aleksey.vasiliev.wolfandsheep.ResourseContainer.cellWidth
import aleksey.vasiliev.wolfandsheep.ResourseContainer.cellsAmount
import aleksey.vasiliev.wolfandsheep.chesspieces.Sheep
import aleksey.vasiliev.wolfandsheep.chesspieces.Wolf
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Board {
    private val brown = assetManager["brown$JPG", Texture::class.java]
    private val ivory = assetManager["ivory$JPG", Texture::class.java]
    private val sheep = Sheep()
    private val wolves = mutableSetOf<Wolf>()
    private val wolvesAmount = 4

    companion object {
        val beginningHeight = (Gdx.graphics.height - cellWidth * cellsAmount) / 2f
        val beginningWidth = (Gdx.graphics.width - cellWidth * cellsAmount) / 2f
    }

    init {
        for (wolfNumber in 0 until wolvesAmount) {
            wolves.add(Wolf(wolfNumber))
        }
    }

    fun draw(spriteBatch: SpriteBatch) {
        drawBoard(spriteBatch)
        sheep.draw(spriteBatch)
        wolves.forEach { it.draw(spriteBatch) }
    }

    fun touchDown(screenX: Int, screenY: Int) {

    }

    private fun drawBoard(spriteBatch: SpriteBatch) {
        for (row in 0 until cellsAmount) {
            for (column in 0 until cellsAmount) {
                if ((row + column) % 2 == 0) {
                    spriteBatch.draw(brown, beginningWidth + row * cellWidth,
                            beginningHeight + column * cellWidth)
                } else {
                    spriteBatch.draw(ivory, beginningWidth + row * cellWidth,
                            beginningHeight + column * cellWidth)
                }
            }
        }
    }

    fun dispose() {
        brown.dispose()
        ivory.dispose()
        sheep.dispose()
        wolves.forEach { it.dispose() }
    }
}