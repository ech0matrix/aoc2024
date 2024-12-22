fun main() {
    //To mix a value into the secret number, calculate the bitwise XOR of the given value and the secret number. Then, the secret number becomes the result of that operation.
    fun mix(secret: Long, num: Long): Long {
        return secret xor num
    }

    //To prune the secret number, calculate the value of the secret number modulo 16777216. Then, the secret number becomes the result of that operation.
    fun Long.prune(): Long {
        return this % 16777216L
    }

    //Calculate the result of multiplying the secret number by 64. Then, mix this result into the secret number. Finally, prune the secret number.
    //Calculate the result of dividing the secret number by 32. Round the result down to the nearest integer. Then, mix this result into the secret number. Finally, prune the secret number.
    //Calculate the result of multiplying the secret number by 2048. Then, mix this result into the secret number. Finally, prune the secret number.
    fun computeNextSecret(num: Long): Long {
        var secret = num

        val mult1 = secret * 64
        secret = mix(secret, mult1)
        secret = secret.prune()

        val div = secret / 32
        secret = mix(secret, div)
        secret = secret.prune()

        val mult2 = secret * 2048
        secret = mix(secret, mult2)
        secret = secret.prune()

        return secret
    }

    fun part1(input: List<String>): Long {
        return input.sumOf { line ->
            var secret = line.toLong()
            repeat(2000) {
                secret = computeNextSecret(secret)
            }
            secret
        }
    }

    fun addPrice(secret: Long, prices: MutableList<Long>, diff: MutableList<Long>, pricePatterns: MutableMap<List<Long>, Long>) {
        prices.add(secret % 10L)
        val lastIndex = prices.size-1
        if (lastIndex > 0) {
            diff.add(prices[lastIndex] - prices[lastIndex-1])
        } else {
            diff.add(0)
        }

        if (lastIndex >= 4 ) {
            val pattern = listOf(diff[lastIndex-3],diff[lastIndex-2],diff[lastIndex-1],diff[lastIndex])
            if (pricePatterns[pattern] == null) {
                pricePatterns[pattern] = prices[lastIndex]
            }
        }
    }

    fun part2(input: List<String>): Long {
        val pricePatternsByMonkey = input.map { line ->
            val prices = mutableListOf<Long>()
            val diff = mutableListOf<Long>()
            val pricePatterns = mutableMapOf<List<Long>, Long>()

            var secret = line.toLong()
            repeat(2000) {
                addPrice(secret, prices, diff, pricePatterns)
                secret = computeNextSecret(secret)
            }
            addPrice(secret, prices, diff, pricePatterns)
            pricePatterns
        }

        val totalPricePatterns = pricePatternsByMonkey.reduce { acc, pricePattern ->
            for((pattern, price) in pricePattern) {
                acc[pattern] = (acc[pattern] ?: 0) + price
            }
            acc
        }

        return totalPricePatterns.maxOf { it.value }
    }

    val testInput = readInput("Day22_test")
    val testInput2 = readInput("Day22_test2")
    checkEquals(37327623L, part1(testInput))
    checkEquals(23L, part2(testInput2))

    val input = readInput("Day22")
    println(timeIt{part1(input)})
    println(timeIt{part2(input)})
}
