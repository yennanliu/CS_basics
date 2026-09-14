"""

853. Car Fleet
Medium

There are n cars going to the same destination along a one-lane road. The destination is target miles away.

You are given two integer array position and speed, both of length n, where position[i] is the position of the ith car and speed[i] is the speed of the ith car (in miles per hour).

A car can never pass another car ahead of it, but it can catch up to it and drive bumper to bumper at the same speed. The faster car will slow down to match the slower car's speed. The distance between these two cars is ignored (i.e., they are assumed to have the same position).

A car fleet is some non-empty set of cars driving at the same position and same speed. Note that a single car is also a car fleet.

If a car catches up to a car fleet right at the destination point, it will still be considered as one car fleet.

Return the number of car fleets that will arrive at the destination.

 

Example 1:

Input: target = 12, position = [10,8,0,5,3], speed = [2,4,1,1,3]
Output: 3
Explanation:
The cars starting at 10 (speed 2) and 8 (speed 4) become a fleet, meeting each other at 12.
The car starting at 0 does not catch up to any other car, so it is a fleet by itself.
The cars starting at 5 (speed 1) and 3 (speed 3) become a fleet, meeting each other at 6. The fleet moves at speed 1 until it reaches target.
Note that no other cars meet these fleets before the destination, so the answer is 3.
Example 2:

Input: target = 10, position = [3], speed = [3]
Output: 1
Explanation: There is only one car, hence there is only one fleet.
Example 3:

Input: target = 100, position = [0,2,4], speed = [4,2,1]
Output: 1
Explanation:
The cars starting at 0 (speed 4) and 2 (speed 2) become a fleet, meeting each other at 4. The fleet moves at speed 2.
Then, the fleet (speed 2) and the car starting at 4 (speed 1) become one fleet, meeting each other at 6. The fleet moves at speed 1 until it reaches target.
 

Constraints:

n == position.length == speed.length
1 <= n <= 105
0 < target <= 106
0 <= position[i] < target
All the values of position are unique.
0 < speed[i] <= 106

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
# IDEA : STACK
# Step 1) Ordering by car position (big -> small)
# Step 2) get every car's arrive time
# Step 3) if `new` car travel_time > `prev` cars
#         -> form a NEW FLEET
# Step 4) keep above and return ans
# time = O(n log n)
# space = O(n)
class Solution(object):
    def carFleet(self, target, position, speed):
        pos_speed = []

        for i in range(len(position)):
            t = float(target - position[i]) / speed[i]
            pos_speed.append([position[i], speed[i], t])

        # NOTE !!!
        # V1
        pos_speed.sort(key=lambda x: -x[0])
        # V2
        #pos_speed.sort(key=lambda x: -x[0])

        st = []

        for p, s, t in pos_speed:
            if not st or t > st[-1]:
                st.append(t)

        return len(st)


# V1
# IDEA : STACK 
# Step 1) Ordering by car position
# Step 2) get every car's arrive time
# Step 3) if "left" car uses less time than "right" car -> they SHOULD become a same fleet
# Step 4) keep above and return ans
# 
# https://blog.csdn.net/fuxuemingzhu/article/details/81867361
# time = O(n log n)
# space = O(n)
class Solution:
    def carFleet(self, target, position, speed):
        """
        :type target: int
        :type position: List[int]
        :type speed: List[int]
        :rtype: int
        """
        cars = [(pos, spe) for pos, spe in zip(position, speed)]
        sorted_cars = sorted(cars)
        times = [(target - pos) / spe for pos, spe in sorted_cars]
        stack = []
        for time in times[::-1]:
            if not stack:
                stack.append(time)
            else:
                if time > stack[-1]:
                    stack.append(time)
        return len(stack)


# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/81867361
# time = O(n log n)
# space = O(n)
class Solution:
    def carFleet(self, target, position, speed):
        """
        :type target: int
        :type position: List[int]
        :type speed: List[int]
        :rtype: int
        """
        cars = [(pos, spe) for pos, spe in zip(position, speed)]
        sorted_cars = sorted(cars, reverse=True)
        times = [(target - pos) / spe for pos, spe in sorted_cars]
        stack = []
        for time in times:
            if not stack:
                stack.append(time)
            else:
                if time > stack[-1]:
                    stack.append(time)
        return len(stack)

# V2
# time = O(n log n)
# space = O(n)
class Solution(object):
    def carFleet(self, target, position, speed):
        """
        :type target: int
        :type position: List[int]
        :type speed: List[int]
        :rtype: int
        """
        times = [float(target-p)/s for p, s in sorted(zip(position, speed))]
        result, curr = 0, 0
        for t in reversed(times):
            if t > curr:
                result += 1
                curr = t
        return result
