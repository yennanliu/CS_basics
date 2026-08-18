//---------------------------------------------------------------
// BINARY SEARCH TREE (BST)
//---------------------------------------------------------------
//
// THE BST INVARIANT -- for every node:
//
//       everything in the        node        everything in the
//       left subtree is    <--   value  -->  right subtree is
//       SMALLER                              LARGER
//
//           9
//         /   \
//        4     20
//       / \   /  \
//      1   6 15  170
//
// Two consequences worth memorising:
//   - lookup/insert/remove follow ONE root-to-leaf path, so they cost
//     O(H) where H is the height
//   - an IN-ORDER walk emits the values already SORTED
//
// H is log N only while the tree stays balanced. Inserting sorted
// input degrades the BST into a linked list and every operation
// becomes O(N) -- that is what AVL / red-black trees exist to prevent.
//
// Time  : lookup / insert / remove  O(H) -> O(log N) balanced, O(N) worst
// Space : O(N) storage, O(H) recursion stack for the walks

class Node {
  constructor(value) {
    this.left = null;
    this.right = null;
    this.value = value;
  }
}

class BinarySearchTree {
  constructor(values = []) {
    this.root = null;
    values.forEach((value) => this.insert(value));
  }

  // Walk down comparing, then hang the new node off the empty side.
  insert(value) {
    const node = new Node(value);
    if (this.root === null) {
      this.root = node;
      return this;
    }

    let current = this.root;
    for (;;) {
      if (value < current.value) {
        if (current.left === null) {
          current.left = node;
          return this;
        }
        current = current.left;
      } else if (value > current.value) {
        if (current.right === null) {
          current.right = node;
          return this;
        }
        current = current.right;
      } else {
        return this; // duplicate -> ignore
      }
    }
  }

  // Return the node holding `value`, or null. Iterative: no stack needed.
  lookup(value) {
    let current = this.root;
    while (current !== null) {
      if (value < current.value) current = current.left;
      else if (value > current.value) current = current.right;
      else return current;
    }
    return null;
  }

  min() {
    if (this.root === null) return undefined;
    let node = this.root;
    while (node.left !== null) node = node.left;
    return node.value;
  }

  max() {
    if (this.root === null) return undefined;
    let node = this.root;
    while (node.right !== null) node = node.right;
    return node.value;
  }

  // Remove `value`, keeping the invariant intact. Returns true if found.
  //
  // Three cases once the node is located:
  //   0 children  drop it
  //   1 child     splice the child in where the node was
  //   2 children  copy in the IN-ORDER SUCCESSOR (the smallest value in
  //               the right subtree -- the only value that keeps every
  //               comparison valid), then delete that successor below
  //
  // Written recursively: each call returns the subtree that should
  // replace the one it was given, which removes all the parent-pointer
  // bookkeeping the iterative version needs.
  remove(value) {
    let removed = false;

    const removeFrom = (node, value) => {
      if (node === null) return null;

      if (value < node.value) {
        node.left = removeFrom(node.left, value);
      } else if (value > node.value) {
        node.right = removeFrom(node.right, value);
      } else {
        removed = true;
        if (node.left === null) return node.right; // 0 or 1 child
        if (node.right === null) return node.left;

        let successor = node.right; // 2 children
        while (successor.left !== null) successor = successor.left;
        node.value = successor.value;
        node.right = removeFrom(node.right, successor.value);
      }
      return node;
    };

    this.root = removeFrom(this.root, value);
    return removed;
  }

  // left -> node -> right, which on a BST is SORTED order.
  inorder() {
    const values = [];
    const walk = (node) => {
      if (node === null) return;
      walk(node.left);
      values.push(node.value);
      walk(node.right);
    };
    walk(this.root);
    return values;
  }

  // node -> left -> right
  preorder() {
    const values = [];
    const walk = (node) => {
      if (node === null) return;
      values.push(node.value);
      walk(node.left);
      walk(node.right);
    };
    walk(this.root);
    return values;
  }

  // Edges on the longest root-to-leaf path. Empty = -1, single node = 0.
  height(node = this.root) {
    if (node === null) return -1;
    return 1 + Math.max(this.height(node.left), this.height(node.right));
  }

  // Nested-object view, handy for printing with JSON.stringify.
  toObject(node = this.root) {
    if (node === null) return null;
    return {
      value: node.value,
      left: this.toObject(node.left),
      right: this.toObject(node.right),
    };
  }
}

// demo
//        9
//      /   \
//     4     20
//    / \   /  \
//   1   6 15  170
const tree = new BinarySearchTree([9, 4, 6, 20, 170, 15, 1]);

// an in-order walk of a BST is sorted -- this is the defining property
console.assert(tree.inorder().join(',') === '1,4,6,9,15,20,170', 'in-order is sorted');
console.assert(tree.preorder().join(',') === '9,4,1,6,20,15,170', 'pre-order');
console.assert(tree.height() === 2, 'height in edges');
console.assert(tree.min() === 1 && tree.max() === 170, 'min / max');

console.assert(tree.lookup(6).value === 6, 'lookup finds a stored value');
console.assert(tree.lookup(999) === null, 'lookup of an absent value is null');

tree.insert(9); // duplicate -> ignored
console.assert(tree.inorder().length === 7, 'duplicates are not stored');

console.assert(tree.remove(170) === true, 'remove a leaf');
console.assert(tree.inorder().join(',') === '1,4,6,9,15,20');

console.assert(tree.remove(20) === true, 'remove a node with one child');
console.assert(tree.inorder().join(',') === '1,4,6,9,15');

console.assert(tree.remove(4) === true, 'remove a node with two children');
console.assert(tree.inorder().join(',') === '1,6,9,15');

console.assert(tree.remove(9) === true, 'remove the root');
console.assert(tree.inorder().join(',') === '1,6,15');

console.assert(tree.remove(999) === false, 'removing an absent value is a no-op');
console.assert(tree.inorder().join(',') === '1,6,15');

// sorted input degrades the BST into a chain: height N-1, not log N
const degenerate = new BinarySearchTree([1, 2, 3, 4, 5]);
console.assert(degenerate.height() === 4, 'sorted input -> a linked list');

console.log(JSON.stringify(tree.toObject()));
console.log('Success.');

module.exports = { BinarySearchTree, Node };
