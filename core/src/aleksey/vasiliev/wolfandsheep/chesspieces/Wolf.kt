package aleksey.vasiliev.wolfandsheep.chesspieces

import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.PNG
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.assetManager
import com.badlogic.gdx.graphics.Texture

class Wolf(wolfNumber: Int): ChessPiece() {

    override val coordinates = 0 to wolfNumber

    override var touched: Boolean = false

    override val texture: Texture = assetManager["wolf$PNG", Texture::class.java]

}