package CPSC331Assignment4;

import java.lang.NullPointerException;
import java.lang.IllegalArgumentException;

import CPSC331Assignment4.HashFunction;

/**
 *
 * A simple hash function, mapping values of type&nbsp;<code>K</code>
 * to nonnegative integers that are less than a specific upper limit
 * (the &ldquo;table size&rdquo;; each value in&nbsp;<code>K</code>
 * is mapped to its hashcode modulo the specified table size.
 *
 */

public class SimpleHashFunction<K> implements HashFunction<K>
{
	private int tableSize;
	
	public SimpleHashFunction(int m)
	{	
		tableSize = m;
	}

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

	public int tableSize()
	{
		return tableSize;
	}
	
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

	public int value (K key)
	{
		if (key != null)
		{
			int hash = key.hashCode()%tableSize;
			if (hash < 0)
			{
				return tableSize+hash;
			}
			else
			{
				return hash;
			}
		}
		else
		{
			throw new NullPointerException();
		}
	}
}
