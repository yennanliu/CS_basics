"""

1156. Swap For Longest Repeated Character Substring
Medium

You are given a string text. You can swap two of the characters in the text.

Return the length of the longest substring with repeated characters.


Example 1:

Input: text = "ababa"
Output: 3
Explanation: We can swap the first 'b' with the last 'a', or the last 'b' with the first 'a'.
Then, the longest repeated character substring is "aaa" with length 3.

Example 2:

Input: text = "aaabaaa"
Output: 6
Explanation: Swap 'b' with the last 'a' (or the first 'a'),
and we get longest repeated character substring "aaaaaa" with length 6.

Example 3:

Input: text = "aaaaa"
Output: 5
Explanation: No need to swap, longest repeated character substring is "aaaaa"
with length is 5.


Constraints:

1 <= text.length <= 2 * 10^4
text consist of lowercase English characters only.

"""

# V0
# IDEA : TWO POINTERS + counting
#        for each run text[i..j-1] of a same char c (length l):
#          - skip the ONE blocking char at index j
#          - take the next run of c right after it (length r)
#          - by swapping we can join them : l + r + 1
#        but we can never use more c's than we own -> cap by cnt[c]
#        answer = max over all runs of min(l + r + 1, cnt[c])
# time = O(n)
# space = O(1)
from collections import Counter
class Solution(object):
    def maxRepOpt1(self, text):
        cnt = Counter(text)
        n = len(text)
        res = 0
        i = 0
        while i < n:
            # run of same char starting at i -> text[i..j-1]
            j = i
            while j < n and text[j] == text[i]:
                j += 1
            l = j - i

            # NOTE !!! skip text[j] (the blocker), then take the run after it
            k = j + 1
            while k < n and text[k] == text[i]:
                k += 1
            r = k - j - 1

            res = max(res, min(l + r + 1, cnt[text[i]]))
            i = j
        return res
