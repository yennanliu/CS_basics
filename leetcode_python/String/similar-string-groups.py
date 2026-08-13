"""

839. Similar String Groups
Hard

Two strings, X and Y, are considered similar if either they are identical or we can
make them equivalent by swapping at most two letters (in distinct positions) within
the string X.

For example, "tars" and "rats" are similar (swapping at positions 0 and 2), and
"rats" and "arts" are similar, but "star" is not similar to "tars", "rats", or "arts".

Together, these form two connected groups by similarity: {"tars", "rats", "arts"}
and {"star"}. Notice that "tars" and "arts" are in the same group even though they
are not similar. Formally, each group is such that a word is in the group if and
only if it is similar to at least one other word in the group.

We are given a list strs of strings where every string in strs is an anagram of
every other string in strs. How many groups are there?


Example 1:

Input: strs = ["tars","rats","arts","star"]
Output: 2

Example 2:

Input: strs = ["omv","ovm"]
Output: 1


Constraints:

1 <= strs.length <= 300
1 <= strs[i].length <= 300
strs[i] consists of lowercase letters only.
All words in strs have the same length and are anagrams of each other.

"""

# V0
# IDEA : UNION FIND
#
#   Since all words are anagrams of each other, two words are "similar"
#   exactly when they differ in 0 or 2 positions (1 difference is impossible
#   for anagrams, and >= 3 needs more than one swap).
#
#   Compare every pair, union the similar ones, and the answer is the number
#   of connected components.
#
# time  = O(n^2 * m)   # n = len(strs), m = len(strs[0])
# space = O(n)
class Solution(object):
    def numSimilarGroups(self, strs):
        n = len(strs)
        parent = list(range(n))

        def find(x):
            while parent[x] != x:
                parent[x] = parent[parent[x]]   # path halving
                x = parent[x]
            return x

        def similar(a, b):
            diff = 0
            for x, y in zip(a, b):
                if x != y:
                    diff += 1
                    # early exit: 3+ mismatches can never be one swap
                    if diff > 2:
                        return False
            return True

        groups = n
        for i in range(n):
            for j in range(i + 1, n):
                if similar(strs[i], strs[j]):
                    ri, rj = find(i), find(j)
                    if ri != rj:
                        parent[ri] = rj
                        groups -= 1

        return groups
