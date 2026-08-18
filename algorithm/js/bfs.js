//---------------------------------------------------------------
// BFS (Breadth-First Search)
//---------------------------------------------------------------
//
// Visit everything at distance 1 from the start, then everything at
// distance 2, and so on -- level by level.
//
//        9          level 0 : 9
//      /   \        level 1 : 4, 20
//     4     20      level 2 : 1, 6, 15, 170
//    / \   /  \
//   1   6 15  170   BFS order: 9, 4, 20, 1, 6, 15, 170
//
// A QUEUE is what makes it breadth-first: the first node discovered is
// the first expanded (FIFO). Swap the queue for a stack and you get
// DFS -- see dfs.js.
//
// WHY IT MATTERS: on an UNWEIGHTED graph, the first time BFS reaches a
// node it has done so along a path with the fewest edges. That is the
// shortest path, for free. (With weights you need Dijkstra.)
//
// Time  : O(V + E)  -- every vertex enqueued once, every edge examined once
// Space : O(V)      -- the queue can hold a whole level

class Node {
  constructor(value) {
    this.left = null;
    this.right = null;
    this.value = value;
  }
}

// Build a small BST so the walks below have something to run on.
function buildTree(values) {
  let root = null;
  const insert = (node, value) => {
    if (node === null) return new Node(value);
    if (value < node.value) node.left = insert(node.left, value);
    else if (value > node.value) node.right = insert(node.right, value);
    return node;
  };
  values.forEach((value) => {
    root = insert(root, value);
  });
  return root;
}

//---------------------------------------------------------------
// 1) BFS on a binary tree
//---------------------------------------------------------------

// Iterative: the queue holds the frontier.
function bfs(root) {
  if (root === null) return [];
  const values = [];
  const queue = [root];

  while (queue.length > 0) {
    const node = queue.shift(); // dequeue the OLDEST -> breadth-first
    values.push(node.value);
    if (node.left) queue.push(node.left);
    if (node.right) queue.push(node.right);
  }
  return values;
}

// Recursive: the same algorithm, with the queue threaded through the call.
// This is NOT the usual reason to recurse -- the queue still does the work --
// but it shows the loop and the recursion are the same thing.
function bfsRecursive(queue, values = []) {
  if (queue.length === 0) return values;

  const node = queue.shift();
  values.push(node.value);
  if (node.left) queue.push(node.left);
  if (node.right) queue.push(node.right);

  return bfsRecursive(queue, values);
}

// One array PER LEVEL -- the LC 102 "level order traversal" shape.
// The trick is to snapshot queue.length before draining: that count is
// exactly how many nodes are on the current level.
function bfsByLevel(root) {
  if (root === null) return [];
  const levels = [];
  let queue = [root];

  while (queue.length > 0) {
    const level = [];
    const next = [];
    for (const node of queue) {
      level.push(node.value);
      if (node.left) next.push(node.left);
      if (node.right) next.push(node.right);
    }
    levels.push(level);
    queue = next;
  }
  return levels;
}

//---------------------------------------------------------------
// 2) BFS on a graph
//---------------------------------------------------------------
//
// One thing changes versus a tree: a graph can have CYCLES, so we must
// remember what we have seen or the search never terminates.
//
// Mark a node visited when it is ENQUEUED, not when it is dequeued --
// otherwise a node reachable from two nodes on the same level gets
// queued twice.

function bfsGraph(adjacency, start) {
  if (!adjacency[start]) return [];
  const visited = new Set([start]);
  const order = [];
  const queue = [start];

  while (queue.length > 0) {
    const node = queue.shift();
    order.push(node);
    for (const neighbour of adjacency[node]) {
      if (visited.has(neighbour)) continue;
      visited.add(neighbour);
      queue.push(neighbour);
    }
  }
  return order;
}

// Fewest EDGES from start to end, or null if unreachable.
// `previous` records how each node was first reached, which is what lets
// us rebuild the path backwards once we arrive.
function shortestPath(adjacency, start, end) {
  if (!adjacency[start] || !adjacency[end]) return null;
  if (start === end) return [start];

  const previous = new Map([[start, null]]);
  const queue = [start];

  while (queue.length > 0) {
    const node = queue.shift();
    for (const neighbour of adjacency[node]) {
      if (previous.has(neighbour)) continue;
      previous.set(neighbour, node);
      if (neighbour === end) {
        const path = [end];
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

// demo
//        9
//      /   \
//     4     20
//    / \   /  \
//   1   6 15  170
const tree = buildTree([9, 4, 6, 20, 170, 15, 1]);

console.assert(bfs(tree).join(',') === '9,4,20,1,6,15,170', 'level by level');
console.assert(bfsRecursive([tree]).join(',') === bfs(tree).join(','),
  'the recursive form agrees with the loop');
console.assert(
  JSON.stringify(bfsByLevel(tree)) === JSON.stringify([[9], [4, 20], [1, 6, 15, 170]]),
  'one array per level'
);
console.assert(bfs(null).length === 0, 'empty tree');

//    0 --- 1 --- 3
//    |    /|     |
//    2 --/ |     4 --- 5 --- 6
const adjacency = {
  0: ['1', '2'],
  1: ['0', '2', '3'],
  2: ['0', '1'],
  3: ['1', '4'],
  4: ['3', '5'],
  5: ['4', '6'],
  6: ['5'],
  island: [],
};

console.assert(bfsGraph(adjacency, '0').join(',') === '0,1,2,3,4,5,6', 'BFS over a graph');
console.assert(shortestPath(adjacency, '0', '6').join(',') === '0,1,3,4,5,6', 'fewest edges');
console.assert(shortestPath(adjacency, '0', '0').join(',') === '0', 'path to self');
console.assert(shortestPath(adjacency, '0', 'island') === null, 'unreachable -> null');

console.log('BFS       ', bfs(tree).join(' '));
console.log('BFS levels', JSON.stringify(bfsByLevel(tree)));
console.log('Success.');

module.exports = { bfs, bfsRecursive, bfsByLevel, bfsGraph, shortestPath, buildTree, Node };
