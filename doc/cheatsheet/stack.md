# Stack

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/stack.jpeg"></p>
- A data structute with Last in, First out (LIFO) propery

<img src ="https://github.com/yennanliu/CS_basics/blob/master/doc/pic/stack_101.png"></p>


- Ref
    - [fuck-Algorithm - single stack](https://github.com/labuladong/fucking-Algorithm/blob/master/%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84%E7%B3%BB%E5%88%97/%E5%8D%95%E8%B0%83%E6%A0%88.md)
    - [fuck-Algorithm - implement array via stack / stack via array ](https://github.com/labuladong/fucking-Algorithm/blob/master/%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84%E7%B3%BB%E5%88%97/%E9%98%9F%E5%88%97%E5%AE%9E%E7%8E%B0%E6%A0%88%E6%A0%88%E5%AE%9E%E7%8E%B0%E9%98%9F%E5%88%97.md)

- It's critical to verify the to use stack if `increasing` or `decreasing` (aka `monotonic stack`) when solving LC problem
- [Vodeo ref1](https://www.bilibili.com/list/525438321?sort_field=pubtime&spm_id_from=333.999.0.0&oid=779764003&bvid=BV1my4y1Z7jj)

- Use cases
    - Find next `big number` (next great number)
        - LC 496
        - LC 503
        - LC 739
        - LC 503
    - Calculator, decode string
        - LC 224
        - LC 227
        - LC 394
    - Remove all adjacent duplicates
         - LC 1047
         - LC 1209
    - monotonic stack
        - LC 2104
        - LC 239

## 0) Concept
- [Java Stack](https://blog.csdn.net/oChangWen/article/details/72859556)
    - Low level : Array

### 0-1) Types

- Single stack
- Build queue via stack
     - LC 232 (use `2 stack`)
- Build stack via queue

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


- monotonic stack (decreasing)

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

## 1) General form

### 1-1) Basic OP

### 1-1-1) basic ops
- Stack insert
- Stack pop 
- Stack isEmpty
- Stack hasElement



### 1-1-2-1) next greater element

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


### 1-1-2-2) next greater element 2

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

### 1-1-3) get `non balanced` String
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

### 1-1-4) deal with `pre num, pre string`
```python
# LC 227, 394
# ...
for i, each in enumerate(s):
    # ...
    if i == len(s) - 1 or each in "+-*/":
        if pre_op == "+":
            # ...
        elif pre_op == "-":
            # ...
        elif pre_op == "*":
            # ...
        elif pre_op == "/":
            # ...
        """
        NOTE this !!!
        """
        pre_op = each
        num = 0
# ...
```

## 2) LC Example

### 2-1) Decode String
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

### 2-2) Next Greater Element I
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

### 2-3) Next Greater Element II
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

```c++
// c++
// LC 503. Next Greater Element II
// Algorithm book (labu) p. 276
vector<int> nextGreaterElements(vector<int> & nums){
    int n = nums.size();
    // save the result
    vector<int> res(n);
    stack<int> s;
    // `simulate` the stack has length double
    for (int i = 2 * n - 1; i >= 0; i --){
        while (!s.empty() && s.top() <= nums[i % n]){
            s.pop();
        }
        res[i % n] = s.empty() ? -1 : s.top();
        s.push(nums[i % n]);
    }
    return res;
}
```

### 2-4) Daily Temperatures
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

### 2-5) Basic Calculator I
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

### 2-5') Basic Calculator II
```python
# python
# LC 227. Basic Calculator II, LC 224. Basic Calculator
# V0
# IDEA : STACK
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

### 2-5) Sum of Subarray Minimums
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

### 2-6) Asteroid Collision
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

### 2-7) Remove All Adjacent Duplicates in String
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

### 2-8) Remove All Adjacent Duplicates in String II
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

### 2-9) Simplify Path
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

### 2-10) Min Stack
```python
# LC 155. Min Stack
# V0
# IDEA : STACK
# IDEA : 
# -> USE A STACK TO STORAGE MIN VALUE IN THE STACK WHEN EVERY PUSH
# -> SO WE CAN RETURN getMin IN CONSTANT TIEM VIA STACK ABOVE
class MinStack(object):

    def __init__(self):
        """
        initialize your data structure here.
        """
        self.stack = []
        
    def push(self, x):
        if not self.stack:
            """
            NOTE : we use stack = [(x, y)]
                    x is the current element
                    y in current MIN value in current stack
            """
            ### note here
            self.stack.append((x, x))
        ### NOTICE HERE 
        # stack[i][1] save to min value when every push
        # so the latest min in stack is at stack[-1][1]
        else:
            ### note here
            self.stack.append((x, min(x, self.stack[-1][1])))
        
    def pop(self):
        self.stack.pop()
        
    def top(self):
        return self.stack[-1][0]
        
    def getMin(self):
        # the latest min in stack is at stack[-1][1]
        return self.stack[-1][1]
```

### 2-11) Sum of Subarray Ranges
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

### 2-11) Largest Rectangle in Histogram

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

### 2-12) Remove Duplicate Letters

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