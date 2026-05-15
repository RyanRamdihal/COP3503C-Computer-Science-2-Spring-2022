// 3/12/2022
// Solution to COP 3503 Program #4: Maze Magic

import java.util.*;

public class maze {
	
	// For regular grid movement.
	final public static int[] DX = {-1,0,0,1};
	final public static int[] DY = {0,-1,1,0};
	
	// For grid storage.
	public static int r;
	public static int c;
	public static char[][] grid;
	
	// Stores where each occurrence of each letter is.
	public static HashSet<Integer>[] letterLocs;
	
	public static void main(String[] args) {
	
		// Get grid size.
		Scanner stdin = new Scanner(System.in);
		r = stdin.nextInt();
		c = stdin.nextInt();
		grid = new char[r][];
		
		// Read in the grid.
		for (int i=0; i<r; i++)
			grid[i] = stdin.next().toCharArray();
		
		// letterLocs[i] will store all locations of letter i.
		letterLocs = new HashSet[26];
		for (int i=0; i<26; i++)
			letterLocs[i] = new HashSet<Integer>();
		
		// For all letter characters, store in the list for that letter its location.
		for (int i=0; i<r; i++)
			for (int j=0; j<c; j++)
				if (Character.isLetter(grid[i][j]))
					letterLocs[grid[i][j]-'A'].add(c*i + j);
				
		// For BFS.
		int start = find('*');
		int end = find('$');
		
		// Ta da!
		System.out.println(bfs(start, end));
	}
	
	// Returns the shortest distance from start to end in the maze, or -1 if no such path exists.
	public static int bfs(int start, int end) {
		
		// Queue for BFS.
		LinkedList<Integer> q = new LinkedList<Integer>();
		q.offer(start);
		
		// Store distances here.
		int[] dist = new int[r*c];
		Arrays.fill(dist, -1);
		dist[start] = 0;
		
		// Keep track of if we've enqueued all of a letter.
		boolean[] usedLetter = new boolean[26];
		
		// Run BFS.
		while (q.size() > 0) {
			
			// Get next item.
			int cur = q.poll();
			char ch = grid[cur/c][cur%c];
			
			// Found it!
			if (cur == end) return dist[cur];
			
			// Go to neighbors. We can always do this.
			for (int i=0; i<DX.length; i++) {
				
				// Next location.
				int nX = cur/c + DX[i];
				int nY = cur%c + DY[i];
				
				// Skip out of bounds, previously enqueued locations, and illegal locations.
				if (!inbounds(nX, nY)) continue;
				if (dist[nX*c+nY] != -1) continue;
				if (grid[nX][nY] == '!') continue;
				
				// Add to queue, update distance.
				q.offer(nX*c+nY);
				dist[nX*c+nY] = dist[cur] + 1;
			}
			
			// Only if this is a letter AND we've never enqueued this letter, we go here.
			if (Character.isLetter(ch) && !usedLetter[ch-'A']) {
				
				// Mark it!
				usedLetter[ch-'A'] = true;
				
				// Go to each spot with this letter and add these to the queue also, if they haven't been before.
				for (Integer next: letterLocs[ch-'A']) {
					if (dist[next] != -1) continue;
					dist[next] = dist[cur] + 1;
					q.offer(next);
				}
			}
		}
		
		// Never found it.
		return -1;
	}
	
	// Returns the first location of item in grid, or -1 if it's not there.
	public static int find(char item) {
	
		// Look everywhere, returning if we find item.
		for (int i=0; i<r; i++)
			for (int j=0; j<c; j++)
				if (grid[i][j] == item)
					return i*c + j;
					
		// Never found it.
		return -1;
	}
	
	// Returns true iff (x, y) is inbounds in grid.
	public static boolean inbounds(int x, int y) {
		return x >= 0 && x < r && y >= 0 && y < c;
	}
}
