fun main() {
    fun fillRegion(location: Coordinates, plant: Char, plots: MutableMap<Coordinates, Char>, region: MutableSet<Coordinates>) {
        if(plots[location] == plant) {
            plots.remove(location)
            region.add(location)

            fillRegion(location.add(Direction.NORTH), plant, plots, region)
            fillRegion(location.add(Direction.SOUTH), plant, plots, region)
            fillRegion(location.add(Direction.WEST), plant, plots, region)
            fillRegion(location.add(Direction.EAST), plant, plots, region)
        }
    }

    fun parseRegions(input: List<String>): List<Set<Coordinates>> {
        val plots = input.flatMapIndexed{ row, line ->
            line.mapIndexed{ col, c ->
                Pair(Coordinates(row, col), c)
            }
        }.toMap().toMutableMap()

        val regions = mutableListOf<Set<Coordinates>>()
        while(plots.isNotEmpty()) {
            val startLocation = plots.keys.first()
            val plant = plots[startLocation]!!
            val newRegion = mutableSetOf<Coordinates>()
            fillRegion(startLocation, plant, plots, newRegion)
            regions.add(newRegion)
        }

        return regions
    }

    fun part1(input: List<String>): Int {
        val regions = parseRegions(input)

        val areas = regions.map { it.count() }
        val perimeters = regions.map { region ->
            region.sumOf { location ->
                listOf(
                    location.add(Direction.NORTH),
                    location.add(Direction.SOUTH),
                    location.add(Direction.WEST),
                    location.add(Direction.EAST)
                ).count { !region.contains(it) }
            }
        }

        val prices = areas.zip(perimeters).map{ (area, perimeter) ->
            area * perimeter
        }

        return prices.sum()
    }

    data class Edge(
        val location: Coordinates,
        val face: Direction
    )

    fun removeEdge(edgePlot: Edge, borderPlots: MutableSet<Edge>) {
        if(borderPlots.contains(edgePlot)) {
            borderPlots.remove(edgePlot)

            if (edgePlot.face == Direction.WEST || edgePlot.face == Direction.EAST) {
                removeEdge(edgePlot.copy(location = edgePlot.location.add(Direction.NORTH)), borderPlots)
                removeEdge(edgePlot.copy(location = edgePlot.location.add(Direction.SOUTH)), borderPlots)
            } else {
                removeEdge(edgePlot.copy(location = edgePlot.location.add(Direction.WEST)), borderPlots)
                removeEdge(edgePlot.copy(location = edgePlot.location.add(Direction.EAST)), borderPlots)
            }
        }
    }

    fun part2(input: List<String>): Int {
        val regions = parseRegions(input)
        val areas = regions.map { it.count() }
        val edges = regions.map { region ->
            val borderPlots = mutableSetOf<Edge>()
            region.forEach{ plot ->
                val validBorderPlots = listOf(
                    Edge(plot.add(Direction.NORTH), Direction.NORTH),
                    Edge(plot.add(Direction.SOUTH), Direction.SOUTH),
                    Edge(plot.add(Direction.WEST), Direction.WEST),
                    Edge(plot.add(Direction.EAST), Direction.EAST),
                ).filter { !region.contains(it.location) }
                borderPlots.addAll(validBorderPlots)
            }

            var edgeCount = 0
            while(borderPlots.isNotEmpty()) {
                val startEdgePlot = borderPlots.first()
                removeEdge(startEdgePlot, borderPlots)
                edgeCount += 1
            }

            edgeCount
        }

        val prices = areas.zip(edges).map{ (area, edges) ->
            area * edges
        }

        return prices.sum()
    }

    val testInput = readInput("Day12_test")
    val testInput2 = readInput("Day12_test2")
    checkEquals(772, part1(testInput))
    checkEquals(1930, part1(testInput2))

    val testInput3 = readInput("Day12_test3")
    checkEquals(436, part2(testInput))
    checkEquals(1206, part2(testInput2))
    checkEquals(368, part2(testInput3))

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}
