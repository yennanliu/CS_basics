"""

1826. Faulty Sensor
Easy

An experiment is being conducted in a lab. To ensure accuracy, there are two sensors collecting data simultaneously. You are given two arrays sensor1 and sensor2, where sensor1[i] and sensor2[i] are the ith data points collected by the two sensors.

However, this type of sensor has a chance of being defective, which causes exactly one data point to be dropped. After the data is dropped, all the data points to the right of the dropped data are shifted one place to the left, and the last data point is replaced with some random value. It is guaranteed that this random value will not be equal to the dropped value.

For example, if the correct data is [1,2,3,4,5] and 3 is dropped, the sensor could return [1,2,4,5,7] (the last position can be any value, not just 7).

We know that there is a defect in at most one of the sensors. Return the sensor number (1 or 2) with the defect. If there is no defect in either sensor or if it is impossible to determine the defective sensor, return -1.


Example 1:

Input: sensor1 = [2,3,4,5], sensor2 = [2,1,3,4]
Output: 1
Explanation: Sensor 2 has the correct values.
The second data point from sensor 2 is dropped, and the last value of sensor 1 is replaced by a 5.

Example 2:

Input: sensor1 = [2,2,2,2,2], sensor2 = [2,2,2,2,5]
Output: -1
Explanation: It is impossible to determine which sensor has a defect.
Dropping the last value for either sensor could produce the output for the other sensor.

Example 3:

Input: sensor1 = [2,3,2,2,3,2], sensor2 = [2,3,2,3,2,7]
Output: 2
Explanation: Sensor 1 has the correct values.
The fourth data point from sensor 1 is dropped, and the last value of sensor 1 is replaced by a 7.


Constraints:

sensor1.length == sensor2.length
1 <= sensor1.length <= 100
1 <= sensor1[i], sensor2[i] <= 100

"""

# V0
# IDEA : FIRST MISMATCH + TEST BOTH SHIFT HYPOTHESES
#
#   the two arrays agree until the drop happens; let i be the first index where
#   they differ (ignore the very last slot -- it holds a random value either
#   way, so it proves nothing).
#
#   if sensor1 is the defective one, it lost one point at i, so from i on it is
#   sensor2 shifted left by one :   sensor1[j] == sensor2[j + 1]
#   symmetrically, sensor2 defective means  sensor2[j] == sensor1[j + 1]
#   both for every j in [i, n - 2].
#
#   check both hypotheses; exactly one holding gives the answer.
#
#   NOTE : if BOTH still hold (e.g. a run of equal values) we cannot tell them
#          apart -> -1, which is example 2.
#   NOTE : if no mismatch is found before the last slot, neither is provably
#          defective -> -1.
#
# time = O(n), space = O(1)
class Solution(object):
    def badSensor(self, sensor1, sensor2):
        n = len(sensor1)
        i = 0
        while i < n - 1 and sensor1[i] == sensor2[i]:
            i += 1
        if i >= n - 1:
            return -1

        ok1 = True   # sensor1 dropped a point
        ok2 = True   # sensor2 dropped a point
        for j in range(i, n - 1):
            if sensor1[j] != sensor2[j + 1]:
                ok1 = False
            if sensor2[j] != sensor1[j + 1]:
                ok2 = False

        if ok1 and not ok2:
            return 1
        if ok2 and not ok1:
            return 2
        return -1


# V0-1
# IDEA : BRUTE FORCE -- SIMULATE EVERY POSSIBLE DROP AND SEE WHICH ONE FITS
#
#   "sensor1 is the broken one" says exactly this: the truth is sensor2, and
#   sensor1 is sensor2 with one entry deleted, the tail shifted left, and the
#   final slot filled with garbage.  the garbage slot carries no information,
#   so the claim is testable by construction --
#
#       exists k :  sensor2 without index k  ==  sensor1[:-1]
#
#   and symmetrically for sensor2.  so just try all n deletions on each side.
#   whichever claim holds alone names the faulty sensor; if both hold the data
#   is ambiguous and if neither holds nothing is provably broken -> -1.
#
# time = O(n^2), space = O(n)
class Solution(object):
    def badSensor(self, sensor1, sensor2):
        ok1 = self._can_drop(sensor2, sensor1)   # sensor1 lost a data point
        ok2 = self._can_drop(sensor1, sensor2)   # sensor2 lost a data point
        if ok1 and not ok2:
            return 1
        if ok2 and not ok1:
            return 2
        return -1

    def _can_drop(self, truth, broken):
        target = broken[:-1]
        for k in range(len(truth)):
            if truth[:k] + truth[k + 1:] == target:
                return True
        return False


# V0-2
# IDEA : SUBSEQUENCE TEST -- GREEDY TWO POINTERS, NO INDEX ARITHMETIC
#
#   deleting one entry from a length-n list is the same statement as "the
#   length n-1 result is a subsequence of the original".  so the whole problem
#   collapses to two greedy scans:
#
#       sensor1 faulty  <=>  sensor1[:-1] is a subsequence of sensor2
#       sensor2 faulty  <=>  sensor2[:-1] is a subsequence of sensor1
#
#   (the dropped-last-slot garbage is excluded on the left of each test, which
#   is why the lengths differ by one and the greedy match is forced.)
#
#   this needs no "first mismatch" index and no shift offsets -- one pointer
#   per list, advance the short one on every match.
#
# time = O(n), space = O(1)
class Solution(object):
    def badSensor(self, sensor1, sensor2):
        ok1 = self._is_subseq(sensor1, sensor2)
        ok2 = self._is_subseq(sensor2, sensor1)
        if ok1 and not ok2:
            return 1
        if ok2 and not ok1:
            return 2
        return -1

    def _is_subseq(self, short, full):
        # is short[:-1] a subsequence of full ?
        i, m = 0, len(short) - 1
        for v in full:
            if i < m and v == short[i]:
                i += 1
        return i == m
