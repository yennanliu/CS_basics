"""

1996. The Number of Weak Characters in the Game
Medium

You are playing a game that contains multiple characters, and each of the characters has two main properties: attack and defense. You are given a 2D integer array properties where properties[i] = [attacki, defensei] represents the properties of the ith character in the game.

A character is said to be weak if any other character has both attack and defense levels strictly greater than this character's attack and defense levels. More formally, a character i is said to be weak if there exists another character j where attackj > attacki and defensej > defensei.

Return the number of weak characters.


Example 1:

Input: properties = [[5,5],[6,3],[3,6]]
Output: 0
Explanation: No character has strictly greater attack and defense than the other.

Example 2:

Input: properties = [[2,2],[3,3]]
Output: 1
Explanation: The first character is weak because the second character has a strictly greater attack and defense.

Example 3:

Input: properties = [[1,5],[10,4],[4,3]]
Output: 1
Explanation: The third character is weak because the second character has a strictly greater attack and defense.


Constraints:

2 <= properties.length <= 10^5
properties[i].length == 2
1 <= attacki, defensei <= 10^5

"""

# V0
# IDEA : SORT BY (attack DESC, defense ASC) + RUNNING MAX DEFENSE
#
#   scanning in that order, every character seen BEFORE the current one has
#   attack >= mine; the tie-break (defense ascending within equal attack)
#   guarantees that anyone with the SAME attack was seen with a defense <=
#   mine, so they can never inflate the running max above my defense.
#
#   therefore : mx (max defense seen so far) > my defense  <=>  someone
#   strictly dominates me -> count me as weak.
#
#   NOTE : the ascending defense tie-break is the whole trick; sorting
#          defense descending would wrongly flag equal-attack characters.
#
# time = O(n log n), space = O(n) for the sort
class Solution(object):
    def numberOfWeakCharacters(self, properties):
        properties.sort(key=lambda p: (-p[0], p[1]))
        res = 0
        mx = 0
        for _, d in properties:
            if d < mx:
                res += 1
            else:
                mx = d
        return res
