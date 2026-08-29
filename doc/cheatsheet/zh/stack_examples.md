# Stack — 實戰題解

> **範圍** — [stack.md](./stack.md) 背後的題解庫：單調堆疊、貪婪移除、相鄰重複、括號家族、走訪與設計類題目，每題每語言各一份標準解，依各自演練的模板分組。
> **另見**：[stack.md](./stack.md) — 母頁：本庫所支撐的標準模板、決策表與陷阱；[stack_expression_parsing.md](./stack_expression_parsing.md) — 計算機、decode string 與後綴運算求值，那是自成一家的題型；[monotonic_stack.md](./monotonic_stack.md) — next greater／previous smaller 的理論，底下不少題目其實歸它管；[iterator.md](./iterator.md) — LC 173／LC 341 之外的迭代器設計；[queue.md](./queue.md) — FIFO 那一側，包括從另一個角度看 LC 232。

## LeetCode 題目清單

- [Stack](https://leetcode.com/problem-list/stack/)
- [Monotonic Stack](https://leetcode.com/problem-list/monotonic-stack/)
- [String](https://leetcode.com/problem-list/string/)

## 總覽

這裡是 [stack.md](./stack.md) 的長尾。母頁放六份模板；這個檔案放實際*套用*它們的題目，才不會讓模板被兩千行的題解埋掉。各節依演練的模板分組，並用一組連續編號。

### 關鍵性質
- **複雜度**：見母頁的 [Time Complexity](./stack.md#time-complexity) 表；底下每份解法都是 O(n) 時間，除非該解法自己的註解另有說明
- **核心想法**：每一節都是母頁某一份模板的演練 —— 要背的是模板，這些是練習量
- **什麼時候用**：當你已經知道一道題該用哪份模板，想看它完整寫出來長什麼樣

### 關於重複收錄

其中十二題在 [monotonic_stack.md](./monotonic_stack.md) 裡也有題解（LC 32、84、155、388、402、496、503、735、739、901、907、2104），LC 173／LC 341 則是 [iterator.md](./iterator.md) 的主題。這些重複目前是刻意保留的 —— 要整併它們是跨檔案的工程，不是單一份 cheatsheet 能處理的。

## LC 範例

### 單調堆疊 —— Next Greater / Smaller

#### 1) Next Greater Element I — LC 496

> `nums1` 是 `nums2` 的子集，所以**只掃 `nums2`**，用一趟單調掃描建出 `{element: next-greater}`
> 的對照表，再照 `nums1` 把答案讀出來。
> 兩段 Python 是暴力法的基準線（沒用堆疊，O(n·m)）；Java 那段才是標準的單調堆疊解。

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

#### 2) Next Greater Element II — LC 503

> **環狀**陣列：跑 `nums * 2`（或索引取 mod `n`），讓元素可以繞回頭去找答案。這裡給了兩個方向 ——
> 由左往右、在 pop 的當下就決定答案；以及由右往左、直接從剩下的堆疊頂端讀答案。

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

#### 3) Daily Temperatures — LC 739 ⭐⭐⭐⭐

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

#### 4) Sum of Subarray Minimums — LC 907

> **貢獻度計數**：對每個元素問「它*主宰*了幾個子陣列？」兩趟單調掃描分別給出往左、往右可以延伸的數量；
> 答案就是 `sum(a * left * right)`。

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

#### 5) Sum of Subarray Ranges — LC 2104

> 把 LC 907 做兩次：`sum(max) - sum(min)`，兩半都用同一套貢獻度計數，並在頭尾放哨兵，
> 逼每個元素都會被彈出堆疊。

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

#### 6) Largest Rectangle in Histogram — LC 84 ⭐⭐⭐⭐

> 被彈出的那根柱子是矩形的**高**；新索引與新的堆疊頂端之間的距離是它的**寬**。
> 底部那個 `-1` 哨兵讓寬度的算式可以統一寫。

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

#### 7) Online Stock Span — LC 901

> **串流版**單調堆疊：它跨呼叫存活，而且每一筆都帶著自己已經吸收的 span，
> 所以一次 pop 就能一整塊天數一起加進來。

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

### 單調堆疊 —— 貪婪移除與字典序

#### 8) Remove K Digits — LC 402 ⭐⭐⭐⭐

```text
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

#### 9) Remove Duplicate Letters — LC 316

> LC 402 的貪婪移除，再加兩個額外的不變式：**每個字母只能出現一次**，而且一個字母只有在
> **後面還會再出現**時才能被彈出 —— 否則丟掉就永遠找不回來了。LC 1081 是同一題。

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

> 上面的 `freq[top] > 0` 與下面的 `lastOccurrence[top] > i` 是同一個條件的兩種寫法 ——
> *「這個字元後面還會再出現嗎？」*。底下的走查會說明，為什麼這個檢查正是讓貪婪彈出安全的關鍵。

**「後面還會出現」邏輯的說明：**

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

#### 10) Asteroid Collision — LC 735

> 一個裝倖存者的堆疊：往左飛的小行星（`new < 0`）只會跟往右飛的堆疊頂端（`ans[-1] > 0`）對撞。
> 注意那個 `for ... else` —— `else` 只有在 `while` 沒被 break 時才會執行，也就是新來的那顆活下來了。

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

### 相鄰重複移除 —— `[element, count]` 配對

#### 11) Remove All Adjacent Duplicates in String — LC 1047

> `k = 2` 的特例：不必記次數，單純「頂端等於當前字元就 pop」就夠了。
> 第二段是 O(1) 額外空間的雙指標寫法 —— 想法一樣，只是陣列自己當堆疊用。

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

#### 12) Remove All Adjacent Duplicates in String II — LC 1209 ⭐⭐⭐⭐

**模式：帶字元計數配對的堆疊**

這個模式用 `Stack<int[]>` 或 `Stack<[char, count]>` 來有效率地追蹤連續重複元素與它們的次數。當你需要移除 k 個連續相同元素時特別好用。

**什麼時候用這個模式：**

1. **題目提到「k 個連續／相鄰的相同元素」**
   - 移除 k 個重複：LC 1209
   - 數 k 個連續：各種計數題

2. **需要同時追蹤字元「和」它的出現次數**
   - 只記字元不行（k 次移除需要次數）
   - 只記次數也不行（要知道是哪個字元）

3. **次數達到門檻 k 時就移除**
   - 不像 LC 1047（k=2，一個 `stack.pop()` 就解決），這裡 k 是變數
   - 需要保留還沒湊滿的進度（例如 k=3 時，"aaab" 裡的 "aa"）

4. **要求 O(n) 空間的一趟解法**
   - 堆疊存的是壓縮形式：{char, count}
   - 比把所有字元都存下來有效率

**辨識訊號：**
- ✓ 關鍵字：「k adjacent」、「k consecutive」、「k duplicates」
- ✓ 剛好累積到 k 次時就移除／計數
- ✓ 需要處理不完整的序列（count < k）
- ✓ 輸入限制：k >= 2（若 k=1，要換另一套做法）

**結構：**
```java
// Core data structure
Stack<int[]> stack = new Stack<>();
// Each element: {character_as_int, count}

// Or for clarity
Stack<Pair<Character, Integer>> stack = new Stack<>();
```

**相似題：**
- LC 1047: Remove All Adjacent Duplicates in String（k=2 的特例）
- LC 1544: Make The String Great（移除相鄰的大小寫相反配對）
- LC 316: Remove Duplicate Letters（用堆疊求字典序）
- LC 394: Decode String（堆疊帶計數，但用途是重複展開）

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

### 括號家族 —— LC 20 模板的各種變化

基礎模板是[母頁的模板 2](./stack.md)；底下這四題就是它點名的四種變化。

#### 13) Minimum Remove to Make Valid Parentheses — LC 1249

> **變化點**：推入 `(` 的**索引**而不是字元本身，這樣最後才能把沒配對到的位置刪掉。沒配對的 `)` 當下就會被抓到（堆疊為空）；沒配對的 `(` 就是掃完後*還留在堆疊裡*的那些。

```java
// java
// LC 1249 - Minimum Remove to Make Valid Parentheses
// IDEA: STACK OF INDICES — mark unmatched '(' and ')' positions, then drop them
// time = O(n), space = O(n)
public String minRemoveToMakeValid(String s) {

    StringBuilder sb = new StringBuilder(s);
    Deque<Integer> st = new ArrayDeque<>(); // indices of UNMATCHED '('

    for (int i = 0; i < sb.length(); i++) {
        char c = sb.charAt(i);
        if (c == '(') {
            st.push(i);
        } else if (c == ')') {
            if (!st.isEmpty()) {
                st.pop();      // matched -> keep both
            } else {
                /** NOTE !!! ')' with no opener -> mark for deletion */
                sb.setCharAt(i, '*'); // '*' is safe: input is only '(' , ')' , a-z
            }
        }
    }

    /** NOTE !!! whatever remains in the stack are unmatched '(' */
    while (!st.isEmpty()) {
        sb.setCharAt(st.pop(), '*');
    }

    return sb.toString().replace("*", "");
}
```

```python
# python
# LC 1249 - Minimum Remove to Make Valid Parentheses
# IDEA: STACK OF INDICES — blank out unmatched '(' and ')' positions
# time = O(n), space = O(n)
class Solution(object):
    def minRemoveToMakeValid(self, s):
        arr = list(s)
        stack = []  # indices of UNMATCHED '('
        for i, c in enumerate(arr):
            if c == '(':
                stack.append(i)
            elif c == ')':
                if stack:
                    stack.pop()      # matched
                else:
                    arr[i] = ''      # unmatched ')' -> delete
        # leftover '(' indices are unmatched -> delete
        for i in stack:
            arr[i] = ''
        return ''.join(arr)
```

#### 14) Minimum Add to Make Parentheses Valid — LC 921

> **變化點**：只有 `(` 和 `)` 時，堆疊退化成它自己的**大小**，所以用一個累加的 balance 就能做到 O(1) 空間。`balance < 0` 代表 `)` 來得太早 → 必須補一個 `(` 並重設。

```java
// java
// LC 921 - Minimum Add to Make Parentheses Valid
// IDEA: BALANCE COUNTER (stack degenerated to its size)
// time = O(n), space = O(1)
public int minAddToMakeValid(String s) {
    int need = 0;     // '(' we must insert
    int balance = 0;  // unmatched '(' so far == "stack size"
    for (char c : s.toCharArray()) {
        balance += (c == '(') ? 1 : -1;
        /** NOTE !!! a ')' with nothing to close -> insert a '(' and reset */
        if (balance < 0) {
            need++;
            balance = 0;
        }
    }
    return need + balance; // + leftover '(' each needing a ')'
}
```

```python
# python
# LC 921 - Minimum Add to Make Parentheses Valid
# IDEA: BALANCE COUNTER (stack degenerated to its size)
# time = O(n), space = O(1)
class Solution(object):
    def minAddToMakeValid(self, s):
        need = 0      # '(' to insert
        balance = 0   # unmatched '(' == stack size
        for c in s:
            balance += 1 if c == '(' else -1
            if balance < 0:
                need += 1
                balance = 0
        return need + balance
```

#### 15) Longest Valid Parentheses — LC 32 ⭐⭐⭐⭐

> **變化點**：我們要的是最長合法區段的**長度**，所以堆疊存索引，而且它的**底部元素是當前合法區段前一格的索引**（也就是「基準點」）。初始化放 `-1`。遇到 `)` 先 pop；如果堆疊變空了，當前這個 `)` 就成為新的基準點，否則 `i - stack.top()` 就是以 `i` 結尾的合法長度。

```java
// java
// LC 32 - Longest Valid Parentheses
// IDEA: STACK OF INDICES + `-1` base sentinel; length = i - stack.peek()
// time = O(n), space = O(n)
public int longestValidParentheses(String s) {

    Deque<Integer> st = new ArrayDeque<>();
    /** NOTE !!! base sentinel: index BEFORE the current valid segment */
    st.push(-1);

    int res = 0;
    for (int i = 0; i < s.length(); i++) {
        if (s.charAt(i) == '(') {
            st.push(i);
        } else {
            st.pop();               // try to match with the top '('
            if (st.isEmpty()) {
                /** NOTE !!! unmatched ')' -> it becomes the NEW base */
                st.push(i);
            } else {
                /** NOTE !!! distance to the base = valid length ending at i */
                res = Math.max(res, i - st.peek());
            }
        }
    }
    return res;
}
```

```python
# python
# LC 32 - Longest Valid Parentheses
# IDEA: STACK OF INDICES + `-1` base sentinel; length = i - stack[-1]
# time = O(n), space = O(n)
class Solution(object):
    def longestValidParentheses(self, s):
        stack = [-1]   # base: index before the current valid segment
        res = 0
        for i, c in enumerate(s):
            if c == '(':
                stack.append(i)
            else:
                stack.pop()
                if not stack:
                    stack.append(i)          # unmatched ')' -> new base
                else:
                    res = max(res, i - stack[-1])
        return res
```

```text
Visual trace — s = ")()())"

i  c   action                       stack        res
-  -   init base                    [-1]         0
0  )   pop -1 -> empty -> new base  [0]          0
1  (   push                         [0, 1]       0
2  )   pop 1, i - top = 2 - 0 = 2   [0]          2
3  (   push                         [0, 3]       2
4  )   pop 3, i - top = 4 - 0 = 4   [0]          4  <- answer
5  )   pop 0 -> empty -> new base   [5]          4
```

#### 16) Score of Parentheses — LC 856

> **變化點**：堆疊的每一格存的是**那個深度裡累積的分數**。`(` 開一個新的框（推入 `0`），`)` 把它收掉：空的框算 1 分，否則就翻倍 —— `max(2 * inner, 1)` —— 然後併回上一層的框。

```java
// java
// LC 856 - Score of Parentheses
// IDEA: STACK OF PARTIAL SCORES — one frame per depth, fold child into parent
// time = O(n), space = O(n)
public int scoreOfParentheses(String s) {
    Deque<Integer> st = new ArrayDeque<>();
    st.push(0); // score of the outermost frame
    for (char c : s.toCharArray()) {
        if (c == '(') {
            st.push(0); // open a new (empty) frame
        } else {
            int inner = st.pop();
            /** NOTE !!! "()" scores 1, "(X)" scores 2*X */
            int cur = st.pop() + Math.max(2 * inner, 1);
            st.push(cur);
        }
    }
    return st.pop();
}
```

```python
# python
# LC 856 - Score of Parentheses
# IDEA: STACK OF PARTIAL SCORES — one frame per depth, fold child into parent
# time = O(n), space = O(n)
class Solution(object):
    def scoreOfParentheses(self, s):
        stack = [0]              # score of the outermost frame
        for c in s:
            if c == '(':
                stack.append(0)  # open a new frame
            else:
                inner = stack.pop()
                # "()" -> 1 ; "(X)" -> 2 * X
                stack[-1] += max(2 * inner, 1)
        return stack[0]
```

### 作用域、反轉與設計

#### 17) Simplify Path — LC 71

> 最迷你版的作用域帳本：名字就推入一層目錄，`..` 彈出父層，`.` 和空片段是雜訊。

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

#### 18) Minimum Number of Swaps to Make the String Balanced — LC 1963

> 把括號模板當成**歸約器**用：掃完之後堆疊裡只剩下不平衡的 `]]][[[` 核心，答案就是它長度的一條公式。

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
        stack.append(s[i])
print (stack)
```

#### 19) 顯式堆疊迭代器 — LC 173, LC 341 ⭐⭐⭐⭐

> **關鍵想法**：遞迴有一個*隱式*的呼叫堆疊，而且會一路跑到底。但**迭代器必須在元素之間暫停**，所以你要把那個堆疊變成**顯式**的，每次 `next()` 只推進一步。堆疊裡放的是*還沒做的工作*。

```text
Core Idea:
  - Constructor : seed the stack with the minimum work needed to expose
                  the FIRST element (do NOT flatten everything -> O(h) space)
  - hasNext()   : normalize the stack top until it IS a real element
  - next()      : pop the element, then push the work it unlocked

Two flavours:
  1) Tree in-order (LC 173): push the whole LEFT SPINE; next() pops a node
     and pushes the left spine of its RIGHT child. O(h) space, O(1) amortized.
  2) Nested list  (LC 341): push children in REVERSE so the leftmost is on top;
     hasNext() expands lists lazily until an integer surfaces.

Watch-outs:
  - Push children in REVERSE order — a stack flips whatever you feed it
  - Put the "normalize" loop in hasNext(), not next(); the judge calls
    hasNext() before every next()
  - "Flatten everything in the constructor" also passes but costs O(n) space —
    the follow-up question is always "can you do it in O(h) / lazily?"

Similar LC:
  - LC 173  Binary Search Tree Iterator      (controlled in-order)
  - LC 341  Flatten Nested List Iterator     (lazy nested expansion)
  - LC 144  Binary Tree Preorder Traversal   (push right BEFORE left)
  - LC 145  Binary Tree Postorder Traversal  (root-right-left, then REVERSE)
  - LC 385  Mini Parser                      (build the nested structure w/ a stack)
```

```java
// java
// LC 173 - Binary Search Tree Iterator
// IDEA: EXPLICIT STACK holding the LEFT SPINE (paused in-order traversal)
// time = O(1) amortized per next(), space = O(h)
class BSTIterator {

    private Deque<TreeNode> stack = new ArrayDeque<>();

    public BSTIterator(TreeNode root) {
        pushLeft(root);
    }

    /** NOTE !!! the left spine = every node we must visit before `node` */
    private void pushLeft(TreeNode node) {
        while (node != null) {
            stack.push(node);
            node = node.left;
        }
    }

    public int next() {
        TreeNode cur = stack.pop();
        /** NOTE !!! after visiting a node, its RIGHT subtree becomes pending */
        pushLeft(cur.right);
        return cur.val;
    }

    public boolean hasNext() {
        return !stack.isEmpty();
    }
}
```

```python
# python
# LC 173 - Binary Search Tree Iterator
# IDEA: EXPLICIT STACK holding the LEFT SPINE (paused in-order traversal)
# time = O(1) amortized per next(), space = O(h)
class BSTIterator(object):

    def __init__(self, root):
        self.stack = []
        self._push_left(root)

    def _push_left(self, node):
        # every node we must visit BEFORE `node` sits above it on the stack
        while node:
            self.stack.append(node)
            node = node.left

    def next(self):
        cur = self.stack.pop()
        # NOTE !!! the right subtree only becomes pending AFTER we visit cur
        self._push_left(cur.right)
        return cur.val

    def hasNext(self):
        return len(self.stack) > 0
```

```java
// java
// LC 341 - Flatten Nested List Iterator
// IDEA: EXPLICIT STACK, children pushed in REVERSE, expanded lazily in hasNext()
// time = O(1) amortized per next(), space = O(depth + width)
public class NestedIterator implements Iterator<Integer> {

    private Deque<NestedInteger> stack = new ArrayDeque<>();

    public NestedIterator(List<NestedInteger> nestedList) {
        pushReversed(nestedList);
    }

    /** NOTE !!! push BACKWARDS so the leftmost element ends up on TOP */
    private void pushReversed(List<NestedInteger> list) {
        for (int i = list.size() - 1; i >= 0; i--) {
            stack.push(list.get(i));
        }
    }

    @Override
    public Integer next() {
        // assumes hasNext() was called first (guaranteed by the problem)
        return stack.pop().getInteger();
    }

    @Override
    public boolean hasNext() {
        /** NOTE !!! normalize HERE: expand lists until an integer is on top */
        while (!stack.isEmpty()) {
            if (stack.peek().isInteger()) {
                return true;
            }
            pushReversed(stack.pop().getList()); // lazy expansion
        }
        return false;
    }
}
```

```python
# python
# LC 341 - Flatten Nested List Iterator
# IDEA: EXPLICIT STACK, children pushed in REVERSE, expanded lazily in hasNext()
# time = O(1) amortized per next(), space = O(depth + width)
class NestedIterator(object):

    def __init__(self, nestedList):
        # NOTE !!! reversed -> leftmost element sits on TOP of the stack
        self.stack = nestedList[::-1]

    def next(self):
        # hasNext() is guaranteed to be called first
        return self.stack.pop().getInteger()

    def hasNext(self):
        # NOTE !!! normalize HERE: keep unwrapping lists until an int surfaces
        while self.stack:
            top = self.stack[-1]
            if top.isInteger():
                return True
            self.stack.pop()
            self.stack.extend(top.getList()[::-1])
        return False
```

#### 20) Add Two Numbers II — LC 445

> **關鍵想法**：單向鏈結串列只能往前走，但有些題目需要**倒著**處理（從個位數開始相加）。把每個節點推入堆疊，就能在**不動到輸入**的前提下倒著存取 —— 這正是面試官問「不反轉串列做得到嗎？」時的答案。

```text
Core Idea:
  - Walk forward, push everything -> popping now yields REVERSE order
  - Build the answer list by PREPENDING (node.next = head; head = node),
    which reverses a second time and lands in the correct order

Trade-off:
  - Stack version: O(n) space, input untouched  <- usually what is asked for
  - Reverse-both-lists version: O(1) space, but MUTATES the input

Similar LC:
  - LC 445  Add Two Numbers II          (two stacks, carry, prepend result)
  - LC 234  Palindrome Linked List      (push all, then compare front vs pop)
  - LC 143  Reorder List                (push all, weave head with popped tail)
  - LC 114  Flatten Binary Tree to Linked List (preorder stack, rewire right ptr)
```

```java
// java
// LC 445 - Add Two Numbers II
// IDEA: TWO STACKS give reverse (least-significant-first) access without mutating input
// time = O(n + m), space = O(n + m)
public ListNode addTwoNumbers(ListNode l1, ListNode l2) {

    Deque<Integer> s1 = new ArrayDeque<>();
    Deque<Integer> s2 = new ArrayDeque<>();

    while (l1 != null) {
        s1.push(l1.val);
        l1 = l1.next;
    }
    while (l2 != null) {
        s2.push(l2.val);
        l2 = l2.next;
    }

    int carry = 0;
    ListNode head = null; // we build the result BACKWARDS

    /** NOTE !!! loop while EITHER stack has digits OR a carry is pending */
    while (!s1.isEmpty() || !s2.isEmpty() || carry != 0) {
        int sum = carry;
        if (!s1.isEmpty()) {
            sum += s1.pop();
        }
        if (!s2.isEmpty()) {
            sum += s2.pop();
        }
        carry = sum / 10;

        /** NOTE !!! PREPEND -> the second reversal, result comes out in order */
        ListNode node = new ListNode(sum % 10);
        node.next = head;
        head = node;
    }

    return head;
}
```

```python
# python
# LC 445 - Add Two Numbers II
# IDEA: TWO STACKS give reverse (least-significant-first) access without mutating input
# time = O(n + m), space = O(n + m)
class Solution(object):
    def addTwoNumbers(self, l1, l2):
        s1, s2 = [], []
        while l1:
            s1.append(l1.val)
            l1 = l1.next
        while l2:
            s2.append(l2.val)
            l2 = l2.next

        carry = 0
        head = None   # build result BACKWARDS
        # NOTE !!! keep going while either stack has digits OR carry is pending
        while s1 or s2 or carry:
            total = carry
            if s1:
                total += s1.pop()
            if s2:
                total += s2.pop()
            carry, digit = divmod(total, 10)

            # NOTE !!! prepend -> second reversal -> correct final order
            node = ListNode(digit)
            node.next = head
            head = node

        return head
```

#### 21) Implement Queue using Stacks — LC 232

> **用兩個 LIFO 湊出 FIFO**：推入時放 `input`，`output` 空掉時就把 `input` 整個倒過去
> —— 那一次反轉攤還下來，每個操作是 O(1)。佇列那一側的觀點見 [queue.md](./queue.md)。

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

### 速查 —— 其他值得知道的堆疊題

| LC | 題目 | 一句話講堆疊怎麼用 |
|----|---------|------------------------|
| 946 | Validate Stack Sequences | **模擬**：逐一推入 `pushed[i]`，然後貪婪地在 `top == popped[j]` 時彈出；每個元素都被彈掉才合法 |
| 844 | Backspace String Compare | 用堆疊把兩個字串各建出來（`'#'` → 非空就 pop），再比較 —— O(n) 空間；O(1) 的追問版本要從後往前掃 |
| 1910 | Remove All Occurrences of a Substring | 推入字元；只要堆疊**最後 `len(part)` 個字元**等於 `part` 就彈掉 —— 一趟就能處理連鎖移除 |
| 331 | Verify Preorder Serialization of a Binary Tree | 把 `"num,#,#"` 三元組彈成單一個 `#`；等價的寫法是追蹤還剩幾個可用「空位」 |
| 385 | Mini Parser | 跟 LC 394 一樣的四種情況掃描，只是堆疊裡放的是 `NestedInteger` 框而不是字串 |
| 1111 | Maximum Nesting Depth of Two Valid Parentheses Strings | 用深度計數器就好，不用真的堆疊：偶數深度給 A，奇數深度給 B |

> **注意**：LC 42 (Trapping Rain Water)、LC 84 / 85 (Maximal Rectangle)、LC 456 (132 Pattern)、LC 853 (Car Fleet)、LC 581、LC 654、LC 769、LC 962 都是**單調堆疊**題 —— 那些模板見 [monotonic_stack.md](./monotonic_stack.md)。
