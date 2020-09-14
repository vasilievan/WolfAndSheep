package aleksey.vasiliev.wolfandsheep.screens

import aleksey.vasiliev.wolfandsheep.Board
import aleksey.vasiliev.wolfandsheep.Configuration
import aleksey.vasiliev.wolfandsheep.screens.Beginning.Companion.prepareCanvas
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class MainMenu(configuration: Configuration) : Screen, InputAdapter() {
    private lateinit var board: Board
    private val spriteBatch = SpriteBatch()

    override fun show() {
        Gdx.input.inputProcessor = this
        board = Board()
    }

    override fun render(delta: Float) {
        prepareCanvas(false)
        spriteBatch.begin()
        board.draw(spriteBatch)
        spriteBatch.end()
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        board.touchDown(screenX, screenY)
        return super.touchDown(screenX, screenY, pointer, button)
    }

    override fun dispose() {
        spriteBatch.dispose()
        board.dispose()
    }


    override fun resize(width: Int, height: Int) {}

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}
}