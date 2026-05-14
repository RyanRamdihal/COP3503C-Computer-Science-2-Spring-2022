# Recitation Program #1 — CD

COP 3503 Spring 2022, Section 2
Kattis problem: https://open.kattis.com/problems/cd

## Problem Summary

Jack and Jill each own a collection of CDs, identified by unique integer IDs.
Each of their individual collections has no duplicates. Given both lists, count
how many CDs they own in common.

Input consists of multiple test cases. Each test case begins with two integers
`N` and `M` (the sizes of Jack's and Jill's collections, each up to 10^6),
followed by `N` lines of Jack's CD IDs and `M` lines of Jill's CD IDs. Input
terminates when `N = 0` and `M = 0`.

For each test case, print the number of CDs they both own.

## Approach

Because neither list has internal duplicates, any duplicate that appears when
both lists are merged must be a CD they both own. Insert every ID from both
lists into a `HashSet<String>`. The number of CDs in common is:

    (N + M) - set.size()

This runs in O(N + M) average time per test case, well within the limits.

## Why a HashSet

A naive double-loop comparison would be O(N * M) — up to 10^12 operations at
worst, which is far too slow. A `HashSet` provides average O(1) insertion and
lookup, reducing the total work to a single linear pass over both lists.

## Input/Output Performance

`Scanner` is too slow at this input size. The solution uses:

- `BufferedReader.readLine()` for input
- `StringTokenizer.nextToken()` + `Integer.parseInt(String)` for parsing the
  `N M` header line
- A `StringBuilder` to accumulate output, flushed once at the end with a single
  `System.out.print` call

## Files

- `cd.java` — source file
- `kattis-accepted.png` — screenshot of accepted submission on Kattis
  (showing username and all green checkmarks)
- `README.md` — this file

## Compile and Run

    javac cd.java
    java cd < input.txt

Where `input.txt` is a file in the format described above. Input also accepts
keyboard input terminated by `0 0`.

## Example

Input:

    3 3
    1
    2
    3
    2
    3
    4
    0 0

Output:

    2

Jack owns {1, 2, 3} and Jill owns {2, 3, 4}; they share CDs 2 and 3.
