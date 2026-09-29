"""

2064. Minimized Maximum of Products Distributed to Any Store
Medium

You are given an integer n indicating there are n specialty retail stores. There are m product types of varying amounts, which are given as a 0-indexed integer array quantities, where quantities[i] represents the number of products of the ith product type.

You need to distribute all products to the retail stores following these rules:

A store can only be given at most one product type but can be given any amount of it.
After distribution, each store will have been given some number of products (possibly 0). Let x represent the maximum number of products given to any store. You want x to be as small as possible, i.e., you want to minimize the maximum number of products that are given to any store.

Return the minimum possible x.


Example 1:

Input: n = 6, quantities = [11,6]
Output: 3
Explanation: One optimal way is:
- The 11 products of type 0 are distributed to the first four stores in these amounts: 2, 3, 3, 3
- The 6 products of type 1 are distributed to the other two stores in these amounts: 3, 3
The maximum number of products given to any store is max(2, 3, 3, 3, 3, 3) = 3.

Example 2:

Input: n = 7, quantities = [15,10,10]
Output: 5
Explanation: One optimal way is:
- The 15 products of type 0 are distributed to the first three stores in these amounts: 5, 5, 5
- The 10 products of type 1 are distributed to the next two stores in these amounts: 5, 5
- The 10 products of type 2 are distributed to the last two stores in these amounts: 5, 5
The maximum number of products given to any store is max(5, 5, 5, 5, 5, 5, 5) = 5.

Example 3:

Input: n = 1, quantities = [100000]
Output: 100000
Explanation: The only optimal way is:
- The 100000 products of type 0 are distributed to the only store.
The maximum number of products given to any store is max(100000) = 100000.


Constraints:

m == quantities.length
1 <= m <= n <= 10^5
1 <= quantities[i] <= 10^5

"""

# V0
class Solution(object):
    def minimizedMaximum(self, n, quantities):
        """
        :type n: int
        :type quantities: List[int]
        :rtype: int
        """
        pass
     

# V0-1
# IDEA: BINARY SEARCH (GPT)
class Solution(object):
    def minimizedMaximum(self, n, quantities):
        """
        :type n: int
        :type quantities: List[int]
        :rtype: int
        """

        # Edge case:
        # Only one store and only one product type.
        if n == 1 and len(quantities) == 1:
            return quantities[0]

        # If there is more than one product type,
        # one store cannot handle multiple product types
        # because each store can only receive products
        # from one type.
        if n == 1 and len(quantities) > 1:
            return -1

        # No products to allocate.
        if not quantities or len(quantities) == 0:
            return 0

        # ---------------------------------------------------------
        # Binary Search
        # ---------------------------------------------------------
        #
        # We are searching for the minimum possible
        # "maximum products per store".
        #
        # Example:
        #
        # quantities = [11, 6]
        #
        # Possible maximum products per store:
        #
        # 1, 2, 3, ..., 11
        #
        # We use binary search instead of checking every value.
        #
        l = 1
        r = max(quantities)

        # Initial answer is the largest possible value.
        ans = r

        while l <= r:
            # Candidate:
            # Assume each store can have at most `mid` products.
            mid = l + (r - l) // 2

            # Check how many stores are required
            # if each store can hold at most `mid` products.
            cnt, max_prod = self.can_allocate(
                mid,
                n,
                quantities
            )

            # If we can distribute all products using
            # <= n stores, `mid` is a valid candidate.
            if cnt <= n:
                # Keep the smallest valid maximum.
                ans = min(ans, max_prod)

                # Try to find an even smaller maximum.
                r = mid - 1

            else:
                # We need more capacity per store.
                # Therefore, increase the candidate.
                l = mid + 1

        return ans

    def can_allocate(self, prod_per_store, n, quantities):
        """
        Check how many stores are required if each store
        can contain at most `prod_per_store` products.

        Returns:
            cnt      = total number of stores required
            max_prod = maximum products in any store
        """

        # Total number of stores required.
        cnt = 0

        # Track the actual maximum products assigned
        # to any single store.
        max_prod = 0

        for prod_cnt in quantities:

            # If this product type has more products
            # than one store can hold, we need multiple stores.
            if prod_cnt > prod_per_store:

                # Number of completely filled stores.
                #
                # Example:
                # prod_cnt = 11
                # prod_per_store = 3
                #
                # 11 // 3 = 3 full stores
                #
                group = prod_cnt // prod_per_store

                # Remaining products after the full stores.
                #
                # 11 % 3 = 2
                #
                remain = prod_cnt % prod_per_store

                # Add the full stores.
                cnt += group

                # If there are remaining products,
                # we need one additional store.
                if remain > 0:
                    cnt += 1

                # Every store can have at most
                # `prod_per_store` products.
                max_prod = max(max_prod, prod_per_store)

            else:
                # This product type fits into one store.
                cnt += 1

                # The store receives all `prod_cnt` products.
                max_prod = max(max_prod, prod_cnt)

        return cnt, max_prod


