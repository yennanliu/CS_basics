package LeetCodeJava.Design;

// https://leetcode.com/problems/design-in-memory-file-system/description/
// https://leetcode.ca/2017-07-10-588-Design-In-Memory-File-System/

import java.util.*;

/**
 * 588. Design In-Memory File System
 * Description
 * Design a data structure that simulates an in-memory file system.
 *
 * Implement the FileSystem class:
 *
 * FileSystem() Initializes the object of the system.
 * List<String> ls(String path)
 * If path is a file path, returns a list that only contains this file's name.
 * If path is a directory path, returns the list of file and directory names in this directory.
 * The answer should in lexicographic order.
 * void mkdir(String path) Makes a new directory according to the given path. The given directory path does not exist. If the middle directories in the path do not exist, you should create them as well.
 * void addContentToFile(String filePath, String content)
 * If filePath does not exist, creates that file containing given content.
 * If filePath already exists, appends the given content to original content.
 * String readContentFromFile(String filePath) Returns the content in the file at filePath.
 *
 *
 * Example 1:
 *
 *
 *
 * Input
 * ["FileSystem", "ls", "mkdir", "addContentToFile", "ls", "readContentFromFile"]
 * [[], ["/"], ["/a/b/c"], ["/a/b/c/d", "hello"], ["/"], ["/a/b/c/d"]]
 * Output
 * [null, [], null, null, ["a"], "hello"]
 *
 * Explanation
 * FileSystem fileSystem = new FileSystem();
 * fileSystem.ls("/"); // return []
 * fileSystem.mkdir("/a/b/c");
 * fileSystem.addContentToFile("/a/b/c/d", "hello");
 * fileSystem.ls("/"); // return ["a"]
 * fileSystem.readContentFromFile("/a/b/c/d"); // return "hello"
 *
 *
 *
 * Constraints:
 *
 * 1 <= path.length, filePath.length <= 100
 * path and filePath are absolute paths which begin with '/' and do not end with '/' except that the path is just "/".
 * You can assume that all directory names and file names only contain lowercase letters, and the same names will not exist in the same directory.
 * You can assume that all operations will be passed valid parameters, and users will not attempt to retrieve file content or list a directory or file that does not exist.
 * 1 <= content.length <= 50
 * At most 300 calls will be made to ls, mkdir, addContentToFile, and readContentFromFile.
 *
 *
 */
public class DesignInMemoryFileSystem {

    // V0
//    class FileSystem {
//        private Trie root = new Trie();
//
//        public FileSystem() {
//        }
//
//        public List<String> ls(String path) {
//        }
//
//        public void mkdir(String path) {
//        }
//
//        public void addContentToFile(String filePath, String content) {
//        }
//
//        public String readContentFromFile(String filePath) {
//        }
//    }


    // V0-1
    // IDEA: TRIE (gemini)
    // TODO: validate
    class FileSystem_0_1 {

        // --- Trie Node Class ---
        private class TrieNode {
            // Map to store children (file or directory name -> TrieNode)
            Map<String, TrieNode> children;

            // Flag to indicate if this node represents a file.
            boolean isFile;

            // Stores the content if this node is a file.
            String content;

            /**
             * time = O(1)
             * space = O(1)
             */
            TrieNode() {
                this.children = new HashMap<>();
                this.isFile = false;
                this.content = "";
            }
        }

        private final TrieNode root;

        public FileSystem_0_1() {
            // Initialize the root node, representing the root directory '/'
            this.root = new TrieNode();
        }

        /**
         * Helper to traverse or create nodes along a path.
         * Path must start with '/'.
         * @param path The full path string (e.g., "/a/b/c").
         * @return The TrieNode at the end of the path.
         */
        private TrieNode traverse(String path) {
            // Split path into components, skipping the leading empty string from splitting on '/'
            String[] components = path.substring(1).split("/");
            TrieNode current = this.root;

            for (String component : components) {
                if (component.isEmpty()) continue; // Handles path like "//" or trailing "/"

                // Create a new node if the directory/file doesn't exist
                current.children.putIfAbsent(component, new TrieNode());
                current = current.children.get(component);
            }
            return current;
        }

        // --- Public Methods ---

        /**
         * time = O(K)
         * space = O(1)
         */
        public List<String> ls(String path) {
            TrieNode current = this.root;
            List<String> result = new ArrayList<>();

            // Traverse to the parent directory of the final component,
            // or just traverse the whole path if it's a directory itself.

            // Get the directory path and the final component name
            String dirPath;
            String name = "";

            // If the path is not the root, separate the final component
            if (!path.equals("/")) {
                int lastSlash = path.lastIndexOf('/');
                dirPath = path.substring(0, lastSlash);
                name = path.substring(lastSlash + 1);

                // Traverse to the parent of the final component.
                current = traverse(dirPath.isEmpty() ? "/" : dirPath);

                // Move to the final component (which must exist for ls to work)
                if (!current.children.containsKey(name)) {
                    return result; // Path does not exist
                }
                current = current.children.get(name);
            }

            // Case 1: If the node is a file, return a list containing only the file name.
            if (current.isFile) {
                // Re-traverse to get the final component name
                result.add(name);
            }
            // Case 2: If the node is a directory (or the file's parent directory), return children names.
            else {
                result.addAll(current.children.keySet());
                Collections.sort(result); // The problem requires sorting alphabetically
            }

            return result;
        }

