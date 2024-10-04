import Foundation

struct NegativeAmountError: Error {}
struct NoSuchFileError: Error {}

func change(_ amount: Int) -> Result<[Int:Int], NegativeAmountError> {
    if amount < 0 {
        return .failure(NegativeAmountError())
    }
    var (counts, remaining) = ([Int:Int](), amount)
    for denomination in [25, 10, 5, 1] {
        (counts[denomination], remaining) =
            remaining.quotientAndRemainder(dividingBy: denomination)
    }
    return .success(counts)
}

func firstThenLowerCase(of array: [String], satisfying predicate: (String) -> Bool) -> String? {
    return array.first(where: predicate)?.lowercased()
}


struct Sayer {
    let phrase: String

    func and(_ word: String) -> Sayer {
        return Sayer(phrase: phrase + " " + word)
    }
}


func say(_ word: String = "")-> Sayer {
    return Sayer(phrase: word)
}


func meaningfulLineCount(_ fileName: String) -> Result<Int, NoSuchFileError> {
    guard let contents = try? String(contentsOfFile: fileName) else {
        return .failure(NoSuchFileError())
    }
    return .success(contents.split(separator: "\n").filter { !$0.isEmpty }.count)
}


struct Quaternion: CustomStringConvertible, Equatable {
    let a, b, c, d: Double

    static let ZERO = Quaternion(a: 0, b: 0, c: 0, d: 0)
    static let I = Quaternion(a: 0, b: 1, c: 0, d: 0)
    static let J = Quaternion(a: 0, b: 0, c: 1, d: 0)
    static let K = Quaternion(a: 0, b: 0, c: 0, d: 1)

    init(a: Double = 0, b: Double = 0, c: Double = 0, d: Double = 0) {
        self.a = a
        self.b = b
        self.c = c
        self.d = d
    }

    var coefficients: [Double] {
        return [a, b, c, d]
    }

    var conjugate: Quaternion {
        return Quaternion(a: a, b: -b, c: -c, d: -d)
    }

    static func +(lhs: Quaternion, rhs: Quaternion) -> Quaternion {
        return Quaternion(a: lhs.a + rhs.a, b: lhs.b + rhs.b, c: lhs.c + rhs.c, d: lhs.d + rhs.d)
    }

    static func *(lhs: Quaternion, rhs: Quaternion) -> Quaternion {
        let a = lhs.a * rhs.a - lhs.b * rhs.b - lhs.c * rhs.c - lhs.d * rhs.d
        let b = lhs.a * rhs.b + lhs.b * rhs.a + lhs.c * rhs.d - lhs.d * rhs.c
        let c = lhs.a * rhs.c - lhs.b * rhs.d + lhs.c * rhs.a + lhs.d * rhs.b
        let d = lhs.a * rhs.d + lhs.b * rhs.c - lhs.c * rhs.b + lhs.d * rhs.a
        return Quaternion(a: a, b: b, c: c, d: d)
    }


    var description: String {
        var result = ""

        if a != 0 {
            result += "\(a)"
        }

        if b != 0 {
            result += (b > 0 && !result.isEmpty ? "+" : "") + "\(b)i"
        }

        if c != 0 {
            result += (c > 0 && !result.isEmpty ? "+" : "") + "\(c)j"
        }

        if d != 0 {
            result += (d > 0 && !result.isEmpty ? "+" : "") + "\(d)k"
        }

        return result.isEmpty ? "0" : result
    }

}


indirect enum BinarySearchTree: CustomStringConvertible {
    case empty
    case node(value: String, left: BinarySearchTree, right: BinarySearchTree)

    var size: Int {
        switch self {
        case .empty: return 0
        case let .node(_, left, right): return 1 + left.size + right.size
        }
    }

    func contains(_ value: String) -> Bool {
        switch self {
        case .empty: return false
        case let .node(v, left, right):
            if value == v { return true }
            if value < v { return left.contains(value) }
            return right.contains(value)
        }
    }

    func insert(_ value: String) -> BinarySearchTree {
        switch self {
        case .empty: return .node(value: value, left: .empty, right: .empty)
        case let .node(v, left, right):
            if value < v { return .node(value: v, left: left.insert(value), right: right) }
            return .node(value: v, left: left, right: right.insert(value))
        }
    }

    var description: String {
        switch self {
        case .empty: return "()"
        case let .node(value, left, right):
            let leftDesc = left.description
              let rightDesc = right.description
                return "(\(leftDesc)\(value)\(rightDesc))"
        }
    }
}
