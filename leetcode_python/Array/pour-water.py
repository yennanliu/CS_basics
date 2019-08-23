# V0 

# V1 
# https://blog.csdn.net/u014688145/article/details/78950488
class Solution(object):
    def pourWater(self, a, V, K):
    """
    :type heights: List[int]
    :type V: int
    :type K: int
    :rtype: List[int]
    """
    n = len(a)
    for i in range(V):
        ok = False
        index = -1
        for j in range(K - 1, -1, -1):
            if a[j] > a[j + 1]: break
            if a[j] < a[j + 1]:
                ok = True
                index = j
        if ok:
            a[index] += 1
            continue
        for j in range(K + 1, n):
            if a[j] > a[j - 1]: break
            if a[j] < a[j - 1]:
                ok = True
                index = j
        if ok:
            a[index] += 1
        else:
            a[K] += 1
    return a

# V2 
# Time:  O(v * n)
# Space: O(1)
class Solution(object):
    def pourWater(self, heights, V, K):
        """
        :type heights: List[int]
        :type V: int
        :type K: int
        :rtype: List[int]
        """
        for _ in range(V):
            best = K
            for d in (-1, 1):
                i = K
                while 0 <= i+d < len(heights) and \
                      heights[i+d] <= heights[i]:
                    if heights[i+d] < heights[i]: best = i+d
                    i += d
                if best != K:
                    break
            heights[best] += 1
        return heights