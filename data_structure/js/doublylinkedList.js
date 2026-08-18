//---------------------------------------------------------------
// DOUBLY LINKED LIST
//---------------------------------------------------------------
//
// Scope: the DOUBLY linked list (each node knows both neighbours).
//        See linkedlist.js for the singly linked version.
//
//     null <- [1] <-> [10] <-> [5] -> null
//              ^                ^
//             head             tail
//
// The extra `prev` pointer costs one reference per node and buys:
//   - backwards traversal
//   - O(1) removal of a node you already hold (no walking the list to
//     find its predecessor)
// That second property is why an LRU cache is a hash map plus a
// doubly linked list.
//
// Time  : prepend / append        O(1)
//         removeNode(node)        O(1)
//         get / insert / remove by index  O(N)
// Space : O(N)

class Node {
  constructor(value) {
    this.value = value;
    this.prev = null;
    this.next = null;
  }
}

class DoublyLinkedList {
  // `new DoublyLinkedList(10)` starts with one node; no argument is empty.
  constructor(value) {
    this.head = null;
    this.tail = null;
    this.length = 0;
    if (value !== undefined) this.append(value);
  }

  isEmpty() {
    return this.length === 0;
  }

  prepend(value) {
    const node = new Node(value);
    if (this.head === null) {
      this.head = node;
      this.tail = node;
    } else {
      node.next = this.head;
      this.head.prev = node;
      this.head = node;
    }
    this.length++;
    return node;
  }

  append(value) {
    const node = new Node(value);
    if (this.tail === null) {
      this.head = node;
      this.tail = node;
    } else {
      node.prev = this.tail;
      this.tail.next = node;
      this.tail = node;
    }
    this.length++;
    return node;
  }

  _nodeAt(index) {
    let node = this.head;
    for (let i = 0; i < index; i++) node = node.next;
    return node;
  }

  get(index) {
    if (index < 0 || index >= this.length) return undefined;
    return this._nodeAt(index).value;
  }

  // Insert `value` so that it ends up AT `index`.
  // FOUR pointers get rewired -- draw it before you code it:
  //     leader <-> follower          becomes
  //     leader <-> new <-> follower
  insert(index, value) {
    if (index <= 0) return this.prepend(value);
    if (index >= this.length) return this.append(value);

    const node = new Node(value);
    const leader = this._nodeAt(index - 1);
    const follower = leader.next;

    node.prev = leader;
    node.next = follower;
    leader.next = node;
    follower.prev = node;
    this.length++;
    return node;
  }

  // Unlink a node we already hold -- O(1), no traversal.
  removeNode(node) {
    if (node.prev) node.prev.next = node.next;
    else this.head = node.next; // node was the head

    if (node.next) node.next.prev = node.prev;
    else this.tail = node.prev; // node was the tail

    node.prev = null;
    node.next = null;
    this.length--;
    return node.value;
  }

  // Remove the node at `index` and return its value.
  remove(index) {
    if (index < 0 || index >= this.length) return undefined;
    return this.removeNode(this._nodeAt(index));
  }

  toArray() {
    const values = [];
    for (let node = this.head; node !== null; node = node.next) {
      values.push(node.value);
    }
    return values;
  }

  // Walk backwards from the tail -- impossible in a singly linked list.
  toArrayReversed() {
    const values = [];
    for (let node = this.tail; node !== null; node = node.prev) {
      values.push(node.value);
    }
    return values;
  }
}

// demo
const list = new DoublyLinkedList(10);
list.append(5);
list.append(16);
list.prepend(1);
console.assert(list.toArray().join(',') === '1,10,5,16', 'prepend + append');
console.assert(list.toArrayReversed().join(',') === '16,5,10,1', 'same list, read backwards');

list.insert(2, 99);
console.assert(list.toArray().join(',') === '1,10,99,5,16', 'insert in the middle');
console.assert(list.toArrayReversed().join(',') === '16,5,99,10,1', 'prev links stayed correct');

list.insert(20, 88); // index past the end -> appended
console.assert(list.tail.value === 88 && list.tail.prev.value === 16, 'tail wired both ways');

console.assert(list.get(2) === 99, 'get by index');
console.assert(list.remove(2) === 99, 'remove returns the removed value');
console.assert(list.toArray().join(',') === '1,10,5,16,88');

// O(1) removal of a node we kept a reference to
const node = list.insert(2, 77);
console.assert(list.toArray().join(',') === '1,10,77,5,16,88');
list.removeNode(node);
console.assert(list.toArray().join(',') === '1,10,5,16,88', 'node unlinked without a walk');

// removing the ends keeps head/tail consistent in BOTH directions
list.remove(0);
list.remove(list.length - 1);
console.assert(list.toArray().join(',') === '10,5,16');
console.assert(list.toArrayReversed().join(',') === '16,5,10');
console.assert(list.head.prev === null && list.tail.next === null, 'ends are terminated');

const single = new DoublyLinkedList(1);
single.remove(0);
console.assert(single.isEmpty() && single.head === null && single.tail === null,
  'emptying resets both pointers');

console.log('Success.');

module.exports = { DoublyLinkedList, Node };
