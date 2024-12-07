fun main() {
    fun part1(input: List<String>): Long {
        val equations = input.map { it.split(": ", " ") }
        return equations.filter { equation ->
            val testValue = equation[0].toLong()
            val operands = equation.subList(1, equation.size).map { it.toLong() }

            val tests = operands.fold(listOf<Long>()) { results, operand ->
                if (results.isEmpty()) {
                    listOf(operand)
                } else {
                    results.flatMap { num ->
                        listOf(num + operand, num * operand)
                    }
                }
            }

            tests.contains(testValue)
        }.sumOf { equation -> equation[0].toLong() }
    }

    fun part2(input: List<String>): Long {
        val equations = input.map { it.split(": ", " ") }
        return equations.filter { equation ->
            val testValue = equation[0].toLong()
            val operands = equation.subList(1, equation.size).map { it.toLong() }

            val tests = operands.fold(listOf<Long>()) { results, operand ->
                if (results.isEmpty()) {
                    listOf(operand)
                } else {
                    results.flatMap { num ->
                        listOf(
                            num + operand,
                            num * operand,
                            (num.toString() + operand.toString()).toLong()
                        )
                    }
                }
            }

            tests.contains(testValue)
        }.sumOf { equation -> equation[0].toLong() }
    }

    val testInput = readInput("Day07_test")
    checkEquals(3749L, part1(testInput))
    checkEquals(11387L, part2(testInput))

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}
