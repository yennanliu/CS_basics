"""

2306. Naming a Company
Hard

You are given an array of strings ideas that represents a list of names to be used in the process of naming a company. The process of naming a company is as follows:

Choose 2 distinct names from ideas, call them ideaA and ideaB.
Swap the first letters of ideaA and ideaB with each other.
If both of the new names are not found in the original ideas, then the name ideaA ideaB (the concatenation of ideaA and ideaB, separated by a space) is a valid company name.
Otherwise, it is not a valid name.

Return the number of distinct valid names for the company.


Example 1:

Input: ideas = ["coffee","donuts","time","toffee"]
Output: 6
Explanation: The following selections are valid:
- ("coffee", "donuts"): The company name created is "doffee conuts".
- ("donuts", "coffee"): The company name created is "conuts doffee".
- ("donuts", "time"): The company name created is "tonuts dime".
- ("donuts", "toffee"): The company name created is "tonuts doffee".
- ("time", "donuts"): The company name created is "dime tonuts".
- ("toffee", "donuts"): The company name created is "doffee tonuts".
Therefore, there are a total of 6 distinct company names.

The following are some examples of invalid selections:
- ("coffee", "time"): The name "toffee" formed after swapping already exists in the original array.
- ("time", "toffee"): Both names are still the same after swapping and exist in the original array.
- ("coffee", "toffee"): Both names formed after swapping already exist in the original array.

Example 2:

Input: ideas = ["lack","back"]
Output: 0
Explanation: There are no valid selections. Therefore, 0 is returned.


Constraints:

2 <= ideas.length <= 5 * 10^4
1 <= ideas[i].length <= 10
ideas[i] consists of lowercase English letters.
All the strings in ideas are unique.

"""

# V0
# IDEA : GROUP BY FIRST LETTER + COUNT SUFFIX OVERLAP (26 x 26 buckets)
#
#   bucket every idea by its first letter, storing only the suffix.
#   for a pair of letters (a, b), take an idea from group a and one from
#   group b. after swapping the first letters both new words must be absent
#   from ideas, i.e.
#     - the suffix taken from group a must NOT also appear in group b, and
#     - the suffix taken from group b must NOT also appear in group a.
#   so if c = |suffixes(a) & suffixes(b)| is the shared count, the usable
#   suffixes are (len(a) - c) and (len(b) - c), and each such unordered
#   pair yields 2 ordered company names.
#
#   NOTE : the shared suffixes are exactly the ones that break BOTH sides,
#          so they are simply excluded from both groups.
#
# time = O(26 * L + 26^2 * L) with L = total length, space = O(L)
class Solution(object):
    def distinctNames(self, ideas):
        groups = [set() for _ in range(26)]
        for w in ideas:
            groups[ord(w[0]) - 97].add(w[1:])

        res = 0
        for a in range(26):
            for b in range(a + 1, 26):
                shared = len(groups[a] & groups[b])
                res += 2 * (len(groups[a]) - shared) * (len(groups[b]) - shared)
        return res
