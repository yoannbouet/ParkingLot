package parking

import java.util.*

enum class Messages(val msg: String) {
    Parked("%s car parked in spot %s."),
    Unparked("Spot %s is free."),
    EmptySpace("There is no car in spot %s."),
    Full("Sorry, the parking lot is full."),
    Created("Created a parking lot with %s spots."),
    NotCreated("Sorry, a parking lot has not been created."),
    ParkingEmpty("Parking lot is empty."),
    Status("%s %s %s"),
    NoColorFound("No cars with color %s were found."),
    NoRegFound("No cars with registration number %s were found."),
    Generic("%s"),
}

fun Messages.show(str1: String = Const.EMPTY, str2: String = Const.EMPTY, str3: String = Const.EMPTY) {
    println(this.msg.format(str1.replaceFirstChar { ch ->
        if (ch.isLowerCase()) ch.titlecase(Locale.getDefault()) else ch.toString()}, str2, str3))
}