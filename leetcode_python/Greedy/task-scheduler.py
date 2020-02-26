# V0 
class Solution(object):
    def leastInterval(self, tasks, n):
        """
        :type tasks: List[str]
        :type n: int
        :rtype: int
        """
        count = collections.Counter(tasks)
        most = list(count.values())[0]
        num_most = len([i for i, v in count.items() if v == most])
        time = (most - 1) * (n + 1) + num_most
        return max(time, len(tasks))

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/81947087
# NOTE : However, there is a non-negative cooling interval n that means between two same tasks, there must be at least n intervals that CPU are doing different tasks or just be idle.
# IDEA : MISSION_TIME = ( # of most mission -1 ) * (n+1) + (# of how many missions with same mission count)
#        -> ANS = max(MISSION_TIME , tasks) (since every mission need to be run at least once)
class Solution(object):
    def leastInterval(self, tasks, n):
        """
        :type tasks: List[str]
        :type n: int
        :rtype: int
        """
        count = collections.Counter(tasks)
        most = count.most_common()[0][1]
        num_most = len([i for i, v in count.items() if v == most])
        time = (most - 1) * (n + 1) + num_most
        return max(time, len(tasks)) # be aware of it 

# V1'
# https://www.jiuzhang.com/solution/task-scheduler/#tag-highlight-lang-python
class Solution:
    """
    @param tasks: the given char array representing tasks CPU need to do
    @param n: the non-negative cooling interval
    @return: the least number of intervals the CPU will take to finish all the given tasks
    """
    def leastInterval(self, tasks, n):
        # write your code here
        d = collections.Counter(tasks)
        counts = list(d.values())
        longest = max(counts)
        ans = (longest - 1) * (n + 1) + counts.count(longest)
        return max(len(tasks), ans)

# V2 
# Time:  O(n)
# Space: O(26) = O(1)
from collections import Counter
class Solution(object):
    def leastInterval(self, tasks, n):
        """
        :type tasks: List[str]
        :type n: int
        :rtype: int
        """
        counter = Counter(tasks)
        _, max_count = counter.most_common(1)[0]

        result = (max_count-1) * (n+1)
        for count in counter.values():
            if count == max_count:
                result += 1
        return max(result, len(tasks))