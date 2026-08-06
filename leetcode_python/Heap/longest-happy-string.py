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
