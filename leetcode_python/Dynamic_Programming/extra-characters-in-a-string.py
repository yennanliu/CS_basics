"""

2707. Extra Characters in a String
Medium

You are given a 0-indexed string s and a dictionary of words dictionary. You have to break s
into one or more non-overlapping substrings such that each substring is present in
dictionary. There may be some extra characters in s which are not present in any of the
substrings.

Return the minimum number of extra characters left over if you break up s optimally.

Example 1:

Input: s = "leetscode", dictionary = ["leet","code","leetcode"]

Output: 1

Explanation: We can break s in two substrings: "leet" from index 0 to 3 and "code" from
index 5 to 8. There is only 1 unused character (at index 4), so we return 1.

Example 2:

Input: s = "sayhelloworld", dictionary = ["hello","world"]

Output: 3

Explanation: We can break s in two substrings: "hello" from index 3 to 7 and "world" from
index 8 to 12. The characters at indices 0, 1, 2 are not used in any substring and thus are
considered as extra characters. Hence, we return 3.

Constraints:

1 <= s.length <= 50
1 <= dictionary.length <= 50
1 <= dictionary[i].length <= 50
dictionary[i] and s consists of only lowercase English letters
dictionary contains distinct words

"""

# V0
# IDEA : 1D DP (BOTTOM-UP) + HASH SET  (word break, but count the leftovers)
#
#   dp[i] = min extra chars in the PREFIX s[0:i]   (dp[0] = 0, empty prefix)
#
#   for the last char s[i-1] there are only two choices:
#     - it is EXTRA                       -> dp[i - 1] + 1
#     - it ENDS a word s[j:i] in the dict -> dp[j]        (for any j < i)
#   dp[i] = min over all of them; the answer is dp[n].
#
#   e.g. s = "leetscode", dict = {leet, code, leetcode}
#        dp[4] = 0 ("leet"), dp[5] = 1 ("s" extra), dp[9] = dp[5] = 1 ("code")
#
# time = O(n^3) (n^2 (j, i) pairs, each slice + hash is O(n)), space = O(n + total dict chars)
class Solution(object):
    def minExtraChar(self, s, dictionary):
        """
        :type s: str
        :type dictionary: List[str]
        :rtype: int
        """
        words = set(dictionary)
        n = len(s)

        # dp[i] = min extra chars in s[0:i]
        dp = [0] * (n + 1)

        for i in range(1, n + 1):
            # default: s[i-1] is not covered by any word -> one more extra char
            dp[i] = dp[i - 1] + 1

            # try every word that ends exactly at index i-1
            for j in range(i):
                # NOTE !!! the word s[j:i] covers s[j..i-1], so we fall back to dp[j]
                if s[j:i] in words:
                    dp[i] = min(dp[i], dp[j])

        return dp[n]


# V1
# IDEA : TOP-DOWN DP (DFS + MEMO) + HASH SET
#
#   same recurrence, read from the other end:
#   best(start) = min extra chars in the SUFFIX s[start:]
#     - skip s[start] as extra            -> 1 + best(start + 1)
#     - s[start:end] is a dict word       -> best(end)
#   base case: best(n) = 0 (nothing left).
#
#   the memo dict makes each start computed once -> same bound as V0.
#   Easiest to derive live: write the recursion first, then add the memo.
#
# time = O(n^3), space = O(n + total dict chars)
class Solution2(object):
    def minExtraChar(self, s, dictionary):
        """
        :type s: str
        :type dictionary: List[str]
        :rtype: int
        """
        words = set(dictionary)
        n = len(s)
        memo = {}

        def best(start):
            """Min extra chars in s[start:]."""
            if start == n:
                return 0
            if start in memo:
                return memo[start]

            # option 1: s[start] is extra
            result = 1 + best(start + 1)

            # option 2: a dictionary word starts here
            for end in range(start + 1, n + 1):
                if s[start:end] in words:
                    result = min(result, best(end))

            memo[start] = result
            return result

        return best(0)


# V2
# IDEA : TRIE + 1D DP (BOTTOM-UP, FROM THE RIGHT)
#
#   V0/V1 pay O(n) per substring check (slice + hash). A trie removes that:
#   from each start, walk the trie one char at a time; the walk stops as soon
#   as no word has that prefix, and every node marked is_word is a word
#   s[start..end] found in O(1) extra work.
#
#   dp[start] = min extra chars in s[start:]   (dp[n] = 0)
#     dp[start] = dp[start + 1] + 1                     (s[start] extra)
#     dp[start] = min(dp[start], dp[end + 1])           (s[start..end] is a word)
#
#   This is the Java version's approach (leetcode_java/.../Trie/ExtraCharactersInAString.java).
#
# time = O(n^2 + total dict chars), space = O(n + total dict chars)
class TrieNode(object):
    def __init__(self):
        self.children = {}
        self.is_word = False


class Solution3(object):
    def minExtraChar(self, s, dictionary):
        """
        :type s: str
        :type dictionary: List[str]
        :rtype: int
        """
        # build the trie of every dictionary word
        root = TrieNode()
        for word in dictionary:
            node = root
            for ch in word:
                if ch not in node.children:
                    node.children[ch] = TrieNode()
                node = node.children[ch]
            node.is_word = True

        n = len(s)
        # dp[start] = min extra chars in s[start:]
        dp = [0] * (n + 1)

        for start in range(n - 1, -1, -1):
            # default: s[start] is extra
            dp[start] = dp[start + 1] + 1

            # walk the trie along s[start], s[start+1], ...
            node = root
            for end in range(start, n):
                ch = s[end]
                if ch not in node.children:
                    # no dictionary word has this prefix -> stop early
                    break
                node = node.children[ch]
                if node.is_word:
                    # s[start..end] is a word -> the rest starts at end + 1
                    dp[start] = min(dp[start], dp[end + 1])

        return dp[0]
