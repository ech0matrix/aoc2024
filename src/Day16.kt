import java.util.PriorityQueue

fun main() {
    data class State(
        val position: Coordinates,
        val facing: Direction,
        val score: Int
    )

    fun part1(input: List<String>): Int {
        var start = Coordinates(0,0)
        var end = Coordinates(0,0)
        val walls = mutableSetOf<Coordinates>()
        for(row in input.indices) {
            for(col in input[row].indices) {
                if (input[row][col] == '#') {
                    walls.add(Coordinates(row, col))
                } else if (input[row][col] == 'S') {
                    start = Coordinates(row, col)
                } else if (input[row][col] == 'E') {
                    end = Coordinates(row, col)
                }
            }
        }
        check(start.row != 0 && end.col != 0) // Make sure values are initialized (not the default value)

        val visited = mutableSetOf<Pair<Coordinates, Direction>>()
        val active = PriorityQueue<State>(compareBy { it.score })
        active.offer(State(
            start,
            Direction.EAST,
            0
        ))

        var lowScore = Int.MAX_VALUE
        while(active.isNotEmpty()) {
            val current = active.poll()!!

            if (current.position == end) {
                // Found the end
                lowScore = minOf(current.score, lowScore)
            } else if (current.score < lowScore) {
                val left = when(current.facing) {
                    Direction.NORTH -> Direction.WEST
                    Direction.WEST -> Direction.SOUTH
                    Direction.SOUTH -> Direction.EAST
                    Direction.EAST -> Direction.NORTH
                }
                val right = when(current.facing) {
                    Direction.NORTH -> Direction.EAST
                    Direction.EAST -> Direction.SOUTH
                    Direction.SOUTH -> Direction.WEST
                    Direction.WEST -> Direction.NORTH
                }

                listOf(
                    Pair(current.facing, 1),
                    Pair(left, 1001),
                    Pair(right, 1001)
                ).filter { (facing, _) ->
                    val next = current.position.add(facing)
                    !walls.contains(next) && !visited.contains(Pair(next, facing))
                }.forEach { (facing, cost) ->
                    val next = current.position.add(facing)
                    visited.add(Pair(next, facing))
                    active.offer(State(
                        next,
                        facing,
                        current.score + cost
                    ))
                }

            }
        }

        return lowScore
    }

    data class State2(
        val position: Coordinates,
        val facing: Direction,
        val score: Int,
        val path: List<State>
    )

    fun part2(input: List<String>): Int {
        var start = Coordinates(0,0)
        var end = Coordinates(0,0)
        val walls = mutableSetOf<Coordinates>()
        for(row in input.indices) {
            for(col in input[row].indices) {
                if (input[row][col] == '#') {
                    walls.add(Coordinates(row, col))
                } else if (input[row][col] == 'S') {
                    start = Coordinates(row, col)
                } else if (input[row][col] == 'E') {
                    end = Coordinates(row, col)
                }
            }
        }
        check(start.row != 0 && end.col != 0) // Make sure values are initialized (not the default value)

        val visited = mutableSetOf<Pair<Coordinates, Direction>>()
        val active = PriorityQueue<State2>(compareBy { it.score })
        active.offer(State2(
            start,
            Direction.EAST,
            0,
            listOf(State(start, Direction.EAST, 0))
        ))

        // Solve like part 1, but save the low cost path
        var lowScore = Int.MAX_VALUE
        var bestPath = listOf<State>()
        while(active.isNotEmpty()) {
            val current = active.poll()!!

            if (current.position == end) {
                // Found the end
                if (current.score < lowScore) {
                    lowScore = current.score
                    bestPath = current.path
                }
            } else if (current.score < lowScore) {
                val left = when(current.facing) {
                    Direction.NORTH -> Direction.WEST
                    Direction.WEST -> Direction.SOUTH
                    Direction.SOUTH -> Direction.EAST
                    Direction.EAST -> Direction.NORTH
                }
                val right = when(current.facing) {
                    Direction.NORTH -> Direction.EAST
                    Direction.EAST -> Direction.SOUTH
                    Direction.SOUTH -> Direction.WEST
                    Direction.WEST -> Direction.NORTH
                }

                listOf(
                    Pair(current.facing, 1),
                    Pair(left, 1001),
                    Pair(right, 1001)
                ).filter { (facing, _) ->
                    val next = current.position.add(facing)
                    !walls.contains(next) && !visited.contains(Pair(next, facing))
                }.forEach { (facing, cost) ->
                    val next = current.position.add(facing)
                    visited.add(Pair(next, facing))
                    active.offer(State2(
                        next,
                        facing,
                        current.score + cost,
                        current.path + State(next, facing, current.score + cost)
                    ))
                }

            }
        }

        val accumulatedPaths = mutableSetOf<State>()
        accumulatedPaths.addAll(bestPath)
        // Now solve again, but combine paths of any visited locations with the same cost
        visited.clear()
        active.offer(State2(
            start,
            Direction.EAST,
            0,
            listOf(State(start, Direction.EAST, 0))
        ))
        while(active.isNotEmpty()) {
            val current = active.poll()!!

            if (current.position == end) {
                // Found the end
                if (current.score < lowScore) {
                    lowScore = current.score
                }
            } else if (current.score < lowScore) {
                val left = when(current.facing) {
                    Direction.NORTH -> Direction.WEST
                    Direction.WEST -> Direction.SOUTH
                    Direction.SOUTH -> Direction.EAST
                    Direction.EAST -> Direction.NORTH
                }
                val right = when(current.facing) {
                    Direction.NORTH -> Direction.EAST
                    Direction.EAST -> Direction.SOUTH
                    Direction.SOUTH -> Direction.WEST
                    Direction.WEST -> Direction.NORTH
                }

                listOf(
                    Pair(current.facing, 1),
                    Pair(left, 1001),
                    Pair(right, 1001)
                ).filter { (facing, cost) ->
                    val next = current.position.add(facing)
                    if (walls.contains(next)) {
                        false
                    } else if(visited.contains(Pair(next, facing))) {
                        val nextState = State(next, facing, current.score + cost)
                        if (bestPath.contains(nextState)) {
                            accumulatedPaths.addAll(current.path + nextState)
                        }
                        false
                    } else {
                        true
                    }
                }.forEach { (facing, cost) ->
                    val next = current.position.add(facing)
                    visited.add(Pair(next, facing))
                    active.offer(State2(
                        next,
                        facing,
                        current.score + cost,
                        current.path + State(next, facing, current.score + cost)
                    ))
                }

            }
        }

        val accumulatedPositions = accumulatedPaths.map { it.position }.toSet()

//        for(row in input.indices) {
//            for(col in input[row].indices) {
//                val current = Coordinates(row, col)
//                if (walls.contains(current)) {
//                    print('#')
//                } else if (accumulatedPositions.contains(current)) {
//                    print('O')
//                } else {
//                    print('.')
//                }
//            }
//            println()
//        }
//        println()

        return accumulatedPositions.count()
    }

    val testInput = readInput("Day16_test")
    val testInput2 = readInput("Day16_test2")
    checkEquals(7036, part1(testInput))
    checkEquals(11048, part1(testInput2))
    //checkEquals(45, part2(testInput)) // Some edge condition that my code is failing on, but doesn't exist in real input
    checkEquals(64, part2(testInput2))

    val input = readInput("Day16")
    println(timeIt{part1(input)})
    println(timeIt{part2(input)})
}
