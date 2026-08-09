"""

4007. Widest Possible Fence
Solved
Medium
premium lock icon
Companies
Hint
You are given an integer array planks, where planks[i] represents the height of the ith wooden plank. Each plank has a width of 1 unit.

You want to build a fence consisting of planks that all have the same height.

You may either use a plank as is, or combine exactly two distinct original planks into a single plank whose height equals the sum of their heights. Each original plank can be used at most once, and not all original planks need to be used.

Return the maximum possible width of the fence that can be built.

 

Example 1:

Input: planks = [1,3,2,5,7,5,4,2,1]

Output: 4

Explanation:

We can have four planks of height 5.

planks[3] = 5
planks[5] = 5
planks[0] + planks[6] = 1 + 4 = 5
planks[1] + planks[2] = 3 + 2 = 5
Hence, the maximum width is 4.

Example 2:

Input: planks = [2,3,7]

Output: 1

Explanation:

It is impossible to form two planks of the same height, even after combining two distinct original planks.
Since not all original planks need to be used, we can choose any one plank as the fence.
Therefore, the maximum possible width is 1.
 

Constraints:

1 <= planks.length <= 1000
1 <= planks[i] <= 109

"""


"""
NOTE !!!


why `hashmap + prefix` approach is NOT working for this LC ?

->

1. Not a Subarray Problem: 
   The problem allows you to combine any two planks 
   in the array regardless of their indices.
   A prefix sum only works if elements must be adjacent.

2. Incorrect Target Assumption: 
   The code assumes the best fence height (target) 
   is simply the height of the most frequent single plank.
   While logical, the optimal height might be formed entirely 
   by pairing different planks together 
   (e.g., [1, 9, 2, 8, 3, 7] can form a fence of 
   width 3 at height 10, even though a 10 plank doesn't 
   exist natively).

3. Combination Math: The problem limits combinations 
   to exactly two original planks.


"""

# V0
class Solution(object):
    def maximumWidth(self, planks):
        """
        :type planks: List[int]
        :rtype: int
        """
        pass


# V0-1
# IDEA: HASH MAP PAIRING (gemini)
"""
CORE IDEA V1:


The Core Logic
For every possible total width, we accumulate the number of groups 

we can form based on 3 rules:



- Distinct Pairs (x, y): 
		min(freq[x], freq[y]) determines 
		how many x + y groups we can make.


- Identical Pairs (x, x):
	freq[x] // 2 determines
	how many x + x groups we can make 
	using the same length plank.


- Single Planks (x):
	freq[x] represents planks that can 
	just be used as a group on their own.


-> 

By calculating and accumulating these values
for every possible combination, 
we can find the target width that yields 
the highest number of groups.


"""

"""
CORE IDEA: V2:

### Key Idea

The main idea is to use a **frequency map** and try every possible pair of plank widths.

Suppose:

```python
planks = [1, 2, 2, 3, 3, 4]
```

The frequency map is:

```python
freq = {
    1: 1,
    2: 2,
    3: 2,
    4: 1
}
```

For a target width `5`, we can form:

```text
1 + 4
2 + 3
2 + 3
```

So we can make `3` groups.

#### 1. Two different widths

For a pair `(x, y)`, the number of `x + y` groups we can make is:

```python
min(freq[x], freq[y])
```

For example:

```python
freq[2] = 2
freq[3] = 2

min(2, 2) = 2
```

Therefore, `2 + 3` can form `2` groups.

#### 2. Two equal widths

For `(x, x)`, each group needs two copies of `x`:

```python
freq[x] // 2
```

For example:

```python
freq[2] = 2

2 // 2 = 1
```

So we can form one `2 + 2` group.

#### 3. A single plank

A single plank can also form a group, so for width `x`:

```python
freq[x]
```

groups are possible.

### Algorithm

For every pair of distinct widths `(x, y)`:

1. Calculate the target width `x + y`.
2. Add `min(freq[x], freq[y])` groups to that target width.

For every width `x`:

1. Add `freq[x]` for single-plank groups.
2. Add `freq[x] // 2` for `x + x` groups.

Finally, return the maximum number of groups obtained for any target width.

### Complexity

If there are `k` distinct plank widths:

* **Time:** `O(k²)`
* **Space:** `O(k)`

The important insight is:

> **We don't need to choose a target width first. Instead, enumerate every possible pair of plank widths and count how many groups can produce each total width.**


"""
from collections import Counter, defaultdict

class Solution(object):
    def maximumWidth(self, planks):
        """
        :type planks: List[int]
        :rtype: int
        """
        # Count the available frequencies of each plank height
        cnt = Counter(planks)
        
        # t[h] will store the maximum width of a fence built at height 'h'
        t = defaultdict(int)
        
        # Iterate through every unique plank height
        for x, v1 in cnt.items():
            
            # 1. We can use the plank 'x' exactly as is
            t[x] += v1
            
            # 2. We can combine two identical planks of height 'x' to form '2x'
            t[x * 2] += v1 // 2
            
            # 3. We can combine plank 'x' with a different plank 'y'
            for y, v2 in cnt.items():
                # Ensure we only process each (x, y) pair once by enforcing y > x
                if y > x:
                    t[x + y] += min(v1, v2)
                    
        # The optimal solution is simply the maximum width across all heights
        return max(t.values())


# V0-2
# IDEA: HASH MAP (gpt)
from collections import Counter

class Solution(object):
    def maximumWidth(self, planks):
        """
        :type planks: List[int]
        :rtype: int
        """
        freq = Counter(planks)
        values = list(freq.keys())

        # mp[width] = maximum number of groups
        # that can have this total width
        mp = {}

        ans = 1

        for i in range(len(values)):
            x = values[i]
            fx = freq[x]

            # Group: [x]
            mp[x] = mp.get(x, 0) + fx
            ans = max(ans, mp[x])

            # Group: [x, x]
            width = 2 * x
            cnt = fx // 2

            mp[width] = mp.get(width, 0) + cnt
            ans = max(ans, mp[width])

            # Group: [x, y]
            for j in range(i + 1, len(values)):
                y = values[j]
                fy = freq[y]

                width = x + y
                cnt = min(fx, fy)

                mp[width] = mp.get(width, 0) + cnt
                ans = max(ans, mp[width])

        return ans


# V1


# V2
