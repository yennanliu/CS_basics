"""

2782. Number of Unique Categories
Medium

You are given an integer n and an object categoryHandler of class CategoryHandler.

There are n elements, numbered from 0 to n - 1. Each element has a category, and your task is to find the number of unique categories.

The class CategoryHandler contains the following function, which may help you:

boolean haveSameCategory(integer a, integer b): Returns true if a and b are in the same category and false otherwise. Also, if either a or b is not a valid number (i.e. it's greater than or equal to n or less than 0), it returns false.

Return the number of unique categories.


Example 1:

Input: n = 6, categoryHandler = [1,1,2,2,3,3]
Output: 3
Explanation: There are 6 elements in this example. The first two elements belong to category 1, the second two belong to category 2, and the last two elements belong to category 3. So there are 3 unique categories.

Example 2:

Input: n = 5, categoryHandler = [1,2,3,4,5]
Output: 5
Explanation: There are 5 elements in this example. Each element belongs to a unique category. So there are 5 unique categories.

Example 3:

Input: n = 3, categoryHandler = [1,1,1]
Output: 1
Explanation: There are 3 elements in this example. All of them belong to one category. So there is only 1 unique category.


Constraints:

1 <= n <= 100

"""

# V0
# IDEA : REPRESENTATIVE SCAN (INTERACTIVE)
#
#   "same category" is an equivalence relation, so the elements are already
#   partitioned into groups; we only need to count the groups.
#
#   NOTE : we do NOT need a full union-find here. Keep one representative per
#          category discovered so far; for a new element i, compare it against
#          the representatives only. If it matches one, it joins that group;
#          if it matches none, it opens a brand new category.
#
#   NOTE : this is correct precisely because of transitivity - matching any
#          member of a group is the same as matching its representative.
#
#   n <= 100, so the O(n^2) worst case (all categories distinct) is fine.
#
#   Definition for a category handler (provided by LeetCode):
#     class CategoryHandler(object):
#         def haveSameCategory(self, a, b):  # -> bool
#             ...
#
# time = O(n^2) haveSameCategory calls, space = O(n)
class Solution(object):
    def numberOfCategories(self, n, categoryHandler):
        reps = []
        for i in range(n):
            found = False
            for r in reps:
                if categoryHandler.haveSameCategory(r, i):
                    found = True
                    break
            if not found:
                reps.append(i)
        return len(reps)
