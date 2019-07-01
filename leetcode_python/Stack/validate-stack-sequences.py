# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/84495797
class Solution(object):
    def validateStackSequences(self, pushed, popped):
        """
        :type pushed: List[int]
        :type popped: List[int]
        :rtype: bool
        """
        stack = []
        N = len(pushed)
        pi = 0
        for i in range(N):
            if stack and popped[i] == stack[-1]:
                stack.pop()
            else:
                while pi < N and pushed[pi] != popped[i]:
                    stack.append(pushed[pi])
                    pi += 1
                pi += 1
        return not stack

# V2 
# Time:  O(n)
# Space: O(n)
class Solution(object):
    def validateStackSequences(self, pushed, popped):
        """
        :type pushed: List[int]
        :type popped: List[int]
        :rtype: bool
        """
        i = 0
        s = []
        for v in pushed:
            s.append(v)
            while s and i < len(popped) and s[-1] == popped[i]:
                s.pop()
                i += 1
        return i == len(popped)