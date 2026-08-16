"""

3668. Restore Finishing Order
Easy

You are given an integer array order of length n and an integer array friends.

order contains every integer from 1 to n exactly once, representing the IDs of the participants of a race in the order they finished.
friends contains the IDs of your friends in increasing order, and each ID appears in order.

Return an array containing your friends' IDs in the order they finished the race.


Example 1:

Input: order = [3,1,2,5,4], friends = [1,3,4]
Output: [3,1,4]
Explanation:
Friend with ID 3 finished first, friend with ID 1 finished second and friend with ID 4 finished last, so the answer is [3, 1, 4].

Example 2:

Input: order = [1,4,5,3,2], friends = [2,5]
Output: [5,2]
Explanation:
Friend with ID 5 finished third and friend with ID 2 finished last, so the answer is [5, 2].


Constraints:

1 <= n == order.length <= 100
order contains every integer from 1 to n exactly once.
1 <= friends.length <= min(8, n)
1 <= friends[i] <= n
friends is strictly increasing and all its elements appear in order.

"""

# V0
# IDEA : ONE PASS OVER order, FILTERING BY A MEMBERSHIP SET
#
#   the requested output order is exactly the order in which ids appear in
#   `order`, so there is nothing to sort — walk `order` once and keep the ids
#   that belong to friends.
#
#   turning friends into a set makes each membership test O(1), which also
#   avoids any dependence on friends already being sorted.
#
# time = O(n), space = O(n)
class Solution(object):
    def recoverOrder(self, order, friends):
        want = set(friends)
        return [x for x in order if x in want]
