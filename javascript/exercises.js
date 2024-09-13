import { open } from "node:fs/promises"


export function change(amount) {
  if (!Number.isInteger(amount)) {
    throw new TypeError("Amount must be an integer")
  }
  if (amount < 0) {
    throw new RangeError("Amount cannot be negative")
  }
  let [counts, remaining] = [{}, amount]
  for (const denomination of [25, 10, 5, 1]) {
    counts[denomination] = Math.floor(remaining / denomination)
    remaining %= denomination
  }
  return counts
}

// Write your first then lower case function here
export function firstThenLowerCase(strings, predicate) {
  const first = strings.find(predicate)
  return first?.toLowerCase()

}

// Write your powers generator here
export function* powersGenerator({ofBase, upTo}) {
  let value = 1
  while (value <= upTo) {
    yield value
    value *= ofBase
  }
}

// Write your say function here
export function say(word = "") {
  let sentence = word.trim()
  return function nextWord(next = "") {
    if (next === "") {
      return sentence
    }
    sentence = `${sentence} ${next.trim()}`
    return nextWord
  }
}

// Write your line count function here
export async function meaningfulLineCount(filePath) {
  try {
    const file = await open(filePath, 'r')
    let count = 0
    for await (const line of file.readable) {
      if (line.toString().trim().length > 0) {
        count++
      }
    }
    await file.close()
    return count
  } catch (error) {
    throw new Error("Error reading file: " + error.message)
  }
}

// Write your Quaternion class here
export class Quaternion {
  constructor(a, b, c, d) {
    this._a = a
    this._b = b
    this._c = c
    this._d = d
    Object.freeze(this)
  }

  get a() { return this._a }
  get b() { return this._b }
  get c() { return this._c }
  get d() { return this._d }

  get coefficients() {
    return [this._a, this._b, this._c, this._d]
  }

  get conjugate() {
    return new Quaternion(this._a, -this._b, -this._c, -this._d)
  }

  plus(other) {
    return new Quaternion(
      this._a + other.a,
      this._b + other.b,
      this._c + other.c,
      this._d + other.d
    )
  }

  times(other) {
    return new Quaternion(
      this._a * other.a - this._b * other.b - this._c * other.c - this._d * other.d,
      this._a * other.b + this._b * other.a + this._c * other.d - this._d * other.c,
      this._a * other.c - this._b * other.d + this._c * other.a + this._d * other.b,
      this._a * other.d + this._b * other.c - this._c * other.b + this._d * other.a
    )
  }

  toString() {
    const parts = []
    if (this._a !== 0) parts.push(this._a.toString())
    if (this._b !== 0) parts.push((this._b === 1 ? '' : this._b === -1 ? '-' : this._b.toString()) + 'i')
    if (this._c !== 0) parts.push((this._c === 1 ? '' : this._c === -1 ? '-' : this._c.toString()) + 'j')
    if (this._d !== 0) parts.push((this._d === 1 ? '' : this._d === -1 ? '-' : this._d.toString()) + 'k')
    return parts.join('+').replace(/\+\-/, '-')
  }
}
