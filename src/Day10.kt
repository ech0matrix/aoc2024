import java.util.LinkedList
import java.util.Queue

fun main() {
    fun part1(input: List<String>): Int {
        val map = input.flatMapIndexed{ row, line ->
            line.mapIndexed{ col, height ->
                Pair(Coordinates(row, col), height.digitToInt())
            }
        }.toMap()

        val trailheads = map.filterValues { it == 0 }.keys

        return trailheads.sumOf { trailhead ->
            val visited = mutableSetOf<Coordinates>()
            val trailends = mutableSetOf<Coordinates>()

            val next: Queue<Coordinates> = LinkedList()
            next.add(trailhead)

            while (!next.isEmpty()) {
                val current = next.remove()
                if (!visited.contains(current)) {
                    visited.add(current)
                    val height = map[current]!!
                    if (height == 9) {
                        trailends.add(current)
                    } else {
                        val nextHeight = height + 1
                        val nextOptions = listOf(
                            current.add(Direction.NORTH),
                            current.add(Direction.SOUTH),
                            current.add(Direction.EAST),
                            current.add(Direction.WEST)
                        ).filter { map[it] == nextHeight }
                        next.addAll(nextOptions)
                    }
                }
            }

            trailends.count()
        }
    }

    fun part2(input: List<String>): Int {
        val map = input.flatMapIndexed{ row, line ->
            line.mapIndexed{ col, height ->
                Pair(Coordinates(row, col), height.digitToInt())
            }
        }.toMap()

        val trailheads = map.filterValues { it == 0 }.keys

        return trailheads.sumOf { trailhead ->
            val trailends = mutableListOf<Coordinates>()

            val next: Queue<Coordinates> = LinkedList()
            next.add(trailhead)

            while (!next.isEmpty()) {
                val current = next.remove()
                val height = map[current]!!
                if (height == 9) {
                    trailends.add(current)
                } else {
                    val nextHeight = height + 1
                    val nextOptions = listOf(
                            current.add(Direction.NORTH),
                            current.add(Direction.SOUTH),
                            current.add(Direction.EAST),
                            current.add(Direction.WEST)
                    ).filter { map[it] == nextHeight }
                    next.addAll(nextOptions)
                }
            }

            trailends.count()
        }
    }

    val testInput = readInput("Day10_test")
    checkEquals(36, part1(testInput))
    checkEquals(81, part2(testInput))

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
