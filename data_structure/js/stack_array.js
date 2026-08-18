//---------------------------------------------------------------
// STACK - array implementation
//---------------------------------------------------------------
//
// Scope: the ARRAY-backed stack. See stack_linkedlist.js for the
//        pointer-based version and the trade-off between the two.
//
// LIFO (Last In, First Out): the last value pushed is the first popped.
//
//     push('c') ->  |   |     pop() ->  |   |
//                   | c |               |   |
//                   | b |               | b |
//                   | a |               | a |
//                   +---+               +---+
//
// A JS array already grows on demand, and push/pop touch only the END
// of it, so both are O(1) amortised. Never use shift()/unshift() for a
// stack -- those work on the FRONT and are O(N).
//
// Time  : push / pop / peek / isEmpty / size -> O(1)
// Space : O(N)

class Stack {
  constructor() {
    this.array = [];
  }

  get length() {
    return this.array.length;
  }

  isEmpty() {
    return this.array.length === 0;
  }

  // Return the top value without removing it; undefined when empty.
  peek() {
    return this.array[this.array.length - 1];
  }

  push(value) {
    this.array.push(value);
    return this;
  }

  // Remove and RETURN the top value; undefined when empty.
  pop() {
    return this.array.pop();
  }

  toArray() {
    // bottom -> top
    return [...this.array];
  }
}

// demo
const stack = new Stack();
console.assert(stack.isEmpty(), 'a new stack is empty');
console.assert(stack.peek() === undefined, 'peek on empty is undefined');
console.assert(stack.pop() === undefined, 'pop on empty is undefined');

stack.push('google').push('udemy').push('discord');
console.assert(stack.length === 3, 'three values pushed');
console.assert(stack.toArray().join(',') === 'google,udemy,discord', 'bottom -> top');

// last in, first out
console.assert(stack.peek() === 'discord', 'peek returns the top');
console.assert(stack.length === 3, 'peek does not remove');
console.assert(stack.pop() === 'discord', 'pop returns the top');
console.assert(stack.pop() === 'udemy');
console.assert(stack.pop() === 'google');
console.assert(stack.isEmpty(), 'stack is drained');

console.log('Success.');

module.exports = { Stack };
