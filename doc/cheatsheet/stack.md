# Stack

## Time Complexity

| Data structure | Search   | Insert   | Delete   | Min/Max  |
| -------------- | -------- | -------- | -------- | -------- |
| Stack          | O(n)     | O(1)     | O(1)     | O(n)     |

> Insert = push, Delete = pop (both at the top, **O(1)**). Min/Max can be made **O(1)** with an auxiliary min/max-stack ([monotonic_stack.md](./monotonic_stack.md)).

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/stack.jpeg"></p>

**Stack** is a data structure with Last-In-First-Out (LIFO) property. Each operation adds/removes from the top of the stack.

### Key Properties
- **Time Complexity**: O(1) for push, pop, peek
- **Space Complexity**: O(n) where n is the number of elements
- **Core Principle**: Last element added is the first one removed
- **Use Case**: Problems involving order reversal, pattern matching, or maintaining context

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/stack_101.png"></p>

### Quick Decision Guide

| Problem Type | Pattern | Key Idea | Examples |
|--------------|---------|----------|----------|
| Find **next greater/smaller** element | Monotonic Stack | Maintain increasing/decreasing order | LC 496, 503, 739 |
| **Remove adjacent duplicates** | Stack with Pair [element, count] | Track counts, pop when k reached | LC 1047, 1209, 1544 |
| **Decode strings** with brackets | Stack with Count | Use pairs for nested repetitions | LC 394, 726 |
| **Arithmetic expressions** | Stack with Operators | Handle precedence and evaluation | LC 224, 227 |
| **Remove k digits** for min number | Greedy + Monotonic | Pop larger digits when beneficial | LC 402 |
| **Lexicographically smallest** with duplicates | Monotonic + Last Occurrence | Greedy removal with "appears later" check | LC 316, 1081 |
| **Streaming/online** frequency | Stack with Span Pairs | Accumulate counts in pairs | LC 901 |
| **FIFO from LIFO** | Two Stacks | Use input/output stacks for queue | LC 232 |

**How to read**: Find your problem goal in the leftmost column, then use the pattern and examples as a starting point.

