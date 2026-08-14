"""

2559. Count Vowel Strings in Ranges
Medium

You are given a 0-indexed array of strings words and a 2D array of integers queries.

Each query queries[i] = [li, ri] asks us to find the number of strings present at the indices ranging from li to ri (both inclusive) of words that start and end with a vowel.

Return an array ans of size queries.length, where ans[i] is the answer to the ith query.

Note that the vowel letters are 'a', 'e', 'i', 'o', and 'u'.


Example 1:

Input: words = ["aba","bcb","ece","aa","e"], queries = [[0,2],[1,4],[1,1]]
Output: [2,3,0]
Explanation: The strings starting and ending with a vowel are "aba", "ece", "aa" and "e".
The answer to the query [0,2] is 2 (strings "aba" and "ece").
to query [1,4] is 3 (strings "ece", "aa", "e").
to query [1,1] is 0.
We return [2,3,0].

Example 2:

Input: words = ["a","e","i"], queries = [[0,2],[0,1],[2,2]]
Output: [3,2,1]
Explanation: Every string satisfies the conditions, so we return [3,2,1].


Constraints:

1 <= words.length <= 10^5
1 <= words[i].length <= 40
words[i] consists only of lowercase English letters.
sum(words[i].length) <= 3 * 10^5
1 <= queries.length <= 10^5
0 <= li <= ri < words.length

"""

# V0
# IDEA : PREFIX SUM
#
#   mark each word with 1 if it both starts and ends with a vowel, else 0,
#   then build presum[i] = number of marked words among words[0..i-1].
#   a query (l, r) is then answered in O(1) as presum[r+1] - presum[l].
#
#   NOTE : a length-1 word like "e" counts — w[0] and w[-1] are the same
#          character, so the single check naturally covers it.
#   NOTE : both queries.length and words.length reach 10^5, so per-query
#          rescanning would be O(n*m) — the prefix array is what keeps it linear.
#
# time = O(n + m), space = O(n)
class Solution(object):
    def vowelStrings(self, words, queries):
        vowels = set('aeiou')
        presum = [0] * (len(words) + 1)
        for i, w in enumerate(words):
            hit = 1 if (w[0] in vowels and w[-1] in vowels) else 0
            presum[i + 1] = presum[i] + hit
        return [presum[r + 1] - presum[l] for l, r in queries]
