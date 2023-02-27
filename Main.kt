package parking

import kotlin.system.exitProcess

open class Car(open var color: String, open var registration: String) {
    fun park() {
        ParkingLot.parking.forEach { space ->
            if (!space.occupied) {
                Messages.Parked.show(this.color.replaceFirstChar { it.uppercase() }, space.number.toString())
                space.occupied = true
                space.color = this.color.replaceFirstChar { it.uppercase() }
                space.registration = this.registration
                return
            }
        }
        Messages.Full.show()
    }

    companion object {
        fun leave(space: ParkingSpace) {
            if (space.occupied) {
                Messages.Unparked.show(space.number.toString())
                space.occupied = false
                space.color = Const.EMPTY
                space.registration = Const.EMPTY
            } else {
                Messages.EmptySpace.show(space.number.toString())
            }
        }
    }
}

data class ParkingSpace(val number: Int = 0, var occupied: Boolean) : Car(Const.EMPTY, Const.EMPTY)

class ParkingLot {
    init {
        prompt@ while (true) {
            val input = readln()
            when {
                // NOT CREATED
                input.contains(
                    Regex("${Const.PARK}|${Const.LEAVE}|${Const.STATUS}|${Const.REG_BY}|${Const.SPOT_BY}")
                ) && parking.isEmpty() -> Messages.NotCreated.show()
                input.contains(Const.CREATE) -> {
                    parking.clear()
                    input.split(Const.BLANK).last().let {
                        it.toInt().downTo(1).forEachIndexed { i, _ ->  parking.add(ParkingSpace(i + 1, false)) }
                        Messages.Created.show(it)
                    }
                }
                // PARK
                Regex("(park)\\s(-?\\w+)+\\s\\w+").matches(input) -> {
                    Car(input.split(Const.BLANK).last().lowercase(), input.split(Const.BLANK)[1]).park()
                }
                input.contains(Const.LEAVE) -> {
                    Car.leave(parking.elementAt(input.split(Const.BLANK).last().toInt() - 1))
                }
                input.contains(Const.STATUS) -> {
                    loop@ for (space in parking) {
                        if (space.occupied) {
                            parking.forEachIndexed { i, it ->
                                if (it.occupied) {
                                    Messages.Status.show(it.number.toString(), it.registration, it.color)
                                }
                            }
                            break@loop
                        } else {
                            Messages.ParkingEmpty.show()
                            break@loop
                        }
                    }
                }
                input.contains(Const.REG_BY) -> {
                    val color = input.split(Const.BLANK).last()
                    val registrations = mutableListOf<String>()
                    for (space in parking) {
                        if (space.color.lowercase() == color.lowercase()) {
                            registrations.add(space.registration)
                        }
                    }
                    if (registrations.isEmpty()) Messages.NoColorFound.show(color.uppercase())
                    else println(registrations.joinToString(Const.COMMA + Const.BLANK))
                }
                input.contains(Const.SPOT_BY) -> {
                    when {
                        input.contains(Const.REG) -> {
                            val registration = input.split(Const.BLANK).last()
                            for (space in parking) {
                                if (space.registration.lowercase() == registration.lowercase()) {
                                    Messages.Generic.show(space.number.toString())
                                    continue@prompt
                                }
                            }
                            Messages.NoRegFound.show(registration)
                        }
                        input.contains(Const.COLOR) -> {
                            val color = input.split(Const.BLANK).last()
                            val result = mutableListOf<Int>()
                            for (space in parking) {
                                if (space.color.lowercase() == color.lowercase()) {
                                    result.add(space.number)
                                }
                            }
                            if (result.isEmpty()) Messages.NoColorFound.show(color.uppercase())
                            else println(result.joinToString(Const.COMMA + Const.BLANK))
                        }
                    }
                }
                input.contains(Const.EXIT) -> exitProcess(0)
            }
        }
    }

    companion object {
        val parking = mutableSetOf<ParkingSpace>()
    }
}

fun main() {
    ParkingLot()
}