import java.util.Queue
import java.util.LinkedList

fun main() {
    data class Gate(
        val in1Name: String,
        val in2Name: String,
        val outName: String,
        val operation: String
    ) {
        private var in1: Boolean? = null
        private var in2: Boolean? = null

        // Sets an input, and returns a value if both inputs are ready (null otherwise)
        fun setInput(name: String, value: Boolean): Boolean? {
            if(name == in1Name) {
                in1 = value
            } else if (name == in2Name) {
                in2 = value
            } else {
                throw IllegalArgumentException("Expected name $in1Name or $in2Name. Instead received: $name")
            }

            if (in1 == null || in2 == null) {
                return null
            }

            //AND gates output 1 if both inputs are 1; if either input is 0, these gates output 0.
            //OR gates output 1 if one or both inputs is 1; if both inputs are 0, these gates output 0.
            //XOR gates output 1 if the inputs are different; if the inputs are the same, these gates output 0.
            return when(operation) {
                "AND" -> in1!! && in2!!
                "OR" -> in1!! || in2!!
                "XOR" -> in1!! xor in2!!
                else -> throw IllegalStateException("Unknown operation: $operation")
            }
        }
    }

    fun wiresToNum(wireValues: Map<String, Boolean>, wirePrefix: Char): Pair<Long, String> {
        val wires = wireValues
                .filterKeys { it[0] == wirePrefix }
                .toList()
                .sortedBy { it.first }
                .reversed()
                .map { (_, value) ->
                    if (value) 1L else 0L
                }
        var number = 0L
        var str = ""
        for(bit in wires) {
            number = number shl 1
            number += bit
            str += bit.toString()
        }
        return Pair(number, str)
    }

    fun part1(input: List<String>): Long {
        val separator = input.indexOf("")
        val values = input.subList(0, separator).associate { line ->
            val parts = line.split(": ")
            val name = parts[0]
            val value: Boolean = parts[1] == "1"
            Pair(name, value)
        }.toMutableMap()
        val gates = input.subList(separator+1, input.size).map { line ->
            val parts = line.split(" -> ")
            checkEquals(2, parts.size)
            val output = parts[1]
            val gateDef = parts[0].split(' ')
            checkEquals(3, gateDef.size)
            Gate(
                in1Name = gateDef[0],
                in2Name = gateDef[2],
                outName = output,
                operation = gateDef[1]
            )
        }
        val wireToGateMap = mutableMapOf<String, MutableSet<Gate>>()
        for(gate in gates) {
            if(wireToGateMap[gate.in1Name] == null) {
                wireToGateMap[gate.in1Name] = mutableSetOf()
            }
            if(wireToGateMap[gate.in2Name] == null) {
                wireToGateMap[gate.in2Name] = mutableSetOf()
            }
            wireToGateMap[gate.in1Name]!!.add(gate)
            wireToGateMap[gate.in2Name]!!.add(gate)
        }

        val signals: Queue<String> = LinkedList()
        signals.addAll(values.keys)
        while(signals.isNotEmpty()) {
            val signalName = signals.remove()
            val signalValue = values[signalName]!!
            if (wireToGateMap[signalName] != null) {
                for (gate in wireToGateMap[signalName]!!) {
                    val outValue = gate.setInput(signalName, signalValue)
                    if (outValue != null) {
                        check(values[gate.outName] == null)
                        values[gate.outName] = outValue
                        signals.add(gate.outName)
                    }
                }
            }
        }

        return wiresToNum(values, 'z').first
    }

    // Swap file sound come in the format (with both directions listed):
    //   abc -> xyz
    //   xyz -> abc
    val swaps = readInput("Day24_swaps").map { line ->
        val wirePairs = line.split(" -> ")
        Pair(wirePairs[0], wirePairs[1])
    }.toMap()
    fun maybeSwap(wire: String): String {
        return swaps[wire] ?: wire
    }

    // This doesn't actually solve, but facilitates debugging of the addition by
    // testing swaps (via maybeSwap()), and then doing bit-by-bit addition to find
    // the least-significant bit that's incorrect.
    // Swaps were found by inspecting a graphviz diagram around the incorrect output bit.
    fun part2(input: List<String>): Long {
        val separator = input.indexOf("")
        val values = input.subList(0, separator).associate { line ->
            val parts = line.split(": ")
            val name = parts[0]
            val value: Boolean = parts[1] == "1"
            Pair(name, value)
        }.toMutableMap()

        // The real input only required 3 swaps to add correctly.
        // This mock input of all 1's on the x, and all 0's on the y helped to find the last swap.
        //for(value in values) {
        //    values[value.key] = value.key[0] == 'x'
        //}

        val gates = input.subList(separator+1, input.size).map { line ->
            val parts = line.split(" -> ")
            checkEquals(2, parts.size)
            val output = parts[1]
            val gateDef = parts[0].split(' ')
            checkEquals(3, gateDef.size)
            Gate(
                in1Name = gateDef[0],
                in2Name = gateDef[2],
                outName = maybeSwap(output),
                operation = gateDef[1]
            )
        }
        val wireToGateMap = mutableMapOf<String, MutableSet<Gate>>()
        for(gate in gates) {
            if(wireToGateMap[gate.in1Name] == null) {
                wireToGateMap[gate.in1Name] = mutableSetOf()
            }
            if(wireToGateMap[gate.in2Name] == null) {
                wireToGateMap[gate.in2Name] = mutableSetOf()
            }
            wireToGateMap[gate.in1Name]!!.add(gate)
            wireToGateMap[gate.in2Name]!!.add(gate)
        }

        val signals: Queue<String> = LinkedList()
        signals.addAll(values.keys)
        while(signals.isNotEmpty()) {
            val signalName = signals.remove()
            val signalValue = values[signalName]!!
            if (wireToGateMap[signalName] != null) {
                for (gate in wireToGateMap[signalName]!!) {
                    val outValue = gate.setInput(signalName, signalValue)
                    if (outValue != null) {
                        check(values[gate.outName] == null)
                        values[gate.outName] = outValue
                        signals.add(gate.outName)
                    }
                }
            }
        }

        val x = wiresToNum(values, 'x')
        val y = wiresToNum(values, 'y')
        val z = wiresToNum(values, 'z')

        println("x:  $x")
        println("y:  $y")
        println("z: ${if(z.first<60000000000000L) " " else ""}$z")

        // Compute first wrong bit in z
        var carry = 0L
        for(i in 0..<x.second.length) {
            val in1 = if(x.second[x.second.length-1-i] == '1') 1L else 0L
            val in2 = if(y.second[y.second.length-1-i] == '1') 1L else 0L
            var out = in1 + in2 + carry
            if (out >= 2L) {
                out -= 2L
                carry = 1L
            } else {
                carry = 0L
            }
            val actualOut = if(z.second[z.second.length-1-i] == '1') 1L else 0L
            if(actualOut != out) {
                print("                    ${if(z.first<60000000000000L) " " else ""}")
                repeat(z.second.length-1-i) {
                    print(" ")
                }
                println("X")
                break
            }
        }

        //println("                                ^")
        println("                     432109876543210987654321098765432109876543210")

        if(x.first + y.first != z.first) {
            println("z should equal: ${x.first+y.first}")
            println("z actual value: ${z.first}")
        } else {
            println("z is correct!")

            // From maybeSwap()
            println(swaps.keys.sorted().joinToString(","))
        }

        return wiresToNum(values, 'z').first
    }

    val testInput = readInput("Day24_test")
    val testInput2 = readInput("Day24_test2")
    checkEquals(4L, part1(testInput))
    checkEquals(2024L, part1(testInput2))

    val input = readInput("Day24")
    println(timeIt{part1(input)})

    println()
    println(part2(input))
}
