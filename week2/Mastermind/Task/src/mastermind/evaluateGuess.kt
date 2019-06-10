package mastermind

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {

    var rightPosition = 0
    var wrongPosition = 0

    val map = mutableMapOf<Char,Int>()

    for (c in secret) {
        if(map.containsKey(c))
            map[c] = map[c]!!.plus(1)
        else
            map[c] = 1
    }

    for (i in 0 until secret.length) {
        if (secret[i] == guess[i]) {
            rightPosition++
            map[guess[i]] = map[guess[i]]!!.minus(1);
        }
    }

    for (i in 0 until secret.length) {
        if (secret[i] != guess[i]) {
            if (secret.contains(guess[i]) && map[guess[i]]!! > 0) {
                wrongPosition++
                map[guess[i]] = map[guess[i]]!!.minus(1);
            }
        }
    }

    return Evaluation(rightPosition, wrongPosition)
}
