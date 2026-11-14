"""

2657. Find the Prefix Common Array of Two Arrays
Medium

You are given two 0-indexed integer permutations A and B of length n.

A prefix common array of A and B is an array C such that C[i] is equal to the
count of numbers that are present at or before the index i in both A and B.

Return the prefix common array of A and B.

A sequence of n integers is called a permutation if it contains all integers
from 1 to n exactly once.

Example 1:

Input: A = [1,3,2,4], B = [3,1,2,4]

Output: [0,2,3,4]

Explanation:

At i = 0: no number is common, so C[0] = 0.
At i = 1: 1 and 3 are common in A and B, so C[1] = 2.
At i = 2: 1, 2, and 3 are common in A and B, so C[2] = 3.
At i = 3: 1, 2, 3, and 4 are common in A and B, so C[3] = 4.

Example 2:

Input: A = [2,3,1], B = [3,1,2]

Output: [0,1,3]

Explanation:

At i = 0: no number is common, so C[0] = 0.
At i = 1: only 3 is common in A and B, so C[1] = 1.
At i = 2: 1, 2, and 3 are common in A and B, so C[2] = 3.

Constraints:

1 <= A.length == B.length == n <= 50
1 <= A[i], B[i] <= n
It is guaranteed that A and B are both a permutation of n integers.

"""

# V0
class Solution(object):
    def findThePrefixCommonArray(self, A, B):
        """
        :type A: List[int]
        :type B: List[int]
        :rtype: List[int]
        """
        pass


# V0-1
# IDEA: COUNTER + val_a is common, val_b is common check (gpt)
from collections import Counter

class Solution(object):
    def findThePrefixCommonArray(self, A, B):
        """
        :type A: List[int]
        :type B: List[int]
        :rtype: List[int]
        """
        # Edge case
        if not A or not B:
            return []

        cnt_a = Counter()
        cnt_b = Counter()

        n = len(A)
        res = [0] * n

        common = 0

        for i in range(n):
            val_a = A[i]
            val_b = B[i]

            # Update frequency
            cnt_a[val_a] += 1
            cnt_b[val_b] += 1

            # Check whether val_a becomes common
            if cnt_a[val_a] == 1 and cnt_b[val_a] > 0:
                common += 1

            # Check whether val_b becomes common
            if val_b != val_a:
                if cnt_b[val_b] == 1 and cnt_a[val_b] > 0:
                    common += 1

            res[i] = common

        return res


# V0-2
# IDEA: SET + val_a is common, val_b is common check (gpt)
class Solution(object):
    def findThePrefixCommonArray(self, A, B):
        if not A or not B:
            return []

        seen_a = set()
        seen_b = set()

        res = []
        common = 0

        for i in range(len(A)):
            a = A[i]
            b = B[i]

            seen_a.add(a)
            seen_b.add(b)

            if a in seen_b:
                common += 1

            if b != a and b in seen_a:
                common += 1

            res.append(common)

        return res


# V0-3
# IDEA: SET + val_a is common, val_b is common check (GEMINI)
class Solution(object):
    def findThePrefixCommonArray(self, A, B):
        """
        :type A: List[int]
        :type B: List[int]
        :rtype: List[int]
        """
        if not A or not B:
            return []

        n = len(A)
        res = [0] * n
        
        seen_a = set()
        seen_b = set()
        common = 0

        for i in range(n):
            val_a = A[i]
            val_b = B[i]

            seen_a.add(val_a)
            seen_b.add(val_b)

            # 檢查新加入的 val_a 是否已經在 B 的前綴中出現過
            if val_a in seen_b:
                common += 1
            
            # 檢查新加入的 val_b 是否已經在 A 的前綴中出現過（注意：若 val_a == val_b 避免重複計算）
            if val_b in seen_a and val_b != val_a:
                common += 1

            res[i] = common

        return res


# V0-4
# IDEA : FREQUENCY ARRAY + RUNNING COMMON COUNT
#
#   A and B are permutations, so every value appears at most ONCE in each
#   array, hence at most twice across both. A value becomes common at the exact
#   moment its count reaches 2 - and it stays common for every later prefix.
#   So keep one running counter and bump it whenever a count hits 2; no need
#   to recompute an intersection per index.
#
#   e.g. A = [1,3,2,4], B = [3,1,2,4]
#        i=0: +1 (freq 1), +3 (freq 1)          -> common = 0
#        i=1: +3 (freq 2 !), +1 (freq 2 !)      -> common = 2
#        i=2: +2 (freq 1), +2 (freq 2 !)        -> common = 3
#
#   (Brute force - intersect set(A[:i+1]) and set(B[:i+1]) at every i - is
#    O(n^2); fine for n <= 50, but the running count is the answer to give.)
#
# time = O(n), space = O(n)
class Solution(object):
    def findThePrefixCommonArray(self, A, B):
        """
        :type A: List[int]
        :type B: List[int]
        :rtype: List[int]
        """
        n = len(A)

        # values are 1..n, so index 0 is unused
        freq = [0] * (n + 1)
        common = 0
        result = []

        for i in range(n):
            # take A[i] into the prefix
            freq[A[i]] += 1
            # NOTE !!! count == 2 means it has now been seen in BOTH arrays
            #          (A[i] == B[i] also works: both updates land on one cell)
            if freq[A[i]] == 2:
                common += 1

            # take B[i] into the prefix
            freq[B[i]] += 1
            if freq[B[i]] == 2:
                common += 1

            result.append(common)

        return result


# V1
# IDEA : TWO HASH SETS - COUNT A VALUE WHEN ITS SECOND SIDE ARRIVES
#
#   Keep the values seen so far in A and in B separately. When A[i] arrives,
#   it becomes common iff B's prefix already holds it; symmetrically for B[i]
#   against A's prefix. Adding A[i] to seen_a BEFORE checking B[i] makes the
#   A[i] == B[i] case count exactly once.
#
#   Unlike V0 this does not rely on values being 1..n, so it is the version
#   that survives a follow-up like "values are arbitrary but unique per array".
#
# time = O(n), space = O(n)
class Solution2(object):
    def findThePrefixCommonArray(self, A, B):
        """
        :type A: List[int]
        :type B: List[int]
        :rtype: List[int]
        """
        seen_a = set()
        seen_b = set()
        common = 0
        result = []

        for i in range(len(A)):
            # A[i] is common if B's prefix already contains it
            if A[i] in seen_b:
                common += 1
            seen_a.add(A[i])

            # B[i] is common if A's prefix (now including A[i]) contains it
            if B[i] in seen_a:
                common += 1
            seen_b.add(B[i])

            result.append(common)

        return result
