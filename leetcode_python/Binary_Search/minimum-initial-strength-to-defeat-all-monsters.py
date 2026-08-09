"""

4008. Minimum Initial Strength to Defeat All Monsters
Solved
Medium
premium lock icon
Companies
Hint
You are given an integer array monsters, where monsters[i] represents the strength of the ith monster.

You are also given a 2D integer array boosts, where boosts[i] = [li, ri, vi] indicates that vi is added to your temporary bonus while fighting any monster whose index lies in [li, ri]. Boost ranges may overlap, and the values of all applicable boosts are added together.

You start with a non-negative initial strength and fight the monsters from left to right.

For each monster at index i:

Let bonus be the sum of the values of all boosts that apply to monster i.
You can defeat the monster only if your current strength plus bonus is at least monsters[i].
After defeating the monster, only your current strength decreases by monsters[i]. If it becomes negative, it is set to 0.
Return the minimum initial strength required to defeat all monsters.

Note: The temporary bonus is used only to determine whether the current monster can be defeated. It does not otherwise change your current strength.

 

Example 1:

Input: monsters = [5,10,15], boosts = [[1,1,10]]

Output: 30

Explanation:

Let's start with an initial strength of 30.

monsters[0] = 5: At index 0, the bonus is 0. Since 30 + 0 >= 5, this monster can be defeated. The strength becomes 30 - 5 = 25.
monsters[1] = 10: At index 1, the bonus is 10. Since 25 + 10 >= 10, this monster can be defeated. The strength becomes 25 - 10 = 15.
monsters[2] = 15: At index 2, the bonus is 0. Since 15 + 0 >= 15, this monster can be defeated. The strength becomes 15 - 15 = 0.
Thus, the minimum initial strength required is 30.

Example 2:

Input: monsters = [5,10,15], boosts = [[1,2,10],[1,2,5]]

Output: 5

Explanation:

Let's start with an initial strength of 5.

monsters[0] = 5: The bonus is 0. Since 5 + 0 >= 5, the monster can be defeated. The strength becomes 5 - 5 = 0.
monsters[1] = 10: The two overlapping boosts provide bonus = 10 + 5 = 15. Since 0 + 15 >= 10, the monster can be defeated. The strength remains 0.
monsters[2] = 15: The two overlapping boosts again provide bonus = 15. Since 0 + 15 >= 15, the monster can be defeated. The strength remains 0.
Thus, the minimum initial strength required is 5.

 

Constraints:

1 <= monsters.length <= 5 * 104
1 <= monsters[i] <= 109
0 <= boosts.length <= 5 * 104
boosts[i] == [li, ri, vi]
0 <= li <= ri < monsters.length
1 <= vi <= 109​​​​​​​


"""

# V0
class Solution(object):
    def minInitialStrength(self, monsters, boosts):
        """
        :type monsters: List[int]
        :type boosts: List[List[int]]
        :rtype: int
        """
        pass


# V0-1
# IDEA: PREFIX SUM + BINARY SEARCH (gemini)
class Solution(object):
    def minInitialStrength(self, monsters, boosts):
        """
        :type monsters: List[int]
        :type boosts: List[List[int]]
        :rtype: int
        """
        n = len(monsters)
        
        # 1. FIX: Use size n + 1 so we can easily subtract at e + 1 
        # without worrying about going out of bounds.
        prefix_list = [0] * (n + 1)
        for s, e, val in boosts:
            prefix_list[s] += val
            prefix_list[e + 1] -= val

        # 2. Enrich the difference array into actual bonuses at each index
        prefix = 0
        for i in range(n):  # FIX: Use range(n)
            prefix += prefix_list[i]
            prefix_list[i] = prefix

        # 3. FIX: Binary search bounds
        # The minimum possible strength is 0, maximum is sum(monsters)
        l = 0
        r = sum(monsters)
        min_str = r

        while l <= r:
            mid = l + (r - l) // 2
            
            # 4. FIX: Added missing colon & removed unused 'boosts' parameter
            if self.can_defeat(monsters, mid, prefix_list):
                min_str = mid
                r = mid - 1  # Keep looking for a smaller valid initial strength
            else:
                l = mid + 1

        return min_str

    def can_defeat(self, monsters, x, prefix_list):
        n = len(monsters)
        
        for i in range(n):
            # If current strength + bonus is not enough to defeat the monster
            if x + prefix_list[i] < monsters[i]:
                return False
            
            # Defeat monster, decrease current strength
            x -= monsters[i]
            
            # Strength cannot drop below zero after winning
            if x < 0:
                x = 0

        return True


# V0-2


# V1
