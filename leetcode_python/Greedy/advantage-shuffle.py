"""

870. Advantage Shuffle
Medium

You are given two integer arrays nums1 and nums2 both of the same length. The advantage of nums1 with respect to nums2 is the number of indices i for which nums1[i] > nums2[i].

Return any permutation of nums1 that maximizes its advantage with respect to nums2.

Example 1:

Input: nums1 = [2,7,11,15], nums2 = [1,10,4,11]
Output: [2,11,7,15]

Example 2:

Input: nums1 = [12,24,8,32], nums2 = [13,25,32,11]
Output: [24,32,8,12]

Constraints:

1 <= nums1.length <= 10^5
nums2.length == nums1.length
0 <= nums1[i], nums2[i] <= 10^9

"""

# V0 

# V1 
# http://bookshadow.com/weblog/2018/07/15/leetcode-advantage-shuffle/
# https://blog.csdn.net/fuxuemingzhu/article/details/82796298
# time = O(n log n)
# space = O(n)
class Solution(object):
    def advantageCount(self, A, B):
        """
        :type A: List[int]
        :type B: List[int]
        :rtype: List[int]
        """
        res = [-1] * len(A)
        A = collections.deque(sorted(A)) # sort A list 
        B = collections.deque(sorted((b, i) for i, b in enumerate(B))) # sort B list with its value and index 
        for i in range(len(A)):
            a = A.popleft()
            b = B[0]
            if a > b[0]:
                B.popleft()  # if A element >  B element, then collect this A element to res list at B element's index (requirement satisfied)
            else:
                b = B.pop()  # if A element <  B element, then collect this A element to res list; but at current B BIGGEST element's index (requirement not satisfied, so use B BIGGEST element as a "waste") 
            res[b[1]] = a
        return res

# V2 
# time = O(nlogn)
# space = O(n)
class Solution(object):
    def advantageCount(self, A, B):
        """
        :type A: List[int]
        :type B: List[int]
        :rtype: List[int]
        """
        sortedA = sorted(A)
        sortedB = sorted(B)

        candidates = {b: [] for b in B}
        others = []
        j = 0
        for a in sortedA:
            if a > sortedB[j]:
                candidates[sortedB[j]].append(a)
                j += 1
            else:
                others.append(a)
        return [candidates[b].pop() if candidates[b] else others.pop()
                for b in B]
