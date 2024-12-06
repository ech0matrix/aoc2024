fun main() {
    data class Guard(
        val position: Coordinates,
        val direction: Direction
    ) {
        fun walk() = this.copy(position = position.add(direction))

        fun turn() = this.copy(direction = when(direction) {
            Direction.NORTH -> Direction.EAST
            Direction.EAST -> Direction.SOUTH
            Direction.SOUTH -> Direction.WEST
            Direction.WEST -> Direction.NORTH
        })
    }

    // Returns Pair, obstruction positions and guard initial state
    fun parse(input: List<String>): Pair<Set<Coordinates>, Guard> {
        val obstructions = mutableSetOf<Coordinates>()
        lateinit var guard: Guard
        for(row in input.indices) {
            for(col in input[row].indices) {
                if (input[row][col] == '#') {
                    obstructions.add(Coordinates(row, col))
                } else if (input[row][col] == '^') {
                    guard = Guard(Coordinates(row, col), Direction.NORTH)
                }
            }
        }

        return Pair(obstructions, guard)
    }

    // Tracks guard until leaves bounds or loops
    // Returns Pair, isLoop and unique visit count
    fun trackGuard(obstructions: Set<Coordinates>, guard: Guard, maxBounds: Coordinates): Pair<Boolean, Int> {
        val visited = mutableSetOf<Guard>()
        var movingGuard = guard

        while(movingGuard.position.row in 0..<maxBounds.row && movingGuard.position.col in 0..<maxBounds.col && !visited.contains(movingGuard)) {
            visited.add(movingGuard)
            movingGuard = if (obstructions.contains(movingGuard.walk().position)) {
                movingGuard.turn()
            } else {
                movingGuard.walk()
            }
        }

        return Pair(visited.contains(movingGuard), visited.map(Guard::position).toSet().count())
    }

    fun part1(input: List<String>): Int {
        val (obstructions, guard) = parse(input)

        val (_, result) = trackGuard(obstructions, guard, Coordinates(input.size, input[0].length))

        return result
    }

    fun part2(input: List<String>): Int {
        val (obstructions, guard) = parse(input)
        val maxBounds = Coordinates(input.size, input[0].length)

        var count = 0
        for (row in input.indices) {
            for (col in input[row].indices) {
                val current = Coordinates(row, col)
                if (!obstructions.contains(current)) {
                    val newSet = obstructions + current
                    val (isLoop, _) = trackGuard(newSet, guard, maxBounds)
                    if (isLoop) {
                        count += 1
                    }
                }
            }
        }

        return count
    }

    val testInput = readInput("Day06_test")
    checkEquals(41, part1(testInput))
    checkEquals(6, part2(testInput))

    val input = readInput("Day06")
    println(part1(input))
    println(timeIt{part2(input)})
}