# V0-2
# IDEA : BINARY SEARCH ON THE ANSWER (minimise the maximum) (claude)
#
#   feasibility is monotone: if a cap of x products per store works, then
#   any larger cap works too. so binary search the smallest feasible x.
#
#   check(x): a product type of size q needs ceil(q / x) stores (one type
#   per store), so the whole plan is feasible iff sum(ceil(q / x)) <= n.
#
#   search range is [1, max(quantities)] since one store can always take a
#   whole type.
#
# time = O(m log(max q)), space = O(1)
class Solution(object):
    def minimizedMaximum(self, n, quantities):
        def check(x):
            need = 0
            for q in quantities:
                need += (q + x - 1) // x
                if need > n:
                    return False
            return True

        lo, hi = 1, max(quantities)
        while lo < hi:
            mid = (lo + hi) // 2
            if check(mid):
                hi = mid
            else:
                lo = mid + 1
        return lo


# V1-1
# IDEA: BINARY SEARCH
# https://leetcode.com/problems/minimized-maximum-of-products-distributed-to-any-store/editorial/
class Solution:
    def can_distribute(self, x: int, quantities: List[int], n: int) -> bool:
        # Pointer to the first not fully distributed product type
        j = 0
        # Remaining quantity of the jth product type
        remaining = quantities[j]

        # Loop through each store
        for i in range(n):
            # Check if the remaining quantity of the jth product type
            # can be fully distributed to the ith store
            if remaining <= x:
                # If yes, move the pointer to the next product type
                j += 1
                # Check if all products have been distributed
                if j == len(quantities):
                    return True
                else:
                    remaining = quantities[j]
            else:
                # Distribute the maximum possible quantity (x) to the ith store
                remaining -= x

        return False

    def minimizedMaximum(self, n: int, quantities: List[int]) -> int:
        # Initialize the boundaries of the binary search
        left = 0
        right = max(quantities)

        # Perform binary search until the boundaries converge
        while left < right:
            middle = (left + right) // 2
            if self.can_distribute(middle, quantities, n):
                # Try for a smaller maximum
                right = middle
            else:
                # Increase the minimum possible maximum
                left = middle + 1

        return left


# V1-1
# IDEA: Greedy Approach Using a Heap (PQ)
# https://leetcode.com/problems/minimized-maximum-of-products-distributed-to-any-store/editorial/
class Solution:
    def minimizedMaximum(self, n, quantities):
        m = len(quantities)

        # Create a list of tuples (-ratio, quantity, stores_assigned)
        type_store_pairs = [(-q, q, 1) for q in quantities]

        # Use heapq.heapify() to convert the list into a heap in O(m) time
        heapq.heapify(type_store_pairs)

        # Iterate over the remaining n - m stores
        for _ in range(n - m):
            # Pop the element with the maximum ratio (due to negative sign it's min-heap)
            (
                neg_ratio,
                total_quantity_of_type,
                stores_assigned_to_type,
            ) = heapq.heappop(type_store_pairs)

            # Calculate the new ratio after assigning one more store
            new_stores_assigned_to_type = stores_assigned_to_type + 1
            new_ratio = total_quantity_of_type / new_stores_assigned_to_type

            # Push the updated pair back into the heap
            heapq.heappush(
                type_store_pairs,
                (
                    -new_ratio,
                    total_quantity_of_type,
                    new_stores_assigned_to_type,
                ),
            )

        # Pop the first element to get the final ratio
        _, total_quantity_of_type, stores_assigned_to_type = heapq.heappop(
            type_store_pairs
        )

        # Return the maximum minimum ratio
        return math.ceil(total_quantity_of_type / stores_assigned_to_type)
