fun main() {
    fun isMAS(input: List<String>, maxRow: Int, maxCol: Int, coordinates: List<Coordinates>): Boolean {
        checkEquals(3, coordinates.size)

        val allValid = coordinates.all { (row, col) ->
            row in 0..<maxRow && col in 0..<maxCol
        }
        if (!allValid) {
            return false
        }

        if (input[coordinates[0].row][coordinates[0].col] != 'M') {
            return false
        }
        if (input[coordinates[1].row][coordinates[1].col] != 'A') {
            return false
        }
        if (input[coordinates[2].row][coordinates[2].col] != 'S') {
            return false
        }

        return true
    }

    fun part1(input: List<String>): Int {
        val maxRow = input.size
        val maxCol = input[0].length
        var count = 0
        for(row in input.indices) {
            for(col in input[row].indices) {
                if (input[row][col] == 'X') {
                    val possibilities = listOf(
                        listOf(Coordinates(row, col+1), Coordinates(row, col+2), Coordinates(row, col+3)),
                        listOf(Coordinates(row, col-1), Coordinates(row, col-2), Coordinates(row, col-3)),
                        listOf(Coordinates(row+1, col), Coordinates(row+2, col), Coordinates(row+3, col)),
                        listOf(Coordinates(row-1, col), Coordinates(row-2, col), Coordinates(row-3, col)),
                        listOf(Coordinates(row+1, col+1), Coordinates(row+2, col+2), Coordinates(row+3, col+3)),
                        listOf(Coordinates(row+1, col-1), Coordinates(row+2, col-2), Coordinates(row+3, col-3)),
                        listOf(Coordinates(row-1, col+1), Coordinates(row-2, col+2), Coordinates(row-3, col+3)),
                        listOf(Coordinates(row-1, col-1), Coordinates(row-2, col-2), Coordinates(row-3, col-3)),
                    )
                    count += possibilities.count { coordinates ->
                        isMAS(input, maxRow, maxCol, coordinates)
                    }
                }
            }
        }
        return count
    }

    fun part2(input: List<String>): Int {
        var count = 0
        for(row in 1..input.size-2) {
            for(col in 1..input[row].length-2) {
                if (input[row][col] == 'A') {
                    if (((input[row-1][col-1] == 'M' && input[row+1][col+1] == 'S')
                            || (input[row-1][col-1] == 'S' && input[row+1][col+1] == 'M'))
                        && ((input[row-1][col+1] == 'M' && input[row+1][col-1] == 'S')
                            || (input[row-1][col+1] == 'S' && input[row+1][col-1] == 'M'))) {
                        count += 1
                    }
                }
            }
        }
        return count
    }

    val testInput = readInput("Day04_test")
    checkEquals(18, part1(testInput))
    checkEquals(9, part2(testInput))

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
