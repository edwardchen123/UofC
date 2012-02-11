package CPSC331Assignment4;

import java.lang.NullPointerException;
import java.lang.IndexOutOfBoundsException;
import java.lang.IllegalStateException;

import CPSC331Assignment3.Pair;

/**
 *
 * Interface for a hash table&rsquo;s data structure,
 * for use in CPSC&nbsp;331.
 * <br />
 *
 * <p>
 *  <strong>Interface Invariant:</strong>
 * <p>
 * <ul>
 * <li> Any implementing class will provide the data structure
 *      that serves as a component of a hash table storing
 *      key-value pairs, where keys are of type&nbsp;<code>K</code>
 *      and values are of type&nbsp;<code>V</code>
 * </li>
 * </ul>
 *
 * <p>
 *  <strong>Note:</strong> Any class implementing this interface
 *  must be used together with a class implementing a hash function.
 *  If an object in a class implementing this interface is used
 *  in combination with a consistent hash function &mdash; one
 *  associating the same hash value to a given <code>key</code>
 *  every time the function is evaluated at this <code>key</code>,
 *  then an object in a class implementing this interface should
 *  behave as a hash structure is expected to, that is, it should
 *  never contain more than one key-value pair for a given key,
 *  and searches for a given key will successfully find and report
 *  the key-value pair for it, if one exists.
 * </p>
 *
 * <p>
 *  This behaviour cannot be guaranteed if this object is combined
 *  with a hash function that is <i>not</i> consistent (that is,
 *  if it does not implement a function, at all)
 * </p>
 *
 * <p>
 *  <strong>Note:</strong>
 *  In order to allow the efficiency of various hashing
 *  structures and strategies to be analyzed, various properties
 *  of this structure can also be reported. Details about this
 *  are provided in the documentations of the methods that
 *  this interface provides.
 * </p>
 *
 */

