# COP 3503 Homework #4: Maze Magic

**Filename:** `maze.java`
**Language:** Java
**Time Limit:** 4 seconds per input case
**I/O:** Standard Input / Standard Output

## Problem Description

Sastry is lost in a maze and needs help getting out as quickly as possible. The maze is an `r x c` grid where each cell is one of the following:

- `*` — Sastry's starting location (exactly one in the grid)
- `$` — the exit (exactly one in the grid)
- `.` — a normal, walkable square
- `!` — a forbidden square that cannot be entered
- A capital letter (`A`–`Z`) — a teleportation square

From any walkable square, Sastry can move one step up, down, left, or right (one move). If Sastry is on a teleportation square labeled with some letter `X`, he may, in a single move, teleport to any other square labeled `X` in the grid. Teleportation is optional — he can still step normally off of a lettered square onto an adjacent walkable square.

The goal is to compute the fewest moves required for Sastry to reach `$`, or output `-1` if no path exists.

## Input Format

```
r c
<row 1 of the grid>
<row 2 of the grid>
...
<row r of the grid>
```

Constraints: `2 ≤ r ≤ 1000` and `2 ≤ c ≤ 1000`. If a letter appears in the grid, it appears in at least two cells.

## Output Format

A single integer — the minimum number of moves to reach `$`, or `-1` if unreachable.

## Sample Cases

| Input | Output |
|-------|--------|
| `3 4`<br>`.$!*`<br>`.!!.`<br>`....` | `8` |
| `4 2`<br>`..`<br>`*!`<br>`!$`<br>`..` | `-1` |
| `6 6`<br>`C..$B.`<br>`......`<br>`!!!!!!`<br>`....CB`<br>`.*.AAA`<br>`C.B.CB` | `4` |

## Approach

The shortest-path problem on an unweighted graph is solved with **Breadth-First Search (BFS)**. Each grid cell is a node, and there is an edge of weight 1 between any two cells that Sastry can move between in a single move (orthogonal neighbors, plus all pairs of same-letter teleport cells).

### Key Optimization: Teleport Letter "Burning"

A naive BFS that connects every pair of same-letter cells as graph edges would be `O((r·c)²)` in the worst case (e.g., a grid mostly filled with `A`s). To stay within the time limit on the hardest 10% of cases — where a single letter can appear arbitrarily many times — the solution uses an important trick:

The **first time** BFS dequeues any cell of letter `X`, it enqueues every other unvisited cell labeled `X` (each at distance `dist[current] + 1`) and marks letter `X` as "used." All subsequent dequeues of `X` cells skip this step, because every `X` cell was already added to the frontier the first time.

This guarantees each teleport letter is expanded only once over the lifetime of BFS, so the total work for teleports is `O(r·c)` instead of quadratic.

### Algorithm Outline

1. Read the grid and record, for each letter, the set of cell indices where it appears.
2. Find the start `*` and the end `$`.
3. Run BFS from `*`:
   - For each dequeued cell, try the four orthogonal neighbors (skipping out-of-bounds, `!`, or already-visited cells).
   - If the dequeued cell is a letter that has not been "used" yet, enqueue every unvisited cell of that letter at distance `dist + 1` and mark the letter used.
   - Return `dist[end]` as soon as the end is dequeued.
4. If BFS finishes without reaching the end, output `-1`.

## Complexity

- **Time:** `O(r·c)` — each cell is visited once, and each teleport letter is expanded once.
- **Space:** `O(r·c)` for the distance array, the BFS queue, and the per-letter location sets.

## Files

- `maze.java` — full solution (BFS with teleport-letter burning)
