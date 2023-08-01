"""

1046. Last Stone Weight
Easy

You are given an array of integers stones where stones[i] is the weight of the ith stone.

We are playing a game with the stones. On each turn, we choose the heaviest two stones and smash them together. Suppose the heaviest two stones have weights x and y with x <= y. The result of this smash is:

If x == y, both stones are destroyed, and
If x != y, the stone of weight x is destroyed, and the stone of weight y has new weight y - x.
At the end of the game, there is at most one stone left.

Return the weight of the last remaining stone. If there are no stones left, return 0.

 

Example 1:

Input: stones = [2,7,4,1,8,1]
Output: 1
Explanation: 
We combine 7 and 8 to get 1 so the array converts to [2,4,1,1,1] then,
we combine 2 and 4 to get 2 so the array converts to [2,1,1,1] then,
we combine 2 and 1 to get 1 so the array converts to [1,1,1] then,
we combine 1 and 1 to get 0 so the array converts to [1] then that's the value of the last stone.
Example 2:

Input: stones = [1]
Output: 1
 

Constraints:

1 <= stones.length <= 30
1 <= stones[i] <= 1000


"""

# V0

# V1
# IDEA : Array-Based Simulation
# https://leetcode.com/problems/last-stone-weight/editorial/
class Solution:
    def lastStoneWeight(self, stones: List[int]) -> int:

        def remove_largest():
            index_of_largest = stones.index(max(stones))
            # Swap the stone to be removed with the end.
            stones[index_of_largest], stones[-1] = stones[-1], stones[index_of_largest]
            return stones.pop()

        while len(stones) > 1:
            stone_1 = remove_largest()
            stone_2 = remove_largest()
            if stone_1 != stone_2:
                stones.append(stone_1 - stone_2)

        return stones[0] if stones else 0

# V2
# IDEA : Sorted Array-Based Simulation
# https://leetcode.com/problems/last-stone-weight/editorial/
class Solution:
    def lastStoneWeight(self, stones: List[int]) -> int:
        stones.sort()
        while len(stones) > 1:
            stone_1 = stones.pop()
            stone_2 = stones.pop()
            if stone_1 != stone_2:
                bisect.insort(stones, stone_1 - stone_2)
        return stones[0] if stones else 0

# V3
# IDEA : Heap-Based Simulation
# https://leetcode.com/problems/last-stone-weight/editorial/
class Solution:
    def lastStoneWeight(self, stones: List[int]) -> int:

        # Make all the stones negative. We want to do this *in place*, to keep the
        # space complexity of this algorithm at O(1). :-)
        for i in range(len(stones)):
            stones[i] *= -1

        # Heapify all the stones.
        heapq.heapify(stones)

        # While there is more than one stone left, remove the two
        # largest, smash them together, and insert the result
        # back into the heap if it is non-zero.
        while len(stones) > 1:
            stone_1 = heapq.heappop(stones)
            stone_2 = heapq.heappop(stones)
            if stone_1 != stone_2:
                heapq.heappush(stones, stone_1 - stone_2)

        # Check if there is a stone left to return. Convert it back
        # to positive.
        return -heapq.heappop(stones) if stones else 0