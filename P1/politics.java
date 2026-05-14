import java.util.*;

/**
 * Program #1: Politics
 *
 * Reads a series of election cases from standard input. Each case lists a set
 * of declared candidates and a set of voters (each paired with the candidate
 * they support). Voters who support a write-in name not on the official
 * candidate list are still counted, but their candidate is treated as being
 * appended to the end of the candidate list in the order it first appears.
 *
 * For every case we print the voters grouped by candidate. Groups appear in
 * the order the candidates were introduced (official list first, write-ins
 * after in first-seen order). Within a group, voters appear in the order
 * they were read from the input.
 *
 * Input format:
 *   <numCandidates> <numVoters>
 *   <candidate_1>
 *   ...
 *   <candidate_numCandidates>
 *   <voter_1_name> <voter_1_choice>
 *   ...
 *   <voter_numVoters_name> <voter_numVoters_choice>
 * Terminates when a case begins with "0 0".
 */
public class politics {

	public static void main(String[] args) {

		Scanner stdin = new Scanner(System.in);

		// Read the header of the first case.
		int numCand = stdin.nextInt();
		int numVoters = stdin.nextInt();

		// Process each case until the "0 0" sentinel is encountered.
		while (numCand != 0) {

			// Maps each candidate name to a numeric id. The id determines the
			// final group ordering: lower id = printed earlier. Declared
			// candidates take ids 0..numCand-1; write-ins get ids numCand,
			// numCand+1, ... as they are discovered.
			HashMap<String,Integer> map = new HashMap<String,Integer>();

			// Read in the official candidates and assign each an id equal to
			// its position in the input list.
			for (int i=0; i<numCand; i++) {
				String s = stdin.next();
				map.put(s, i);
			}

			// One voter entry per supporter for this case.
			voter[] voters = new voter[numVoters];

			// Read each voter line: "<name> <candidate>".
			for (int i=0; i<numVoters; i++) {
				String name = stdin.next();
				String cand = stdin.next();

				// Known candidate: look up the existing id.
				if (map.containsKey(cand))
					voters[i] = new voter(name, i, map.get(cand));

				// Unknown (write-in) candidate: register them with the next
				// available id, then bump numCand so future write-ins get a
				// fresh id of their own.
				else {
					map.put(cand, numCand);
					voters[i] = new voter(name, i, numCand);
					numCand++;
				}
			}

			// Sorting by (candidate id, input order) gives the required
			// grouped output in a single pass.
			Arrays.sort(voters);
			for (int i=0; i<voters.length; i++)
				System.out.println(voters[i]);

			// Read the header of the next case (or the "0 0" terminator).
			numCand = stdin.nextInt();
			numVoters = stdin.nextInt();
		}
	}
}

/**
 * Represents a single voter and how they should be ordered in the output.
 *
 * Each voter remembers their own name (for printing), the position they were
 * read from the input (used as the tie-breaker), and the id of the candidate
 * they support (used as the primary sort key).
 */
class voter implements Comparable<voter> {

	private String name;     // Voter's name, as printed in the output.
	private int initPos;     // 0-based read order; tie-breaker within a group.
	private int candNum;     // Id of the supported candidate; primary sort key.

	public voter(String s, int pos, int support) {
		name = s;
		initPos = pos;
		candNum = support;
	}

	/**
	 * Order voters first by their candidate's id (so all supporters of the
	 * same candidate end up adjacent, in candidate-introduction order), then
	 * by the order they appeared in the input (stable within a group).
	 */
	public int compareTo(voter other) {
		if (this.candNum != other.candNum) return this.candNum - other.candNum;
		return this.initPos - other.initPos;
	}

	/** Output representation is just the voter's name. */
	public String toString() {
		return name;
	}
}
