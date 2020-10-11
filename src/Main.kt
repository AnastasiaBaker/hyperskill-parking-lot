package parking

object Patterns {
    val exit = Regex("exit")
    val create = Regex("""create\s(\d+)""")
    val status = Regex("status")
    val park = Regex("""park\s([a-zA-Z_\-0-9]+)\s(\w+)""")
    val leave = Regex("""leave\s(\d+)""")
    val regByColor = Regex("""reg_by_color\s(\w+)""")
    val spotByColor = Regex("""spot_by_color\s(\w+)""")
    val spotByReg = Regex("""spot_by_reg\s([a-zA-Z_\-0-9]+)""")
}

class Spot {
    var empty = true
    var vin = ""
    var color = ""
}

class Parking(size: Int) {
    val spots = Array(size) { Spot() }

    fun park(input: String) {
        for (i in spots.indices) {
            if (spots[i].empty) {
                val match = Patterns.park.find(input)
                val (vin, color) = match!!.destructured

                spots[i].empty = false
                spots[i].vin = vin
                spots[i].color = color

                return println("$color car parked in spot ${i + 1}.")
            } else if (i == spots.lastIndex) println("Sorry, the parking lot is full.")
        }
    }

    fun leave(input: String) {
        val match = Patterns.leave.find(input)
        val (spotNum) = match!!.destructured

        if (spots[spotNum.toInt() - 1].empty) println("There is no car in spot $spotNum.")
        else {
            spots[spotNum.toInt() - 1].empty = true
            spots[spotNum.toInt() - 1].vin = ""
            spots[spotNum.toInt() - 1].color = ""
            println("Spot $spotNum is free.")
        }
    }

    fun status() {
        var empty = true

        for (i in spots.indices) {
            if (!spots[i].empty) {
                println("${i + 1} ${spots[i].vin} ${spots[i].color}")
                empty = false
            }
        }

        if (empty) println("Parking lot is empty.")
    }

    fun regByColor(input: String) {
        val match = Patterns.regByColor.find(input)
        val (color) = match!!.destructured
        val result = mutableListOf<String>()

        for (i in spots.indices) {
            if (spots[i].color.toLowerCase() == color.toLowerCase()) result.add(spots[i].vin)
        }

        if (result.isEmpty()) println("No cars with color $color were found.") else println(result.joinToString(", "))
    }

    fun spotByColor(input: String) {
        val match = Patterns.spotByColor.find(input)
        val (color) = match!!.destructured
        val result = mutableListOf<Int>()

        for (i in spots.indices) {
            if (spots[i].color.toLowerCase() == color.toLowerCase()) result.add(i + 1)
        }

        if (result.isEmpty()) println("No cars with color $color were found.") else println(result.joinToString(", "))
    }

    fun spotByReg(input: String) {
        val match = Patterns.spotByReg.find(input)
        val (vin) = match!!.destructured

        for (i in spots.indices) {
            if (spots[i].vin == vin) return println(i + 1)
        }

        println("No cars with registration number $vin were found.")
    }
}

fun main() {
    var parking = Parking(0)

    while (true) {
        val input = readLine()!!

        when {
            Patterns.exit.matches(input) -> break
            Patterns.create.matches(input) -> {
                val match = Patterns.create.find(input)
                val (size) = match!!.destructured
                parking = Parking(size.toInt())
                println("Created a parking lot with $size spots.")
            }
            parking.spots.isEmpty() -> println("Sorry, a parking lot has not been created.")
            Patterns.status.matches(input) -> parking.status()
            Patterns.park.matches(input) -> parking.park(input)
            Patterns.leave.matches(input) -> parking.leave(input)
            Patterns.regByColor.matches(input) -> parking.regByColor(input)
            Patterns.spotByColor.matches(input) -> parking.spotByColor(input)
            Patterns.spotByReg.matches(input) -> parking.spotByReg(input)
        }
    }
}
