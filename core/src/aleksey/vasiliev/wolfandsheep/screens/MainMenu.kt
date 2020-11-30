package aleksey.vasiliev.wolfandsheep.screens

import aleksey.vasiliev.wolfandsheep.helpers.Board
import aleksey.vasiliev.wolfandsheep.screens.Beginning.Companion.prepareCanvas
import aleksey.vasiliev.wolfandsheep.screens.Beginning.Companion.yNormalized
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class MainMenu : Screen, InputAdapter() {
    private lateinit var board: Board
    private val spriteBatch = SpriteBatch()

    // Привязка к данному окну обработчика событий, инициализация доски.
    override fun show() {
        Gdx.input.inputProcessor = this
        board = Board()
    }

    // Функция отрисовки, выполняется столько раз в секунду, сколько fps у движка на устройстве.
    override fun render(delta: Float) {
        prepareCanvas(false)
        spriteBatch.begin()
        board.draw(spriteBatch)
        spriteBatch.end()
    }

    // Обработка нажатия на доску.
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        board.touchDown(screenX.toFloat(), yNormalized(screenY))
        return super.touchDown(screenX, screenY, pointer, button)
    }

    // Освобождение графических ресурсов.
    override fun dispose() {
        spriteBatch.dispose()
        board.dispose()
    }


    override fun resize(width: Int, height: Int) {}

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}
}