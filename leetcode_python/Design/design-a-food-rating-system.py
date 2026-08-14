"""

2353. Design a Food Rating System
Medium

Design a food rating system that can do the following:

Modify the rating of a food item listed in the system.
Return the highest-rated food item for a type of cuisine in the system.

Implement the FoodRatings class:

FoodRatings(String[] foods, String[] cuisines, int[] ratings) Initializes the system. The food items are described by foods, cuisines and ratings, all of which have a length of n.
  foods[i] is the name of the ith food,
  cuisines[i] is the type of cuisine of the ith food, and
  ratings[i] is the initial rating of the ith food.
void changeRating(String food, int newRating) Changes the rating of the food item with the name food.
String highestRated(String cuisine) Returns the name of the food item that has the highest rating for the given type of cuisine. If there is a tie, return the item with the lexicographically smaller name.

Note that a string x is lexicographically smaller than string y if x comes before y in dictionary order, that is, either x is a prefix of y, or if i is the first position such that x[i] != y[i], then x[i] comes before y[i] in alphabetic order.


Example 1:

Input
["FoodRatings", "highestRated", "highestRated", "changeRating", "highestRated", "changeRating", "highestRated"]
[[["kimchi", "miso", "sushi", "moussaka", "ramen", "bulgogi"], ["korean", "japanese", "japanese", "greek", "japanese", "korean"], [9, 12, 8, 15, 14, 7]], ["korean"], ["japanese"], ["sushi", 16], ["japanese"], ["ramen", 16], ["japanese"]]
Output
[null, "kimchi", "ramen", null, "sushi", null, "ramen"]

Explanation
FoodRatings foodRatings = new FoodRatings(["kimchi", "miso", "sushi", "moussaka", "ramen", "bulgogi"], ["korean", "japanese", "japanese", "greek", "japanese", "korean"], [9, 12, 8, 15, 14, 7]);
foodRatings.highestRated("korean"); // return "kimchi"
                                   // "kimchi" is the highest rated korean food with a rating of 9.
foodRatings.highestRated("japanese"); // return "ramen"
                                      // "ramen" is the highest rated japanese food with a rating of 14.
foodRatings.changeRating("sushi", 16); // "sushi" now has a rating of 16.
foodRatings.highestRated("japanese"); // return "sushi"
                                      // "sushi" is the highest rated japanese food with a rating of 16.
foodRatings.changeRating("ramen", 16); // "ramen" now has a rating of 16.
foodRatings.highestRated("japanese"); // return "ramen"
                                      // Both "sushi" and "ramen" have a rating of 16.
                                      // However, "ramen" is lexicographically smaller than "sushi".


Constraints:

1 <= n <= 2 * 10^4
n == foods.length == cuisines.length == ratings.length
1 <= foods[i].length, cuisines[i].length <= 10
foods[i], cuisines[i] consist of lowercase English letters.
1 <= ratings[i] <= 10^8
All the strings in foods are distinct.
food will be the name of a food item in the system across all calls to changeRating.
cuisine will be a type of cuisine of at least one food item in the system across all calls to highestRated.
At most 2 * 10^4 calls in total will be made to changeRating and highestRated.

"""

# V0
# IDEA : HASH MAP + MIN HEAP WITH LAZY DELETION (avoid an ordered set)
#
#   heap key = (-rating, food) so the heap top is the highest rating, ties
#   broken by the lexicographically smallest name -- exactly the tie rule.
#
#   changeRating() only records the new rating in g[food] and pushes a fresh
#   entry; the stale entry stays in the heap.
#   highestRated() pops while the top entry disagrees with the current rating
#   stored in g[food] (that entry is stale), then returns the surviving top.
#
#   NOTE : at most one stale entry is created per changeRating call, so the
#          total popping work is bounded by the number of calls.
#
# time = O(n log n) init, O(log n) amortized per call, space = O(n)
import heapq
from collections import defaultdict
class FoodRatings(object):

    def __init__(self, foods, cuisines, ratings):
        self.d = defaultdict(list)   # cuisine -> heap of (-rating, food)
        self.g = {}                  # food -> (rating, cuisine)
        for food, cuisine, rating in zip(foods, cuisines, ratings):
            self.g[food] = (rating, cuisine)
            self.d[cuisine].append((-rating, food))
        for cuisine in self.d:
            heapq.heapify(self.d[cuisine])

    def changeRating(self, food, newRating):
        cuisine = self.g[food][1]
        self.g[food] = (newRating, cuisine)
        heapq.heappush(self.d[cuisine], (-newRating, food))

    def highestRated(self, cuisine):
        h = self.d[cuisine]
        while h and self.g[h[0][1]][0] != -h[0][0]:
            heapq.heappop(h)
        return h[0][1]


# Your FoodRatings object will be instantiated and called as such:
# obj = FoodRatings(foods, cuisines, ratings)
# obj.changeRating(food,newRating)
# param_2 = obj.highestRated(cuisine)
