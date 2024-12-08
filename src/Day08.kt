fun main() {
    fun parse(input: List<String>): Map<Char, Set<Coordinates>> {
        return input.flatMapIndexed{ row, line ->
            line.mapIndexed{ col, c ->
                if (c != '.') {
                    Pair(Coordinates(row, col), c)
                } else {
                    null
                }
            }
        }.filterNotNull()
        .groupBy(Pair<Coordinates, Char>::second, Pair<Coordinates, Char>::first)
        .mapValues { (_, v) -> v.toSet() }
    }

    fun part1(input: List<String>): Int {
        val antennas = parse(input)

        val antinodes = mutableSetOf<Coordinates>()
        antennas.forEach{ (_, antennaSet) ->
            antennaSet.forEach { antenna ->
                val otherAntennas = antennaSet.minus(antenna)
                otherAntennas.forEach { other ->
                    val slope = antenna.subtract(other)
                    antinodes.add(antenna.add(slope))
                }
            }
        }

        return antinodes.count { (row, col) ->
            row in input.indices && col in input[row].indices
        }
    }

    fun part2(input: List<String>): Int {
        val antennas = parse(input)

        val antinodes = mutableSetOf<Coordinates>()
        antennas.forEach{ (_, antennaSet) ->
            antennaSet.forEach { antenna ->
                val otherAntennas = antennaSet.minus(antenna)
                otherAntennas.forEach { other ->
                    val slope = antenna.subtract(other)
                    var antinode = antenna
                    while(antinode.row in input.indices && antinode.col in input[0].indices) {
                        antinodes.add(antinode)
                        antinode = antinode.add(slope)
                    }
                }
            }
        }

        return antinodes.count()
    }

    val testInput = readInput("Day08_test")
    checkEquals(14, part1(testInput))
    checkEquals(34, part2(testInput))

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}