- Ref
    - [fuck-Algorithm - single stack](https://github.com/labuladong/fucking-Algorithm/blob/master/%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84%E7%B3%BB%E5%88%97/%E5%8D%95%E8%B0%83%E6%A0%88.md)
    - [fuck-Algorithm - implement array via stack / stack via array ](https://github.com/labuladong/fucking-Algorithm/blob/master/%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84%E7%B3%BB%E5%88%97/%E9%98%9F%E5%88%97%E5%AE%9E%E7%8E%B0%E6%A0%88%E6%A0%88%E5%AE%9E%E7%8E%B0%E9%98%9F%E5%88%97.md)

### Important Tips
- **Monotonic Stack**: Critical pattern for problems involving "next greater/smaller" — check if the pattern requires increasing or decreasing order
- **Stack with Pair**: For adjacent duplicate removal or nested counting problems, store `[element, count]` pairs
- **Greedy Removal**: Some problems benefit from greedily removing elements while maintaining an invariant

### Video References
- [Stack Fundamentals](https://www.bilibili.com/list/525438321?sort_field=pubtime&spm_id_from=333.999.0.0&oid=779764003&bvid=BV1my4y1Z7jj)

## 0) Concept
- [Java Stack](https://blog.csdn.net/oChangWen/article/details/72859556)
    - Low level : Array

### 0-1) Types

- Single stack
- Build queue via stack
     - LC 232 (use `2 stack`)
- Build stack via queue
- **Stack with Pair (char, count)**
     - Store `[element, count]` pairs instead of raw elements
     - LC 1047 (k=2 special case, simple pop)
     - LC 1209 (k consecutive duplicates removal)
     - LC 1544 (Make The String Great)
     - LC 394 (Decode String, stack with count for repetition)
     - LC 726 (Number of Atoms)

### 0-2) Pattern

```python
# python
# LC 739, LC 503 - Find next `big number`
# ...
stack = [] # [[idx, val]]
for i, val in enumerate(len(tmp)):
    while stack and stack[-1][1] < val:
        _idx, _val = stack.pop(-1)
        res[tmp[_idx]] = i - _idx
    stack.append([i, val]) 
# ...
```

```java
// java
// LC 739
for (int j = 0; j < temperatures.length; j++){
    int x = temperatures[j];
    /**
     *  NOTE !!!
     *   1) while loop
     *   2) stack is NOT empty
     *   3) cache temperature smaller than current temperature
     *
     *   st.peek().get(0) is cached temperature
     */
    while (!st.isEmpty() && st.peek().get(0) < x){
        /**
         *  st.peek().get(1) is idx
         *
         */
        nextGreater[st.peek().get(1)] = j - st.peek().get(1);
        st.pop();
    }
```

- monotonic stack (`increasing`)

```java
// java
// LC 239
// LC 496
// ...

// Traverse the array from right to left
for (int i = 0; i < n; i++) {
    // Maintain a decreasing monotonic stack
    /** NOTE !!! below */
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

- **Stack with Pair `[element, count]`** (adjacent duplicate removal with k)

```
Core Idea:
  - Instead of pushing raw elements, push [element, count] pairs
  - When current element == stack top element: increment count
  - When count reaches k: pop (remove k consecutive duplicates)
  - Handles partial sequences naturally (e.g., "aa" waiting for 3rd 'a')

When to Use:
  - Remove k consecutive/adjacent equal elements (k >= 2)
  - Track both identity AND frequency of consecutive runs
  - Need compressed representation of the string/array
  - LC 1047 is k=2 special case (simple pop, no count needed)

Similar LC:
  - LC 1047  Remove All Adjacent Duplicates in String (k=2)
  - LC 1209  Remove All Adjacent Duplicates in String II (general k)
  - LC 1544  Make The String Great (adjacent opposite-case removal)
  - LC 394   Decode String (stack with count for repetition)
  - LC 726   Number of Atoms (nested count tracking)
```

```python
# python - Stack Pair pattern
# LC 1209
stack = [['#', 0]]  # sentinel to avoid empty-check
for c in s:
    if stack[-1][0] == c:
        stack[-1][1] += 1
        if stack[-1][1] == k:
            stack.pop()
    else:
        stack.append([c, 1])
return ''.join(c * cnt for c, cnt in stack)
```

```java
// java - Stack Pair pattern
// LC 1209
Stack<int[]> stack = new Stack<>(); // {char_as_int, count}
for (char c : s.toCharArray()) {
    if (!stack.isEmpty() && stack.peek()[0] == c) {
        stack.peek()[1]++;
        if (stack.peek()[1] == k) {
            stack.pop();
        }
    } else {
        stack.push(new int[]{c, 1});
    }
}
StringBuilder sb = new StringBuilder();
for (int[] pair : stack) {
    for (int i = 0; i < pair[1]; i++) {
        sb.append((char) pair[0]);
    }
}
return sb.toString();
```

- monotonic stack (`decreasing`)

```java

// java
for (int j = 0; j < temperatures.length; j++) {
    int x = temperatures[j];
    /**
     *  NOTE !!!
     *   1) while loop
     *   2) stack is NOT empty
     *   3) cache temperature greater than current temperature
     *
     *   st.peek().get(0) is cached temperature
     */
     /** NOTE !!! below */
    while (!st.isEmpty() && st.peek().get(0) > x) {
        /**
         *  st.peek().get(1) is idx
         *
         */
        nextGreater[st.peek().get(1)] = j - st.peek().get(1);
        st.pop();
    }
    
    // Push the current temperature and its index to the stack
    st.push(new Pair<>(x, j));
}
```

- **monotonic stack (greedy removal pattern - Remove K Digits)** ⭐⭐⭐⭐

```
Core Idea:
  - To make the SMALLEST number, a high-place digit weighs more than any
    low-place digit. So a bigger digit sitting BEFORE a smaller one is bad
    -> greedily pop it while we still have removals (k > 0).
  - Maintain a monotonic INCREASING stack: for each incoming digit, pop the
    stack top whenever top > digit and k > 0 (each pop uses one removal).
  - Each digit is pushed once and popped at most once -> O(n).

Pattern (3 phases):
  1) SCAN + POP  : for each digit, pop larger tops while k > 0, then push.
  2) TAIL CUT    : if k still > 0 (digits were non-decreasing, e.g. "12345"),
                   remove the last k digits -> stack[:-k].
  3) CLEAN UP    : strip leading zeros (lstrip('0')); if empty -> "0".

When to Use:
  - "Remove k elements to get the smallest/largest sequence" (order preserved)
  - "Build lexicographically smallest/largest result by dropping elements"
  - Result must keep the RELATIVE order of the kept elements (not sorting)

Watch-outs:
  - Leading zeros: "10200", k=1 -> "0200" -> strip -> "200"
  - Removals left over after the scan -> cut from the TAIL, not the front
  - Empty result -> return "0" (LC 402), or handle per-problem sentinel

Similar LC:
  - LC 402   Remove K Digits (canonical greedy monotonic removal)
  - LC 316   Remove Duplicate Letters (greedy + "appears later" check)
  - LC 1081  Smallest Subsequence of Distinct Characters (same as LC 316)
  - LC 1673  Find the Most Competitive Subsequence (keep exactly n-k, min result)
  - LC 321   Create Maximum Number (greedy pick, monotonic, two-array merge)
```

```python
# python
# LC 402 - Remove K Digits
# IDEA: MONOTONIC STACK + greedy removal (pop larger digit before a smaller one)
# time = O(n), space = O(n)
class Solution(object):
    def removeKdigits(self, num, k):
        stack = []

        # 1) SCAN + POP: while a bigger digit sits before current, drop it
        for digit in num:
            while k > 0 and stack and stack[-1] > digit:
                stack.pop()
                k -= 1
            stack.append(digit)

        # 2) TAIL CUT: removals left over (num was non-decreasing) -> chop the end
        while k > 0:
            stack.pop()
            k -= 1

        # 3) CLEAN UP: strip leading zeros; empty -> "0"
        res = "".join(stack).lstrip('0')
        return res if res else "0"
```

```java
// java
// LC 402 Remove K Digits
// Pattern: Maintain monotonic increasing stack by greedily removing larger digits

Deque<Character> stack = new ArrayDeque<>();

for (int i = 0; i < num.length(); i++) {
    char digit = num.charAt(i);

    /**
     * NOTE !!!
     * Greedy removal: while we can still remove digits (k > 0)
     * and current digit is smaller than stack top,
     * pop the larger digit to make the number smaller
     */
    while (k > 0 && !stack.isEmpty() && stack.peekLast() > digit) {
        stack.removeLast();
        k--;
    }
    stack.addLast(digit);
}

// If k > 0, remove remaining digits from the end
while (k > 0) {
    stack.removeLast();
    k--;
}

// Remove leading zeros
StringBuilder sb = new StringBuilder();
boolean leadingZero = true;
while (!stack.isEmpty()) {
    char c = stack.removeFirst();
    if (leadingZero && c == '0')
        continue;
    leadingZero = false;
    sb.append(c);
}

return sb.length() == 0 ? "0" : sb.toString();
```

- monotonic stack (lexicographical order with deduplication - Remove Duplicate Letters)

```java
// java
// LC 316 Remove Duplicate Letters (same as LC 1081)
// Pattern: Monotonic increasing stack with "will appear later" check for lexicographical smallest result

/**
 * Key differences from basic greedy removal:
 * 1. Track which characters are already in result (inStack/seen)
 * 2. Only remove if character will appear again later (lastOccurrence)
 * 3. Each character must appear exactly once
 */

// Step 1: Store the LAST index where each character appears
int[] lastOccurrence = new int[26];
for (int i = 0; i < s.length(); i++) {
    lastOccurrence[s.charAt(i) - 'a'] = i;
}

Stack<Character> stack = new Stack<>();
// Use boolean array for O(1) "contains" check
boolean[] inStack = new boolean[26];

for (int i = 0; i < s.length(); i++) {
    char c = s.charAt(i);

    // If character already in stack, skip it (each char appears once)
    if (inStack[c - 'a'])
        continue;

    /**
     * NOTE !!! MONO STACK LOGIC with "will appear later" check
     *
     * We can safely remove stack top if ALL conditions met:
     * 0. Stack is NOT empty
     * 1. Stack top is BIGGER than current char (for lexicographical order)
     * 2. Stack top will appear AGAIN LATER (lastOccurrence[stack.peek()] > i)
     *
     * This ensures we get the lexicographically smallest result
     */
    while (!stack.isEmpty() && stack.peek() > c && lastOccurrence[stack.peek() - 'a'] > i) {
        char removed = stack.pop();
        inStack[removed - 'a'] = false;  // Mark as not in stack
    }

    stack.push(c);
    inStack[c - 'a'] = true;  // Mark as in stack
}

// Build result from stack
StringBuilder sb = new StringBuilder();
for (char c : stack) {
    sb.append(c);
}
return sb.toString();
```

**Explanation of "will appear later" logic:**

```java
/**
 * lastOccurrence array tracks the LAST index of each character
 *
 * Example: s = "cbacdcbc"
 *
 * lastOccurrence['c' - 'a'] = 7  (last 'c' at index 7)
 * lastOccurrence['b' - 'a'] = 6  (last 'b' at index 6)
 * lastOccurrence['a' - 'a'] = 2  (last 'a' at index 2)
 * lastOccurrence['d' - 'a'] = 4  (last 'd' at index 4)
 *
 * When at index i = 1 (char 'b'):
 * - Stack has 'c', current char is 'b'
 * - 'c' > 'b' (can potentially remove 'c')
 * - lastOccurrence['c'] = 7 > 1 (YES, 'c' appears later)
 * - Safe to remove 'c' and add 'b' first
 *
 * When at index i = 2 (char 'a'):
 * - Stack has 'b', current char is 'a'
 * - 'b' > 'a' (can potentially remove 'b')
 * - lastOccurrence['b'] = 6 > 2 (YES, 'b' appears later)
 * - Safe to remove 'b' and add 'a' first
 *
 * Result: "acdb" (lexicographically smallest)
 */
```

- monotonic stack (span accumulation pattern - Online Stock Span)

```java
// java
// LC 901 Online Stock Span
// Pattern: Monotonic decreasing stack with span accumulation for streaming data

Deque<int[]> stack = new ArrayDeque<>(); // {price, span}

public int next(int price) {
    int span = 1; // Today always counts

    /**
     * NOTE !!!
     * Pop all smaller/equal prices and accumulate their spans
     * This gives us the count of consecutive days with price <= current
     */
    while (!stack.isEmpty() && stack.peek()[0] <= price) {
        span += stack.pop()[1]; // Absorb previous span
    }

    stack.push(new int[] { price, span });
    return span;
}

/**
 * Key differences from other monotonic stack patterns:
 * 1. Streaming/online: processes data one at a time (not batch)
 * 2. Span accumulation: accumulates counts rather than finding next element
 * 3. Stateful: maintains stack across multiple calls
 * 4. Stack stores pairs: [value, accumulated_count]
 */
```

- Implement Queue using Stacks

```java
// java

// LC 232
// V3
// https://leetcode.com/problems/implement-queue-using-stacks/solutions/6579732/video-simple-solution-by-niits-sqaw/
// IDEA: 2 stack
class MyQueue_3{
    private Stack<Integer> input;
    private Stack<Integer> output;

    public MyQueue_3() {
        input = new Stack<>();
        output = new Stack<>();
    }

    public void push(int x) {
        input.push(x);
    }

    public int pop() {
        /**
         *  NOTE !!!
         *
         *  1)  before calling pop() directly,
         *      we firstly call `peak()`
         *      purpose:
         *        reset / reassign elements at `output` stack,
         *        so we can have the element in `queue ordering` in `output` stack
         *
         *  2) peak() return an integer, but it DOES NOT terminate the pop() execution
         *     since the `peek()` method is called and NOT assign its result to any object,
         *     then the `output.pop();` code is executed and return as result
         */
        peek();
        return output.pop();
    }

    public int peek() {
        if (output.isEmpty()) {
            while (!input.isEmpty()) {
                output.push(input.pop());
            }
        }
        return output.peek();
    }

    public boolean empty() {
        return input.isEmpty() && output.isEmpty();
    }
}
```

## 1) Core Patterns

### 1-1) Basic Stack Operations

**Stack push (insert):**
```java
// Java
Stack<Integer> stack = new Stack<>();
stack.push(element);  // O(1)
```

```python
# Python
stack = []
stack.append(element)  # O(1)
```

**Stack pop (remove top):**
```java
// Java
int top = stack.pop();  // O(1), throws if empty
```

```python
# Python
top = stack.pop()  # O(1), raises if empty
```

**Stack peek (view top):**
```java
// Java
int top = stack.peek();  // O(1), throws if empty
if (!stack.isEmpty()) {
    top = stack.peek();
}
```

```python
# Python
top = stack[-1]  # O(1), raises if empty
if stack:
    top = stack[-1]
```

---

### 1-2) Monotonic Stack: Next Greater Element — LC 496

```java
// java
// LC 496
  // V0
    // IDEA : STACK
    // https://www.youtube.com/watch?v=68a1Dc_qVq4
    /** NOTE !!!
     *
     *  nums1 is "sub set" of nums2,
     *  so all elements in nums1 are in nums2 as well
     *  and in order to find next greater element in nums1 reference nums2
     *  -> ACTUALLY we only need to check nums2
     *  -> then append result per element in nums1
     */
    /**
     *
     *  Example 1)
     *
     *  nums1 = [4,1,2]
     *  nums2 = [1,3,4,2]
     *           x
     *             x
     *               x
     *                 x
     *  st = [1]
     *  st = [3]  map : {1:3}
     *  st = [4], map : {1:3, 3:4}
     *  st = [], map : {1:3, 3:4}
     *
     *  so, res = [-1, 3, -1]
     *
     *
     *  Example 2)
     *
     *   nums1 = [1,3,5,2,4]
     *   nums2 = [6,5,4,3,2,1,7]
     *            x
     *              x
     *               x
     *                 x
     *                   x
     *                     x
     *                       x
     *                         x
     *
     *  st = [6], map :{}
     *  st = [6,5],  map :{}
     *  ..
     *
     *  st = [6,5,4,3,2,1], map = {}
     *  st = [], map = {6:7, 5:7,4:7,3:7,2:7,1:7}
     *
     */
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {

        if (nums1.length == 1 && nums2.length == 1){
            return new int[]{-1};
        }

        /**
         *  NOTE !!!
         *  we use map " collect next greater element"
         *  map definition :  {element, next-greater-element}
         */
        Map<Integer, Integer> map = new HashMap<>();
        Stack<Integer> st = new Stack<>();

        for (int x : nums2){
            /**
             *  NOTE !!!
             *   1) use while loop
             *   2) while stack is NOT null and stack "top" element is smaller than current element (x) is nums2
             *
             *   -> found "next greater element", so update map
             */
            while(!st.isEmpty() && st.peek() < x){
                int cur = st.pop();
                map.put(cur, x);
            }
            /** NOTE !!! if not feat above condition, we put element to stack */
            st.add(x);
        }

        //System.out.println("map = " + map);
        int[] res = new int[nums1.length];
        // fill with -1 for element without next greater element
        Arrays.fill(res, -1);
        for (int j = 0; j < nums1.length; j++){
            if(map.containsKey(nums1[j])){
                res[j] = map.get(nums1[j]);
            }
        }

        //System.out.println("res = " + res);
        return res;
    }
```


### 1-1-2-2) next greater element 2 — LC 503

```python
# V0
# IDEA : LC 739, LC 503
class Solution(object):
    def nextGreaterElements(self, nums):
        # edge case
        if not nums:
            return
        _len = len(nums)
        # note : we init res as [-1] * _len
        res = [-1] * _len
        # note : we use "nums = 2 * nums" to simuldate "circular array"
        nums = 2 * nums
        stack = [] # [[idx, val]]
        for idx, val in enumerate(nums):
            while stack and stack[-1][1] < val:
                _idx, _val = stack.pop(-1)
                """
                NOTE !!!
                    -> we get remainder via "_idx % _len" for handling idx issue
                      (since we made nums = 2 * nums earlier)
                """
                res[_idx % _len] = val
            stack.append([idx, val])
        return res
```

### 1-1-3) get `non balanced` String — LC 1963
```python
# LC 1963. Minimum Number of Swaps to Make the String Balanced

# NOTE !!! below trick will ONLY collect not Balanced ], [
#          -> e.g. "]][[" or "]]][[["
 
s = "]]][[["
stack = []
for i in range(len(s)):
    # NOTE HERE !!!
    if stack and s[i] == "]":
        stack.pop(-1)
    else:
        stack.append()
print (stack)
```

### 1-1-4) Delay-Insert to Stack (act on `pre_op`, not current op) — LC 227

**Key Insight**: When scanning an expression left-to-right, we can't decide what to do with the current number until we see the **next** operator — so we **delay** the push until then, acting on `pre_op`.

**Why?**
- `+` / `-` (low precedence): push `±num` directly, defer to final `sum(stack)`
- `*` / `/` (high precedence): pop last value and combine immediately — but we only know this **after** `num` is fully built and the next operator arrives

**Setup**:
- `pre_op = '+'` (init) — makes the first number push as positive automatically
- `num` accumulates digits; trigger fires on operator or end-of-string

**Visual trace — `"3+2*2"` → 7**:
```
char  num  trigger?  pre_op  action               stack
'3'   3    no        '+'     —                    []
'+'   3    YES       '+'     push(3)  → pre_op='+' [3]
'2'   2    no        '+'     —                    [3]
'*'   2    YES       '+'     push(2)  → pre_op='*' [3, 2]
'2'   2    YES(end)  '*'     pop()→2, push(2*2=4)  [3, 4]
sum([3, 4]) = 7 ✓
```

**Template**:
```python
# LC 227, 394
stack = []
pre_op = '+'   # init to '+' so first num is pushed as-is
num = 0
for i, each in enumerate(s):
    if each.isdigit():
        num = 10 * num + int(each)
    # trigger: delay until we see the NEXT operator (or end of string)
    if i == len(s) - 1 or each in "+-*/":
        if pre_op == "+":
            stack.append(num)
        elif pre_op == "-":
            stack.append(-num)
        elif pre_op == "*":
            stack.append(stack.pop() * num)
        elif pre_op == "/":
            stack.append(int(float(stack.pop()) / num))  # truncate toward zero
        pre_op = each   # save current op → becomes pre_op next iteration
        num = 0
return sum(stack)
```

**Related problems using this pattern**:

| LC | Problem | Variation |
|----|---------|-----------|
| 227 | Basic Calculator II | `+-*/`, no parentheses |
| 224 | Basic Calculator I | `+-()`, no `*/` |
| 772 | Basic Calculator III | `+-*/()` combined |
| 394 | Decode String | `pre_op` tracks repeat count before `[` |

## 2) LeetCode Examples

### 2-1) Decode String — LC 394
```python
# LC 394 Decode String
# V0
# IDEA : STACK
# NOTE : treat before cases separately
#        1) isdigit
#        2) isalpha
#        3) "["
#        4) "]"
# and define num = 0 for dealing with "100a[b]", "10abc" cases
class Solution:
    def decodeString(self, s):
        num = 0
        string = ''
        stack = []
        """
        NOTE : we deal with 4 cases
            1) digit
            2) "["
            3) alphabet
            4) "]"

        NOTE :
            we use pre_num, pre_string for dealing with previous result
        """
        for c in s:
            # case 1) : digit
            if c.isdigit():
                num = num*10 + int(c)
            # case 2) : "["
            elif c == "[":
                stack.append(string)
                stack.append(num)
                string = ''
                num = 0
            # case 3) : alphabet
            elif c.isalpha():
                string += c
            # case 4) "]"
            elif c == ']':
                pre_num = stack.pop()
                pre_string = stack.pop()
                string = pre_string + pre_num * string
        return string
```

```java
// java
// LC 394 Decode String

/**
 * Problem: Given an encoded string, return its decoded string.
 *
 * Encoding rule: k[encoded_string] means repeat encoded_string k times
 *
 * Examples:
 * - "3[a]2[bc]" → "aaabcbc"
 * - "3[a2[c]]" → "accaccacc"
 * - "2[abc]3[cd]ef" → "abcabccdcdcdef"
 *
 * Key Insight:
 * - Use stack to handle nested brackets
 * - Process 4 cases: digit, '[', letter, ']'
 * - Build number incrementally (e.g., "100" = 1*10 + 0*10 + 0)
 * - On ']': pop count and previous string, build result
 *
 * Time: O(maxK * N) where maxK is max k value and N is length of decoded string
 * Space: O(N) for the stack
 */

// V0
// IDEA: STACK + 4 CASES (digit, '[', letter, ']')
public String decodeString(String s) {
    if (s == null || s.length() == 0) {
        return "";
    }

    /**
     * NOTE !!!
     * Stack stores alternating pattern:
     * - String (previous accumulated string)
     * - Integer (repeat count)
     * - String (next accumulated string)
     * - Integer (next repeat count)
     * ...
     *
     * Example for "3[a2[c]]":
     * When processing '2[c]':
     *   Stack bottom: ["", 3, "a", 2] Stack top
     */
    Stack<Object> stack = new Stack<>();

    int num = 0;              // Current number being built
    String currentString = ""; // Current string being built

    for (char c : s.toCharArray()) {

        /**
         * Case 1: Digit
         * Build multi-digit numbers (e.g., "100")
         */
        if (Character.isDigit(c)) {
            num = num * 10 + (c - '0');
        }

        /**
         * Case 2: '['
         * Push current string and number to stack
         * Reset for new nested level
         */
        else if (c == '[') {
            // Push current string first, then number
            stack.push(currentString);
            stack.push(num);

            // Reset for new level
            currentString = "";
            num = 0;
        }

        /**
         * Case 3: Letter
         * Append to current string
         */
        else if (Character.isLetter(c)) {
            currentString += c;
        }

        /**
         * Case 4: ']'
         * Pop count and previous string
         * Build repeated string and concatenate
         */
        else if (c == ']') {
            // Pop in reverse order of push
            int repeatCount = (int) stack.pop();
            String prevString = (String) stack.pop();

            /**
             * NOTE !!!
             * Repeat current string repeatCount times
             * Then prepend previous string
             */
            StringBuilder temp = new StringBuilder(prevString);
            for (int i = 0; i < repeatCount; i++) {
                temp.append(currentString);
            }

            currentString = temp.toString();
        }
    }

    return currentString;
}

/**
 * Example Walkthrough: s = "3[a2[c]]"
 *
 * Step 1: c='3' (digit)
 *   num = 3
 *
 * Step 2: c='[' (open bracket)
 *   stack.push("") → stack: [""]
 *   stack.push(3)  → stack: ["", 3]
 *   currentString = "", num = 0
 *
 * Step 3: c='a' (letter)
 *   currentString = "a"
 *
 * Step 4: c='2' (digit)
 *   num = 2
 *
 * Step 5: c='[' (open bracket)
 *   stack.push("a") → stack: ["", 3, "a"]
 *   stack.push(2)   → stack: ["", 3, "a", 2]
 *   currentString = "", num = 0
 *
 * Step 6: c='c' (letter)
 *   currentString = "c"
 *
 * Step 7: c=']' (close bracket)
 *   repeatCount = stack.pop() = 2
 *   prevString = stack.pop() = "a"
 *   temp = "a" + "c" * 2 = "acc"
 *   currentString = "acc"
 *   stack: ["", 3]
 *
 * Step 8: c=']' (close bracket)
 *   repeatCount = stack.pop() = 3
 *   prevString = stack.pop() = ""
 *   temp = "" + "acc" * 3 = "accaccacc"
 *   currentString = "accaccacc"
 *   stack: []
 *
 * Result: "accaccacc"
 */

// V1
// IDEA: STACK with separate stacks for counts and strings (cleaner approach)
public String decodeString_v1(String s) {
    Stack<Integer> countStack = new Stack<>();
    Stack<String> stringStack = new Stack<>();

    String currentString = "";
    int num = 0;

    for (char c : s.toCharArray()) {
        if (Character.isDigit(c)) {
            num = num * 10 + (c - '0');
        }
        else if (c == '[') {
            // Push current state to stacks
            countStack.push(num);
            stringStack.push(currentString);

            // Reset for nested level
            currentString = "";
            num = 0;
        }
        else if (c == ']') {
            // Build decoded string for this level
            int repeatCount = countStack.pop();
            StringBuilder temp = new StringBuilder(stringStack.pop());

            for (int i = 0; i < repeatCount; i++) {
                temp.append(currentString);
            }

            currentString = temp.toString();
        }
        else {
            // Letter
            currentString += c;
        }
    }

    return currentString;
}

/**
 * Common Mistakes:
 *
 * 1. Not handling multi-digit numbers (e.g., "100[a]")
 *    ✗ num = c - '0'
 *    ✓ num = num * 10 + (c - '0')
 *
 * 2. Wrong stack push/pop order
 *    ✗ push(num, string) → pop(string, num)  // Wrong!
 *    ✓ push(string, num) → pop(num, string)  // Correct LIFO
 *
 * 3. Forgetting to reset num and currentString after '['
 *    ✗ Only reset one of them
 *    ✓ Reset both: num = 0; currentString = "";
 *
 * 4. Not handling strings outside brackets (e.g., "2[abc]3[cd]ef")
 *    ✓ Continue building currentString for letters outside brackets
 *
 * 5. Using Stack<Object> without proper casting
 *    ✓ Use separate stacks (countStack, stringStack) for type safety
 */

/**
 * Interview Tips:
 *
 * 1. Clarify constraints:
 *    - Is input always valid? (no unmatched brackets)
 *    - Max value of k? (affects overflow considerations)
 *
 * 2. Edge cases to test:
 *    - No brackets: "abc" → "abc"
 *    - Nested brackets: "2[a2[b]]" → "abbabb"
 *    - Multi-digit numbers: "100[a]"
 *    - Mixed: "2[abc]3[cd]ef" → "abcabccdcdcdef"
 *
 * 3. Follow-up questions:
 *    - What if string is invalid? (add validation)
 *    - Can we decode in-place? (no, need stack for nesting)
 *    - How to handle very large k values? (streaming approach)
 */
```

### 2-2) Next Greater Element I — LC 496
```python
# 496. Next Greater Element I

# V0
# IDEA : STACK (for + while loop)
class Solution(object):
    def nextGreaterElement(self, nums1, nums2):
        # edge case
        if not nums2 or (not nums1 and not nums2):
            return nums1
        res = []
        # NOTE : the trick here (found as a flag)
        found = False
        for i in nums1:
            #print ("i = " + str(i) + " res = " + str(res))
            idx = nums2.index(i)
            # start from "next" element in nums2
            # here we init tmp _nums2
            _nums2 = nums2[idx+1:]
            # while loop keep pop _nums2 for finding the next bigger element
            while _nums2:
                tmp = _nums2.pop(0)
                # if found, then append to res, and break the while loop directly
                if tmp > i:
                    found = True
                    res.append(tmp)
                    break
            # if not found, we need to append -1 to res
            if not found:
                res.append(-1)
            found = False
        return res

# V0
# IDEA : double for loop (one of loops is INVERSE ORDERING) + case conditions op
class Solution(object):
    def nextGreaterElement(self, nums1, nums2):
        res = [None for _ in range(len(nums1))]
        tmp = []
        for i in range(len(nums1)):
            ### NOTE : from last idx to 0 idx. (Note the start and end idx)
            for j in range(len(nums2)-1, -1, -1):
                #print ("i = " + str(i) + " j = " + str(j) + " tmp = " + str(tmp))

                # case 1) No "next greater element" found in nums2
                if not tmp and nums2[j] == nums1[i]:
                    res[i] = -1
                    break
                # case 2) found "next greater element" in nums2, keep inverse looping
                elif nums2[j] > nums1[i]:
                    tmp.append(nums2[j])
                # case 3) already reach same element in nums2 (as nums1), pop "last" "next greater element", paste to res, break the loop
                elif tmp and nums2[j] == nums1[i]:
                    _tmp = tmp.pop(-1)
                    res[i] = _tmp
                    tmp = []
                    break
        return res
```

```java
// java
// LC 496
// V0
// IDEA : STACK
// https://www.youtube.com/watch?v=68a1Dc_qVq4
/** NOTE !!!
 *
 *  nums1 is "sub set" of nums2,
 *  so all elements in nums1 are in nums2 as well
 *  and in order to find next greater element in nums1 reference nums2
 *  -> ACTUALLY we only need to check nums2
 *  -> then append result per element in nums1
 */
public int[] nextGreaterElement(int[] nums1, int[] nums2) {

    if (nums1.length == 1 && nums2.length == 1){
        return new int[]{-1};
    }

    /**
     *  NOTE !!!
     *  we use map " collect next greater element"
     *  map definition :  {element, next-greater-element}
     */
    Map<Integer, Integer> map = new HashMap<>();
    Stack<Integer> st = new Stack<>();

    for (int x : nums2){
        /**
         *  NOTE !!!
         *   1) use while loop
         *   2) while stack is NOT null and stack "top" element is smaller than current element (x) is nums2
         *
         *   -> found "next greater element", so update map
         */
        while(!st.isEmpty() && st.peek() < x){
            int cur = st.pop();
            map.put(cur, x);
        }
        /** NOTE !!! if not feat above condition, we put element to stack */
        st.add(x);
    }

    //System.out.println("map = " + map);
    int[] res = new int[nums1.length];
    // fill with -1 for element without next greater element
    Arrays.fill(res, -1);
    for (int j = 0; j < nums1.length; j++){
        if(map.containsKey(nums1[j])){
            res[j] = map.get(nums1[j]);
        }
    }

    //System.out.println("res = " + res);
    return res;
}
```

### 2-3) Next Greater Element II — LC 503
```python
# LC 503. Next Greater Element II

# V0'
# IDEA : LC 739
class Solution(object):
    def nextGreaterElements(self, nums):
        # edge case
        if not nums:
            return
        _len = len(nums)
        # note : we init res as [-1] * _len
        res = [-1] * _len
        # note : we use "nums = 2 * nums" to simuldate "circular array"
        nums = 2 * nums
        stack = [] # [[idx, val]]
        for idx, val in enumerate(nums):
            while stack and stack[-1][1] < val:
                _idx, _val = stack.pop(-1)
                """
                NOTE !!!
                    -> we get remainder via "_idx % _len" for handling idx issue
                      (since we made nums = 2 * nums earlier)
                """
                res[_idx % _len] = val
            stack.append([idx, val])
        return res

# V0'
# IDEA : STACK + circular loop handling
class Solution:
    def nextGreaterElements(self, nums):
        ### NOTE : since we can search nums circurly, 
        #  -> so here we make a new array (augLst = nums + nums) for that     
        augLst = nums + nums
        stack = []
        # init ans
        res = [-1] * len(nums)
        ### NOTE : we looping augLst with inverse order
        for i in range(len(augLst)-1, -1, -1):
            ### NOTE : if stack and last value in stack smaller than augLst[i], we pop last value from stack
            while stack and stack[-1] <= augLst[i]:
                stack.pop()
            ### NOTE : the remaining element in stack must fit the condition, so we append it to res
            #   -> note : append to `i % len(nums)` idx in res
            if stack:
                res[i % len(nums)] = stack[-1]
            ### NOTE : we also need to append augLst[i] to stack
            stack.append(augLst[i])
        return res
```

### 2-4) Daily Temperatures — LC 739
```python
# LC 739. Daily Temperatures
# V0
# IDEA : STACK
# DEMO 
#     ...: T=[73, 74, 75, 71, 69, 72, 76, 73]
#     ...: s=Solution()
#     ...: r= s.dailyTemperatures(T)
#     ...: print(r)
#     ...: 
# i : 1, stack : [(73, 0)], res : [0, 0, 0, 0, 0, 0, 0, 0]
# i : 2, stack : [(74, 1)], res : [1, 0, 0, 0, 0, 0, 0, 0]
# i : 5, stack : [(75, 2), (71, 3), (69, 4)], res : [1, 1, 0, 0, 0, 0, 0, 0]
# i : 5, stack : [(75, 2), (71, 3)], res : [1, 1, 0, 0, 1, 0, 0, 0]
# i : 6, stack : [(75, 2), (72, 5)], res : [1, 1, 0, 2, 1, 0, 0, 0]
# i : 6, stack : [(75, 2)], res : [1, 1, 0, 2, 1, 1, 0, 0]
# [1, 1, 4, 2, 1, 1, 0, 0]
class Solution(object):
    def dailyTemperatures(self, T):
        N = len(T)
        stack = []
        res = [0] * N
        ### NOTE : we only use 1 for loop in this problem
        for i, t in enumerate(T):
            # if stack is not bland and last temp < current tmpe
            # -> pop the stack (get its temp)
            # -> and calculate the difference 
            ### BEWARE "while" op 
            while stack and stack[-1][0] < t:
                oi = stack.pop()[1]
                res[oi] = i - oi
            # no matter any case, we have to insert current temp into stack anyway
            # since the result (next higher temp) is decided by the coming temp, rather than current temp 
            stack.append((t, i))
        return res
```

```java
// java
// LC 739

// V0
// IDEA : STACK (MONOTONIC STACK)
// LC 496
public int[] dailyTemperatures(int[] temperatures) {

    if (temperatures.length == 1){
        return temperatures;
    }

    /**
     *  Stack :
     *
     *   -> cache elements (temperature) that DOESN'T have (NOT found) next warmer temperature yet
     *   -> structure : stack ([temperature, idx])
     */
    Stack<List<Integer>> st = new Stack<>(); // element, idx
    /** NOTE !!!
     *
     *    can't use map, since there will be "duplicated" temperature
     *   -> which will cause different val has same key (hashMap key)
     */
    //Map<Integer, Integer> map = new HashMap<>(); // {temperature : idx-of-next-warmer-temperature}
    /**
     *  NOTE !!!
     *
     *   we use nextGreater collect answer,
     *   -> idx : temperature, val : idx-of-next-warmer-temperature
     */
    int[] nextGreater = new int[temperatures.length];
    Arrays.fill(nextGreater, 0); // idx : temperature, val : idx-of-next-warmer-temperature
    for (int j = 0; j < temperatures.length; j++){
        int x = temperatures[j];
        /**
         *  NOTE !!!
         *   1) while loop
         *   2) stack is NOT empty
         *   3) cache temperature smaller than current temperature
         *
         *   st.peek().get(0) is cached temperature
         */
        while (!st.isEmpty() && st.peek().get(0) < x){
            /**
             *  st.peek().get(1) is idx
             *
             */
            nextGreater[st.peek().get(1)] = j - st.peek().get(1);
            st.pop();
        }
        List<Integer> cur = new ArrayList<>();
        cur.add(x); // element
        cur.add(j); // idx
        st.add(cur);
    }

    //System.out.println("nextGreater = " + nextGreater);
    return nextGreater;
}
```

### 2-5) Basic Calculator I — LC 224
```python
# LC 224 Basic Calculator
# V0'
# IDEA : STACK
# https://leetcode.com/problems/basic-calculator/solution/
class Solution:
    def calculate(self, s):

        stack = []
        operand = 0
        res = 0 # For the on-going result
        sign = 1 # 1 means positive, -1 means negative  

        for ch in s:
            if ch.isdigit():

                # Forming operand, since it could be more than one digit
                operand = (operand * 10) + int(ch)

            elif ch == '+':

                # Evaluate the expression to the left,
                # with result, sign, operand
                res += sign * operand

                # Save the recently encountered '+' sign
                sign = 1

                # Reset operand
                operand = 0

            elif ch == '-':

                res += sign * operand
                sign = -1
                operand = 0

            elif ch == '(':

                # Push the result and sign on to the stack, for later
                # We push the result first, then sign
                stack.append(res)
                stack.append(sign)

                # Reset operand and result, as if new evaluation begins for the new sub-expression
                sign = 1
                res = 0

            elif ch == ')':

                # Evaluate the expression to the left
                # with result, sign and operand
                res += sign * operand

                # ')' marks end of expression within a set of parenthesis
                # Its result is multiplied with sign on top of stack
                # as stack.pop() is the sign before the parenthesis
                res *= stack.pop() # stack pop 1, sign

                # Then add to the next operand on the top.
                # as stack.pop() is the result calculated before this parenthesis
                # (operand on stack) + (sign on stack * (result from parenthesis))
                res += stack.pop() # stack pop 2, operand

                # Reset the operand
                operand = 0

        return res + sign * operand
```

### 2-5') Basic Calculator II — LC 227

> **Pattern**: Delay-Insert (see [1-1-4](#1-1-4-delay-insert-to-stack-act-on-pre_op-not-current-op----lc-227)) — push `num` only when the *next* operator arrives; act on `pre_op` at that point.

```python
# python
# LC 227. Basic Calculator II, LC 224. Basic Calculator
# V0
# IDEA : STACK + delay-insert (pre_op pattern)
class Solution:
    def calculate(self, s):
        stack = []
        # NOTE THIS !!
        pre_op = '+'
        num = 0
        for i, each in enumerate(s):
            # case 1) : digit
            if each.isdigit():
                num = 10 * num + int(each)  # the way to deal with number like "100", "10"... 
            if i == len(s) - 1 or each in '+-*/':
                """
                NOTE !!! : we deal with "pre_op" (rather than current op)
                """
                # case 2) : "+"
                if pre_op == '+':
                    stack.append(num)
                # case 3) : "-"    
                elif pre_op == '-':
                    stack.append(-num)
                # case 3) : "*" 
                elif pre_op == '*':
                    stack.append(stack.pop() * num)
                # case 3) : "/" 
                elif pre_op == '/':
                    top = stack.pop()
                    if top < 0:
                        stack.append(int(top / num))
                    else:
                        stack.append(top // num)
                # NOTE this!
                pre_op = each
                num = 0
        return sum(stack)

# Algorithm book (labu) p. 342
# IDEA : STACK + RECURSIVE
# TODO : fix output format
class Solution(object):
    def calculate(self, s):

        def helper(s):
            stack = []
            sign = '+'
            num = 0

            while len(s) > 0:
                c = s.pop(0)
                if c.isdigit():
                    num = 10 * num + int(c)
                    """
                    do recursive when meet "("
                    """
                    if c == '(':
                        num = helper(s)
                    if (not c.isdigit() and c != ' ') or len(s) == 0:
                        if sign == '+':
                            stack.append(num)
                        elif sign == '-':
                            stack.append(-num)
                        elif sign == '*':
                            stack[-1] = stack[-1] * num
                        elif sign == '/':
                            stack[-1] = int(stack[-1] / float(num))
                        num = 0
                        sign = c
                    """
                    end recursive when meet ")"
                    """
                    if c == ')':
                        break
            return sum(stack)

        # run the helper func    
        return helper(list(s))

# V1
# python 3
class Solution:
    def calculate(self, s):
        stack = []
        pre_op = '+'
        num = 0
        for i, each in enumerate(s):
            if each.isdigit():
                num = 10 * num + int(each)  # the way to deal with number like "100", "10"... 
            if i == len(s) - 1 or each in '+-*/':
                if pre_op == '+':
                    stack.append(num)
                elif pre_op == '-':
                    stack.append(-num)
                elif pre_op == '*':
                    stack.append(stack.pop() * num)
                elif pre_op == '/':
                    top = stack.pop()
                    if top < 0:
                        stack.append(int(top / num))
                    else:
                        stack.append(top // num)
                pre_op = each
                num = 0
        return sum(stack)
```

### 2-5'') Universal Calculator — LC 224 / 227 / 772 ⭐⭐⭐⭐⭐

> **One algorithm for all three calculator problems**: `+-` only (224), `+-*/` no parens (227), and `+-*/()` combined (772). Handles operator **precedence** with a stack and **parentheses** with recursion.

**Core Idea** — combine two independent tricks that each solve half the problem:

| Sub-problem | Trick | How it shows up |
|-------------|-------|-----------------|
| **Precedence** (`*` / `/` bind tighter than `+` / `-`) | **Delay-Insert on `pre_op`** (see [1-1-4](#1-1-4-delay-insert-to-stack-act-on-pre_op-not-current-op----lc-227)) | `+`/`-` push the signed number onto the stack (defer); `*`/`/` immediately pop-and-combine with the top. Final answer = `sum(stack)`. |
| **Parentheses** (a sub-expression evaluated first) | **Recursion** — a `(` opens a fresh scope, a `)` closes it | On `(`, recurse on the *same* queue; the recursive call consumes up to its matching `)` and returns the sub-total, which is treated as a plain `curr_num`. |

**Why the stack handles precedence for free:** additive terms are deferred as signed values (`+num` → push `num`, `-num` → push `-num`), while multiplicative operators eat the previous term on the spot (`stack[-1] *= num`). Because `*`/`/` mutate the top *before* it is ever summed, `sum(stack)` at the end naturally respects precedence — e.g. `2 + 3 * 4` builds `[2, 12]` → `14`, not `20`.

**Why we act on `pre_op`, not the current char:** when we read an operator (or hit `)` / end-of-input) it marks the *end* of the number we were building, so we apply the operator that came *before* that number. `pre_op` is initialized to `'+'` so the very first number is simply pushed.

**Why a `deque` + recursion cleanly handles parens:** `popleft()` consumes characters left-to-right and the queue is **shared across recursive calls**. When `helper` recurses on `(`, the child keeps popping from the *same* queue and `break`s on `)`, so the parent resumes exactly after the matching `)`. This is what upgrades the LC 227 delay-insert solution into a full LC 772 solver.

```python
# python
# LC 224 / 227 / 772 — universal basic calculator
# IDEA: deque + recursion (parentheses) + delay-insert on pre_op (precedence)
# time = O(n), space = O(n)  (stack + recursion depth)
import collections

class Solution(object):
    def calculate(self, s):
        # strip spaces, scan left-to-right with a shared queue
        queue = collections.deque(s.replace(" ", ""))

        def helper(q):
            stack = []
            curr_num = 0
            op = '+'                     # operator that precedes curr_num; '+' by default

            while q:
                char = q.popleft()

                if char.isdigit():
                    curr_num = curr_num * 10 + int(char)   # build multi-digit number
                elif char == '(':
                    curr_num = helper(q)   # RECURSE: fully evaluate the parenthesised scope

                # flush when we see an operator, a ')', or run out of input
                if char in "+-*/" or char == ')' or not q:
                    if op == '+':
                        stack.append(curr_num)
                    elif op == '-':
                        stack.append(-curr_num)
                    elif op == '*':
                        stack.append(stack.pop() * curr_num)
                    elif op == '/':
                        # truncate toward zero (Python // floors, so divide as float)
                        stack.append(int(float(stack.pop()) / curr_num))
                    curr_num = 0
                    op = char            # remember this operator for the next number

                if char == ')':
                    break                # end of this scope → return sub-total to caller

            return sum(stack)

        return helper(queue)
```

**How it degrades to each problem:**

| LC | Chars present | What the algo does |
|----|---------------|--------------------|
| 224 | `+ - ( )` | recursion + push/negate only; `*`/`/` branches never fire |
| 227 | `+ - * / ` | never recurses (no `(`); pure delay-insert precedence |
| 772 | `+ - * / ( )` | both mechanisms active — the general case |

**Gotcha — integer division truncates toward zero:** Python's `//` floors (`-7 // 2 == -4`), but these problems require truncation toward zero (`-7 / 2 == -3`). Using `int(float(stack.pop()) / curr_num)` gives the correct behavior for negative intermediates.

### 2-5) Sum of Subarray Minimums — LC 907
```python
# LC 907. Sum of Subarray Minimums
# V0
# IDEA :  increasing stacks
class Solution:
    def sumSubarrayMins(self, A):
        n, mod = len(A), 10**9 + 7
        left, right, s1, s2 = [0] * n, [0] * n, [], []

        for i in range(n):
            count = 1
            while s1 and s1[-1][0] > A[i]:
                count += s1.pop()[1]
            left[i] = count
            s1.append([A[i], count])

        for i in range(n)[::-1]:
            count = 1
            while s2 and s2[-1][0] >= A[i]:
                count += s2.pop()[1]
            right[i] = count
            s2.append([A[i], count])
        return sum(a * l * r for a, l, r in zip(A, left, right)) % mod
```

### 2-6) Asteroid Collision — LC 735
```python
# LC 735. Asteroid Collision
# V0
class Solution(object):
    def asteroidCollision(self, asteroids):
        ans = []
        for new in asteroids:
            while ans and new < 0 < ans[-1]:
                if ans[-1] < -new:
                    ans.pop()
                    continue
                elif ans[-1] == -new:
                    ans.pop()
                break
            else:
                ans.append(new)
        return ans
```

### 2-7) Remove All Adjacent Duplicates in String — LC 1047
```python
# LC 1047. Remove All Adjacent Duplicates In String
# V0
# IDEA : STACK
class Solution:
     def removeDuplicates(self, x):
          # edge
          if not x:
            return
          stack = []
          """
          NOTE !!! below op
          """
          for i in range(len(x)):
               # NOTE !!! : trick here : if stack last element == current x's element
               #       -> we pop last stack element
               #       -> and NOT add current element
               if stack and stack[-1] == x[i]:
                    stack.pop(-1)
               # if stack last element != current x's element
               #      -> we append x[i]
               else:
                    stack.append(x[i])
          return "".join(stack)

# V0'
# IDEA : TWO POINTERS
#      -> pointers : end, c
class Solution:
     def removeDuplicates(self, S):
            end =  -1
            a = list(S)
            for c in a:
                if end >= 0 and a[end] == c:
                    end -= 1
                else:
                    end += 1
                    a[end] = c
            return ''.join(a[: end + 1])
```

### 2-8) Remove All Adjacent Duplicates in String II — LC 1209

**Pattern: Stack with Character-Count Pairs**

This pattern uses `Stack<int[]>` or `Stack<[char, count]>` to efficiently track consecutive duplicates and their counts. It's particularly useful when you need to remove k consecutive equal elements.

**When to Use This Pattern:**

1. **Problem mentions "k consecutive/adjacent equal elements"**
   - Remove k duplicates: LC 1209
   - Count k consecutive: various counting problems

2. **Need to track both character AND its frequency**
   - Can't just track character (need count for k-removal)
   - Can't just track count (need to know which character)

3. **Removal happens when count reaches threshold k**
   - Unlike LC 1047 (k=2, simple stack.pop()), k is variable
   - Need to track partial progress (e.g., "aa" in "aaab" with k=3)

4. **One-pass solution required with O(n) space**
   - Stack stores compressed form: {char, count}
   - More efficient than storing all characters

**Recognition Signs:**
- ✓ Keywords: "k adjacent", "k consecutive", "k duplicates"
- ✓ Remove/count when reaching exactly k occurrences
- ✓ Need to handle partial sequences (count < k)
- ✓ Input constraint: k >= 2 (if k=1, different approach needed)

**Structure:**
```java
// Core data structure
Stack<int[]> stack = new Stack<>();
// Each element: {character_as_int, count}

// Or for clarity
Stack<Pair<Character, Integer>> stack = new Stack<>();
```

**Similar Problems:**
- LC 1047: Remove All Adjacent Duplicates in String (k=2 special case)
- LC 1544: Make The String Great (remove adjacent opposite case)
- LC 316: Remove Duplicate Letters (lexicographical order with stack)
- LC 394: Decode String (stack with count, but for repetition)

```python
# LC 1209. Remove All Adjacent Duplicates in String II
# V0
# IDEA : STACK
class Solution:
     def removeDuplicates(self, x, k):
          # edge case
          if not x:
            return None
          stack = []
          """
          NOTE !!!
            1) we use [[element, _count]] format for below op
            2) note the case when deal with duplicated elements

               if stack and stack[-1][0] == x[i]:
                    if stack[-1][1] < k-1:
                         stack[-1][1] += 1
                    else:
                         stack.pop(-1)
          """
          for i in range(len(x)):
               if stack and stack[-1][0] == x[i]:
                    if stack[-1][1] < k-1:
                         stack[-1][1] += 1
                    else:
                         stack.pop(-1)
               else:
                    stack.append([x[i], 1])
          #print (">> stack = " + str(stack))
          tmp = [x[0]*x[1] for x in stack]
          #print (">> tmp = " + str(tmp))
          return "".join(tmp)

# V0'
# IDEA : STACK
# NOTE !!! we DON'T need to modify original s, (but maintain an extra stack for duplicated checks)
class Solution:
     def removeDuplicates(self, s, k):
            stack = [['#', 0]]
            for c in s:
                #print ("c = " + str(c) + " stack = " + str(stack))
                if stack[-1][0] == c:
                    stack[-1][1] += 1
                    if stack[-1][1] == k:
                        stack.pop()
                else:
                    stack.append([c, 1])
            return ''.join(c * k for c, k in stack)
```

```java
// java
// LC 1209. Remove All Adjacent Duplicates in String II

/**
 * Problem: Remove k consecutive equal characters repeatedly until no more removals possible.
 *
 * Examples:
 * - s = "deeedbbcccbdaa", k = 3
 *   "deeedbbcccbdaa" → "ddbbbdaa" (remove "eee", "ccc")
 *   "ddbbbdaa" → "dddaa" (remove "bbb")
 *   "dddaa" → "aa" (remove "ddd")
 *
 * - s = "pbbcggttciiippooaais", k = 2
 *   Output: "ps"
 *
 * Key Insight:
 * - Use Stack<int[]> to store {character, count} pairs
 * - When char matches stack top: increment count
 * - When count reaches k: pop (remove k consecutive chars)
 * - This handles cascading removals naturally
 *
 * Time: O(N) - single pass through string
 * Space: O(N) - worst case all different characters
 */

// V0
// IDEA: STACK with {char, count} pairs
/**
 * time = O(N)
 * space = O(N)
 */
public String removeDuplicates(String s, int k) {
    if (s == null || s.length() == 0 || k <= 0) {
        return s;
    }

    /**
     * NOTE !!!
     * Stack stores int array: {character_as_int, count}
     *
     * Why int[] instead of Pair<Character, Integer>?
     * - More memory efficient (no object wrapper overhead)
     * - Direct access: pair[0] = char, pair[1] = count
     * - Java doesn't have built-in Pair in older versions
     */
    Stack<int[]> st = new Stack<>();

    for (char ch : s.toCharArray()) {
        /**
         * Case 1: Character matches stack top
         * Increment the count of consecutive occurrences
         */
        if (!st.isEmpty() && st.peek()[0] == ch) {
            st.peek()[1]++;

            /**
             * NOTE !!!
             * When count reaches k, remove the entire block
             * This triggers potential cascading removals
             */
            if (st.peek()[1] == k) {
                st.pop();
            }
        }
        /**
         * Case 2: New character (different from stack top)
         * Start a new block with count = 1
         */
        else {
            st.push(new int[] { ch, 1 });
        }
    }

    /**
     * Build final string from remaining characters in stack
     * Each stack element may have count > 1
     */
    StringBuilder sb = new StringBuilder();
    for (int[] pair : st) {
        char c = (char) pair[0];
        int count = pair[1];

        // Append character 'count' times
        for (int i = 0; i < count; i++) {
            sb.append(c);
        }
    }

    return sb.toString();
}

/**
 * Example Walkthrough: s = "deeedbbcccbdaa", k = 3
 *
 * Iteration:
 * ch='d': st = [{d,1}]
 * ch='e': st = [{d,1}, {e,1}]
 * ch='e': st = [{d,1}, {e,2}]
 * ch='e': st = [{d,1}, {e,3}] → count==k, pop → st = [{d,1}]
 * ch='d': st = [{d,2}]
 * ch='b': st = [{d,2}, {b,1}]
 * ch='b': st = [{d,2}, {b,2}]
 * ch='c': st = [{d,2}, {b,2}, {c,1}]
 * ch='c': st = [{d,2}, {b,2}, {c,2}]
 * ch='c': st = [{d,2}, {b,2}, {c,3}] → pop → st = [{d,2}, {b,2}]
 * ch='b': st = [{d,2}, {b,3}] → pop → st = [{d,2}]
 * ch='d': st = [{d,3}] → pop → st = []
 * ch='a': st = [{a,1}]
 * ch='a': st = [{a,2}]
 *
 * Result: "aa"
 */

// V1
// IDEA: Two separate stacks (cleaner type safety)
/**
 * time = O(N)
 * space = O(N)
 */
public String removeDuplicates_v1(String s, int k) {
    Stack<Character> charStack = new Stack<>();
    Stack<Integer> countStack = new Stack<>();

    for (char ch : s.toCharArray()) {
        if (!charStack.isEmpty() && charStack.peek() == ch) {
            countStack.push(countStack.peek() + 1);
        } else {
            countStack.push(1);
        }

        charStack.push(ch);

        // Remove k characters when count reaches k
        if (countStack.peek() == k) {
            for (int i = 0; i < k; i++) {
                charStack.pop();
                countStack.pop();
            }
        }
    }

    // Build result
    StringBuilder sb = new StringBuilder();
    while (!charStack.isEmpty()) {
        sb.append(charStack.pop());
    }

    return sb.reverse().toString();
}

/**
 * Common Mistakes:
 *
 * 1. Forgetting to check !st.isEmpty() before peek()
 *    ✗ if (st.peek()[0] == ch)  // NPE if stack empty!
 *    ✓ if (!st.isEmpty() && st.peek()[0] == ch)
 *
 * 2. Removing only one character instead of the whole block
 *    ✗ if (st.peek()[1] == k) st.peek()[1] = 0;  // Wrong!
 *    ✓ if (st.peek()[1] == k) st.pop();
 *
 * 3. Not handling count correctly when building result
 *    ✗ sb.append((char) pair[0]);  // Only appends once!
 *    ✓ for (int i = 0; i < count; i++) sb.append(c);
 *
 * 4. Using wrong data structure (List instead of Stack)
 *    ✗ List doesn't support peek() efficiently
 *    ✓ Stack or Deque for O(1) peek/pop
 */

/**
 * Interview Tips:
 *
 * 1. Clarify edge cases:
 *    - What if k > s.length()? (no removal possible)
 *    - What if k == 1? (all characters removed)
 *    - Empty string input?
 *
 * 2. Discuss trade-offs:
 *    - Stack<int[]> vs two separate stacks
 *      • int[]: more memory efficient, less readable
 *      • Two stacks: cleaner, type-safe, slightly more space
 *
 * 3. Follow-up optimizations:
 *    - Can we do it in-place? (tricky, but possible with two pointers)
 *    - What if k is very large? (same approach works)
 *    - What if we need to track which removals were made? (add to result list)
 *
 * 4. Related patterns:
 *    - LC 1047 (k=2): simpler, can use single stack
 *    - LC 394 (Decode String): similar stack with count pattern
 *    - LC 316 (Remove Duplicate Letters): stack with different removal criteria
 */
```

### 2-9) Simplify Path — LC 71
```python
# LC 71. Simplify Path

# V0
# IDEA : STACK
class Solution:
    def simplifyPath(self, path: str) -> str:
        s = path.split('/')
        result = []
        for i in range(len(s)):
            if s[i] and s[i] != '.' and s[i]!='/' and s[i]!='..':
                result.append(s[i])
            elif s[i] == '..':
                if result:
                    result.pop()
        
        return "/"+"/".join(result)
```

### 2-10) Min Stack — LC 155

**Pattern: 2 Stacks (main stack + min-tracking stack)**

```
Key Insight:
  minStack does NOT store elements in sorted order.
  Instead, minStack[i] stores the minimum value seen
  in the main stack up to position i.

  -> This lets getMin() return the current minimum in O(1)
     by simply reading minStack[-1] (the top).

  Example: push -2, 0, -3
    stack    = [-2,  0, -3]
    minStack = [-2, -2, -3]   ← each entry is min-so-far, not sorted elements

  After pop():
    stack    = [-2,  0]
    minStack = [-2, -2]       ← getMin() correctly returns -2

When to Use:
  - Need O(1) getMin() on a stack
  - minStack mirrors the main stack size (one entry per push/pop)
  - Both stacks are always the same length
```

```python
# LC 155. Min Stack
# V0
# IDEA: 2 STACKS
class MinStack(object):

    def __init__(self):
        self.stack = []
        self.minStack = []

    def push(self, val):
        self.stack.append(val)
        # minStack tracks running minimum, NOT sorted elements
        if not self.minStack:
            self.minStack.append(val)
        else:
            self.minStack.append(min(val, self.minStack[-1]))

    def pop(self):
        # both stacks must stay in sync — always pop together
        self.minStack.pop()
        return self.stack.pop()

    def top(self):
        return self.stack[-1]

    def getMin(self):
        # top of minStack is always the current minimum — O(1)
        return self.minStack[-1]
```

```python
# V1: single stack storing (value, current_min) tuples
class MinStack(object):

    def __init__(self):
        self.stack = []

    def push(self, x):
        if not self.stack:
            self.stack.append((x, x))
        else:
            self.stack.append((x, min(x, self.stack[-1][1])))

    def pop(self):
        self.stack.pop()

    def top(self):
        return self.stack[-1][0]

    def getMin(self):
        return self.stack[-1][1]
```

### 2-11) Sum of Subarray Ranges — LC 2104
```python
# LC 2104. Sum of Subarray Ranges
# NOTE : there are also brute force, 2 pointers ... approaches
# V0'
# IDEA : monotonic stack
# https://zhuanlan.zhihu.com/p/444725220
class Solution:
    def subArrayRanges(self, nums):
        A, s, res = [-float('inf')] + nums + [-float('inf')], [], 0
        for i, num in enumerate(A):
            while s and num < A[s[-1]]:
                j = s.pop()
                res -= (i - j) * (j - s[-1]) * A[j]
            s.append(i)
        A, s = [float('inf')] + nums + [float('inf')], []
        for i, num in enumerate(A):
            while s and num > A[s[-1]]:
                j = s.pop()
                res += (i - j) * (j - s[-1]) * A[j]
            s.append(i)
        return res 
```

### 2-11) Largest Rectangle in Histogram — LC 84

```python
# LC 84. Largest Rectangle in Histogram
# python
# V1'''
# IDEA : STACK
# https://leetcode.com/problems/largest-rectangle-in-histogram/solution/
class Solution:
    def largestRectangleArea(self, heights: List[int]) -> int:
        stack = [-1]
        max_area = 0
        for i in range(len(heights)):
            while stack[-1] != -1 and heights[stack[-1]] >= heights[i]:
                current_height = heights[stack.pop()]
                current_width = i - stack[-1] - 1
                max_area = max(max_area, current_height * current_width)
            stack.append(i)

        while stack[-1] != -1:
            current_height = heights[stack.pop()]
            current_width = len(heights) - stack[-1] - 1
            max_area = max(max_area, current_height * current_width)
        return max_area
```

### 2-12) Remove Duplicate Letters — LC 316

```java
// java
// LC 316

/**
*  NOTE
*
*  Lexicographically Smaller
*
* A string a is lexicographically smaller than a
* string b if in the first position where a and b differ,
* string a has a letter that appears earlier in the alphabet
* than the corresponding letter in b.
* If the first min(a.length, b.length) characters do not differ,
* then the shorter string is the lexicographically smaller one.
*
*/

// V0-1
// IDEA: STACK (fixed by gpt)
// Time: O(n) — one pass over the string and each character is pushed/popped at most once.
// Space: O(1) — constant space for 26 characters (seen, freq, stack)
/**
* 📌 Example Walkthrough
*
* Input: "cbacdcbc"
*    1.  'c' → Stack: ["c"]
*    2.  'b' < 'c' and 'c' still appears → pop 'c', push 'b'
*    3.  'a' < 'b' → pop 'b', push 'a'
*    4.  'c' > 'a' → push 'c'
*    5.  'd' > 'c' → push 'd'
*    6.  'c' already seen → skip
*    7.  'b' > 'd' → push 'b'
*    8.  'c' > 'b' → push 'c'
*
* Final stack: ['a', 'c', 'd', 'b']
* Lexicographically smallest valid string: "acdb"
*
*/
public String removeDuplicateLetters_0_1(String s) {
  if (s == null || s.length() == 0) {
      return "";
  }

/**
 *  •   freq: array to count how many times each letter appears in s.
 *  •   We use c - 'a' to map each character to index 0–25 ('a' to 'z').
 *  •   This helps us later determine if we can remove a character and see it again later.
 */
int[] freq = new int[26]; // frequency of each character
  for (char c : s.toCharArray()) {
      freq[c - 'a']++;
  }

/**
 *  •   Tracks which characters have already been added to the result.
 *  •   This ensures we only include each character once.
 *
 *
 *  NOTE !!! sean is a `boolean` array
 */
boolean[] seen = new boolean[26]; // whether character is in stack/result

/** NOTE !!!
 *
 *  we init stack here
 *
 *
 *  •   This stack is used to build the final result.
 *  •   We’ll maintain characters in order and manipulate
 *      the top to maintain lexicographical order.
 */
/**
 *  NOTE !!!
 *
 *   use `STACK`, but NOT use `PQ`
 *
 */
Stack<Character> stack = new Stack<>();

/**
 *  •   Iterate through the string one character at a time.
 *  •   Since we’ve now processed c, decrement its frequency count.
 */
for (char c : s.toCharArray()) {
      freq[c - 'a']--; // reduce frequency, since we're processing this char

    /**
     *  •   If we’ve already added this character to the result,
     *      skip it — we only want one occurrence of each letter.
     */
      if (seen[c - 'a']) {
          continue; // already added, skip
      }

  /** NOTE !!!
   *
   * Now we’re checking:
   *
   *    •   Is the stack NOT empty?
   *
   *    •   Is the current character c lexicographically
   *        smaller than the character at the top of the stack?
   *
   *    •   Does the character at the top of the stack still
   *        appear later (i.e., its freq > 0)?
   *
   * If yes to all, we can:
   *
   *    •   pop it from the result,
   *
   *    •   and add it later again in a better
   *        position (lexicographically smaller order).
   */
  // remove characters that are bigger than current AND appear later again
  while (!stack.isEmpty() && c < stack.peek() && freq[stack.peek() - 'a'] > 0) {
          /**
           *
           *    Remove the character from the stack,
           *    and mark it as not seen so it can be added again later.
           */
          char removed = stack.pop();
          seen[removed - 'a'] = false;
      }

  /**
   *    •   Push the current character c to the stack,
   *    •   And mark it as seen (i.e., already in the result).
   */
  stack.push(c);
  seen[c - 'a'] = true;
  }

  // build result from stack
  StringBuilder sb = new StringBuilder();
  for (char c : stack) {
      sb.append(c);
  }

  return sb.toString();
}
```

### 2-13) Remove K Digits — LC 402

```java
// java
// LC 402. Remove K Digits

/**
 * Problem: Given a non-negative integer num and an integer k,
 * return the smallest possible integer after removing k digits from num.
 *
 * Key Insight:
 * To make the number as small as possible, we want smaller digits
 * at the beginning (most significant positions).
 *
 * Greedy Strategy:
 * - Use a monotonic increasing stack
 * - If current digit is smaller than stack top, pop the larger digit
 * - Continue popping while k > 0 and current digit < stack top
 * - This ensures we remove larger digits from higher positions
 *
 * Time: O(N) - each digit pushed/popped at most once
 * Space: O(N) - stack size
 */

// V0-1
// IDEA: MONOTONIC STACK (increasing)
public String removeKdigits(String num, int k) {
    int n = num.length();
    if (k == n)
        return "0";

    // Use Deque as stack for efficient operations
    Deque<Character> stack = new ArrayDeque<>();

    for (int i = 0; i < n; i++) {
        char digit = num.charAt(i);

        /**
         * NOTE !!!
         * While we can still remove digits (k > 0)
         * and current digit is smaller than stack top,
         * pop the stack (greedy removal of larger digits)
         */
        while (k > 0 && !stack.isEmpty() && stack.peekLast() > digit) {
            stack.removeLast();
            k--;
        }
        stack.addLast(digit);
    }

    // Edge case: if k > 0, remove digits from end (e.g., "1111")
    while (k > 0) {
        stack.removeLast();
        k--;
    }

    // Build result and remove leading zeros
    StringBuilder sb = new StringBuilder();
    boolean leadingZero = true;
    while (!stack.isEmpty()) {
        char c = stack.removeFirst();
        if (leadingZero && c == '0')
            continue;
        leadingZero = false;
        sb.append(c);
    }

    return sb.length() == 0 ? "0" : sb.toString();
}

/**
 * Example Walkthrough:
 *
 * Input: num = "1432219", k = 3
 *
 * Step-by-step:
 * 1. Push '1': [1]
 * 2. Push '4': [1, 4]
 * 3. '3' < '4': Pop '4', push '3', k=2. Stack: [1, 3]
 * 4. '2' < '3': Pop '3', push '2', k=1. Stack: [1, 2]
 * 5. Push '2': [1, 2, 2]
 * 6. '1' < '2': Pop '2', push '1', k=0. Stack: [1, 2, 1]
 * 7. k=0, push '9': [1, 2, 1, 9]
 *
 * Result: "1219"
 *
 * Why ArrayDeque?
 * - Stack<Character> is synchronized and slow
 * - ArrayDeque is faster and modern alternative for stack operations
 */
```

### 2-14) Online Stock Span — LC 901

```java
// java
// LC 901. Online Stock Span

/**
 * Problem: Design an algorithm that collects daily price quotes for some stock
 * and returns the span of that stock's price for the current day.
 *
 * The span is the maximum number of consecutive days (starting from today and going backward)
 * for which the stock price was less than or equal to today's price.
 *
 * Example:
 * Prices: [100, 80, 60, 70, 60, 75, 85]
 * Spans:  [1,   1,  1,  2,  1,  4,  6]
 *
 * Key Insight:
 * - Use monotonic decreasing stack to track [price, span] pairs
 * - When new price arrives, pop all smaller/equal prices
 * - Accumulate their spans into current span
 * - This gives us the count of consecutive days with price <= current
 *
 * Time: O(1) amortized per next() call (each element pushed/popped once)
 * Space: O(N) for the stack
 */

// V0
// IDEA: MONOTONIC STACK (decreasing) + SPAN ACCUMULATION
class StockSpanner {

    /**
     * NOTE !!!
     * Stack stores [price, span] pairs
     * - price: the stock price
     * - span: how many consecutive days (including itself) had price <= this price
     */
    private Deque<int[]> stack; // {price, span}

    public StockSpanner() {
        stack = new ArrayDeque<>();
    }

    /**
     * NOTE !!!
     * Monotonic decreasing stack pattern:
     * 1. Start with span = 1 (today counts)
     * 2. While stack top has price <= current price:
     *    - Pop it and add its span to current span
     * 3. Push [current price, accumulated span]
     * 4. Return span
     */
    public int next(int price) {
        int span = 1; // Today always counts as 1

        /**
         * Pop all prices that are less than or equal to current price
         * and accumulate their spans
         */
        while (!stack.isEmpty() && stack.peek()[0] <= price) {
            // "Absorb" the previous span into current span
            span += stack.pop()[1];
        }

        // Push current price with its accumulated span
        stack.push(new int[] { price, span });

        return span;
    }
}

/**
 * Example Walkthrough:
 *
 * Input: [100, 80, 60, 70, 60, 75, 85]
 *
 * next(100):
 *   - span = 1, stack is empty
 *   - Push [100, 1]
 *   - Return 1
 *   Stack: [[100, 1]]
 *
 * next(80):
 *   - span = 1, stack top is [100, 1], 100 > 80, don't pop
 *   - Push [80, 1]
 *   - Return 1
 *   Stack: [[80, 1], [100, 1]]
 *
 * next(60):
 *   - span = 1, stack top is [80, 1], 80 > 60, don't pop
 *   - Push [60, 1]
 *   - Return 1
 *   Stack: [[60, 1], [80, 1], [100, 1]]
 *
 * next(70):
 *   - span = 1
 *   - stack top is [60, 1], 60 <= 70, pop and add span: span = 1 + 1 = 2
 *   - stack top is [80, 1], 80 > 70, stop
 *   - Push [70, 2]
 *   - Return 2
 *   Stack: [[70, 2], [80, 1], [100, 1]]
 *
 * next(60):
 *   - span = 1
 *   - stack top is [70, 2], 70 > 60, don't pop
 *   - Push [60, 1]
 *   - Return 1
 *   Stack: [[60, 1], [70, 2], [80, 1], [100, 1]]
 *
 * next(75):
 *   - span = 1
 *   - stack top is [60, 1], 60 <= 75, pop and add: span = 1 + 1 = 2
 *   - stack top is [70, 2], 70 <= 75, pop and add: span = 2 + 2 = 4
 *   - stack top is [80, 1], 80 > 75, stop
 *   - Push [75, 4]
 *   - Return 4 (covers prices: 60, 70, 60, 75)
 *   Stack: [[75, 4], [80, 1], [100, 1]]
 *
 * next(85):
 *   - span = 1
 *   - stack top is [75, 4], 75 <= 85, pop and add: span = 1 + 4 = 5
 *   - stack top is [80, 1], 80 <= 85, pop and add: span = 5 + 1 = 6
 *   - stack top is [100, 1], 100 > 85, stop
 *   - Push [85, 6]
 *   - Return 6 (covers prices: 60, 70, 60, 75, 80, 85)
 *   Stack: [[85, 6], [100, 1]]
 *
 * Why this works:
 * - When we pop [60, 1] and [70, 2], we're saying:
 *   "60 had 1 consecutive day <= 60 (itself)"
 *   "70 had 2 consecutive days <= 70 (60, 70)"
 * - By accumulating: span = 1 + 1 + 2 = 4
 *   We get: "75 has 4 consecutive days <= 75 (60, 70, 60, 75)"
 */

/**
 * Usage:
 * StockSpanner obj = new StockSpanner();
 * int span = obj.next(price);
 */
```