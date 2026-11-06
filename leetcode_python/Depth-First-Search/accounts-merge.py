"""

721. Accounts Merge
Medium

Given a list of accounts where each element accounts[i] is a list of strings, where the first element accounts[i][0] is a name, and the rest of the elements are emails representing emails of the account.

Now, we would like to merge these accounts. Two accounts definitely belong to the same person if there is some common email to both accounts. Note that even if two accounts have the same name, they may belong to different people as people could have the same name. A person can have any number of accounts initially, but all of their accounts definitely have the same name.

After merging the accounts, return the accounts in the following format: the first element of each account is the name, and the rest of the elements are emails in sorted order. The accounts themselves can be returned in any order.

 

Example 1:

Input: accounts = [["John","johnsmith@mail.com","john_newyork@mail.com"],["John","johnsmith@mail.com","john00@mail.com"],["Mary","mary@mail.com"],["John","johnnybravo@mail.com"]]
Output: [["John","john00@mail.com","john_newyork@mail.com","johnsmith@mail.com"],["Mary","mary@mail.com"],["John","johnnybravo@mail.com"]]
Explanation:
The first and second John's are the same person as they have the common email "johnsmith@mail.com".
The third John and Mary are different people as none of their email addresses are used by other accounts.
We could return these lists in any order, for example the answer [['Mary', 'mary@mail.com'], ['John', 'johnnybravo@mail.com'], 
['John', 'john00@mail.com', 'john_newyork@mail.com', 'johnsmith@mail.com']] would still be accepted.
Example 2:

Input: accounts = [["Gabe","Gabe0@m.co","Gabe3@m.co","Gabe1@m.co"],["Kevin","Kevin3@m.co","Kevin5@m.co","Kevin0@m.co"],["Ethan","Ethan5@m.co","Ethan4@m.co","Ethan0@m.co"],["Hanzo","Hanzo3@m.co","Hanzo1@m.co","Hanzo0@m.co"],["Fern","Fern5@m.co","Fern1@m.co","Fern0@m.co"]]
Output: [["Ethan","Ethan0@m.co","Ethan4@m.co","Ethan5@m.co"],["Gabe","Gabe0@m.co","Gabe1@m.co","Gabe3@m.co"],["Hanzo","Hanzo0@m.co","Hanzo1@m.co","Hanzo3@m.co"],["Kevin","Kevin0@m.co","Kevin3@m.co","Kevin5@m.co"],["Fern","Fern0@m.co","Fern1@m.co","Fern5@m.co"]]
 

Constraints:

1 <= accounts.length <= 1000
2 <= accounts[i].length <= 10
1 <= accounts[i][j].length <= 30
accounts[i][0] consists of English letters.
accounts[i][j] (for j > 0) is a valid email.

"""

# V0
class Solution(object):
    def accountsMerge(self, accounts):
        """
        :type accounts: List[List[str]]
        :rtype: List[List[str]]
        """
        pass



# V0-1
# IDEA: UNION FIND (gpt)
from collections import defaultdict

class UF:
    def __init__(self, n):
        self.parents = list(range(n))
        self.rank = [1] * n

    def get_parent(self, x):
        # Path compression
        if self.parents[x] != x:
            self.parents[x] = self.get_parent(self.parents[x])

        return self.parents[x]

    def union(self, x, y):
        root_x = self.get_parent(x)
        root_y = self.get_parent(y)

        if root_x == root_y:
            return

        # Union by rank
        if self.rank[root_x] < self.rank[root_y]:
            root_x, root_y = root_y, root_x

        self.parents[root_y] = root_x

        if self.rank[root_x] == self.rank[root_y]:
            self.rank[root_x] += 1

    def is_same_parent(self, x, y):
        return self.get_parent(x) == self.get_parent(y)


class Solution(object):
    def accountsMerge(self, accounts):
        """
        :type accounts: List[List[str]]
        :rtype: List[List[str]]
        """
        if not accounts:
            return []

        n = len(accounts)
        uf = UF(n)

        # email -> account index
        email_to_id = {}

        # Step 1: Union accounts sharing the same email
        for i, account in enumerate(accounts):
            for email in account[1:]:
                if email in email_to_id:
                    uf.union(i, email_to_id[email])
                else:
                    email_to_id[email] = i

        # Step 2: Group emails by Union-Find root
        groups = defaultdict(set)

        for email, account_id in email_to_id.items():
            root = uf.get_parent(account_id)
            groups[root].add(email)

        # Step 3: Build the final result
        res = []

        for root, emails in groups.items():
            name = accounts[root][0]
            res.append([name] + sorted(emails))

        return res


# V0-2
# IDEA: UNION FIND (gemini)
from collections import defaultdict

class UF(object):
    def __init__(self, n):
        # 初始化每個節點的父節點為自己
        self.parent = list(range(n))

    def find(self, i):
        # 尋找根節點，並伴隨路徑壓縮 (Path Compression)
        if self.parent[i] == i:
            return i
        self.parent[i] = self.find(self.parent[i])
        return self.parent[i]

    def union(self, i, j):
        # 合併兩個節點所在的集合
        root_i = self.find(i)
        root_j = self.find(j)
        if root_i != root_j:
            self.parent[root_i] = root_j


