package CPSC331Assignment3;

/**
*
* Interface for a simple map, for use in CPSC&nbsp;331.
* <br />
*
* <p>
*  Based loosely on the <code>Map</code> interface included in the Java
*  Collections Framework.
* </p>
*
* <p>
*  <strong>Interface Invariant:</strong>
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

public interface SimpleMap<K, V> {

  /**
   *
   * Returns the number of key-value pairs in this map.
   * <br />
   *
   * <p>
   *  <strong>Precondition:</strong> The intervace invariant is satisfied
   * </p>
   * <p>
   *  <strong>Postcondition:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The map is not changed (so the interface invariant
   *      is still satisfied) </li>
   * <li> The value returned is the number of key-value pairs
   *      in this map </li>
   * </ol>
   *
   * @return the number of key-value pairs in this map
   *
   */

  public int size();

  /**
   *
   * Returns the value to which the specified key is mapped,
   * or <code>null</code> if this map has no value associated to
   * the key.
   * <br />
   *
   * <p>
   *  <strong>Precondition 1:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The interface invariant is satisfied </li>
   * <li> The map includes a key-value pair for
   *      the specified non-<code>null</code>
   *      <code>key</code> </li>
   * </ol>
   * <p>
   *  <strong>Postcondition 1:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The map is not changed (so the interface invariant
   *      is still satisfied) </li>
   * <li> The value, that is mapped to the specified
   *      <code>key</code>, is returned. </li>
   * </ol>
   *
   * <p>
   *  <strong>Precondition 2:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The interface invariant is satisfied </li>
   * <li> The map does not include any key-value pair
   *      for the specified non-<code>null</code>
   *      <code>key</code> </li>
   * </ol>
   * <p>
   *  <strong>Postcondition 2:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The map is not changed (so the interface invariant
   *      is still satisfied) </li>
   * <li> <code>null</code> is returned </li>
   * </ol>
   *
   * <p>
   *  <strong>Precondition 3:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The interface invariant is satisfied </li>
   * <li> The input is <code>null</code> </li>
   * </ol>
   * <p>
   *  <strong>Postcondition 3:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The map is not changed (so the interface invariant
   *      is still satisfied) </li>
   * <li> A <code>NullPointerException</code> is thrown </li>
   * </ol>
   *
   * @param key the key whose associated value is to be returned
   * @return the value associated with the input <code>key</code>,
   *         or <code>null</code> if no value is associated with it
   * @throws NullPointerException if the specified key is <code>null</code>
   *
   */

  public V get(K key);

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
   * <li> The interface invariant is satisfied </li>
   * <li> A non-<code>null</code> key and a non-<code>null</code> value
   *      are supplied; the map already includes a key-value
   *      pair for the input key </li>
   * </ol>
   * <p>
   *  <strong>Postcondition 1:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The interface invariant is satisfied </li>
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
   * <li> The interface invariant is satisfied </li>
   * <li> A non-<code>null</code> key and a non-<code>null</code> value
   *      are supplied; the map does not already include a
   *      key-value pair with the input key </li>
   * </ol>
   * <p>
   *  <strong>Postcondition 2:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The interface invariant is satisfied </li>
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
   * <li> The interface invariant is satisfied </li>
   * <li> Either (or both) of the input key or value is <code>null</code> </li>
   * </ol>
   * <p>
   *  <strong>Postcondition 3:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The map is not changed (so the interface invariant
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

  public V put(K key, V value);

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
   * <li> The interface invariant is satisfied </li>
   * <li> The map includes a key-value pair for
   *      the specified non-<code>null</code>
   *      <code>key</code> </li>
   * </ol>
   * <p>
   *  <strong>Postcondition 1:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The interface invariant is satisfied </li>
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
   * <li> The interface invariant is satisfied </li>
   * <li> The map does not include any key-value pair
   *      for the specified non-<code>null</code>
   *      <code>key</code> </li>
   * </ol>
   * <p>
   *  <strong>Postcondition 2:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The map is not changed (so the interface invariant
   *      is still satisfied) </li>
   * <li> <code>null</code> is returned as output </li>
   * </ol>
   *
   * <p>
   *  <strong>Precondition 3:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The interface invariant is satisfied </li>
   * <li> The input is <code>null</code> </li>
   * </ol>
   * <p>
   *  <strong>Postcondition 3:</strong>
   * </p>
   * <ol style="list-style-type: lower-alpha">
   * <li> The map is not changed (so the interface invariant
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

  public V remove(K key);

}
