"""

1268. Search Suggestions System
Medium

You are given an array of strings products and a string searchWord.

Design a system that suggests at most three product names from products after each character of searchWord is typed. Suggested products should have common prefix with searchWord. If there are more than three products with a common prefix return the three lexicographically minimums products.

Return a list of lists of the suggested products after each character of searchWord is typed.

 

Example 1:

Input: products = ["mobile","mouse","moneypot","monitor","mousepad"], searchWord = "mouse"
Output: [
["mobile","moneypot","monitor"],
["mobile","moneypot","monitor"],
["mouse","mousepad"],
["mouse","mousepad"],
["mouse","mousepad"]
]
Explanation: products sorted lexicographically = ["mobile","moneypot","monitor","mouse","mousepad"]
After typing m and mo all products match and we show user ["mobile","moneypot","monitor"]
After typing mou, mous and mouse the system suggests ["mouse","mousepad"]
Example 2:

Input: products = ["havana"], searchWord = "havana"
Output: [["havana"],["havana"],["havana"],["havana"],["havana"],["havana"]]
Example 3:

Input: products = ["bags","baggage","banner","box","cloths"], searchWord = "bags"
Output: [["baggage","bags","banner"],["baggage","bags","banner"],["baggage","bags"],["bags"]]
 

Constraints:

1 <= products.length <= 1000
1 <= products[i].length <= 3000
1 <= sum(products[i].length) <= 2 * 104
All the strings of products are unique.
products[i] consists of lowercase English letters.
1 <= searchWord.length <= 1000
searchWord consists of lowercase English letters.

"""

# V0

# V1
# IDEA : TRIE
# https://leetcode.com/problems/search-suggestions-system/discuss/436183/Python-Trie-Solution
class TrieNode:
    def __init__(self):
        self.children = dict()
        self.words = []

class Trie:
    def __init__(self):
        self.root = TrieNode()
    
    def insert(self, word):
        node = self.root
        for char in word:
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
            node.words.append(word)
            node.words.sort()
            while len(node.words) > 3:
                node.words.pop()
    
    def search(self, word):
        res = []
        node = self.root
        for char in word:
            if char not in node.children:
                break
            node = node.children[char]
            res.append(node.words[:])
        l_remain = len(word) - len(res)
        for _ in range(l_remain):
            res.append([])
        return res

class Solution:
    def suggestedProducts(self, products: List[str], searchWord: str):
        trie = Trie()
        for prod in products:
            trie.insert(prod)
        return trie.search(searchWord)

# V1'
# IDEA : array + sort
# https://leetcode.com/problems/search-suggestions-system/discuss/1243220/Python-3-very-simple-solution
class Solution:
    def suggestedProducts(self, products, searchWord):
        answ=[]
        products.sort()
        for i in range(len(searchWord)):
            newProduct=[]
            for p in products:
                if i<len(p) and p[i]==searchWord[i]:
                    newProduct.append(p)
            answ.append(newProduct[:3])
            products=newProduct
        return answ

# V1''
# IDEA :  array + sort
# https://leetcode.com/problems/search-suggestions-system/discuss/882649/Python
class Solution:
    def suggestedProducts(self, products, searchWord):
            products = sorted(products)

            slen = len(searchWord)
            output = []
            for i in range(1,slen+1):
                curr = searchWord[:i]
                temp = []
                counter=0
                for j in range(len(products)):
                    if counter<3:
                        if curr == products[j][0:i]:
                            temp.append(products[j])
                            counter+=1
                output.append(temp)
            return(output)

# V1''''
# IDEA : HEAP
# https://leetcode.com/problems/search-suggestions-system/discuss/1242697/Python-heap
class Solution:
    def suggestedProducts(self, products, searchWord):
        ans = []
        
        #create a heap of words
        #this is automatically sorted by lexicographical order
        hp = []
        for x in products:
            heapq.heappush(hp, x)
        
        #loop over every character of searchWord
        for i, char in enumerate(searchWord):
            #if empty heap, nothing to be done
            if not hp:
                ans.append([])
                continue
            #buffer contains at most 3 elements
            buffer = []
            while hp and len(buffer) < 3:
                this = heapq.heappop(hp)
                if len(this) >= i + 1 and this[:i+1] == searchWord[:i+1]:
                    buffer.append(this)
            #add the elements back to heap
            for x in buffer:
                heapq.heappush(hp, x)
            #suggestions at current character
            ans.append(buffer)
        
        return ans

