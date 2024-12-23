fun main() {
    fun parse(input: List<String>): Map<String, Set<String>> {
        val connections = input.map { line ->
            val pair = line.split('-')
            Pair(pair[0], pair[1])
        }

        val computers = mutableMapOf<String, MutableSet<String>>()
        for((com1, com2) in connections) {
            if(computers[com1] == null) {
                computers[com1] = mutableSetOf()
            }
            if(computers[com2] == null) {
                computers[com2] = mutableSetOf()
            }
            computers[com1]!!.add(com2)
            computers[com2]!!.add(com1)
        }
        return computers
    }

    fun part1(input: List<String>): Int {
        val computers = parse(input)

        val parties = mutableSetOf<Set<String>>()
        for(com1 in computers) {
            for(com2Key in com1.value) {
                val com2Value = computers[com2Key]!!
                val com3Keys = com1.value.intersect(com2Value)
                for(com3Key in com3Keys) {
                    parties.add(setOf(com1.key, com2Key, com3Key))
                }
            }
        }

        return parties.count { party ->
            party.any { com -> com[0] == 't' }
        }
    }

    // Warning: This naive approach takes 31 minutes on the real input
    fun part2(input: List<String>): String {
        val computers = parse(input)

        var parties = mutableSetOf<Set<String>>()
        for(com1 in computers) {
            for(com2Key in com1.value) {
                val com2Value = computers[com2Key]!!
                val com3Keys = com1.value.intersect(com2Value)
                for(com3Key in com3Keys) {
                    parties.add(setOf(com1.key, com2Key, com3Key))
                }
            }
        }

        println("One: ${parties.count()}")

        while(parties.size != 1) {
            val parties2 = mutableSetOf<Set<String>>()
            val partiesList = parties.toList()

            for (i in partiesList.indices) {
                val party1 = partiesList[i]
                for (j in (i + 1)..<partiesList.size) {
                    val party2 = partiesList[j]
                    val oddOut1 = (party1 - party2)
                    val oddOut2 = (party2 - party1)
                    if (oddOut1.size == 1
                            && oddOut2.size == 1
                            && computers[oddOut1.first()]!!.containsAll(party2)
                            && computers[oddOut2.first()]!!.containsAll(party1)
                    ) {
                        parties2.add(party1.union(party2))
                    }
                }
            }
            parties = parties2
            println("Next: ${parties.count()}")
        }

        checkEquals(1, parties.count())

        return parties.first().sorted().joinToString(",")
    }

    val testInput = readInput("Day23_test")
    checkEquals(7, part1(testInput))
    checkEquals("co,de,ka,ta", part2(testInput))

    val input = readInput("Day23")
    println(timeIt{part1(input)})
    println(timeIt{part2(input)})
}
