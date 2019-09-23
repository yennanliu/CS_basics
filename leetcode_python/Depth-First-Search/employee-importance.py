# V0 
"""
# Employee info
class Employee:
    def __init__(self, id, importance, subordinates):
        # It's the unique id of each node.
        # unique id of this employee
        self.id = id
        # the importance value of this employee
        self.importance = importance
        # the id of direct subordinates
        self.subordinates = subordinates
"""
class Solution:
    """
    @param imput: 
    @param id: 
    @return: the total importance value 
    """
    def getImportance(self, employees, id):
        # Write your code here.

        def dfs(id):
            count = G[id][0]
            for sub in G[id][1]:
                count += dfs(sub)
            return count

        G = {}
        for emp in employees:
            G[emp.id] = [emp.importance, emp.subordinates]
        return dfs(id) if id in G else 0

# V1
# https://www.jiuzhang.com/solution/employee-importance/#tag-highlight-lang-python
# IDEA : DFS + PROBLEM GIVE FUNC 
class Solution:
    """
    @param imput: 
    @param id: 
    @return: the total importance value 
    """
    def getImportance(self, employees, id):
        # Write your code here.

        def dfs(id):
            count = G[id][0]
            for sub in G[id][1]:
                count += dfs(sub)
            return count

        G = {}
        for emp in employees:
            G[emp.id] = [emp.importance, emp.subordinates]
        return dfs(id) if id in G else 0

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79091343
"""
# Employee info
class Employee(object):
    def __init__(self, id, importance, subordinates):
        # It's the unique id of each node.
        # unique id of this employee
        self.id = id
        # the importance value of this employee
        self.importance = importance
        # the id of direct subordinates
        self.subordinates = subordinates
"""
class Solution(object):
    def getImportance(self, employees, id):
        """
        :type employees: Employee
        :type id: int
        :rtype: int
        """
        employee_dict = {employee.id : employee for employee in employees}
        def dfs(id):
            return employee_dict[id].importance + sum(dfs(id) for id in employee_dict[id].subordinates)
        return dfs(id)

# V1'' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79091343
class Solution:
    def getImportance(self, employees, id):
        """
        :type employees: Employee
        :type id: int
        :rtype: int
        """
        emap = {employee.id : employee for employee in employees}
        res = emap[id].importance
        for sub in emap[id].subordinates:
            res += self.getImportance(employees, sub)
        return res

# V1''' 
# https://www.jiuzhang.com/solution/employee-importance/#tag-highlight-lang-python
class Solution:
    """
    @param imput: 
    @param id: 
    @return: the total importance value 
    """
    def getImportance(self, employees, id):
        # Write your code here.

        def dfs(id):
            count = G[id][0]
            for sub in G[id][1]:
                count += dfs(sub)
            return count

        G = {}
        for emp in employees:
            G[emp.id] = [emp.importance, emp.subordinates]
        return dfs(id) if id in G else 0

# V2 
# Time:  O(n)
# Space: O(h)
"""
# Employee info
class Employee(object):
    def __init__(self, id, importance, subordinates):
        # It's the unique id of each node.
        # unique id of this employee
        self.id = id
        # the importance value of this employee
        self.importance = importance
        # the id of direct subordinates
        self.subordinates = subordinates
"""
import collections
class Solution(object):
    def getImportance(self, employees, id):
        """
        :type employees: Employee
        :type id: int
        :rtype: int
        """
        if employees[id-1] is None:
            return 0
        result = employees[id-1].importance
        for id in employees[id-1].subordinates:
            result += self.getImportance(employees, id)
        return result


# Time:  O(n)
# Space: O(w), w is the max number of nodes in the levels of the tree
import collections
class Solution2(object):
    def getImportance(self, employees, id):
        """
        :type employees: Employee
        :type id: int
        :rtype: int
        """
        result, q = 0, collections.deque([id])
        while q:
            curr = q.popleft()
            employee = employees[curr-1]
            result += employee.importance
            for id in employee.subordinates:
                q.append(id)
        return result