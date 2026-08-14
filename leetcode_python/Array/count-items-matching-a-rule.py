"""

1773. Count Items Matching a Rule
Easy

You are given an array items, where each items[i] = [typei, colori, namei] describes the type, color, and name of the i^th item. You are also given a rule represented by two strings, ruleKey and ruleValue.

The i^th item is said to match the rule if one of the following is true:

ruleKey == "type" and ruleValue == typei.
ruleKey == "color" and ruleValue == colori.
ruleKey == "name" and ruleValue == namei.

Return the number of items that match the given rule.

Example 1:

Input: items = [["phone","blue","pixel"],["computer","silver","lenovo"],["phone","gold","iphone"]], ruleKey = "color", ruleValue = "silver"
Output: 1
Explanation: There is only one item matching the given rule, which is ["computer","silver","lenovo"].

Example 2:

Input: items = [["phone","blue","pixel"],["computer","silver","phone"],["phone","gold","iphone"]], ruleKey = "type", ruleValue = "phone"
Output: 2
Explanation: There are only two items matching the given rule, which are ["phone","blue","pixel"] and ["phone","gold","iphone"]. Note that the item ["computer","silver","phone"] does not match.

Constraints:

1 <= items.length <= 10^4
1 <= typei.length, colori.length, namei.length, ruleValue.length <= 10
ruleKey is equal to either "type", "color", or "name".
All strings consist only of lowercase letters.

"""

# V0
# IDEA : MAP THE RULE KEY TO A COLUMN INDEX, THEN COUNT
#
#   "type" -> column 0, "color" -> column 1, "name" -> column 2.
#   after that a single pass comparing items[i][col] with ruleValue is enough.
#
# time = O(n), space = O(1)
class Solution(object):
    def countMatches(self, items, ruleKey, ruleValue):
        col = {"type": 0, "color": 1, "name": 2}[ruleKey]
        res = 0
        for it in items:
            if it[col] == ruleValue:
                res += 1
        return res
