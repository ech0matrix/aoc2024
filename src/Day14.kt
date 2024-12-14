fun main() {
    data class Robot(
        val x: Long,
        val y: Long,
        val mx: Long,
        val my: Long
    )

    fun parse(input: List<String>): List<Robot> {
        return input.map { line ->
            // p=0,4 v=3,-3
            // p=x,y p=0,0
            val parts = line.split(' ')
            val position = parts[0].substring(2).split(',').map(String::toLong)
            val velocity = parts[1].substring(2).split(',').map(String::toLong)
            Robot(position[0], position[1], velocity[0], velocity[1])
        }
    }

    fun part1(input: List<String>, xBounds: Long, yBounds: Long, seconds: Long): Long {
        val robots = parse(input)

        val movedRobots = robots.map { robot ->
            var x = (robot.x + (robot.mx * seconds)) % xBounds
            var y = (robot.y + (robot.my * seconds)) % yBounds
            if (x < 0) x += xBounds
            if (y < 0) y += yBounds
            robot.copy(x = x, y = y)
        }

        // Count of robots in quadrants (numbered 1-4, left to right / top to bottom)
        var q1 = 0L
        var q2 = 0L
        var q3 = 0L
        var q4 = 0L
        val topHalf = 0..<(yBounds/2L)
        val bottomHalf = (yBounds/2L + 1L)..<yBounds
        val leftHalf = 0..<(xBounds/2L)
        val rightHalf = (xBounds/2L + 1L)..<xBounds
        movedRobots.forEach { robot ->
            if (robot.y in topHalf && robot.x in leftHalf) {
                q1 += 1
            } else if (robot.y in topHalf && robot.x in rightHalf) {
                q2 += 1
            } else if (robot.y in bottomHalf && robot.x in leftHalf) {
                q3 += 1
            } else if (robot.y in bottomHalf && robot.x in rightHalf) {
                q4 += 1
            }
        }

        return q1 * q2 * q3 * q4
    }

    fun checkForTree(robots: List<Robot>): Boolean {
        val positions = robots.map{ Coordinates(it.y.toInt(), it.x.toInt()) }

        // Check for top of tree (triangle)
        return positions.any { position ->
            positions.contains(Coordinates(position.row+1, position.col-1))
                && positions.contains(Coordinates(position.row+1, position.col+1))
                && positions.contains(Coordinates(position.row+2, position.col-2))
                && positions.contains(Coordinates(position.row+2, position.col+2))
                && positions.contains(Coordinates(position.row+3, position.col-3))
                && positions.contains(Coordinates(position.row+3, position.col+3))
        }
    }

    fun part2(input: List<String>, xBounds: Long, yBounds: Long): Long {
        var robots = parse(input)

        var seconds = 0L
        while(!checkForTree(robots)) {
            robots = robots.map { robot->
                var x = (robot.x + robot.mx) % xBounds
                var y = (robot.y + robot.my) % yBounds
                if (x < 0) x += xBounds
                if (y < 0) y += yBounds
                robot.copy(x = x, y = y)
            }

            seconds += 1L
        }

        val positions = robots.map{ Coordinates(it.y.toInt(), it.x.toInt()) }
        for(row in 0..<yBounds) {
            for(col in 0..<xBounds) {
                if (positions.contains(Coordinates(row.toInt(), col.toInt()))) {
                    print('#')
                } else {
                    print('.')
                }
            }
            println()
        }

        return seconds
    }

    val testInput = readInput("Day14_test")
    checkEquals(12, part1(testInput, 11L, 7L, 100L))

    val input = readInput("Day14")
    println(part1(input, 101L, 103L, 100L))
    println(part2(input, 101L, 103L))
}
