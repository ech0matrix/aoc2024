fun main() {
    data class TrackStep(
        val position: Coordinates,
        val distanceToEnd: Int,
        val nextStep: TrackStep?
    )

    data class RaceTrack(
        val track: Set<Coordinates>,
        val start: Coordinates,
        val end: Coordinates
    )

    fun parse(input: List<String>): RaceTrack {
        val track = mutableSetOf<Coordinates>()
        var start: Coordinates? = null
        var end: Coordinates? = null
        for(row in input.indices) {
            for(col in input[row].indices) {
                if(input[row][col] != '#') {
                    track.add(Coordinates(row, col))
                }

                if(input[row][col] == 'S') {
                    start = Coordinates(row, col)
                } else if(input[row][col] == 'E') {
                    end = Coordinates(row, col)
                }
            }
        }
        return RaceTrack(
            track,
            start!!,
            end!!
        )
    }

    fun buildTrackWithDistances(track: Set<Coordinates>, start: Coordinates, end: Coordinates): Pair<TrackStep, Map<Coordinates, Int>> {
        // Solve race without cheating, but in reverse to keep count of steps from any point to the end
        val trackSteps = mutableMapOf<Coordinates, Int>()
        var current = end
        var steps = 0
        var trackPath = TrackStep(end, 0, null)
        while(current != start) {
            trackSteps[current] = steps
            val next = listOf(
                    current.add(Direction.NORTH),
                    current.add(Direction.SOUTH),
                    current.add(Direction.WEST),
                    current.add(Direction.EAST)
            ).filter { track.contains(it) && !trackSteps.containsKey(it) }
            check(next.count() == 1)

            steps += 1
            current = next[0]
            trackPath = TrackStep(current, steps, trackPath)
        }
        trackSteps[start] = steps
        return Pair(trackPath, trackSteps)
    }

    fun part1(input: List<String>, saveAtLeast: Int): Int {
        val (track, start, end) = parse(input)

        val (trackPath, trackSteps) = buildTrackWithDistances(track, start, end)

        // Now walk the track forwards, checking for cheats along the way
        val cheatCounts = mutableMapOf<Int, Int>() // Steps saved, to Counts of cheats
        var currentStep = trackPath
        while(currentStep.position != end) {
            listOf(
                Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST
            ).filter { possibleDirection ->
                val collision = currentStep.position.add(possibleDirection)
                // Cheat must go through wall
                if(track.contains(collision)){
                    return@filter false
                }
                // Cheat must get back on track
                val backOnTrack = collision.add(possibleDirection)
                if(!track.contains(backOnTrack)) {
                    return@filter false
                }
                // Cheat should be a short-cut
                (trackSteps[backOnTrack]!! + 2) < currentStep.distanceToEnd
            }.forEach { cheatDirection ->
                val cheatPosition = currentStep.position.add(cheatDirection).add(cheatDirection)
                val stepsSaved = currentStep.distanceToEnd - (trackSteps[cheatPosition]!! + 2)
                cheatCounts[stepsSaved] = (cheatCounts[stepsSaved] ?: 0) + 1
            }
            currentStep = currentStep.nextStep!!
        }

//        cheatCounts.toList().sortedBy { it.first }.forEach { (stepsSaved, count) ->
//            println("There are $count cheats that save $stepsSaved picoseconds.")
//        }

        return cheatCounts.filter{ (stepsSaved, _) ->
            stepsSaved >= saveAtLeast
        }.values.sum()
    }

    fun part2(input: List<String>, saveAtLeast: Int): Int {
        val (track, start, end) = parse(input)
        val (trackPath, trackSteps) = buildTrackWithDistances(track, start, end)

        var testStart = trackPath
        var numCheats = 0
        while(testStart.nextStep != null) {
            numCheats += track.filter { testEnd ->
                if(testStart.position == testEnd) {
                    return@filter false
                }
                val distance = testStart.position.manhattanDistance(testEnd)
                if (distance < 2 || distance > 20) {
                    return@filter false
                }
                val stepsSaved = testStart.distanceToEnd - (trackSteps[testEnd]!! + distance)

                stepsSaved >= saveAtLeast
            }.count()
            testStart = testStart.nextStep!!
        }

        return numCheats
    }

    val testInput = readInput("Day20_test")
    checkEquals(44, part1(testInput, 1))
    checkEquals(285, part2(testInput, 50))

    val input = readInput("Day20")
    println(timeIt{part1(input, 100)})
    println(timeIt{part2(input, 100)})
}
