"""

3638. Maximum Balanced Shipments
Medium

You are given an integer array weight of length n, representing the weights
of n parcels arranged in a straight line. A shipment is defined as a
contiguous subarray of parcels. A shipment is considered balanced if the
weight of the last parcel is strictly less than the maximum weight among all
parcels in that shipment.

Select a set of non-overlapping, contiguous, balanced shipments such that
each parcel appears in at most one shipment (parcels may remain unshipped).

Return the maximum possible number of balanced shipments that can be formed.

Example 1:

Input: weight = [2,5,1,4,3]
Output: 2
Explanation:
We can form the maximum of two balanced shipments as follows:
Shipment 1: [2, 5, 1]
Maximum parcel weight = 5
Last parcel weight = 1, which is strictly less than 5. Thus, it's balanced.
Shipment 2: [4, 3]
Maximum parcel weight = 4
Last parcel weight = 3, which is strictly less than 4. Thus, it's balanced.
It is impossible to partition the parcels to achieve more than two balanced
shipments, so the answer is 2.

Example 2:

Input: weight = [4,4]
Output: 0
Explanation:
No balanced shipment can be formed in this case:
A shipment [4, 4] has maximum weight 4 and the last parcel's weight is also
4, which is not strictly less. Thus, it's not balanced.
Single-parcel shipments [4] have the last parcel weight equal to the maximum
parcel weight, thus not balanced.
As there is no way to form even one balanced shipment, the answer is 0.

Constraints:

2 <= n <= 10^5
1 <= weight[i] <= 10^9

"""

# V0
# IDEA : GREEDY — CLOSE A SHIPMENT AT THE FIRST MOMENT IT BECOMES BALANCED
#
#   shipments are contiguous and ordered, so a plan is just a set of cut
#   points. scanning left to right and keeping the running maximum, the
#   moment weight[i] drops below that maximum the current shipment is already
#   balanced.
#
#   closing it right there is never worse: keeping it open only consumes more
#   parcels for the same single shipment, while the parcels released are free
#   to start a fresh shipment. an exchange argument turns any optimal plan
#   into this greedy one shipment at a time.
#
# time = O(n), space = O(1)
class Solution(object):
    def maxBalancedShipments(self, weight):
        count = 0
        cur_max = 0
        for w in weight:
            if w < cur_max:
                count += 1
                cur_max = 0
            elif w > cur_max:
                cur_max = w
        return count
