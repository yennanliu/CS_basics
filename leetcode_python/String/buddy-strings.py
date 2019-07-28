# V0 

# V1
# https://blog.csdn.net/XX_123_1_RJ/article/details/80984277
class Solution:
    def buddyStrings(self, A, B):
        if len(A) != len(B): return False  # if len(A) != len(B)
        if A == B:  # if len(A) != len(B), to fit the requirement,  A must have repeat elements 
            seen = set()
            for a in A:
                if a in seen: return True
                seen.add(a)
            return False
        else:
            pairs = []
            for a, b in zip(A, B):  # if A != B
                if a != b:
                    pairs.append([a, b])
                if len(pairs) > 2: return False
            return len(pairs) == 2 and pairs[0] == pairs[1][::-1]

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/80916203
class Solution:
    def buddyStrings(self, A, B):
        """
        :type A: str
        :type B: str
        :rtype: bool
        """
        if len(A) != len(B):
            return False
        diff = 0
        idxs = []
        for i, a in enumerate(A):
            if B[i] != a:
                diff += 1
                idxs.append(i)
        counter = dict()
        if diff == 0:
            for a in A:
                if a in counter and counter[a]:
                    return True
                else:
                    counter[a] = True
        if diff != 2:
            return False
        return A[idxs[0]] == B[idxs[1]] and A[idxs[1]] == B[idxs[0]]

# V2 
# Time:  O(n)
# Space: O(1)
import itertools
class Solution(object):
    def buddyStrings(self, A, B):
        """
        :type A: str
        :type B: str
        :rtype: bool
        """
        if len(A) != len(B):
            return False
        diff = []
        for a, b in itertools.izip(A, B):
            if a != b:
                diff.append((a, b))
                if len(diff) > 2:
                    return False
        return (not diff and len(set(A)) < len(A)) or \
               (len(diff) == 2 and diff[0] == diff[1][::-1])