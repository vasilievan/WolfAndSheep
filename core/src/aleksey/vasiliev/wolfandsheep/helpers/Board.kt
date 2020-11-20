package aleksey.vasiliev.wolfandsheep.helpers

import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.JPG
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.assetManager
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.cellWidth
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.cellsAmount
import aleksey.vasiliev.wolfandsheep.ais.AI
import aleksey.vasiliev.wolfandsheep.ais.SheepAI
import aleksey.vasiliev.wolfandsheep.ais.WolfAI
import aleksey.vasiliev.wolfandsheep.chesspieces.ChessPiece
import aleksey.vasiliev.wolfandsheep.chesspieces.ChessPiece.Companion.countTextureCoordinates
import aleksey.vasiliev.wolfandsheep.chesspieces.Sheep
import aleksey.vasiliev.wolfandsheep.chesspieces.Wolf
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.configuration
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.graph
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Board {
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
        ai = if (configuration == Configuration.SHEEP) WolfAI(sheep, wolves) else SheepAI(sheep, wolves)
    }

    fun draw(spriteBatch: SpriteBatch) {
        drawBoard(spriteBatch)
        drawChosen(spriteBatch)
        sheep.draw(spriteBatch)
        wolves.forEach { it.draw(spriteBatch) }
    }

    fun touchDown(screenX: Float, screenY: Float) {
        if (configuration == Configuration.SHEEP) {
            moveTheStuff(sheep, screenX, screenY)
            sheep.isTouched(screenX, screenY)
        } else {
            val touchedWolf = wolves.firstOrNull { it.touched }
            if (touchedWolf != null) {
                moveTheStuff(touchedWolf, screenX, screenY)
            }
            wolves.forEach { it.isTouched(screenX, screenY) }
        }
    }

    private fun moveTheStuff(chessPiece: ChessPiece, screenX: Float, screenY: Float) {
        graph.options(chessPiece).forEach {
            if (it !in wolves.map { wolf -> wolf.node } && it != sheep.node) {
                val coordinates = countTextureCoordinates(it)
                if (screenX in coordinates.first..coordinates.first + cellWidth &&
                        screenY in coordinates.second..coordinates.second + cellWidth) {
                    chessPiece.move(it)
                    ai.move()
                    return
                }
            }
        }
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
        val chosenOne: ChessPiece? = if (configuration == Configuration.SHEEP && sheep.touched) { sheep }
        else { wolves.firstOrNull { it.touched } }
        if (chosenOne != null) {
            val coordinates = chosenOne.leftCornerCoordinates()
            spriteBatch.draw(chosen, coordinates.first, coordinates.second)
            graph.options(chosenOne).forEach {
                if (it !in wolves.map { wolf -> wolf.node } && it != sheep.node) {
                    val optionCoordinates = countTextureCoordinates(it)
                    spriteBatch.draw(chosen, optionCoordinates.first, optionCoordinates.second)
                }
            }
        }
    }

    fun dispose() {
        brown.dispose()
        ivory.dispose()
        chosen.dispose()
        sheep.dispose()
        wolves.forEach { it.dispose() }
    }
}