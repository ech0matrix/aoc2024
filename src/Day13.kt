fun main() {
    data class Button(
        val x: Long,
        val y: Long
    )

    data class Prize(
        val x: Long,
        val y: Long
    )

    data class ClawMachine(
        val a: Button,
        val b: Button,
        val prize: Prize
    )

    data class ButtonPresses(
        val a: Long,
        val b: Long
    )

    fun solve(machine: ClawMachine): ButtonPresses? {
        val aDividend = (machine.b.x * machine.prize.y) - (machine.b.y * machine.prize.x)
        val aDivisor = (machine.b.x * machine.a.y) - (machine.b.y * machine.a.x)

        if(aDividend % aDivisor != 0L) {
            return null
        }

        val a = aDividend / aDivisor

        val bDividend = (machine.prize.x) - (machine.a.x * a)
        val bDivisor = machine.b.x

        if(bDividend % bDivisor != 0L) {
            return null
        }

        val b = bDividend / bDivisor

        return ButtonPresses(a, b)
    }

    fun parse(input: List<String>): List<ClawMachine> {
        return input.chunked(4) { machineSpecs ->
            val aSpecs = machineSpecs[0].substring(12).split(", Y+").map(String::toLong)
            val bSpecs = machineSpecs[1].substring(12).split(", Y+").map(String::toLong)
            val prizeSpecs = machineSpecs[2].substring(9).split(", Y=").map(String::toLong)

            ClawMachine(
                    Button(aSpecs[0], aSpecs[1]),
                    Button(bSpecs[0], bSpecs[1]),
                    Prize(prizeSpecs[0], prizeSpecs[1])
            )
        }
    }

    fun part1(input: List<String>): Long {
        val machines = parse(input)

        return machines.mapNotNull { solve(it) }.sumOf { presses ->
            3 * presses.a + presses.b
        }
    }

    fun part2(input: List<String>): Long {
        val machines = parse(input).map { machine ->
            machine.copy(prize = Prize(machine.prize.x + 10000000000000L, machine.prize.y + 10000000000000L))
        }

        return machines.mapNotNull { solve(it) }.sumOf { presses ->
            3 * presses.a + presses.b
        }
    }

    val testInput = readInput("Day13_test")
    checkEquals(480L, part1(testInput))

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}
