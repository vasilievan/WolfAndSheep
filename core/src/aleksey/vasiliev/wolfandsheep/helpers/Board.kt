package aleksey.vasiliev.wolfandsheep.helpers

import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.JPG
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.assetManager
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.cellWidth
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.cellsAmount
import aleksey.vasiliev.wolfandsheep.ais.AI
import aleksey.vasiliev.wolfandsheep.ais.SheepAI
import aleksey.vasiliev.wolfandsheep.ais.WolfAI
import aleksey.vasiliev.wolfandsheep.chesspieces.Sheep
import aleksey.vasiliev.wolfandsheep.chesspieces.Wolf
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Board(private val configuration: Configuration) {
    private val brown: Texture
    private val ivory: Texture
    private val chosen: Texture
    private val sheep: Sheep
    private val wolves: MutableSet<Wolf> = mutableSetOf()
    private val wolvesAmount = 4
    private val ai: AI

    companion object {
        val beginningHeight = (Gdx.graphics.height - cellWidth * cellsAmount) / 2f
        val beginningWidth = (Gdx.graphics.width - cellWidth * cellsAmount) / 2f
    }

    init {
        for (wolfNumber in 0 until wolvesAmount) {
            wolves.add(Wolf(wolfNumber))
        }
        brown = assetManager["brown$JPG", Texture::class.java]
        ivory = assetManager["ivory$JPG", Texture::class.java]
        chosen = assetManager["chosen$JPG", Texture::class.java]
        sheep = Sheep()
        ai = if (configuration == Configuration.SHEEP) WolfAI() else SheepAI()
    }

    fun draw(spriteBatch: SpriteBatch) {
        drawBoard(spriteBatch)
        drawChosen(spriteBatch)
        sheep.draw(spriteBatch)
        wolves.forEach { it.draw(spriteBatch) }
    }

    fun touchDown(screenX: Float, screenY: Float) {
        if (configuration == Configuration.SHEEP) { sheep.isTouched(screenX, screenY) }
        else { wolves.forEach { it.isTouched(screenX, screenY) } }
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

    private fun drawChosen(spriteBatch: SpriteBatch) {
        val coordinates: Pair<Float, Float>? = if (configuration == Configuration.SHEEP && sheep.touched) {
            sheep.leftCornerCoordinates()
        } else {
            wolves.firstOrNull { it.touched }?.leftCornerCoordinates()
        }
        if (coordinates != null) spriteBatch.draw(chosen, coordinates.first, coordinates.second)
    }

    fun dispose() {
        brown.dispose()
        ivory.dispose()
        chosen.dispose()
        sheep.dispose()
        wolves.forEach { it.dispose() }
    }
}