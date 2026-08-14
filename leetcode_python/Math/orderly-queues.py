"""

899. Orderly Queue
Hard

You are given a string s and an integer k. You can choose one of the first k letters
of s and append it at the end of the string.

Return the lexicographically smallest string you could have after applying the
mentioned step any number of moves.


Example 1:

Input: s = "cba", k = 1
Output: "acb"
Explanation:
In the first move, we move the 1st character 'c' to the end, obtaining the string "bac".
In the second move, we move the 1st character 'b' to the end, obtaining the final
result "acb".

Example 2:

Input: s = "baaca", k = 3
Output: "aaabc"
Explanation:
In the first move, we move the 1st character 'b' to the end, obtaining the string "aacab".
In the second move, we move the 3rd character 'c' to the end, obtaining the final
result "aaabc".


Constraints:

1 <= k <= s.length <= 1000
s consist of lowercase English letters.

"""

# V0
# IDEA : MATH / CASE SPLIT ON k
"""
 k == 1:
    the only thing we can do is rotate the string, so there are exactly
    len(s) reachable strings -> just take the smallest rotation.

 k >= 2:
    we can swap ANY two adjacent characters, therefore any permutation is
    reachable -> the answer is simply the sorted string.

    why? for "abc[xy]def" move a, b, c to the end   -> "[xy]defabc"
                            move y, then x to end   -> "defabc[yx]"
                            move d, e, f to the end -> "abc[yx]def"
    i.e. x and y got swapped and nothing else changed.
"""
# time = O(n^2)  # building n rotations of length n when k == 1
# space = O(n)
class Solution(object):
    def orderlyQueue(self, s, k):
        if k > 1:
            # any permutation reachable -> sorted string is the smallest
            return "".join(sorted(s))

        # k == 1 -> only rotations are reachable
        n = len(s)
        return min(s[i:] + s[:i] for i in range(n))
