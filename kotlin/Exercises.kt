import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

fun change(amount: Long): Map<Int, Long> {
    require(amount >= 0) { "Amount cannot be negative" }

    val counts = mutableMapOf<Int, Long>()
    var remaining = amount
    for (denomination in listOf(25, 10, 5, 1)) {
        counts[denomination] = remaining / denomination
        remaining %= denomination
    }
    return counts
}

// First Then Lower Case function
fun firstThenLowerCase(array: List<String>, predicate: (String) -> Boolean): String? {
    return array.firstOrNull(predicate)?.lowercase()
}

// Say function
data class Say(val phrase: String) {
    fun and(nextPhrase: String): Say {
        return Say("$phrase $nextPhrase")
    }
}
fun say(phrase: String = ""): Say {
    return Say(phrase)
}

// Meaningful Line Count function
@Throws(IOException::class)
fun meaningfulLineCount(filePath: String): Long {
    return BufferedReader(FileReader(filePath)).use { reader ->
        reader.lineSequence().count { it.isNotBlank() }.toLong()
    }
}

// Quaternion data class
data class Quaternion(val a: Double, val b: Double, val c: Double, val d: Double) {
    companion object {
        val ZERO = Quaternion(0.0, 0.0, 0.0, 0.0)
        val I = Quaternion(0.0, 1.0, 0.0, 0.0) // Corrected I to match complex number convention
        val J = Quaternion(0.0, 0.0, 1.0, 0.0)
        val K = Quaternion(0.0, 0.0, 0.0, 1.0)
    }

    fun coefficients(): List<Double> = listOf(a, b, c, d)

    fun conjugate(): Quaternion = Quaternion(a, -b, -c, -d)

    operator fun plus(other: Quaternion): Quaternion {
        return Quaternion(a + other.a, b + other.b, c + other.c, d + other.d)
    }

    operator fun times(other: Quaternion): Quaternion {
        return Quaternion(
            a * other.a - b * other.b - c * other.c - d * other.d,
            a * other.b + b * other.a + c * other.d - d * other.c,
            a * other.c - b * other.d + c * other.a + d * other.b,
            a * other.d + b * other.c - c * other.b + d * other.a
        )
    }

    override fun toString(): String {
        val terms = mutableListOf<String>()
        if (a != 0.0) terms.add(a.toString())
        if (b != 0.0) terms.add("${if (b < 0) "" else "+"}${b}i")
        if (c != 0.0) terms.add("${if (c < 0) "" else "+"}${c}j")
        if (d != 0.0) terms.add("${if (d < 0) "" else "+"}${d}k")
        return if (terms.isEmpty()) "0" else terms.joinToString("")
    }
}


// Binary Search Tree interface and implementations
sealed interface BinarySearchTree {
    fun size(): Int
    fun contains(value: String): Boolean

    data class Node(val value: String, val left: BinarySearchTree = Empty, val right: BinarySearchTree = Empty) : BinarySearchTree {
        override fun size(): Int = 1 + left.size() + right.size()

        override fun contains(value: String): Boolean {
            return when {
                value < this.value -> left.contains(value)
                value > this.value -> right.contains(value)
                else -> true
            }
        }

        override fun toString(): String {
            val leftStr = if (left == Empty) "" else left.toString()
            val rightStr = if (right == Empty) "" else right.toString()
            return "($leftStr$value$rightStr)"
        }
    }

    object Empty : BinarySearchTree {
        override fun size(): Int = 0
        override fun contains(value: String): Boolean = false
        override fun toString(): String = ""
    }

    fun insert(value: String): BinarySearchTree {
        return when (this) {
            is Empty -> Node(value)
            is Node -> {
                when {
                    value < this.value -> Node(this.value, left.insert(value), right)
                    value > this.value -> Node(this.value, left, right.insert(value))
                    else -> this // No duplicates allowed
                }
            }
        }
    }
}
