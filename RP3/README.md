# Ternarian Weights

A Java solution to the [Ternarian Weights](https://open.kattis.com/problems/ternarianweights) problem on Kattis, completed for COP 3503 Spring 2022 (Section 2), Recitation Program #3.

## Problem Overview

Given a target weight `n`, the program determines how to balance a two-pan scale using weights that are powers of 3 (1, 3, 9, 27, 81, ...). Each power-of-3 weight may be placed on the left pan, the right pan (with the target), or left off entirely. The output lists which weights go on each pan so that the scale balances.

## Author

Ryan Ramdihal

## Files

- `weights.java` — The Java source file containing the solution.

## How It Works

The algorithm is a greedy, base-3 conversion approach:

1. **Precompute powers of 3** up to `3^19`, which is large enough to cover all valid input values.
2. **Greedy base-3 decomposition.** For each test case, the target weight is repeatedly reduced by the largest power of 3 that fits, recording how many of each power were used (digits 0, 1, or 2 in base 3).
3. **Eliminate digits of 2.** Since each weight value exists only once, a digit of 2 at position `i` is rewritten as `-1` at position `i` plus a carry of `+1` at position `i+1`. A digit of 3 carries over fully to the next position. This pass converts the representation into balanced ternary (digits `-1`, `0`, `+1`).
4. **Assign pans.** Positive digits indicate weights placed on the right pan (opposite the unknown target), and negative digits indicate weights placed on the left pan (same side as the target).

## Why This Works (Intuitive Proof)

Every positive integer has a unique representation in balanced ternary, where each digit is one of `{-1, 0, +1}` and the place values are powers of 3. Because the place values triple at each step, any digit of `2` at position `i` can always be rewritten as `3 - 1` at position `i`, i.e., a `+1` carry into position `i+1` and a `-1` at position `i`. Similarly, a digit of `3` is a clean carry. Applying these rewrites from least significant to most significant always terminates (each carry moves strictly up) and yields a representation with digits only in `{-1, 0, +1}`. Mapping `+1` → right pan and `-1` → left pan balances the scale, since the sum of right-pan weights minus the sum of left-pan weights equals exactly `n`. This runs in `O(log₃ n)` per test case, easily fast enough for the input bounds.

