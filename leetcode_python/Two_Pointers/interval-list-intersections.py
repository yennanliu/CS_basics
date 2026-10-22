# https://leetcode.com/problems/interval-list-intersections/description/

"""

986. Interval List Intersections
Medium

You are given two lists of closed intervals, firstList and secondList, where firstList[i] = [starti, endi] and secondList[j] = [startj, endj]. Each list of intervals is pairwise disjoint and in sorted order.

Return the intersection of these two interval lists.

A closed interval [a, b] (with a <= b) denotes the set of real numbers x with a <= x <= b.

The intersection of two closed intervals is a set of real numbers that are either empty or represented as a closed interval. For example, the intersection of [1, 3] and [2, 4] is [2, 3].

 

Example 1:


Input: firstList = [[0,2],[5,10],[13,23],[24,25]], secondList = [[1,5],[8,12],[15,24],[25,26]]
Output: [[1,2],[5,5],[8,10],[15,23],[24,24],[25,25]]
Example 2:

Input: firstList = [[1,3],[5,9]], secondList = []
Output: []
 

Constraints:

0 <= firstList.length, secondList.length <= 1000
firstList.length + secondList.length >= 1
0 <= starti < endi <= 109
endi < starti+1
0 <= startj < endj <= 109 
endj < startj+1

"""


# V0
# IDEA: interval + 2 pointers (gemini)
"""
NOTE !!!


we DON'T need to deal with `prev intervals comparision in this LC
"""
class Solution(object):
    def intervalIntersection(self, firstList, secondList):
        # Edge case
        if not firstList or not secondList:
            return []

        ans = []

        i = 0
        j = 0

        len_f = len(firstList)
        len_s = len(secondList)

        # NOTE !!!
        # while loop: `i < len_f and j < len_s`
        while i < len_f and j < len_s:
            s1, e1 = firstList[i]
            s2, e2 = secondList[j]

            """
            NOTE !!!

            use below logic to check if intervals are overlap

            -> if NOT (not overlap)

            -> e.g. ONLY 2 cases intervals are NOT overlap


            - case 1)

             |---|
                     |----|

            - case 2)

                      |----|
              |---|
            """
            # Check if intervals overlap
            if not (e1 < s2 or s1 > e2):
                ans.append([max(s1, s2), min(e1, e2)])

            """
            NOTE !!!! CRITICAL !!!

            -> ONLY move the idx with the `end earlier` one

            -> by doing so, we can sort of
               compare `next interval with cur one`
               -> so we DON'T need to deal with `prev intervals comparision`


            e.g.


			Input: 

			firstList = [[13,23],[24,25]]
			secondList = [[15,24],[25,26]]

			
			-> Output: [[15,23],[24,24],[25,25]]


			(NOTE why `[24,24],[25,25]` is collected)
            """
            # Move the interval that ends first
            if e1 < e2:
                i += 1
            else:
                j += 1

        return ans


# V0-1
# IDEA: 2 POINTERS + `MOVE SMALLER END IDX INTERVAL` (gemini)
class Solution(object):

  def intervalIntersection(self, firstList, secondList):
    """:type firstList: List[List[int]]

    :type secondList: List[List[int]]
    :rtype: List[List[int]]
    """
    res = []
    i = 0
    j = 0

    while i < len(firstList) and j < len(secondList):
      start_f, end_f = firstList[i]
      start_s, end_s = secondList[j]

      # 1. 計算重疊區間的起點與終點
      # 起點取兩者中較大的（max），終點取兩者中較小的（min）
      start_max = max(start_f, start_s)
      end_min = min(end_f, end_s)


      """
      NOTE !!!

      below trick


      -> check if overalp via checking

         if `start_max <= end_min`

    
      """
      # 2. 判斷是否有實質重疊（起點必須小於等於終點）
      if start_max <= end_min:
        res.append([start_max, end_min])

      # 3. 誰先結束，誰的指標就往前推進（因為結束早的區間不可能再與後續區間相交）
      if end_f < end_s:
        i += 1
      elif end_f > end_s:
        j += 1
      else:
        # 兩者同時結束，雙方指標一起前進
        i += 1
        j += 1

    return res



