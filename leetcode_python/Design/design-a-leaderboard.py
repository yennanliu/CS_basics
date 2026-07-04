# https://leetcode.ca/all/1244.html

"""

1244. Design A Leaderboard
Design a Leaderboard class, which has 3 functions:

addScore(playerId, score): Update the leaderboard by adding score to the given player's score. If there is no player with such id in the leaderboard, add him to the leaderboard with the given score.
top(K): Return the score sum of the top K players.
reset(playerId): Reset the score of the player with the given id to 0. It is guaranteed that the player was added to the leaderboard before calling this function.
Initially, the leaderboard is empty.

 

Example 1:

Input: 
["Leaderboard","addScore","addScore","addScore","addScore","addScore","top","reset","reset","addScore","top"]
[[],[1,73],[2,56],[3,39],[4,51],[5,4],[1],[1],[2],[2,51],[3]]
Output: 
[null,null,null,null,null,null,73,null,null,null,141]

Explanation: 
Leaderboard leaderboard = new Leaderboard ();
leaderboard.addScore(1,73);   // leaderboard = [[1,73]];
leaderboard.addScore(2,56);   // leaderboard = [[1,73],[2,56]];
leaderboard.addScore(3,39);   // leaderboard = [[1,73],[2,56],[3,39]];
leaderboard.addScore(4,51);   // leaderboard = [[1,73],[2,56],[3,39],[4,51]];
leaderboard.addScore(5,4);    // leaderboard = [[1,73],[2,56],[3,39],[4,51],[5,4]];
leaderboard.top(1);           // returns 73;
leaderboard.reset(1);         // leaderboard = [[2,56],[3,39],[4,51],[5,4]];
leaderboard.reset(2);         // leaderboard = [[3,39],[4,51],[5,4]];
leaderboard.addScore(2,51);   // leaderboard = [[2,51],[3,39],[4,51],[5,4]];
leaderboard.top(3);           // returns 141 = 51 + 51 + 39;
 

Constraints:

1 <= playerId, K <= 10000
It's guaranteed that K is less than or equal to the current number of players.
1 <= score <= 100
There will be at most 1000 function calls.
Difficulty:
Medium
Lock:
Prime
Company:
Wayfair
Problem Solution
1244-Design-A-Leaderboard


"""


# V0
class Leaderboard:
    def __init__(self):

    def addScore(self, playerId, score):

    def top(self, K):
        

    def reset(self, playerId):




# V0-1
# IDEA: HASHMAP (gpt)
class Leaderboard:

    def __init__(self):
        self.user_score = {}

    def addScore(self, playerId, score):
        self.user_score[playerId] = self.user_score.get(playerId, 0) + score

    def top(self, K):
        return sum(sorted(self.user_score.values(), reverse=True)[:K])

    def reset(self, playerId):
        self.user_score[playerId] = 0



# V0-2
# IDEA: PQ (gemini)
import heapq

class Leaderboard:

    def __init__(self):
        # Maps playerId -> current total score
        self.scores = {}

    def addScore(self, playerId: int, score: int) -> None:
        # If the player exists, add to their score. Otherwise, initialize it.
        if playerId in self.scores:
            self.scores[playerId] += score
        else:
            self.scores[playerId] = score

    def top(self, K: int) -> int:
        # Step 1: Use a min-heap to keep track of the top K scores
        min_heap = []
        
        """
        NOTE !!! below

        -> we form the PQ when `top` is called everytime,
           then collect the top K
           -> and NOTE that since what we need is `sum` of top K
           		-> so we can atually use `min heap`
        """
        for score in self.scores.values():
            heapq.heappush(min_heap, score)
            # If the heap size exceeds K, pop the smallest element
            if len(min_heap) > K:
                heapq.heappop(min_heap)
                
        # Step 2: Return the SUM of the top K elements
        return sum(min_heap)

    def reset(self, playerId: int) -> None:
        # Delete or reset the player's score to 0
        if playerId in self.scores:
            del self.scores[playerId]


# V1
# https://leetcode.ca/2019-04-27-1244-Design-A-Leaderboard/
from sortedcontainers import SortedList


class Leaderboard:
    def __init__(self):
        self.d = defaultdict(int)
        self.rank = SortedList()

    def addScore(self, playerId: int, score: int) -> None:
        if playerId not in self.d:
            self.d[playerId] = score
            self.rank.add(score)
        else:
            self.rank.remove(self.d[playerId])
            self.d[playerId] += score
            self.rank.add(self.d[playerId])

    def top(self, K: int) -> int:
        return sum(self.rank[-K:])

    def reset(self, playerId: int) -> None:
        self.rank.remove(self.d.pop(playerId))


# V2
