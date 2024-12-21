
abstract class Pad(
    protected var current: Char = 'A'
) {
    abstract fun press(button: Char): String

    abstract fun copy(): Pad
}

class NumPad(
    current: Char = 'A'
): Pad(current) {
    companion object {
        private val graph = mapOf(
                Pair('A', 'A') to "",
                Pair('A', '0') to "<",
                Pair('A', '1') to "^<<",
                Pair('A', '2') to "<^",
                Pair('A', '3') to "^",
                Pair('A', '4') to "^^<<",
                Pair('A', '5') to "<^^",
                Pair('A', '6') to "^^",
                Pair('A', '7') to "^^^<<",
                Pair('A', '8') to "<^^^",
                Pair('A', '9') to "^^^",
                Pair('0', 'A') to ">",
                Pair('0', '0') to "",
                Pair('0', '1') to "^<",
                Pair('0', '2') to "^",
                Pair('0', '3') to "^>",
                Pair('0', '4') to "^^<",
                Pair('0', '5') to "^^",
                Pair('0', '6') to "^^>",
                Pair('0', '7') to "^^^<",
                Pair('0', '8') to "^^^",
                Pair('0', '9') to "^^^>",
                Pair('1', 'A') to ">>v",
                Pair('1', '0') to ">v",
                Pair('1', '1') to "",
                Pair('1', '2') to ">",
                Pair('1', '3') to ">>",
                Pair('1', '4') to "^",
                Pair('1', '5') to "^>",
                Pair('1', '6') to "^>>",
                Pair('1', '7') to "^^",
                Pair('1', '8') to "^^>",
                Pair('1', '9') to "^^>>",
                Pair('2', 'A') to "v>",
                Pair('2', '0') to "v",
                Pair('2', '1') to "<",
                Pair('2', '2') to "",
                Pair('2', '3') to ">",
                Pair('2', '4') to "<^",
                Pair('2', '5') to "^",
                Pair('2', '6') to "^>",
                Pair('2', '7') to "<^^",
                Pair('2', '8') to "^^",
                Pair('2', '9') to "^^>",
                Pair('3', 'A') to "v",
                Pair('3', '0') to "<v",
                Pair('3', '1') to "<<",
                Pair('3', '2') to "<",
                Pair('3', '3') to "",
                Pair('3', '4') to "<<^",
                Pair('3', '5') to "<^",
                Pair('3', '6') to "^",
                Pair('3', '7') to "<<^^",
                Pair('3', '8') to "<^^",
                Pair('3', '9') to "^^",
                Pair('4', 'A') to ">>vv",
                Pair('4', '0') to ">vv",
                Pair('4', '1') to "v",
                Pair('4', '2') to "v>",
                Pair('4', '3') to "v>>",
                Pair('4', '4') to "",
                Pair('4', '5') to ">",
                Pair('4', '6') to ">>",
                Pair('4', '7') to "^",
                Pair('4', '8') to "^>",
                Pair('4', '9') to "^>>",
                Pair('5', 'A') to "vv>",
                Pair('5', '0') to "vv",
                Pair('5', '1') to "<v",
                Pair('5', '2') to "v",
                Pair('5', '3') to "v>",
                Pair('5', '4') to "<",
                Pair('5', '5') to "",
                Pair('5', '6') to ">",
                Pair('5', '7') to "<^",
                Pair('5', '8') to "^",
                Pair('5', '9') to "^>",
                Pair('6', 'A') to "vv",
                Pair('6', '0') to "<vv",
                Pair('6', '1') to "<<v",
                Pair('6', '2') to "<v",
                Pair('6', '3') to "v",
                Pair('6', '4') to "<<",
                Pair('6', '5') to "<",
                Pair('6', '6') to "",
                Pair('6', '7') to "^<<",
                Pair('6', '8') to "<^",
                Pair('6', '9') to "^",
                Pair('7', 'A') to ">>vvv",
                Pair('7', '0') to ">vvv",
                Pair('7', '1') to "vv",
                Pair('7', '2') to "vv>",
                Pair('7', '3') to "vv>>",
                Pair('7', '4') to "v",
                Pair('7', '5') to "v>",
                Pair('7', '6') to "v>>",
                Pair('7', '7') to "",
                Pair('7', '8') to ">",
                Pair('7', '9') to ">>",
                Pair('8', 'A') to "vvv>",
                Pair('8', '0') to "vvv",
                Pair('8', '1') to "<vv",
                Pair('8', '2') to "vv",
                Pair('8', '3') to "vv>",
                Pair('8', '4') to "<v",
                Pair('8', '5') to "v",
                Pair('8', '6') to "v>",
                Pair('8', '7') to "<",
                Pair('8', '8') to "",
                Pair('8', '9') to ">",
                Pair('9', 'A') to "vvv",
                Pair('9', '0') to "<vvv",
                Pair('9', '1') to "<<vv",
                Pair('9', '2') to "<vv",
                Pair('9', '3') to "vv",
                Pair('9', '4') to "<<v",
                Pair('9', '5') to "<v",
                Pair('9', '6') to "v",
                Pair('9', '7') to "<<",
                Pair('9', '8') to "<",
                Pair('9', '9') to "",
        ).mapValues { (_, route) -> route + "A" }
    }

    override fun press(button: Char): String {
        check(button in '0'..'9' || button == 'A')
        val keyPresses = graph[Pair(current, button)]!!
        current = button
        return keyPresses
    }

    override fun copy(): Pad {
        return NumPad(current)
    }
}

