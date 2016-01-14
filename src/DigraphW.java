
import java.util.ArrayList;
import java.util.*;


// You are not allowed to use anything else from the Java API.

/**
 * Adjacency list implementation of a weighted directed graph.
 * 
 * Mathematical definition of a weighted digraph: It is a quintuple G = (V,E,source,target,w), where:
 * - V is a finite set of vertices: without loss of generality V = { 0, ..., n-1 } for some non-negative integer n.
 * - E is a finite set of edges: without loss of generality E = { 0, ..., m-1 } for some non-negative integer m.
 * - source: E -> V is a function that maps each edge to its source vertex.
 * - target: E -> V is a function that maps each edge to its target vertex.
 * - w: E -> R^+ is a function that maps each edge to its weight, which is a non-negative real number.
 * 
 * Notes: - The above definition allows parallel edges. So does the implementation.
 * Definition: Two distinct edges e1 != e2 are parallel if source(e1)= source(e2) and target(e1) = target(e2).
 * 
 */
public class DigraphW
{
	/** Number of vertices. */
	private int n;
	/** Number of edges. */
	private int m;
	/** Adjacency list. */
	AdjListEntry[] edges;
	/** Heuristic distance estimates. */
	double[][] heurDist; // heurDist[u][v] = heuristic estimate of the distance from u to v. HINT: To be used in A*.

	/**
	 * Constructs a digraph with no edges.
	 * 
	 * @param n Number of vertices.
	 */
	public DigraphW(int n) {
		this.n = n;
		edges = new AdjListEntry[n];
		heurDist = new double[n][n];
	}

	/**
	 * NOTE: The heuristic distance estimate must always UNDERESTIMATE the real distance.
	 * 
	 * @param u Source vertex.
	 * @param v Target vertex.
	 * @param heurD Heuristic distance estimate for distance from u to v.
	 */
	public void setHeurDist(int u, int v, double heurD) {
		if (u < 0 || u >= n || v < 0 || v >= n || heurD < 0) {
			throw new IllegalArgumentException();
		}

		heurDist[u][v] = heurD;
	}

	/**
	 * O(1).
	 * 
	 * Adds an edge to the graph.
	 * 
	 * @param u Source vertex.
	 * @param v Target vertex.
	 * @param w Weight.
	 */
	public void addEdge(int u, int v, double w) {
		if (u < 0 || u >= n) {
			throw new IllegalArgumentException();
		}

		AdjListEntry newEntry = new AdjListEntry(n, v, w);
		newEntry.next = edges[u];
		edges[u] = newEntry;
	}

	/**
	 * @return Number of vertices of the graph.
	 */
	public int getN() {
		return n;
	}

	/**
	 * @return Number of edges of the graph.
	 */
	public int getM() {
		return m;
	}

	/**
	 * O(size of graph).
	 * 
	 * @param u Source vertex.
	 * @param v Target vertex.
	 * @return True iff there is an edge (u,v) (even if its weight is 0)
	 */
	public boolean isThereEdge(int u, int v) {
		if (u < 0 || u >= n) {
			throw new IllegalArgumentException();
		}
		if (v < 0 || v >= n) {
			throw new IllegalArgumentException();
		}

		AdjListEntry current = edges[u];
		while (current != null) {
			if (current.vtx == v) {
				return true;
			}
			current = current.next;
		}

		return false;
	}
	
	/**

	* Iterative Deepening A* (IDA*) ALGORITHM.

	* 

	* Precondition: visited & path are empty when the method is called.

	* 

	* @param source Source vertex.

	* @param dest Destination vertex.

	* @param visited Output argument: vertices visited (in the order they are visited)

	* @param path Output argument: Path from source to dest that witnesses the returned distance. If there is no path, it should be empty.

	* @return Possibly suboptimal distance from source to dest.

	*/

