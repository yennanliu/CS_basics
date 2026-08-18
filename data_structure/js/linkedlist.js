//---------------------------------------------------------------
// LINKED LIST (singly linked)
//---------------------------------------------------------------
//
// Scope: the SINGLY linked list (each node points forward only).
//        See doublylinkedList.js for the prev+next variant.
//
//     head                          tail
//      |                             |
//      v                             v
//     [10] -> [5] -> [16] -> null
//
// Unlike an array there is no index arithmetic: reaching position i
// means walking i links. In exchange, inserting/removing costs O(1)
// once you hold the node BEFORE the target -- nothing shifts.
//
// Time  : prepend / append   O(1)   (a `tail` pointer is kept)
//         get / insert / remove by index   O(N)
//         reverse            O(N)
// Space : O(N)

class Node {
  constructor(value, next = null) {
    this.value = value;
    this.next = next;
  }
}

class LinkedList {
  // `new LinkedList(10)` starts with one node; `new LinkedList()` is empty.
  constructor(value) {
    this.head = null;
    this.tail = null;
    this.length = 0;
    if (value !== undefined) this.append(value);
  }

  isEmpty() {
    return this.length === 0;
  }

  // Insert at the FRONT.  before: 1->2   after: 0->1->2
  prepend(value) {
    const node = new Node(value, this.head);
    this.head = node;
    if (this.tail === null) this.tail = node; // first node ever
    this.length++;
    return this;
  }

  // Insert at the END.  before: 1->2   after: 1->2->3
  append(value) {
    const node = new Node(value);
    if (this.tail === null) {
      this.head = node;
      this.tail = node;
    } else {
      this.tail.next = node;
      this.tail = node;
    }
    this.length++;
    return this;
  }

  // Node at `index`. Caller must have validated the index.
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
  //   before : 1 -> 2 -> 3     insert(1, 9)
  //   after  : 1 -> 9 -> 2 -> 3
  // An index past the end appends, rather than throwing.
  insert(index, value) {
    if (index <= 0) return this.prepend(value);
    if (index >= this.length) return this.append(value);

    const leader = this._nodeAt(index - 1); // the node BEFORE the target slot
    leader.next = new Node(value, leader.next);
    this.length++;
    return this;
  }

  // Remove the node at `index` and return its value; undefined if out of range.
  remove(index) {
    if (index < 0 || index >= this.length) return undefined;

    let removed;
    if (index === 0) {
      removed = this.head;
      this.head = removed.next;
      if (this.head === null) this.tail = null; // list became empty
    } else {
      const leader = this._nodeAt(index - 1);
      removed = leader.next;
      leader.next = removed.next;
      if (removed === this.tail) this.tail = leader; // removed the last node
    }
    this.length--;
    return removed.value;
  }

  // Reverse in place by flipping every `next` pointer.
  //   before : 1 -> 2 -> 3
  //   after  : 3 -> 2 -> 1
  // `previous` trails one node behind `current`; `next` is saved first
  // because rewriting current.next destroys the way forward.
  reverse() {
    let previous = null;
    let current = this.head;
    this.tail = this.head;

    while (current !== null) {
      const next = current.next;
      current.next = previous;
      previous = current;
      current = next;
    }
    this.head = previous;
    return this;
  }

  toArray() {
    const values = [];
    for (let node = this.head; node !== null; node = node.next) {
      values.push(node.value);
    }
    return values;
  }
}

// demo
const list = new LinkedList(10);
list.append(5).append(16).prepend(1);
console.assert(list.toArray().join(',') === '1,10,5,16', 'prepend + append');
console.assert(list.length === 4);

list.insert(2, 99);
console.assert(list.toArray().join(',') === '1,10,99,5,16', 'insert in the middle');

list.insert(20, 88); // index past the end -> appended
console.assert(list.toArray().join(',') === '1,10,99,5,16,88', 'out-of-range insert appends');
console.assert(list.tail.value === 88, 'tail follows the append');

console.assert(list.get(2) === 99, 'get by index');
console.assert(list.get(99) === undefined, 'out-of-range get is undefined');

console.assert(list.remove(2) === 99, 'remove returns the removed value');
console.assert(list.toArray().join(',') === '1,10,5,16,88');

console.assert(list.remove(list.length - 1) === 88, 'remove the tail');
console.assert(list.tail.value === 16, 'tail is repaired after removing the last node');

list.reverse();
console.assert(list.toArray().join(',') === '16,5,10,1', 'reversed');
console.assert(list.head.value === 16 && list.tail.value === 1, 'head/tail swapped');

const single = new LinkedList(1);
single.remove(0);
console.assert(single.isEmpty() && single.head === null && single.tail === null,
  'emptying resets both pointers');

console.log('Success.');

module.exports = { LinkedList, Node };
