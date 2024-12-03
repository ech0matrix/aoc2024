fun main() {
    fun part1(input: List<String>): Int {
        val code = input.joinToString("__")
        val regex = "mul\\(\\d+,\\d+\\)".toRegex()
        val validCommands = regex.findAll(code).toList().map { it.value }
        return validCommands.sumOf { command ->
            val nums = command.split("mul(", ",", ")")
                .filter { it.isNotEmpty() }
                .map { it.toInt() }
            checkEquals(2, nums.size)
            nums[0]*nums[1]
        }
    }

    fun part2_wrong_answer(input: List<String>): Int {
        val code = input.joinToString("__")
        val disabledRegex = "don't\\(\\).*?do\\(\\)".toRegex()
        val disabledChunks = disabledRegex.findAll(code).toList()
            .map { it.value }
        val enabledCode = disabledChunks.fold(code) { modifiedCode, disabledChunk ->
            modifiedCode.replace(disabledChunk, "__")
        }

        return part1(listOf(enabledCode))
    }

    fun part2(input: List<String>): Int {
        val code = input.joinToString("__")
        val modifiedCodeBuilder = StringBuilder()
        modifiedCodeBuilder.append(code.substring(0, 7))
        var isEnabled = true
        for(i in 7 until code.length) {
            if (isEnabled) {
                modifiedCodeBuilder.append(code[i])

                if(code.substring(i-6, i+1) == "don't()") {
                    isEnabled = false
                }
            } else {
                if(code.substring(i-3, i+1) == "do()") {
                    isEnabled = true
                }
            }
        }
        val modifiedCode = modifiedCodeBuilder.toString()

        return part1(listOf(modifiedCode))
    }

    val testInput = readInput("Day03_test")
    checkEquals(161, part1(testInput))
    val testInput2 = readInput("Day03_test2")
    checkEquals(48, part2(testInput2))

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
