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
1 <= accounts[i][j] <= 30
accounts[i][0] consists of English letters.
accounts[i][j] (for j > 0) is a valid email.

"""

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

# V1
# IDEA : Depth First Search (DFS)
# https://leetcode.com/problems/accounts-merge/solution/
# JAVA
# class Solution {
#     HashSet<String> visited = new HashSet<>();
#     Map<String, List<String>> adjacent = new HashMap<String, List<String>>();
#   
#     private void DFS(List<String> mergedAccount, String email) {
#         visited.add(email);
#         // Add the email vector that contains the current component's emails
#         mergedAccount.add(email);
#        
#         if (!adjacent.containsKey(email)) {
#             return;
#         }
#       
#         for (String neighbor : adjacent.get(email)) {
#             if (!visited.contains(neighbor)) {
#                 DFS(mergedAccount, neighbor);
#             }
#         }
#     }
#   
#     public List<List<String>> accountsMerge(List<List<String>> accountList) {
#         int accountListSize = accountList.size();
#        
#         for (List<String> account : accountList) {
#             int accountSize = account.size();
#            
#             // Building adjacency list
#             // Adding edge between first email to all other emails in the account
#             String accountFirstEmail = account.get(1);
#             for (int j = 2; j < accountSize; j++) {
#                 String accountEmail = account.get(j);
#                
#                 if (!adjacent.containsKey(accountFirstEmail)) {
#                     adjacent.put(accountFirstEmail, new ArrayList<String>());
#                 }
#                 adjacent.get(accountFirstEmail).add(accountEmail);
#               
#                 if (!adjacent.containsKey(accountEmail)) {
#                     adjacent.put(accountEmail, new ArrayList<String>());
#                 }
#                 adjacent.get(accountEmail).add(accountFirstEmail);
#             }
#         }
#       
#         // Traverse over all th accounts to store components
#         List<List<String>> mergedAccounts = new ArrayList<>();
#         for (List<String> account : accountList) {
#             String accountName = account.get(0);
#             String accountFirstEmail = account.get(1);
#           
#             // If email is visited, then it's a part of different component
#             // Hence perform DFS only if email is not visited yet
#             if (!visited.contains(accountFirstEmail)) {
#                 List<String> mergedAccount = new ArrayList<>();
#                 // Adding account name at the 0th index
#                 mergedAccount.add(accountName);
#               
#                 DFS(mergedAccount, accountFirstEmail);
#                 Collections.sort(mergedAccount.subList(1, mergedAccount.size())); 
#                 mergedAccounts.add(mergedAccount);
#             }
#         }
#       
#         return mergedAccounts;
#     }
# }

# V1
# IDEA : Disjoint Set Union (DSU)
# https://leetcode.com/problems/accounts-merge/solution/
# JAVA
# class DSU {
#     int representative [];
#     int size [];
#   
#     DSU(int sz) {
#         representative = new int[sz];
#         size = new int[sz];
#          
#         for (int i = 0; i < sz; ++i) {
#             // Initially each group is its own representative
#             representative[i] = i;
#             // Intialize the size of all groups to 1
#             size[i] = 1;
#         }
#     }
#  
#     // Finds the representative of group x
#     public int findRepresentative(int x) {
#         if (x == representative[x]) {
#             return x;
#         }
#        
#         // This is path compression
#         return representative[x] = findRepresentative(representative[x]);
#     }
#    
#     // Unite the group that contains "a" with the group that contains "b"
#     public void unionBySize(int a, int b) {
#         int representativeA = findRepresentative(a);
#         int representativeB = findRepresentative(b);
#        
#         // If nodes a and b already belong to the same group, do nothing.
#         if (representativeA == representativeB) {
#             return;
#         }
#        
#         // Union by size: point the representative of the smaller
#         // group to the representative of the larger group.
#         if (size[representativeA] >= size[representativeB]) {
#             size[representativeA] += size[representativeB];
#             representative[representativeB] = representativeA;
#         } else {
#             size[representativeB] += size[representativeA];
#             representative[representativeA] = representativeB;
#         }
#     }
# }
#
# class Solution {
#     public List<List<String>> accountsMerge(List<List<String>> accountList) {
#         int accountListSize = accountList.size();
#         DSU dsu = new DSU(accountListSize);
#        
#         // Maps email to their component index
#         Map<String, Integer> emailGroup = new HashMap<>();
#       
#         for (int i = 0; i < accountListSize; i++) {
#             int accountSize = accountList.get(i).size();
#            
#             for (int j = 1; j < accountSize; j++) {
#                 String email = accountList.get(i).get(j);
#                 String accountName = accountList.get(i).get(0);
#                
#                 // If this is the first time seeing this email then
#                 // assign component group as the account index
#                 if (!emailGroup.containsKey(email)) {
#                     emailGroup.put(email, i);
#                 } else {
#                     // If we have seen this email before then union this
#                     // group with the previous group of the email
#                     dsu.unionBySize(i, emailGroup.get(email));
#                 }
#             }
#         }
#       
#         // Store emails corresponding to the component's representative
#         Map<Integer, List<String>> components = new HashMap<Integer, List<String>>();
#         for (String email : emailGroup.keySet()) {
#             int group = emailGroup.get(email);
#             int groupRep = dsu.findRepresentative(group);
#            
#             if (!components.containsKey(groupRep)) {
#                 components.put(groupRep, new ArrayList<String>());
#             }
#           
#             components.get(groupRep).add(email);
#         }
#       
#         // Sort the components and add the account name
#         List<List<String>> mergedAccounts = new ArrayList<>();
#         for (int group : components.keySet()) {
#             List<String> component = components.get(group);
#             Collections.sort(component); 
#             component.add(0, accountList.get(group).get(0));
#             mergedAccounts.add(component);
#         }
#       
#         return mergedAccounts;
#     }
# }

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