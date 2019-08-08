# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/85389781
class Solution(object):
    def spellchecker(self, wordlist, queries):
        """
        :type wordlist: List[str]
        :type queries: List[str]
        :rtype: List[str]
        """
        wordset = set(wordlist)
        capdict = {word.lower() : word for word in wordlist[::-1]}
        vodict = {re.sub(r'[aeiou]', '#', word.lower()) : word for word in wordlist[::-1]}
        res = []
        for q in queries:
            if q in wordset:
                res.append(q)
            elif q.lower() in capdict:
                res.append(capdict[q.lower()])
            elif re.sub(r'[aeiou]', '#', q.lower()) in vodict:
                res.append(vodict[re.sub(r'[aeiou]', '#', q.lower())])
            else:
                res.append("")
        return res
        
# V2 
# Time:  O(n)
# Space: O(w)
class Solution(object):
    def spellchecker(self, wordlist, queries):
        """
        :type wordlist: List[str]
        :type queries: List[str]
        :rtype: List[str]
        """
        vowels = set(['a', 'e', 'i', 'o', 'u'])
        def todev(word):
            return "".join('*' if c.lower() in vowels else c.lower()
                           for c in word)

        words = set(wordlist)
        caps = {}
        vows = {}

        for word in wordlist:
            caps.setdefault(word.lower(), word)
            vows.setdefault(todev(word), word)

        def check(query):
            if query in words:
                return query
            lower = query.lower()
            if lower in caps:
                return caps[lower]
            devow = todev(lower)
            if devow in vows:
                return vows[devow]
            return ""
        return map(check, queries)