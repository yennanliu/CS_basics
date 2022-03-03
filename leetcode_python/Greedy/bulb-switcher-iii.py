"""

LeetCode 1375. Bulb Switcher III

There is a room with n bulbs, numbered from 1 to n, arranged in a row from left to right. Initially, all the bulbs are turned off.

At moment k (for k from 0 to n - 1), we turn on the light[k] bulb. A bulb change color to blue only if it is on and all the previous bulbs (to the left) are turned on too.

Return the number of moments in which all turned on bulbs are blue.

Example 1:


Input: light = [2,1,3,5,4]
Output: 3
Explanation: All bulbs turned on, are blue at the moment 1, 2 and 4.
Example 2:

Input: light = [3,2,4,1,5]
Output: 2
Explanation: All bulbs turned on, are blue at the moment 3, and 4 (index-0).
Example 3:

Input: light = [4,1,2,3]
Output: 1
Explanation: All bulbs turned on, are blue at the moment 3 (index-0).
Bulb 4th changes to blue at the moment 3.
Example 4:

Input: light = [2,1,4,3,6,5]
Output: 3
Example 5:

Input: light = [1,2,3,4,5,6]
Output: 6
Constraints:

n == light.length
1 <= n <= 5 * 10^4
light is a permutation of  [1, 2, ..., n]

"""

# V0
# IDEA:
# https://iter01.com/575553.html
# if bulb on idx = k can be changed to blue
#  -> ALL of bulb on the left (idx=k) SHOULD also on
#  -> idx=0 ~ k-1 bulb are ALL on
# so, we can maintain 2 var: max_open, cur_open
# max_open get current open bulb with MAX index
# cur_open records how many bulb are on currently
# and if max_open == cur_open on idx=k
#   -> means all bulb on left are on, so current bulb (idx=k) can become blue
class Solution:
    def numTimesAllBlue(self, light):
        # edge case
        if not light:
            return
        res = 0
        max_open = 0
        cur_open = 0
        for i in range(len(light)):
            max_open = max(max_open, light[i])
            cur_open += 1
            if max_open == cur_open:
                res += 1
        return res

# V0'
class Solution:
    def numTimesAllBlue(self, light):
        max_bulb_ind = 0
        count = 0
        turnedon_bulb = 0
        for bulb in light:
            max_bulb_ind = max(max_bulb_ind,bulb)
            turnedon_bulb += 1
            if turnedon_bulb == max_bulb_ind:
                count += 1     
        return count

# V1
# https://iter01.com/575553.html
class Solution:
    def numTimesAllBlue(self, light):
        max_bulb_ind = 0
        count = 0
        turnedon_bulb = 0
        
        for bulb in light:
            max_bulb_ind = max(max_bulb_ind,bulb)
            turnedon_bulb += 1
            if turnedon_bulb == max_bulb_ind:
                count += 1
        
        return count

# V1''
# https://www.codeleading.com/article/31473623024/
class Solution:
    def numTimesAllBlue(self, light):
        ans=0
        sub_max=-1
        for k in range(len(light)):
            sub_max=max(sub_max,light[k])
            if sub_max==k+1:
                ans=ans+1
        return ans

# V1'''
# https://blog.csdn.net/Wonz5130/article/details/104734212
# https://blog.csdn.net/qq_37821701/article/details/111772365

# V1''''
# https://zxi.mytechroad.com/blog/algorithms/array/leetcode-1375-bulb-switcher-iii/
# C++
# class Solution {
# public:
#   int numTimesAllBlue(vector<int>& light) {    
#     int ans = 0;
#     int right = 0;
#     for (int i = 0; i < light.size(); ++i) {
#       right = max(right, light[i]);
#       ans += right == i + 1;
#     }
#     return ans;
#   }
# };

# V2