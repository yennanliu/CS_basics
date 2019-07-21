# V0 

# V1 
# http://bookshadow.com/weblog/2018/07/15/leetcode-advantage-shuffle/
# https://blog.csdn.net/fuxuemingzhu/article/details/82796298
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
# Time:  O(nlogn)
# Space: O(n)
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