	public double iterativeDeepeningAStar(int source, int dest, ArrayList<Integer> visited, ArrayList<Integer> path) {

	if (source < 0 || source >= n || dest < 0 || dest >= n || visited == null || path == null || visited.size() > 0 || path.size() > 0) {

	throw new IllegalArgumentException();

	}


	double hRoot = heurDist[source][dest];
	double gRoot = 0;

	double fRoot = hRoot + gRoot;

	double bound = fRoot;


	/*

	while (bound < Double.POSITIVE_INFINITY)

	{

	double result = search(source, dest, visited, path, source, gRoot, bound);

	if (result != Double.POSITIVE_INFINITY){

	return result;

	}

	bound = result;

	}

	*/



	for(int depth = 0; depth <= Double.POSITIVE_INFINITY ; depth++)

	{

	double result = depthLimitedDFS(source, dest, visited, path, depth);

	if(result != bound && result != Double.POSITIVE_INFINITY){

	return result;

	}

	}


	return Double.POSITIVE_INFINITY;

	}



	/**

	* 
	* Iterative Deepening Depth-First ALGORITHM.
	* 
	* Precondition: visited & path are empty when the method is called.
	* 

	* HINT: A vertex is visited when it is removed from the priority queue.

	* 

	* @param source Source vertex.
	* @param dest Destination vertex.
	* @param visited Output argument: vertices visited (in the order they are visited)
	* @param path Output argument: Path from source to dest that witnesses the returned distance. If there is no path, it should be empty.
	* @param depth The depth to do the Depth First Search on each iteration
	* @return Possibly suboptimal distance from source to dest.

	*/

	public double IterativeDeepening(int source, int dest, ArrayList<Integer> visited, ArrayList<Integer> path) {

	if (source < 0 || source >= n || dest < 0 || dest >= n || visited == null || path == null || visited.size() > 0 || path.size() > 0) {

	throw new IllegalArgumentException();

	}


	for(int depth = 0; depth <= Double.POSITIVE_INFINITY ; depth++)

	{

	//int cutoff = 100;

	double result = depthLimitedDFS(source, dest, visited, path, depth);

	if(result != Double.POSITIVE_INFINITY){

	return result;

	}

	}

	return Double.POSITIVE_INFINITY;

	}


	
	/**
	 * 
	 * DIJKSTRA'S ALGORITHM
	 * 
	 * Precondition: visited & path are empty when the method is called.
	 * 
	 * HINT: A vertex is visited when it is removed from the priority queue.
	 * 
	 * Definition: The distance from u to v is the length of the shortest path from u to v.
	 * 
	 * @param source Source vertex.
	 * @param dest Destination vertex.
	 * @param visited Output argument: vertices visited (in the order they are visited)
	 * @param path Output argument: Path from source to dest that witnesses the returned distance. If there is no path, it should be empty.
	 * @return Distance from source to dest.
	 */
	public double shortestPath(int source, int dest, ArrayList<Integer> visited, ArrayList<Integer> path) {
		if (source < 0 || source >= n || dest < 0 || dest >= n || visited == null || path == null || visited.size() > 0 || path.size() > 0) {
			throw new IllegalArgumentException();
		}

		// Intialize the heap/priority queue, paths
		HeapEntry[] entries = new HeapEntry[n];
		int[] paths = new int[n];
		for (int i = 0; i < entries.length; i++) {
			entries[i] = new HeapEntry(i, Double.POSITIVE_INFINITY);
			paths[i] = -1;
		}
		entries[source] = new HeapEntry(source, 0);
		MinHeap PQueue = new MinHeap(entries);
		double destnode = Double.POSITIVE_INFINITY;
		
		// Iterate through each node
		while (PQueue.size() > 0) {
			HeapEntry x = PQueue.extractMin();
			visited.add(x.key);

			// return if found the destination
			if (x.key == dest) {
				int currentNode = x.key;
				while (paths[currentNode] != -1) {
					path.add(currentNode);
					currentNode = paths[currentNode];
				}
				Collections.reverse(path);
				if (currentNode != source) {
					path.clear();
				}
				destnode = x.value;
			} else {
				// update values and process the node
				AdjListEntry listEntry = edges[x.key];
				while (listEntry != null) {
					double newDist = x.value + listEntry.weight;
					HeapEntry node = entries[listEntry.vtx];

					// update the new distance
					if (newDist < node.value) {
						PQueue.update(node, newDist);
						paths[node.key] = x.key;
					}
					listEntry = listEntry.next;
				}
			}
		}
		if (destnode != Double.POSITIVE_INFINITY) {
			return destnode;
		}
		return Double.POSITIVE_INFINITY;
	}
	
	
	/**
	 * 
	 * UNIFORM COST SEARCH
	 * 
	 * Precondition: visited & path are empty when the method is called.
	 * 
	 * HINT: A vertex is visited when it is removed from the priority queue.
	 * 
	 * Definition: The distance from u to v is the length of the shortest path from u to v.
	 * 
	 * @param source Source vertex.
	 * @param dest Destination vertex.
	 * @param visited Output argument: vertices visited (in the order they are visited)
	 * @param path Output argument: Path from source to dest that witnesses the returned distance. If there is no path, it should be empty.
	 * @return Distance from source to dest.
	 */
	public double uniformCost(int source, int dest, ArrayList<Integer> visited, ArrayList<Integer> path) {
		if (source < 0 || source >= n || dest < 0 || dest >= n || visited == null || path == null || visited.size() > 0 || path.size() > 0) {
			throw new IllegalArgumentException();
		}

		// Intialize the heap/priority queue, paths
		HeapEntry[] entries = new HeapEntry[n];
		int[] paths = new int[n];
		for (int i = 0; i < entries.length; i++) {
			entries[i] = new HeapEntry(i, Double.POSITIVE_INFINITY);
			paths[i] = -1;
		}
		entries[source] = new HeapEntry(source, 0);
		MinHeap PQueue = new MinHeap(entries);

		// Iterate through each node
		while (PQueue.size() > 0) {
			HeapEntry x = PQueue.extractMin();
			visited.add(x.key);

			// return if found the destination
			if (x.key == dest) {
				int currentNode = x.key;
				while (paths[currentNode] != -1) {
					path.add(currentNode);
					currentNode = paths[currentNode];
				}
				if (currentNode != source) {
					path.clear();
				}
				Collections.reverse(path);
				return x.value;
			} else {
				// update values and process the node
				AdjListEntry listEntry = edges[x.key];
				while (listEntry != null) {
					double newDist = x.value + listEntry.weight;
					HeapEntry node = entries[listEntry.vtx];

					// update the new distance
					if (newDist < node.value) {
						PQueue.update(node, newDist);
						paths[node.key] = x.key;
					}
					listEntry = listEntry.next;
				}
			}
		}
		visited.clear();
		return Double.POSITIVE_INFINITY;
	}

