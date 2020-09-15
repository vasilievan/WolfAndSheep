package aleksey.vasiliev.wolfandsheep

import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer
import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration

class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = AndroidApplicationConfiguration()
        initialize(ResourseContainer, config)
    }
}