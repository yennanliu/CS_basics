package LeetCodeJava.Tree;

// https://leetcode.com/problems/implement-trie-prefix-tree/

/**
 * Your Trie object will be instantiated and called as such:
 * Trie obj = new Trie();
 * obj.insert(word);
 * boolean param_2 = obj.search(word);
 * boolean param_3 = obj.startsWith(prefix);
 */

public class ImplementTrie {


    // V0
    // TODO : fix below

//    public class ListNode2{
//
//        // attr
//        public String val;
//        public ListNode2 next;
//
//        // constructor
//        public ListNode2(){
//
//        }
//
//        public ListNode2(String val){
//            this.val = val;
//        }
//
//        ListNode2(String val, ListNode2 next){
//            this.val = val;
//            this.next = next;
//        }
//
//    }

//    class Trie {
//
//        // attr
//        //LinkedList<String> node;
//        ListNode2 node;
//        HashMap<String, ListNode2> map;
//
//        // constructor
//        public Trie() {
//            this.node = new ListNode2();
//            this.map = new HashMap<>();
//        }
//
//        public void insert(String word) {
//            String _first = String.valueOf(word.charAt(0));
//            if (!this.map.containsKey(_first)){
//                this.map.put(_first, new ListNode2(_first));
//            }else{
//                ListNode2 tmpNode = this.map.get(_first);
//                ListNode2 tmpNodeUpdated = this.addElementToNode(_first, tmpNode);
//                map.put(_first, tmpNodeUpdated);
//            }
//        }
//
//        public boolean search(String word) {
//
//            char[] charArray = word.toCharArray();
//            String _first = String.valueOf(word.charAt(0));
//            if (! startsWith(_first)){
//                return false;
//            }
//            if (!this.map.containsKey(_first)){
//                return false;
//            }
//            ListNode2 curNode = this.map.get(_first);
//            while (curNode.next != null){
//
//            }
//
//            return true;
//        }
//
//        public boolean startsWith(String prefix) {
//
//            return false;
//        }
//
//        private ListNode2 addElementToNode(String element, ListNode2 node){
//
//            return node;
//        }
//
//    }

    // V1
    // https://leetcode.com/problems/implement-trie-prefix-tree/editorial/
    class TrieNode {

        // R links to node children
        private TrieNode[] links;

        private final int R = 26;

        private boolean isEnd;

        public TrieNode() {
            links = new TrieNode[R];
        }

        public boolean containsKey(char ch) {
            return links[ch -'a'] != null;
        }
        public TrieNode get(char ch) {
            return links[ch -'a'];
        }
        public void put(char ch, TrieNode node) {
            links[ch -'a'] = node;
        }
        public void setEnd() {
            isEnd = true;
        }
        public boolean isEnd() {
            return isEnd;
        }
    }

    class Trie2 {
        private TrieNode root;

        public Trie2() {
            root = new TrieNode();
        }

        // Inserts a word into the trie.
        public void insert(String word) {
            TrieNode node = root;
            for (int i = 0; i < word.length(); i++) {
                char currentChar = word.charAt(i);
                if (!node.containsKey(currentChar)) {
                    node.put(currentChar, new TrieNode());
                }
                node = node.get(currentChar);
            }
            node.setEnd();
        }


        // search a prefix or whole key in trie and
        // returns the node where search ends
        private TrieNode searchPrefix(String word) {
            TrieNode node = root;
            for (int i = 0; i < word.length(); i++) {
                char curLetter = word.charAt(i);
                if (node.containsKey(curLetter)) {
                    node = node.get(curLetter);
                } else {
                    return null;
                }
            }
            return node;
        }

        // Returns if the word is in the trie.
        public boolean search(String word) {
            TrieNode node = searchPrefix(word);
            return node != null && node.isEnd();
        }

        // Returns if there is any word in the trie
        // that starts with the given prefix.
        public boolean startsWith(String prefix) {
            TrieNode node = searchPrefix(prefix);
            return node != null;
        }

    }

    // V2
    // https://leetcode.com/problems/implement-trie-prefix-tree/solutions/3309950/java-easiest-solution/
    class TrieNode3 {
        boolean isWord;
        TrieNode3[] children;

        public TrieNode3() {
            isWord = false;
            children = new TrieNode3[26]; // 26 English lowercase letters
        }
    }

    class Trie3 {
        TrieNode3 root;

        public Trie3() {
            root = new TrieNode3();
        }

        public void insert(String word) {
            TrieNode3 node = root;
            for (char c : word.toCharArray()) {
                int index = c - 'a';
                if (node.children[index] == null) {
                    node.children[index] = new TrieNode3();
                }
                node = node.children[index];
            }
            node.isWord = true;
        }

        public boolean search(String word) {
            TrieNode3 node = root;
            for (char c : word.toCharArray()) {
                int index = c - 'a';
                if (node.children[index] == null) {
                    return false;
                }
                node = node.children[index];
            }
            return node.isWord;
        }

        public boolean startsWith(String prefix) {
            TrieNode3 node = root;
            for (char c : prefix.toCharArray()) {
                int index = c - 'a';
                if (node.children[index] == null) {
                    return false;
                }
                node = node.children[index];
            }
            return true;
        }
    }

    // V3
    // https://leetcode.com/problems/implement-trie-prefix-tree/solutions/3308019/simple-implementation-in-java/
    class Trie4 {
        // Trie Node class
        class TrieNode {
            // to mark the last letter of a string as true
            // that yes, a word ends at this letter
            boolean isComplete;
            // a node array of size 26
            // to store nodes for 26 lowercase alphabets
            TrieNode[] children;
            public TrieNode() {
                isComplete = false;
                children = new TrieNode[26];
            }
        }

        TrieNode root;  // Declare the root node
        public Trie4() {
            root = new TrieNode();  // initialize the root node
        }

        public void insert(String word) {
            TrieNode node  = root;  // start traversing from the root
            for (char c : word.toCharArray()) {
                // if the node index for the character is empty
                if (node.children[c - 'a'] == null) {
                    // create a new node at that index
                    node.children[c - 'a'] = new TrieNode();
                }
                // jump to that node
                node = node.children[c - 'a'];
            }
            // mark the last letter as true
            // means a word ends at this letter
            node.isComplete = true;
        }

        public boolean search(String word) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                // if the node index for the character is empty
                if (node.children[c - 'a'] == null) {
                    return false;   // means word doesn't exist
                }
                // jump to the character node
                node = node.children[c - 'a'];
            }
            // return if the last letter is marked as true/false
            // if true -> a word ends at this character otherwise not
            // if this exact word were inserted, we would have marked
            // the last character as true for sure
            return node.isComplete;
        }

        public boolean startsWith(String prefix) {
            TrieNode node = root;
            for (char c : prefix.toCharArray()) {
                // if the node index for the character is empty
                if (node.children[c - 'a'] == null) {
                    return false;
                }
                node = node.children[c - 'a'];
            }
            // all the character nodes were found in the tree
            return true;    // means prefix exists
        }
    }

}