	/**
	 * 
	 * GREEDY BEST-FIRST SEARCH.
	 * 
	 * Precondition: visited & path are empty when the method is called.
	 * 
	 * HINT: A vertex is visited when it is removed from the priority queue.
	 * 
	 * @param source Source vertex.
	 * @param dest Destination vertex.
	 * @param visited Output argument: vertices visited (in the order they are visited)
	 * @param path Output argument: Path from source to dest that witnesses the returned distance. If there is no path, it should be empty.
	 * @return Possibly suboptimal distance from source to dest.
	 */
	public double greedyBestFirst(int source, int dest, ArrayList<Integer> visited, ArrayList<Integer> path) {
		if (source < 0 || source >= n || dest < 0 || dest >= n || visited == null || path == null || visited.size() > 0 || path.size() > 0) {
			throw new IllegalArgumentException();
		}

		// Intialize the heap/priority queue, paths, distances
		double[] distances = new double[n];
		HeapEntry[] entries = new HeapEntry[n];
		int[] paths = new int[n];
		for (int i = 0; i < entries.length; i++) {
			entries[i] = new HeapEntry(i, Double.POSITIVE_INFINITY);
			distances[i] = Double.POSITIVE_INFINITY;
			paths[i] = -1;
		}
		entries[source] = new HeapEntry(source, 0);
		distances[source] = 0;
		MinHeap PQueue = new MinHeap(entries);

		// Iterate through each node
		while (PQueue.size() > 0) {
			HeapEntry x = PQueue.extractMin();
			visited.add(x.key);

			// return if found the destination
			if (x.key == dest) {
				int currentNode = x.key;
				while (paths[currentNode] >= 0) {
					path.add(currentNode);
					currentNode = paths[currentNode];
				}
				Collections.reverse(path);
				if (currentNode != source) {
					path.clear();
				}
				return x.value;
			} else {
				// update values and process the node
				AdjListEntry listEntry = edges[x.key];
				while (listEntry != null) {
					double newDist = distances[x.key] + listEntry.weight;
					HeapEntry node = entries[listEntry.vtx];

					// update the new distance
					if (heurDist[listEntry.vtx][dest] < node.value) {
						PQueue.update(node, heurDist[listEntry.vtx][dest]);
						paths[node.key] = x.key;
						distances[listEntry.vtx] = newDist;
					}
					listEntry = listEntry.next;
				}
			}
		}
		visited.clear();
		return Double.POSITIVE_INFINITY;
	}
	
