# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82806311
# IDEA : TWO POINTERS 
class Solution(object):
    def numRescueBoats(self, people, limit):
        """
        :type people: List[int]
        :type limit: int
        :rtype: int
        """
        people.sort()
        res = 0
        hi, lo = len(people) - 1, 0
        while hi >= lo:
            if people[hi] + people[lo] <= limit:
                lo += 1
            hi -= 1
            res += 1
        return res
        
# V2 
# Time:  O(nlogn)
# Space: O(n)
class Solution(object):
    def numRescueBoats(self, people, limit):
        """
        :type people: List[int]
        :type limit: int
        :rtype: int
        """
        people.sort()
        result = 0
        left, right = 0, len(people)-1
        while left <= right:
            result += 1
            if people[left] + people[right] <= limit:
                left += 1
            right -= 1
        return result
