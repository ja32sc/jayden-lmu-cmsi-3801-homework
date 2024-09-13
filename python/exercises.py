from dataclasses import dataclass
from collections.abc import Callable


def change(amount: int) -> dict[int, int]:
    if not isinstance(amount, int):
        raise TypeError('Amount must be an integer')
    if amount < 0:
        raise ValueError('Amount cannot be negative')
    counts, remaining = {}, amount
    for denomination in (25, 10, 5, 1):
        counts[denomination], remaining = divmod(remaining, denomination)
    return counts


def first_then_lower_case(array, predicate):
    for item in array:
        if predicate(item):
            return item.lower()
    return None


def powers_generator(base :int, limit: int):
    power = 0
    while (result := base ** power) <= limit:
        yield result
        power += 1


def say(message: str = '') -> Callable:
    def add(next_word: str = None):
        if next_word is None:
            return message
        return say(f"{message} {next_word}")

    return add


def meaningful_line_count(filename: str) -> int:
    try:
        with open(filename, 'r') as file:
            return sum(1 for line in file if line.strip() and not line.lstrip().startswith('#'))
    except FileNotFoundError as e:
        raise FileNotFoundError(f'No such file: {filename}') from e


class Quaternion:
    def __init__(self, a: float, b: float, c: float, d: float):
        self.a = a
        self.b = b
        self.c = c
        self.d = d

    @property
    def coefficients(self) -> tuple:
        return self.a, self.b, self.c, self.d

    @property
    def conjugate(self):
        return Quaternion(self.a, -self.b, -self.c, -self.d)

    def __add__(self, other: 'Quaternion') -> 'Quaternion':
        return Quaternion(self.a + other.a, self.b + other.b, self.c + other.c, self.d + other.d)

    def __mul__(self, other: 'Quaternion') -> 'Quaternion':
        a1, b1, c1, d1 = self.a, self.b, self.c, self.d
        a2, b2, c2, d2 = other.a, other.b, other.c, other.d
        return Quaternion(
            a1 * a2 - b1 * b2 - c1 * c2 - d1 * d2,
            a1 * b2 + b1 * a2 + c1 * d2 - d1 * c2,
            a1 * c2 - b1 * d2 + c1 * a2 + d1 * b2,
            a1 * d2 + b1 * c2 - c1 * b2 + d1 * a2
        )

    def __str__(self):
        terms = []
        if self.a:
            terms.append(f"{self.a}")
        if self.b:
            terms.append(f"{'+' if self.b >= 0 else '-'}{abs(self.b)}i")
        if self.c:
            terms.append(f"{'+' if self.c >= 0 else '-'}{abs(self.c)}j")
        if self.d:
            terms.append(f"{'+' if self.d >= 0 else '-'}{abs(self.d)}k")
        return ''.join(terms).lstrip('+') or '0'

    def __eq__(self, other: 'Quaternion') -> bool:
        return (self.a == other.a and
                self.b == other.b and
                self.c == other.c and
                self.d == other.d)
