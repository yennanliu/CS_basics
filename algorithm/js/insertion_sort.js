//---------------------------------------------------------------
// INSERTION SORT
//---------------------------------------------------------------
//
// Build the sorted part one value at a time, the way you sort a hand
// of cards: pick up the next value and slide it left past everything
// bigger than it.
//
//   [5 | 1, 4, 2]   take 1, shift 5 right, drop 1 at the front
//   [1, 5 | 4, 2]   take 4, shift 5 right, drop 4
//   [1, 4, 5 | 2]   take 2, shift 5 and 4 right, drop 2
//   [1, 2, 4, 5]
//
// The inner loop SHIFTS rather than swaps: each bigger value is copied
// one slot right, and `key` is written once at the end. That is half
// the writes of the swap-based version.
//
// WHY IT IS THE ONE O(N^2) SORT WORTH KNOWING:
//   - STABLE (the `>` comparison never moves an equal value past `key`)
//   - in place, O(1) extra memory
//   - O(N) on nearly-sorted input -- each new value stops immediately
//   - low constant factor, so real sort implementations (Timsort,
//     introsort) switch to it for small subarrays
//
// Time  : Best O(N) (already sorted), Avg/Worst O(N^2)
// Space : O(1)

function insertionSort(array) {
  for (let i = 1; i < array.length; i++) {
    const key = array[i]; // the value being placed
    let j = i - 1;

    // shift everything greater than `key` one slot to the right
    while (j >= 0 && array[j] > key) {
      array[j + 1] = array[j];
      j--;
    }

    array[j + 1] = key; // the hole left behind is where `key` belongs
  }
  return array;
}

// demo
console.assert(
  insertionSort([99, 44, 6, 2, 1, 5, 63, 87, 283, 4, 0]).join(',') === '0,1,2,4,5,6,44,63,87,99,283',
  'sorts a mixed array'
);
console.assert(insertionSort([]).join(',') === '', 'empty array');
console.assert(insertionSort([1]).join(',') === '1', 'single element');
console.assert(insertionSort([1, 2, 3]).join(',') === '1,2,3', 'already sorted');
console.assert(insertionSort([3, 2, 1]).join(',') === '1,2,3', 'reversed');
console.assert(insertionSort([2, 1, 2, 1]).join(',') === '1,1,2,2', 'duplicates');
console.assert(insertionSort([0, -3, 5, -1]).join(',') === '-3,-1,0,5', 'negatives');

// stability: equal keys keep their original relative order
const pairs = [{ k: 2, tag: 'a' }, { k: 1, tag: 'b' }, { k: 2, tag: 'c' }];
for (let i = 1; i < pairs.length; i++) {
  const key = pairs[i];
  let j = i - 1;
  while (j >= 0 && pairs[j].k > key.k) {
    pairs[j + 1] = pairs[j];
    j--;
  }
  pairs[j + 1] = key;
}
console.assert(pairs.map((p) => p.tag).join('') === 'bac', 'stable: a stays before c');

console.log(insertionSort([99, 44, 6, 2, 1, 5, 63, 87, 283, 4, 0]).join(' '));
console.log('Success.');

module.exports = { insertionSort };
