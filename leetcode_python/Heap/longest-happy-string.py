# https://leetcode.com/problems/longest-happy-string/description/

"""

1405. Longest Happy String
Solved
Medium
Topics
premium lock icon
Companies
Hint
A string s is called happy if it satisfies the following conditions:

s only contains the letters 'a', 'b', and 'c'.
s does not contain any of "aaa", "bbb", or "ccc" as a substring.
s contains at most a occurrences of the letter 'a'.
s contains at most b occurrences of the letter 'b'.
s contains at most c occurrences of the letter 'c'.
Given three integers a, b, and c, return the longest possible happy string. If there are multiple longest happy strings, return any of them. If there is no such string, return the empty string "".

A substring is a contiguous sequence of characters within a string.

 

Example 1:

Input: a = 1, b = 1, c = 7
Output: "ccaccbcc"
Explanation: "ccbccacc" would also be a correct answer.
Example 2:

Input: a = 7, b = 1, c = 0
Output: "aabaa"
Explanation: It is the only correct answer in this case.
 

Constraints:

0 <= a, b, c <= 100
a + b + c > 0

"""

# V0
class Solution(object):
    def longestDiverseString(self, a, b, c):
        """
        :type a: int
        :type b: int
        :type c: int
        :rtype: str
        """
        pass


# V0-1
# IDEA: PQ (gemini)
"""
NOTE !!!


we ONLY `append` ONE element at a time (every iteration)

e.g. 

per below input, we DON'T append "aa" at once,
-> we do it in 2 iteration. doing so make our logic clean, and simple

```
Input: a = 7, b = 1, c = 0
Output: "aabaa"
```


---


Dry run



**We do it in different iterations.**

The code appends exactly **one character per loop cycle**. It never appends `"aa"` all at once.

Here is exactly how the `while` loop handles your input of `a = 7, b = 1, c = 0`:

1. **Iteration 1:**
We pop `'a'`. The string is empty, so it's safe.
We append **one** `'a'`.
`res = ["a"]`


2. **Iteration 2:**
We pop `'a'` again (because it still has the highest count).
The string only has one character, so it passes the consecutive check.
We append **one** `'a'`.
`res = ["a", "a"]`


3. **Iteration 3:**
We pop `'a'` again.
This time, `res[-1]` and `res[-2]` are both `'a'`, triggering our `if` statement!
We hold `'a'` and pop `'b'` instead. We append **one** `'b'`.
`res = ["a", "a", "b"]`


4. **Iteration 4:**
We pop `'a'`.
`res[-1]` is now `'b'`, so the consecutive check passes safely.
We append **one** `'a'`.
`res = ["a", "a", "b", "a"]`


### Why do it this way?

Appending one character at a time makes the logic incredibly simple and completely immune to edge cases.

If you try to append `"aa"` at once, you run into messy math when you only have `1` count of a character left, or when the *second* most frequent character also needs to be appended multiple times. By taking it one step at a time, the Greedy algorithm naturally builds the perfect string.


"""
import heapq

class Solution(object):
    def longestDiverseString(self, a, b, c):
        """
        :type a: int
        :type b: int
        :type c: int
        :rtype: str
        """
        # 1. Initialize a max-heap with negative counts (to simulate max-heap)
        pq = []
        if a > 0: heapq.heappush(pq, (-a, 'a'))
        if b > 0: heapq.heappush(pq, (-b, 'b'))
        if c > 0: heapq.heappush(pq, (-c, 'c'))
        
        res = []
        
        while pq:
            # 2. Pop the character we have the most of
            count1, char1 = heapq.heappop(pq)
            
            # 3. Does adding this character create 3 in a row?
            if len(res) >= 2 and res[-1] == char1 and res[-2] == char1:
                # We can't use char1. We must use the second most frequent character.
                if not pq:
                    break # No other characters available! We must stop to avoid "aaa".
                
                count2, char2 = heapq.heappop(pq)
                res.append(char2)
                count2 += 1 # Decrement magnitude (since it is negative)
                
                # Push char2 back if we still have more of it
                if count2 != 0:
                    heapq.heappush(pq, (count2, char2))
                    
                # Push char1 back so we can try to use it on the NEXT turn
                heapq.heappush(pq, (count1, char1))
                
            else:
                # 4. Safe to use the most frequent character
                res.append(char1)
                count1 += 1
                
                # Push char1 back if we still have more of it
                if count1 != 0:
                    heapq.heappush(pq, (count1, char1))
                    
        return "".join(res)



# V0-2
# IDEA: PQ
# https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/LeetCodeJava/Heap/LongestHappyString.java#L51
import heapq

