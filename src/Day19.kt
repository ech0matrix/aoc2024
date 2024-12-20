import java.util.PriorityQueue

fun main() {
    fun part1(input: List<String>): Int {
        val towels = input[0].split(", ")
        val patterns = input.subList(2, input.size)

        return patterns.count { pattern ->
            val visited = mutableSetOf<String>()
            val active = PriorityQueue<String>(compareBy { it.length })
            active.offer(pattern)
            while(active.isNotEmpty()) {
                val current = active.poll()!!

                if (current.isEmpty()) {
                    return@count true
                } else {
                    towels.filter { towel ->
                        towel.length <= current.length
                            && current.startsWith(towel)
                    }.map { towel ->
                        current.substring(towel.length)
                    }.filter { option ->
                        !visited.contains(option)
                    }.forEach { option ->
                        visited.add(option)
                        active.offer(option)
                    }
                }
            }

            return@count false
        }
    }

    fun part2(input: List<String>): Long {
        val towels = input[0].split(", ")
        val patterns = input.subList(2, input.size)

        return patterns.sumOf { pattern ->
            val visited = mutableMapOf<String, Long>()
            val active = PriorityQueue<String>(compareBy { 0 - it.length })
            active.offer(pattern)
            while(active.isNotEmpty()) {
                val current = active.poll()!!

                if (current.isNotEmpty()) {
                    val towelOptions = towels.filter { towel ->
                        towel.length <= current.length
                                && current.startsWith(towel)
                    }
                    val remainingStacks = towelOptions.map { towel ->
                        current.substring(towel.length)
                    }
                    remainingStacks.forEach { option ->
                        if(visited.contains(option)) {
                            visited[option] = visited[option]!! + visited[current]!!
                        } else {
                            visited[option] = visited[current] ?: 1L
                            active.offer(option)
                        }
                    }
                }
            }

            return@sumOf visited[""] ?: 0L
        }
    }

    val testInput = readInput("Day19_test")
    checkEquals(6, part1(testInput))
    checkEquals(16L, part2(testInput))

    val input = readInput("Day19")
    println(part1(input))
    println(part2(input))
}
