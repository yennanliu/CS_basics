// https://www.coursera.org/learn/algorithms-part1/lecture/7An9B/binary-search-trees

import java.util.LinkedList;

 
public class BST<Key extends Comparable<Key>, Value>
{
    private Node root;

    private class Node
    {
        private Key key;
        private Value val;
        private Node left, right;
        public Node(Key key, Value val)
        {
            this.key = key;
            this.val = val;
        }

    }
        
    public void put(Key key, Value val)
    {}

    public void get(Key key)
    {}

    public void delete(Key key)
     {}

    public Iterable<Key> iterator()
    {}

}

