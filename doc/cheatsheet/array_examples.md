# Array — Worked Examples

> **Scope** — The worked-solution archive behind [array.md](./array.md): thirteen problems that are genuinely about rewriting an array in place or using its indices as storage, grouped by the trick each one turns on.
> **See also**: [array.md](./array.md) — the parent sheet: the basic operations, the special algorithms and the chooser table that says which sheet a problem actually belongs to; [2_pointers.md](./2_pointers.md), [sliding_window.md](./sliding_window.md), [prefix_sum.md](./prefix_sum.md), [difference_array.md](./difference_array.md) — the four pattern families that own most array-tagged problems; [sort.md](./sort.md), [matrix.md](./matrix.md), [kadane_algorithm.md](./kadane_algorithm.md), [stock_trading.md](./stock_trading.md) — the other owners the chooser routes to.

## LeetCode Problem Lists

- [Array](https://leetcode.com/problem-list/array/)

## Overview

This is the long tail of [array.md](./array.md). The parent keeps the operations, the special
algorithms and the chooser table; this file keeps the problems that *apply* them.

### Key Properties
- **Complexity**: stated per solution; the in-place group is O(n) time and O(1) extra space, which is usually the point of the problem
- **Core Idea**: the array is not just the input — it is also the scratch space, whether by sign, by position, or by a backward write pointer
- **When to Use**: after the [chooser](./array.md#2-pattern-selection) has told you the problem is not really a window, pointer or prefix-sum problem

### A Note on Overlap

Five of these problems are also worked in the sheet that owns their *technique* — LC 121 in
[stock_trading.md](./stock_trading.md) and [kadane_algorithm.md](./kadane_algorithm.md),
LC 1109 in [difference_array.md](./difference_array.md), LC 1567 in
[kadane_algorithm.md](./kadane_algorithm.md), LC 251 in [design.md](./design.md), and LC 406 in
[sort.md](./sort.md). The copies are deliberate for now: reconciling them is the cross-file
consolidation pass, not a per-sheet job.


## In-Place Rewriting & Index Tricks

### 1) First Missing Positive — LC 41 ⭐⭐⭐⭐⭐

> Two ways to use **the array itself as the hash table**, both O(n) time and O(1) extra
> space. They are kept as a pair because the trick differs: the first *marks* a slot by
> flipping its sign, the second *moves* each value to the slot it belongs in.

**Approach A — mark by sign** (clamp out-of-range values, then negate `nums[v-1]`):

```python
# LC 41. First Missing Positive
# V1'
# IDEA :  Index as a hash key.
# https://leetcode.com/problems/first-missing-positive/solution/
# /doc/pic/first-missing-positive.png
class Solution:
    def firstMissingPositive(self, nums: List[int]) -> int:
        n = len(nums)

        # Base case.
        if 1 not in nums:
            return 1

        # Replace negative numbers, zeros,
        # and numbers larger than n by 1s.
        # After this convertion nums will contain
        # only positive numbers.
        for i in range(n):
            if nums[i] <= 0 or nums[i] > n:
                nums[i] = 1

        # Use index as a hash key and number sign as a presence detector.
        # For example, if nums[1] is negative that means that number `1`
        # is present in the array.
        # If nums[2] is positive - number 2 is missing.
        for i in range(n):
            a = abs(nums[i])
            # If you meet number a in the array - change the sign of a-th element.
            # Be careful with duplicates : do it only once.
            if a == n:
                nums[0] = - abs(nums[0])
            else:
                nums[a] = - abs(nums[a])

        # Now the index of the first positive number
        # is equal to first missing positive.
        for i in range(1, n):
            if nums[i] > 0:
                return i

        if nums[0] > 0:
            return n

        return n + 1
```

```java
// java
// LC 41. First Missing Positive
// V0
// IDEA: CYCLIC SORT
/**
 * Cyclic Sort Pattern:
 * Place each positive number x at its "correct" index (x - 1)
 *
 * Key idea:
 * - For a valid positive integer x (1 <= x <= n), it should be at index x-1
 * - Example: number 3 should be at nums[2], number 1 should be at nums[0]
 *
 * Algorithm:
 * 1. For each position i, keep swapping nums[i] to its correct position
 *    until nums[i] is already at the right place or out of range
 * 2. After sorting, scan for the first index i where nums[i] != i + 1
 * 3. That index + 1 is the first missing positive
 *
 * Example: nums = [3, 4, -1, 1]
 * Step 1: Place 3 at index 2 → [-1, 4, 3, 1]
 * Step 2: Place 4 at index 3 → [-1, 1, 3, 4]
 * Step 3: Place 1 at index 0 → [1, -1, 3, 4]
 * Step 4: Scan → nums[1] = -1 ≠ 2, return 2
 *
 * Time: O(N) - each element is swapped at most once
 * Space: O(1) - in-place sorting
 */
public int firstMissingPositive(int[] nums) {
    int n = nums.length;

    // 1. "Cyclic Sort": Place each number x at index x - 1
    // Example: nums[i] = 3 should be at nums[2]
    for (int i = 0; i < n; i++) {
        while (nums[i] > 0 && nums[i] <= n && nums[nums[i] - 1] != nums[i]) {
            // Swap nums[i] with the element at its target index
            int temp = nums[nums[i] - 1];
            nums[nums[i] - 1] = nums[i];
            nums[i] = temp;
        }
    }

    // 2. Scan for the first index where the number is wrong
    for (int i = 0; i < n; i++) {
        if (nums[i] != i + 1) {
            return i + 1; // Found the missing positive!
        }
    }

    // 3. If all numbers 1 to n are present, the answer is n + 1
    return n + 1;
}
```


**Approach B — cyclic sort** (walk each value home, then scan for the first gap):

```python
# LC 41. First Missing Positive
# V0
# IDEA : for loop + while loop + problem understanding
class Solution:
    def firstMissingPositive(self, nums):
        for i, n in enumerate(nums):
            if n < 0:
                continue
            else:
                while n <= len(nums) and n > 0:
                    tmp = nums[n-1]
                    nums[n-1] = float('inf')
                    n = tmp
        for i in range(len(nums)):
            if nums[i] != float('inf'):
                return i+1
            
        return len(nums)+1
```

**Variation — LC 287 Find the Duplicate Number (marking by SIGN):** same "index as a hash key" idea as LC 41, but instead of *swapping* values into place we **negate** `nums[v]` to record "value `v` has been seen". The first time we land on an already-negative slot, that index is the duplicate. Values are in `1..n` with array length `n+1`, so `abs(v)` is always a legal index.

```python
# python
# LC 287 - Find the Duplicate Number
# IDEA: index-as-hash + MARK BY SIGN (negate nums[v] to mean "v was seen")
class Solution(object):
    def findDuplicate(self, nums):
        # time = O(n), space = O(1)  (mutates nums, then restores it)
        res = -1
        for x in nums:
            ### NOTE : always read through abs(), slots may already be negated
            i = abs(x)
            if nums[i] < 0:      # slot i already marked -> i is the duplicate
                res = i
                break
            nums[i] = -nums[i]   # mark "value i seen"

        # restore the array (needed if the caller must not see mutations)
        for i in range(len(nums)):
            nums[i] = abs(nums[i])
        return res
```

```java
// java
// LC 287 - Find the Duplicate Number
// IDEA: index-as-hash + MARK BY SIGN (negate nums[v] to mean "v was seen")
public int findDuplicate(int[] nums) {
    // time = O(n), space = O(1)  (mutates nums, then restores it)
    int res = -1;
    for (int x : nums) {
        int i = Math.abs(x); // values are 1..n, array length n+1 -> always in range
        if (nums[i] < 0) {   // already marked -> duplicate found
            res = i;
            break;
        }
        nums[i] = -nums[i];
    }
    for (int i = 0; i < nums.length; i++) {
        nums[i] = Math.abs(nums[i]); // restore
    }
    return res;
}
```

> **Note:** LC 287's strict follow-up forbids modifying the array — that version is solved with **Floyd cycle detection** (treat `i -> nums[i]` as a linked list), see [2_pointers.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/2_pointers.md). The sign-marking version above is the one to reach for when mutation is allowed.
>
> **Marking-by-sign checklist:** ① values must map to valid indices, ② always dereference with `abs(...)`, ③ restore the signs if the array is reused (LC 442 / 448 use the exact same trick).

### 2) Rotate Array — LC 189 ⭐⭐⭐⭐

```python
# LC 189. Rotate Array
# V0
# IDEA : pop + insert
class Solution(object):
    def rotate(self, nums, k):
        _len = len(nums)
        k = k % _len
        while k > 0:
            tmp = nums.pop(-1)
            nums.insert(0, tmp)
            k -= 1

# V0'
# IDEA : SLICE (in place)
class Solution(object):
    def rotate(self, nums, k):
        # edge case
        if k == 0 or not nums or len(nums) == 1:
            return nums
        ### NOTE this
        k = k % len(nums)
        if k == 0:
            return nums
        """
        NOTE this !!!!
        """
        nums[:k], nums[k:] = nums[-k:], nums[:-k]
        return nums
```

```java
// java
// LC 189. Rotate Array
// V0
// IDEA: REVERSE ARRAY (3-reverse trick)
/**
 * The 3-reverse trick:
 * 1. Reverse the entire array
 * 2. Reverse the first k elements
 * 3. Reverse the remaining elements
 *
 * Example: nums = [1,2,3,4,5,6,7], k = 3
 * Step 1: Reverse entire array → [7,6,5,4,3,2,1]
 * Step 2: Reverse first k=3 elements → [5,6,7,4,3,2,1]
 * Step 3: Reverse remaining elements → [5,6,7,1,2,3,4]
 *
 * Time: O(N) - each element is reversed twice
 * Space: O(1) - in-place rotation
 */
public void rotate(int[] nums, int k) {
    if (nums == null || nums.length <= 1)
        return;

    int n = nums.length;
    // Step 1: Handle cases where k > n
    k = k % n;
    if (k == 0)
        return;

    // Step 2: Apply the 3-reverse trick
    // 1. Reverse the whole array
    reverse(nums, 0, n - 1);
    // 2. Reverse the first k elements (0 to k-1)
    reverse(nums, 0, k - 1);
    // 3. Reverse the rest (k to n-1)
    reverse(nums, k, n - 1);
}

private void reverse(int[] nums, int start, int end) {
    while (start < end) {
        int temp = nums[start];
        nums[start] = nums[end];
        nums[end] = temp;
        start++;
        end--;
    }
}
```

### 3) Product of Array Except Self — LC 238 ⭐⭐⭐⭐⭐

```python
# 238 Product of Array Except Self
# IDEA : 
# SINCE output[i] = (x0 * x1 * ... * xi-1) * (xi+1 * .... * xn-1)
# -> SO DO A 2 LOOP
# -> 1ST LOOP : GO THROGH THE ARRAY (->) : (x0 * x1 * ... * xi-1)
# -> 2ND LOOP : GO THROGH THE ARRAY (<-) : (xi+1 * .... * xn-1)
# e.g.
# given [1,2,3,4], return [24,12,8,6].
# -> output = [2*3*4, 1,1,1]  <-- 2*3*4    (right of 1: 2,3,4)
# -> output = [2*3*4, 1*3*4,1,1] <-- 1*3*4 (left of 2 :1, right of 2: 3,4)
# -> output = [2*3*4, 1*3*4,1*2*4,1] <-- 1*2*4 (left of 3: 1,2 right of 3 : 4)
# -> output = [2*3*4, 1*3*4,1*2*4,1*2*3] <-- 1*2*3 (left of 4 : 1,2,3)
# -> final output  = [2*3*4, 1*3*4,1*2*4,1*2*3] = [24,12,8,6]
class Solution:
    def productExceptSelf(self, nums):
        size = len(nums)
        output = [1] * size
        left = 1
        for x in range(size - 1):
            left *= nums[x]
            output[x + 1] *= left
        right = 1
        for x in range(size - 1, 0, -1):
            right *= nums[x]
            output[x - 1] *= right
        return output
```

### 4) Maximum Swap — LC 670

```python
# 670 Maximum Swap
class Solution(object):
    def maximumSwap(self, num):
        """
        :type num: int
        :rtype: int
        """
        # BE AWARE OF IT 
        digits = list(str(num))
        left, right = 0, 0
        max_idx = len(digits)-1
        for i in range(len(digits))[::-1]:
            # BE AWARE OF IT 
            if digits[i] > digits[max_idx]:
                max_idx = i
            # BE AWARE OF IT  
            # if current digit > current max digit -> swap them 
            elif digits[max_idx] > digits[i]:
                left, right = i, max_idx        # if current max digit > current digit -> save current max digit to right idnex, and save current index to left
        digits[left], digits[right] = digits[right], digits[left] # swap left and right when loop finished 
        return int("".join(digits))
```

## Scanning & Running State

### 5) Best Time to Buy and Sell Stock — LC 121 ⭐⭐⭐⭐

```python
# LC 121 Best Time to Buy and Sell Stock
# V0
# IDEA : array op + problem understanding
class Solution(object):
    def maxProfit(self, prices):
        if len(prices) == 0:
            return 0
        ### NOTE : we define 1st minPrice as prices[0]
        minPrice = prices[0]
        maxProfit = 0
        ### NOTE : we only loop prices ONCE
        for p in prices:
            # only if p < minPrice, we get minPrice
            if p < minPrice:
                minPrice = p
            ### NOTE : only if p - minPrice > maxProfit, we get maxProfit
            elif p - minPrice > maxProfit:
                maxProfit = p - minPrice
        return maxProfit
```

**Variation — LC 122 Best Time to Buy and Sell Stock II (unlimited transactions):** the twist is that with unlimited buys/sells you no longer need to track `minPrice` at all — just **sum every positive day-to-day delta** (every upward step can be captured independently).

```python
# python
# LC 122 - Best Time to Buy and Sell Stock II
# IDEA: unlimited transactions -> greedily collect EVERY upward move
class Solution(object):
    def maxProfit(self, prices):
        # time = O(n), space = O(1)
        profit = 0
        for i in range(1, len(prices)):
            # buy at i-1, sell at i, whenever it goes up
            if prices[i] > prices[i - 1]:
                profit += prices[i] - prices[i - 1]
        return profit
```

```java
// java
// LC 122 - Best Time to Buy and Sell Stock II
// IDEA: unlimited transactions -> greedily collect EVERY upward move
public int maxProfit(int[] prices) {
    // time = O(n), space = O(1)
    int profit = 0;
    for (int i = 1; i < prices.length; i++) {
        if (prices[i] > prices[i - 1]) {
            profit += prices[i] - prices[i - 1];
        }
    }
    return profit;
}
```

> **Why the greedy is correct:** any profitable interval `[i, j]` telescopes into the sum of its daily deltas (`p[j] - p[i] = Σ (p[k+1] - p[k])`), and dropping the negative deltas can only increase the total. So "sum of positive deltas" is an upper bound that is also achievable.
>
> **Contrast:** LC 121 = **1** transaction → track running min. LC 122 = **∞** transactions → sum positive deltas.

### 6) Maximum Length of Subarray With Positive Product — LC 1567

```python
# LC 1567 Maximum Length of Subarray With Positive Product

# V0
class Solution:
    def getMaxLen(self, nums):
        first_neg, zero = None, -1
        mx = neg = 0
        for i,v in enumerate(nums):
            if v == 0:
                first_neg, zero, neg = None, i, 0
                continue
            if v < 0:
                neg += 1
                if first_neg == None:
                    first_neg = i
            j = zero if not neg % 2 else first_neg if first_neg != None else 10**9
            mx = max(mx, i-j)
        return mx

# V0'
# IDEA : 2 POINTERS
class Solution:
    def getMaxLen(self, nums):
        res = 0
        k = -1 # most recent 0
        j = -1 # first negative after most recent 0
        cnt = 0 # count of negatives after most recent 0
        for i, n in enumerate(nums):
            if n == 0:
                k = i
                j = i
                cnt = 0
            elif n < 0:
                cnt += 1
                if cnt % 2 == 0:
                    res = max(res, i - k)
                else:
                    if cnt == 1:
                        j = i
                    else:
                        res = max(res, i - j)        
            else:
                if cnt % 2 == 0:
                    res = max(res, i - k)
                else:
                    res = max(res, i - j)
        return res
```

### 7) Increasing Triplet Subsequence — LC 334

```python
# LC 334 Increasing Triplet Subsequence
# V0
# IDEA : MAINTAIN var first, second
#        AND GO THROUGH nums to check if there exists x (on the right hand side of a, b )
#        such that x > second > first
class Solution(object):
    def increasingTriplet(self, nums):
        """
        NOTE !!! we init first, second as POSITIVE float('inf')
        """
        first = float('inf')
        second = float('inf')
        # loop with normal ordering
        for num in nums:
            if num <= first:     # min num
                first = num
            elif num <= second:  # 2nd min num
                second = num
            else:                # 3rd min num
                return True      
        return False
```

### 8) Maximize Distance to Closest Person — LC 849


```java
// java
// LC 849. Maximize Distance to Closest Person

// V0-1
// IDEA (fixed by gpt)
/**
*  IDEA :
*
*  Explanation of the Code:
*    1.  Initial Setup:
*        •   lastOccupied keeps track of the index of the last seat occupied by a person.
*        •   maxDistance is initialized to 0 to store the maximum distance found.
*
*    2.  Iterate Through the Array:
*        •   When a seat is occupied (seats[i] == 1):
*        •   If it’s the first occupied seat, calculate the distance from the start of the array to this seat (i).
*        •   Otherwise, calculate the middle distance between the current and the last occupied seat using (i - lastOccupied) / 2.
*
*    3.  Check the Last Segment:
*        •   If the last seat is empty, calculate the distance from the last occupied seat to the end of the array (seats.length - 1 - lastOccupied).
*
*    4.  Return the Maximum Distance:
*        •   The value of maxDistance at the end of the loop is the answer.
*
*
* Example :
*  input : seats = [1, 0, 0, 0, 1, 0, 1]
*
*   Execution Steps:
*    1.  First occupied seat at index 0 → Distance to start = 0.
*    2.  Second occupied seat at index 4 → Middle distance = (4 - 0) / 2 = 2.
*    3.  Third occupied seat at index 6 → Middle distance = (6 - 4) / 2 = 1.
*    4.  No empty seats after the last occupied seat.
*    5.  maxDistance = 2.
*
*  output:  2
*
*/
/**
*  Cases
*
*  Case 1)  0001  ( all "0" till meat first "1")
*  Case 2)  1001001 (all "0" are enclosed by "1")
*  Case 3)  1001000 (there are "0" that NOT enclosed by "1" on the right hand side)
*
*/
public int maxDistToClosest_0_1(int[] seats) {
    int maxDistance = 0;
    int lastOccupied = -1;

    // Traverse the array to calculate maximum distances
    for (int i = 0; i < seats.length; i++) {
        /** NOTE !!! handle the seat val == 1 cases */
        if (seats[i] == 1) {
            if (lastOccupied == -1) {
                // Handle the case where the `first` occupied seat is found
                /**
                 * NOTE !!!
                 *
                 *  for handling below case:
                 *
                 *    e.g. :  0001
                 *
                 *  (so, elements are all "0" till first visit "1")
                 *  in this case, we still can get put a person to seat, and get distance
                 *
                 */
                maxDistance = i; // Distance from the start to the first occupied seat
            } else {
                // Calculate the distance to the closest person for the middle segment
                /** NOTE !!! need to divided by 2, since the person need to seat at `middle` seat */
                maxDistance = Math.max(maxDistance, (i - lastOccupied) / 2);
            }
            lastOccupied = i;
        }
    }

    // Handle the case where the last segment is empty
   /**
    *  NOTE !!!
    *
    *   the condition is actually quite straightforward,
    *   just need to check if the last element in array is "0"
    *   if is "0", means the array is NOT enclosed by "1"
    *   then we need to handle such case
    *   (example as below)
    *
    *   e.g.  100010000
    *
    */
    if (seats[seats.length - 1] == 0) {
        maxDistance = Math.max(maxDistance, seats.length - 1 - lastOccupied);
    }

    return maxDistance;
}
```
## Counting, Bookings & Simulation

### 9) Corporate Flight Bookings — LC 1109

```python
# LC 1109. Corporate Flight Bookings
# V1
# IDEA : ARRAY + prefix sum
# https://leetcode.com/problems/corporate-flight-bookings/discuss/328856/JavaC%2B%2BPython-Sweep-Line
# IDEA :
# Set the change of seats for each day.
# If booking = [i, j, k],
# it needs k more seat on ith day,
# and we don't need these seats on j+1th day.
# We accumulate these changes then we have the result that we want.
# Complexity
# Time O(booking + N) for one pass on bookings
# Space O(N) for the result
class Solution:
    def corpFlightBookings(self, bookings, n):
        res = [0] * (n + 1)
        for i, j, k in bookings:
            res[i - 1] += k
            res[j] -= k
        for i in range(1, n):
            res[i] += res[i - 1]
        return res[:-1]

# V1''
# IDEA : ARRAY
# https://leetcode.com/problems/corporate-flight-bookings/discuss/328893/Short-python-solution
# IDEA : Simply use two arrays to keep track of how many bookings are added for every flight.
class Solution:
    def corpFlightBookings(self, bookings: List[List[int]], n: int) -> List[int]:        
        opens = [0]*n
        closes = [0]*n
        
        for e in bookings:
            opens[e[0]-1] += e[2]
            closes[e[1]-1] += e[2]
            
        ret, tmp = [0]*n, 0
        for i in range(n):
            tmp += opens[i]
            ret[i] = tmp
            tmp -= closes[i]
            
        return ret
```

### 10) Bulb Switcher III — LC 1375

```python
# LC 1375. Bulb Switcher III
# V0
class Solution:
    def numTimesAllBlue(self, light):
        max_bulb_ind = 0
        count = 0
        turnedon_bulb = 0
        
        for bulb in light:
            max_bulb_ind = max(max_bulb_ind,bulb)
            turnedon_bulb += 1
            if turnedon_bulb == max_bulb_ind:
                count += 1
        
        return count
```

### 11) Robot Bounded In Circle — LC 1041

```python
# LC 1041. Robot Bounded In Circle
# V0
# IDEA : math + array
class Solution:
    def isRobotBounded(self, instructions):
        """
        NOTE !!! we make direction as below

         c == 'L':  move LEFT : [0,-1]
         c == 'R':  move RIGHT : [0,1]
        """
        dirs = [[0,1], [1,0], [0,-1], [-1,0]]
        x = 0;
        y = 0;
        idx = 0;
        for c in instructions:
            print ("c = " + str(c) + " idx = " + str(idx))
            """
            NOTE !!! since we need to verify if robot back to start point
                -> we use (idx + k)  % 4 for detecting cyclic cases
            """
            if c == 'L':
                idx = (idx + 3) % 4
            elif c == 'R':
                idx = (idx + 1) % 4
            elif c == 'G':
                x = x + dirs[idx][0]
                y = y + dirs[idx][1]
        return (x == 0 and y ==0) or idx !=0
```

### 12) Queue Reconstruction by Height — LC 406

```python
# LC 406 Queue Reconstruction by Height
class Solution(object):
    def reconstructQueue(self, people):
        people.sort(key = lambda x : (-x[0], x[1]))
        res = []
        # py insert syntax:
        # https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/python_trick_indexing.md#insert-into-a-list-in-place-
        # arr.insert(<index>, <value>)
        for p in people:
            res.insert(p[1], p)
        return res
```

### 13) Flatten 2D Vector — LC 251

```python
# LC 251. Flatten 2D Vector
# V0
# IDEA : ARRAY OP
class Vector2D:

    def __init__(self, v):
        # We need to iterate over the 2D vector, getting all the integers
        # out of it and putting them into the nums list.
        self.nums = []
        for inner_list in v:
            for num in inner_list:
                self.nums.append(num)
        # We'll keep position 1 behind the next number to return.
        self.position = -1

    def next(self):
        # Move up to the current element and return it.
        self.position += 1
        return self.nums[self.position]

    def hasNext(self):
        # If the next position is a valid index of nums, return True.
        return self.position + 1 < len(self.nums)
```
