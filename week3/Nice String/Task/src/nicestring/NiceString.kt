package nicestring

fun String.isNice(): Boolean {
    val hasVowels = count { letter -> letter.isVowel() } >= 3

    val hasDoubleLetter = zipWithNext()
                          .count { (first,second) -> first == second } >= 1

    val hasNotSubString = !(contains("bu") ||
                            contains("ba") ||
                            contains("be"))

    return (hasVowels && hasDoubleLetter) || (hasVowels && hasNotSubString) ||
            (hasDoubleLetter && hasNotSubString);
}

fun Char.isVowel(): Boolean {
    return this == 'a' || this == 'e' || this == 'i' || this == 'o' || this == 'u'
}