# V0 

# V1 
# https://blog.csdn.net/zjucor/article/details/84927488
class Solution(object):
    def minDeletionSize(self, A):
        """
        :type A: List[str]
        :rtype: int
        """
        res=0
        remains = ['']*len(A)
        for i in range(len(A[0])):
            remains_tmp = [t+A[idx][i] for idx,t in enumerate(remains)]
            if ''.join(remains_tmp)==''.join(sorted(remains_tmp)):
                remains = remains_tmp
            else:
                res+=1
        return res
        
# V2 
# Time:  O(n * l)
# Space: O(n)
class Solution(object):
    def minDeletionSize(self, A):
        """
        :type A: List[str]
        :rtype: int
        """
        result = 0
        unsorted = set(range(len(A)-1))
        for j in range(len(A[0])):
            if any(A[i][j] > A[i+1][j] for i in unsorted):
                result += 1
            else:
                unsorted -= set(i for i in unsorted if A[i][j] < A[i+1][j])
        return result

# Time:  O(n * m)
# Space: O(n)
class Solution2(object):
    def minDeletionSize(self, A):
        """
        :type A: List[str]
        :rtype: int
        """
        result = 0
        is_sorted = [False]*(len(A)-1)
        for j in range(len(A[0])):
            tmp = is_sorted[:]
            for i in range(len(A)-1):
                if A[i][j] > A[i+1][j] and tmp[i] == False:
                    result += 1
                    break
                if A[i][j] < A[i+1][j]:
                    tmp[i] = True
            else:
                is_sorted = tmp
        return result