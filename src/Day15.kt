fun main() {
    fun canMove(location: Coordinates, direction: Direction, walls: Set<Coordinates>, boxes: MutableSet<Coordinates>): Boolean {
        val next = location.add(direction)
        if (walls.contains(next)) {
            return false
        }
        if (!boxes.contains(next)) {
            return true
        }

        // Box in the way. Attempt to move it.
        val canMoveBox = canMove(next, direction, walls, boxes)
        if (!canMoveBox) {
            return false
        }

        val boxNext = next.add(direction)
        boxes.remove(next)
        boxes.add(boxNext)
        return true
    }

    fun print(walls: Set<Coordinates>, boxes: Set<Coordinates>, robot: Coordinates) {
        for(row in 0..walls.maxOf(Coordinates::row)) {
            for(col in 0..walls.maxOf(Coordinates::col)) {
                val loc = Coordinates(row, col)
                if (walls.contains(loc)) {
                    print('#')
                } else if (boxes.contains(loc)) {
                    print('O')
                } else if (robot == loc) {
                    print('@')
                } else {
                    print('.')
                }
            }
            println()
        }
        println()
    }

    fun part1(input: List<String>): Int {
        val indexToSplitList = input.indexOf("")
        val rawGrid = input.subList(0, indexToSplitList)
        val rawMoves = input.subList(indexToSplitList+1, input.size).joinToString("")

        val walls = mutableSetOf<Coordinates>()
        val boxes = mutableSetOf<Coordinates>()
        var robot = Coordinates(0, 0)
        for(row in rawGrid.indices) {
            for(col in rawGrid[row].indices) {
                when(rawGrid[row][col]) {
                    '#' -> walls.add(Coordinates(row, col))
                    'O' -> boxes.add(Coordinates(row, col))
                    '@' -> robot = Coordinates(row, col)
                }
            }
        }
        check(robot.row != 0 && robot.col != 0) // Make sure robot position is initialized (not the default value)

        //print(walls, boxes, robot)

        val moves = rawMoves.map { c ->
            when(c) {
                '<' -> Direction.WEST
                '^' -> Direction.NORTH
                '>' -> Direction.EAST
                'v' -> Direction.SOUTH
                else -> throw IllegalArgumentException()
            }
        }

        moves.forEach { move ->
            val validMove = canMove(robot, move, walls, boxes)
            if (validMove) {
                robot = robot.add(move)
            }
        }

        //print(walls, boxes, robot)

        return boxes.sumOf { (row, col) ->
            row*100 + col
        }
    }

    fun canMoveBox(box: Coordinates, move: Direction, walls: Set<Coordinates>, boxes: Set<Coordinates>, boxPairs: Map<Coordinates, Coordinates>): Boolean {
        if (move == Direction.WEST || move == Direction.EAST) {
            val next = box.add(move).add(move)
            return if (walls.contains(next)) {
                false
            } else if (!boxes.contains(next)) {
                true
            } else {
                canMoveBox(next, move, walls, boxes, boxPairs)
            }
        } else {
            val otherHalf = boxPairs[box]!!
            val next = box.add(move)
            val next2 = otherHalf.add(move)
            return if (walls.contains(next) || walls.contains(next2)) {
                false
            } else {
                (!boxes.contains(next) || canMoveBox(next, move, walls, boxes, boxPairs))
                    && (!boxes.contains(next2) || canMoveBox(next2, move, walls, boxes, boxPairs))
            }
        }
    }

    fun moveBox(box: Coordinates, move: Direction, boxes: MutableSet<Coordinates>, boxPairs: MutableMap<Coordinates, Coordinates>) {
        if (move == Direction.WEST || move == Direction.EAST) {
            val neighboringBox = box.add(move).add(move)
            if (boxes.contains(neighboringBox)) {
                moveBox(neighboringBox, move, boxes, boxPairs)
            }

            val otherHalf = boxPairs[box]!!

            boxes.remove(box)
            boxes.remove(otherHalf)
            boxPairs.remove(box)
            boxPairs.remove(otherHalf)

            val movedBox = box.add(move)
            val movedOtherHalf = otherHalf.add(move)

            boxes.add(movedBox)
            boxes.add(movedOtherHalf)
            boxPairs[movedBox] = movedOtherHalf
            boxPairs[movedOtherHalf] = movedBox

            check(boxes.count() % 2 == 0)
            check(boxPairs.count() % 2 == 0)
        } else {
            val otherHalf = boxPairs[box]!!

            val movedBox = box.add(move)
            val movedOtherHalf = otherHalf.add(move)

            if (boxes.contains(movedBox)) {
                moveBox(movedBox, move, boxes, boxPairs)
            }
            // Check in separate if-blocks, in case both locations became free from moving the 1 box
            if (boxes.contains(movedOtherHalf)) {
                moveBox(movedOtherHalf, move, boxes, boxPairs)
            }

            boxes.remove(box)
            boxes.remove(otherHalf)
            boxPairs.remove(box)
            boxPairs.remove(otherHalf)

            boxes.add(movedBox)
            boxes.add(movedOtherHalf)
            boxPairs[movedBox] = movedOtherHalf
            boxPairs[movedOtherHalf] = movedBox

            check(boxes.count() % 2 == 0)
            check(boxPairs.count() % 2 == 0)
        }
    }

    fun part2(input: List<String>): Int {
        val indexToSplitList = input.indexOf("")
        val rawGrid = input.subList(0, indexToSplitList)
        val rawMoves = input.subList(indexToSplitList+1, input.size).joinToString("")

        val walls = mutableSetOf<Coordinates>()
        val boxes = mutableSetOf<Coordinates>()
        val boxPairs = mutableMapOf<Coordinates, Coordinates>()
        var robot = Coordinates(0, 0)
        for(row in rawGrid.indices) {
            for(col in rawGrid[row].indices) {
                when(rawGrid[row][col]) {
                    '#' -> {
                        walls.add(Coordinates(row, col*2))
                        walls.add(Coordinates(row, col*2 + 1))
                    }
                    'O' -> {
                        val b1 = Coordinates(row, col*2)
                        val b2 = Coordinates(row, col*2 + 1)
                        boxes.add(b1)
                        boxes.add(b2)
                        boxPairs[b1] = b2
                        boxPairs[b2] = b1
                    }
                    '@' -> robot = Coordinates(row, col*2)
                }
            }
        }
        check(robot.row != 0 && robot.col != 0) // Make sure robot position is initialized (not the default value)

        //print(walls, boxes, robot)

        val moves = rawMoves.map { c ->
            when(c) {
                '<' -> Direction.WEST
                '^' -> Direction.NORTH
                '>' -> Direction.EAST
                'v' -> Direction.SOUTH
                else -> throw IllegalArgumentException()
            }
        }

        moves.forEach { move ->
            val next = robot.add(move)
            if (walls.contains(next)) {
                // Can't move
            } else if (!boxes.contains(next)) {
                // Can move
                robot = next
            } else {
                // Check if box can be pushed
                val canMove = canMoveBox(next, move, walls, boxes, boxPairs)
                if (canMove) {
                    moveBox(next, move, boxes, boxPairs)
                    robot = next
                }
            }
        }

        return boxes.sumOf { box ->
            val otherHalf = boxPairs[box]!!
            if(box.col < otherHalf.col) {
                box.row * 100 + box.col
            } else {
                0
            }
        }
    }

    val testInput = readInput("Day15_test")
    val testInput2 = readInput("Day15_test2")
    checkEquals(2028, part1(testInput))
    checkEquals(10092, part1(testInput2))
    checkEquals(9021, part2(testInput2))

    val input = readInput("Day15")
    println(part1(input))
    println(part2(input))
}
