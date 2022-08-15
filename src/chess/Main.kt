package chess

import kotlin.math.abs

const val GAME_NAME = " Pawns-Only Chess"
val REG_INPUT_STEP = Regex("[a-h][1-8][a-h][1-8]")
var lastIndexes = IntArray(4) { -1 }
val map = MutableList(8) { MutableList(8) { ' ' } } // A field with located pawns

fun main() {
    setStart() // Setting initial conditions
    println(GAME_NAME)
    println("First Player's name:")
    val firstPlayerName = readln()
    println("Second Player's name:")
    val secondPlayerName = readln()
    printGameField()
    var switchPlayer = true
    while (true) {
        if (switchPlayer) {
            if (!step(name = firstPlayerName, 'W')) {
                break
            }
        } else {
            if (!step(name = secondPlayerName, 'B')) {
                break
            }
        }
        switchPlayer = !switchPlayer
    }
    println("Bye!")
}

/**
 * Print the current playing field
 */
fun printGameField() {
    for (i in 7 downTo 0) {
        println("  " + "+---".repeat(8) + '+')
        print("${i + 1} ")
        for (j in 0..7) {
            print("| ${map[i][j]} ")
        }
        println('|')
    }
    println("  " + "+---".repeat(8) + '+')
    print(' ')
    for (i in 'a'..'h') {
        print("   $i")
    }
    println()
    println()
}

/**
 * Attempt to make a move by a player
 */
fun step(name: String, color: Char): Boolean {
    println("$name's turn:")
    var step = readln()
    if (step == "exit") {
        return false
    }
    val indexes = IntArray(4) { 0 }
    while (!REG_INPUT_STEP.matches(step)) {
        println("Invalid Input")
        println("$name's turn:")
        step = readln()
        if (step == "exit") {
            return false
        }
    }
    while (true) { // Loop for repeat input when invalid input was
        setIndexes(indexes, step)
        if (checkStep(color, indexes, step)) {
            break
        }
        println("$name's turn:")
        step = readln()
        if (step == "exit") {
            return false
        }
    }
    if (indexes[0] == indexes[2]) { // If a step forward
        val switch = map[indexes[1]][indexes[0]]
        map[indexes[1]][indexes[0]] = map[indexes[3]][indexes[2]]
        map[indexes[3]][indexes[2]] = switch
    } else if (indexes[2] == lastIndexes[2] && lastIndexes[3] - indexes[1] == 0) { // If it eats horizontally
        map[indexes[3]][indexes[2]] = map[indexes[1]][indexes[0]]
        map[indexes[1]][indexes[0]] = ' '
        map[lastIndexes[3]][lastIndexes[2]] = ' '
    } else { // If eaten diagonally
        map[indexes[3]][indexes[2]] = map[indexes[1]][indexes[0]]
        map[indexes[1]][indexes[0]] = ' '
    }
    printGameField()
    if (checkWinCondition(color)) {
        return false
    }
    lastIndexes = indexes
    return true
}

/**
 * Checking for the achievement of the winning condition or the impossibility of making a move
 */
