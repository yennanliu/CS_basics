"""

821. Shortest Distance to a Character
Easy

Given a string s and a character c that occurs in s, return an array of integers answer where answer.length == s.length and answer[i] is the distance from index i to the closest occurrence of character c in s.

The distance between two indices i and j is abs(i - j), where abs is the absolute value function.

Example 1:

Input: s = "loveleetcode", c = "e"
Output: [3,2,1,0,1,0,0,1,2,2,1,0]
Explanation: The character 'e' appears at indices 3, 5, 6, and 11 (0-indexed).
The closest occurrence of 'e' for index 0 is at index 3, so the distance is abs(0 - 3) = 3.
The closest occurrence of 'e' for index 1 is at index 3, so the distance is abs(1 - 3) = 2.
For index 4, there is a tie between the 'e' at index 3 and the 'e' at index 5, but the distance is still the same: abs(4 - 3) == abs(4 - 5) = 1.
The closest occurrence of 'e' for index 8 is at index 6, so the distance is abs(8 - 6) = 2.

Example 2:

Input: s = "aaab", c = "b"
Output: [3,2,1,0]

Constraints:

1 <= s.length <= 10^4
s[i] and c are lowercase English letters.
It is guaranteed that c occurs at least once in s.

"""

# V0 


# V1
# https://nifannn.github.io/2018/09/14/%E7%AE%97%E6%B3%95%E7%AC%94%E8%AE%B0-Leetcode-821-Shortest-Distance-to-a-Character/
# time = O(n)
# space = O(1)
class Solution:
    def shortestToChar(self, S, C):
        """
        :type S: str
        :type C: str
        :rtype: List[int]
        """
        ans = [float('inf')]
        for i, char in enumerate(S):
            curDist = 0 if char == C else ans[i] + 1
            ans.append(curDist)
        temp = float('inf')
        for i, char in enumerate(S[::-1]):
            temp = 0 if char == C else temp + 1
            ans[-1-i] = min(ans[-1-i], temp)
        return ans[1:]
        
# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/80471765
# https://nifannn.github.io/2018/09/14/%E7%AE%97%E6%B3%95%E7%AC%94%E8%AE%B0-Leetcode-821-Shortest-Distance-to-a-Character/
# time = O(n)
# space = O(1)
class Solution:
    def shortestToChar(self, S, C):
        """
        :type S: str
        :type C: str
        :rtype: List[int]
        """
        _len = len(S)
        index = -1000000
        ans = [0] * _len
        for i, s in enumerate(S):
            if s == C:
                index = i
            ans[i] = abs(i - index)
        index = -100000
        for i in range(_len - 1, -1 , -1):
            if S[i] == C:
                index = i
            ans[i] = min(abs(i - index), ans[i])
        return ans

# V1''
# http://bookshadow.com/weblog/2018/04/22/leetcode-shortest-distance-to-a-character/
# time = O(n)
# space = O(1)
class Solution(object):
    def shortestToChar(self, S, C):
        """
        :type S: str
        :type C: str
        :rtype: List[int]
        """
        INF = 0x7FFFFFFF
        N = len(S)
        ans = [INF] * N
        lastC = -INF
        for i in range(N):
            if S[i] == C: lastC = i
            ans[i] = min(ans[i], i - lastC)

        lastC = INF
        for i in range(N - 1, -1, -1):
            if S[i] == C: lastC = i
            ans[i] = min(ans[i], lastC - i)
        return ans

# V2
# time = O(n)
# space = O(1)
import itertools
class Solution(object):
    def shortestToChar(self, S, C):
        """
        :type S: str
        :type C: str
        :rtype: List[int]
        """
        result = [len(S)] * len(S)
        prev = -len(S)
        for i in itertools.chain(range(len(S)),
                                 reversed(range(len(S)))):
            if S[i] == C:
                prev = i
            result[i] = min(result[i], abs(i-prev))
        return result

