"""

969. Pancake Sorting
Medium

Given an array of integers arr, sort the array by performing a series of pancake flips.

In one pancake flip we do the following steps:

Choose an integer k where 1 <= k <= arr.length.
Reverse the sub-array arr[0...k-1] (0-indexed).
For example, if arr = [3,2,1,4] and we performed a pancake flip choosing k = 3, we reverse the sub-array [3,2,1], so arr = [1,2,3,4] after the pancake flip at k = 3.

Return an array of the k-values corresponding to a sequence of pancake flips that sort arr. Any valid answer that sorts the array within 10 * arr.length flips will be judged as correct.

 

Example 1:

Input: arr = [3,2,4,1]
Output: [4,2,4,3]
Explanation: 
We perform 4 pancake flips, with k values 4, 2, 4, and 3.
Starting state: arr = [3, 2, 4, 1]
After 1st flip (k = 4): arr = [1, 4, 2, 3]
After 2nd flip (k = 2): arr = [4, 1, 2, 3]
After 3rd flip (k = 4): arr = [3, 2, 1, 4]
After 4th flip (k = 3): arr = [1, 2, 3, 4], which is sorted.
Example 2:

Input: arr = [1,2,3]
Output: []
Explanation: The input is already sorted, so there is no need to flip anything.
Note that other answers, such as [3, 3], would also be accepted.
 

Constraints:

1 <= arr.length <= 100
1 <= arr[i] <= arr.length
All integers in arr are unique (i.e. arr is a permutation of the integers from 1 to arr.length).

"""


# V0

# V1
# https://leetcode.com/problems/pancake-sorting/discuss/817978/Python-O(n2)-by-simulation-w-Comment
# https://leetcode.com/problems/pancake-sorting/discuss/330990/Python
class Solution:
    def pancakeSort(self, A):

        res = []

        for x in range(len(A), 1, -1):
            # Carry out pancake-sort from largest number n to smallest number 1

            # find the index of x
            i = A.index(x)

            # flip first i+1 elements to put x on A[0]
            # flip first x elements to put x on A[x-1]
            # now, x is on its corresponding position A[x-1] on ascending order
            # 
            """
            # array extend
            In [10]: x = [1,2,3]

            In [11]: x.extend([4])

            In [12]: x
            Out[12]: [1, 2, 3, 4]

            In [13]: x = [1,2,3]

            In [14]: x = x + [4]

            In [15]: x
            Out[15]: [1, 2, 3, 4]

            """
            #res.extend([i + 1, x])
            res = res + [i + 1, x]

            # update A
            """
            https://stackoverflow.com/questions/509211/understanding-slice-notation

            a[::-1]    # all items in the array, reversed
            a[1::-1]   # the first two items, reversed
            a[:-3:-1]  # the last two items, reversed
            a[-3::-1]  # everything except the last two items, reversed

            -> A[:i:-1] : last i items, reversed

            """
            A = A[:i:-1] + A[:i]
        #print ("res = " + str(res))
        return res

# V1
# IDEA : RECURSIVE
# https://leetcode.com/problems/pancake-sorting/discuss/553116/My-python-solution
# https://leetcode.com/problems/pancake-sorting/discuss/274921/PythonDetailed-Explanation-for-This-Problem
class Solution:
    def pancakeSort(self, A):
        pointer = len(A)
        result = []

        while pointer > 1:
            idx = A.index(pointer)
            result.append(idx + 1)
            A = A[idx::-1] + A[idx + 1:]
            result.append(pointer)
            A = A[pointer - 1::-1] + A[pointer:]
            pointer -= 1
            
        return result

# V1'
# https://leetcode.com/problems/pancake-sorting/discuss/274921/PythonDetailed-Explanation-for-This-Problem
class Solution:
    def pancakeSort(self, A):
        """
        find biggest number, set index idx as
        reverse numbers in 0 - 1, and make A[i] as 1st nunber,
        reverse whole array, and put max number to the last index
        """
        n = len(A)
        res = []
        for i in range(n):
            cur_max = max(A[0:n-i])
            j = 0
            while A[j] != cur_max:
                j += 1
            # should reverse j+1 elements
            A[:j+1] = reversed(A[:j+1])
            res.append(j+1)
            # reverse all
            A[:n-i] = reversed(A[:n-i])
            res.append(n-i)
        return res

