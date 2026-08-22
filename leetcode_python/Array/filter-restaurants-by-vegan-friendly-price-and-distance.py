"""

1333. Filter Restaurants by Vegan-Friendly, Price and Distance
Medium

Given the array restaurants where
restaurants[i] = [id_i, rating_i, veganFriendly_i, price_i, distance_i].
You have to filter the restaurants using three filters.

The veganFriendly filter will be either true (meaning you should only include
restaurants with veganFriendly_i set to true) or false (meaning you can include
any restaurant). In addition, you have the filters maxPrice and maxDistance
which are the maximum value for price and distance of restaurants you should
consider respectively.

Return the array of restaurant IDs after filtering, ordered by rating from
highest to lowest. For restaurants with the same rating, order them by id from
highest to lowest. For simplicity veganFriendly_i and veganFriendly take
value 1 when it is true, and 0 when it is false.


Example 1:

Input: restaurants = [[1,4,1,40,10],[2,8,0,50,5],[3,8,1,30,4],[4,10,0,10,3],[5,1,1,15,1]], veganFriendly = 1, maxPrice = 50, maxDistance = 10
Output: [3,1,5]
Explanation:
The restaurants are:
Restaurant 1 [id=1, rating=4, veganFriendly=1, price=40, distance=10]
Restaurant 2 [id=2, rating=8, veganFriendly=0, price=50, distance=5]
Restaurant 3 [id=3, rating=8, veganFriendly=1, price=30, distance=4]
Restaurant 4 [id=4, rating=10, veganFriendly=0, price=10, distance=3]
Restaurant 5 [id=5, rating=1, veganFriendly=1, price=15, distance=1]
After filter restaurants with veganFriendly = 1, maxPrice = 50 and
maxDistance = 10 we have restaurant 3, restaurant 1 and restaurant 5
(ordered by rating from highest to lowest).

Example 2:

Input: restaurants = [[1,4,1,40,10],[2,8,0,50,5],[3,8,1,30,4],[4,10,0,10,3],[5,1,1,15,1]], veganFriendly = 0, maxPrice = 50, maxDistance = 10
Output: [4,3,2,1,5]
Explanation: The restaurants are the same as in example 1, but in this case
the filter veganFriendly = 0, therefore all restaurants are considered.

Example 3:

Input: restaurants = [[1,4,1,40,10],[2,8,0,50,5],[3,8,1,30,4],[4,10,0,10,3],[5,1,1,15,1]], veganFriendly = 0, maxPrice = 30, maxDistance = 3
Output: [4,5]


Constraints:

1 <= restaurants.length <= 10^4
restaurants[i].length == 5
1 <= id_i, rating_i, price_i, distance_i <= 10^5
1 <= maxPrice, maxDistance <= 10^5
veganFriendly_i and veganFriendly are 0 or 1.
All id_i are distinct.

"""

# V0
# IDEA: FILTER + SORT (by (-rating, -id))
#
#  NOTE:
#   - `vegan >= veganFriendly` is the trick:
#       veganFriendly == 0 -> always true (keep everything)
#       veganFriendly == 1 -> only keep vegan == 1
#
# time = O(n log n)
# space = O(n)
class Solution(object):
    def filterRestaurants(self, restaurants, veganFriendly, maxPrice, maxDistance):
        res = []
        for _id, rating, vegan, price, dist in restaurants:
            if vegan >= veganFriendly and price <= maxPrice and dist <= maxDistance:
                res.append((rating, _id))

        # sort by rating desc, then id desc
        res.sort(key=lambda x: (-x[0], -x[1]))
        return [_id for _, _id in res]


# V0-1
# IDEA : MAX-HEAP -- HEAPIFY THE SURVIVORS, THEN DRAIN THEM IN ORDER
#
#   the ranking key is (rating desc, id desc), so negating both fields turns it
#   into a min-heap key and heapq can do the ordering.  building the heap is
#   O(k) and each pop is O(log k), so the total is the same O(k log k) as a
#   sort -- the reason to reach for it here is that it generalises: if the
#   follow-up only wants the top few, stop popping early and the log factor is
#   paid only for those.
#
# time = O(n + k log k), space = O(k)   (k = number of survivors)
import heapq

class Solution(object):
    def filterRestaurants(self, restaurants, veganFriendly, maxPrice, maxDistance):
        h = []
        for _id, rating, vegan, price, dist in restaurants:
            if veganFriendly and not vegan:
                continue
            if price <= maxPrice and dist <= maxDistance:
                h.append((-rating, -_id))

        heapq.heapify(h)
        res = []
        while h:
            _, neg_id = heapq.heappop(h)
            res.append(-neg_id)
        return res


# V0-2
# IDEA : BUCKET / COUNTING SORT -- BOTH KEYS ARE BOUNDED, SO NO COMPARISONS
#
#   ratings and ids are both <= 10^5 and the ids are distinct, so the ordering
#   can be produced by two counting passes instead of a comparison sort:
#
#     pass 1 : park each survivor's rating in slot rate_of[id] -- one slot per
#              id, which IS the sort by id (distinctness makes it collision
#              free, no counts or offsets needed).
#     pass 2 : walk ids from high to low and drop each into the bucket of its
#              rating.  because the feed is already descending by id, every
#              bucket comes out descending by id for free.
#
#   concatenating the buckets from the highest rating down then gives exactly
#   (rating desc, id desc).  this is the stable-radix trick: sort by the minor
#   key first, then bucket by the major key.
#
# time = O(n + A + R), space = O(n + A + R)   (A = max id, R = max rating)
class Solution(object):
    def filterRestaurants(self, restaurants, veganFriendly, maxPrice, maxDistance):
        keep = []
        for _id, rating, vegan, price, dist in restaurants:
            if veganFriendly and not vegan:
                continue
            if price <= maxPrice and dist <= maxDistance:
                keep.append((_id, rating))
        if not keep:
            return []

        max_id = max(x[0] for x in keep)
        max_rate = max(x[1] for x in keep)

        rate_of = [-1] * (max_id + 1)
        for _id, rating in keep:
            rate_of[_id] = rating

        buckets = [[] for _ in range(max_rate + 1)]
        for _id in range(max_id, -1, -1):
            r = rate_of[_id]
            if r >= 0:
                buckets[r].append(_id)

        res = []
        for r in range(max_rate, -1, -1):
            res.extend(buckets[r])
        return res