class DirectionPad(
    current: Char = 'A'
): Pad(current) {
    companion object {
        val graph = mapOf(
                Pair('A', 'A') to "",
                Pair('A', '^') to "<",
                Pair('A', '<') to "v<<",
                Pair('A', 'v') to "<v",
                Pair('A', '>') to "v",
                Pair('^', 'A') to ">",
                Pair('^', '^') to "",
                Pair('^', '<') to "v<",
                Pair('^', 'v') to "v",
                Pair('^', '>') to "v>",
                Pair('<', 'A') to ">>^",
                Pair('<', '^') to ">^",
                Pair('<', '<') to "",
                Pair('<', 'v') to ">",
                Pair('<', '>') to ">>",
                Pair('v', 'A') to "^>",
                Pair('v', '^') to "^",
                Pair('v', '<') to "<",
                Pair('v', 'v') to "",
                Pair('v', '>') to ">",
                Pair('>', 'A') to "^",
                Pair('>', '^') to "<^",
                Pair('>', '<') to "<<",
                Pair('>', 'v') to "<",
                Pair('>', '>') to "",
        ).mapValues { (_, route) -> route + "A" }

        private val validKeys = setOf('A', '^', 'v', '<', '>')
    }

    override fun press(button: Char): String {
        check(validKeys.contains(button))
        val keyPresses = graph[Pair(current, button)]!!
        current = button
        return keyPresses
    }

    override fun copy(): Pad {
        return DirectionPad(current)
    }
}

fun main() {
    fun part1(doorCodes: List<String>): Long {
        val radiationCodes = doorCodes.map { doorCode ->
            val numPad = NumPad()
            doorCode.map { digit ->
                numPad.press(digit)
            }.joinToString("")
        }

        val frozenCodes = radiationCodes.map { radiationCode ->
            val directionPad = DirectionPad()
            radiationCode.map { digit ->
                directionPad.press(digit)
            }.joinToString("")
        }

        val myRemoteCodes = frozenCodes.map { frozenCode ->
            val directionPad = DirectionPad()
            frozenCode.map { digit ->
                directionPad.press(digit)
            }.joinToString("")
        }

        return doorCodes.zip(myRemoteCodes).sumOf { (num, code) ->
            val n = num.substring(0, num.length-1).toLong()
            val l = code.length.toLong()
            n * l
        }
    }

    fun nestedCounts(code: String, depth: Int, cache: MutableMap<Pair<String, Int>, Long>): Long {
        if (cache[Pair(code, depth)] != null) {
            return cache[Pair(code, depth)]!!
        }

        val directionPad = DirectionPad()
        return code.sumOf { digit ->
            val directions = directionPad.press(digit)
            if (depth > 1) {
                nestedCounts(directions, depth - 1, cache)
            } else {
                directions.length.toLong()
            }
        }.also {
            cache[Pair(code, depth)] = it
        }
    }

    fun part2(doorCodes: List<String>, nestedPadCount: Int): Long {
        val doorPadRobotCodes = doorCodes.map { doorCode ->
            val numPad = NumPad()
            doorCode.map { digit ->
                numPad.press(digit)
            }.joinToString("")
        }

        val cache = mutableMapOf<Pair<String, Int>, Long>()

        val myRemoteCodesLength = doorPadRobotCodes.map { nestedCounts(it, nestedPadCount, cache) }

        return doorCodes.zip(myRemoteCodesLength).sumOf { (num, codeLength) ->
            val n = num.substring(0, num.length-1).toLong()
            n * codeLength
        }
    }

    val testInput = readInput("Day21_test")
    checkEquals(126384L, part1(testInput))
    checkEquals(126384L, part2(testInput, 2))

    val input = readInput("Day21")
    println(timeIt{part1(input)})
    println(timeIt{part2(input, 25)})
}
