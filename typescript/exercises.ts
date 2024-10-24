import { open } from "node:fs/promises"

export function change(amount: bigint): Map<bigint, bigint> {
  if (amount < 0) {
    throw new RangeError("Amount cannot be negative")
  }
  let counts: Map<bigint, bigint> = new Map()
  let remaining = amount
  for (const denomination of [25n, 10n, 5n, 1n]) {
    counts.set(denomination, remaining / denomination)
    remaining %= denomination
  }
  return counts
}

export function firstThenApply<T, U>(
  items: T[],
  predicate: (item: T) => boolean,
  consumer: (item: T) => U
): U | undefined {
  const foundItem = items.find(predicate);
  return foundItem !== undefined ? consumer(foundItem) : undefined;
}

export function* powersGenerator(base: bigint): Generator<bigint> {
  for(let power = 1n; ; power *= base) {
    yield power
  }
}

export async function meaningfulLineCount(filePath: string): Promise<number> {
  try {
    const fileHandle = await open(filePath, 'r');
    const fileStream = fileHandle.createReadStream();
    let count = 0;

    for await (const chunk of fileStream) {
      const lines = chunk.toString().split('\n');

      count += lines
        .map((line: string) => line.trim()) // Trim each line
        .filter((line: string) => line !== "" && !line.startsWith("#"))
        .length;
    }

    await fileHandle.close();
    return count;
  } catch (error) {
    if (error instanceof Error) {
      throw new Error("Error reading file: " + error.message);
    } else {
      throw new Error("Unknown error occurred");
    }
  }
}

interface Sphere {
  kind: "Sphere"
  radius: number
}

interface Box {
  kind: "Box"
  width: number
  length: number
  depth: number
}

export type Shape = Sphere | Box

export function surfaceArea(shape: Shape): number {
  switch (shape.kind) {
    case "Sphere":
      return 4 * Math.PI * shape.radius ** 2
    case "Box":
      return 2 * (shape.width * shape.length + shape.width * shape.depth + shape.length * shape.depth)
  }
}

export function volume(shape: Shape) : number {
  switch (shape.kind) {
    case "Sphere":
      return (4 / 3) * Math.PI * shape.radius ** 3;
    case "Box":
      return shape.width * shape.length * shape.depth;
  }
}

export interface BinarySearchTree<T> {
    size(): number;
    insert(value: T): BinarySearchTree<T>;
    contains(value: T): boolean;
    inorder(): Iterable<T>;
}

export class Empty<T> implements BinarySearchTree<T> {
    size(): number {
        return 0;
    }

    insert(value: T): BinarySearchTree<T> {
        return new Node(value, new Empty<T>(), new Empty<T>());
    }

    contains(value: T): boolean {
        return false;
    }

    *inorder(): Iterable<T> {
    }

    toString(): string {
        return "()";
    }
}

class Node<T> implements BinarySearchTree<T> {
    constructor(
        private value: T,
        private left: BinarySearchTree<T>,
        private right: BinarySearchTree<T>
    ) {}

    size(): number {
        return 1 + this.left.size() + this.right.size();
    }

    insert(value: T): BinarySearchTree<T> {
        if (value < this.value) {
            const newLeft = this.left.insert(value);
            return new Node(this.value, newLeft, this.right);
        } else if (value > this.value) {
            const newRight = this.right.insert(value);
            return new Node(this.value, this.left, newRight);
        }
        return this;
    }

    contains(value: T): boolean {
        if (value === this.value) {
            return true;
        } else if (value < this.value) {
            return this.left.contains(value);
        } else {
            return this.right.contains(value);
        }
    }

    *inorder(): Iterable<T> {
        yield* this.left.inorder();
        yield this.value;
        yield* this.right.inorder();
    }

    toString(): string {
        const leftStr = this.left.toString();
        const rightStr = this.right.toString();
        return `(${leftStr}${this.value}${rightStr})`.replace(/\(\)/g, '');
    }
}
