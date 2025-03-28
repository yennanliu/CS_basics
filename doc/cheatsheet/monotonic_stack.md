# Monotonic Stack


A Monotonic Stack is a specialized stack data structure that is used to efficiently solve problems related to finding the `next greater element`, previous smaller element, or any other monotonic property of sequences. The stack maintains a monotonic (either strictly increasing or strictly decreasing) order of elements, which helps optimize algorithms for a variety of problems, especially those involving arrays or sequences.



## LC problems

- next bigger number
  - LC 2104
  - LC 239
  - LC 496
- circular next bigger number
  - LC 503


## Pattern

- monotonic stack

```java
// LC 239
// LC 496
// ...

// Traverse the array from right to left
for (int i = 0; i < n; i++) {
    // Maintain a decreasing monotonic stack
    while (!stack.isEmpty() && nums[stack.peek()] <= nums[i]) {
        stack.pop();  // Pop elements from the stack that are smaller or equal to the current element
    }
    
    // If stack is not empty, the next greater element is at the top of the stack
    if (!stack.isEmpty()) {
        result[i] = nums[stack.peek()];
    }
    
    // Push the current element's index onto the stack
    stack.push(i);
}

// ...
```
