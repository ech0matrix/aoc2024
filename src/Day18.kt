import java.util.PriorityQueue

fun main() {
    data class State(
        val position: Coordinates,
        val numSteps: Int
    )

    fun part1(input: List<String>, maxRow: Int, maxCol: Int, numBytes: Int): Int {
        val walls = input.subList(0, numBytes).map { line ->
            val nums = line.split(',')
            Coordinates(nums[1].toInt(), nums[0].toInt())
        }.toSet()

        val start = Coordinates(0,0)
        val end = Coordinates(maxRow, maxCol)
        val visited = mutableSetOf<Coordinates>()
        val active = PriorityQueue<State>(compareBy { it.numSteps })
        active.offer(State(start, 0))

        var lowestNumSteps = Int.MAX_VALUE
        while(active.isNotEmpty()) {
            val current = active.poll()!!

            if (current.position == end) {
                // Found the end
                lowestNumSteps = minOf(current.numSteps, lowestNumSteps)
            } else if (current.numSteps < lowestNumSteps) {
                listOf(
                    current.position.add(Direction.NORTH),
                    current.position.add(Direction.SOUTH),
                    current.position.add(Direction.EAST),
                    current.position.add(Direction.WEST)
                ).filter { option ->
                    option.row in 0..maxRow
                        && option.col in 0..maxCol
                        && !walls.contains(option)
                        && !visited.contains(option)
                }.forEach { option ->
                    visited.add(option)
                    active.offer(State(option, current.numSteps + 1))
                }
            }
        }

        return lowestNumSteps
    }

    fun part2(input: List<String>, maxRow: Int, maxCol: Int, startFromByte: Int): String {
        var numBytes = startFromByte
        while(part1(input, maxRow, maxCol, numBytes) != Int.MAX_VALUE) {
            numBytes += 1
        }

        return input[numBytes-1]
    }

    val testInput = readInput("Day18_test")
    checkEquals(22, part1(testInput, 6, 6, 12))
    checkEquals("6,1", part2(testInput, 6, 6, 13))

    val input = readInput("Day18")
    println(timeIt{part1(input, 70, 70, 1024)})
    println(timeIt{part2(input, 70, 70, 1025)})
}
