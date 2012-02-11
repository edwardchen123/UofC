package CPSC331Assignment4;

import java.lang.NullPointerException;

/**
 *
 * Interface for a hash function, for use in CPSC&nbsp;331.
 * <br />
 *
 * <p>
 *  Access is provided to a hash function mapping elements
 *  of type&nbsp;<code>K</code> to nonnegative integers
 *  less than are equal to a specific upper limit that
 *  is set when this object is constructed.
 * </p>
 *
 * <p>
 *  While it is possible that randomization might be used
 *  when an object is constructed (so that, in particular,
 *  &ldquo;universal hashing&rdquo; can be supported),
 *  the function&rsquo;s behaviour must be deterministic
 *  after that, in order to ensure that each key is
 *  consistently mapped to the same location by this
 *  hash function.
 * </p>
 *
 */

public interface HashFunction<K> {

  /**
   *
   * Returns the number of positions in a hash table for which
   * this function can be used; function values should be
   * nonnegative integers that are less than this value.
   * <br />
   *
   * @return the number of positions in a hash table for which
   *         this function can be used
   *
   */

   public int tableSize();

  /**
   *
   * Provides the value of this function for the given key.
   * <br />
   *
   * <p>
   *  <strong>Precondition 1:</strong> A non-null value is supplied
   *  as input.
   * </p>
   * <p>
   *  <strong>Postcondition 1:</strong> The value of the hash function
   *    for this input value is returned; this is an integer
   *    between&nbsp;0 and this.tableSize() &minus; 1.
   * </p>
   *
   * <p>
   *  <strong>Precondition 2:</strong> A null input is supplied.
   * </p>
   * <p>
   *  <strong>Postcondition 2:</strong> A <code>NullPointerException</code>
   *  is thrown.
   * </p>
   *
   * @param key the key whose hash table location is to be computed
   * @return the location of the given key
   * @throws NullPointExeption if the input is null
   *
   */

   public int value(K key);

}