        /**
         * time = O(K)
         * space = O(K)
         */
        public void mkdir(String path) {
            // Use traverse helper to create all nodes along the path.
            // The traverse helper handles the creation of directories implicitly.
            traverse(path);
            // No further action is needed since directories are just TrieNodes.
        }

        /**
         * time = O(K)
         * space = O(1)
         */
        public void addContentToFile(String filePath, String content) {
            TrieNode fileNode = traverse(filePath);

            // Mark the node as a file
            fileNode.isFile = true;

            // Append the new content to existing content
            fileNode.content += content;

            // Note: The logic for handling the file name in the Trie is handled
            // correctly by the traverse helper, which sees the file name as the final component.
        }

        /**
         * time = O(K)
         * space = O(1)
         */
        public String readContentFromFile(String filePath) {
            TrieNode fileNode = traverse(filePath);

            // We only return content if the node exists AND is a file.
            // The traverse helper guarantees existence if called with a valid path.
            if (fileNode.isFile) {
                return fileNode.content;
            }
            return ""; // Should theoretically not be hit if input paths are valid files
        }
    }

    // V0-2
    // IDEA: TRIE (gpt)
    // TODO: validate
    public class FileSystem_0_2 {
        private class Node {
            Map<String, Node> children = new TreeMap<>();
            StringBuilder content = null;   // null if this node is a directory
        }

        private final Node root;

        public FileSystem_0_2() {
            root = new Node();
        }

        /**
         * time = O(K)
         * space = O(1)
         */
        public List<String> ls(String path) {
            Node node = traverse(path, false);
            // if it's a file
            if (node != null && node.content != null) {
                int idx = path.lastIndexOf("/");
                return Arrays.asList(path.substring(idx + 1));
            }
            // else it's a directory
            if (node == null) return new ArrayList<>();
            List<String> res = new ArrayList<>(node.children.keySet());
            // TreeMap ensures lex order already
            return res;
        }

        /**
         * time = O(K)
         * space = O(K)
         */
        public void mkdir(String path) {
            traverse(path, true);
        }

        /**
         * time = O(K)
         * space = O(1)
         */
        public void addContentToFile(String filePath, String content) {
            Node node = traverse(filePath, true);
            if (node.content == null) node.content = new StringBuilder();
            node.content.append(content);
        }

        /**
         * time = O(K)
         * space = O(1)
         */
        public String readContentFromFile(String filePath) {
            Node node = traverse(filePath, false);
            if (node == null || node.content == null) return "";
            return node.content.toString();
        }

        /** Traverse the path. If create == true, create missing nodes. */
        private Node traverse(String path, boolean create) {
            String[] parts = path.split("/");
            Node cur = root;
            // skip first empty if path starts with "/"
            for (int i = 1; i < parts.length; i++) {
                String p = parts[i];
                if (!cur.children.containsKey(p)) {
                    if (create) {
                        cur.children.put(p, new Node());
                    } else {
                        return null;
                    }
                }
                cur = cur.children.get(p);
            }
            return cur;
        }
    }


    // V1
    // IDEA: TRIE
    // https://leetcode.ca/2017-07-10-588-Design-In-Memory-File-System/
    class Trie {
        String name;
        boolean isFile;
        StringBuilder content = new StringBuilder();
        Map<String, Trie> children = new HashMap<>();

        Trie insert(String path, boolean isFile) {
            Trie node = this;
            String[] ps = path.split("/");
            for (int i = 1; i < ps.length; ++i) {
                String p = ps[i];
                if (!node.children.containsKey(p)) {
                    node.children.put(p, new Trie());
                }
                node = node.children.get(p);
            }
            node.isFile = isFile;
            if (isFile) {
                node.name = ps[ps.length - 1];
            }
            return node;
        }

        Trie search(String path) {
            Trie node = this;
            String[] ps = path.split("/");
            for (int i = 1; i < ps.length; ++i) {
                String p = ps[i];
                if (!node.children.containsKey(p)) {
                    return null;
                }
                node = node.children.get(p);
            }
            return node;
        }
    }

    class FileSystem {
        private Trie root = new Trie();

        public FileSystem() {
        }

        /**
         * time = O(K)
         * space = O(1)
         */
        public List<String> ls(String path) {
            List<String> ans = new ArrayList<>();
            Trie node = root.search(path);
            if (node == null) {
                return ans;
            }
            if (node.isFile) {
                ans.add(node.name);
                return ans;
            }
            for (String v : node.children.keySet()) {
                ans.add(v);
            }
            Collections.sort(ans);
            return ans;
        }

        /**
         * time = O(K)
         * space = O(K)
         */
        public void mkdir(String path) {
            root.insert(path, false);
        }

        /**
         * time = O(K)
         * space = O(1)
         */
        public void addContentToFile(String filePath, String content) {
            Trie node = root.insert(filePath, true);
            node.content.append(content);
        }

        /**
         * time = O(K)
         * space = O(1)
         */
        public String readContentFromFile(String filePath) {
            Trie node = root.search(filePath);
            return node.content.toString();
        }
    }


    // V2

    // V3


}
