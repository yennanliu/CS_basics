# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/81079015
class Solution(object):
    def asteroidCollision(self, asteroids):
        """
        :type asteroids: List[int]
        :rtype: List[int]
        """
        stack = []
        for ast in asteroids:
            while stack and ast < 0 and stack[-1] >= 0:
                pre = stack.pop()
                if ast == -pre:
                    ast = None
                    break
                elif -ast < pre:
                    ast = pre
            if ast != None:
                stack.append(ast)
        return stack

# V2 
# Time:  O(n)
# Space: O(n)
class Solution(object):
    def asteroidCollision(self, asteroids):
        """
        :type asteroids: List[int]
        :rtype: List[int]
        """
        result = []
        for asteroid in asteroids:
            while result and asteroid < 0 < result[-1]:
                if result[-1] < -asteroid:
                    result.pop()
                    continue
                elif result[-1] == -asteroid:
                    result.pop()
                break
            else:
                result.append(asteroid)
        return result