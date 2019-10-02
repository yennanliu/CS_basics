# V0 

# V1 
# http://bookshadow.com/weblog/2017/11/05/leetcode-accounts-merge/
# https://blog.csdn.net/fuxuemingzhu/article/details/82913712
import collections
class Solution(object):
    def accountsMerge(self, accounts):
        """
        :type accounts: List[List[str]]
        :rtype: List[List[str]]
        """
        eset = set()
        owner = {}
        parent = {}
        result = collections.defaultdict(list)
        def findParent(e):
            rank = 0
            while parent[e] != e:
                rank += 1
                e = parent[e]
            return e, rank
        def merge(a, b):
            pa, ra = findParent(a)
            pb, rb = findParent(b)
            if ra < rb: parent[pa] = pb
            else: parent[pb] = pa
        for account in accounts:
            name, emails = account[0], account[1:]
            for email in emails:
                eset.add(email)
                owner[email] = name
                if email not in parent: parent[email] = email
            for email in emails[1:]:
                merge(emails[0], email)
        for email in eset:
            result[findParent(email)[0]].append(email)
        return [[owner[k]] + sorted(v) for k, v in result.iteritems()]

# V1'
# https://www.jiuzhang.com/solution/accounts-merge/#tag-highlight-lang-python
class Solution:
    """
    @param accounts: List[List[str]]
    @return: return a List[List[str]]
    """
    def accountsMerge(self, accounts):
        self.initialize(len(accounts))
        email_to_ids = self.get_email_to_ids(accounts)
        
        # union
        for email, ids in email_to_ids.items():
            root_id = ids[0]
            for id in ids[1:]:
                self.union(id, root_id)
                
        id_to_email_set = self.get_id_to_email_set(accounts)
        
        merged_accounts = []
        for user_id, email_set in id_to_email_set.items():
            merged_accounts.append([
                accounts[user_id][0],
                *sorted(email_set),
            ])
        return merged_accounts
    
    def get_id_to_email_set(self, accounts):
        id_to_email_set = {}
        for user_id, account in enumerate(accounts):
            root_user_id = self.find(user_id)
            email_set = id_to_email_set.get(root_user_id, set())
            for email in account[1:]:
                email_set.add(email)
            id_to_email_set[root_user_id] = email_set
        return id_to_email_set
            
    def get_email_to_ids(self, accounts):
        email_to_ids = {}
        for i, account in enumerate(accounts):
            for email in account[1:]:
                email_to_ids[email] = email_to_ids.get(email, [])
                email_to_ids[email].append(i)
        return email_to_ids
        
    def initialize(self, n):
        self.father = {}
        for i in range(n):
            self.father[i] = i
            
    def union(self, id1, id2):
        self.father[self.find(id1)] = self.find(id2)

    def find(self, user_id):
        path = []
        while user_id != self.father[user_id]:
            path.append(user_id)
            user_id = self.father[user_id]
            
        for u in path:
            self.father[u] = user_id
            
        return user_id

# V2 
# Time:  O(nlogn), n is the number of total emails,
#                  and the max length ofemail is 320, p.s. {64}@{255}
# Space: O(n)
import collections
class UnionFind(object):
    def __init__(self):
        self.set = []

    def get_id(self):
        self.set.append(len(self.set))
        return len(self.set)-1

    def find_set(self, x):
        if self.set[x] != x:
            self.set[x] = self.find_set(self.set[x])  # path compression.
        return self.set[x]

    def union_set(self, x, y):
        x_root, y_root = map(self.find_set, (x, y))
        if x_root != y_root:
            self.set[min(x_root, y_root)] = max(x_root, y_root)

class Solution(object):
    def accountsMerge(self, accounts):
        """
        :type accounts: List[List[str]]
        :rtype: List[List[str]]
        """
        union_find = UnionFind()
        email_to_name = {}
        email_to_id = {}
        for account in accounts:
            name = account[0]
            for i in range(1, len(account)):
                if account[i] not in email_to_id:
                    email_to_name[account[i]] = name
                    email_to_id[account[i]] = union_find.get_id()
                union_find.union_set(email_to_id[account[1]],
                                     email_to_id[account[i]])

        result = collections.defaultdict(list)
        for email in email_to_name.keys():
            result[union_find.find_set(email_to_id[email])].append(email)
        for emails in result.values():
            emails.sort()
        return [[email_to_name[emails[0]]] + emails
                for emails in result.values()]