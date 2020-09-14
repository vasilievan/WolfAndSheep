package aleksey.vasiliev.wolfandsheep

import aleksey.vasiliev.wolfandsheep.screens.Beginning
import com.badlogic.gdx.Game
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture

object ResourseContainer : Game() {

    val assetManager = AssetManager()
    const val JPG = ".jpg"
    const val PNG = ".png"
    const val cellWidth = 128f
    private val cells = setOf("brown", "ivory")
    private val chessPieces = setOf("sheep", "wolf")
    val cellsAmount = 8

    override fun create() {
        cells.forEach { assetManager.load("$it$JPG", Texture::class.java) }
        chessPieces.forEach { assetManager.load("$it$PNG", Texture::class.java) }
        setScreen(Beginning())
    }
}