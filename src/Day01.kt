import kotlin.math.abs

fun main() {
    fun parse(input: List<String>): Pair<List<Int>,List<Int>> {
        val inputPairs = input.map {
            val nums = it.split(' ')
            Pair(nums.first(), nums.last())
        }
        val left = inputPairs.map { it.first.toInt() }.sorted()
        val right = inputPairs.map { it.second.toInt() }.sorted()
        return Pair(left, right)
    }

    fun part1(input: List<String>): Int {
        val (left, right) = parse(input)
        return left.zip(right) { a, b ->
            abs(a-b)
        }.sum()
    }

    fun part2(input: List<String>): Int {
        val (left, right) = parse(input)
        return left.sumOf { num ->
            val count = right.count { it == num }
            num * count
        }
    }

    val testInput = readInput("Day01_test")
    checkEquals(11, part1(testInput))
    checkEquals(31, part2(testInput))

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
