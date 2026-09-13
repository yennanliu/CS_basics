"""

781. Rabbits in Forest
Medium

There is a forest with an unknown number of rabbits. We asked n rabbits "How many other rabbits have the same color as you?" and collected the answers in an integer array answers where answers[i] is the answer of the i^th rabbit.

Given the array answers, return the minimum number of rabbits that could be in the forest.

Example 1:

Input: answers = [1,1,2]
Output: 5
Explanation:
The two rabbits that answered "1" could both be the same color, say red.
The rabbit that answered "2" can't be red or the answers would be inconsistent.
Say the rabbit that answered "2" was blue.
Then there should be 2 other blue rabbits in the forest that didn't answer into the array.
The smallest possible number of rabbits in the forest is therefore 5: 3 that answered plus 2 that didn't.

Example 2:

Input: answers = [10,10,10]
Output: 11

Constraints:

1 <= answers.length <= 1000
0 <= answers[i] < 1000

"""

# V0

# V1 : dev  

# V2
# https://blog.csdn.net/fuxuemingzhu/article/details/79457764
import collections
# time = O(n)  # n = len(answers)
# space = O(n)
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
# time = O(n)  # n = len(answers)
# space = O(n)
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
# time = O(n)  # n = len(answers)
# space = O(n)
import collections
class Solution(object):
    def numRabbits(self, answers):
        """
        :type answers: List[int]
        :rtype: int
        """
        count = collections.Counter(answers)
        return sum((((k+1)+v-1)//(k+1))*(k+1) for k, v in count.items())