	/**
	 * 
	 * A STAR ALGORITHM.
	 * 
	 * Precondition: visited & path are empty when the method is called.
	 * 
	 * HINT: A vertex is visited when it is removed from the priority queue.
	 * 
	 * @param source Source vertex.
	 * @param dest Destination vertex.
	 * @param visited Output argument: vertices visited (in the order they are visited)
	 * @param path Output argument: Path from source to dest that witnesses the returned distance. If there is no path, it should be empty.
	 * @return Possibly suboptimal distance from source to dest.
	 */
	public double shortestPathHeur(int source, int dest, ArrayList<Integer> visited, ArrayList<Integer> path) {
		if (source < 0 || source >= n || dest < 0 || dest >= n || visited == null || path == null || visited.size() > 0 || path.size() > 0) {
			throw new IllegalArgumentException();
		}

		// Intialize the heap/priority queue, paths, distances
		double[] distances = new double[n];
		HeapEntry[] entries = new HeapEntry[n];
		int[] paths = new int[n];
		for (int i = 0; i < entries.length; i++) {
			entries[i] = new HeapEntry(i, Double.POSITIVE_INFINITY);
			distances[i] = Double.POSITIVE_INFINITY;
			paths[i] = -1;
		}
		entries[source] = new HeapEntry(source, 0);
		distances[source] = 0;
		MinHeap PQueue = new MinHeap(entries);
		
		// Iterate through each node
		while (PQueue.size() > 0) {
			HeapEntry x = PQueue.extractMin();
			visited.add(x.key);

			// return if found the destination
			if (x.key == dest) {
				int currentNode = x.key;
				while (paths[currentNode] >= 0) {
					path.add(currentNode);
					currentNode = paths[currentNode];
				}
				Collections.reverse(path);
				if (currentNode != source) {
					path.clear();
				}
				return x.value;
			} else {
				// update values and process the node
				AdjListEntry listEntry = edges[x.key];
				while (listEntry != null) {
					double newDist = distances[x.key] + listEntry.weight;
					HeapEntry node = entries[listEntry.vtx];

					// update the new distance
					if (newDist + heurDist[listEntry.vtx][dest] < node.value) {
						PQueue.update(node, newDist + heurDist[listEntry.vtx][dest]);
						paths[node.key] = x.key;
						distances[listEntry.vtx] = newDist;
					}
					listEntry = listEntry.next;
				}
			}
		}
		
		
		visited.clear();
		return Double.POSITIVE_INFINITY;
	}

