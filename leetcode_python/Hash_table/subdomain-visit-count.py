"""
A website domain like "discuss.leetcode.com" consists of various subdomains. At the top level, we have "com", at the next level, we have "leetcode.com", and at the lowest level, "discuss.leetcode.com". When we visit a domain like "discuss.leetcode.com", we will also visit the parent domains "leetcode.com" and "com" implicitly.

Now, call a "count-paired domain" to be a count (representing the number of visits this domain received), followed by a space, followed by the address. An example of a count-paired domain might be "9001 discuss.leetcode.com".

We are given a list cpdomains of count-paired domains. We would like a list of count-paired domains, (in the same format as the input, and in any order), that explicitly counts the number of visits to each subdomain.

Example 1:
Input: 
["9001 discuss.leetcode.com"]
Output: 
["9001 discuss.leetcode.com", "9001 leetcode.com", "9001 com"]
Explanation: 
We only have one website domain: "discuss.leetcode.com". As discussed above, the subdomain "leetcode.com" and "com" will also be visited. So they will all be visited 9001 times.

Example 2:
Input: 
["900 google.mail.com", "50 yahoo.com", "1 intel.mail.com", "5 wiki.org"]
Output: 
["901 mail.com","50 yahoo.com","900 google.mail.com","5 wiki.org","5 org","1 intel.mail.com","951 com"]
Explanation: 
We will visit "google.mail.com" 900 times, "yahoo.com" 50 times, "intel.mail.com" once and "wiki.org" 5 times. For the subdomains, we will visit "mail.com" 900 + 1 = 901 times, "com" 900 + 50 + 1 = 951 times, and "org" 5 times.

Notes:

The length of cpdomains will not exceed 100. 
The length of each domain name will not exceed 100.
Each address will have either 1 or 2 "." characters.
The input count in any count-paired domain will not exceed 10000.
The answer output can be returned in any order.

"""
# Time:  O(n), is the length of cpdomains (assuming the length of cpdomains[i] is fixed)
# Space: O(n)

# A website domain like "discuss.leetcode.com" consists of various subdomains.
# At the top level, we have "com", at the next level, we have "leetcode.com",
# and at the lowest level, "discuss.leetcode.com".
# When we visit a domain like "discuss.leetcode.com",
# we will also visit the parent domains "leetcode.com" and "com" implicitly.
#
# Now, call a "count-paired domain" to be a count
# (representing the number of visits this domain received),
# followed by a space, followed by the address.
# An example of a count-paired domain might be "9001 discuss.leetcode.com".
#
# We are given a list cpdomains of count-paired domains.
# We would like a list of count-paired domains,
# (in the same format as the input, and in any order),
# that explicitly counts the number of visits to each subdomain.
#
# Example 1:
# Input:
# ["9001 discuss.leetcode.com"]
# Output:
# ["9001 discuss.leetcode.com", "9001 leetcode.com", "9001 com"]
# Explanation:
# We only have one website domain: "discuss.leetcode.com".
# As discussed above, the subdomain "leetcode.com" and
# "com" will also be visited. So they will all be visited 9001 times.
#
# Example 2:
# Input:
# ["900 google.mail.com", "50 yahoo.com", "1 intel.mail.com", "5 wiki.org"]
# Output:
# ["901 mail.com","50 yahoo.com","900 google.mail.com","5 wiki.org","5 org","1 intel.mail.com","951 com"]
# Explanation:
# We will visit "google.mail.com" 900 times, "yahoo.com" 50 times,
# "intel.mail.com" once and "wiki.org" 5 times.
# For the subdomains, we will visit "mail.com" 900 + 1 = 901 times,
# "com" 900 + 50 + 1 = 951 times, and "org" 5 times.
#
# Notes:
# - The length of cpdomains will not exceed 100.
# - The length of each domain name will not exceed 100.
# - Each address will have either 1 or 2 "." characters.
# - The input count in any count-paired domain will not exceed 10000.

# V0 

# V1 
# http://bookshadow.com/weblog/2018/04/02/leetcode-subdomain-visit-count/
import collections
class Solution(object):
    def subdomainVisits(self, cpdomains):
        """
        :type cpdomains: List[str]
        :rtype: List[str]
        """
        cnt = collections.defaultdict(int)
        for cpdomain in cpdomains:
            count, domain = cpdomain.split()
            parts = domain.split('.')[::-1]
            for x in range(1, len(parts) + 1):
                cnt['.'.join(parts[:x][::-1])] += int(count)
        return ['{} {}'.format(v, k) for k, v in cnt.items()]

# V1'
# https://www.jiuzhang.com/solution/subdomain-visit-count/#tag-highlight-lang-python
class Solution:
    def subdomainVisits(self, cpdomains):
        count = {}
        for domain in cpdomains:
            visits = int(domain.split()[0])
            domain_segments = domain.split()[1].split('.')
            top_level_domain = domain_segments[-1]
            sec_level_domain = domain_segments[-2] + '.' + domain_segments[-1]
            count[top_level_domain] = count[top_level_domain] + visits if top_level_domain in count.keys() else visits
            count[sec_level_domain] = count[sec_level_domain] + visits if sec_level_domain in count.keys() else visits
            if domain.count('.') == 2:
                count[domain.split()[1]] = count[domain.split()[1]] + visits if domain.split()[1] in count.keys() else visits
        return [str(v) + ' ' + k for k,v in count.items()]

# V2 
# Time:  O(n), is the length of cpdomains (assuming the length of cpdomains[i] is fixed)
# Space: O(n)
import collections
class Solution(object):
    def subdomainVisits(self, cpdomains):
        """
        :type cpdomains: List[str]
        :rtype: List[str]
        """
        result = collections.defaultdict(int)
        for domain in cpdomains:
            count, domain = domain.split()
            count = int(count)
            frags = domain.split('.')
            curr = []
            for i in reversed(range(len(frags))):
                curr.append(frags[i])
                result[".".join(reversed(curr))] += count

        return ["{} {}".format(count, domain) \
                for domain, count in result.iteritems()]
