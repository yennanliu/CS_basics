# V1 : dev  

# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/79457764
import collections
class Solution(object):
    def numRabbits(self, answers):
        """
        :type answers: List[int]
        :rtype: int
        """
        count = collections.Counter(answers)
        print (count)
        return sum((count[x] + x) / (x + 1) * (x + 1) for x in count)

# V3 
# http://bookshadow.com/weblog/2018/02/16/leetcode-rabbits-in-forest/
#### Greedy Algorithm ###
class Solution(object):
    def numRabbits(self, answers):
        """
        :type answers: List[int]
        :rtype: int
        """
        ans = 0
        cntDict = collections.defaultdict(int)
        for n in answers:
            if cntDict[n + 1]:
                cntDict[n + 1] -= 1
            else:
                ans += n + 1
                cntDict[n + 1] = n
        return ans

# V4
# Time:  O(n)
# Space: O(n)
import collections
class Solution(object):
    def numRabbits(self, answers):
        """
        :type answers: List[int]
        :rtype: int
        """
        count = collections.Counter(answers)
        return sum((((k+1)+v-1)//(k+1))*(k+1) for k, v in count.items())
