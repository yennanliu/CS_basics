# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/81867361
class Solution:
    def carFleet(self, target, position, speed):
        """
        :type target: int
        :type position: List[int]
        :type speed: List[int]
        :rtype: int
        """
        cars = [(pos, spe) for pos, spe in zip(position, speed)]
        sorted_cars = sorted(cars)
        times = [(target - pos) / spe for pos, spe in sorted_cars]
        stack = []
        for time in times[::-1]:
            if not stack:
                stack.append(time)
            else:
                if time > stack[-1]:
                    stack.append(time)
        return len(stack)


# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/81867361
class Solution:
    def carFleet(self, target, position, speed):
        """
        :type target: int
        :type position: List[int]
        :type speed: List[int]
        :rtype: int
        """
        cars = [(pos, spe) for pos, spe in zip(position, speed)]
        sorted_cars = sorted(cars, reverse=True)
        times = [(target - pos) / spe for pos, spe in sorted_cars]
        stack = []
        for time in times:
            if not stack:
                stack.append(time)
            else:
                if time > stack[-1]:
                    stack.append(time)
        return len(stack)

# V2 
# Time:  O(nlogn)
# Space: O(n)
class Solution(object):
    def carFleet(self, target, position, speed):
        """
        :type target: int
        :type position: List[int]
        :type speed: List[int]
        :rtype: int
        """
        times = [float(target-p)/s for p, s in sorted(zip(position, speed))]
        result, curr = 0, 0
        for t in reversed(times):
            if t > curr:
                result += 1
                curr = t
        return result
