package tests

import aleksey.vasiliev.wolfandsheep.helpers.Configuration
import aleksey.vasiliev.wolfandsheep.helpers.Graph
import kotlin.test.*

/* Классы-обертки (с суффиксом Wrapper) ничем не отличаются от соответствующих им за
исключением отвязки от текстуры и упрощения кода посредством исключения методов отрисовки.
В данном классе представлены тесты для основной логики приложения.
*/

class Tests {
    private val testGraph = Graph()
    @Test
    fun checkGraph() {
        testGraph.create()
        val node = testGraph.Node(0 to 3)
        assertEquals(testGraph.options(node), listOf(node.ne(), node.nw()))
        assertNotEquals(testGraph.options(node), listOf(node.se(), node.sw()))

        assertEquals(testGraph.optionsForSheep(node), listOf(node.ne(), node.nw()))
        assertNotEquals(testGraph.optionsForSheep(node), listOf(node.se(), node.sw()))

        assertEquals(testGraph.optionsForWolf(node), listOf())
        assertNotEquals(testGraph.optionsForWolf(node), listOf(node.se(), node.sw()))
    }

    @Test
    fun checkSheepAI() {
        testGraph.create()
        val configuration = Configuration.WOLF
        val sheep = SheepWrapper(configuration, testGraph)
        val wolves = mutableListOf<WolfWrapper>()
        for (i in 0..3) {
            wolves.add(WolfWrapper(configuration, i, testGraph))
        }
        val ai = SheepAIWrapper(sheep, wolves)
        val before = sheep.node.coordinates
        wolves[0].move(testGraph.Node(6 to 0))
        ai.move()
        val after = sheep.node.coordinates
        assertTrue(after.first < before.first)
        ai.move()
        wolves[0].move(testGraph.Node(5 to 0))
        val afterAfter = sheep.node.coordinates
        assertFalse(after.first < afterAfter.first)
    }

    @Test
    fun checkWolfAI() {
        testGraph.create()
        val configuration = Configuration.SHEEP
        val sheep = SheepWrapper(configuration, testGraph)
        val wolves = mutableListOf<WolfWrapper>()
        for (i in 0..3) {
            wolves.add(WolfWrapper(configuration, i, testGraph))
        }
        val ai = WolfAIWrapper(sheep, wolves)
        val before = wolves[0].node.coordinates
        sheep.move(testGraph.Node(6 to 0))
        ai.move()
        val after = wolves[0].node.coordinates
        assertTrue(after.first < before.first)
        sheep.move(testGraph.Node(5 to 0))
        val afterAfter = sheep.node.coordinates
        assertFalse(after.first < afterAfter.first)
    }
}