	/**
	 * A DFS reachability algorithm.
	 * 
	 * @param source Source vertex.
	 * @param dest Destination vertex.
	 * @param visited Output argument: vertices visited (in the order they are visited)
	 * @param path Output argument: A path from source to dest. If there is no path, it should be empty.
	 * @return Length of returned path.
	 */
	public double DFS(int source, int dest, ArrayList<Integer> visited, ArrayList<Integer> path) {
		if (source < 0 || source >= n || dest < 0 || dest >= n || visited == null || path == null || visited.size() > 0 || path.size() > 0) {
			throw new IllegalArgumentException();
		}

		// Set of vertices that have already been processed.
		boolean[] closedVertices = new boolean[n];
		// Vertices in stack.
		boolean[] isInStack = new boolean[n];
		// distances
		double[] dist = new double[n];
		for (int i = 0; i < n; i++) {
			dist[i] = Double.POSITIVE_INFINITY;
		}
		dist[source] = 0;
		// Back pointers.
		int[] previous = new int[n];
		for (int i = 0; i < n; i++) {
			previous[i] = -1;
		}

		java.util.Stack<Integer> stack = new java.util.Stack<Integer>();
		stack.add(source);
		isInStack[source] = true;

		while (!stack.isEmpty()) {
			int u = stack.pop();
			isInStack[u] = false;
			closedVertices[u] = true;
			visited.add(u);
			// Destination reached?
			if (u == dest) {
				break;
			}

			AdjListEntry succ = edges[u]; // Edges emanating from u.
			while (succ != null) {
				int v = succ.vtx;

				if (!closedVertices[v]) {
					// Vertex v has not been processed yet.

					// Calculate new distance estimate to v (from source).
					double edgeWeight = succ.weight;
					double newDist = dist[u] + edgeWeight;

					if (!isInStack[v]) {
						stack.push(v);
						isInStack[v] = true;
					}

					if (newDist < dist[v]) {
						// Better distance estimate.
						// Update distance estimate for vertex v.
						dist[v] = newDist;
						previous[v] = u;
					}
				}

				succ = succ.next;
			} // successors loop

		} // main loop

		if (dist[dest] == Double.POSITIVE_INFINITY) {
			return dist[dest];
		}

		// Find path to destination from back pointers.
		ArrayList<Integer> reversePath = new ArrayList<Integer>();
		int current = dest;
		while (current != source) {
			reversePath.add(current);
			current = previous[current];
		}
		path.add(source);
		for (int i = reversePath.size() - 1; i >= 0; i--) {
			path.add(reversePath.remove(i));
		}

		return dist[dest];
	}

	/**
	 * A BFS reachability algorithm.
	 * 
	 * @param source Source vertex.
	 * @param dest Destination vertex.
	 * @param visited Output argument: vertices visited (in the order they are visited)
	 * @param path Output argument: A path from source to dest. If there is no path, it should be empty.
	 * @return Length of returned path.
	 */
	public double BFS(int source, int dest, ArrayList<Integer> visited, ArrayList<Integer> path) {
		if (source < 0 || source >= n || dest < 0 || dest >= n || visited == null || path == null || visited.size() > 0 || path.size() > 0) {
			throw new IllegalArgumentException();
		}

		// Set of vertices that have already been processed.
		boolean[] closedVertices = new boolean[n];
		
		// Vertices in stack.
		boolean[] isInQueue = new boolean[n];
		
		// distances to source
		double[] dist = new double[n];
		for (int i = 0; i < n; i++) {
			dist[i] = Double.POSITIVE_INFINITY;
		}
		dist[source] = 0;
		
		// Back pointers.
		int[] previous = new int[n];
		for (int i = 0; i < n; i++) {
			previous[i] = -1;
		}
		
		java.util.Queue<Integer> queue = new java.util.LinkedList<Integer>();
		queue.add(source);
		isInQueue[source] = true;

		
		while (!queue.isEmpty()) {
			int u = queue.remove();
			isInQueue[u] = false;
			closedVertices[u] = true;
			visited.add(u);
			// Destination reached?
			if (u == dest) {
				break;
			}

			AdjListEntry succ = edges[u]; // Edges emanating from u.
			while (succ != null) {
				int v = succ.vtx;

				if (!closedVertices[v]) {
					// Vertex v has not been processed yet.

					// Calculate new distance estimate to v (from source).
					double edgeWeight = succ.weight;
					double newDist = dist[u] + edgeWeight;

					if (!isInQueue[v]) {
						queue.add(v);
						isInQueue[v] = true;
					}

					if (newDist < dist[v]) {
						// Better distance estimate.
						// Update distance estimate for vertex v.
						dist[v] = newDist;
						previous[v] = u;
					}
				}

				succ = succ.next;
			} // successors loop

		} // main loop

		if (dist[dest] == Double.POSITIVE_INFINITY) {
			return dist[dest];
		}

		// Find path to destination from back pointers.
		ArrayList<Integer> reversePath = new ArrayList<Integer>();
		int current = dest;
		while (current != source) {
			reversePath.add(current);
			current = previous[current];
		}
		path.add(source);
		for (int i = reversePath.size() - 1; i >= 0; i--) {
			path.add(reversePath.remove(i));
		}

		return dist[dest];
	}
	
	
	
