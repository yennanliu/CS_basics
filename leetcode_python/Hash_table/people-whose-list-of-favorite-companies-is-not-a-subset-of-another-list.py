"""

1452. People Whose List of Favorite Companies Is Not a Subset of Another List
Medium

Given the array favoriteCompanies where favoriteCompanies[i] is the list of
favorites companies for the ith person (indexed from 0).

Return the indices of people whose list of favorite companies is not a subset of
any other list of favorites companies. You must return the indices in increasing
order.


Example 1:

Input: favoriteCompanies = [["leetcode","google","facebook"],["google","microsoft"],["google","facebook"],["google"],["amazon"]]
Output: [0,1,4]
Explanation:
Person with index=2 has favoriteCompanies[2]=["google","facebook"] which is a
subset of favoriteCompanies[0]=["leetcode","google","facebook"] corresponding to
the person with index 0.
Person with index=3 has favoriteCompanies[3]=["google"] which is a subset of
favoriteCompanies[0]=["leetcode","google","facebook"] and
favoriteCompanies[1]=["google","microsoft"].
Other lists of favorite companies are not a subset of another list, therefore,
the answer is [0,1,4].

Example 2:

Input: favoriteCompanies = [["leetcode","google","facebook"],["leetcode","amazon"],["facebook","google"]]
Output: [0,1]
Explanation: In this case favoriteCompanies[2]=["facebook","google"] is a subset
of favoriteCompanies[0]=["leetcode","google","facebook"], therefore, the answer
is [0,1].

Example 3:

Input: favoriteCompanies = [["leetcode"],["google"],["facebook"],["amazon"]]
Output: [0,1,2,3]


Constraints:

1 <= favoriteCompanies.length <= 100
1 <= favoriteCompanies[i].length <= 500
1 <= favoriteCompanies[i][j].length <= 20
All strings in favoriteCompanies[i] are distinct.
All lists of favorite companies are distinct, that is, If we sort alphabetically
each list then favoriteCompanies[i] != favoriteCompanies[j].
All strings consist of lowercase English letters only.

"""

# V0
# IDEA: HASH TABLE (intern company name -> int id) + pairwise set subset check
#
#  - turn every person's list into a set of small ints
#    (comparing ints is much cheaper than comparing 20-char strings)
#  - person i survives iff no OTHER person j has nums[i] <= nums[j]
#
# time = O(n^2 * m)
# space = O(n * m)
class Solution(object):
    def peopleIndexes(self, favoriteCompanies):
        n = len(favoriteCompanies)

        # NOTE !!! intern each company name into an int id
        ids = {}
        nums = []
        for companies in favoriteCompanies:
            cur = set()
            for c in companies:
                if c not in ids:
                    ids[c] = len(ids)
                cur.add(ids[c])
            nums.append(cur)

        res = []
        for i in range(n):
            is_subset = False
            for j in range(n):
                # NOTE !!! i != j, and `nums[i] <= nums[j]` means "i is subset of j"
                if i != j and nums[i] <= nums[j]:
                    is_subset = True
                    break
            if not is_subset:
                res.append(i)

        return res