public interface HashStructure<K, V> 
       extends Iterable<Pair<K, V>> {

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
   * <li> The interface invariant is satisfied. </li>
   * </ol>
   *
   * <p>
   *  <strong>Postcondition:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The hash structure has not been changed, so the interface
   *      invariant is still satisfied.
   * </li>
   * <li> The value returned is the length of the array that is
   *      the main part of the hash structure. For a hash table
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

   public int numberPositions();

  /**
   *
   * Reports the number of key-value pairs that are currently stored.
   * <br />
   *
   * <p>
   *  <strong>Precondition:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The interface invariant is satisfied. </li>
   * </ol>
   *
   * <p>
   *  <strong>Postcondition:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The hash structure has not been changed, so the interface
   *      invariant is still satisfied.
   * </li>
   * <li> The value returned is the number of key-value pairs that
   *      are currently stored in this structure.
   * </li>
   * </ol>
   *
   * @return the number of key-value pairs currently stored in
   *         this hash table
   *
   */

   public int size();

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
   * <li> The interface invariant is satisfied. </li>
   * <li> The structure is not empty, that is, at least
   *      one key-value pair is stored in it. </li>
   * </ol>
   *
   * <p>
   *  <strong>Postcondition 1:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The hash structure has not been changed, so the interface
   *      invariant is still satisfied.
   * </li>
   * <li> The maximum number of comparisons with keys, used for
   *      a successful search in this table, is returned. 
   *      The manner in which this is defined and computed depends
   *      on the type of hash structure that is implemented.
   * </li>
   * </ol>
   *
   * <p>
   *  <strong>Precondition 2:</strong>
   * </p>
   * <ol style = "list-style-type: lower-alpha">
   * <li> The interface invariant is satisfied. </li>
   * <li> The structure is empty, that is, no key-value pairs
   *      are stored in it. </li>
   * </ol>
   *
   * <p>
   *  <strong>Postcondition 2:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The hash structure has not changed, so the interface
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

   public int maxAccess();

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
   * <li> The interface invariant is satisfied. </li>
   * <li> The structure is not empty, that is, at least
   *      one key-value pair is stored in it. </li>
   * </ol>
   *
   * <p>
   *  <strong>Postcondition 1:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The hash structure has not been changed, so the interface
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
   * <li> The interface invariant is satisfied. </li>
   * <li> The structure is empty, that is, no key-value pairs
   *      are stored in it. </li>
   * </ol>
   *
   * <p>
   *  <strong>Postcondition 2:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The hash structure has not changed, so the interface
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

  public float eSuccess();

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
   * <li> The interface invariant is satisfied. </li>
   * </ol>
   *
   * <p>
   *  <strong>Postcondition:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The hash structure has not been changed, so the interface
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

  public float eFail();

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
   * <li> the interface invariant is satisfied </li>
   * <li> the given <code>key</code> is not <code>null</code> </li>
   * <li> the given <code>location</code> is valid, that is, it
   *      is a nonnegative integer that is less than the number 
   *      of positions in this hash structure </li>
   * <li> there is already a key-value pair with the given
   *      <code>key</code> that would be found by a search in this
   *      hash structure, assuming that the given <code>location</code>
   *      is this <code>key</code>&rsquo;s hash value </li>
   * </ol>
   *
   * <p>
   *  <strong>Postcondition 1:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> the hash structure is not changed, so the interface
   *      invariant is still satisfied </li>
   * <li> the <code>value</code> returned is the value associated with 
   *      the given <code>key</code> in the key-value pair that can
   *      be found, using the given <code>location</code> as the
   *      <code>key</code>&rsquo;s hash value </li>
   * </ol>
   *
   * <p>
   *  <strong>Precondition 2:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> the interface invariant is satisfied </li>
   * <li> the given <code>key</code> is not <code>null</code> </li>
   * <li> the given <code>location</code> is valid, that is, it
   *      is a nonnegative integer that is less than the number
   *      of positions in this hash structure </li>
   * <li> there is no key-value pair with the given
   *      <code>key</code> that would be found by a search in this
   *      hash structure, assuming that the given <code>location</code>
   *      is this <code>key</code>&rsquo;s hash value </li>
   * </ol>
   *
   * <p>
   *  <strong>Postcondition 2:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> the hash structure is not changed, so the interface
   *      invariant is still satisfied </li>
   * <li> <code>null</code> is returned as output </li>
   * </ol>
   *
   * <p>
   *  <strong>Precondition 3:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> the interface invariant is satisfied </li>
   * <li> the given <code>key</code> is <code>null</code> </li>
   * </ol>
   * 
   * <p>
   *  <strong>Postcondition 3:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> the hash structure is not changed, so the interface
   *      invariant is still satisfied </li>
   * <li> a <code>NullPointerException</code> is thrown </li>
   * </ol>
   *
   * <p>
   *  <strong>Precondition 4:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> the interface invariant is satisfied </li>
   * <li> the given <code>key</code> is not <code>null</code> </li>
   * <li> the given <code>location</code> is not valid, that is,
   *      it is either less than zero or greater than or equal to
   *      the number of positions in this hash structure </li>
   * </ol>
   *
   * <p>
   *  <strong>Postcondition 4:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> the hash structure is not changed, so the interface
   *      invariant is still satisfied </li>
   * <li> an <code>IndexOutOfBoundsException</code> is thrown </li>
   * </ol>
   *
   * @param key the key whose value is to be searched for
   * @param location the value of the hash function for the
   *          given key
   *
   * @return the value mapped to the given key, or <code>null</code>
   *          if no key-value pair for this key is found
   *
   * @throws NullPointerException if the given key is null
   * @throws IndexOutOfBoundsException is the given key is not
   *          null, but the given location is
   *          negative, or greater than or equal to the number of
   *          positions in this hash structure
   *
   */

   public V get(K key, int location);

  /**
   *
   * Associates the specified value with the specified key in this
   * map, using the specified location as the value of the hash
   * function for this key. If the map previously contained a value
   * for this key &mdash; and the same location has been consistently
   * supplied for it, so that this value can be found &mdash;
   * then the old value is replaced by the input value and the
   * the old value is returned as output. A new key-value pair is
   * inserted using the given hash table location, and <code>null</code>
   * is returned, otherwise
   * <br />
   *
   * <p>
   *  <strong>Precondition 1:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> the interface invariant is satisfied </li>
   * <li> the given <code>key</code> is not <code>null</code> </li>
   * <li> the given <code>location</code> is valid, that is, it
   *      is a nonnegative integer that is less than the number
   *      of positions in this hash structure </li>
   * <li> there is already a key-value pair with the given
   *      <code>key</code> that would be found by a search in this
   *      hash structure, assuming that the given <code>location</code>
   *      is this <code>key</code>&rsquo;s hash value </li>
   * </ol>
   *
   * <p>
   *  <strong>Postcondition 1:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> the key-value pair, with the given <code>key</code>,
   *      that could be found assuming that the given
   *      <code>location</code> is the <code>key</code>&rsquo;s
   *      hash value, is replaced by one (at the same location)
   *      with the same <code>key</code> and with the given
   *      <code>value</code> </li>
   * <li> no other changes are made to the hash strucrure, so
   *      the interface invariant is still satisfied </li>
   * <li> the value returned is the value that was formerly
   *      associated with the key and that has replaced (in the
   *      hash structure) with the <code>value</code> supplied
   *      as input </li>
   * </ol>
   *
   * <p>
   *  <strong>Precondition 2:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> the interface invariant is satisfied </li>
   * <li> the given <code>key</code> is not <code>null</code> </li>
   * <li> the given <code>location</code> is valid, that is, it
   *      is a nonnegative integer that is less than the number
   *      of positions in this hash structure </li>
   * <li> there is no key-value pair with the given
   *      <code>key</code> that would be found by a search in this
   *      hash structure, assuming that the given <code>location</code>
   *      is this <code>key</code>&rsquo;s hash value </li>
   * </ol>
   *
   * <strong>Postcondition 2:</strong>
   * <ol style="list-style-type: lower-alpha">
   * <li> a key-value pair, with the given <code>key</code>
   *      and <code>value,</code> is inserted into the structure
   *      assuming that the given location is the hash value for the
   *      <code>key</code> </li>
   * <li> no other changes are made, so that the interface
   *      invariant is still satisfied </li>
   * <li> <code>null</code> is returned as output </li>
   * </ol>
   *
   * <p>
   *  <strong>Precondition 3:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> the interface invariant is satisfied </li>
   * <li> one or both of the given <code>key</code> and the
   *      given <code>value</code> is <code>null</code> </li>
   * </ol>
   * 
   * <p>
   *  <strong>Postcondition 3:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> the hash structure is not changed, so the interface
   *      invariant is still satisfied </li>
   * <li> a <code>NullPointerException</code> is thrown </li>
   * </ol>
   *
   * <p>
   *  <strong>Precondition 4:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> the interface invariant is satisfied </li>
   * <li> the given <code>key</code> and <code>value</code>
   *      are not <code>null</code> </li>
   * <li> the given <code>location</code> is not valid, that is,
   *      it is either less than zero or greater than or equal to
   *      the number of positions in this hash structure </li>
   * </ol>
   *
   * <p>
   *  <strong>Postcondition 4:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> the hash structure is not changed, so the interface
   *      invariant is still satisfied </li>
   * <li> an <code>IndexOutOfBoundsException</code> is thrown </li>
   * </ol>
   *
   * @param key the key whose value is to be updated
   * @param location the value of the hash function for this key
   * @param value the value to be associated with this key in the
   *            mapping
   *
   * @return the value that was formerly associated with this key,
   *        if one is found using the specified key and location;
   *        <code>null,</code> otherwise
   *
   * @throws NullPointerException if the given key or value is null
   * @throws IndexOutOfBoundsException if the given key and value
   *        are not null, but the given location is
   *        negative, or greater than or equal to the number of positions
   *        in this structure
   *
   */

   public V put(K key, int location, V value);
   
  /**
   *
   * Removes the key-value pair if it is present and can be found
   * using the specified location.
   * <br />
   *
   * <p>
   *  <strong>Precondition 1:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> the interface invariant is satisfied </li>
   * <li> the given <code>key</code> is not <code>null</code> </li>
   * <li> the given <code>location</code> is valid, that is, it
   *      is a nonnegative integer that is less than the number
   *      of positions in this hash structure </li>
   * <li> there is already a key-value pair with the given
   *      <code>key</code> that would be found by a search in this
   *      hash structure, assuming that the given <code>location</code>
   *      is this <code>key</code>&rsquo;s hash value </li>
   * </ol>
   *
   * <p>
   *  <strong>Postcondition 1:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> the key-value pairing that includes the given
   *      <code>key</code>, mentioned in the above precondition,
   *      is removed from the structure, and the value included
   *      in this pair is returned as output </li>
   * <li> no other changes are made to the structure, so the
   *      interface invariant is still satisfied </li>
   * </ol>
   *
   * <p>
   *  <strong>Precondition 2:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> the interface invariant is satisfied </li>
   * <li> the given <code>key</code> is not <code>null</code> </li>
   * <li> the given <code>location</code> is valid, that is, it
   *      is a nonnegative integer that is less than the number
   *      of positions in this hash structure </li>
   * <li> there is no key-value pair with the given
   *      <code>key</code> that would be found by a search in this
   *      hash structure, assuming that the given <code>location</code>
   *      is this <code>key</code>&rsquo;s hash value </li>
   * </ol>
   *
   * <strong>Postcondition 2:</strong>
   * <ol style="list-style-type: lower-alpha">
   * <li> the hash structure is not changed, so the interface
   *      invariant is still satisfied </li>
   * <li> <code>null</code> is returned as output </li>
   * </ol>
   *
   * <p>
   *  <strong>Precondition 3:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> the interface invariant is satisfied </li>
   * <li> the given <code>key</code> is <code>null</code> </li>
   * </ol>
   * 
   * <p>
   *  <strong>Postcondition 3:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> the hash structure is not changed, so the interface
   *      invariant is still satisfied </li>
   * <li> a <code>NullPointerException</code> is thrown </li>
   * </ol>
   *
   * <p>
   *  <strong>Precondition 4:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> the interface invariant is satisfied </li>
   * <li> the given <code>key</code> is not <code>null</code> </li>
   * <li> the given <code>location</code> is not valid, that is,
   *      it is either less than zero or greater than or equal to
   *      the number of positions in this hash structure </li>
   * </ol>
   *
   * <p>
   *  <strong>Postcondition 4:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> the hash structure is not changed, so the interface
   *      invariant is still satisfied </li>
   * <li> an <code>IndexOutOfBoundsException</code> is thrown </li>
   * </ol>
   *
   * @param key the key whose key-value pair is to removed
   * @param location the value of the hash function for this key
   *
   * @return the value of the key that was found in the mapping using
   *        the specified key and location, of <code>null</code>
   *        if no key-value pair was found
   *
   * @throws NullPointerException if the given key is null
   * @throws IndexOutOfBoundsException if the given key and value
   *        are not null, but the given location is
   *        negative, or greater than or equal to the number of
   *        positions in this hash structure
   *
   */
 
  public V remove(K key, int location);

}
