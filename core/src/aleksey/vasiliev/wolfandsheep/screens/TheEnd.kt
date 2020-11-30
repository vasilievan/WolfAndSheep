package aleksey.vasiliev.wolfandsheep.screens

import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.playerWon
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.setScreen
import aleksey.vasiliev.wolfandsheep.screens.Beginning.Companion.prepareCanvas
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator

/* Класс, представляющий собой окно, которое видит пользователь по окончании игры.
Пользователю показано состояние, выиграл он - "You won!" или проиграл - "You lose!".
По нажатию запускается класс Beginning, игра начинается сначала.
*/
class TheEnd : Screen, InputAdapter() {
    private val spriteBatch = SpriteBatch()
    private var bitmapFont = BitmapFont()
    private val won = "You won!"
    private val lose = "You lose!"

    override fun show() { Gdx.input.inputProcessor = this }

    // Функция отрисовки, выполняется столько раз в секунду, сколько fps у движка на устройстве.
    override fun render(delta: Float) {
        prepareCanvas(false)
        spriteBatch.begin()
        labelMaker()
        val glyphLayout = if (playerWon) GlyphLayout(bitmapFont, won) else GlyphLayout(bitmapFont, lose)
        if (playerWon) {
            bitmapFont.draw(spriteBatch, won, Gdx.graphics.width / 2f - glyphLayout.width / 2,
                    Gdx.graphics.height / 2f + glyphLayout.height / 2)
        } else {
            bitmapFont.draw(spriteBatch, lose, Gdx.graphics.width / 2f - glyphLayout.width / 2,
                    Gdx.graphics.height / 2f + glyphLayout.height / 2)
        }
        spriteBatch.end()
    }

    // Создание надписи с информацией о победе/поражении.
    private fun labelMaker(): BitmapFont {
        val generator = FreeTypeFontGenerator(Gdx.files.internal("Arkhip.ttf"))
        val parameter: FreeTypeFontGenerator.FreeTypeFontParameter = FreeTypeFontGenerator.FreeTypeFontParameter()
        parameter.size = (130f / (2130f / Gdx.graphics.width.toFloat())).toInt()
        parameter.color = Color(0.92f, 0.87f, 0.75f, 1f)
        bitmapFont = generator.generateFont(parameter)
        generator.dispose()
        return bitmapFont
    }

    // Переход в начальное состояние по нажатию.
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        setScreen(Beginning())
        return super.touchDown(screenX, screenY, pointer, button)
    }

    // Освобождение графических ресурсов.
    override fun dispose() {
        bitmapFont.dispose()
        spriteBatch.dispose()
    }

    override fun resize(width: Int, height: Int) {}

    override fun pause() {}

    override fun resume() {}

    override fun hide() {}
}