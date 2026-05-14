# Program #1: Politics

Spring 2022 Computer Science 2 — programming assignment.

## Overview

This program reads a series of election cases from standard input. For each
case it prints the voters grouped by the candidate they support, with groups
appearing in the order the candidates were introduced and voters inside a
group appearing in the order they were read.

A "write-in" candidate (a candidate named by a voter but not present in the
official candidate list) is supported. Write-ins are appended to the
candidate list in first-seen order, so their groups appear after every
officially declared candidate's group.

## Files

- `politics.java` — the solution (single file, contains both the `politics`
  class with `main` and a helper `voter` class).
- `politics_sample.in` — the sample input provided with the assignment.
- `README.md` — this file.

## Input format

Each case begins with two integers on a line:

```
<numCandidates> <numVoters>
```

followed by `numCandidates` candidate names (one per token), then
`numVoters` lines of the form:

```
<voterName> <candidateName>
```

The input ends with a sentinel case of `0 0`.

### Example (`politics_sample.in`)

```
3 5
STEVENS
MICHAELS
JORDAN
BOB JORDAN
JACK STEVENS
MACK MICHAELS
BILL JORDAN
CHRIS MATTHEWS
1 5
FRED
SAM FRED
ARTHUR GEORGE
DANIEL HERBERT
MARK GEORGE
MIKE HERBERT
0 0
```

## Output format

For each case, print one voter name per line, ordered by:

1. The id of the candidate they support (official candidates first in the
   order they were declared, then write-ins in the order they were first
   named by a voter).
2. The order the voter was read from the input, as a tie-breaker.

### Expected output for the sample input

```
JACK
MACK
BOB
BILL
CHRIS
SAM
ARTHUR
MARK
DANIEL
MIKE
```

## How it works

The solution keeps a `HashMap<String,Integer>` that maps each candidate name
to a numeric id. Declared candidates get ids `0..numCandidates-1`. When a
voter names a candidate that isn't in the map, that candidate is registered
with the next available id and the running count of candidates is bumped so
future write-ins receive fresh ids.

Each voter is wrapped in a small `voter` object that stores the voter's
name, their 0-based input position, and the id of the candidate they
support. `voter` implements `Comparable<voter>` so a single `Arrays.sort`
puts voters into the required grouped order:

- Primary key: candidate id (ascending).
- Tie-breaker: input position (ascending).

After sorting, the voters are printed in order.

