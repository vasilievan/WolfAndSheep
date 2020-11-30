package aleksey.vasiliev.wolfandsheep

import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer
import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration

/** Данное мобильное приложение - часть курсового проекта по "Алгоритмам и структурам данных".
 * Оно написано для OS Google Android на игровом движке libGDX и представляет собой
 * реализацию популярной шашечной игры "Волк и овцы" (существует еще один вариант названия на
 * русском языке, "Заяц и волки"), на немецком "Wolf und Schafe", аналог первого названия.
 *
 * Суть игры: овца должна прорваться сквозь строй волков и достичь противоположного конца доски,
 * волки должны создать ситуацию, когда у овцы нет ходов, "поймать её".
 * Овца всегда ходит первой, может ходить в людом направлении по диагонали на одну клетку.
 * Волки могут ходить по диагонали только вперёд относительно движения на одну клетку.
 *
 * Приложение имеет минималистичный дизайн.
 *
 * В реализации были использованы такие структуры данных как граф, из алгоритмов - поиск в
 * ширину, поиск длины кратчайшего пути между вершинами графа и другие графовые алгоритмы.
 *
 * @author      <a href="mailto:vasiliev.an@edu.spbstu.ru">Aleksei Vasilev</a>
 * @version     1.1
 * @since       1.1
 */

class AndroidLauncher : AndroidApplication() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val config = AndroidApplicationConfiguration()
        initialize(ResourseContainer, config)
    }
}