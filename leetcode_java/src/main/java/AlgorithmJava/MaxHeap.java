package AlgorithmJava;

// https://leetcode.com/explore/learn/card/heap/643/heap/4017/

// Implementing "Max Heap"
public class MaxHeap {
    // Create a complete binary tree using an array
    // Then use the binary tree to construct a Heap
    int[] maxHeap;
    // the number of elements is needed when instantiating an array
    // heapSize records the size of the array
    int heapSize;
    // realSize records the number of elements in the Heap
    int realSize = 0;

    public MaxHeap(int heapSize) {
        this.heapSize = heapSize;
        maxHeap = new int[heapSize + 1];
        // To better track the indices of the binary tree,
        // we will not use the 0-th element in the array
        // You can fill it with any value
        maxHeap[0] = 0;
    }

    // Function to add an element
    public void add(int element) {
        realSize++;
        // If the number of elements in the Heap exceeds the preset heapSize
        // print "Added too many elements" and return
        if (realSize > heapSize) {
            System.out.println("Added too many elements!");
            realSize--;
            return;
        }
        // Add the element into the array
        maxHeap[realSize] = element;
        // Index of the newly added element
        int index = realSize;
        // Parent node of the newly added element
        // Note if we use an array to represent the complete binary tree
        // and store the root node at index 1
        // index of the parent node of any node is [index of the node / 2]
        // index of the left child node is [index of the node * 2]
        // index of the right child node is [index of the node * 2 + 1]

        int parent = index / 2;
        // If the newly added element is larger than its parent node,
        // its value will be exchanged with that of the parent node
        while ( maxHeap[index] > maxHeap[parent] && index > 1 ) {
            int temp = maxHeap[index];
            maxHeap[index] = maxHeap[parent];
            maxHeap[parent] = temp;
            index = parent;
            parent = index / 2;
        }
    }

    // Get the top element of the Heap
    public int peek() {
        return maxHeap[1];
    }

    // Delete the top element of the Heap
    public int pop() {
        // If the number of elements in the current Heap is 0,
        // print "Don't have any elements" and return a default value
        if (realSize < 1) {
            System.out.println("Don't have any element!");
            return Integer.MIN_VALUE;
        } else {
            // When there are still elements in the Heap
            // realSize >= 1
            int removeElement = maxHeap[1];
            // Put the last element in the Heap to the top of Heap
            maxHeap[1] = maxHeap[realSize];
            realSize--;
            int index = 1;
            // When the deleted element is not a leaf node
            while (index <= realSize / 2) {
                // the left child of the deleted element
                int left = index * 2;
                // the right child of the deleted element
                int right = (index * 2) + 1;
                // If the deleted element is smaller than the left or right child
                // its value needs to be exchanged with the larger value
                // of the left and right child
                if (maxHeap[index] < maxHeap[left] || maxHeap[index] < maxHeap[right]) {
                    if (maxHeap[left] > maxHeap[right]) {
                        int temp = maxHeap[left];
                        maxHeap[left] = maxHeap[index];
                        maxHeap[index] = temp;
                        index = left;
                    } else {
                        // maxHeap[left] <= maxHeap[right]
                        int temp = maxHeap[right];
                        maxHeap[right] = maxHeap[index];
                        maxHeap[index] = temp;
                        index = right;
                    }
                } else {
                    break;
                }
            }
            return removeElement;
        }
    }

    // return the number of elements in the Heap
    public int size() {
        return realSize;
    }

    public String toString() {
        if (realSize == 0) {
            return "No element!";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append('[');
            for (int i = 1; i <= realSize; i++) {
                sb.append(maxHeap[i]);
                sb.append(',');
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(']');
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        // Test case
        MaxHeap maxheap = new MaxHeap(5);
        maxheap.add(1);
        maxheap.add(2);
        maxheap.add(3);
        // [3,1,2]
        System.out.println(maxheap.toString());
        // 3
        System.out.println(maxheap.peek());
        // 3
        System.out.println(maxheap.pop());
        System.out.println(maxheap.pop());
        System.out.println(maxheap.pop());
        // No element
        System.out.println(maxheap.toString());
        maxheap.add(4);
        // Add too many elements
        maxheap.add(5);
        // [4,1,2]
        System.out.println(maxheap.toString());
    }
}

