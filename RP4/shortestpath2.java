//Ryan Ramdihal, COP 3503 RP4, Kattis ShortestPath2
import java.util.*;

public class shortestpath2 {

    public static void main(String[] args) {
        Scanner stdin= new Scanner(System.in);

        while(stdin.hasNextInt()) {
            int nodesLength = stdin.nextInt();
            int edgesLength = stdin.nextInt();
            int queLength = stdin.nextInt();
            int startIndex = stdin.nextInt();

            if(nodesLength == 0 && edgesLength == 0 && queLength == 0 && startIndex == 0){
                break;
            }

            ArrayList<Edge>[] neighbors = new ArrayList[nodesLength];

            for(int i = 0; i < nodesLength; i++) {
                neighbors[i] = new ArrayList<>();
            }

            for(int i = 0; i < edgesLength; i++) {
                neighbors[stdin.nextInt()].add(new Edge(stdin.nextInt(), stdin.nextInt(), stdin.nextInt(), stdin.nextInt()));
                // add to get graph
            }


            Dijkstra dijkstra = new Dijkstra(neighbors, nodesLength, startIndex);

            for(int query = 0; query < queLength; query++) {
                int length = dijkstra.getDistance(stdin.nextInt());
                if(length != Integer.MAX_VALUE) {
                    System.out.println(length);
                } else {
                    System.out.println("Impossible");
                }
            }

            System.out.println();
        }
        stdin.close();
    }


    public static class Dijkstra {
        public ArrayList<Edge>[] neighbors;
        public int[] distances;
        public int[] parents;
        public int nodes;
        public int startIndex;

        Dijkstra(ArrayList<Edge>[] neighbors, int numNodes, int startIndex) {
            this.nodes = numNodes;
            this.neighbors = neighbors;
            this.distances = new int[nodes];
            this.parents = new int[nodes];
            this.startIndex = startIndex;
            dijkstra(startIndex);
        }

        private void dijkstra(int start) {
            PriorityQueue<Integer> unvisitedNodes = new PriorityQueue<>(Comparator.comparingInt(index -> distances[index]));
            
            // set to infinity
            for(int i = 0; i < nodes; i++) {
                distances[i] = Integer.MAX_VALUE; 
                parents[i] = -1;
            }

            distances[start] = 0; 
            unvisitedNodes.add(start);

            while(!unvisitedNodes.isEmpty()) {
                int node = unvisitedNodes.poll();
                List<Edge> neighbors = this.neighbors[node];

                // Loop through all edgeLookup out of the current node
                for(Edge neighbor : neighbors) {

                    if(neighbor.getNextAvaliableTime(distances[node]) == Integer.MAX_VALUE) continue;
                    int distance = Math.max(distances[node], neighbor.getNextAvaliableTime(distances[node])) + neighbor.cost;

                    // shorter distance
                    if(distance < distances[neighbor.end]) {
                        distances[neighbor.end] = distance;
                        parents[neighbor.end] = node;
                        unvisitedNodes.add(neighbor.end);
                    }
                }
            }
        }

        //distance from start to another node
        public int getDistance(int toNode) {
            return distances[toNode];
        }

    
        public List<Integer> getPath(int toNode) {
            List<Integer> nodes = new ArrayList<>();

            int currentNode = toNode;

            while(true) {
                nodes.add(currentNode);
                int parent = parents[currentNode];

                // path found               //start to end
                if(parent == -1 && currentNode == startIndex) {
                    break;
                }

                // if no path exists      
                if(parent == -1) {
                    return null;
                }

                currentNode = parent;
            }


            return nodes;
        }
    }
// saves locations
    static class Edge {    
        int end;
        int startT; 
        int period; 
        int cost; 

        Edge(int end, int startTime, int period, int cost) {
            this.end = end;
            this.startT = startTime;
            this.period = period;
            this.cost = cost;
        }

        int getNextAvaliableTime(int current) {
            if(current <= startT) return startT;
            if(period != 0) return (int)Math.ceil((current - startT) / (double) period) * period + startT;
            return Integer.MAX_VALUE;
        }
    }
}
