"""

553. Optimal Division
Medium

You are given an integer array nums. The adjacent integers in nums will perform the float division.

For example, for nums = [2,3,4], we will evaluate the expression "2/3/4".
However, you can add any number of parenthesis at any position to change the priority of operations. You want to add these parentheses such the value of the expression after the evaluation is maximum.

Return the corresponding expression that has the maximum value in string format.

Note: your expression should not contain redundant parenthesis.

 

Example 1:

Input: nums = [1000,100,10,2]
Output: "1000/(100/10/2)"
Explanation:
1000/(100/10/2) = 1000/((100/10)/2) = 200
However, the bold parenthesis in "1000/((100/10)/2)" are redundant, since they don't influence the operation priority. So you should return "1000/(100/10/2)".
Other cases:
1000/(100/10)/2 = 50
1000/(100/(10/2)) = 50
1000/100/10/2 = 0.5
1000/100/(10/2) = 2
Example 2:

Input: nums = [2,3,4]
Output: "2/(3/4)"
Example 3:

Input: nums = [2]
Output: "2"
 

Constraints:

1 <= nums.length <= 10
2 <= nums[i] <= 1000
There is only one optimal division for the given iput.

"""

# V0
# IDEA : MATH 
class Solution(object):
    def optimalDivision(self, nums):
        nums = [ str(x) for x in nums ]
        if len(nums) <= 2:
            return '/'.join(nums)
        return str(nums[0]) + "/" + "(" + "/".join( str(i) for i in nums[1:]) + ")"

# V0'
# IDEA : MATH 
class Solution(object):
    def optimalDivision(self, nums):
        nums = [ str(x) for x in nums ]
        if len(nums) <= 2:
            return '/'.join(nums)
        return '{}/({})'.format(nums[0], '/'.join(nums[1:]))

# V0''
class Solution(object):
    def optimalDivision(self, nums):
        """
        :type nums: List[int]
        :rtype: str
        """
        if len(nums) == 1:
            return str(nums[0])
        if len(nums) == 2:
            return str(nums[0]) + "/" + str(nums[1])
        result = [str(nums[0]) + "/(" + str(nums[1])]
        for i in range(2, len(nums)):
            result += "/" + str(nums[i])
        result += ")"
        return "".join(result)

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/79403723
# ideas:
# case 1 : return nums if there is only one number
# case 2 : return nums[0]/nums[1] if there are only 2 numbers 
# case 3 : return nums[0]/(nums[1]*nums[2]*....*nums[N]) if there are N numbers 
class Solution(object):
    def optimalDivision(self, nums):
        """
        :type nums: List[int]
        :rtype: str
        """ 
        nums = list(map(str, nums)) # adapt here for python 3 
        if len(nums) <= 2:
            return '/'.join(nums)
        return '{}/({})'.format(nums[0], '/'.join(nums[1:]))

### Test case :
s=Solution()
assert s.optimalDivision([1000,100,10,2]) == "1000/(100/10/2)"
assert s.optimalDivision([1000]) == "1000"
assert s.optimalDivision([1000, 10]) == "1000/10"
assert s.optimalDivision([]) == ""
assert s.optimalDivision([1000,100, 99,98, 97, 96]) == '1000/(100/99/98/97/96)'
assert s.optimalDivision([ _ for _ in range(50, 40, -1)]) == '50/(49/48/47/46/45/44/43/42/41)'

# V1'
# http://bookshadow.com/weblog/2017/04/16/leetcode-optimal-division/
# IDEA : MATH 
class Solution(object):
    def optimalDivision(self, nums):
        """
        :type nums: List[int]
        :rtype: str
        """
        nums = map(str, nums)
        return len(nums) < 3 and '/'.join(nums) or nums[0] + '/(' + '/'.join(nums[1:]) + ')'

# V1''
# http://bookshadow.com/weblog/2017/04/16/leetcode-optimal-division/
# IDEA : BRUTE FORCE
class Solution(object):
    def optimalDivision(self, nums):
        """
        :type nums: List[int]
        :rtype: str
        """
        def solve(snums):
            if len(snums) == 1: yield snums[0]
            for x in range(1, len(snums)):
                left, right = snums[:x], snums[x:]
                l = '/'.join(left)
                if len(right) == 1:
                    yield l + '/' + right[0]
                else:
                    for r in solve(right):
                        yield l + '/(' + r + ')'
        ans, best = '', 0
        for expr in solve(map(str, nums)):
            val = eval(expr.replace('/', '.0/'))
            if val > best: ans, best = expr, val
        return ans

# V1'''
# https://blog.csdn.net/orientliu96/article/details/104304944
class Solution(object):
    def optimalDivision(self, nums):
        """
        :type nums: List[int]
        :rtype: str
        """
        nums = list(map(str, nums))
        if len(nums) > 2:
            nums[1] = "(" + nums[1]
            nums[-1] = nums[-1] + ")"
        return "/".join(nums)

# V2
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def optimalDivision(self, nums):
        """
        :type nums: List[int]
        :rtype: str
        """
        if len(nums) == 1:
            return str(nums[0])
        if len(nums) == 2:
            return str(nums[0]) + "/" + str(nums[1])
        result = [str(nums[0]) + "/(" + str(nums[1])]
        for i in range(2, len(nums)):
            result += "/" + str(nums[i])
        result += ")"
        return "".join(result)