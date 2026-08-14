"""

1538. Guess the Majority in a Hidden Array
Medium

We have an integer array nums, where all the integers in nums are 0 or 1. You will not be given direct access to the array, instead, you will have an API ArrayReader which have the following functions:

int query(int a, int b, int c, int d): where 0 <= a < b < c < d < ArrayReader.length(). The function returns the distribution of the value of the 4 elements and returns:
    4 : if the values of the 4 elements are the same (0 or 1).
    2 : if three elements have a value equal to 0 and one element has value equal to 1 or vice versa.
    0 : if two element have a value equal to 0 and two elements have a value equal to 1.
int length(): Returns the size of the array.

You are allowed to call query() 2 * n times at most where n is equal to ArrayReader.length().

Return any index of the most frequent value in nums, in case of tie, return -1.


Example 1:

Input: nums = [0,0,1,0,1,1,1,1]
Output: 5
Explanation: The following calls to the API
reader.length() // returns 8 because there are 8 elements in the hidden array.
reader.query(0,1,2,3) // returns 2 this is a query that compares the elements nums[0], nums[1], nums[2], nums[3]
// Three elements have a value equal to 0 and one element has value equal to 1 or viceversa.
reader.query(4,5,6,7) // returns 4 because nums[4], nums[5], nums[6], nums[7] have the same value.
we can infer that the most frequent value is found in the last 4 elements.
Index 2, 4, 6, 7 is also a correct answer.

Example 2:

Input: nums = [0,0,1,1,0]
Output: 0

Example 3:

Input: nums = [1,0,1,0,1,0,1,0]
Output: -1


Constraints:

5 <= nums.length <= 10^5
0 <= nums[i] <= 1


Follow up: What is the minimum number of calls needed to find the majority element?

"""

# V0
# IDEA : COMPARE EVERY INDEX AGAINST A FIXED REFERENCE INDEX
#
#   key fact : if three of the four query slots are HELD FIXED, the return
#   value is a strictly different number for a 0 vs a 1 in the free slot.
#   so query(a, b, c, i) == query(a, b, c, j)  <=>  nums[i] == nums[j].
#
#   pick index 3 as the reference :
#     - x = query(0,1,2,3); for every i >= 4, query(0,1,2,i) == x tells us
#       whether nums[i] == nums[3].                        [n - 4 calls]
#     - indices 0,1,2 cannot use that trio, so anchor on {..,4} instead :
#       y = query(0,1,2,4), then
#         query(1,2,3,4) == y  <=>  nums[3] == nums[0]   (fixed 1,2,4)
#         query(0,2,3,4) == y  <=>  nums[3] == nums[1]   (fixed 0,2,4)
#         query(0,1,3,4) == y  <=>  nums[3] == nums[2]   (fixed 0,1,4)
#
#   a = how many indices match nums[3] (index 3 itself counts), b = the rest,
#   and k remembers one index that differs.
#   NOTE : total calls = (n - 4) + 5 = n + 1, well inside the 2n budget.
#
# time = O(n) calls, space = O(1)
# """
# This is the ArrayReader's API interface.
# You should not implement it, or speculate about its implementation
# """
# class ArrayReader(object):
#    # Compares 4 different elements in the array
#    # return 4 if the values of the 4 elements are the same (0 or 1).
#    # return 2 if three elements have a value equal to 0 and one element has value equal to 1 or vice versa.
#    # return 0 if two element have a value equal to 0 and two elements have a value equal to 1.
#    def query(self, a, b, c, d):
#
#    # Returns the length of the array
#    def length(self):
class Solution(object):
    def guessMajority(self, reader):
        n = reader.length()

        x = reader.query(0, 1, 2, 3)
        a, b = 1, 0          # index 3 itself matches nums[3]
        k = -1               # some index whose value differs from nums[3]

        for i in range(4, n):
            if reader.query(0, 1, 2, i) == x:
                a += 1
            else:
                b += 1
                k = i

        y = reader.query(0, 1, 2, 4)
        for idx, trio in ((0, (1, 2, 3, 4)), (1, (0, 2, 3, 4)), (2, (0, 1, 3, 4))):
            if reader.query(trio[0], trio[1], trio[2], trio[3]) == y:
                a += 1
            else:
                b += 1
                k = idx

        if a == b:
            return -1
        return 3 if a > b else k
