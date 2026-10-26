# https://leetcode.com/problems/satisfiability-of-equality-equations/description/

"""

990. Satisfiability of Equality Equations
Medium

You are given an array of strings equations that represent relationships between variables where each string equations[i] is of length 4 and takes one of two different forms: "xi==yi" or "xi!=yi".Here, xi and yi are lowercase letters (not necessarily different) that represent one-letter variable names.

Return true if it is possible to assign integers to variable names so as to satisfy all the given equations, or false otherwise.

 

Example 1:

Input: equations = ["a==b","b!=a"]
Output: false
Explanation: If we assign say, a = 1 and b = 1, then the first equation is satisfied, but not the second.
There is no way to assign the variables to satisfy both equations.
Example 2:

Input: equations = ["b==a","a==b"]
Output: true
Explanation: We could assign a = 1 and b = 1 to satisfy both equations.
 

Constraints:

1 <= equations.length <= 500
equations[i].length == 4
equations[i][0] is a lowercase letter.
equations[i][1] is either '=' or '!'.
equations[i][2] is '='.
equations[i][3] is a lowercase letter.

"""


# V0
# IDEA: DFS (gpt)
# time = O(n)  # n = len(equations); graph bounded by 26 variable nodes
# space = O(n)
class Solution(object):
    def equationsPossible(self, equations):

        # adjacency list for ==
        same_group = {}

        # initialize every variable
        for eq in equations:
            a = eq[0]
            b = eq[3]

            """
            NOTE !!!

            we need to build graph
            with bi-direction

            -> 

            1. otherwise, `same_group[x]` will raises KeyError.
            	(if x is NOT in key in graph)


            2. check below, 
            	self.helper(a, b, same_group, visited) or self.helper(b, a, same_group, visited)
            	is WRONG


            """
            if a not in same_group:
                same_group[a] = []

            if b not in same_group:
                same_group[b] = []

        # build graph
        for eq in equations:
            a = eq[0]
            b = eq[3]

            if eq[1:3] == "==":
                same_group[a].append(b)
                same_group[b].append(a)

        # verify all != equations
        for eq in equations:
            a = eq[0]
            b = eq[3]

            if eq[1:3] == "!=":
                visited = set()

                """
                NOTE !!!


                we DON'T need below:

				```
                if y in same_group[x]:
            		return False
            	```

            	->

            	since the dfs (this helper func)
            	already natually does it.
            	
                """



                """
	            NOTE !!!

	            CAN'T only build graph in `single` direction,
	            and call recursion in with `swap` node 


	            e.g.
	            
	            -> below is WRONG !!!

	            if ( not self.helper(a, b, same_group, visited)
	              or not self.helper(b, a, same_group, visited) ):
	              	return False



	            -> 

	            No. Calling the DFS twice with swapped arguments 
	                is NOT equivalent to making the graph bidirectional.


				given the relations:
				
				```
				a==b
				b==c
				```



				If you only build the graph in one direction:


				```
				a → b → c
				```

				
				->

				Case 1: helper(a, c)
					-> so it returns True.

				
				Case 2: helper(c, a)

					-> there are no outgoing edges, so it returns False.


				-> final result is False

				-> but it should be `True`, so above idea is wrong.


                """
                # if connected -> contradiction
                if self.helper(a, b, same_group, visited):
                    return False

        return True

    def helper(self, cur, target, graph, visited):

        if cur == target:
            return True

        if cur in visited:
            return False

        visited.add(cur)

        for nxt in graph[cur]:
            if self.helper(nxt, target, graph, visited):
                return True

        return False




# V0-1
# IDEA: DFS (gemini)
# time = O(n)  # n = len(equations); graph bounded by 26 variable nodes
# space = O(n)
class Solution(object):
    def equationsPossible(self, equations):
        """
        :type equations: List[str]
        :rtype: bool
        """
        # Graph map to store undirected '==' relationships
        same_group = {}
        
        # Step 1: Build the graph using only '==' equations
        for eq in equations:
            # Every equation is exactly 4 chars long: e.g., "a==b"
            a = eq[0]
            b = eq[3]
            
            if eq[1:3] == "==":
                if a not in same_group: same_group[a] = []
                if b not in same_group: same_group[b] = []
                
                # Relations are mutual (undirected graph edges)
                same_group[a].append(b)
                same_group[b].append(a)

        # Step 2: Validate the contradiction rules using '!=' equations
        for eq in equations:
            a = eq[0]
            b = eq[3]
            
            if eq[1:3] == "!=":
                # Self-contradiction safeguard (e.g., "a!=a")
                if a == b:
                    return False
                
                # Check if 'a' can reach 'b' through the '==' graph bridges
                visited = set()
                if self.can_reach(a, b, same_group, visited):
                    # Contradiction! Graph says they are equal, but equation says "!="
                    return False
                    
        return True

    def can_reach(self, current, target, same_group, visited):
        if current == target:
            return True
            
        visited.add(current)
        
        # If current node has no connected neighbors, it can't reach anything
        if current not in same_group:
            return False
            
        for neighbor in same_group[current]:
            if neighbor not in visited:
                if self.can_reach(neighbor, target, same_group, visited):
                    return True
                    
        return False

# V1
# IDEA: UNION FIND
#   1. every "a==b" merges a and b into the same group
#   2. every "a!=b" then fails if a and b ended up in the same group
#   -> "==" must be processed BEFORE "!=", since a later "==" can
#      connect two variables that an earlier "!=" separated
# time = O(n * α(26))  # n = len(equations); at most 26 variables, path compression
# space = O(1)  # parent array is fixed at 26 letters
class Solution(object):
    def equationsPossible(self, equations):
        """
        :type equations: List[str]
        :rtype: bool
        """
        # parent[i] is the parent of letter chr(ord('a') + i);
        # every letter starts as its own group
        parent = list(range(26))

        # step 1: union every "==" pair
        for eq in equations:
            if eq[1] == '=':
                x = ord(eq[0]) - ord('a')
                y = ord(eq[3]) - ord('a')
                self.union(parent, x, y)

        # step 2: a "!=" pair in the same group is a contradiction
        # (this also covers "a!=a", since a letter is always in its own group)
        for eq in equations:
            if eq[1] == '!':
                x = ord(eq[0]) - ord('a')
                y = ord(eq[3]) - ord('a')
                if self.find(parent, x) == self.find(parent, y):
                    return False

        return True

    def find(self, parent, x):
        # walk up to the root
        root = x
        while parent[root] != root:
            root = parent[root]

        # path compression: point every node on the path straight at the root
        while parent[x] != root:
            next_node = parent[x]
            parent[x] = root
            x = next_node

        return root

    def union(self, parent, x, y):
        root_x = self.find(parent, x)
        root_y = self.find(parent, y)
        if root_x != root_y:
            parent[root_x] = root_y


# V2
# IDEA: HASHMAP
# https://leetcode.com/problems/satisfiability-of-equality-equations/solutions/2625908/on-solution-using-hashmap-by-namanxk-2dkh/
# time = O(n * α(n))  # n = len(equations); union-find with path compression
# space = O(n)
class Solution:
    def equationsPossible(self, equations: List[str]) -> bool:
        unionFind = {}

        def find(x):
            unionFind.setdefault(x, x)
            if x != unionFind[x]:
                unionFind[x] = find(unionFind[x])
            return unionFind[x]

        def union(x, y):
            unionFind[find(x)] = find(y)
            
        for e in equations:
            if e[1] == '=':
                union(e[0], e[-1])
        for e in equations:
            if e[1] == '!':
                if find(e[0]) == find(e[-1]):
                    return False
        return True      

