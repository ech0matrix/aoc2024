fun main() {
    // Parse text file, return Pair(rules, prints)
    fun parse(input: List<String>): Pair<Map<Int,Set<Int>>, List<List<Int>>> {
        val indexToSplitList = input.indexOf("")
        val rawRules = input.subList(0, indexToSplitList)
        val rawPrints = input.subList(indexToSplitList+1, input.size)

        val rules = mutableMapOf<Int, MutableSet<Int>>()
        for(rawRule in rawRules) {
            val rule = rawRule.split('|').map(String::toInt)
            checkEquals(2, rule.size)
            rules.putIfAbsent(rule[0], mutableSetOf())
            rules[rule[0]]!!.add(rule[1])
        }

        val prints = rawPrints.map { line ->
            line.split(',').map(String::toInt)
        }

        return Pair(
            rules.mapValues { (_, v) -> v.toSet() }.toMap(),
            prints
        )
    }

    // Partitions prints, returning Pair(validPrints, invalidPrints)
    fun splitPrints(rules: Map<Int, Set<Int>>, prints: List<List<Int>>): Pair<List<List<Int>>, List<List<Int>>> {
        return prints.partition { print ->
            for(i in print.size-1 downTo 1) {
                val page = print[i]
                val relevantRules = rules[page]
                if (relevantRules != null) {
                    val previousPages = print.subList(0, i).toSet()
                    val violations = previousPages.intersect(relevantRules)
                    if (violations.isNotEmpty()) {
                        return@partition false
                    }
                }
            }
            return@partition true
        }
    }

    fun sumMiddlePages(prints: List<List<Int>>): Int {
        return prints.sumOf { print ->
            print[print.size/2]
        }
    }

    fun part1(input: List<String>): Int {
        val (rules, prints) = parse(input)

        val (validPrints, _) = splitPrints(rules, prints)

        return sumMiddlePages(validPrints)
    }

    fun part2(input: List<String>): Int {
        val (rules, prints) = parse(input)

        val (_, invalidPrints) = splitPrints(rules, prints)

        val sorted = invalidPrints.map { print ->
            print.sortedWith { lhs, rhs ->
                val beforeRules = rules[lhs]
                if (beforeRules != null && beforeRules.contains(rhs)) {
                    return@sortedWith -1
                }

                val afterRules = rules[rhs]
                if (afterRules != null && afterRules.contains(lhs)) {
                    return@sortedWith 1
                }

                return@sortedWith 0
            }
        }

        return sumMiddlePages(sorted)
    }

    val testInput = readInput("Day05_test")
    checkEquals(143, part1(testInput))
    checkEquals(123, part2(testInput))

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
