//---------------------------------------------------------------
// DFS (Depth-First Search)
//---------------------------------------------------------------
//
// Follow one branch as deep as it goes, then back up and take the next
// one. Where BFS fans out level by level (see bfs.js), DFS commits.
//
//        9
//      /   \
//     4     20
//    / \   /  \
//   1   6 15  170
//
// THE THREE ORDERS -- the only thing that changes is WHEN the node
// itself is recorded relative to its two subtrees:
//
//   pre-order    node, left, right     9 4 1 6 20 15 170
//                -> copy / serialise a tree, top-down decisions
//   in-order     left, node, right     1 4 6 9 15 20 170
//                -> on a BST this is SORTED order
//   post-order   left, right, node     1 6 4 15 170 20 9
//                -> children before parent: free a tree, evaluate an
//                   expression, or rebuild one from its traversal
//
// A STACK is what makes it depth-first -- usually the CALL stack via
// recursion, but the iterative versions below make it explicit. Swap
// the stack for a queue and you get BFS.
//
// Time  : O(V + E)  -- every vertex visited once, every edge examined once
// Space : O(H) for a tree (H = height; O(N) if it degrades to a chain),
//         O(V) for a graph (the visited set)

class Node {
  constructor(value) {
    this.left = null;
    this.right = null;
    this.value = value;
  }
}

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
// 1) Recursive DFS on a binary tree
//---------------------------------------------------------------

function preOrder(node, values = []) {
  if (node === null) return values;
  values.push(node.value); // visit BEFORE the subtrees
  preOrder(node.left, values);
  preOrder(node.right, values);
  return values;
}

function inOrder(node, values = []) {
  if (node === null) return values;
  inOrder(node.left, values);
  values.push(node.value); // visit BETWEEN the subtrees
  inOrder(node.right, values);
  return values;
}

function postOrder(node, values = []) {
  if (node === null) return values;
  postOrder(node.left, values);
  postOrder(node.right, values);
  values.push(node.value); // visit AFTER the subtrees
  return values;
}

//---------------------------------------------------------------
// 2) Iterative DFS -- an explicit stack instead of the call stack
//---------------------------------------------------------------

// Pre-order: push RIGHT first so LEFT is popped first.
function preOrderIterative(root) {
  if (root === null) return [];
  const values = [];
  const stack = [root];

  while (stack.length > 0) {
    const node = stack.pop();
    values.push(node.value);
    if (node.right) stack.push(node.right);
    if (node.left) stack.push(node.left);
  }
  return values;
}

// In-order: dive left pushing every node, then pop-and-visit and turn
// right. This is the pattern behind most "BST iterator" problems.
function inOrderIterative(root) {
  const values = [];
  const stack = [];
  let node = root;

  while (stack.length > 0 || node !== null) {
    while (node !== null) {
      stack.push(node); // remember the way back
      node = node.left;
    }
    node = stack.pop(); // nothing further left -> visit
    values.push(node.value);
    node = node.right;
  }
  return values;
}

//---------------------------------------------------------------
// 3) DFS on a graph
//---------------------------------------------------------------
//
// One thing changes versus a tree: a graph can have CYCLES, so we must
// remember what we have seen or the search never terminates.

function dfsGraph(adjacency, start) {
  const visited = new Set();
  const order = [];

  const walk = (node) => {
    visited.add(node);
    order.push(node);
    for (const neighbour of adjacency[node] || []) {
      if (!visited.has(neighbour)) walk(neighbour);
    }
  };

  if (adjacency[start]) walk(start);
  return order;
}

// "Does a path exist?" -- DFS's natural question, the way "what is the
// SHORTEST path?" is BFS's.
function hasPath(adjacency, start, end, visited = new Set()) {
  if (start === end) return true;
  if (visited.has(start) || !adjacency[start]) return false;
  visited.add(start);
  return adjacency[start].some((neighbour) => hasPath(adjacency, neighbour, end, visited));
}

// demo
//        9
//      /   \
//     4     20
//    / \   /  \
//   1   6 15  170
const tree = buildTree([9, 4, 6, 20, 170, 15, 1]);

console.assert(preOrder(tree).join(',') === '9,4,1,6,20,15,170', 'pre-order');
console.assert(inOrder(tree).join(',') === '1,4,6,9,15,20,170', 'in-order is sorted on a BST');
console.assert(postOrder(tree).join(',') === '1,6,4,15,170,20,9', 'post-order');

// the iterative forms must agree with the recursive ones
console.assert(preOrderIterative(tree).join(',') === preOrder(tree).join(','), 'pre-order, iterative');
console.assert(inOrderIterative(tree).join(',') === inOrder(tree).join(','), 'in-order, iterative');

console.assert(preOrder(null).length === 0 && inOrderIterative(null).length === 0, 'empty tree');

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

// depth first: dive 0 -> 1 -> 2, back up, then 3 -> 4 -> 5 -> 6
console.assert(dfsGraph(adjacency, '0').join(',') === '0,1,2,3,4,5,6', 'DFS over a graph');
console.assert(hasPath(adjacency, '0', '6') === true, 'a path exists');
console.assert(hasPath(adjacency, '0', 'island') === false, 'no path to an isolated node');

console.log('pre-order ', preOrder(tree).join(' '));
console.log('in-order  ', inOrder(tree).join(' '));
console.log('post-order', postOrder(tree).join(' '));
console.log('Success.');

module.exports = {
  preOrder, inOrder, postOrder,
  preOrderIterative, inOrderIterative,
  dfsGraph, hasPath, buildTree, Node,
};
