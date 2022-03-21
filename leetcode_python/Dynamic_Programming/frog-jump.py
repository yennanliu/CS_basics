"""

403. Frog Jump
Hard

A frog is crossing a river. The river is divided into some number of units, and at each unit, there may or may not exist a stone. The frog can jump on a stone, but it must not jump into the water.

Given a list of stones' positions (in units) in sorted ascending order, determine if the frog can cross the river by landing on the last stone. Initially, the frog is on the first stone and assumes the first jump must be 1 unit.

If the frog's last jump was k units, its next jump must be either k - 1, k, or k + 1 units. The frog can only jump in the forward direction.

 

Example 1:

Input: stones = [0,1,3,5,6,8,12,17]
Output: true
Explanation: The frog can jump to the last stone by jumping 1 unit to the 2nd stone, then 2 units to the 3rd stone, then 2 units to the 4th stone, then 3 units to the 6th stone, 4 units to the 7th stone, and 5 units to the 8th stone.
Example 2:

Input: stones = [0,1,2,3,4,8,9,11]
Output: false
Explanation: There is no way to jump to the last stone as the gap between the 5th and 6th stone is too large.
 

Constraints:

2 <= stones.length <= 2000
0 <= stones[i] <= 231 - 1
stones[0] == 0
stones is sorted in a strictly increasing order.

"""

# V0

# V1
# IDEA : BFS
# https://leetcode.com/problems/frog-jump/discuss/518608/python-BFS
class Solution:
    def canCross(self, stones: List[int]) -> bool:
        last = stones[-1] 
        visited = set()
        stones = set(stones)
        q = [(0,0)]
        while q:
            for _ in range(len(q)):
                node,jump = q.pop(0)
                if node == last:
                    return True
                for j in [jump+1,jump-1,jump]:
                    if j > 0:
                        if 0<=node+j<=last:
                            if node+j in stones and ((node+j,j) not in visited):
                                q.append((node+j,j))
                                visited.add( (node+j, j) )

        return False

# V1'
# https://leetcode-cn.com/problems/frog-jump/
# https://soloveri.gitbook.io/leetcode/dynamic-programming/403-frog-jump
# https://blog.csdn.net/Guo15331092/article/details/78483997

# V1''
# IDEA : Memoization
# https://leetcode.com/problems/frog-jump/discuss/88854/Python-solution-with-detailed-explanation
class Solution(object):
    def helper(self, pos, k, stones, N, cache):
        if pos > N:
            return False
        elif pos == N:
            return True
        elif pos in cache and k in cache[pos]:
            return cache[pos][k]
        else:
            cache.setdefault(pos, {})[k] = False
            for jump in (k-1, k, k+1):
                if jump > 0 and (pos+jump) in stones:
                    if self.helper(pos+jump, jump, stones, N, cache):
                        cache[pos][k] = True
                        return True
            return False
        
    def canCross(self, stones):
        """
        :type stones: List[int]
        :rtype: bool
        """
        return self.helper(stones[0], 0, set(stones), stones[-1], {})

# V1'''
# IDEA : DFS
# https://leetcode.com/problems/frog-jump/discuss/511679/Python-by-DFS-DP-w-Comment
class Solution(object):
    def canCross(self, stones):
        
        # total number of valid jump index
        n = len(stones)
        
        # valid jump index
        stone_set = set(stones)
        
        # record of visited status
        visited = set()
        
        # source stone index, destination stone index
        source, destination = stones[0], stones[n-1]
        
        # ----------------------------------------
        
        def frog_jump( cur_idx, cur_step):
            
            # Compute current position
            cur_move = cur_idx + cur_step
            
			
			## Base cases:
            if (cur_step <= 0) or (cur_move not in stone_set) or ( (cur_idx, cur_step) in visited ):
                
                # Reject on backward move
                # Reject on invalid move
                # Reject on repeated path
                return False
            
            elif cur_move == destination:
                
                # Accept on destination arrival
                return True
            
			
			## General cases:
			
            # mark current status as visited
            visited.add( (cur_idx, cur_step) )
            
            # explore all possible next move
            return any( frog_jump(cur_move, cur_step + step_offset) for step_offset in (1, 0, -1) )
        
        # ----------------------------------------
        return frog_jump(cur_idx=source, cur_step=1)

# V1''''
# IDEA : DP bottom up
# https://leetcode.com/problems/frog-jump/discuss/223586/Python-solution
# IDEA :
# Use a dictionary dic which maps the position of a stone in stones to the set of stepsizes that can jump onto the stone. We initialize dic = {0:{0}}, meaning that we start with the stone at position 0. Next, we iterate i over range(len(stones)), and check if stones[i] is in dic, if it is, it means that there are previous jumps that land on this stone, and we can continue jumping ahead, in which case we iterate over all val in dic[stones[i]], and for each val, we can continue jumping ahead with three stepsizes (val-1, val, and val+1). Therefore, we add val-1 to dic[stones[i]+val-1], val to dic[stones[i]+val], and val+1 to dic[stones[i]+val+1]. Finally, we check if stones[-1] is in dic, if it is, we return True; Else we return False.
#
# Time complexity: O(n^2), space complexity: O(n^2).
class Solution:
    def canCross(self, stones):
        dic = collections.defaultdict(set)
        dic[0].add(0)
        for i in range(len(stones)):
            if stones[i] in dic:
                for val in dic[stones[i]]:
                    if val > 0:
                        dic[stones[i]+val].add(val)
                    if val > 1:
                        dic[stones[i]+val-1].add(val-1)
                    dic[stones[i]+val+1].add(val+1)
        return stones[-1] in dic

