package aleksey.vasiliev.wolfandsheep.ais

import aleksey.vasiliev.wolfandsheep.chesspieces.Sheep
import aleksey.vasiliev.wolfandsheep.chesspieces.Wolf
import aleksey.vasiliev.wolfandsheep.helpers.Graph
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.graph
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.playerWon
import aleksey.vasiliev.wolfandsheep.helpers.ResourseContainer.setScreen
import aleksey.vasiliev.wolfandsheep.screens.TheEnd

// Данный класс является реализацией искуственного интеллекта при игре пользователя за овечку.
class WolfAI(private val sheep: Sheep, private val wolves: MutableList<Wolf>) : AI {

    /* Класс с данными для обработки исключений. Представлю позицию волков и овечки как пятимерный вектор,
    состоящий для удобства восприятия и соблюдения принципов ООП из двух разных объектов.
    Дополнительная информация в классе представляет собой порядковый номер волка, который должен ходить,
    а также выигрышный ход.
    */
    data class Case(val wolvesPosition: List<Graph.Node>, val sheepPosition: Graph.Node, val wolfIndex: Int, val turn: Graph.Node)

    /* Эвристика проста.
    Проверяю все возможные ходы волков на 1 ход вперёд.
    Используя поиск в ширину, нахожу позиции для овечки, когда она стоит позади волка
    в непосредственной близости и может ходить (назову такое состояние "точка невозрата").
    Т.е. цепь волков прорвана.
    Выбираю ход, при котором овечке дольше всего бежать до точки невозрата (максимальная глубина
    рекурсии при BFS). И хожу так. Очевидно, есть исключения, но их всего 16. Обрабатываю отдельно.

    Данный вариант качественно отличается от алгортма Minimax,
    предложенного в источниках к курсовой работе, т.к. является гораздо
    более производительным, также существенна экономия памяти, при этом
    ИИ при игре за волков побеждает в большинстве случаев, но не во всех,
    что сохраняет интерес игрока к игре за овечку.

    Сложность алгоритма - O(v + e), где v - к-во вершин графа, e - к-во рёбер (соседей данной вершины).
    Память - O(v), v - к-во вершин графа.
    */
    override fun move() {
        // Возможна победа игрока.
        if (sheep.node.coordinates.first == 7) {
            playerWon = true
            setScreen(TheEnd())
            return
        }
        val wolvesPositions = wolves.map { it.node }
        // Обработка специальных случаев.
        val isSpecialCase = checkInSpecialCases(wolvesPositions, sheep.node)
        if (isSpecialCase) return
        // Обработка общих случаев.
        val wolvesTurns = mutableListOf<List<Graph.Node>>()
        wolvesPositions.forEach {
            val possibleTurns = graph.optionsForWolf(it).filter { one -> one !in wolvesPositions && one != sheep.node }
            wolvesTurns.add(possibleTurns)
        }
        /* Нахождение максимального расстояния среди возможных позиций волков с использованием
        стандартного алгоритма.
        */
        var maximum = Int.MIN_VALUE
        var toGo = -1 to -1
        for (index in wolvesTurns.indices) {
            for (innerIndex in wolvesTurns[index].indices) {
                val position = wolvesPositions.take(index) + wolvesTurns[index][innerIndex] + wolvesPositions.subList(index + 1, 4)
                val cost = countCost(mutableSetOf(), listOf(sheep.node), position, 1)
                if (cost > maximum) {
                    maximum = cost
                    toGo = index to innerIndex
                }
            }
        }
        /* Этот случай не наступит в штатном режиме, но будет означать победу игрока. Добавлен для сохранения
        аналогии с классом SheepAI.
        */
        if (toGo == -1 to -1) {
            playerWon = true
            setScreen(TheEnd())
            return
        }
        // Перемещение на найденную ноду.
        wolves[toGo.first].move(wolvesTurns[toGo.first][toGo.second])
        /* Проверка, победили ли волки после данного хода, т.к. следующий
        цикл ходов игрок-волки уже не наступит.
        */
        val possibleTurns = graph.options(sheep.node).filter { it !in wolves.map { one -> one.node } }
        if (possibleTurns.isEmpty()) {
            playerWon = false
            setScreen(TheEnd())
        }
    }

    // Рекурсивная реализация поиска в ширину.
    private fun countCost(visited: MutableSet<Graph.Node>,
                          toVisit: List<Graph.Node>,
                          wolvesPositions: List<Graph.Node>,
                          depth: Int): Int {
        visited.addAll(toVisit)
        val turns = mutableSetOf<Graph.Node>()
        toVisit.forEach { turns.addAll(graph.options(it)) }
        val found = turns.filter { one -> one !in wolvesPositions && one !in visited }
        /* Если посетили все клетки, не нашлось ни одной "точки невозврата", возвращаю значение,
        равное изначальному при поиске максимального.
        */
        if (found.isEmpty()) return Int.MAX_VALUE
        for (wolf in wolvesPositions) {
            for (sheepTurn in found) {
                if (sheepTurn in graph.optionsForSheep(wolf)) {
                    val possibleMoves = graph.optionsForSheep(sheepTurn).filter { it !in wolvesPositions }
                    /* Иначе возращаю глубину рекурсии - расстояние в шахматный клетках до
                    искомой вершины графа.
                    */
                    if (possibleMoves.isNotEmpty()) return depth
                }
            }
        }
        // Погружаюсь на следующую глубину рекурсии.
        return countCost(visited, found, wolvesPositions, depth + 1)
    }

