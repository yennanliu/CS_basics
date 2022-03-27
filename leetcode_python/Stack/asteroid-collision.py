"""

735. Asteroid Collision
Medium

We are given an array asteroids of integers representing asteroids in a row.

For each asteroid, the absolute value represents its size, and the sign represents its direction (positive meaning right, negative meaning left). Each asteroid moves at the same speed.

Find out the state of the asteroids after all collisions. If two asteroids meet, the smaller one will explode. If both are the same size, both will explode. Two asteroids moving in the same direction will never meet.

 

Example 1:

Input: asteroids = [5,10,-5]
Output: [5,10]
Explanation: The 10 and -5 collide resulting in 10. The 5 and 10 never collide.
Example 2:

Input: asteroids = [8,-8]
Output: []
Explanation: The 8 and -8 collide exploding each other.
Example 3:

Input: asteroids = [10,2,-5]
Output: [10]
Explanation: The 2 and -5 collide resulting in -5. The 10 and -5 collide resulting in 10.
 

Constraints:

2 <= asteroids.length <= 104
-1000 <= asteroids[i] <= 1000
asteroids[i] != 0

"""

# V0
# IDEA : STACK
class Solution(object):
    def asteroidCollision(self, asteroids):
        stack = []
        for item in asteroids:
            while stack and item < 0 and stack[-1] >= 0:
                pre = stack.pop()
                if item == -pre:
                    item = None
                    break
                elif -item < pre:
                    item = pre
            if item != None:
                stack.append(item)
        return stack

# V0
# IDEA : STACK
class Solution(object):
    def asteroidCollision(self, asteroids):
        ans = []
        for new in asteroids:
            while ans and new < 0 < ans[-1]:
                if ans[-1] < -new:
                    ans.pop()
                    continue
                elif ans[-1] == -new:
                    ans.pop()
                break
            else:
                ans.append(new)
        return ans

# V1
# IDEA : STACK
# https://leetcode.com/problems/asteroid-collision/solution/
class Solution(object):
    def asteroidCollision(self, asteroids):
        ans = []
        for new in asteroids:
            while ans and new < 0 < ans[-1]:
                if ans[-1] < -new:
                    ans.pop()
                    continue
                elif ans[-1] == -new:
                    ans.pop()
                break
            else:
                ans.append(new)
        return ans

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/81079015
class Solution(object):
    def asteroidCollision(self, asteroids):
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