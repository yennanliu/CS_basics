//---------------------------------------------------------------
// GRAPH (adjacency list)
//---------------------------------------------------------------
//
// A graph is a set of vertices plus a set of edges. Unlike a tree it
// may contain cycles and has no root.
//
//     0 --- 1 --- 3
//     |    /|     |
//     2 --/ |     4 --- 5 --- 6
//
// TWO WAYS TO STORE IT
//
//   adjacency LIST    { 0: [1, 2], 1: [0, 2, 3], ... }
//     space O(V + E); listing a vertex's neighbours is O(deg(v))
//     -> the default for SPARSE graphs, which is nearly every
//        interview graph
//
//   adjacency MATRIX  matrix[i][j] === 1 when the edge i-j exists
//     space O(V^2); edge lookup is O(1), neighbour listing is O(V)
//     -> only worth it for DENSE graphs or O(1) edge tests
//
// Time  : addVertex / addEdge  O(1)
//         hasEdge              O(deg(v))
//         bfs / dfs            O(V + E)
// Space : O(V + E)

class Graph {
  constructor(directed = false) {
    this.directed = directed;
    this.adjacentList = {};
  }

  get numberOfNodes() {
    return Object.keys(this.adjacentList).length;
  }

  get numberOfEdges() {
    const total = Object.values(this.adjacentList).reduce((n, list) => n + list.length, 0);
    return this.directed ? total : total / 2;
  }

  // An isolated vertex is NOT the same as an edge.
  addVertex(node) {
    if (!this.adjacentList[node]) this.adjacentList[node] = [];
    return this;
  }

  addEdge(node1, node2) {
    this.addVertex(node1);
    this.addVertex(node2);
    this.adjacentList[node1].push(node2);
    if (!this.directed) this.adjacentList[node2].push(node1); // record BOTH directions
    return this;
  }

  neighbours(node) {
    return this.adjacentList[node] || [];
  }

  hasEdge(node1, node2) {
    return this.neighbours(node1).includes(node2);
  }

  // Breadth-first: nearest vertices first, using a QUEUE.
  bfs(start) {
    if (!this.adjacentList[start]) return [];
    const visited = new Set([start]);
    const order = [];
    const queue = [start];

    while (queue.length > 0) {
      const node = queue.shift();
      order.push(node);
      for (const neighbour of this.adjacentList[node]) {
        if (visited.has(neighbour)) continue;
        visited.add(neighbour); // mark on ENQUEUE, or a vertex can be queued twice
        queue.push(neighbour);
      }
    }
    return order;
  }

  // Depth-first: follow one branch to the end, using recursion.
  dfs(start) {
    const visited = new Set();
    const order = [];

    const walk = (node) => {
      visited.add(node);
      order.push(node);
      for (const neighbour of this.neighbours(node)) {
        if (!visited.has(neighbour)) walk(neighbour);
      }
    };

    if (this.adjacentList[start]) walk(start);
    return order;
  }

  // Fewest EDGES from start to end (BFS), or null if unreachable.
  // Only correct on an UNWEIGHTED graph -- with weights you need Dijkstra.
  shortestPath(start, end) {
    if (!this.adjacentList[start] || !this.adjacentList[end]) return null;
    if (start === end) return [start];

    const previous = new Map([[start, null]]);
    const queue = [start];

    while (queue.length > 0) {
      const node = queue.shift();
      for (const neighbour of this.adjacentList[node]) {
        if (previous.has(neighbour)) continue;
        previous.set(neighbour, node);
        if (neighbour === end) {
          const path = [end]; // rebuild the path backwards
          while (previous.get(path[path.length - 1]) !== null) {
            path.push(previous.get(path[path.length - 1]));
          }
          return path.reverse();
        }
        queue.push(neighbour);
      }
    }
    return null;
  }

  showConnections() {
    return Object.keys(this.adjacentList)
      .sort()
      .map((node) => `${node}-->${this.adjacentList[node].join(' ')}`)
      .join('\n');
  }
}

// demo
//    0 --- 1 --- 3
//    |    /|     |
//    2 --/ |     4 --- 5 --- 6
const graph = new Graph();
[['3', '1'], ['3', '4'], ['4', '2'], ['4', '5'],
 ['1', '2'], ['1', '0'], ['0', '2'], ['6', '5']].forEach(([a, b]) => graph.addEdge(a, b));

console.assert(graph.numberOfNodes === 7, 'seven vertices');
console.assert(graph.numberOfEdges === 8, 'eight edges');
console.assert(graph.hasEdge('3', '1') && graph.hasEdge('1', '3'), 'undirected: both ways');
console.assert(!graph.hasEdge('0', '6'), 'no direct edge');
// copy before sorting -- .sort() mutates, and neighbours() returns the live array
console.assert([...graph.neighbours('1')].sort().join(',') === '0,2,3');

console.assert(graph.bfs('0').join(',') === '0,1,2,3,4,5,6', 'BFS from 0');
console.assert(graph.dfs('0').join(',') === '0,1,3,4,2,5,6', 'DFS from 0');

console.assert(graph.shortestPath('0', '6').join(',') === '0,2,4,5,6', 'fewest edges');
console.assert(graph.shortestPath('0', '0').join(',') === '0', 'path to self');
graph.addVertex('island');
console.assert(graph.shortestPath('0', 'island') === null, 'unreachable -> null');

// a DIRECTED edge goes one way only
const digraph = new Graph(true);
digraph.addEdge('a', 'b');
console.assert(digraph.hasEdge('a', 'b') && !digraph.hasEdge('b', 'a'), 'directed');
console.assert(digraph.numberOfEdges === 1);

console.log(graph.showConnections());
console.log('Success.');

module.exports = { Graph };
