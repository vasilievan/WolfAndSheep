package aleksey.vasiliev.wolfandsheep.chesspieces

import aleksey.vasiliev.wolfandsheep.ResourseContainer.PNG
import aleksey.vasiliev.wolfandsheep.ResourseContainer.assetManager
import com.badlogic.gdx.graphics.Texture

class Sheep : ChessPiece() {

    override val coordinates: Pair<Int, Int> = 7 to (0..3).random()

    override val texture: Texture = assetManager["sheep$PNG", Texture::class.java]

}