# V0-2
# IDEA: 2 POINTERS + `MOVE SMALLER END IDX INTERVAL` (gpt)
class Solution(object):
    def intervalIntersection(self, firstList, secondList):
        """
        :type firstList: List[List[int]]
        :type secondList: List[List[int]]
        :rtype: List[List[int]]
        """
        if not firstList or not secondList:
            return []

        res = []

        i = 0
        j = 0

        while i < len(firstList) and j < len(secondList):
            inter_f = firstList[i]
            inter_s = secondList[j]

            # Calculate the intersection
            start = max(inter_f[0], inter_s[0])
            end = min(inter_f[1], inter_s[1])

            # Closed intervals overlap when start <= end
            if start <= end:
                res.append([start, end])

            # Move the pointer whose interval ends first
            if inter_f[1] < inter_s[1]:
                i += 1
            elif inter_f[1] > inter_s[1]:
                j += 1
            else:
                # Both intervals end at the same point
                i += 1
                j += 1

        return res


# V1
# IDEA: interval + 2 pointers (gemini)
class Solution(object):
    def intervalIntersection(self, firstList, secondList):
        """
        :type firstList: List[List[int]]
        :type secondList: List[List[int]]
        :rtype: List[List[int]]
        """
        # 1. FIX: Intersection with an empty list is always empty []
        if not firstList or not secondList:
            return []

        ans = []
        i, j = 0, 0
        len_f, len_s = len(firstList), len(secondList)

        while i < len_f and j < len_s:
            s1, e1 = firstList[i]
            s2, e2 = secondList[j]

            # 2. Check for overlap: max(start) <= min(end)
            start = max(s1, s2)
            end = min(e1, e2)

            if start <= end:
                ans.append([start, end])

            # 3. FIX: Move pointer of interval that ends first to prevent infinite loop
            if e1 < e2:
                i += 1
            else:
                j += 1

        return ans


# V1'
# IDEA: interval + 2 pointers (GPT)
class Solution(object):
    def intervalIntersection(self, firstList, secondList):
        """
        :type firstList: List[List[int]]
        :type secondList: List[List[int]]
        :rtype: List[List[int]]
        """

        # Edge case
        if not firstList or not secondList:
            return []

        ans = []

        i = 0
        j = 0

        len_f = len(firstList)
        len_s = len(secondList)

        while i < len_f and j < len_s:
            s1, e1 = firstList[i]
            s2, e2 = secondList[j]

            # Check if intervals overlap
            if not (e1 < s2 or s1 > e2):
                ans.append([max(s1, s2), min(e1, e2)])

            # Move the interval that ends first
            if e1 < e2:
                i += 1
            else:
                j += 1

        return ans


# V1''
# IDEA: SCAN LINE (gemini)
class Solution(object):
    def intervalIntersection(self, firstList, secondList):
        """
        :type firstList: List[List[int]]
        :type secondList: List[List[int]]
        :rtype: List[List[int]]
        """
        # Event Type Constants:
        # We assign START = -1 so Python sorts START before END when coordinates tie.
        START = -1
        END = 1

        events = []

        # 1. Convert intervals into discrete start/end events
        for s, e in firstList:
            events.append((s, START))
            events.append((e, END))

        for s, e in secondList:
            events.append((s, START))
            events.append((e, END))

        # 2. Sort events chronologically by coordinate x
        # If coordinates tie, -1 (START) comes before 1 (END)
        events.sort()

        ans = []
        active_count = 0
        start_pos = None

        # 3. Sweep across the timeline
        for x, event_type in events:
            if event_type == START:
                active_count += 1
                # When active count hits 2, an overlap begins
                if active_count == 2:
                    start_pos = x
            
            elif event_type == END:
                # If active count was 2, the current overlap ends here
                if active_count == 2:
                    ans.append([start_pos, x])
                active_count -= 1

        return ans


# V1'''
# IDEA: SCAN LINE (gpt)

class Solution(object):
    def intervalIntersection(self, firstList, secondList):
        """
        :type firstList: List[List[int]]
        :type secondList: List[List[int]]
        :rtype: List[List[int]]
        """

        events = []

        # firstList: type = 0
        for s, e in firstList:
            events.append((s, 1, 0))   # start
            events.append((e, -1, 0))  # end

        # secondList: type = 1
        for s, e in secondList:
            events.append((s, 1, 1))   # start
            events.append((e, -1, 1))  # end

        # Sort by position.
        # At the same position, start (+1) comes before end (-1)
        events.sort(key=lambda x: (x[0], -x[1]))

        ans = []

        active_first = 0
        active_second = 0

        intersection_start = None

        for pos, delta, list_type in events:

            # Update active count
            if list_type == 0:
                active_first += delta
            else:
                active_second += delta

            # Both lists are active
            if active_first > 0 and active_second > 0:
                if intersection_start is None:
                    intersection_start = pos

            # Intersection just ended
            else:
                if intersection_start is not None:
                    ans.append([intersection_start, pos])
                    intersection_start = None

        return ans


# V2
