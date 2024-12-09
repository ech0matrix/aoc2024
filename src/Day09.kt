fun main() {
    fun part1(input: List<String>): Long {
        val hardDrive = mutableListOf<Int?>()
        input[0].map { it.digitToInt() }.plus(0).chunked(2).forEachIndexed { i, diskMap ->
            repeat(diskMap[0]) {
                hardDrive.add(i)
            }
            repeat(diskMap[1]) {
                hardDrive.add(null)
            }
        }

        var front = 0
        var back = hardDrive.size-1
        while(front < back) {
            if (hardDrive[front] == null) {
                hardDrive[front] = hardDrive[back]
                hardDrive[back] = null
                front += 1
                back -= 1
                while(front < back && hardDrive[back] == null) {
                    back -= 1
                }
            } else {
                front += 1
            }
        }

        return hardDrive.mapIndexed { index, id ->
            index * (id ?: 0)
        }.sumOf { it.toLong() }
    }

    data class File(
        val size: Int,
        val id: Int?
    )

    fun part2(input: List<String>): Long {
        val hardDrive = mutableListOf<File>()
        input[0].map{ it.digitToInt() }.plus(0).chunked(2).forEachIndexed{ i, diskMap ->
            if(diskMap[0] > 0) {
                hardDrive.add(File(diskMap[0], i))
            }
            if(diskMap[1] > 0) {
                hardDrive.add(File(diskMap[1], null))
            }
        }

        var back = hardDrive.size-1
        while(back > 0) {
            if (hardDrive[back].id == null) {
                back -= 1
            } else {
                // Find first available space
                var front = 0
                while (front < back) {
                    if (hardDrive[front].id == null && hardDrive[front].size >= hardDrive[back].size) {
                        break
                    }
                    front += 1
                }
                if (front < back) {
                    // Found
                    if(hardDrive[front].size == hardDrive[back].size) {
                        // Even swap
                        val file = hardDrive[back]
                        hardDrive[back] = hardDrive[front]
                        hardDrive[front] = file
                        back -= 1
                    } else {
                        // Split empty space
                        val file = hardDrive[back]
                        val space = hardDrive[front].size
                        hardDrive[back] = File(file.size, null)
                        hardDrive[front] = file
                        val diff = space - file.size
                        check(diff > 0)
                        hardDrive.add(front+1, File(diff, null))
                    }
                } else {
                    back -= 1
                }
            }
        }

//        println()
//        hardDrive.forEach { file ->
//            val id = file.id?.toString() ?: "."
//            repeat(file.size) {
//                print(id)
//            }
//        }
//        println()

        return hardDrive.flatMap{ (size, id) ->
            List(size) { _ -> id}
        }.mapIndexed { index, id ->
            index * (id ?: 0)
        }.sumOf { it.toLong() }
    }

    val testInput = readInput("Day09_test")
    checkEquals(1928L, part1(testInput))
    checkEquals(2858L, part2(testInput))

    val input = readInput("Day09")
    println(timeIt{part1(input)})
    println(timeIt{part2(input)})
}
