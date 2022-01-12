"""

Given a char array representing tasks CPU need to do. 

It contains capital letters A to Z where different letters represent different tasks.

Tasks could be done without original order. Each task could be done in one interval.

For each interval, CPU could finish one task or just be idle.

However, there is a non-negative cooling interval n that means between two same tasks, 

there must be at least n intervals that CPU are doing different tasks or just be idle.

You need to return the least number of intervals the CPU will take to finish all the given tasks.

Example 1:

Input: tasks = ["A","A","A","B","B","B"], n = 2
Output: 8
Explanation: A -> B -> idle -> A -> B -> idle -> A -> B.

1
2
3
Note:

The number of tasks is in the range [1, 10000].
The integer n is in the range [0, 100].

"""

# V0
# pattern :
#    =============================================================================
#    -> task_time = (max_mission_count - 1) * (n + 1) + (number_of_max_mission)
#    =============================================================================
#   
#    -> Example 1) :
#    ->  AAAABBBBCCD, n=3
#    => THE EXPECTED tuned missions is like : ABXXABXXABXXAB
#    -> (4 - 1) * (3 + 1) + 2 = 14
#    -> 4 is the "how many missions the max mission has" (AAAA or BBBB)
#    -> 3 is n
#    -> 2 is "how many mission have max mission count" -> A and B. so it's 2
#    -> in sum,
#    -> (4 - 1) * (3 + 1) is for ABXXABXXABXX
#    -> and 2 is for AB
#
#   -> Example 2) :
#   -> AAABBB, n = 2
#   -> THE EXPECTED tuned missions is like : ABXABXAB
#   -> (3 - 1) * (2 + 1) + (2) = 8
class Solution(object):
    def leastInterval(self, tasks, n):
        count = collections.Counter(tasks)
        most = count.most_common()[0][1]
        num_most = len([i for i, v in count.items() if v == most])
        """
        example 1 : tasks = ["A","A","A","B","B","B"], n = 2
            -> we can split tasks as : A -> B -> idle -> A -> B -> idle -> A -> B
               -> 1) so there are 3-1 group. e.g. (A -> B -> idle), (A -> B -> idle)
                     and each group has (n+1) elements. e.g. (A -> B -> idle)
               -> 2) and the remain element is num_most. e.g. (A, B)
               -> 3) so total cnt = (3-1) * (2+1) + 2 = 8
    
        example 2 : tasks = ["A","A","A","A","A","A","B","C","D","E","F","G"], n = 2
            -> we can split tasks as A -> B -> C -> A -> D -> E -> A -> F -> G -> A -> idle -> idle -> A -> idle -> idle -> A
                -> 1) so there are 6-1 group. e.g. (A -> B -> C), (A -> D -> E), (A -> F -> G), (A -> idle -> idle), (A -> idle -> idle)
                      and each group has (n+1) elements. e.g. (A,B,C) .... (as above)
                -> 2) and the remain element is num_most. e.g. (A) 
                -> 3) so total cnt = (6-1)*(2+1) + 1 =  16
        """
        time = (most - 1) * (n + 1) + num_most
        return max(time, len(tasks)) # be aware of it 

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