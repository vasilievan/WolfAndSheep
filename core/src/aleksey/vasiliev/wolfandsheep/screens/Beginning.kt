package aleksey.vasiliev.wolfandsheep.screens

import aleksey.vasiliev.wolfandsheep.helpers.Configuration
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.PNG
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.assetManager
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.cellWidth
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.configuration
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.setScreen
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Beginning : Screen, InputAdapter() {
    private var loading = true
    private val spriteBatch = SpriteBatch()
    private lateinit var sheepTexture: Texture
    private lateinit var wolfTexture: Texture

    companion object {
        fun prepareCanvas(beginning: Boolean) {
            if (beginning) Gdx.gl.glClearColor(0.92f, 0.87f, 0.75f, 1f)
            else Gdx.gl.glClearColor(0.3f, 0.22f, 0.18f, 1f)
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        }
        fun yNormalized(screenY: Int): Float = (Gdx.graphics.height - screenY).toFloat()
    }

    override fun show() {}

    override fun render(delta: Float) {
        if (assetManager.update() && loading) {
            sheepTexture = assetManager["sheep$PNG", Texture::class.java]
            wolfTexture = assetManager["wolf$PNG", Texture::class.java]
            Gdx.input.inputProcessor = this
            loading = false
        }
        if (!loading) {
            prepareCanvas(true)
            spriteBatch.begin()
            spriteBatch.draw(sheepTexture, Gdx.graphics.width / 2f - cellWidth, Gdx.graphics.height / 2f - cellWidth)
            spriteBatch.draw(wolfTexture, Gdx.graphics.width / 2f + cellWidth, Gdx.graphics.height / 2f - cellWidth)
            spriteBatch.end()
        }
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val heightRange = Gdx.graphics.height / 2f - cellWidth..Gdx.graphics.height / 2f
        var touched = false
        if (yNormalized(screenY) in heightRange) {
            if (screenX.toFloat() in Gdx.graphics.width / 2f - cellWidth..Gdx.graphics.width / 2f) {
                configuration = Configuration.SHEEP
                touched = true
            }
            if (screenX.toFloat() in Gdx.graphics.width / 2f + cellWidth..Gdx.graphics.width / 2f + cellWidth * 2f) {
                configuration = Configuration.WOLF
                touched = true
            }
            if (touched) setScreen(MainMenu())
        }
        return super.touchDown(screenX, screenY, pointer, button)
    }

    override fun resize(width: Int, height: Int) {}

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}

    override fun dispose() {
        sheepTexture.dispose()
        wolfTexture.dispose()
        spriteBatch.dispose()
    }
}