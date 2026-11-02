"""

332. Reconstruct Itinerary
Hard

You are given a list of airline tickets where tickets[i] = [from_i, to_i] represent the departure and the arrival airports of one flight. Reconstruct the itinerary in order and return it.

All of the tickets belong to a man who departs from "JFK", thus, the itinerary must begin with "JFK". If there are multiple valid itineraries, you should return the itinerary that has the smallest lexical order when read as a single string.

For example, the itinerary ["JFK", "LGA"] has a smaller lexical order than ["JFK", "LGB"].

You may assume all tickets form at least one valid itinerary. You must use all the tickets once and only once.

Example 1:

https://assets.leetcode.com/uploads/2021/03/14/itinerary1-graph.jpg

Input: tickets = [["MUC","LHR"],["JFK","MUC"],["SFO","SJC"],["LHR","SFO"]]
Output: ["JFK","MUC","LHR","SFO","SJC"]

Example 2:

https://assets.leetcode.com/uploads/2021/03/14/itinerary2-graph.jpg

Input: tickets = [["JFK","SFO"],["JFK","ATL"],["SFO","ATL"],["ATL","JFK"],["ATL","SFO"]]
Output: ["JFK","ATL","JFK","SFO","ATL","SFO"]
Explanation: Another possible reconstruction is ["JFK","SFO","ATL","JFK","ATL","SFO"] but it is larger in lexical order.

Constraints:

1 <= tickets.length <= 300
tickets[i].length == 2
from_i.length == 3
to_i.length == 3
from_i and to_i consist of uppercase English letters.
from_i != to_i

"""

# V0
# IDEA : HIERHOLZER (EULERIAN PATH) + MIN-HEAP + POST-ORDER DFS
#
#   Airports are nodes, tickets are directed edges, and "use every ticket
#   exactly once" means the answer is an EULERIAN PATH starting at JFK.
#
#   Greedy "always take the smallest next airport" alone can get stuck in a
#   dead end before every ticket is used. Hierholzer fixes that: an airport is
#   appended to the route only AFTER all of its outgoing tickets are used
#   (post-order). A dead end is therefore appended first, so it ends up LAST
#   once the route is reversed -- exactly where a dead end must go.
#
#   e.g. JFK -> KUL, JFK -> NRT, NRT -> JFK
#        visit KUL first (smaller), stuck -> route = [KUL]
#        back at JFK, take NRT -> JFK (no tickets left) -> route = [KUL, JFK, NRT, JFK]
#        reversed -> [JFK, NRT, JFK, KUL]
#
#   (Brute force -- try every order with backtracking -- is V1; it is
#    exponential in the worst case.)
#
# time = O(E log E), space = O(E), E = number of tickets
import heapq
from collections import defaultdict


class Solution(object):
    def findItinerary(self, tickets):
        """
        :type tickets: List[List[str]]
        :rtype: List[str]
        """
        # airport -> min-heap of destinations still reachable by an unused ticket
        graph = defaultdict(list)
        for src, dst in tickets:
            heapq.heappush(graph[src], dst)

        route = []
        self._visit("JFK", graph, route)

        # route was built in post-order (dead ends first), so flip it
        route.reverse()
        return route

    def _visit(self, airport, graph, route):
        """Use every ticket out of airport (smallest first), then record airport."""
        while graph[airport]:
            # popping the ticket marks it as used
            next_airport = heapq.heappop(graph[airport])
            self._visit(next_airport, graph, route)

        # NOTE !!! append AFTER the loop (post-order), not before it --
        #          that is what puts a dead end at the end of the itinerary
        route.append(airport)


# V0-1
# IDEA : HIERHOLZER, ITERATIVE (EXPLICIT STACK INSTEAD OF RECURSION)
#
#   Same algorithm as V0. The stack holds the current walk; the top airport
#   either still has a ticket (walk forward) or has none left (it is finished,
#   so pop it into the route -- the same post-order append as V0).
#   Use this when the recursion depth could exceed Python's limit.
#
# time = O(E log E), space = O(E)
class Solution2(object):
    def findItinerary(self, tickets):
        """
        :type tickets: List[List[str]]
        :rtype: List[str]
        """
        graph = defaultdict(list)
        for src, dst in tickets:
            heapq.heappush(graph[src], dst)

        route = []
        stack = ["JFK"]
        while stack:
            airport = stack[-1]
            if graph[airport]:
                # still has an unused ticket: take the smallest one
                stack.append(heapq.heappop(graph[airport]))
            else:
                # no tickets left out of here: it is finished
                route.append(stack.pop())

        route.reverse()
        return route


