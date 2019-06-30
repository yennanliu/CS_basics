# V0

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79285081
class Solution(object):
    def dailyTemperatures(self, T):
        """
        :type T: List[int]
        :rtype: List[int]
        """
        N = len(T)
        stack = []
        res = [0] * N
        for i, t in enumerate(T):
            while stack and stack[-1][0] < t:
                oi = stack.pop()[1]
                res[oi] = i - oi
            stack.append((t, i))
        return res
        
# V2 
# Time:  O(n)
# Space: O(n)
class Solution(object):
    def dailyTemperatures(self, temperatures):
        """
        :type temperatures: List[int]
        :rtype: List[int]
        """
        result = [0] * len(temperatures)
        stk = []
        for i in range(len(temperatures)):
            while stk and \
                  temperatures[stk[-1]] < temperatures[i]:
                idx = stk.pop()
                result[idx] = i-idx
            stk.append(i)
        return result