class Solution(object):
    def accountsMerge(self, accounts):
        """
        :type accounts: List[List[str]]
        :rtype: List[List[str]]
        """
        if not accounts or len(accounts) == 0:
            return []

        n = len(accounts)
        uf = UF(n)
        
        # 記錄 {email: 首次出現的帳戶 index}
        email_to_account_idx = {}
        # 記錄 {email: 名字}，方便最後組裝結果
        email_to_name = {}

        for i, acc in enumerate(accounts):
            name = acc[0]
            for email in acc[1:]:
                email_to_name[email] = name
                if email in email_to_account_idx:
                    # 如果這個 email 之前已經在其他帳戶出現過，將兩個帳戶索引進行聯集
                    uf.union(i, email_to_account_idx[email])
                else:
                    email_to_account_idx[email] = i

        # 將同一群組（相同的 root）的 email 收集起來
        # 格式：{root_index: [email1, email2, ...]}
        root_to_emails = defaultdict(list)
        for email, account_idx in email_to_account_idx.items():
            root_idx = uf.find(account_idx)
            root_to_emails[root_idx].append(email)

        # 組裝最後的輸出結果
        res = []
        for root_idx, emails in root_to_emails.items():
            name = email_to_name[emails[0]]
            # 題目要求 email 必須排序
            res.append([name] + sorted(emails))

        return res


# V0-5
# IDEA : DFS ON THE EMAIL GRAPH (connected components) (CLAUDE)
#
#   Each email is a node. Inside one account, link its first email to every
#   other email (a star is enough to connect them). Two accounts sharing an
#   email now sit in the same connected component, so every component is
#   exactly one person: its emails, sorted, under the name of any account
#   that touched it.
#
#   e.g. John[a, b], John[a, c] -> edges a-b, a-c -> one component {a, b, c}
#
#   The DFS uses an explicit stack: a chain of 1000 accounts each sharing one
#   email with the next is deeper than Python's default recursion limit.
#
# time = O(n log n), space = O(n)   # n = total emails; the sort dominates
from collections import defaultdict


class Solution(object):
    def accountsMerge(self, accounts):
        """
        :type accounts: List[List[str]]
        :rtype: List[List[str]]
        """
        # email -> neighbouring emails (undirected)
        graph = defaultdict(list)
        # email -> owner name (all accounts of one person share a name)
        email_to_name = {}

        for account in accounts:
            name = account[0]
            first_email = account[1]
            email_to_name[first_email] = name
            for email in account[2:]:
                email_to_name[email] = name
                graph[first_email].append(email)
                graph[email].append(first_email)

        visited = set()
        merged = []

        for email in email_to_name:
            if email in visited:
                continue

            # collect the whole component that contains this email
            component = []
            stack = [email]
            # NOTE !!! mark visited when PUSHING, so no email is pushed twice
            visited.add(email)
            while stack:
                current = stack.pop()
                component.append(current)
                for neighbour in graph[current]:
                    if neighbour not in visited:
                        visited.add(neighbour)
                        stack.append(neighbour)

            component.sort()
            merged.append([email_to_name[email]] + component)

        return merged


# V1
# IDEA : UNION FIND (DSU) OVER ACCOUNT INDICES (CLAUDE)
#
#   Each account starts as its own group. Walk every email: the first account
#   to show it owns it; any later account showing the same email is unioned
#   with that owner. Afterwards, every email is filed under the root of the
#   account that owns it, and each root is one merged person.
#
#   Path compression + union by size keep find() near O(1) (inverse Ackermann).
#   Same bound as V0; prefer it when the follow-up adds accounts one at a time,
#   because a DSU absorbs a new edge without re-running a traversal.
#
# time = O(n log n), space = O(n)   # n = total emails; the sort dominates
from collections import defaultdict


class Solution2(object):
    def accountsMerge(self, accounts):
        """
        :type accounts: List[List[str]]
        :rtype: List[List[str]]
        """
        account_count = len(accounts)
        parent = list(range(account_count))
        size = [1] * account_count

        def find(x):
            """Return the root of x's group, compressing the path on the way."""
            root = x
            while parent[root] != root:
                root = parent[root]
            # path compression: point every node on the path straight at root
            while parent[x] != root:
                next_node = parent[x]
                parent[x] = root
                x = next_node
            return root

        def union(a, b):
            """Merge the groups of a and b, hanging the smaller under the larger."""
            root_a = find(a)
            root_b = find(b)
            if root_a == root_b:
                return
            if size[root_a] < size[root_b]:
                root_a, root_b = root_b, root_a
            parent[root_b] = root_a
            size[root_a] += size[root_b]

        # email -> index of the first account it appeared in
        email_owner = {}
        for i in range(account_count):
            for email in accounts[i][1:]:
                if email in email_owner:
                    # NOTE !!! a shared email means the two accounts are one person
                    union(i, email_owner[email])
                else:
                    email_owner[email] = i

        # root account index -> every email in that merged group
        groups = defaultdict(list)
        for email, owner in email_owner.items():
            groups[find(owner)].append(email)

        merged = []
        for root, emails in groups.items():
            emails.sort()
            merged.append([accounts[root][0]] + emails)

        return merged