# V1
# IDEA : BACKTRACKING DFS OVER SORTED TICKETS
#
#   Try destinations in lexical order; the first complete path that uses all
#   tickets is the answer, because every earlier choice was the smallest one
#   that could still lead to a full itinerary. On a dead end, undo the ticket
#   and try the next destination.
#   NOTE : the intuitive approach and easy to explain, but exponential in the
#          worst case -- V0 is the one to give in an interview.
#
# time = O(E^d) worst case (d = max out-degree), space = O(E)
class Solution3(object):
    def findItinerary(self, tickets):
        """
        :type tickets: List[List[str]]
        :rtype: List[str]
        """
        # airport -> sorted destinations; used[src][i] marks a used ticket
        graph = defaultdict(list)
        for src, dst in sorted(tickets):
            graph[src].append(dst)
        used = {}
        for src in graph:
            used[src] = [False] * len(graph[src])

        total = len(tickets)
        route = ["JFK"]

        def backtrack(airport):
            """Extend route from airport; return True once every ticket is used."""
            if len(route) == total + 1:
                return True

            previous = None
            for i in range(len(graph[airport])):
                dst = graph[airport][i]
                # skip a used ticket, and a duplicate ticket we just failed with
                if used[airport][i] or dst == previous:
                    continue
                used[airport][i] = True
                route.append(dst)
                if backtrack(dst):
                    return True
                # dead end: undo this ticket and try the next destination
                route.pop()
                used[airport][i] = False
                previous = dst
            return False

        backtrack("JFK")
        return route


# V0-2
# https://blog.csdn.net/fuxuemingzhu/article/details/83551204
# time = O(E log E), E = number of tickets (sorting edges)
# space = O(E)
import collections
class Solution4(object):
    def findItinerary(self, tickets):
        """
        :type tickets: List[List[str]]
        :rtype: List[str]
        """
        graph = collections.defaultdict(list)
        for frm, to in tickets:
            graph[frm].append(to)
        for frm, tos in graph.items():
            tos.sort(reverse=True)
        res = []
        self.dfs(graph, "JFK", res)
        return res[::-1]

    def dfs(self, graph, source, res):
        while graph[source]:
            v = graph[source].pop()
            self.dfs(graph, v, res)
        res.append(source)

# V0-3
# https://www.jiuzhang.com/solution/reconstruct-itinerary/#tag-highlight-lang-python
# time = O(E log E), E = number of tickets (sorting edges)
# space = O(E)
class Solution5(object):
    def findItinerary(self, tickets):
        targets = collections.defaultdict(list)
        for a, b in sorted(tickets)[::-1]:
            targets[a] += b,
        route = []
        
        def dfs(airport):
            while targets[airport]:
                dfs(targets[airport].pop())
            route.append(airport)
            
        dfs('JFK')
        return route[::-1]
        
# V2
# IDEA : BACKTRACKING (valid flag per ticket)
# time = O(t! / (n1! * n2! * ... nk!)), t is the total number of tickets,
#                                       ni is the number of the ticket which from is city i,
#                                       k is the total number of cities.
# space = O(t)
import collections
class Solution6(object):
    def findItinerary(self, tickets):
        """
        :type tickets: List[List[str]]
        :rtype: List[str]
        """
        def route_helper(origin, ticket_cnt, graph, ans):
            if ticket_cnt == 0:
                return True

            for i, (dest, valid)  in enumerate(graph[origin]):
                if valid:
                    graph[origin][i][1] = False
                    ans.append(dest)
                    if route_helper(dest, ticket_cnt - 1, graph, ans):
                        return ans
                    ans.pop()
                    graph[origin][i][1] = True
            return False

        graph = collections.defaultdict(list)
        for ticket in tickets:
            graph[ticket[0]].append([ticket[1], True])
        for k in graph.keys():
            graph[k].sort()

        origin = "JFK"
        ans = [origin]
        route_helper(origin, len(tickets), graph, ans)
        return ans