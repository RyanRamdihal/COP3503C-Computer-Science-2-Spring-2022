// ============================================================================
// COP 3503 - Homework #3: Destroying Connectivity
// File:    destroy.java
// Date:    2/25/2022
//
// Summary:
//   Given an undirected graph of n computers and m connections, plus a list of
//   d connections that will be destroyed one at a time, output the "connectivity"
//   of the graph (the sum of the squares of the sizes of each connected
//   component) BEFORE any destruction, then after each destruction.
//
// Key Insight (why this works in O((n + m) * alpha(n))):
//   A disjoint-set (union-find) data structure can MERGE components quickly,
//   but it cannot efficiently SPLIT them. The destructions in this problem
//   are splits, which is the wrong direction.
//
//   The trick is to process the events OFFLINE and in REVERSE:
//     1. Pretend every "destroyed" edge was never there, and build the graph
//        out of only the edges that survive to the end.
//     2. That final graph's connectivity is the LAST line of output.
//     3. Walk the destruction list backwards, re-adding edges one at a time
//        (a union operation). After each re-add, record the connectivity.
//     4. Print the recorded values in forward order.
//
// Tracking Connectivity Efficiently:
//   We need sum-of-component-sizes-squared after every union.
//   Recomputing it from scratch would be O(n) per query -- too slow.
//   Instead, the djset class maintains a running total `sumSizeSq`.
//   When two components of sizes A and B merge into one of size A+B:
//       delta = (A+B)^2 - (A^2 + B^2) = 2*A*B
//   so we just add 2*A*B to the running total on each successful union.
//
// Note on Overflow:
//   With n up to 1e5, a single component can contribute up to (1e5)^2 = 1e10,
//   which overflows a 32-bit int. All size-squared math is done in `long`.
// ============================================================================

import java.util.*;

public class destroy {

    public static void main(String[] args) {

        // -- Read header: n vertices, m edges, d destructions. --
        Scanner stdin = new Scanner(System.in);
        int numV = stdin.nextInt();
        int numE = stdin.nextInt();
        int numD = stdin.nextInt();

        // -- Read all m edges. Convert from 1-indexed (input) to 0-indexed (arrays). --
        int[][] edges = new int[numE][2];
        for (int i = 0; i < numE; i++) {
            edges[i][0] = stdin.nextInt() - 1;
            edges[i][1] = stdin.nextInt() - 1;
        }

        // -- Mark which edges survive to the end. Start by assuming all do. --
        boolean[] in = new boolean[numE];
        Arrays.fill(in, true);

        // -- Read the destruction order. Mark each destroyed edge as "not in". --
        //    `destroy[i]` is the edge index that gets removed at step i.
        int[] destroy = new int[numD];
        for (int i = 0; i < numD; i++) {
            destroy[i] = stdin.nextInt() - 1;
            in[destroy[i]] = false;
        }

        // -- Build the "final" graph (after all destructions) inside the DSU. --
        djset dj = new djset(numV);
        for (int i = 0; i < numE; i++) {
            if (in[i]) {
                dj.union(edges[i][0], edges[i][1]);
            }
        }

        // -- res[i] holds the connectivity AFTER i destructions have occurred. --
        //    res[numD] = final graph (already built above).
        //    res[0]    = initial graph (before anything is destroyed) -- printed first.
        long[] res = new long[numD + 1];
        res[numD] = dj.getConnectivity();

        // -- Walk the destruction list in reverse, re-adding each edge. --
        //    Re-adding edge `destroy[i]` corresponds to rewinding time from the
        //    state after (i+1) destructions back to the state after i destructions.
        for (int i = numD - 1; i >= 0; i--) {
            dj.union(edges[destroy[i]][0], edges[destroy[i]][1]);
            res[i] = dj.getConnectivity();
        }

        // -- Output: initial connectivity first, then after each destruction. --
        StringBuilder out = new StringBuilder();
        for (int i = 0; i <= numD; i++) {
            out.append(res[i]).append('\n');
        }
        System.out.print(out);
    }
}

// ============================================================================
// Disjoint Set (Union-Find) with two key modifications for this problem:
//   1) Component sizes are stored as `long` (to avoid overflow when squared).
//   2) The structure carries a running `sumSizeSq` total, updated in O(1) on
//      every successful union, so connectivity queries are O(1).
//
// Performance notes:
//   - `find` uses path compression.
//   - `union` here does NOT use union-by-size/rank; it always attaches v's
//     root under u's root. Combined with path compression this still gives
//     near-linear amortized behavior, which is fast enough for the given
//     constraints (n <= 1e5, m <= 3e5).
// ============================================================================
class djset {

    private int[] par;       // par[i] = parent of node i in the DSU forest.
    private long[] size;     // size[r] = number of nodes in the tree rooted at r.
                             //           Only meaningful when r is a root.
                             //           Non-roots are set to -1 after merging.
    private long sumSizeSq;  // Running sum of (component size)^2 across all
                             // current components. This IS the "connectivity".

    // Build a DSU of n singletons. Each node starts alone, so every component
    // has size 1 and contributes 1^2 = 1 to the sum. Total = n.
    public djset(int n) {
        par = new int[n];
        size = new long[n];
        for (int i = 0; i < n; i++) {
            par[i] = i;
            size[i] = 1;
        }
        sumSizeSq = n;
    }

    // Return the representative (root) of v's component, applying path
    // compression on the way back up.
    public int find(int v) {
        if (par[v] == v) return v;
        return par[v] = find(par[v]);
    }

    // Merge the components containing u and v.
    // Returns true if a merge happened, false if u and v were already together.
    public boolean union(int u, int v) {

        // Replace u and v with their root representatives.
        u = find(u);
        v = find(v);

        // Already in the same component -- nothing to do, total unchanged.
        if (u == v) return false;

        // Update the sum-of-squares total. Before this merge the two
        // components contributed size[u]^2 + size[v]^2; afterwards the merged
        // component contributes (size[u] + size[v])^2. The net change is:
        //     (size[u] + size[v])^2 - size[u]^2 - size[v]^2 = 2*size[u]*size[v]
        // (Equivalent simpler form, kept here for clarity with the algebra.)
        long newSize = size[u] + size[v];
        sumSizeSq += (newSize * newSize) - (size[u] * size[u] + size[v] * size[v]);

        // Attach v's tree under u, then update u's size. Mark v's old size
        // as -1 so that any accidental future read on a non-root fails loudly.
        par[v] = u;
        size[u] += size[v];
        size[v] = -1;
        return true;
    }

    // Current connectivity = sum over components of (component size)^2.
    public long getConnectivity() {
        return sumSizeSq;
    }
}
