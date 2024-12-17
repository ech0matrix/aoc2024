import java.lang.IllegalArgumentException
import kotlin.math.pow

fun main() {
    data class Program(
        val a: Long,
        val b: Long,
        val c: Long,
        val instructions: String
    )

    fun parse(input: List<String>): Program {
        //Register A: 729
        //Register B: 0
        //Register C: 0
        //
        //Program: 0,1,5,4,3,0
        return Program(
            input[0].substring(12).toLong(),
            input[1].substring(12).toLong(),
            input[2].substring(12).toLong(),
            input[4].substring(9)
        )
    }

    fun comboOperand(operand: Int, a: Long, b: Long, c: Long): Long {
        return when(operand) {
            0,1,2,3 -> operand.toLong()
            4 -> a
            5 -> b
            6 -> c
            else -> throw IllegalArgumentException("Bad operand: $operand")
        }
    }

    fun part1(program: Program): String {
        val out = mutableListOf<Int>()
        var a = program.a
        var b = program.b
        var c = program.c
        val instructions = program.instructions.split(',').map(String::toInt)
        var ip = 0

        while(ip in instructions.indices) {
            val instruction = instructions[ip]
            val operand = instructions[ip+1]

            when(instruction) {
                0 -> {
                    // adv: A-register division, store in A
                    a = (a / 2.0.pow(comboOperand(operand,a,b,c).toInt())).toLong()
                }
                1 -> {
                    // bxl: bitwise XOR, store in B
                    b = b xor operand.toLong()
                }
                2 -> {
                    // bst: Store in B
                    b = comboOperand(operand,a,b,c) % 8
                }
                3 -> {
                    // jnz: Jump not zero
                    if (a != 0L) {
                        ip = operand - 2 // Subtract two to offset for increment at the end
                    }
                }
                4 -> {
                    // bxc: B xor C
                    b = b xor c
                }
                5 -> {
                    // out: output
                    out.add((comboOperand(operand,a,b,c) % 8).toInt())
                }
                6 -> {
                    // bdv: A-register division, store in B
                    b = (a / 2.0.pow(comboOperand(operand,a,b,c).toInt())).toLong()
                }
                7 -> {
                    // cdv: A-register division, store in C
                    c = (a / 2.0.pow(comboOperand(operand,a,b,c).toInt())).toLong()
                }
                else -> throw IllegalArgumentException("Bad instruction: $instruction")
            }

            ip += 2
        }

        return out.joinToString(",")
    }

    //                 0: 000
    //               3,0: 111 010
    //             5,3,0: 111 010 110
    //           5,5,3,0: 111 010 110 000
    //         3,5,5,3,0: 111 010 110 000 000
    //       0,3,5,5,3,0: 111 010 110 000 000 010
    //     6,0,3,5,5,3,0: 111 100 010 001 001 001 101
    //   4,6,0,3,5,5,3,0: 111 100 010 001 001 001 101 000

    fun part2(program: Program): Long {
        val instructions = program.instructions.split(',').map(String::toInt)

        var fewerInstructions = instructions.subList(instructions.size-4, instructions.size)
        var fewerInstructionsString = fewerInstructions.joinToString(",")
        var a = 0L
        while(part1(program.copy(a = a)) != fewerInstructionsString) {
            a += 1L
        }

        var nextA = a
        while (fewerInstructions.size < instructions.size) {
            fewerInstructions = instructions.subList(instructions.size-fewerInstructions.size-1, instructions.size)
            fewerInstructionsString = fewerInstructions.joinToString(",")
            a = nextA * 8
            while(part1(program.copy(a = a)) != fewerInstructionsString) {
                a += 1L
            }
            nextA = a
        }

        return nextA
    }

    checkEquals("0,1,2", part1(Program(10,0,0,"5,0,5,1,5,4")))
    checkEquals("4,2,5,6,7,7,7,7,3,1,0", part1(Program(2024,0,0,"0,1,5,4,3,0")))

    val testInput = readInput("Day17_test")
    checkEquals("4,6,3,5,6,3,5,2,1,0", part1(parse(testInput)))

    val testInput2 = readInput("Day17_test2")
    checkEquals(117440, part2(parse(testInput2)))

    val input = readInput("Day17")
    println(part1(parse(input)))
    println(timeIt{part2(parse(input))})
}