# V1'''''
# IDEA : bisect
# https://leetcode.com/problems/search-suggestions-system/discuss/517125/Python-simple-solution
class Solution:
    def suggestedProducts(self, products, searchWord):
            products.sort()
            res = []
            key = ''
            for c in searchWord:
                key += c
                idx = bisect.bisect_left(products, key)
                res.append([products[i] for i in range(idx, min(len(products), idx + 3)) if products[i].startswith(key)])
            return res

# V1''''''
# IDEA : BINARY SEARCH
# https://leetcode.com/problems/search-suggestions-system/solution/
# JAVA
# class Solution {
#     // Equivalent code for lower_bound in Java
#     int lower_bound(String[] products, int start, String word) {
#         int i = start, j = products.length, mid;
#         while (i < j) {
#             mid = (i + j) / 2;
#             if (products[mid].compareTo(word) >= 0)
#                 j = mid;
#             else
#                 i = mid + 1;
#         }
#         return i;
#     }
# public
#     List<List<String>> suggestedProducts(String[] products, String searchWord) {
#         Arrays.sort(products);
#         List<List<String>> result = new ArrayList<>();
#         int start = 0, bsStart = 0, n = products.length;
#         String prefix = new String();
#         for (char c : searchWord.toCharArray()) {
#             prefix += c;
#
#             // Get the starting index of word starting with `prefix`.
#             start = lower_bound(products, bsStart, prefix);
#
#             // Add empty vector to result.
#             result.add(new ArrayList<>());
#
#             // Add the words with the same prefix to the result.
#             // Loop runs until `i` reaches the end of input or 3 times or till the
#             // prefix is same for `products[i]` Whichever comes first.
#             for (int i = start; i < Math.min(start + 3, n); i++) {
#                 if (products[i].length() < prefix.length() || !products[i].substring(0, prefix.length()).equals(prefix))
#                     break;
#                 result.get(result.size() - 1).add(products[i]);
#             }
#
#             // Reduce the size of elements to binary search on since we know
#             bsStart = Math.abs(start);
#         }
#         return result;
#     }
# }

# V1''''''''
# IDEA : TRIE + DFS
# https://leetcode.com/problems/search-suggestions-system/solution/
# JAVA
# // Custom class Trie with function to get 3 words starting with given prefix
# class Trie {
#
#     // Node definition of a trie
#     class Node {
#         boolean isWord = false;
#         List<Node> children = Arrays.asList(new Node[26]);
#     };
#     Node Root, curr;
#     List<String> resultBuffer;
#
#     // Runs a DFS on trie starting with given prefix and adds all the words in the resultBuffer, limiting result size to 3
#     void dfsWithPrefix(Node curr, String word) {
#         if (resultBuffer.size() == 3)
#             return;
#         if (curr.isWord)
#             resultBuffer.add(word);
#
#         // Run DFS on all possible paths.
#         for (char c = 'a'; c <= 'z'; c++)
#             if (curr.children.get(c - 'a') != null)
#                 dfsWithPrefix(curr.children.get(c - 'a'), word + c);
#     }
#     Trie() {
#         Root = new Node();
#     }
#
#     // Inserts the string in trie.
#     void insert(String s) {
#
#         // Points curr to the root of trie.
#         curr = Root;
#         for (char c : s.toCharArray()) {
#             if (curr.children.get(c - 'a') == null)
#                 curr.children.set(c - 'a', new Node());
#             curr = curr.children.get(c - 'a');
#         }
#
#         // Mark this node as a completed word.
#         curr.isWord = true;
#     }
#     List<String> getWordsStartingWith(String prefix) {
#         curr = Root;
#         resultBuffer = new ArrayList<String>();
#         // Move curr to the end of prefix in its trie representation.
#         for (char c : prefix.toCharArray()) {
#             if (curr.children.get(c - 'a') == null)
#                 return resultBuffer;
#             curr = curr.children.get(c - 'a');
#         }
#         dfsWithPrefix(curr, prefix);
#         return resultBuffer;
#     }
# };
# class Solution {
#     List<List<String>> suggestedProducts(String[] products,
#                                          String searchWord) {
#         Trie trie = new Trie();
#         List<List<String>> result = new ArrayList<>();
#         // Add all words to trie.
#         for (String w : products)
#             trie.insert(w);
#         String prefix = new String();
#         for (char c : searchWord.toCharArray()) {
#             prefix += c;
#             result.add(trie.getWordsStartingWith(prefix));
#         }
#         return result;
#     }
# };

# V2