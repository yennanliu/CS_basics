# Stack
> Last in, First out (LIFO)

- Ref
    - [fucking-Algorithm - single stack](https://github.com/labuladong/fucking-Algorithm/blob/master/%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84%E7%B3%BB%E5%88%97/%E5%8D%95%E8%B0%83%E6%A0%88.md)
    - [fucking-Algorithm - implement array via stack](https://github.com/labuladong/fucking-Algorithm/blob/master/%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84%E7%B3%BB%E5%88%97/%E9%98%9F%E5%88%97%E5%AE%9E%E7%8E%B0%E6%A0%88%E6%A0%88%E5%AE%9E%E7%8E%B0%E9%98%9F%E5%88%97.md)

## 0) Concept  

### 0-1) Types
- Single stack
- Build queue via stack
- Build stack via queue

### 0-2) Pattern
```c++
// c++
vector<int> nextGreaterElement(vector<int>& nums) {
    vector<int> ans(nums.size()); // list storage answer
    stack<int> s;
    for (int i = nums.size() - 1; i >= 0; i--) { // put into stack with inverser order
        while (!s.empty() && s.top() <= nums[i]) { // check if height is higher or shorter 
            s.pop(); // start from shorter height
        }
        ans[i] = s.empty() ? -1 : s.top(); // the first higher after this one
        s.push(nums[i]); // put into stack, will check the height later
    }
    return ans;
}
```

## 1) General form

### 1-1) Basic OP

### 1-1-1) basic ops
- Stack insert
- Stack pop 
- Stack isEmpty
- Stack hasElement

### 1-1-2) next greater number
```java
// LC 496, 503 (see below)
// java
// aAlgorithm book (labu) p. 273
vector<int> nextGreaterElement(vector<int> & nums){
    vector<int> ans(nums.size()); // array for ans
    stack<int> s;
    // inverse ordering
    for (int i = nums.size() - 1 ; i >= 0; i --){
        // check whether nummber is greater
        while (!s.empty() && s.top() <= nums[i]){
            s.pop()
        }
        // the last great number in stack
        ans[i] = s.empty() ? -1: s.top();
        s.push(nums[i]);
    }
return ans;
}
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
        for c in s:
            if c.isdigit():
                num = num*10 + int(c)
            elif c == "[":
                stack.append(string)
                stack.append(num)
                string = ''
                num = 0
            elif c.isalpha():
                string += c
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

### 2-3) Next Greater Element II
```python
# LC 503. Next Greater Element II
# V0
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
```c++
// LC 739. Daily Temperatures
// c++
// Algorithm book (labu) p. 274
vector<int> dailyTemperatures(vector<int> & T){
    vector<int> ans(T.size());
    // put index in below stack (not element)
    satck<int> s;
    for (int i = T.size()-1; i >= 0; i --){
        while (!s.empty() && T[s.top()] <= T[i]){
            s.pop();
        }
        // get index distance
        ans[i] = s.empty() ? 0 : (s,top() - i);
        // add index, but not element
        s.push(i);
    }
    return ans;
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