# Recitation Program #4 - Shortest Path 2

**Course:** COP 3503 Spring 2022, Section 2
**Author:** Ryan Ramdihal
**Problem:** [Kattis - shortestpath2](https://open.kattis.com/problems/shortestpath2)

## Overview

This program solves the *Single Source Shortest Path with Time Tables* problem from Kattis. It is a variation of the classic single-source shortest path problem in which each edge is only available at specific times defined by a starting time and a recurring period. The program computes the shortest travel time from a given start node to each queried destination node.

## Approach

The solution uses a modified version of **Dijkstra's algorithm**. The standard relaxation step is adjusted to account for the time-based availability of each edge:

- Each edge has four parameters: `end` (destination), `startT` (first time the edge becomes available), `period` (interval at which it is available again), and `cost` (travel time across the edge).
- When relaxing an edge, the algorithm calculates the **next available time** the edge can be used given the current arrival time at a node, then adds the cost of traversing the edge.
- If `period == 0`, the edge can only be used at `startT`. If the current time has already passed `startT`, that edge is unusable.
- A priority queue ordered by current shortest known distance is used to always expand the node with the minimum distance first.
- If a destination is unreachable (`distance == Integer.MAX_VALUE`), the program prints `Impossible`.

## Files

- `shortestpath2.java` — Java source file containing the full solution.
- `README.md` — This file.
- Kattis accepted screenshot — Screenshot showing the solution was accepted on all test cases.

## Input Format

Each test case begins with four integers: `n` (number of nodes), `m` (number of edges), `q` (number of queries), and `s` (start node). The next `m` lines each describe an edge with four integers: `u v t0 P d` (from `u` to `v`, first available at `t0`, recurring every `P` time units, taking `d` to traverse). The next `q` lines each contain a single query node. Input ends with a line of four zeros.

## Output Format

For each query, the program prints the shortest time from the start node to the query node, or `Impossible` if no path exists. A blank line is printed after each test case.