    /* Существует конечное множество случаев, не покрываемое основной эвристикой.
    Их обработаю отдельно. Случаи взяты из научной статьи "Wolf und Schafe: Taktik"
    авторства Hansruedi Kaiser, 2002, описанной в курсовой работе.
    */
    private val specialCases = mutableSetOf(
            Case(listOf(graph.Node(5 to 0), graph.Node(6 to 1), graph.Node(6 to 2), graph.Node(7 to 3)), graph.Node(5 to 3), 3, graph.Node(6 to 3)),
            Case(listOf(graph.Node(5 to 0), graph.Node(5 to 1), graph.Node(6 to 2), graph.Node(6 to 3)), graph.Node(5 to 2), 0, graph.Node(4 to 1)),
            Case(listOf(graph.Node(5 to 0), graph.Node(6 to 1), graph.Node(6 to 2), graph.Node(7 to 3)), graph.Node(5 to 2), 3, graph.Node(6 to 3)),
            Case(listOf(graph.Node(4 to 1), graph.Node(5 to 1), graph.Node(6 to 2), graph.Node(6 to 3)), graph.Node(4 to 2), 3, graph.Node(5 to 2)),
            Case(listOf(graph.Node(4 to 1), graph.Node(5 to 1), graph.Node(6 to 2), graph.Node(5 to 2)), graph.Node(3 to 2), 3, graph.Node(4 to 3)),
            Case(listOf(graph.Node(4 to 1), graph.Node(5 to 1), graph.Node(5 to 2), graph.Node(4 to 3)), graph.Node(3 to 1), 2, graph.Node(4 to 2)),
            Case(listOf(graph.Node(4 to 1), graph.Node(5 to 1), graph.Node(4 to 2), graph.Node(4 to 3)), graph.Node(2 to 1), 0, graph.Node(3 to 0)),
            Case(listOf(graph.Node(4 to 1), graph.Node(5 to 1), graph.Node(6 to 2), graph.Node(5 to 2)), graph.Node(3 to 1), 3, graph.Node(4 to 2)),
            Case(listOf(graph.Node(4 to 1), graph.Node(5 to 1), graph.Node(4 to 2), graph.Node(3 to 2)), graph.Node(1 to 3), 3, graph.Node(2 to 3)),
            Case(listOf(graph.Node(4 to 1), graph.Node(5 to 1), graph.Node(6 to 2), graph.Node(4 to 3)), graph.Node(2 to 1), 0, graph.Node(3 to 0)),
            Case(listOf(graph.Node(3 to 0), graph.Node(5 to 1), graph.Node(6 to 2), graph.Node(4 to 3)), graph.Node(3 to 1), 1, graph.Node(4 to 1)),
            Case(listOf(graph.Node(4 to 1), graph.Node(5 to 1), graph.Node(6 to 2), graph.Node(4 to 2)), graph.Node(2 to 1), 0, graph.Node(3 to 0)),
            Case(listOf(graph.Node(3 to 1), graph.Node(4 to 2), graph.Node(5 to 2), graph.Node(4 to 3)), graph.Node(1 to 1), 0, graph.Node(2 to 1)),
            Case(listOf(graph.Node(2 to 1), graph.Node(4 to 2), graph.Node(5 to 2), graph.Node(4 to 3)), graph.Node(2 to 2), 1, graph.Node(3 to 1)),
            Case(listOf(graph.Node(4 to 1), graph.Node(5 to 1), graph.Node(4 to 2), graph.Node(3 to 2)), graph.Node(1 to 2), 3, graph.Node(2 to 3)),
            Case(listOf(graph.Node(4 to 1), graph.Node(5 to 1), graph.Node(4 to 2), graph.Node(2 to 2)), graph.Node(2 to 1), 0, graph.Node(3 to 0)),
            Case(listOf(graph.Node(3 to 0), graph.Node(0 to 2), graph.Node(2 to 2), graph.Node(2 to 3)), graph.Node(0 to 3), 2, graph.Node(1 to 2)),
            Case(listOf(graph.Node(2 to 2), graph.Node(3 to 2), graph.Node(4 to 3), graph.Node(6 to 3)), graph.Node(0 to 2), 0, graph.Node(1 to 1)),
            Case(listOf(graph.Node(1 to 1), graph.Node(3 to 2), graph.Node(4 to 3), graph.Node(6 to 3)), graph.Node(1 to 2), 1, graph.Node(2 to 2)),
            Case(listOf(graph.Node(2 to 2), graph.Node(2 to 3), graph.Node(4 to 3), graph.Node(5 to 3)), graph.Node(0 to 2), 0, graph.Node(1 to 1))
    )

    /* Проверка особых случаев. Сравнение списков на равенство осуществляется не самым оптимальным
    алгоритмов, но с использованием функций высшего порядка, что облегчает чтение кода.
    */
    private fun checkInSpecialCases(wolvesPosition: List<Graph.Node>, sheepPosition: Graph.Node): Boolean {
        for (case in specialCases) {
            if (case.wolvesPosition.zip(wolvesPosition).all { (first, second) -> first == second && sheepPosition == case.sheepPosition }) {
                wolves[case.wolfIndex].move(case.turn)
                return true
            }
        }
        return false
    }
}