# V1'''''
# IDEA : Brute Force (TLE)
# https://leetcode.com/problems/frog-jump/solution/
# JAVA
# public class Solution {
#     public boolean canCross(int[] stones) {
#         return can_Cross(stones, 0, 0);
#     }
#     public boolean can_Cross(int[] stones, int ind, int jumpsize) {
#         for (int i = ind + 1; i < stones.length; i++) {
#             int gap = stones[i] - stones[ind];
#             if (gap >= jumpsize - 1 && gap <= jumpsize + 1) {
#                 if (can_Cross(stones, i, gap)) {
#                     return true;
#                 }
#             }
#         }
#         return ind == stones.length - 1;
#     }
# }

# V1''''''
# IDEA : BETTER BRUTE FORCE (TLE)
# https://leetcode.com/problems/frog-jump/solution/
# JAVA
# public class Solution {
#     public boolean canCross(int[] stones) {
#         return can_Cross(stones, 0, 0);
#     }
#     public boolean can_Cross(int[] stones, int ind, int jumpsize) {
#         if (ind == stones.length - 1) {
#             return true;
#         }
#         int ind1 = Arrays.binarySearch(stones, ind + 1, stones.length, stones[ind] + jumpsize);
#         if (ind1 >= 0 && can_Cross(stones, ind1, jumpsize)) {
#             return true;
#         }
#         int ind2 = Arrays.binarySearch(stones, ind + 1, stones.length, stones[ind] + jumpsize - 1);
#         if (ind2 >= 0 && can_Cross(stones, ind2, jumpsize - 1)) {
#             return true;
#         }
#         int ind3 = Arrays.binarySearch(stones, ind + 1, stones.length, stones[ind] + jumpsize + 1);
#         if (ind3 >= 0 && can_Cross(stones, ind3, jumpsize + 1)) {
#             return true;
#         }
#         return false;
#     }
# }

# V1''''''
# IDEA : Using Memoization
# https://leetcode.com/problems/frog-jump/solution/
# JAVA
# public class Solution {
#     public boolean canCross(int[] stones) {
#         int[][] memo = new int[stones.length][stones.length];
#         for (int[] row : memo) {
#             Arrays.fill(row, -1);
#         }
#         return can_Cross(stones, 0, 0, memo) == 1;
#     }
#     public int can_Cross(int[] stones, int ind, int jumpsize, int[][] memo) {
#         if (memo[ind][jumpsize] >= 0) {
#             return memo[ind][jumpsize];
#         }
#         for (int i = ind + 1; i < stones.length; i++) {
#             int gap = stones[i] - stones[ind];
#             if (gap >= jumpsize - 1 && gap <= jumpsize + 1) {
#                 if (can_Cross(stones, i, gap, memo) == 1) {
#                     memo[ind][gap] = 1;
#                     return 1;
#                 }
#             }
#         }
#         memo[ind][jumpsize] = (ind == stones.length - 1) ? 1 : 0;
#         return memo[ind][jumpsize];
#     }
# }

# V1'''''''
# IDEA : Using Memoization with Binary Search
# https://leetcode.com/problems/frog-jump/solution/
# JAVA
# public class Solution {
#     public boolean canCross(int[] stones) {
#         int[][] memo = new int[stones.length][stones.length];
#         for (int[] row : memo) {
#             Arrays.fill(row, -1);
#         }
#         return can_Cross(stones, 0, 0, memo) == 1;
#     }
#     public int can_Cross(int[] stones, int ind, int jumpsize, int[][] memo) {
#         if (memo[ind][jumpsize] >= 0) {
#             return memo[ind][jumpsize];
#         }
#         int ind1 = Arrays.binarySearch(stones, ind + 1, stones.length, stones[ind] + jumpsize);
#         if (ind1 >= 0 && can_Cross(stones, ind1, jumpsize, memo) == 1) {
#             memo[ind][jumpsize] = 1;
#             return 1;
#         }
#         int ind2 = Arrays.binarySearch(stones, ind + 1, stones.length, stones[ind] + jumpsize - 1);
#         if (ind2 >= 0 && can_Cross(stones, ind2, jumpsize - 1, memo) == 1) {
#             memo[ind][jumpsize - 1] = 1;
#             return 1;
#         }
#         int ind3 = Arrays.binarySearch(stones, ind + 1, stones.length, stones[ind] + jumpsize + 1);
#         if (ind3 >= 0 && can_Cross(stones, ind3, jumpsize + 1, memo) == 1) {
#             memo[ind][jumpsize + 1] = 1;
#             return 1;
#         }
#         memo[ind][jumpsize] = ((ind == stones.length - 1) ? 1 : 0);
#         return memo[ind][jumpsize];
#     }
# }

# V2