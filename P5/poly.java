import java.util.*;

public class poly {

	private long[] coeff;
	private int length;

	// Constructs a polynomial, rounding the array length up to the next power of 2.
	public poly(long[] vals) {
		length = 1;
		while (length < vals.length) length <<= 1;
		coeff = new long[length];
		for (int i=0; i<vals.length; i++)
			coeff[i] = vals[i];
	}

	// Prints coefficients from x^leadTerm down to the constant, one per line.
	public void print(int leadTerm) {
		StringBuffer sb = new StringBuffer();
		for (int i=leadTerm; i>=0; i--)
			sb.append(coeff[i]+"\n");
		System.out.print(sb);
	}

	// Returns this + other.
	public poly add(poly other) {
		long[] res = new long[Math.max(length, other.length)];
		for (int i=0; i<res.length; i++) {
			if (i<length) res[i] += coeff[i];
			if (i<other.length) res[i] += other.coeff[i];
		}
		return new poly(res);
	}

	// Returns this - other.
	public poly sub(poly other) {
		long[] res = new long[Math.max(length, other.length)];
		for (int i=0; i<res.length; i++) {
			if (i<length) res[i] += coeff[i];
			if (i<other.length) res[i] -= other.coeff[i];
		}
		return new poly(res);
	}

	// Grade-school O(n^2) multiplication.
	public poly multSlow(poly other) {
		long[] res = new long[length+other.length];
		for (int i=0; i<length; i++)
			for (int j=0; j<other.length; j++)
				res[i+j] += (coeff[i]*other.coeff[j]);
		return new poly(res);
	}

	// Equality check for testing.
	public boolean equal(poly other) {
		if (length != other.length) return false;
		for (int i=0; i<length; i++)
			if (coeff[i] != other.coeff[i])
				return false;
		return true;
	}

	// Karatsuba multiplication: three recursive products instead of four.
	public poly mult(poly other) {

		// Switch to the O(n^2) algorithm once the problem is small.
		if (length <= (1<<5)) return multSlow(other);

		// Split each polynomial into high and low halves: a = aLeft*x^(n/2) + aRight.
		poly aLeft = getLeft();
		poly aRight = getRight();
		poly bLeft = other.getLeft();
		poly bRight = other.getRight();

		poly sumA = aLeft.add(aRight);
		poly sumB = bLeft.add(bRight);

		// Three half-size products yield all four needed cross terms.
		poly left  = aLeft.mult(bLeft);
		poly right = aRight.mult(bRight);
		poly mid   = sumA.mult(sumB);

		// mid currently holds (aL+aR)(bL+bR); subtract off left and right to isolate the cross terms.
		mid = mid.sub(left);
		mid = mid.sub(right);

		// Combine the three pieces with the appropriate x-power shifts.
		long[] res = new long[2*length];
		for (int i=0; i<right.length; i++)
			res[i] += right.coeff[i];
		for (int i=0; i<left.length; i++)
			res[i+right.length] += left.coeff[i];
		for (int i=0; i<mid.length; i++)
			res[i+mid.length/2] += mid.coeff[i];

		return new poly(res);
	}

	// High half (most significant terms).
	private poly getLeft() {
		long[] res = new long[length/2];
		for (int i=length/2; i<length; i++)
			res[i-length/2] = coeff[i];
		return new poly(res);
	}

	// Low half (least significant terms).
	private poly getRight() {
		long[] res = new long[length/2];
		for (int i=0; i<length/2; i++)
			res[i] = coeff[i];
		return new poly(res);
	}

	public static void main(String[] args) {

		Scanner stdin = new Scanner(System.in);
		int pow = stdin.nextInt();

		// Input is given highest-degree first, but coeff[i] stores x^i, so fill in reverse.
		long[] tmp = new long[1<<pow];
		for (int i=tmp.length-1; i>=0; i--)
			tmp[i] = stdin.nextLong();
		poly firstPoly = new poly(tmp);

		tmp = new long[1<<pow];
		for (int i=tmp.length-1; i>=0; i--)
			tmp[i] = stdin.nextLong();
		poly secondPoly = new poly(tmp);

		// Product has 2^(pow+1) - 1 coefficients, so the lead term sits at index 2^(pow+1) - 2.
		poly res = firstPoly.mult(secondPoly);
		res.print((1<<(pow+1))-2);
	}
}
