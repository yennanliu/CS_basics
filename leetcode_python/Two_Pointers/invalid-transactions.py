"""

1169. Invalid Transactions
Medium

A transaction is possibly invalid if:

the amount exceeds $1000, or;
if it occurs within (and including) 60 minutes of another transaction with the same name in a different city.

You are given an array of strings transaction where transactions[i] consists of comma-separated values
representing the name, time (in minutes), amount, and city of the transaction.

Return a list of transactions that are possibly invalid. You may return the answer in any order.

Example 1:

Input: transactions = ["alice,20,800,mtv","alice,50,100,beijing"]
Output: ["alice,20,800,mtv","alice,50,100,beijing"]
Explanation: The first transaction is invalid because the second transaction occurs within a difference of
60 minutes, have the same name and is in a different city. Similarly the second one is invalid too.

Example 2:

Input: transactions = ["alice,20,800,mtv","alice,50,1200,mtv"]
Output: ["alice,50,1200,mtv"]

Example 3:

Input: transactions = ["alice,20,800,mtv","bob,50,1200,mtv"]
Output: ["bob,50,1200,mtv"]

Constraints:

transactions.length <= 1000
Each transactions[i] takes the form "{name},{time},{amount},{city}"
Each {name} and {city} consist of lowercase English letters, and have lengths between 1 and 10.
Each {time} consist of digits, and represent an integer between 0 and 1000.
Each {amount} consist of digits, and represent an integer between 0 and 2000.

"""

# V0
# IDEA : HASH TABLE (group by name) + PAIRWISE CHECK
#
#  note : duplicated transaction strings are handled by index,
#         so each occurrence is judged (and returned) on its own.
#
# time = O(n^2)
# space = O(n)
from collections import defaultdict
class Solution(object):
    def invalidTransactions(self, transactions):
        parsed = []
        for t in transactions:
            name, time, amount, city = t.split(",")
            parsed.append((name, int(time), int(amount), city))

        by_name = defaultdict(list)
        for i, (name, _, _, _) in enumerate(parsed):
            by_name[name].append(i)

        res = []
        for i, (name, time, amount, city) in enumerate(parsed):
            bad = amount > 1000
            if not bad:
                for j in by_name[name]:
                    if j == i:
                        continue
                    _, t2, _, c2 = parsed[j]
                    if c2 != city and abs(t2 - time) <= 60:
                        bad = True
                        break
            if bad:
                res.append(transactions[i])
        return res
