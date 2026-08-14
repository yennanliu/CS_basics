"""

2085. Count Common Words With One Occurrence
Easy

Given two string arrays words1 and words2, return the number of strings that appear exactly once in each of the two arrays.


Example 1:

Input: words1 = ["leetcode","is","amazing","as","is"], words2 = ["amazing","leetcode","is"]
Output: 2
Explanation:
- "leetcode" appears exactly once in each of the two arrays. We count this string.
- "amazing" appears exactly once in each of the two arrays. We count this string.
- "is" appears in each of the two arrays, but there are 2 occurrences of it in words1. We do not count this string.
- "as" appears once in words1, but does not appear in words2. We do not count this string.
Thus, there are 2 strings that appear exactly once in each of the two arrays.

Example 2:

Input: words1 = ["b","bb","bbb"], words2 = ["a","aa","aaa"]
Output: 0
Explanation: There are no strings that appear in each of the two arrays.

Example 3:

Input: words1 = ["a","ab"], words2 = ["a","a","a","ab"]
Output: 1
Explanation: The only string that appears exactly once in each of the two arrays is "a".


Constraints:

1 <= words1.length, words2.length <= 1000
1 <= words1[i].length, words2[j].length <= 30
words1[i] and words2[j] consists only of lowercase English letters.

"""

# V0
# IDEA : TWO COUNTERS, KEEP THE WORDS WITH COUNT == 1 IN BOTH
#
#   "exactly once in EACH array" — both counts must equal 1, so intersecting
#   the two count-1 key sets is the whole job.
#
# time = O(n1 + n2), space = O(n1 + n2)
from collections import Counter


class Solution(object):
    def countWords(self, words1, words2):
        c1, c2 = Counter(words1), Counter(words2)
        return sum(1 for w, c in c1.items() if c == 1 and c2[w] == 1)