class Solution(object):
    def longestDiverseString(self, a, b, c):
        """
        :type a: int
        :type b: int
        :type c: int
        :rtype: str
        """
        # Max-heap using negative counts
        # Tuple format: (-count, char)
        pq = []
        if a > 0:
            heapq.heappush(pq, (-a, 'a'))
        if b > 0:
            heapq.heappush(pq, (-b, 'b'))
        if c > 0:
            heapq.heappush(pq, (-c, 'c'))

        res = []

        while pq:
            first_cnt, first_val = heapq.heappop(pq)

            # NOTE !!!
            # ONLY 2 cases below:
            # 1. if adding this char causes 3 consecutive
            # 2. else (can add cur char)

            # case 1) if adding this char causes 3 consecutive
            if len(res) >= 2 and res[-1] == first_val and res[-2] == first_val:
                
                # edge case
                if not pq:
                    break
                
                # if use cur val may cause `3 consecutive`,
                # we'll pop the `next` element in PQ
                second_cnt, second_val = heapq.heappop(pq)

                res.append(second_val)
                # Decrement the absolute count (add 1 because it is negative)
                second_cnt += 1

                # ONLY add `second` element back to PQ if its count still > 0
                # (meaning less than 0 since it is negative)
                if second_cnt < 0:
                    heapq.heappush(pq, (second_cnt, second_val))

                # DON'T forget to add `first` element back to PQ,
                # since it was NOT used
                heapq.heappush(pq, (first_cnt, first_val))

            # case 2) else (can add cur char)
            else:
                res.append(first_val)
                first_cnt += 1

                # ONLY add `first` element back to PQ if its count still > 0
                if first_cnt < 0:
                    heapq.heappush(pq, (first_cnt, first_val))

        # Join the list into a single string
        return "".join(res)


# V0-2
# IDEA: PQ (gpt)
import heapq

class ValCnt:
    def __init__(self, val, cnt):
        self.val = val
        self.cnt = cnt

    # Max-heap by count
    def __lt__(self, other):
        return self.cnt > other.cnt


class Solution(object):
    def longestDiverseString(self, a, b, c):
        """
        :type a: int
        :type b: int
        :type c: int
        :rtype: str
        """

        pq = []

        if a > 0:
            heapq.heappush(pq, ValCnt('a', a))
        if b > 0:
            heapq.heappush(pq, ValCnt('b', b))
        if c > 0:
            heapq.heappush(pq, ValCnt('c', c))

        res = []

        while pq:

            first = heapq.heappop(pq)

            # Case 1: adding first would make 3 consecutive chars
            if (len(res) >= 2 and
                res[-1] == first.val and
                res[-2] == first.val):

                if not pq:
                    break

                second = heapq.heappop(pq)

                res.append(second.val)
                second.cnt -= 1

                if second.cnt > 0:
                    heapq.heappush(pq, second)

                # first wasn't used
                heapq.heappush(pq, first)

            # Case 2: safe to use first
            else:

                use = 1

                # Greedily use two copies if beneficial
                if first.cnt >= 2:
                    if not pq:
                        use = 2
                    else:
                        second = pq[0]
                        if first.cnt > second.cnt:
                            use = 2

                for _ in range(use):
                    res.append(first.val)

                first.cnt -= use

                if first.cnt > 0:
                    heapq.heappush(pq, first)

        return "".join(res)


# V1


# V2-1
# IDEA: PQ
# https://leetcode.com/problems/longest-happy-string/editorial/
class Solution:
    def longestDiverseString(self, a: int, b: int, c: int) -> str:
        pq = []
        if a > 0:
            heapq.heappush(pq, (-a, "a"))
        if b > 0:
            heapq.heappush(pq, (-b, "b"))
        if c > 0:
            heapq.heappush(pq, (-c, "c"))

        result = []
        while pq:
            count, character = heapq.heappop(pq)
            count = -count
            if (
                len(result) >= 2
                and result[-1] == character
                and result[-2] == character
            ):
                if not pq:
                    break
                tempCnt, tempChar = heapq.heappop(pq)
                result.append(tempChar)
                if (tempCnt + 1) < 0:
                    heapq.heappush(pq, (tempCnt + 1, tempChar))
                heapq.heappush(pq, (-count, character))
            else:
                count -= 1
                result.append(character)
                if count > 0:
                    heapq.heappush(pq, (-count, character))

        return "".join(result)


# V2-2
# IDEA: COUNTER
# https://leetcode.com/problems/longest-happy-string/editorial/
class Solution:
    def longestDiverseString(self, a: int, b: int, c: int) -> str:
        curra, currb, currc = 0, 0, 0
        # Maximum total iterations possible is given by the sum of a, b, and c.
        total_iterations = a + b + c
        result = []

        for i in range(total_iterations):
            if (a >= b and a >= c and curra != 2) or (
                a > 0 and (currb == 2 or currc == 2)
            ):
                # If 'a' is maximum and its streak is less than 2, or if streak of 'b' or 'c' is 2, then 'a' will be the next character.
                result.append("a")
                a -= 1
                curra += 1
                currb = 0
                currc = 0
            elif (b >= a and b >= c and currb != 2) or (
                b > 0 and (currc == 2 or curra == 2)
            ):
                # If 'b' is maximum and its streak is less than 2, or if streak of 'a' or 'c' is 2, then 'b' will be the next character.
                result.append("b")
                b -= 1
                currb += 1
                curra = 0
                currc = 0
            elif (c >= a and c >= b and currc != 2) or (
                c > 0 and (curra == 2 or currb == 2)
            ):
                # If 'c' is maximum and its streak is less than 2, or if streak of 'a' or 'b' is 2, then 'c' will be the next character.
                result.append("c")
                c -= 1
                currc += 1
                curra = 0
                currb = 0

        return "".join(result)
