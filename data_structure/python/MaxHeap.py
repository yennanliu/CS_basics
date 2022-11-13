#---------------------------------------------------------------
# MaxHeap (heap V2)
#---------------------------------------------------------------

# https://leetcode.com/explore/learn/card/heap/643/heap/4017/

# Implementing "Max Heap"
class MaxHeap:
    def __init__(self, heapSize):
        # Create a complete binary tree using an array
        # Then use the binary tree to construct a Heap
        self.heapSize = heapSize
        # the number of elements is needed when instantiating an array
        # heapSize records the size of the array
        self.maxheap = [0] * (heapSize + 1)
        # realSize records the number of elements in the Heap
        self.realSize = 0

    # Function to add an element
    def add(self, element):
        self.realSize += 1
        # If the number of elements in the Heap exceeds the preset heapSize
        # print "Added too many elements" and return
        if self.realSize > self.heapSize:
            print("Added too many elements!")
            self.realSize -= 1
            return
        # Add the element into the array
        self.maxheap[self.realSize] = element
        # Index of the newly added element
        index = self.realSize
        # Parent node of the newly added element
        # Note if we use an array to represent the complete binary tree
        # and store the root node at index 1
        # index of the parent node of any node is [index of the node / 2]
        # index of the left child node is [index of the node * 2]
        # index of the right child node is [index of the node * 2 + 1]
        parent = index // 2
        
        # If the newly added element is larger than its parent node,
        # its value will be exchanged with that of the parent node 
        while (self.maxheap[index] > self.maxheap[parent] and index > 1):
            self.maxheap[parent], self.maxheap[index] = self.maxheap[index], self.maxheap[parent]
            index = parent
            parent = index // 2
            
    # Get the top element of the Heap
    def peek(self):
        return self.maxheap[1]
    
    # Delete the top element of the Heap
    def pop(self):
        # If the number of elements in the current Heap is 0,
        # print "Don't have any elements" and return a default value
        if self.realSize < 1:
            print("Don't have any element!")
            return -sys.maxsize
        else:
            # When there are still elements in the Heap
            # self.realSize >= 1
            removeElement = self.maxheap[1]
            # Put the last element in the Heap to the top of Heap
            self.maxheap[1] = self.maxheap[self.realSize]
            self.realSize -= 1
            index = 1
            # When the deleted element is not a leaf node
            while (index <= self.realSize // 2):
                # the left child of the deleted element
                left = index * 2
                # the right child of the deleted element
                right = (index * 2) + 1
                # If the deleted element is smaller than the left or right child
                # its value needs to be exchanged with the larger value
                # of the left and right child
                if (self.maxheap[index] < self.maxheap[left] or self.maxheap[index] < self.maxheap[right]):
                    if self.maxheap[left] > self.maxheap[right]:
                        self.maxheap[left], self.maxheap[index] = self.maxheap[index], self.maxheap[left]
                        index = left
                    else:
                        self.maxheap[right], self.maxheap[index] = self.maxheap[index], self.maxheap[right]
                        index = right
                else:
                    break
            return removeElement
    
    # return the number of elements in the Heap
    def size(self):
        return self.realSize
    
    def __str__(self):
        return str(self.maxheap[1 : self.realSize + 1])
        

if __name__ == "__main__":
        # Test cases
        maxHeap = MaxHeap(5)
        maxHeap.add(1)
        maxHeap.add(2)
        maxHeap.add(3)
        # [3,1,2]
        print(maxHeap)
        # 3
        print(maxHeap.peek())
        # 3
        print(maxHeap.pop())
        # 2
        print(maxHeap.pop())
        # 1
        print(maxHeap.pop())
        maxHeap.add(4)
        maxHeap.add(5)
        # [5,4]
        print(maxHeap)