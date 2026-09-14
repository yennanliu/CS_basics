"""

4045. Count Robot Groups
Medium

You are given an integer array position, sorted in increasing order, where
position[i] is the initial position of the ith robot at time t = 0.

You are also given an integer array speed, where speed[i] is the constant
speed of the ith robot in units per second, and an integer distance.

All robots move in the positive direction. Whenever the distance between two
robots or groups becomes at most distance, they merge into a single group.
After a merge, the resulting group takes the current position and speed of
the rightmost robot in that group. Once merged, robots never separate.

Return the number of groups remaining after all possible merges have occurred.


Example 1:

Input: position = [1,5,6,20], speed = [4,3,2,3], distance = 1

Output: 2

Explanation:

Robots at 5 and 6 are already within distance 1 at t = 0, so they merge and
the group takes the rightmost robot's state (position 6, speed 2).
The robot at 1 (speed 4) closes on that group at 2 units/sec and merges at
t = 2. The robot at 20 (speed 3) is faster than the group ahead of it, so the
gap only grows and it stays alone.


Example 2:

Input: position = [0,10], speed = [1,100], distance = 1

Output: 2

Explanation:

The rear robot is slower than the front one, so the gap never shrinks.


Constraints:

1 <= position.length == speed.length <= 10^5

0 <= position[i] <= 10^9

position is sorted in strictly increasing order

1 <= speed[i] <= 10^6

0 <= distance <= 10^9

"""

"""
NOTE !!!

    we DONT NO need `stack` for this LC
"""

"""

LC 4045 VS LC 853


**核心相似性**


兩者本質上都是**一維空間中「追趕與合併」 (Catch-up and Merge)** 的類比問題，都需要將實體由右至左（由右側至左側）排序並掃描，以避免前方實體速度因合併而改變導致順序錯亂。

**LC 853 (Car Fleet) 與 LC 4045 (Count Robot Groups) 的關鍵差異**

| 維度 | LC 853: Car Fleet | LC 4045: Count Robot Groups |
| --- | --- | --- |
| **終點設定** | 有固定終點 `target` [cite: 1.2.1] | 無固定終點，無限向右移動 [cite: 1.1.2] |
| **合併觸發條件** | 後車追上前車（在 `target` 前或剛好在 `target` 追上） [cite: 1.2.1] | 任意時刻兩者距離 $\le \text{distance}$ [cite: 1.1.2] |
| **合併後的屬性** | 車隊速度變為較慢者的速度 [cite: 1.2.1] | 採用該群組中**最右側機器人**的位置與速度 [cite: 1.1.2] |
| **初始狀態處理** | 每個車子各自獨立起步 [cite: 1.2.1] | $t = 0$ 瞬間，初始距離 $\le \text{distance}$ 的相鄰機器人必須先合併 [cite: 1.1.2] |

**關於「一題用 Greedy、一題用 Stack」的迷思**
其實**兩者都可以同時用 Greedy（貪心/單一變數維護）或 Monotonic Stack（單調堆疊）來解**：

* **LC 853** 常被歸類在 Stack，但實際上你只需用一個變數 `prev_time` 記錄前車到達終點的時間（Greedy 貪心掃描），只要後車到達時間 `curr_time <= prev_time`，它就會被合併；大於才獨立成新車隊 [cite: 1.2.1]。Stack 只是把這個概念用 `stack[-1]` 具現化 [cite: 1.2.2]。
* **LC 4045** 先在 $t = 0$ 貪心地將初始貼近的機器人合併（取最右者） [cite: 1.1.2]，接著由右至左掃描時，用單調棧維護速度或追趕關係，本質上也是利用貪心邏輯來決定後方群組會不會追上前方的群組 [cite: 1.1.1]。

[Car Fleet LeetCode 853 Two Approaches Stack and Running Max](https://www.youtube.com/watch?v=c1wgX-HTSuU)

這部影片對比了 Car Fleet (LC 853) 中使用單調棧與貪心滾動最大值的兩種解法，有助於釐清這類追趕合併問題的底層邏輯。

"""



# V0
# IDEA : RIGHT -> LEFT SCAN (CAR FLEET), COLLAPSED TO 2 VARS
#
#   a robot can never pass the one ahead, so it can only ever merge with the
#   entity DIRECTLY ahead of it. walking right -> left, `cur` holds the
#   frontmost group that has SURVIVED so far, as (position, speed) of its
#   rightmost robot.
#
#   robot i is absorbed iff EITHER test fires:
#
#      position[i+1] - position[i] <= distance   -> touching at t = 0
#      speed[i] > cur_s                          -> it is closing on the front
#                                                   group, so the gap
#                                                   gap0 + (cur_s - speed[i])*t
#                                                   crosses `distance` eventually
#
#   NOTE !!! the two tests use DIFFERENT references, and that is the whole
#            trick. a merge adopts the rightmost robot's state, so `cur` never
#            moves when someone joins from behind -- but at t = 0 the merges
#            are SIMULTANEOUS and chain through the neighbour, not through
#            `cur`. testing the t=0 touch against cur_p silently drops those
#            chains:
#
#              pos = [18,19,22,24], gaps 1,3,2, distance = 3
#              -> every consecutive pair touches, so all 4 collapse at t = 0,
#                 even though 18 is 6 away from the group's position 24
#
#   the speed test is safe against `cur` on its own: if robot i+1 left because
#   it was faster than the front (speed[i+1] > cur_s) and robot i is faster
#   still, then speed[i] > cur_s too -- so a robot that catches its neighbour
#   always catches the front group as well.
#
#   e.g. pos = [0,10,20], speed = [5,100,1], distance = 1
#        -> 20 survives (speed 1); 10 is faster (100 > 1) so it merges and the
#           group keeps speed 1; 0 is faster (5 > 1) so it merges too -> 1
#
# time = O(n), space = O(1)
class Solution(object):
    def countGroups(self, position, speed, distance):

        """
        NOTE !!!

            we DONT NO need `stack` for this LC
        """

        # edge
        if not position:
            return 0

        n = len(position)

        # the frontmost robot is always a group on its own
        cur_p, cur_s = position[n - 1], speed[n - 1]
        cnt = 1

        for i in range(n - 2, -1, -1):
            # touching test -> the NEIGHBOUR;  closing test -> the FRONT group
            if position[i + 1] - position[i] <= distance or speed[i] > cur_s:
                continue

            cnt += 1
            cur_p, cur_s = position[i], speed[i]

        return cnt


# V0-1
# IDEA: GREEDY, inverse loop (gpt)
"""
CORE IDEA:
    
    從右往左走，只需要記住右邊最近的 position，
    以及右邊目前 group 的 speed threshold。


---

Steps:

LC 4045
Reverse Greedy

right → left

記：
p2 = 右邊位置
s2 = 右邊 group speed

if p2 - p > distance
   and s <= s2:
       new group
       res += 1
       s2 = s

p2 = p


"""
class Solution(object):
    def countGroups(self, position, speed, distance):
        """
        :type position: List[int]
        :type speed: List[int]
        :type distance: int
        :rtype: int
        """

        """
        NOTE !!!

            we DONT NO need `stack` for this LC
        """


        # edge
        if not position or not speed:
            return 0

        n = len(position)

        if n == 1:
            return 1

        # right neighbor / right group
        p2 = float("inf")
        s2 = float("inf")

        res = 0

        # right -> left
        for i in range(n - 1, -1, -1):
            p = position[i]
            s = speed[i]

            # current car can form a new group
            if p2 - p > distance and s <= s2:
                res += 1
                s2 = s

            p2 = p

        return res
