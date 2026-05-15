# Dance Recital (Kattis: dancerecital)

**Course:** COP 3503 - Spring 2022, Section 2
**Assignment:** Recitation Program #2
**Author:** Ryan Ramdihal
**Instructor:** Professor Arup Guha
**Date:** 02/15/2022

## Problem

Kattis problem link: https://open.kattis.com/problems/dancerecital

A dance recital consists of up to 10 dance routines. Each routine uses a set of dancers (represented by characters). Between two consecutive routines, every dancer who appears in both routines must make a "quick change." The goal is to find the ordering of the routines that **minimizes the total number of quick changes** required across the whole recital.

With up to 10 routines, there are 10! = 3,628,800 possible orderings. A naive evaluation of every permutation from scratch is too slow, so the solution uses a backtracking-based permutation approach (as taught in COP 3502 and reinforced in the Backtracking section of this course).

## Solution Overview

The program reads the number of routines and the string of dancers in each routine, then explores all permutations using a backtracking algorithm:

- `input[]` stores each routine's dancer string.
- `test[]` tracks the current permutation being explored (indices of routines).
- `tryRoutine[]` marks which routines are already placed in the current permutation.
- `quick[]` stores the number of quick changes between each consecutive pair of routines in the current permutation.
- `high` holds the best (minimum) total quick changes found so far, initialized to a large value.

For each newly placed routine, the program counts how many of its dancers also appear in the immediately preceding routine (a quick change), accumulates the running total, and prunes branches that already exceed `high`. When a full permutation is built, `high` is updated. Backtracking continues until all permutations have been explored.

The minimum number of quick changes is printed at the end.

## Files

- `dancerecital.java` - source code for the solution.
- `README.md` - this file.
- Kattis accepted screenshot - showing username and checkmarks for all test cases (required submission).

## How to Compile and Run

```bash
javac dancerecital.java
java dancerecital < input.txt
```

### Input Format
- First line: integer `R` — the number of routines (1 ≤ R ≤ 10).
- Next `R` lines: each line is a string of unique uppercase letters representing the dancers in that routine.

### Output Format
- A single integer: the minimum total number of quick changes over all possible orderings of the routines.

### Example
**Input**
```
3
ABC
ABEF
DEF
```

**Output**
```
3
```

## Submission Requirements (per assignment directions)

1. Source file: `dancerecital.java`.
2. Screenshot of accepted Kattis submission showing username and all test cases passing.
