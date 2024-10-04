import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Predicate;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Exercises {
    public static void main(String[] args) {
      
    }

    static Map<Integer, Long> change(long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        var counts = new HashMap<Integer, Long>();
        for (var denomination : List.of(25, 10, 5, 1)) {
            counts.put(denomination, amount / denomination);
            amount %= denomination;
        }
        return counts;
    }

    public static Optional<String> firstThenLowerCase(List<String> list, Predicate<String> predicate) {
        return list.stream()
                .filter(predicate)
                .findFirst()
                .map(String::toLowerCase);
    }

    static record Sayer(String phrase) {
        Sayer and(String word) {
            String separator = phrase.endsWith(" ") || word.startsWith(" ") ? "" : " ";
            return new Sayer(phrase + separator + word);
        }
    }

    public static Sayer say() {
        return new Sayer("");
    }

    public static Sayer say(String word) {
        return new Sayer(word);
    }

    public static long meaningfulLineCount(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return reader.lines()
                    .filter(line -> !line.trim().isEmpty())
                    .count();
        }
    }
}

record Quaternion(double a, double b, double c, double d) {

    static final Quaternion ZERO = new Quaternion(0, 0, 0, 0);
    static final Quaternion I = new Quaternion(0, 1, 0, 0);
    static final Quaternion J = new Quaternion(0, 0, 1, 0);
    static final Quaternion K = new Quaternion(0, 0, 0, 1);

    Quaternion {
        if (Double.isNaN(a) || Double.isNaN(b) || Double.isNaN(c) || Double.isNaN(d)) {
            throw new IllegalArgumentException("Coefficients cannot be NaN");
        }
    }

    Quaternion plus(Quaternion other) {
        return new Quaternion(a + other.a, b + other.b, c + other.c, d + other.d);
    }

    Quaternion times(Quaternion other) {
        double newA = a * other.a - b * other.b - c * other.c - d * other.d;
        double newB = a * other.b + b * other.a + c * other.d - d * other.c;
        double newC = a * other.c - b * other.d + c * other.a + d * other.b;
        double newD = a * other.d + b * other.c + c * other.b + d * other.a;
        return new Quaternion(newA, newB, newC, newD);
    }

    public Quaternion conjugate() {
        return new Quaternion(a, -b, -c, -d);
    }

    public List<Double> coefficients() {
        return List.of(a, b, c, d);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        if (a != 0) result.append(a);
        if (b != 0) result.append(result.length() > 0 && b > 0 ? "+" + b + "i" : b + "i");
        if (c != 0) result.append(result.length() > 0 && c > 0 ? "+" + c + "j" : c + "j");
        if (d != 0) result.append(result.length() > 0 && d > 0 ? "+" + d + "k" : d + "k");

        return result.length() == 0 ? "0" : result.toString();
    }
}

sealed interface BinarySearchTree permits Empty, Node {
    int size();
    boolean contains(String value);
    BinarySearchTree insert(String value);
}

final record Empty() implements BinarySearchTree {
    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean contains(String value) {
        return false;
    }

    @Override
    public BinarySearchTree insert(String value) {
        return new Node(value, this, this);
    }

    @Override
    public String toString() {
        return "()";
    }
}

final class Node implements BinarySearchTree {
    private final String value;
    private final BinarySearchTree left;
    private final BinarySearchTree right;

    Node(String value, BinarySearchTree left, BinarySearchTree right) {
        this.value = value;
        this.left = left;
        this.right = right;
    }

    @Override
    public int size() {
        return 1 + left.size() + right.size();
    }

    @Override
    public boolean contains(String value) {
        return this.value.equals(value) || left.contains(value) || right.contains(value);
    }

    @Override
    public BinarySearchTree insert(String value) {
        if (value.compareTo(this.value) < 0) {
            return new Node(this.value, left.insert(value), right);
        } else {
            return new Node(this.value, left, right.insert(value));
        }
    }

    @Override
    public String toString() {
        String leftStr = left instanceof Empty ? "" : left.toString();
        String rightStr = right instanceof Empty ? "" : right.toString();
        return "(" + leftStr + value + rightStr + ")";
    }
}
