fun main() {
    fun isSafe(report: List<Int>): Boolean {
        val isAscending = report[1]-report[0] > 0
        val allowedDiffs = if(isAscending) setOf(1,2,3) else setOf(-1,-2,-3)
        return report.zipWithNext()
            .map { it.second - it.first }
            .all { allowedDiffs.contains(it) }
    }

    fun part1(input: List<String>): Int {
        val reports = input.map { report ->
            report.split(' ').map(String::toInt)
        }
        return reports.count { isSafe(it) }
    }

    fun part2(input: List<String>): Int {
        val reports = input.map { report ->
            report.split(' ').map(String::toInt)
        }
        return reports.count { report ->
            if (isSafe(report)) {
                true
            } else {
                // Try every combination of removing 1 level
                val dampenedReports = List(report.size) { i ->
                    report.filterIndexed { j, _ -> i != j }
                }
                dampenedReports.any { isSafe(it) }
            }
        }
    }

    val testInput = readInput("Day02_test")
    checkEquals(2, part1(testInput))
    checkEquals(4, part2(testInput))

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
