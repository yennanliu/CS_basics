//---------------------------------------------------------------
// STACK - linked list implementation
//---------------------------------------------------------------
//
// Scope: the POINTER-based stack. See stack_array.js for the
//        array-backed version.
//
// LIFO (Last In, First Out). Every node points at the node BELOW it,
// so pushing and popping only ever touch `top`:
//
//     top
//      |
//      v
//     [c] -> [b] -> [a] -> null
//                          (bottom)
//
// ARRAY vs LINKED LIST for a stack:
//   array         contiguous memory, better cache locality, but the
//                 underlying buffer is reallocated as it grows
//   linked list   every push allocates a node (more memory per value),
//                 but push/pop are worst-case O(1), never amortised
//
// Time  : push / pop / peek / isEmpty -> O(1)
// Space : O(N)

class Node {
  constructor(value) {
    this.value = value;
    this.next = null;
  }
}

class Stack {
  constructor() {
    this.top = null;
    this.bottom = null;
    this.length = 0;
  }

  isEmpty() {
    return this.length === 0;
  }

  // Return the top VALUE without removing it; undefined when empty.
  peek() {
    return this.top ? this.top.value : undefined;
  }

  push(value) {
    const node = new Node(value);
    if (this.top === null) {
      // first node: it is both the top and the bottom
      this.top = node;
      this.bottom = node;
    } else {
      node.next = this.top; // the new node sits ON TOP of the old one
      this.top = node;
    }
    this.length++;
    return this;
  }

  // Remove and RETURN the top value; undefined when empty.
  pop() {
    if (this.top === null) return undefined;
    const node = this.top;
    this.top = node.next;
    if (this.top === null) this.bottom = null; // stack just emptied
    this.length--;
    return node.value;
  }

  toArray() {
    // top -> bottom
    const values = [];
    for (let node = this.top; node !== null; node = node.next) {
      values.push(node.value);
    }
    return values;
  }
}

// demo
const stack = new Stack();
console.assert(stack.isEmpty(), 'a new stack is empty');
console.assert(stack.peek() === undefined, 'peek on empty is undefined');
console.assert(stack.pop() === undefined, 'pop on empty is undefined');

stack.push('google').push('udemy').push('discord');
console.assert(stack.length === 3, 'three values pushed');
console.assert(stack.toArray().join(',') === 'discord,udemy,google', 'top -> bottom');
console.assert(stack.bottom.value === 'google', 'bottom is the first value pushed');

// last in, first out
console.assert(stack.peek() === 'discord', 'peek returns the top value');
console.assert(stack.length === 3, 'peek does not remove');
console.assert(stack.pop() === 'discord', 'pop returns the top value');
console.assert(stack.pop() === 'udemy');
console.assert(stack.pop() === 'google');
console.assert(stack.isEmpty(), 'stack is drained');
console.assert(stack.bottom === null, 'bottom is reset, so push still works');

stack.push('again');
console.assert(stack.peek() === 'again' && stack.bottom.value === 'again');

console.log('Success.');

module.exports = { Stack, Node };
