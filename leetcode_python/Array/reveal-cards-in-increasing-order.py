# V0

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/84728767
import collections
class Solution(object):
    def deckRevealedIncreasing(self, deck):
        """
        :type deck: List[int]
        :rtype: List[int]
        """
        deck.sort()
        N = len(deck)
        res = [0] * N
        que = collections.deque()
        for i in range(N):
            if que:
                que.appendleft(que.pop())
            que.appendleft(deck.pop())
        return list(que)

# V1'
# https://www.cnblogs.com/seyjs/p/10058367.html
class Solution(object):
    def deckRevealedIncreasing(self, deck):
        """
        :type deck: List[int]
        :rtype: List[int]
        """
        deck.sort(reverse = True)
        res = []
        while len(deck) > 0:
            v = deck.pop(0)
            if len(res) == 0:
                res.append(v)
            else:
                res.insert(0,res.pop(-1))
                res.insert(0,v)
        return res
        
# V2 
# Time:  O(n)
# Space: O(n)
import collections
class Solution(object):
    def deckRevealedIncreasing(self, deck):
        """
        :type deck: List[int]
        :rtype: List[int]
        """
        d = collections.deque()
        deck.sort(reverse=True)
        for i in deck:
            if d:
                d.appendleft(d.pop())
            d.appendleft(i)
        return list(d)