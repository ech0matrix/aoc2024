
fun main() {
    data class Stone(
        val num: Long,
        val blinksRemaining: Int
    )

    fun computeCount(stone: Stone, cache: MutableMap<Stone, Long>): Long {
        if(stone.blinksRemaining == 0) {
            return 1L
        }

        if (cache.containsKey(stone)) {
            //println("Cache hit $stone, with count $count")
            return cache[stone]!!
        }

        // If the stone is engraved with the number 0,
        //         it is replaced by a stone engraved with the number 1.
        // If the stone is engraved with a number that has an even number of digits,
        //         it is replaced by two stones. The left half of the digits are engraved on the new left stone,
        //         and the right half of the digits are engraved on the new right stone.
        //         (The new numbers don't keep extra leading zeroes: 1000 would become stones 10 and 0.)
        //If none of the other rules apply, the stone is replaced by a new stone;
        //         the old stone's number multiplied by 2024 is engraved on the new stone.
        return if (stone.num == 0L) {
            computeCount(Stone(1, stone.blinksRemaining-1), cache)
        } else if (stone.num.toString().length % 2 == 0) {
            val str = stone.num.toString()
            val num1 = str.substring(0, str.length/2).toLong()
            val num2 = str.substring(str.length/2, str.length).toLong()
            val blinksRemaining = stone.blinksRemaining-1
            //println("${stone.num} split into $num1 and $num2")
            computeCount(Stone(num1, blinksRemaining), cache) + computeCount(Stone(num2, blinksRemaining), cache)
        } else {
            computeCount(Stone(stone.num * 2024L, stone.blinksRemaining-1), cache)
        }.also {
            cache[stone] = it
        }
    }

    fun part1(input: String, blinks: Int): Long {
        val stones = input.split(' ').map{ Stone(it.toLong(), blinks) }
        val cache = mutableMapOf<Stone, Long>()
        return stones.sumOf { stone ->
            computeCount(stone, cache)
        }
    }

//    No part2 needed today. Just change the blink count argument from part1.
//
//    fun part2(input: String, blinks: Int): Long {
//
//    }

    val testInput = readInput("Day11_test")[0]
    val testInput2 = readInput("Day11_test2")[0]
    checkEquals(7L, part1(testInput, 1))
    checkEquals(22L, part1(testInput2, 6))
    checkEquals(55312L, part1(testInput2, 25))

    val input = readInput("Day11")[0]
    println(timeIt{part1(input, 25)})
    println(timeIt{part1(input, 75)})
}
