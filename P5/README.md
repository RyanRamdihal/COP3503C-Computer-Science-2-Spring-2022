# Polynomial Multiplication via Karatsuba's Algorithm

COP 3503 Homework #5. Multiplies two polynomials whose degrees are one less than a power of two, using a Karatsuba-style divide-and-conquer that runs in O(n^log2(3)) ≈ O(n^1.585) instead of the O(n^2) grade-school approach.

## The Problem

Given two polynomials A(x) and B(x), each of degree 2^n − 1 (so 2^n coefficients), compute C(x) = A(x) · B(x). The product has degree 2^(n+1) − 2.

Input on stdin:

```
n
a_(2^n - 1)  a_(2^n - 2)  ...  a_1  a_0
b_(2^n - 1)  b_(2^n - 2)  ...  b_1  b_0
```

Output on stdout: 2^(n+1) − 1 lines, the coefficients of the product from the highest-degree term down to the constant, one per line.

For example, (5x³ − 7x² + 8x + 6) · (2x³ + 3x² + 0x + 4) is given as:

```
2
5 -7 8 6
2 3 0 4
```

and produces 10, 1, −5, 56, −10, 32, 24 — that is, 10x⁶ + x⁵ − 5x⁴ + 56x³ − 10x² + 32x + 24.

Coefficients can be up to 10⁶ in absolute value, so the product coefficients may overflow int and require `long`.

## The Algorithm

The grade-school method multiplies every coefficient of A by every coefficient of B, which is Θ(n²). Karatsuba's insight is that with a clever rearrangement, you only need three half-size products instead of four.

Split each polynomial at the midpoint:

```
A(x) = A_L(x) · x^(n/2) + A_R(x)
B(x) = B_L(x) · x^(n/2) + B_R(x)
```

Their product expands to:

```
A·B = (A_L · B_L) · x^n
    + (A_L · B_R + A_R · B_L) · x^(n/2)
    + (A_R · B_R)
```

The naive recursion computes four half-size products: A_L·B_L, A_L·B_R, A_R·B_L, A_R·B_R. Karatsuba's trick is to compute only:

```
left  = A_L · B_L
right = A_R · B_R
mid   = (A_L + A_R) · (B_L + B_R)
```

and then recover the middle cross terms with one subtraction:

```
A_L · B_R + A_R · B_L = mid − left − right
```

Three half-size multiplications instead of four gives the recurrence T(n) = 3·T(n/2) + O(n), which solves to T(n) = O(n^log2(3)) ≈ O(n^1.585).

## Base Case

Once the subproblem shrinks to length 32 (1 << 5), the code falls back to the O(n²) loop in `multSlow`. Below that size, the overhead of splitting, allocating arrays, and doing the extra additions outweighs the asymptotic savings.

## Storage Convention

Each polynomial is stored as a `long[]` where index `i` holds the coefficient of x^i — so the constant term lives at index 0 and the leading term at the highest index. The input is read in the opposite order (highest-degree first), so `main` fills the array in reverse.

Array lengths are always rounded up to a power of two so the recursive split is clean. Unused high-order slots simply hold zero.
