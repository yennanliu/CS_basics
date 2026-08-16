"""

3638. Maximum Balanced Shipments
Medium

You are given an integer array weight of length n, representing the weights of n parcels arranged in a fixed order.

Partition these parcels into one or more shipments. Each shipment must consist of a contiguous block of parcels.

A shipment is considered balanced if the weight of the last parcel is strictly less than the maximum weight among all parcels in that shipment.

Return the maximum possible number of balanced shipments that can be formed from these parcels.

Note: Not all parcels need to be shipped.


Example 1:

Input: weight = [2,5,1,4,3]
Output: 2
Explanation:
Split into shipments [2, 5, 1] and [4, 3]:
Shipment [2, 5, 1] has a maximum weight of 5, and the last parcel weighs 1, which is strictly less than 5. So this shipment is balanced.
Shipment [4, 3] has a maximum weight of 4, and the last parcel weighs 3, which is strictly less than 4. So this shipment is balanced.
It is impossible to partition these parcels into more than two balanced shipments.

Example 2:

Input: weight = [4,4]
Output: 0
Explanation:
No balanced shipment can be formed, since the last parcel of any shipment is never strictly less than the maximum weight of that shipment.


Constraints:

2 <= n == weight.length <= 10^5
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
