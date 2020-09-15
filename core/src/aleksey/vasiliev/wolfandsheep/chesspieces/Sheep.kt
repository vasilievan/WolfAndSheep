package aleksey.vasiliev.wolfandsheep.chesspieces

import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.PNG
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.assetManager
import com.badlogic.gdx.graphics.Texture

class Sheep : ChessPiece() {

    override val coordinates: Pair<Int, Int> = 7 to (0..3).random()

    override var touched: Boolean = false

    override val texture: Texture = assetManager["sheep$PNG", Texture::class.java]
}