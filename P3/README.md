# COP 3503 Homework #3 — Destroying Connectivity

**File:** `destroy.java`
**Time limit:** 2 seconds per test case
**I/O:** Standard input / standard output

## Problem

A network of `n` computers is modeled as an undirected graph with `m` initial connections. The **connectivity** of a graph is defined as the sum of the squares of the sizes of its connected components:

```
connectivity = sum over components C of |C|^2
```

For example, a graph with components of size 2, 6, and 1 has connectivity `2^2 + 6^2 + 1^2 = 41`.

A list of `d` connections will be destroyed one at a time. The program must output the connectivity of the network **before** any destruction, and **after** each of the `d` destructions — a total of `d + 1` lines.

## Input format

```
n m d
u_1 v_1
u_2 v_2
...
u_m v_m
e_1
e_2
...
e_d
```

- `1 ≤ n ≤ 10^5` — number of computers (1-indexed).
- `1 ≤ m ≤ 3·10^5` — number of initial connections (1-indexed).
- `1 ≤ d ≤ m` — number of connections that will be destroyed.
- Each `u_i v_i` is a unique unordered pair (`u_i ≠ v_i`).
- Each `e_i` is a distinct connection index in `[1, m]`, listed in destruction order.

## Output format

`d + 1` lines. Line 1 is the connectivity of the original graph; line `i + 1` is the connectivity after the first `i` destructions.

## Approach

A disjoint-set (union-find) data structure can merge components in nearly O(1), but it cannot efficiently split them. The destructions in this problem are splits — the wrong direction for DSU.

The standard trick: **process events offline, in reverse.**

1. Mark every edge that gets destroyed at some point. Build a DSU containing only the edges that **survive** to the end. The connectivity of that graph is the **last** line of output.
2. Walk the destruction list backwards. Re-adding a destroyed edge is a `union`. After each re-add, record the current connectivity.
3. Print the recorded values in forward order.

### Tracking connectivity in O(1) per union

We do not recompute the sum of squares from scratch after each union. Instead, the DSU maintains a running total `sumSizeSq`. When two components of sizes `A` and `B` merge:

```
delta = (A + B)^2 − (A^2 + B^2) = 2 · A · B
```

So `sumSizeSq += 2·A·B` on each successful union, giving O(1) connectivity queries.

### Overflow

A single component can hold up to `n = 10^5` nodes, contributing `(10^5)^2 = 10^10` to the sum — well past `int` range. All size and sum-of-squares math is done in `long`.

## Complexity

Let `α` denote the inverse Ackermann function (effectively constant).

- Building the surviving graph: `O(m · α(n))`
- Reverse re-adds: `O(d · α(n))`
- Total: **`O((n + m) · α(n))`**, comfortably within the 2-second limit.

## Files

- `destroy.java` — solution source (a `destroy` class with `main`, plus a `djset` class).

## How to compile and run

```bash
javac destroy.java
java destroy < input.txt
```

## Sample I/O

**Sample 1**

Input:
```
9 8 2
1 5
2 3
2 6
3 6
6 7
7 8
7 9
8 9
5
3
```

Output:
```
41
23
23
```

**Sample 2**

Input:
```
3 3 3
1 2
1 3
2 3
3
1
2
```

Output:
```
9
9
5
3
```

## Implementation notes

- The DSU uses **path compression** in `find`. It does not use union-by-size/rank — `union` always attaches `v`'s root under `u`'s root — which is sufficient for the given constraints when combined with path compression.
- Non-root entries in the `size[]` array are set to `-1` after merging so that any accidental read on a non-root is obviously wrong.
- Input is read with `Scanner` for simplicity. If pushing closer to the time limit, swapping in `BufferedReader` + manual tokenization would be the easiest speedup.
