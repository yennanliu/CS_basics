<br>
<details class="hint-accordion">  
    <summary>Recommended Time & Space Complexity</summary>
    <p>
    You should aim for a solution with <code>O(n)</code> time and <code>O(1)</code> space without modifying the input array, where <code>n</code> is the size of the input array.
    </p>
</details>

<br>
<details class="hint-accordion">  
    <summary>Hint 1</summary>
    <p>
    A naive approach would be to use a hash set, which provides <code>O(1)</code> average-time lookups but requires <code>O(n)</code> extra space. To avoid extra space, consider treating the array like a linked list: from an index <code>i</code>, the next index is <code>nums[i]</code>. Every value is in the range <code>1</code> to <code>len(nums) - 1</code>, so every value is also a valid index.
    </p>
</details>

<br>
<details class="hint-accordion">  
    <summary>Hint 2</summary>
    <p>
    Since one value is repeated, multiple indices point to the same next index. Following the indices starting from index <code>0</code> must therefore lead to a cycle, whose entry corresponds to the duplicate value. Which algorithm can detect a cycle using constant extra space without modifying the array?
    </p>
</details>

<br>
<details class="hint-accordion">  
    <summary>Hint 3</summary>
    <p>
    Use Floyd's cycle detection algorithm. Initialize a slow and a fast pointer at index <code>0</code>. Repeatedly move the slow pointer one step with <code>slow = nums[slow]</code> and the fast pointer two steps with <code>fast = nums[nums[fast]]</code> until they meet. Is this first meeting point necessarily the duplicate?
    </p>
</details>

<br>
<details class="hint-accordion">  
    <summary>Hint 4</summary>
    <p>
    The first meeting point may be anywhere inside the cycle. After the pointers meet, initialize a second slow pointer at index <code>0</code>. Move both slow pointers one step at a time using the values in <code>nums</code>. The index where they meet is the cycle entry, which is the duplicate number.
    </p>
</details>