fun checkWinCondition(color: Char): Boolean {
    var haveOne = false
    val indexes = IntArray(4) { 0 }
    var step: String
    if (color == 'W') {
        for (i in 0..7) { // Check for passage to the end
            if (map[7][i] == 'W') {
                println("White Wins!")
                return true
            }
        }
        loop@ for (i in 0..7) { // Checking for the presence of enemy pawns
            for (j in 0..7) {
                if (map[i][j] == 'B') {
                    haveOne = true
                    break@loop
                }
            }
        }
        if (!haveOne) {
            println("White Wins!")
            return true
        }
        for (i in 7 downTo 0) { // Checking for possible moves
            for (j in 7 downTo 0) {
                if (map[i][j] == 'B') {
                    step =
                        "${translateFromDigitToLetter(j)}${i + 1}${translateFromDigitToLetter(j)}${i}" // Checking the possibility of moving forward by one cell
                    setIndexes(indexes, step)
                    if (checkStepWithoutErrorMessage('B', indexes)) {
                        return false
                    }
                    step =
                        "${translateFromDigitToLetter(j)}${i + 1}${translateFromDigitToLetter(j)}${i - 1}" // Checking the possibility of moving forward by two cells
                    setIndexes(indexes, step)
                    if (checkStepWithoutErrorMessage('B', indexes)) {
                        return false
                    }
                    step =
                        "${translateFromDigitToLetter(j)}${i + 1}${translateFromDigitToLetter(j + 1)}${i}" // Checking the possibility of a move to eat diagonally to the left
                    setIndexes(indexes, step)
                    if (checkStepWithoutErrorMessage('B', indexes)) {
                        return false
                    }
                    step =
                        "${translateFromDigitToLetter(j)}${i + 1}${translateFromDigitToLetter(j - 1)}${i}" // Checking the possibility of a move to eat diagonally to the right
                    setIndexes(indexes, step)
                    if (checkStepWithoutErrorMessage('B', indexes)) {
                        return false
                    }
                }
            }
        }
    } else {
        for (i in 0..7) { // Check for passage to the end
            if (map[0][i] == 'B') {
                println("Black Wins!")
                return true
            }
        }
        loop@ for (i in 0..7) { // Checking for the presence of enemy pawns
            for (j in 0..7) {
                if (map[i][j] == 'W') {
                    haveOne = true
                    break@loop
                }
            }
        }
        if (!haveOne) {
            println("Black Wins!")
            return true
        }
        for (i in 0..7) { // Checking for possible moves
            for (j in 0..7) {
                if (map[i][j] == 'W') {
                    step =
                        "${translateFromDigitToLetter(j)}${i + 1}${translateFromDigitToLetter(j)}${i + 2}" // Checking the possibility of moving forward by one cell
                    setIndexes(indexes, step)
                    if (checkStepWithoutErrorMessage('W', indexes)) {
                        return false
                    }
                    step =
                        "${translateFromDigitToLetter(j)}${i + 1}${translateFromDigitToLetter(j)}${i + 3}" // Checking the possibility of moving forward by two cells
                    setIndexes(indexes, step)
                    if (checkStepWithoutErrorMessage('W', indexes)) {
                        return false
                    }
                    step =
                        "${translateFromDigitToLetter(j)}${i + 1}${translateFromDigitToLetter(j + 1)}${i + 2}" // Checking the possibility of a move to eat diagonally to the left
                    setIndexes(indexes, step)
                    if (checkStepWithoutErrorMessage('W', indexes)) {
                        return false
                    }
                    step =
                        "${translateFromDigitToLetter(j)}${i + 1}${translateFromDigitToLetter(j - 1)}${i + 2}" // Checking the possibility of a move to eat diagonally to the right
                    setIndexes(indexes, step)
                    if (checkStepWithoutErrorMessage('W', indexes)) {
                        return false
                    }
                }
            }
        }
    }
    println("Stalemate!")
    return true
}

/**
 * Checking the progress for validity
 */