# V1''
# https://leetcode.com/problems/pancake-sorting/discuss/767576/python-solution
class Solution(object):
    def pancakeSort(self, A):
        ans = []
        end = len(A)
        while end > 0:
            maxi = max(A[:end])
            max_index = A[:end].index(maxi)
            if max_index+1 == end:
                end -= 1
            else:
                ans.append(max_index+1)
                A[:max_index+1] = A[:max_index+1][::-1]
                ans.append(end)
                A[:end] = A[:end][::-1]
                end -= 1
        return ans

# V1''''
# https://leetcode.com/problems/pancake-sorting/discuss/214213/JavaC%2B%2BPython-Straight-Forward
# IDEA : 
# Find the index i of the next maximum number x.
# Reverse i + 1 numbers, so that the x will be at A[0]
# Reverse x numbers, so that x will be at A[x - 1].
# Repeat this process N times.
class Solution:
     def pancakeSort(self, A):
            res = []
            for x in range(len(A), 1, -1):
                i = A.index(x)
                res.extend([i + 1, x])
                A = A[:i:-1] + A[:i]
            return res

# V1'''''
# IDEA : similar to BUBBLE SORT
# https://leetcode.com/problems/pancake-sorting/solution/
class Solution:
    def pancakeSort(self, A):
        """ sort like bubble-sort
            sink the largest number to the bottom at each round
        """
        def flip(sublist, k):
            i = 0
            while i < k / 2:
                sublist[i], sublist[k-i-1] = sublist[k-i-1], sublist[i]
                i += 1

        ans = []
        value_to_sort = len(A)
        while value_to_sort > 0:
            # locate the position for the value to sort in this round
            index = A.index(value_to_sort)

            # sink the value_to_sort to the bottom,
            #   with at most two steps of pancake flipping.
            if index != value_to_sort - 1:
                # flip the value to the head if necessary
                if index != 0:
                    ans.append(index+1)
                    flip(A, index+1)
                # now that the value is at the head, flip it to the bottom
                ans.append(value_to_sort)
                flip(A, value_to_sort)

            # move on to the next round
            value_to_sort -= 1

        return ans

# V2 
# http://blog.jobbole.com/74263/
# Sorts Pancakes                                                                                    
def sortPancakes(stack):
 
    sorted_index = 10
    for i in reversed(list(range(len(stack)))):
        stack = flip(stack, findLargestPancake(stack, i))
        print(("Flip Up", stack))
        stack = flip(stack, i)
        print(("Flip Down", stack))
    return stack
 
# All of the pancakes are sorted after index                                                        
# Returns the index of largest unsorted pancake                                                     
def findLargestPancake(stack, index):
 
    largest_pancake = stack[index]
    largest_index = index;
 
    for i in range(index):
        if stack[i] > largest_pancake:
            largest_pancake = stack[i]
            largest_index = i
 
    print("")
    print(("Insert Spatula in index", largest_index, "Size", largest_pancake))
    return largest_index
 
# Slide spatula under pancake at index and flip to top                                              
def flip(stack, index):
    newStack = stack[:(index + 1)]
    newStack.reverse()
    newStack += stack[(index + 1):]
    return newStack

# V3 
# https://blog.csdn.net/fuxuemingzhu/article/details/85937314
class Solution(object):
    def pancakeSort(self, A):
        N = len(A)
        res = []
        for x in range(N, 0, -1):
            i = A.index(x)
            res.extend([i + 1, x])
            A = A[:i:-1] + A[:i]
        return res

# V4
# Time:  O(n^2)
# Space: O(1)
class Solution(object):
    def pancakeSort(self, A):
        """
        :type A: List[int]
        :rtype: List[int]
        """
        def reverse(l, begin, end):
            for i in range((end-begin) // 2):
                l[begin+i], l[end-1-i] = l[end-1-i], l[begin+i]
        result = []
        for n in reversed(list(range(1, len(A)+1))):
            i = A.index(n)
            reverse(A, 0, i+1)
            result.append(i+1)
            reverse(A, 0, n)
            result.append(n)
        return result