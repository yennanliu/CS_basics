# https://leetcode.com/problems/meeting-scheduler/
# https://leetcode.ca/all/1229.html

"""

1229. Meeting Scheduler
Medium

Given the availability time slots arrays slots1 and slots2 of two people and a
meeting duration duration, return the earliest time slot that works for both of
them and is of duration duration.

If there is no common time slot that satisfies the requirements, return an empty
array.

The format of a time slot is an array of two elements [start, end] representing
an inclusive time range from start to end.

It is guaranteed that no two availability slots of the same person intersect
with each other. That is, for any two time slots [start1, end1] and
[start2, end2] of the same person, either start1 > end2 or start2 > end1.

Example 1:

Input: slots1 = [[10,50],[60,120],[140,210]], slots2 = [[0,15],[60,70]], duration = 8
Output: [60,68]

Example 2:

Input: slots1 = [[10,50],[60,120],[140,210]], slots2 = [[0,15],[60,70]], duration = 12
Output: []

Constraints:

1 <= slots1.length, slots2.length <= 10^4
slots1[i].length, slots2[i].length == 2
slots1[i][0] < slots1[i][1]
slots2[i][0] < slots2[i][1]
0 <= slots1[i][j], slots2[i][j] <= 10^9
1 <= duration <= 10^6

"""

# V0
# IDEA : SORT + TWO POINTERS (interval intersection, like LC 986)
#
#   sort both lists by start. the overlap of slots1[i] and slots2[j] is
#   [max(starts), min(ends)]; if it is >= duration, it is the EARLIEST answer,
#   because both lists are scanned in start order.
#
#   otherwise advance the pointer whose slot ENDS first: that slot cannot
#   overlap anything later in the other list beyond what we already checked,
#   while the slot that ends later still might.
#
#   e.g. [10,50] vs [0,15]  -> overlap [10,15] len 5 < 8, 15 ends first -> j++
#        [10,50] vs [60,70] -> no overlap, 50 ends first -> i++
#        [60,120] vs [60,70] -> overlap [60,70] len 10 >= 8 -> [60,68]
#
# time = O(m log m + n log n), space = O(m + n) (sorted copies)
class Solution(object):
    def minAvailableDuration(self, slots1, slots2, duration):
        """
        :type slots1: List[List[int]]
        :type slots2: List[List[int]]
        :type duration: int
        :rtype: List[int]
        """
        if not slots1 or not slots2:
            return []

        # sorted copies, so the caller's input is not mutated
        sorted1 = sorted(slots1, key=lambda slot: slot[0])
        sorted2 = sorted(slots2, key=lambda slot: slot[0])

        i = 0
        j = 0
        while i < len(sorted1) and j < len(sorted2):
            start1, end1 = sorted1[i]
            start2, end2 = sorted2[j]

            # the common part of the two slots (empty when overlap_start > overlap_end)
            overlap_start = max(start1, start2)
            overlap_end = min(end1, end2)

            if overlap_end - overlap_start >= duration:
                return [overlap_start, overlap_start + duration]

            # NOTE !!! move the slot that ends FIRST -- it is used up,
            #          the other one may still overlap the next slot
            if end1 < end2:
                i += 1
            else:
                j += 1

        return []


# V1
# IDEA : MIN HEAP OVER BOTH PEOPLE'S SLOTS
#
#   drop slots shorter than duration (they can never host the meeting), push
#   everything else into one heap ordered by start. one person's slots never
#   intersect each other, so any two slots that DO overlap must belong to
#   different people -- and the overlapping pair is adjacent in start order.
#   so pop in start order and compare each slot with the next one: if the
#   current slot's end reaches next_start + duration, next_start is the answer.
#
#   NOTE : same bound as V0 and leans on the "no self-intersection" guarantee;
#          V0 is the one to say in an interview.
#
# time = O((m + n) log(m + n)), space = O(m + n)
import heapq


class Solution2(object):
    def minAvailableDuration(self, slots1, slots2, duration):
        """
        :type slots1: List[List[int]]
        :type slots2: List[List[int]]
        :type duration: int
        :rtype: List[int]
        """
        heap = []
        for start, end in slots1 + slots2:
            # a slot shorter than the meeting is useless to either person
            if end - start >= duration:
                heap.append((start, end))
        heapq.heapify(heap)

        while len(heap) > 1:
            start, end = heapq.heappop(heap)
            next_start, next_end = heap[0]
            # the next slot (by start) overlaps this one for >= duration
            if end >= next_start + duration:
                return [next_start, next_start + duration]

        return []
