fun main() {
    fun part1(input: List<String>): Int {
        val locks = mutableListOf<List<Int>>()
        val keys = mutableListOf<List<Int>>()
        input.chunked(8).forEach { schematic ->
            val positions = mutableSetOf<Coordinates>()
            for(row in 1..<6) {
                for(col in 0..<schematic[row].length) {
                    if(schematic[row][col] == '#') {
                        positions.add(Coordinates(row, col))
                    }
                }
            }
            val columns = listOf(
                positions.count { it.col == 0 },
                positions.count { it.col == 1 },
                positions.count { it.col == 2 },
                positions.count { it.col == 3 },
                positions.count { it.col == 4 }
            )
            if(schematic[0] == "#####") {
                locks.add(columns)
            } else {
                keys.add(columns)
            }
        }

        var count = 0
        for(lock in locks) {
            for(key in keys) {
                var isFits = true
                for(i in 0..4) {
                    if (lock[i] + key[i] > 5) {
                        isFits = false
                        break
                    }
                }
                if (isFits) {
                    count += 1
                }
            }
        }

        return count
    }

    val testInput = readInput("Day25_test")
    checkEquals(3, part1(testInput))

    val input = readInput("Day25")
    println(part1(input))
}
