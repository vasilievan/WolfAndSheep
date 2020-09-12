package aleksey.vasiliev.wolfandsheep

import aleksey.vasiliev.wolfandsheep.ResourseContainer.assetManager
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class MainMenu : Screen, InputAdapter() {

    private var loading = true
    private lateinit var board: Board
    private val spriteBatch = SpriteBatch()

    override fun show() {
        Gdx.input.inputProcessor = this
    }

    override fun render(delta: Float) {
        if (assetManager.update() && loading) {
            board = Board()
            loading = false
        }
        if (!loading) {
            prepareCanvas()
            spriteBatch.begin()
            board.draw(spriteBatch)
            spriteBatch.end()
        }
    }

    override fun dispose() {
        spriteBatch.dispose()
        board.dispose()
    }

    private fun prepareCanvas() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
    }

    override fun resize(width: Int, height: Int) {}

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}
}