"""

1966. Binary Searchable Numbers in an Unsorted Array
Medium

Consider a function that implements an algorithm similar to Binary Search. The function has two input parameters: sequence is a sequence of integers, and target is an integer value. The purpose of the function is to find if the target exists in the sequence.

The pseudocode of the function is as follows:

func(sequence, target)
  while sequence is not empty
    randomly choose an element from sequence as the pivot
    if pivot = target, return true
    else if pivot < target, remove pivot and all elements to its left from the sequence
    else, remove pivot and all elements to its right from the sequence
  end while
  return false

When the sequence is sorted, the function works correctly for all values. When the sequence is not sorted, the function does not work for all values, but may still work for some values.

Given an integer array nums, representing the sequence, that contains unique numbers and may or may not be sorted, return the number of values that are guaranteed to be found using the function, for every possible pivot selection.


Example 1:

Input: nums = [7]
Output: 1
Explanation:
Searching for value 7 is guaranteed to be found.
Since the sequence has only one element, 7 will be chosen as the pivot. Because the pivot equals the target, the function will return true.

Example 2:

Input: nums = [-1,5,2]
Output: 1
Explanation:
Searching for value -1 is guaranteed to be found.
If -1 was chosen as the pivot, the function would return true.
If 5 was chosen as the pivot, 5 and 2 would be removed. In the next loop, the sequence would have only -1 and the function would return true.
If 2 was chosen as the pivot, 2 would be removed. In the next loop, the sequence would have -1 and 5. No matter which number was chosen as the next pivot, the function would find -1 and return true.

Searching for value 5 is NOT guaranteed to be found.
If 2 was chosen as the pivot, -1, 5 and 2 would be removed. The sequence would be empty and the function would return false.

Searching for value 2 is NOT guaranteed to be found.
If 5 was chosen as the pivot, 5 and 2 would be removed. In the next loop, the sequence would have only -1 and the function would return false.

Because only -1 is guaranteed to be found, you should return 1.


Constraints:

1 <= nums.length <= 10^5
-10^5 <= nums[i] <= 10^5
All the values of nums are unique.

Follow-up: If nums has duplicates, would you modify your algorithm? If so, how?

"""

# V0
# IDEA : PREFIX MAX + SUFFIX MIN
#
#   nums[i] survives every pivot choice iff it is "in its sorted place":
#     - everything to its LEFT is smaller  -> nums[i] > max(nums[0..i-1])
#     - everything to its RIGHT is larger  -> nums[i] < min(nums[i+1..n-1])
#
#   why : if some left neighbour is bigger, picking it as pivot when searching
#   nums[i] gives pivot > target, so the whole right part (containing nums[i])
#   is dropped. symmetric for a smaller right neighbour.
#
#   so : one forward pass for the running max, one backward pass for the
#   running min, count the indices that pass both tests.
#
# time = O(n), space = O(n)
class Solution(object):
    def binarySearchableNumbers(self, nums):
        n = len(nums)
        ok = [True] * n

        mx = float("-inf")
        for i in range(n):
            if nums[i] < mx:
                ok[i] = False
            else:
                mx = nums[i]

        mi = float("inf")
        for i in range(n - 1, -1, -1):
            if nums[i] > mi:
                ok[i] = False
            else:
                mi = nums[i]

        return sum(1 for f in ok if f)


# V0-1
# IDEA : BRUTE FORCE (test the definition directly for every element)
#
#   nums[i] is guaranteed to be found iff nothing bigger sits to its left and
#   nothing smaller sits to its right -- so just look at every other element.
#   no auxiliary arrays, but quadratic work.
#
# time  = O(n^2)
# space = O(1)
class Solution(object):
    def binarySearchableNumbers(self, nums):
        n = len(nums)
        res = 0
        for i in range(n):
            left_ok = all(nums[j] < nums[i] for j in range(i))
            right_ok = all(nums[j] > nums[i] for j in range(i + 1, n))
            if left_ok and right_ok:
                res += 1
        return res


# V0-2
# IDEA : MONOTONIC (INCREASING) STACK, ONE PASS
#
#   keep a stack holding the candidates that are still alive.  a value can only
#   be a candidate if it beats every earlier value, so the stack is increasing
#   from bottom to top.  for each new value v :
#
#     - v > running max -> v is a new candidate, push it
#     - otherwise       -> v kills every stacked candidate bigger than v (a
#                          smaller value now sits to their right), and v itself
#                          is not a candidate either
#
#   the running max must stay the max of ALL values seen (including popped
#   ones), since candidacy means beating every earlier element.  what is left
#   on the stack at the end IS the answer set, so one pass replaces V0's two
#   prefix/suffix sweeps and its boolean array.
#
# time  = O(n) amortised (every value is pushed and popped at most once)
# space = O(n) for the stack
class Solution(object):
    def binarySearchableNumbers(self, nums):
        stack = []
        mx = float("-inf")
        for v in nums:
            if v > mx:
                mx = v
                stack.append(v)
            else:
                while stack and stack[-1] > v:
                    stack.pop()
        return len(stack)
