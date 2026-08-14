"""

2515. Shortest Distance to Target String in a Circular Array
Easy

You are given a 0-indexed circular string array words and a string target. A circular array means that the array's end connects to the array's beginning.

Formally, the next element of words[i] is words[(i + 1) % n] and the previous element of words[i] is words[(i - 1 + n) % n], where n is the length of words.

Starting from startIndex, you can move to either the next word or the previous word with 1 step at a time.

Return the shortest distance needed to reach the string target. If the string target does not exist in words, return -1.


Example 1:

Input: words = ["hello","i","am","leetcode","hello"], target = "hello", startIndex = 1
Output: 1
Explanation: We start from index 1 and can reach "hello" by
- moving 3 units to the right to reach index 4.
- moving 2 units to the left to reach index 4.
- moving 4 units to the right to reach index 0.
- moving 1 unit to the left to reach index 0.
The shortest distance to reach "hello" is 1.

Example 2:

Input: words = ["a","b","leetcode"], target = "leetcode", startIndex = 0
Output: 1
Explanation: We start from index 0 and can reach "leetcode" by
- moving 2 units to the right to reach index 2.
- moving 1 unit to the left to reach index 2.
The shortest distance to reach "leetcode" is 1.

Example 3:

Input: words = ["i","eat","leetcode"], target = "ate", startIndex = 0
Output: -1
Explanation: Since "ate" does not exist in words, we return -1.


Constraints:

1 <= words.length <= 100
1 <= words[i].length <= 100
words[i] and target consist of only lowercase English letters.
0 <= startIndex < words.length

"""

# V0
# IDEA : ONE PASS (circular distance = min(clockwise, counter-clockwise))
#
#   on a ring of size n, the gap between index i and startIndex going one way
#   is t = abs(i - startIndex), and going the other way it is n - t. so the
#   real distance to that occurrence is min(t, n - t).
#
#   scan every index holding `target` and keep the smallest such distance.
#
#   NOTE : seed the answer with n (a strictly unreachable value, since any real
#          circular distance is <= n // 2) so "still n at the end" cleanly means
#          target was never found -> return -1.
#
# time = O(n * L), space = O(1)
class Solution(object):
    def closestTarget(self, words, target, startIndex):
        n = len(words)
        res = n
        for i, w in enumerate(words):
            if w == target:
                t = abs(i - startIndex)
                res = min(res, t, n - t)
        return -1 if res == n else res
