package CPSC331Assignment4;

import java.util.Iterator;
import java.lang.Iterable;
import java.lang.IllegalArgumentException;
import java.lang.NullPointerException;

import CPSC331Assignment3.SimpleMap;
import CPSC331Assignment3.Pair;
import CPSC331Assignment4.HashFunction;
import CPSC331Assignment4.HashStructure;

/**
 *
 * Class providing a simple map, implemented using a hash table.
 * <br />
 *
 * <p>
 *  <strong>Class Invariant:</strong>
 * </p>
 * <ul>
 * <li> Access is provided to a finite map. Keys are chosen
 *      from some type&nbsp;<code>K</code>, and values from
 *      some type&nbsp;<code>V</code>.
 * </li>
 * <li> If a mapping from a key&nbsp;<code>k</code> to a
 *      value&nbsp;<code>v</code> is included then
 *      neither&nbsp;<code>k</code> nor&nbsp;<code>v</code>
 *      is&nbsp;<code>null</code>.
 * </li>
 * </ol>
 *
 */

public class SimpleHashTable<K, V>
             implements SimpleMap<K, V>, Iterable<Pair<K, V>>  {

  protected HashFunction<K> function;
  protected HashStructure<K, V> structure;

  /**
   *
   * Constructor of a hash table using a given hash function
   * and hash structure.
   * <br />
   *
   * <p>
   *  <strong>Precondition 1:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The given function and structure are consistent, that
   *      is, each corresponds to the same table size
   * </li>
   * </ol>
   *
   * <p>
   *  <strong>Postcondition 1:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> A new empty hash table, using the given function and
   *      structure, is created
   * </li>
   * </ol>
   *
   * <p>
   *  <strong>Precondition 2:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The given function and structure are not consistent,
   *      that is, they do not correspond to the same table size
   * </li>
   * </ol>
   *
   * <p>
   *  <strong>Postcondition 2:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> An <code>IllegalArgumentException</code> is thrown
   *      and a hash table is not created
   * </li>
   * </ol>
   *
   * @param function the HashFunction to be used
   * @param structure the HashStructure to be used
   * @throws IllegalArgumentException if the function and
   *         structure do not correspond to the same table size
   *
   */

  public SimpleHashTable( HashFunction<K> function,
                          HashStructure<K, V> structure ) {

    if (function.tableSize() == structure.numberPositions()) {

      this.function = function;
      this.structure = structure;

    } else {

      throw new IllegalArgumentException();

    };

  }

  /**
   *
   * Reports the length of the array that is the main part of this
   * hash table (which would also be the maximum size if open hashing
   * was being used).
   * <br />
   *
   * <p>
   *  <strong>Precondition:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The class invariant is satisfied. </li>
   * </ol>
   *
   * <p>
   *  <strong>Postcondition:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The hash table has not been changed, so the class
   *      invariant is still satisfied.
   * </li>
   * <li> The value returned is the length of the array that is
   *      the main part of the hash table. For a hash table
   *      with chaining, this would be equal to the number of
   *      different linked lists that are used to store key-value
   *      pairs. For a hash table with open hashing this would
   *      be equal to the &ldquo;capacity&rdquo; of the structure,
   *      that is, the maximum number of key-value pairs that
   *      could possibly be stored.
   * </li>
   * </ol>
   *
   * @return the length of the array that is the main part of this
   *         hash table
   *
   */

  public int numberPositions() {

    return structure.numberPositions();

  }

  /**
   *
   * Reports the number of key-value pairs that are currently stored.
   * <br />
   *
   * <p>
   *  <strong>Precondition:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The class invariant is satisfied. </li>
   * </ol>
   *
   * <p>
   *  <strong>Postcondition:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The hash table has not been changed, so the class
   *      invariant is still satisfied.
   * </li>
   * <li> The value returned is the number of key-value pairs that
   *      are currently stored in this hash table.
   * </li>
   * </ol>
   *
   * @return the number of key-value pairs currently stored in
   *         this hash table
   *
   */

  public int size() {

    return structure.size();

  }

  /**
   *
   * Reports the maximum number of comparison with keys used
   * for a successful search in this table.
   * <br />
   *
   * <p>
   *  <strong>Precondition 1:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The class invariant is satisfied. </li>
   * <li> The hash table is not empty, that is, at least
   *      one key-value pair is stored in it. </li>
   * </ol>
   *
   * <p>
   *  <strong>Postcondition 1:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The hash table has not been changed, so the class
   *      invariant is still satisfied.
   * </li>
   * <li> The maximum number of comparisons with keys, used for
   *      a successful search in this table, is returned.
   *      The manner in which this is defined and computed depends
   *      on the type of hash table that is implemented.
   * </li>
   * </ol>
   *
   * <p>
   *  <strong>Precondition 2:</strong>
   * </p>
   * <ol style = "list-style-type: lower-alpha">
   * <li> The class invariant is satisfied. </li>
   * <li> The hash table is empty, that is, no key-value pairs
   *      are stored in it. </li>
   * </ol>
   *
   * <p>
   *  <strong>Postcondition 2:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The hash table has not changed, so the class
   *      invariant is still satisfied.
   * </li>
   * <li> An <code>IllegalStateException</code> is thrown
   * </li>
   * </ol>
   *
   * @return the maximum number of comparisons with keys used
   *         for a successful search in this table
   * @throws IllegalStateException if the table is empty
   *
   */

  public int maxAccess() {

    return structure.maxAccess();

  }

  /**
   *
   * Reports the expected number of comparisons with keys used
   * for a successful search in this table, assuming that one
   * searches for each value that is currently stored.
   * <br />
   *
   * <p>
   *  <strong>Precondition 1:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The class invariant is satisfied. </li>
   * <li> The hash table is not empty, that is, at least
   *      one key-value pair is stored in it. </li>
   * </ol>
   *
   * <p>
   *  <strong>Postcondition 1:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The hash table has not been changed, so the class
   *      invariant is still satisfied.
   * </li>
   * <li> The expected number of comparisons with keys, used for
   *      a successful search in this table, is returned. It is
   *      assumed that that all keys (currently in the table)
   *      are searched for with the same probability, so that this
   *      value is equal to the sum of the numbers of comparisons
   *      needed to search for each of the keys, divided by the
   *      number of keys that are currently stored.
   * </li>
   * </ol>
   *
   * <p>
   *  <strong>Precondition 2:</strong>
   * </p>
   * <ol style = "list-style-type: lower-alpha">
   * <li> The class invariant is satisfied. </li>
   * <li> The hash table is empty, that is, no key-value pairs
   *      are stored in it. </li>
   * </ol>
   *
   * <p>
   *  <strong>Postcondition 2:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The hash table has not changed, so the interface
   *      invariant is still satisfied.
   * </li>
   * <li> An <code>IllegalStateException</code> is thrown
   * </li>
   * </ol>
   *
   *
   * @return the expected number of comparisons with keys used
   *         for a sucessful search
   * @throws IllegalStateException if the table is empty
   *
   */

  public float eSuccess() {

    return structure.eSuccess();

  }

  /**
   *
   * Reports the expected number of comparisons (including a
   * final comparison with a null key) for an unsuccessful
   * search, assuming that the hash value of the given key
   * is equal to each of the hash table locations with the
   * same probability.
   * <br />
   *
   * <p>
   *  <strong>Precondition:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The class invariant is satisfied. </li>
   * </ol>
   *
   * <p>
   *  <strong>Postcondition:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The hash table has not been changed, so the class
   *      invariant is still satisfied.
   * </li>
   * <li> The expected number of comparisons with keys, used for
   *      an unsuccessful search in this table, is returned. It is
   *      assumed here, for an unsuccessful search, that the value
   *      of the hash function (that is, the initial hash table
   *      position searched) is equal to each of the possible
   *      hash table positions with the same probability.
   * </li>
   * </ol>
   *
   * @return the expected number of comparisons with keys
   *         (including a null value) for an unsuccessful search
   *
   */

  public float eFail() {

    return structure.eFail();

  }

  /**
   *
   * Returns the value corresponding to a given key or null if
   * no key-value pair for the given key is currently stored.
   * <br />
   *
   * <p>
   *  <strong>Precondition 1:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> the class invariant is satisfied </li>
   * <li> the given <code>key</code> is not <code>null</code> </li>
   * <li> there is already a key-value pair with the given
   *      <code>key</code> in the hash table </li>
   * </ol>
   *
   * <p>
   *  <strong>Postcondition 1:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> the hash table is not changed, so the class
   *      invariant is still satisfied </li>
   * <li> the <code>value</code> returned is the value associated with
   *      the given <code>key</code> </li>
   * </ol>
   *
   * <p>
   *  <strong>Precondition 2:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> the interface invariant is satisfied </li>
   * <li> the given <code>key</code> is not <code>null</code> </li>
   * <li> there is no key-value pair with the given
   *      <code>key</code> in the hash table </li>
   * </ol>
   *
   * <p>
   *  <strong>Postcondition 2:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> the hash table is not changed, so the class
   *      invariant is still satisfied </li>
   * <li> <code>null</code> is returned as output </li>
   * </ol>
   *
   * <p>
   *  <strong>Precondition 3:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> the class invariant is satisfied </li>
   * <li> the given <code>key</code> is <code>null</code> </li>
   * </ol>
   *
   * <p>
   *  <strong>Postcondition 3:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> the hash table is not changed, so the class
   *      invariant is still satisfied </li>
   * <li> a <code>NullPointerException</code> is thrown </li>
   * </ol>
   *
   * @param key the key whose value is to be searched for
   *
   * @return the value mapped to the given key, or <code>null</code>
   *          if no key-value pair for this key is found
   *
   * @throws NullPointerException if the given key is null
   *
   */

  public V get(K key) {

    return structure.get(key, function.value(key));

  }

  /**
   *
   * Associates the specified value with the specified key in this
   * map. If the map previously contained a value for this key,
   * the old value is replaced by the input value and the old value
   * is returned as output
   * <br />
   *
   * <p>
   *  <strong>Precondition 1:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The class invariant is satisfied </li>
   * <li> A non-<code>null</code> key and a non-<code>null</code> value
   *      are supplied; the map already includes a key-value
   *      pair for the input key </li>
   * </ol>
   * <p>
   *  <strong>Postcondition 1:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The class invariant is satisfied </li>
   * <li> The key-value pair that included the input key has been replaced
   *      by a key-value pair with the input key and the input value;
   *      the map is otherwise unchanged </li>
   * <li> The old value, that was associated with the input key before
   *      this operation was performed, is returned as output </li>
   * </ol>
   *
   * <p>
   *  <strong>Precondition 2:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The class invariant is satisfied </li>
   * <li> A non-<code>null</code> key and a non-<code>null</code> value
   *      are supplied; the map does not already include a
   *      key-value pair with the input key </li>
   * </ol>
   * <p>
   *  <strong>Postcondition 2:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The class invariant is satisfied </li>
   * <li> A key-value pair associating the input value to the
   *      input key have been added to the map, which is otherwise
   *      unchanged </li>
   * <li> <code>null</code> is returned as output </li>
   * </ol>
   *
   * <p>
   *  <strong>Precondition 3:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The class invariant is satisfied </li>
   * <li> Either (or both) of the input key or value is
   *      <code>null</code> </li>
   * </ol>
   * <p>
   *  <strong>Postcondition 3:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The map is not changed (so the class invariant
   *      is still satisfied) </li>
   * <li> A <code>NullPointerException</code> is thrown </li>
   * </ol>
   *
   * @param key to which the specified value is to be associated
   * @param value value to be associated with the specified key
   * @return the previous value associated with the input key,
   *         of <code>null</code> if there was no key-value
   *         pair for this key before this operation was performed
   * @throws NullPointerException if either the specified key
   *         of the specified value (or both) is <code>null</code>
   *
   */

  public V put(K key, V value) {

    return structure.put(key, function.value(key), value);

  }

  /**
   *
   * Removes the key-value pair for a key from this map if it
   * is present.
   * <br />
   *
   * <p>
   *  <strong>Precondition 1:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The class invariant is satisfied </li>
   * <li> The map includes a key-value pair for
   *      the specified non-<code>null</code>
   *      <code>key</code> </li>
   * </ol>
   * <p>
   *  <strong>Postcondition 1:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The class invariant is satisfied </li>
   * <li> The key-value pair for the specified <code>key</code>
   *      is removed from the map, which is othewise unchanged </li>
   * <li> The value that was associated with the specified
   *      <code>key</code> before the operation was performed is
   *      returned as output </li>
   * </ol>
   *
   * <p>
   *  <strong>Precondition 2:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The class invariant is satisfied </li>
   * <li> The map does not include any key-value pair
   *      for the specified non-<code>null</code>
   *      <code>key</code> </li>
   * </ol>
   * <p>
   *  <strong>Postcondition 2:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The map is not changed (so the class invariant
   *      is still satisfied) </li>
   * <li> <code>null</code> is returned as output </li>
   * </ol>
   *
   * <p>
   *  <strong>Precondition 3:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The class invariant is satisfied </li>
   * <li> The input is <code>null</code> </li>
   * </ol>
   * <p>
   *  <strong>Postcondition 3:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The map is not changed (so the class invariant
   *      is still satisfied) </li>
   * <li> A <code>NullPointerException</code> is thrown </li>
   * </ol>
   *
   * @param key the key that is to be removed from the map
   * @return the value that was associated with the input <code>key</code>,
   *         or <code>null</code> if no key-value pair for the
   *         <code>key</code> was found
   * @throws NullPointerException if the specified key is <code>null</code>
   *
   */

  public V remove(K key) {

    return structure.remove(key, function.value(key));

  }

  /**
   *
   * Returns an iterator for the key-value pairs stored in this
   * hash tablel
   * <br />
   *
   * <p>
   *  No ordering of values is assumed. At most a constant number of
   *  steps are used to determine whether additional key-value
   *  pairs remain to be listed or to list the next pair
   * </p>
   *
   * <p>
   *  <strong>Precondition:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The class invariant is satisfied </li>
   * </ol>
   *
   * <p>
   *  <strong>Postcondition:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The map, and hash table, are unchanged, so the class
   *      invariant is still satisfied </li>
   * <li> A new iterator is created and returned </li>
   * </ol>
   *
   * @return an iterator over the key-values pairs in this map
   *
   */

  public Iterator<Pair<K, V>> iterator() {

    return structure.iterator();

  } 

}

