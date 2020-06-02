# V0
# IDEA : STACK
# DEMO 
#     ...: T=[73, 74, 75, 71, 69, 72, 76, 73]
#     ...: s=Solution()
#     ...: r= s.dailyTemperatures(T)
#     ...: print(r)
#     ...: 
# i : 1, stack : [(73, 0)], res : [0, 0, 0, 0, 0, 0, 0, 0]
# i : 2, stack : [(74, 1)], res : [1, 0, 0, 0, 0, 0, 0, 0]
# i : 5, stack : [(75, 2), (71, 3), (69, 4)], res : [1, 1, 0, 0, 0, 0, 0, 0]
# i : 5, stack : [(75, 2), (71, 3)], res : [1, 1, 0, 0, 1, 0, 0, 0]
# i : 6, stack : [(75, 2), (72, 5)], res : [1, 1, 0, 2, 1, 0, 0, 0]
# i : 6, stack : [(75, 2)], res : [1, 1, 0, 2, 1, 1, 0, 0]
# [1, 1, 4, 2, 1, 1, 0, 0]
class Solution(object):
    def dailyTemperatures(self, T):
        N = len(T)
        stack = []
        res = [0] * N
        for i, t in enumerate(T):
            # if stack is not bland and last temp < current tmpe
            # -> pop the stack (get its temp)
            # -> and calculate the difference 
            ### BEWARE "while" op 
            while stack and stack[-1][0] < t:
                oi = stack.pop()[1]
                res[oi] = i - oi
            # no matter any case, we have to insert current temp into stack anyway
            # since the result (next higher temp) is decided by the coming temp, rather than current temp 
            stack.append((t, i))
        return res

# V0'
# IDEA : STACK
class Solution:
    def dailyTemperatures(self, T):
        res = [0 for _ in range(len(T))]
        stack = []
        for i in range(len(T)):
            while len(stack)>0 and T[i]>T[stack[-1]]:
                pop_idx = stack.pop()
                res[pop_idx] = i-pop_idx
            stack.append(i)
        return res

# V0''
# IDEA : BRUTE FORCE : TIME OUT ERROR
class Solution:
    def dailyTemperatures(self,T):
        r=[]
        for i in range(len(T)-1):
            for j in range(i+1, len(T)):
                print (i,j)
                if T[j] > T[i]:
                    r.append(j-i)
                    break
                elif j==len(T)-1:
                    r.append(0)
        r.append(0)    
        return r 

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

### Test case
s=Solution()
assert s.dailyTemperatures([73, 74, 75, 71, 69, 72, 76, 73]) == [1, 1, 4, 2, 1, 1, 0, 0]
assert s.dailyTemperatures([]) == []
assert s.dailyTemperatures([0]) == [0]
assert s.dailyTemperatures([0,0,0,0]) == [0,0,0,0]
assert s.dailyTemperatures([1,1,1,1]) == [0,0,0,0]
assert s.dailyTemperatures([0,0,1,1]) == [2,1,0,0]
assert s.dailyTemperatures([4,23,99,3,55,4,30,100,3,0,4]) == [1, 1, 5, 1, 3, 1, 1, 0, 2, 1, 0]
assert s.dailyTemperatures([-1,-1,-1])==[0,0,0]
assert s.dailyTemperatures([-1,-1,-1,0,0,0])==[3,2,1,0,0,0]

# V1'
# https://leetcode.com/problems/daily-temperatures/solution/
# IDEA : NEXT ARRAY
# Time Complexity: O(NW), where NN is the length of T and WW is the number of allowed values for T[i]. Since W = 71W=71, we can consider this complexity O(N)O(N).
# Space Complexity: O(N + W), the size of the answer and the next array.
class Solution(object):
    def dailyTemperatures(self, T):
        nxt = [float('inf')] * 102
        ans = [0] * len(T)
        for i in range(len(T) - 1, -1, -1):
            #Use 102 so min(nxt[t]) has a default value
            warmer_index = min(nxt[t] for t in range(T[i]+1, 102))
            if warmer_index < float('inf'):
                ans[i] = warmer_index - i
            nxt[T[i]] = i
        return ans

# V1''
# https://leetcode.com/problems/daily-temperatures/solution/
# IDEA : STACK
#Time Complexity: O(N), where NN is the length of T and WW is the number of allowed values for T[i]. Each index gets pushed and popped at most once from the stack.
#Space Complexity: O(W). The size of the stack is bounded as it represents strictly increasing temperatures.
class Solution(object):
    def dailyTemperatures(self, T):
        ans = [0] * len(T)
        stack = [] #indexes from hottest to coldest
        for i in range(len(T) - 1, -1, -1):
            while stack and T[i] >= T[stack[-1]]:
                stack.pop()
            if stack:
                ans[i] = stack[-1] - i
            stack.append(i)
        return ans

# V1'''
# http://bookshadow.com/weblog/2017/12/03/leetcode-daily-temperatures/
import collections
class Solution(object):
    def dailyTemperatures(self, temperatures):
        """
        :type temperatures: List[int]
        :rtype: List[int]
        """
        minIdx = collections.defaultdict(int)
        ans = []
        size = len(temperatures)
        for x in range(size - 1, -1, -1):
            n = temperatures[x]
            minIdx[n] = x
            z = 0
            for y in range(n + 1, 101):
                if minIdx[y] and (minIdx[y] < z or not z):
                    z = minIdx[y]
            ans.append(z - x if z else 0)
        return ans[::-1]

# V1'''''
# https://leetcode-cn.com/problems/daily-temperatures/solution/zhan-by-oliver8641/
class Solution:
    def dailyTemperatures(self, T: List[int]) -> List[int]:
        res = [0 for _ in range(len(T))]
        stack = []
        for i in range(len(T)):
            while len(stack)>0 and T[i]>T[stack[-1]]:
                pop_idx = stack.pop()
                res[pop_idx] = i-pop_idx
            stack.append(i)
        return res

# V1'''''''
# https://leetcode.com/problems/daily-temperatures/discuss/397728/Easy-Python-O(n)-time-O(1)-space-beat-99.9
class Solution:
    def dailyTemperatures(self, T: List[int]) -> List[int]:
        n, right_max = len(T), float('-inf')
        res = [0] * n
        for i in range(n-1, -1, -1):
            t = T[i]
            if right_max <= t:
                right_max = t
            else:
                days = 1
                while T[i+days] <= t:
                    days += res[i+days]
                res[i] = days
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