# V0 
import collections
class Solution:
    """
    @param ages: 
    @return: nothing
    """
    def numFriendRequests(self, ages):
        def request(a, b):
            return not (b <= 0.5 * a + 7 or b > a or b > 100 and a < 100)
        c = collections.Counter(ages)
        return sum(request(a, b) * c[a] * (c[b] - (a == b)) for a in c for b in c)

# V0'
from collections import Counter
class Solution:
    def numFriendRequests(self, ages):
        def request(x,y):
            return not ( y <= 0.5*x + 7  or 
                         y > x  or  
                        (y > 100 and x < 100)) 
        c = collections.Counter(ages)
        return sum(request(a, b) * c[a] * (c[b] - (a == b)) for a in c for b in c)

# V1 
# http://bookshadow.com/weblog/2018/04/29/leetcode-friends-of-appropriate-ages/
import collections
class Solution(object):
    def numFriendRequests(self, ages):
        """
        :type ages: List[int]
        :rtype: int
        """
        cnt = collections.Counter(ages)
        ans = 0
        for age in ages:
            cnt[age] -= 1
            left, right = age / 2 + 8, age
            ans += sum(cnt[age] for age in range(left, right + 1))
            cnt[age] += 1
        return ans
# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/83183022
import collections
class Solution(object):
    def numFriendRequests(self, ages):
        """
        :type ages: List[int]
        :rtype: int
        """
        count = collections.Counter(ages)
        ages = sorted(count.keys())
        N = len(ages)
        res = 0
        for A in ages:
            for B in range(int(0.5 * A) + 7 + 1, A + 1):
                res += count[A] * (count[B] - int(A == B))
        return res

# V1''
# https://www.jiuzhang.com/solution/friends-of-appropriate-ages/#tag-highlight-lang-python
import collections
class Solution:
    """
    @param ages: 
    @return: nothing
    get the friend request conditions, 2 for loop can it sorted  
    """
    def numFriendRequests(self, ages):
        def request(a, b):
            return not (b <= 0.5 * a + 7 or b > a or b > 100 and a < 100)
        c = collections.Counter(ages)
        return sum(request(a, b) * c[a] * (c[b] - (a == b)) for a in c for b in c)

# V2 
# Time:  O(a^2 + n), a is the number of ages,
#                    n is the number of people
# Space: O(a)
import collections
class Solution(object):
    def numFriendRequests(self, ages):
        """
        :type ages: List[int]
        :rtype: int
        """
        def request(a, b):
            return 0.5*a+7 < b <= a

        c = collections.Counter(ages)
        return sum(int(request(a, b)) * c[a]*(c[b]-int(a == b))
                   for a in c
                   for b in c)
