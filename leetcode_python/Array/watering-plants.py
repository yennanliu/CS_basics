"""

2079. Watering Plants
Medium

You want to water n plants in your garden with a watering can. The plants are
arranged in a row and are labeled from 0 to n - 1 from left to right where the
ith plant is located at x = i. There is a river at x = -1 that you can refill
your watering can at.

Each plant needs a specific amount of water. You will water the plants in the
following way:

- Water the plants in order from left to right.
- After watering the current plant, if you do not have enough water to
  completely water the next plant, return to the river to fully refill the
  watering can.
- You cannot refill the watering can early.

You are initially at the river (i.e., x = -1). It takes one step to move one
unit on the x-axis.

Given a 0-indexed integer array plants of n integers, where plants[i] is the
amount of water the ith plant needs, and an integer capacity representing the
watering can capacity, return the number of steps needed to water all the
plants.

Example 1:

Input: plants = [2,2,3,3], capacity = 5

Output: 14

Explanation: Start at the river with a full watering can:
- Walk to plant 0 (1 step) and water it. Watering can has 3 units of water.
- Walk to plant 1 (1 step) and water it. Watering can has 1 unit of water.
- Since you cannot completely water plant 2, walk back to the river to refill (2 steps).
- Walk to plant 2 (3 steps) and water it. Watering can has 2 units of water.
- Since you cannot completely water plant 3, walk back to the river to refill (3 steps).
- Walk to plant 3 (4 steps) and water it.
Steps needed = 1 + 1 + 2 + 3 + 3 + 4 = 14.

Example 2:

Input: plants = [1,1,1,4,2,3], capacity = 4

Output: 30

Explanation: Start at the river with a full watering can:
- Water plants 0, 1, and 2 (3 steps). Return to river (3 steps).
- Water plant 3 (4 steps). Return to river (4 steps).
- Water plant 4 (5 steps). Return to river (5 steps).
- Water plant 5 (6 steps).
Steps needed = 3 + 3 + 4 + 4 + 5 + 5 + 6 = 30.

Example 3:

Input: plants = [7,7,7,7,7,7,7], capacity = 8

Output: 49

Explanation: You have to refill before watering each plant.
Steps needed = 1 + 1 + 2 + 2 + 3 + 3 + 4 + 4 + 5 + 5 + 6 + 6 + 7 = 49.

Constraints:

n == plants.length
1 <= n <= 1000
1 <= plants[i] <= 10^6
max(plants[i]) <= capacity <= 10^9

"""

# V0
# IDEA : ARRAY + SIMULATION (REFILL TRIP = 2 * i + 1 STEPS) (claude)
#
#   We stand at plant i - 1 (x = i - 1) about to water plant i.
#   - enough water   -> 1 step forward
#   - not enough     -> walk back to the river (i - 1 -> -1 = i steps),
#                       then out to plant i (-1 -> i = i + 1 steps)
#                       => 2 * i + 1 steps, and the can is full again
#   Refilling is never optional, so the greedy walk IS the only walk.
#
#   e.g. plants = [2,2,3,3], cap = 5
#        i=0: 1 (left 3) | i=1: 1 (left 1) | i=2: 2*2+1=5 (left 2)
#        i=3: 2*3+1=7 (left 2) -> 1 + 1 + 5 + 7 = 14
#
# time = O(n), space = O(1)
class Solution(object):
    def wateringPlants(self, plants, capacity):
        """
        :type plants: List[int]
        :type capacity: int
        :rtype: int
        """
        steps = 0
        water_left = capacity

        for i in range(len(plants)):
            need = plants[i]

            if water_left >= need:
                # enough water: one step from plant i - 1 to plant i
                steps += 1
            else:
                # NOTE !!! back to the river is i steps, out to plant i is
                #          i + 1 steps -> 2 * i + 1, then the can is full
                steps += 2 * i + 1
                water_left = capacity

            water_left -= need

        return steps


# V0-1
# IDEA: MATH + problem understanding (GPT)
class Solution(object):
    def wateringPlants(self, plants, capacity):
        if not plants:
            return 0

        steps = 0
        water = capacity
        last_idx = -1

        for i, need in enumerate(plants):
            if water >= need:
                # Walk from last position to plant i
                steps += i - last_idx
            else:
                # Walk back to river, then from river to plant i
                steps += (last_idx + 1) + (i + 1)
                water = capacity

            water -= need
            last_idx = i

        return steps


# V0-2
# IDEA: MATH + problem understanding (gemini)
class Solution(object):

  def wateringPlants(self, plants, capacity):
    """:type plants: List[int]

    :type capacity: int
    :rtype: int
    """
    steps = 0
    curr_water = capacity

    for i in range(len(plants)):
      # 如果當前水量夠澆第 i 株植物
      if curr_water >= plants[i]:
        curr_water -= plants[i]
        steps += 1
      else:
        # 水不夠：走回河邊 (i 步) + 走回第 i 株植物 (i + 1 步) = 2 * i + 1 步
        steps += 2 * i + 1
        curr_water = capacity - plants[i]

    return steps


# V0-3
# IDEA : ARRAY + SIMULATION (COUNT FORWARD STEPS ONCE, ADD ROUND TRIPS) (claude)
#
#   Every plant costs exactly one forward step no matter what, so the
#   forward walk is always n steps in total. A refill before plant i only
#   ADDS a round trip from plant i - 1 to the river and back: 2 * i steps.
#
#   answer = n + sum(2 * i for every i where we had to refill)
#
#   Same algorithm as V0; this accounting is the one-line formula to say out
#   loud when an interviewer asks "can you write the answer in closed form?"
#
# time = O(n), space = O(1)
class Solution2(object):
    def wateringPlants(self, plants, capacity):
        """
        :type plants: List[int]
        :type capacity: int
        :rtype: int
        """
        # one forward step per plant, always
        steps = len(plants)
        water_left = capacity

        for i in range(len(plants)):
            if water_left < plants[i]:
                # round trip: plant i - 1 -> river -> plant i - 1
                steps += 2 * i
                water_left = capacity
            water_left -= plants[i]

        return steps
