# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/83478570
class Solution(object):
    def numUniqueEmails(self, emails):
        """
        :type emails: List[str]
        :rtype: int
        """
        eset = set()
        for email in emails:
            simper = self.simpifyEmail(email)
            eset.add(simper)
        return len(eset)
    
    def simpifyEmail(self, email):
        local, domain = email.split("@")
        local = local.replace('.', '')
        plus_i = local.find('+')
        if plus_i != -1:
            local = local[:plus_i]
        return local + "@" + domain

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/83478570
class Solution:
    def numUniqueEmails(self, emails):
        """
        :type emails: List[str]
        :rtype: int
        """
        res = set()
        for email in emails:
            name, domain = email.split("@")
            name = name.split("+")[0].replace(".", "")
            res.add(name + "@" + domain)
        return len(res)
        
# V2 
# Time:  O(n * l)
# Space: O(n * l)
class Solution(object):
    def numUniqueEmails(self, emails):
        """
        :type emails: List[str]
        :rtype: int
        """
        def convert(email):
            name, domain = email.split('@')
            name = name[:name.index('+')]
            return "".join(["".join(name.split(".")), '@', domain])

        lookup = set()
        for email in emails:
            lookup.add(convert(email))
        return len(lookup)