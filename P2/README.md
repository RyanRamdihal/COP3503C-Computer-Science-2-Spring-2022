# Program #2: Tentaizu

**Course:** COP 3503 – Computer Science 2
**Semester:** Spring 2022
**Instructor:** Professor Guha
**Author:** Ryan Ramdihal
**Date:** 01/25/2022

## Overview

This program solves the **Tentaizu** puzzle (a Minesweeper-style logic puzzle) using a recursive
backtracking algorithm. Given a 7×7 board where some squares contain digits indicating the number
of bombs in their 8 surrounding cells, the program determines a valid placement of exactly
**10 bombs** that satisfies every numbered clue, then prints the completed board.

Bombs are denoted with `*`, empty squares with `.`, and numbered (clue) cells keep their digit.

## Files

| File | Description |
|------|-------------|
| `tentaizu.java` | Main Java source file containing the solver. |
| `tentaizu_sample.in` | Sample input file with test cases. |
| `P2-Directions.pdf` | Original assignment directions. |
| `README.md` | This file. |

## Compilation & Execution

Compile the program with:

```
javac tentaizu.java
```

Run it using standard input/output redirection:

```
java tentaizu < tentaizu_sample.in > myoutput.out
```

To compare the produced output against expected results:

- **Windows:** `fc myoutput.out correct.out`
- **Unix / Eustis:** `diff -w myoutput.out correct.out`

## Input Format

The first line contains an integer **N**, the number of test cases. Each test case is a 7×7 grid
where:

- A digit `0`–`8` represents a numbered clue (count of adjacent bombs).
- A period `.` represents an unknown cell that may or may not contain a bomb.

Blank lines separate cases.

### Example Input

```
2
1....3.
.......
.1..0..
...2...
.3....3
....1..
...1..1

...1.3.
12....2
.2.1...
...31..
...2..3
..1....
.......
```

## Output Format

For each test case the program prints:

```
Tentaizu Board #k:
<7 rows of the solved board>
```

followed by a blank line. If the puzzle is unsolvable, it prints `No solution found!` instead of
the board.

Solved cells use the following characters:

- `*` — a bomb
- `.` — confirmed empty cell
- digit — original numbered clue

## Algorithm

The solver walks the 49 cells in row-major order and, at each unnumbered cell, tries two options:

1. **Place a bomb** — increment the adjacent-bomb counter for each of the 8 neighbors, recurse.
2. **Don't place a bomb** — recurse without modifying counters.

Numbered clues are never overwritten; the recursion simply skips them.

### Pruning (the heart of backtracking)

To keep the search well below the brute-force 2⁴⁹ possibilities, the program prunes branches that
cannot possibly succeed:

- When the algorithm advances past cell `(r, c)`, any clue at `(r-1, c-2)` is **finalized** — all
  of its 8 neighbors have been decided. If the running bomb count for that clue does not match,
  the branch is abandoned immediately.
- Similar finalization checks are performed for the last column rolling over to the next row, and
  for the entire bottom row at the end.
- The total bomb count must be exactly 10; placements past that threshold are rejected.

### Data Structures

| Array | Purpose |
|-------|---------|
| `t[7][7]` | The original board clues (`-1` for unknown cells). |
| `t2[7][7]` | Running count of bombs adjacent to each cell, updated on place / undone on backtrack. |
| `square[7][7]` | `1` where the current trial path has placed a bomb. |
| `fill` | Running total of bombs placed (must equal 10 at the end). |

## Performance

Each puzzle solves in well under one second. The provided contest input contains 25 cases and the
program comfortably handles all of them within the time limit.