	/**
	 * Bidirectional search.
	 * 
	 * @param source Source vertex.
	 * @param dest Destination vertex.
	 * @param visited Output argument: vertices visited (in the order they are visited)
	 * @param path Output argument: A path from source to dest. If there is no path, it should be empty.
	 * @return Length of returned path.
	 */
	public double bidirectional(int source, int dest, ArrayList<Integer> visited, ArrayList<Integer> path) {
		if (source < 0 || source >= n || dest < 0 || dest >= n || visited == null || path == null || visited.size() > 0 || path.size() > 0) {
			throw new IllegalArgumentException();
		}

		// Set of vertices that have already been processed.
		boolean[] closedVertices = new boolean[n];
		boolean[] closedVerticesGoal = new boolean[n];
		// Vertices in stack.
		boolean[] isInQueue = new boolean[n];
		boolean[] isInQueueGoal = new boolean[n];
		// distances to source
		double[] dist = new double[n];
		for (int i = 0; i < n; i++) {
			dist[i] = Double.POSITIVE_INFINITY;
		}
		dist[source] = 0;
		
		// distances to goal
		double[] distFromGoal = new double[n];
		for (int i = 0; i < n; i++) {
			distFromGoal[i] = Double.POSITIVE_INFINITY;
		}
		distFromGoal[dest] = 0;
		
		// Back pointers.
		int[] previous = new int[n];
		for (int i = 0; i < n; i++) {
			previous[i] = -1;
		}
		
		//Forward pointers.
		int[] forward = new int[n];
		for (int i = 0; i < n; i++) {
			forward[i] = -1;
		}

		java.util.Queue<Integer> queue = new java.util.LinkedList<Integer>();
		queue.add(source);
		isInQueue[source] = true;

		java.util.Queue<Integer> queueGoal = new java.util.LinkedList<Integer>();
		queueGoal.add(dest);
		isInQueueGoal[dest] = true;
		
		int foundIntersection = -1;
		while (!queue.isEmpty() && !queueGoal.isEmpty()) {
			int u = queue.remove();
			isInQueue[u] = false;
			closedVertices[u] = true;
			visited.add(u);
			// Destination reached?
			if (u == dest || queueGoal.contains(u)) {
				foundIntersection = u;
				break;
			}

			AdjListEntry succ = edges[u]; // Edges emanating from u.
			while (succ != null) {
				int v = succ.vtx;

				if (!closedVertices[v]) {
					// Vertex v has not been processed yet.

					// Calculate new distance estimate to v (from source).
					double edgeWeight = succ.weight;
					double newDist = dist[u] + edgeWeight;

					if (!isInQueue[v]) {
						queue.add(v);
						isInQueue[v] = true;
					}

					if (newDist < dist[v]) {
						// Better distance estimate.
						// Update distance estimate for vertex v.
						dist[v] = newDist;
						previous[v] = u;
					}
				}

				succ = succ.next;
			} // successors loop
			
			// now searching from the goal
			int v = queueGoal.remove();
			isInQueueGoal[v] = false;
			closedVerticesGoal[v] = true;
			visited.add(v);
			// Destination reached?
			if (v == source || queue.contains(v)) {
				foundIntersection = v;
				break;
			}

			succ = edges[v]; // Edges emanating from v.
			while (succ != null) {
				int w = succ.vtx;

				if (!closedVerticesGoal[w]) {
					// Vertex w has not been processed yet.

					// Calculate new distance estimate to w (from goal).
					double edgeWeight = succ.weight;
					double newDist = distFromGoal[v] + edgeWeight;

					if (!isInQueueGoal[w]) {
						queueGoal.add(w);
						isInQueueGoal[w] = true;
					}

					if (newDist < distFromGoal[w]) {
						// Better distance estimate.
						// Update distance estimate for vertex w.
						distFromGoal[w] = newDist;
						forward[w] = v;
					}
				}

				succ = succ.next;
			} // successors loop

		} // main loop

		if (foundIntersection == -1) {
			return Double.POSITIVE_INFINITY;
		}

		// Find path to destination from back pointers.
		ArrayList<Integer> reversePath = new ArrayList<Integer>();
		int current = foundIntersection;
		while (current != source) {
			reversePath.add(current);
			current = previous[current];
		}
		path.add(source);
		for (int i = reversePath.size() - 1; i >= 0; i--) {
			path.add(reversePath.remove(i));
		}
		
		current = forward[foundIntersection];
		while (current != dest) {
			path.add(current);
			current = forward[current];
		}
		path.add(dest);

		return dist[foundIntersection] + distFromGoal[foundIntersection];
	}
	
