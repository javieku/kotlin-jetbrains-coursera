package rationals

import java.lang.IllegalArgumentException
import java.math.BigInteger

class Rational : Comparable<Rational>{

    val num: BigInteger
    val den: BigInteger

    constructor(num: BigInteger, den: BigInteger) {
        if (den == 0.toBigInteger()) {
            throw IllegalArgumentException("Denominator cannot be 0")
        }

        var tmpNum = num
        var tmpDen = den
        if (num < 0.toBigInteger() && den < 0.toBigInteger() ||(num > 0.toBigInteger() && den < 0.toBigInteger())) {
            tmpNum = tmpNum.times(-1.toBigInteger())
            tmpDen = tmpDen.times(-1.toBigInteger())
        }

        // reduce the value at construct time....
        val div = if (tmpNum == 0.toBigInteger()) 1.toBigInteger() else tmpNum.gcd(tmpDen)
        // div is guaranteed to be a divisor, so integer division is safe
        this.num = tmpNum / div
        this.den = if (tmpNum == 0.toBigInteger()) 1.toBigInteger() else tmpDen / div
    }

    constructor(num: Int, den: Int) : this(num.toBigInteger(), den.toBigInteger())
    constructor(num: Long, den: Long) : this(num.toBigInteger(), den.toBigInteger())

    operator fun plus(x: Rational) = Rational( x.num * den + num * x.den, den * x.den )
    operator fun minus(x: Rational) = Rational(  num * x.den - x.num * den, x.den * this.den )
    operator fun times(x: Rational) = Rational( x.num * this.num, x.den * this.den )
    operator fun div(x: Rational) : Rational {
        return Rational( this.num * x.den, x.num * this.den )
    }

    operator fun unaryMinus() : Rational {
        return Rational( num.times(-1.toBigInteger()), den )
    }

    override operator fun compareTo(x: Rational) : Int {
        val difference = minus(x)
        if (difference.num > 0.toBigInteger()) {
            return 1
        }
        return if (difference.num < 0.toBigInteger()) {
            -1
        } else 0
    }

    operator fun rangeTo(end: Rational): ClosedRange<Rational> {
        return object : ClosedRange<Rational> {
            override val endInclusive: Rational = end
            override val start: Rational = this@Rational
        }
    }

    operator fun contains(x: Rational) : Boolean {
        return x > this
    }

    override fun equals(other: Any?) :Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Rational

        return other.num == num && other.den == den
    }

    override fun toString() :String {
        if (den == 1.toBigInteger())
            return num.toString()
        else
            return num.toString() + "/" + den.toString()
    }
}

infix fun Int.divBy(x: Int ): Rational = Rational(this, x)
infix fun Long.divBy(x: Long ): Rational = Rational(this, x)
infix fun BigInteger.divBy(x: BigInteger ): Rational = Rational(this, x)

fun String.toRational () : Rational {
    val l = this.split("/")
    if (l.isEmpty()) {
        throw IllegalArgumentException()
    }
    val num = l[0].toBigInteger()

    if (l.size == 1)
        return Rational(num,1.toBigInteger())
    val den = l[1].toBigInteger()
    return Rational(num,den)
}

fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}