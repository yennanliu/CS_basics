# https://leetcode.com/problems/tuple-with-same-product/description/

"""

1726. Tuple with Same Product
Solved
Medium
Topics
premium lock icon
Companies
Hint
Given an array nums of distinct positive integers, return the number of tuples (a, b, c, d) such that a * b = c * d where a, b, c, and d are elements of nums, and a != b != c != d.

 

Example 1:

Input: nums = [2,3,4,6]
Output: 8
Explanation: There are 8 valid tuples:
(2,6,3,4) , (2,6,4,3) , (6,2,3,4) , (6,2,4,3)
(3,4,2,6) , (4,3,2,6) , (3,4,6,2) , (4,3,6,2)
Example 2:

Input: nums = [1,2,4,5,10]
Output: 16
Explanation: There are 16 valid tuples:
(1,10,2,5) , (1,10,5,2) , (10,1,2,5) , (10,1,5,2)
(2,5,1,10) , (2,5,10,1) , (5,2,1,10) , (5,2,10,1)
(2,10,4,5) , (2,10,5,4) , (10,2,4,5) , (10,2,5,4)
(4,5,2,10) , (4,5,10,2) , (5,4,2,10) , (5,4,10,2)
 

Constraints:

1 <= nums.length <= 1000
1 <= nums[i] <= 104
All elements in nums are distinct.
 

"""


# V0
class Solution(object):
    def tupleSameProduct(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        pass


# V1-1
# IDEA: product, math, brute force (gpt)
"""
CORE:

We want

```
a * b = c * d

```


where all four indices are distinct.

So:

1. Compute the product of every pair.


2. Count how many pairs produce the same product.


3. If a product appears m times, then we can choose any two pairs:

   Cm2 (pick 2 elements from m elements)


4. Each choice of two pairs corresponds to 8 tuples because the order matters.
(a,b,c,d)
(a,b,d,c)
(b,a,c,d)
(b,a,d,c)
(c,d,a,b)
(c,d,b,a)
(d,c,a,b)
(d,c,b,a)



-> Hence:

     answer += C(m, 2) * 8

"""
class Solution(object):
    def tupleSameProduct(self, nums):
        # product -> number of pairs
        prod_map = {}

        n = len(nums)

        for i in range(n):
            for j in range(i + 1, n):
                prod = nums[i] * nums[j]
                prod_map[prod] = prod_map.get(prod, 0) + 1

        ans = 0

        for cnt in prod_map.values():
            if cnt >= 2:
                ans += cnt * (cnt - 1) // 2 * 8

        return ans



# V1-2
# IDEA: product, math, brute force (gemini)
class Solution(object):
    def tupleSameProduct(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        # {product: count_of_pairs_with_this_product}
        prod_map = {}
        ans = 0
        n = len(nums)
        
        # Check every possible pair of numbers
        for i in range(n):
            for j in range(i + 1, n):
                
                prod = nums[i] * nums[j]
                
                # If we've seen this product before, we can form valid tuples!
                if prod in prod_map:
                    # The current pair can form 8 tuples with EVERY previous 
                    # pair that shared this exact product.
                    ans += 8 * prod_map[prod]
                    
                # Update the map to include our newly found pair
                prod_map[prod] = prod_map.get(prod, 0) + 1
                
        return ans


# V2-1
# IDEA: Optimized Brute Force
# https://leetcode.com/problems/tuple-with-same-product/editorial/
class Solution:
    def tupleSameProduct(self, nums: List[int]) -> int:
        nums_length = len(nums)
        nums.sort()

        total_number_of_tuples = 0

        # Iterate over all possible values for 'a'
        for a_index in range(nums_length):
            # Iterate over all possible values for 'b', starting from the end
            # of the list
            for b_index in range(nums_length - 1, a_index, -1):
                product = nums[a_index] * nums[b_index]

                # Use a set to store possible values of 'd'
                possible_d_values = set()

                # Iterate over all possible values for 'c' between 'a' and 'b'
                for c_index in range(a_index + 1, b_index):
                    # Check if the product is divisible by nums[c_index]
                    if product % nums[c_index] == 0:
                        d_value = product // nums[c_index]

                        # If 'd_value' is in the set, increment the tuple count
                        # by 8
                        if d_value in possible_d_values:
                            total_number_of_tuples += 8

                        # Add nums[c_index] to the set for future checks
                        possible_d_values.add(nums[c_index])

        return total_number_of_tuples


# V2-2
# IDEA: Count Product Frequency
# https://leetcode.com/problems/tuple-with-same-product/editorial/
class Solution:
    def tupleSameProduct(self, nums):
        nums_length = len(nums)

        pair_products = []

        total_number_of_tuples = 0

        # Iterate over nums to calculate all pairwise products
        for first_index in range(nums_length):
            for second_index in range(first_index + 1, nums_length):
                pair_products.append(nums[first_index] * nums[second_index])

        pair_products.sort()

        last_product_seen = -1
        same_product_count = 0

        # Iterate over pair_products to count how many times each product occurs
        for product_index in range(len(pair_products)):
            if pair_products[product_index] == last_product_seen:
                # Increment the count of same products
                same_product_count += 1
            else:
                # Calculate how many pairs had the previous product value
                pairs_of_equal_product = (
                    (same_product_count - 1) * same_product_count // 2
                )

                total_number_of_tuples += 8 * pairs_of_equal_product

                # Update last_product_seen and reset same_product_count
                last_product_seen = pair_products[product_index]
                same_product_count = 1

        # Handle the last group of products (since the loop ends without adding
        # it)
        pairs_of_equal_product = (
            (same_product_count - 1) * same_product_count // 2
        )
        total_number_of_tuples += 8 * pairs_of_equal_product

        return total_number_of_tuples


# V2-3
# IDEA: Product Frequency Hash Map
# https://leetcode.com/problems/tuple-with-same-product/editorial/
class Solution(object):
    def tupleSameProduct(self, nums):
        nums_length = len(nums)

        # Initialize a dictionary to store the frequency of each product of pairs
        pair_products_frequency = {}

        total_number_of_tuples = 0

        # Iterate through each pair of numbers in `nums`
        for first_index in range(nums_length):
            for second_index in range(first_index + 1, nums_length):
                # Increment the frequency of the product of the current pair
                product_value = nums[first_index] * nums[second_index]
                if product_value in pair_products_frequency:
                    pair_products_frequency[product_value] += 1
                else:
                    pair_products_frequency[product_value] = 1

        # Iterate through each product value and its frequency in the dictionary
        for product_frequency in pair_products_frequency.values():
            # Calculate the number of ways to choose two pairs with the same product
            pairs_of_equal_product = (
                (product_frequency - 1) * product_frequency // 2
            )

            # Add the number of tuples for this product to the total (each pair
            # can form 8 tuples)
            total_number_of_tuples += 8 * pairs_of_equal_product

        return total_number_of_tuples

# V3
