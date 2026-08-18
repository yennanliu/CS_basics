//---------------------------------------------------------------
// QUEUE - linked list implementation
//---------------------------------------------------------------
//
// FIFO (First In, First Out): the first value enqueued is the first
// dequeued.
//
//     dequeue <--- [first] a -> b -> c [last] <--- enqueue
//
// Keeping a `last` pointer is what makes enqueue O(1) -- without it we
// would have to walk the whole list to find the end.
//
// WHY NOT JUST AN ARRAY? `array.push()` is O(1), but `array.shift()`
// has to reindex every remaining element, so it is O(N). A linked list
// gives O(1) at BOTH ends, which is why queues are usually built this
// way. (In real code, reach for two stacks or a ring buffer instead.)
//
// Time  : enqueue / dequeue / peek / isEmpty -> O(1)
// Space : O(N)

class Node {
  constructor(value) {
    this.value = value;
    this.next = null;
  }
}

class Queue {
  constructor() {
    this.first = null; // dequeue from here
    this.last = null;  // enqueue to here
    this.length = 0;
  }

  isEmpty() {
    return this.length === 0;
  }

  // Return the front VALUE without removing it; undefined when empty.
  peek() {
    return this.first ? this.first.value : undefined;
  }

  enqueue(value) {
    const node = new Node(value);
    if (this.last === null) {
      // first node: it is both the front and the back
      this.first = node;
      this.last = node;
    } else {
      this.last.next = node; // link it after the current back
      this.last = node;
    }
    this.length++;
    return this;
  }

  // Remove and RETURN the front value; undefined when empty.
  dequeue() {
    if (this.first === null) return undefined;
    const node = this.first;
    this.first = node.next;
    if (this.first === null) this.last = null; // queue just emptied
    this.length--;
    return node.value;
  }

  toArray() {
    // front -> back
    const values = [];
    for (let node = this.first; node !== null; node = node.next) {
      values.push(node.value);
    }
    return values;
  }
}

// demo
const queue = new Queue();
console.assert(queue.isEmpty(), 'a new queue is empty');
console.assert(queue.peek() === undefined, 'peek on empty is undefined');
console.assert(queue.dequeue() === undefined, 'dequeue on empty is undefined');

queue.enqueue('Joy').enqueue('Matt').enqueue('Pavel');
console.assert(queue.length === 3, 'three values enqueued');
console.assert(queue.toArray().join(',') === 'Joy,Matt,Pavel', 'front -> back');

// first in, first out
console.assert(queue.peek() === 'Joy', 'peek returns the front value');
console.assert(queue.length === 3, 'peek does not remove');
console.assert(queue.dequeue() === 'Joy', 'dequeue returns the front value');
console.assert(queue.dequeue() === 'Matt');
console.assert(queue.dequeue() === 'Pavel');
console.assert(queue.isEmpty(), 'queue is drained');
console.assert(queue.last === null, 'last is reset, so enqueue still works');

queue.enqueue('Again');
console.assert(queue.peek() === 'Again' && queue.last.value === 'Again');

console.log('Success.');

module.exports = { Queue, Node };
