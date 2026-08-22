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


# V0-1
# IDEA : AGGREGATE THE COLUMN INTO A COUNTER, THEN ONE LOOKUP
#
#   instead of comparing every row against ruleValue, tally the whole column
#   once and read the answer out of the hash map. same O(n) pass, but the work
#   is "count everything" rather than "test everything" -- handy when several
#   ruleValues have to be answered for the SAME ruleKey.
#
# time = O(n)
# space = O(n)   (one bucket per distinct value in that column)
from collections import Counter
class Solution(object):
    def countMatches(self, items, ruleKey, ruleValue):
        col = ("type", "color", "name").index(ruleKey)
        cnt = Counter(it[col] for it in items)
        return cnt[ruleValue]


# V0-2
# IDEA : PRE-BUILD AN INVERTED INDEX OVER ALL 3 ATTRIBUTES
#
#   key the counter by the PAIR (attribute name, value) instead of by value
#   only. one pass over the 3n cells answers any (ruleKey, ruleValue) query in
#   O(1) afterwards, so repeated queries cost nothing extra.
#   note `cnt[...]` on a Counter returns 0 for a missing key -- no KeyError for
#   a ruleValue that never occurs.
#
# time = O(n)     (3 cells per item)
# space = O(n)
from collections import Counter
class Solution(object):
    def countMatches(self, items, ruleKey, ruleValue):
        cnt = Counter()
        for typ, color, name in items:
            cnt[("type", typ)] += 1
            cnt[("color", color)] += 1
            cnt[("name", name)] += 1
        return cnt[(ruleKey, ruleValue)]
