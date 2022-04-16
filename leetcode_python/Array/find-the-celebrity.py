"""

277. Find the Celebrity
Medium

Suppose you are at a party with n people labeled from 0 to n - 1 and among them, there may exist one celebrity. The definition of a celebrity is that all the other n - 1 people know the celebrity, but the celebrity does not know any of them.

Now you want to find out who the celebrity is or verify that there is not one. The only thing you are allowed to do is ask questions like: "Hi, A. Do you know B?" to get information about whether A knows B. You need to find out the celebrity (or verify there is not one) by asking as few questions as possible (in the asymptotic sense).

You are given a helper function bool knows(a, b) that tells you whether A knows B. Implement a function int findCelebrity(n). There will be exactly one celebrity if they are at the party.

Return the celebrity's label if there is a celebrity at the party. If there is no celebrity, return -1.

 

Example 1:


Input: graph = [[1,1,0],[0,1,0],[1,1,1]]
Output: 1
Explanation: There are three persons labeled with 0, 1 and 2. graph[i][j] = 1 means person i knows person j, otherwise graph[i][j] = 0 means person i does not know person j. The celebrity is the person labeled as 1 because both 0 and 2 know him but 1 does not know anybody.
Example 2:


Input: graph = [[1,0,1],[1,1,0],[0,1,1]]
Output: -1
Explanation: There is no celebrity.
 

Constraints:

n == graph.length
n == graph[i].length
2 <= n <= 100
graph[i][j] is 0 or 1.
graph[i][i] == 1
 

Follow up: If the maximum number of allowed calls to the API knows is 3 * n, could you find a solution without exceeding the maximum number of calls?

"""

# V0
class Solution:
    def findCelebrity(self, n: int) -> int:
        self.n = n
        for i in range(n):
            if self.is_celebrity(i):
                return i
        return -1
    
    def is_celebrity(self, i):
        for j in range(self.n):
            if i == j: continue # Don't ask if they know themselves.
            if knows(i, j) or not knows(j, i):
                return False
        return True

# V0'
class Solution:
    # @param {int} n a party with n people
    # @return {int} the celebrity's label or -1
    def findCelebrity(self, n):
        celeb = 0
        
        for i in range(1, n):
            if Celebrity.knows(celeb, i): # if celeb knows i, then the given celeb must not a celebrity, so we move to the next possible celeb
                celeb = i                 # move from celeb to i 
        
        # Check if the final candicate is the celebrity
        for i in range(n):
            if celeb != i and Celebrity.knows(celeb, i):    # to check if the Celebrity really knows no one 
                return -1
            if celeb != i and not Celebrity.knows(i, celeb): # to check if everyone (except Celebrity) really knows the Celebrity
                return -1
        return celeb

# V1
# IDEA : BRUTE FORCE
# https://leetcode.com/problems/find-the-celebrity/solution/
class Solution:
    def findCelebrity(self, n: int) -> int:
        self.n = n
        for i in range(n):
            if self.is_celebrity(i):
                return i
        return -1
    
    def is_celebrity(self, i):
        for j in range(self.n):
            if i == j: continue # Don't ask if they know themselves.
            if knows(i, j) or not knows(j, i):
                return False
        return True

# V1
# IDEA : Logical Deduction
# https://leetcode.com/problems/find-the-celebrity/solution/
class Solution:
    def findCelebrity(self, n: int) -> int:
        self.n = n
        celebrity_candidate = 0
        for i in range(1, n):
            if knows(celebrity_candidate, i):
                celebrity_candidate = i
        if self.is_celebrity(celebrity_candidate):
            return celebrity_candidate
        return -1

    def is_celebrity(self, i):
        for j in range(self.n):
            if i == j: continue
            if knows(i, j) or not knows(j, i):
                return False
        return True

# V1
# IDEA :  Logical Deduction with Caching
# https://leetcode.com/problems/find-the-celebrity/solution/
from functools import lru_cache

class Solution:
    
    @lru_cache(maxsize=None)
    def cachedKnows(self, a, b):
        return knows(a, b)
    
    def findCelebrity(self, n: int) -> int:
        self.n = n
        celebrity_candidate = 0
        for i in range(1, n):
            if self.cachedKnows(celebrity_candidate, i):
                celebrity_candidate = i
        if self.is_celebrity(celebrity_candidate):
            return celebrity_candidate
        return -1

    def is_celebrity(self, i):
        for j in range(self.n):
            if i == j: continue
            if self.cachedKnows(i, j) or not self.cachedKnows(j, i):
                return False
        return True

# V1 
# https://www.jiuzhang.com/solution/find-the-celebrity/#tag-highlight-lang-python
# IDEA :
# AS A CELEBRITY, HE/SHE MOST KNOW NO ONE IN THE GROUP
# AND REST OF PEOPLE (EXCEPT CELEBRITY) MOST ALL KNOW THE CELEBRITY
# SO WE CAN USE THE FACT 1) : AS A CELEBRITY, HE/SHE MOST KNOW NO ONE IN THE GROUP
# -> GO THROUGH ALL PEOPLE IN THE GROUP TO FIND ONE WHO KNOW NO ONE, THEN HE/SHE MAY BE THE CELEBRITY POSSIBILY 
# -> THEN GO THROUGH REST OF THE PEOPLE AGAIN TO VALIDATE IF HE/SHE IS THE TRUE CELEBRITY
"""
The knows API is already defined for you.
@param a, person a
@param b, person b
@return a boolean, whether a knows b
you can call Celebrity.knows(a, b)
"""
class Solution:
    # @param {int} n a party with n people
    # @return {int} the celebrity's label or -1
    def findCelebrity(self, n):
        celeb = 0
        
        for i in range(1, n):
            if Celebrity.knows(celeb, i): # if celeb knows i, then the given celeb must not a celebrity, so we move to the next possible celeb
                celeb = i                 # move from celeb to i 
        
        # Check if the final candicate is the celebrity
        for i in range(n):
            if celeb != i and Celebrity.knows(celeb, i):    # to check if the Celebrity really knows no one 
                return -1
            if celeb != i and not Celebrity.knows(i, celeb): # to check if everyone (except Celebrity) really knows the Celebrity
                return -1
        return celeb

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def findCelebrity(self, n):
        """
        :type n: int
        :rtype: int
        """
        candidate = 0
        # Find the candidate.
        for i in range(1, n):
            if knows(candidate, i):  # noqa
                candidate = i        # All candidates < i are not celebrity candidates.
        # Verify the candidate.
        for i in range(n):
            candidate_knows_i = knows(candidate, i) # noqa
            i_knows_candidate = knows(i, candidate) # noqa
            if i != candidate and (candidate_knows_i or
                                   not i_knows_candidate):
                return -1
        return candidate