	/**
	 * A depth-first limited reachability algorithm.
	 * 
	 * @param source Source vertex.
	 * @param dest Destination vertex.
	 * @param visited Output argument: vertices visited (in the order they are visited)
	 * @param path Output argument: A path from source to dest. If there is no path, it should be empty.
	 * @return Length of returned path.
	 */
	public double depthLimitedDFS(int source, int dest, ArrayList<Integer> visited, ArrayList<Integer> path, int depth) {
		if (source < 0 || source >= n || dest < 0 || dest >= n || visited == null || path == null || visited.size() > 0 || path.size() > 0) {
			throw new IllegalArgumentException();
		}

		// Set of vertices that have already been processed.
		boolean[] closedVertices = new boolean[n];
		// Vertices in stack.
		boolean[] isInStack = new boolean[n];
		// distances
		double[] dist = new double[n];
		for (int i = 0; i < n; i++) {
			dist[i] = Double.POSITIVE_INFINITY;
		}
		dist[source] = 0;
		// Back pointers.
		int[] previous = new int[n];
		for (int i = 0; i < n; i++) {
			previous[i] = -1;
		}

		java.util.Stack<IntPair> stack = new java.util.Stack<IntPair>();
		stack.add(new IntPair(source, 0));
		isInStack[source] = true;

		while (!stack.isEmpty()) {
			IntPair p = stack.pop();
			int u = p.x;
			isInStack[u] = false;
			closedVertices[u] = true;
			visited.add(u);
			// Destination reached?
			if (u == dest) {
				break;
			}
			
			if (p.y == depth) {
				continue;
			}

			AdjListEntry succ = edges[u]; // Edges emanating from u.
			while (succ != null) {
				int v = succ.vtx;

				if (!closedVertices[v]) {
					// Vertex v has not been processed yet.

					// Calculate new distance estimate to v (from source).
					double edgeWeight = succ.weight;
					double newDist = dist[u] + edgeWeight;

					if (!isInStack[v]) {
						stack.push(new IntPair(v, p.y+1));
						isInStack[v] = true;
					}

					if (newDist < dist[v]) {
						// Better distance estimate.
						// Update distance estimate for vertex v.
						dist[v] = newDist;
						previous[v] = u;
					}
				}

				succ = succ.next;
			} // successors loop

		} // main loop

		if (dist[dest] == Double.POSITIVE_INFINITY) {
			visited.clear();
			return dist[dest];
		}

		// Find path to destination from back pointers.
		ArrayList<Integer> reversePath = new ArrayList<Integer>();
		int current = dest;
		while (current != source) {
			reversePath.add(current);
			current = previous[current];
		}
		path.add(source);
		for (int i = reversePath.size() - 1; i >= 0; i--) {
			path.add(reversePath.remove(i));
		}

		return dist[dest];
	}
	@Override
	public String toString() {
		String str = "n = " + n + "\n";
		for (int k = 0; k < n; k++) {
			str += "vtx " + k + ": [";
			AdjListEntry succ = edges[k];
			if (succ != null) {
				str += succ.vtx + ":" + succ.weight;
				succ = succ.next;
			}
			while (succ != null) {
				str += ", " + succ.vtx + ":" + succ.weight;
				succ = succ.next;
			}
			str += "]\n";
		}
		return str;
	}

}
