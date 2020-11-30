package aleksey.vasiliev.wolfandsheep.helpers

import aleksey.vasiliev.wolfandsheep.screens.Beginning
import com.badlogic.gdx.Game
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture

// Класс-хранилище объектов, используемых как глобальные.
object ResourseContainer : Game() {
    var assetManager = AssetManager()
    const val JPG = ".jpg"
    const val PNG = ".png"
    const val cellWidth = 128f
    const val cellsAmount = 8
    private val cells = setOf("brown", "ivory", "chosen")
    private val chessPieces = setOf("sheep", "wolf")
    var configuration: Configuration? = null
    var graph = Graph()
    var playerWon = false

    // Инициализация игры, создание графа, подготока ресурсов.
    override fun create() {
        graph.create()
        cells.forEach { assetManager.load("$it$JPG", Texture::class.java) }
        chessPieces.forEach { assetManager.load("$it$PNG", Texture::class.java) }
        setScreen(Beginning())
    }
}