fun checkStep(color: Char, indexes: IntArray, step: String): Boolean {
    if (map[indexes[1]][indexes[0]] != color && color == 'W') { // Checking for finding a pawn in the cells from where the move begins
        println("No white pawn at ${step[0]}${step[1]}")
        return false
    }
    if (map[indexes[1]][indexes[0]] != color && color == 'B') {
        println("No black pawn at ${step[0]}${step[1]}")
        return false
    }
    if (map[indexes[3]][indexes[2]] == color) { // Checking for the availability of the destination
        println("Invalid Input")
        return false
    }
    if (indexes[0] == indexes[2]) { // Forward running conditions
        if (map[indexes[3]][indexes[2]] == 'W' || map[indexes[3]][indexes[2]] == 'B') { // If we walk straight, there should be no obstacles
            println("Invalid Input")
            return false
        }
        return if (color == 'W' && indexes[1] == 1 && indexes[3] - indexes[1] in 1..2) { // The step can be only 1-2 cells ahead of the starting position
            true
        } else if (color == 'B' && indexes[1] == 6 && indexes[1] - indexes[3] in 1..2) { // The step can be only 1-2 cells ahead of the starting position
            true
        } else if (color == 'W' && indexes[3] - indexes[1] == 1) { // If the position is not the starting position, then the step can be only one cell ahead
            true
        } else if (color == 'B' && indexes[1] - indexes[3] == 1) { // If the position is not the starting position, then the step can be only one cell ahead
            true
        } else {
            println("Invalid Input") // If the input does not meet the upper conditions, exit
            false
        }
    } else { // Conditions of possible interception
        if (abs(indexes[0] - indexes[2]) == 1) {
            if (color == 'W' && map[indexes[3]][indexes[2]] == 'B' && indexes[3] - indexes[1] == 1) { // You can eat only if there is an enemy pawn in the destination
                return true
            }
            if (color == 'B' && map[indexes[3]][indexes[2]] == 'W' && indexes[1] - indexes[3] == 1) {
                return true
            }
            if (indexes[2] == lastIndexes[2] && lastIndexes[3] - indexes[1] == 0) { // Or if the opponent was on the next square horizontally with the last move
                return true
            }
        }
    }
    println("Invalid Input") // If the conditions are not met, we output an error message
    return false
}

/**
 * Sets the initial position of the pawns on the field.
 */
fun setStart() {
    for (i in 0..7) {
        for (j in 0..7) {
            if (i == 1) {
                map[i][j] = 'W'
            }
            if (i == 6) {
                map[i][j] = 'B'
            }
        }
    }
}

/**
 * Translates from a step letter to a numerical position in the list
 */
fun translateFromLetterToDigit(letter: Char): Int {
    return when (letter) {
        'a' -> 0
        'b' -> 1
        'c' -> 2
        'd' -> 3
        'e' -> 4
        'f' -> 5
        'g' -> 6
        else -> 7
    }
}

/**
 * Translate from a numerical position in the list to a step
 */
fun translateFromDigitToLetter(digit: Int): Char {
    return when (digit) {
        0 -> 'a'
        1 -> 'b'
        2 -> 'c'
        3 -> 'd'
        4 -> 'e'
        5 -> 'f'
        6 -> 'g'
        else -> 'h'
    }
}

/**
 * Sets the parameters of the step from the text representation to the numerical one
 */
fun setIndexes(indexes: IntArray, step: String) {
    indexes[0] = translateFromLetterToDigit(step[0])
    indexes[1] = step[1].digitToInt() - 1
    indexes[2] = translateFromLetterToDigit(step[2])
    indexes[3] = step[3].digitToInt() - 1
}

/**
 * Same function as checkStep only without error messages
 */
fun checkStepWithoutErrorMessage(color: Char, indexes: IntArray): Boolean {
    if (map[indexes[1]][indexes[0]] != color && color == 'W') {
        return false
    }
    if (map[indexes[1]][indexes[0]] != color && color == 'B') {
        return false
    }
    if (map[indexes[3]][indexes[2]] == color) {
        return false
    }
    if (indexes[0] == indexes[2]) { // step
        if (map[indexes[3]][indexes[2]] == 'W' || map[indexes[3]][indexes[2]] == 'B') {
            return false
        }
        return if (color == 'W' && indexes[1] == 1 && indexes[3] - indexes[1] in 1..2) {
            true
        } else if (color == 'B' && indexes[1] == 6 && indexes[1] - indexes[3] in 1..2) {
            true
        } else if (color == 'W' && indexes[3] - indexes[1] == 1) {
            true
        } else color == 'B' && indexes[1] - indexes[3] == 1
    } else { // capture
        if (abs(indexes[0] - indexes[2]) == 1) {
            if (color == 'W' && map[indexes[3]][indexes[2]] == 'B' && indexes[3] - indexes[1] == 1) {
                return true
            }
            if (color == 'B' && map[indexes[3]][indexes[2]] == 'W' && indexes[1] - indexes[3] == 1) {
                return true
            }
            if (indexes[2] == lastIndexes[2] && lastIndexes[3] - indexes[1] == 0) {
                return true
            }
        }
    }
    return false
}