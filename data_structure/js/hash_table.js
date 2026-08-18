//---------------------------------------------------------------
// HASH TABLE (separate chaining)
//---------------------------------------------------------------
//
// A hash table buys O(1) lookup by SPENDING memory -- the classic
// time-space tradeoff.
//
//   1) hash the key to an integer
//   2) fold it into a bucket index         hash % numberOfBuckets
//   3) store [key, value] in that bucket
//
// Two keys can land in the same bucket (a COLLISION). Here that is
// resolved by SEPARATE CHAINING: each bucket holds an array of
// entries, and lookup scans that short array.
//
//     data[0] -> [ ['a', 1] ]
//     data[1] -> undefined
//     data[2] -> [ ['b', 2], ['g', 7] ]     <- collision, chained
//
// The full key still has to be compared inside the bucket: an equal
// hash does NOT mean an equal key.
//
// Time  : set / get / remove -> O(1) average, O(N) worst
//                               (worst = every key in one bucket)
//         keys / values      -> O(numberOfBuckets + N)
// Space : O(N)

class HashTable {
  constructor(size = 50) {
    this.data = new Array(size);
    this.size = 0; // number of stored entries
  }

  // Turn a string key into a bucket index.
  // NOTE: this is a teaching hash, not a good one -- a real table uses
  // something like FNV-1a or MurmurHash to spread keys evenly.
  _hash(key) {
    let hash = 0;
    for (let i = 0; i < key.length; i++) {
      hash = (hash + key.charCodeAt(i) * (i + 1)) % this.data.length;
    }
    return hash;
  }

  // Insert, or OVERWRITE when the key is already present.
  set(key, value) {
    const address = this._hash(key);
    if (!this.data[address]) this.data[address] = [];

    const bucket = this.data[address];
    for (const entry of bucket) {
      if (entry[0] === key) {
        entry[1] = value; // overwrite -- do not append a duplicate
        return this;
      }
    }
    bucket.push([key, value]);
    this.size++;
    return this;
  }

  // Return the value for key, or undefined.
  get(key) {
    const bucket = this.data[this._hash(key)];
    if (!bucket) return undefined;
    for (const [storedKey, value] of bucket) {
      if (storedKey === key) return value;
    }
    return undefined;
  }

  has(key) {
    return this.get(key) !== undefined;
  }

  // Delete key; returns true if something was removed.
  remove(key) {
    const bucket = this.data[this._hash(key)];
    if (!bucket) return false;
    for (let i = 0; i < bucket.length; i++) {
      if (bucket[i][0] === key) {
        bucket.splice(i, 1);
        this.size--;
        return true;
      }
    }
    return false;
  }

  // Every key, across every bucket -- including chained ones.
  keys() {
    const keys = [];
    for (const bucket of this.data) {
      if (!bucket) continue;
      for (const [key] of bucket) keys.push(key);
    }
    return keys;
  }

  values() {
    return this.keys().map((key) => this.get(key));
  }
}

// demo
const table = new HashTable(50);
table.set('grapes', 10000);
table.set('apples', 9);
table.set('oranges', 2);
console.assert(table.size === 3, 'three entries stored');
console.assert(table.get('grapes') === 10000, 'lookup by key');
console.assert(table.has('apples'), 'has() finds a stored key');

// setting an existing key overwrites instead of growing the table
table.set('grapes', 42);
console.assert(table.get('grapes') === 42, 'value overwritten');
console.assert(table.size === 3, 'size unchanged by an overwrite');

console.assert(table.get('missing') === undefined, 'absent key is undefined');
console.assert(table.remove('apples') === true, 'remove reports success');
console.assert(table.remove('apples') === false, 'removing twice is a no-op');
console.assert(table.size === 2);
console.assert(table.keys().sort().join(',') === 'grapes,oranges', 'all keys');

// 2 buckets but 20 keys -> heavy chaining, everything still resolves
const crowded = new HashTable(2);
for (let i = 0; i < 20; i++) crowded.set(`key${i}`, i);
console.assert(crowded.size === 20, 'chained entries are all kept');
console.assert(crowded.keys().length === 20, 'keys() walks whole chains');
console.assert(
  [...Array(20).keys()].every((i) => crowded.get(`key${i}`) === i),
  'every chained key still resolves'
);

console.log('Success.');

module.exports = { HashTable };
