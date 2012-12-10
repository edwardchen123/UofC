package CPSC331Assignment4;

import java.util.ArrayList;
import java.util.Iterator;
import java.lang.Iterable;
import java.lang.NullPointerException;
import java.lang.IndexOutOfBoundsException;
import java.lang.IllegalArgumentException;
import java.lang.IllegalStateException;
import java.util.NoSuchElementException;

import CPSC331Assignment3.Pair;
import CPSC331Assignment4.HashStructure;

/**
 *
 * Hash structure to support hashing with chaining.
 * <br />
 *
 * <p>
 *  <strong>Class Invariant:</strong>
 * </p>
 * <ul>
 * <li> This provides the data structure that serves as a component
 *      of a hash table with chaining, storing key-value pairs
 *      where keys are of type&nbsp;<code>K</code> and values are
 *      of type&nbsp;<code>V</code>
 * </li>
 * </ul>
 *
 * <p>
 *  <strong>Note:</strong> Any object in this class
 *  must be used together with an object in a class implementing
 *  a hash function. If an object in this class is used
 *  in combination with a consistent hash function &mdash; one
 *  associating the same hash value to a given <code>key</code>
 *  every time the function is evaluated at this <code>key</code>,
 *  then an object in this class should behave as a hash structure
 *  is expected to, that is, it should never contain more than one
 *  key-value pair for a given key, and searches for a given key will
 *  successfully find and report the key-value pair for it, if one exists.
 * </p>
 *
 * <p>
 *  This behaviour cannot be guaranteed if this object is combined
 *  with a hash function that is <i>not</i> consistent (that is,
 *  if it does not implement a function, at all)
 * </p>
 *
 */

public class HashStructureWithChaining<K, V> implements HashStructure<K, V>
{
	/* * *
	 * A custom-written Node class, left undocumented as it is private
	 * * */	
	
	private class Node
	{
		private K key;
		private V value;
		private LinkedList tableIndex;
		private Node next;
		private Node previous;

		protected Node(K newKey, V newValue)
		{
			key = newKey;
			value = newValue;
			tableIndex = null;
			next = null;
			previous = null;
		}
		
		protected Node(LinkedList newTableIndex)
		{
			key = null;
			value = null;
			tableIndex = newTableIndex;
			next = null;
			previous = null;
		}

		protected K getKey()
		{
			return key;
		}

		protected V getValue()
		{
			return value;
		}

		protected void setValue(V newValue)
		{
			value = newValue;
		}
		
		protected LinkedList getTableIndex()
		{
			return tableIndex;
		}

		protected Node getNext()
		{
			return next;
		}

		protected void setNext(Node newNext)
		{
			next = newNext;
		}
		
		protected Node getPrevious()
		{
			return previous;
		}

		protected void setPrevious(Node newPrevious)
		{
			previous = newPrevious;
		}
	}

	/* * *
	 * A custom-written LinkedList class, left undocumented as it is private
	 * * */

	private class LinkedList
	{
		private Node root;
		private Node occupiedIndex;
		private int size;

		protected LinkedList()
		{
			root = null;
			occupiedIndex = null;
			size = 0;
		}
		
		protected Node getRoot()
		{
			return root;
		}
		
		protected Node getOccupiedIndex()
		{
			return occupiedIndex;
		}
		
		protected void setOccupiedIndex(Node newNode)
		{
			occupiedIndex = newNode;
		}
		
		protected int getSize()
		{
			return size;
		}
		
		protected V getNode(K key)
		{
			Node currentNode = traverse(key);
			if (currentNode != null)
			{
				return currentNode.getValue();
			}
			else
			{
				return null;
			}
		}	
		
		protected V putNode(K newKey, V newValue)
		{
			Node currentNode = traverse(newKey);
			if (currentNode != null)
			{
				V oldValue = currentNode.getValue();
				currentNode.setValue(newValue);
				return oldValue;
			}
			else
			{
				Node newNode = new Node(newKey, newValue);
				Node oldRoot = root;
				root = newNode;
				newNode.setNext(oldRoot);
				if (oldRoot != null)
				{
					oldRoot.setPrevious(newNode);
				}
				size++;
				return null;
			}
		}
		
		protected Node putNode(LinkedList tableIndex)
		{
			Node newNode = new Node(tableIndex);
			Node oldRoot = root;
			root = newNode;
			newNode.setNext(oldRoot);
			if (oldRoot != null)
			{
				oldRoot.setPrevious(newNode);
			}
			size++;
			return newNode;
		}
		
		protected V removeNode(K key)
		{
			Node currentNode = traverse(key);
			if (currentNode != null)
			{
				if (currentNode == root)
				{
					root = currentNode.getNext();
				}
				else
				{
					(currentNode.getPrevious()).setNext(currentNode.getNext());
				}
				if (currentNode.getNext() != null)
				{
					(currentNode.getNext()).setPrevious(currentNode.getPrevious());
				}
				size--;
				return currentNode.getValue();
			}
			else
			{
				return null;
			}
		}
		
		protected void removeNode(Node currentNode)
		{
			if (currentNode == root)
			{
				root = currentNode.getNext();
			}
			else
			{
				(currentNode.getPrevious()).setNext(currentNode.getNext());
			}
			if (currentNode.getNext() != null)
			{
				(currentNode.getNext()).setPrevious(currentNode.getPrevious());
			}
			size--;
		}
		
		protected Node traverse(K key)
		{
			Node currentNode = root;
			while (currentNode != null)
			{
				if ((currentNode.getKey()).equals(key))
				{
					return currentNode;
				}
				else
				{
					currentNode = currentNode.getNext();
				}
			}
			return null;
		}
	}

	/**
	*
	* An iterator for this hash structure.
	* <br />
	*
	*/

	public class ChainingIterator implements Iterator<Pair<K, V>>
	{	
		private Node currentChain;	// The current chain being iterated through
		private Node nextNode;		// The next node to be iterated through on the current chain
		
		public ChainingIterator()
		{
			currentChain = occupied.getRoot();
			if (currentChain != null)
			{
				nextNode = (currentChain.getTableIndex()).getRoot();
			}
			else
			{
				nextNode = null;
			}
		}
		
		/**
		*
		* Returns a boolean value that depends on whether there exists
		* an as-yet-untraversed node on the map
		* <br />
		*
		* <p>
		*  <strong>Precondition 1:</strong>
		* </p>
		* <ol style="list-style-type: lower-alpha">
		* <li>	The class invariant is satisfied </li>
		* <li>	There exists no node in the map that has not
		*	been traversed. </li>
		* </ol>	
		* <p>
		*  <strong>Postcondition 1:</strong>
		* </p>
		* <ol style="list-style-type: lower-alpha">
		* <li> The map is not changed (so the class invariant
		*      is still satisfied) </li>
		* <li> <code>false</code> is returned
		* </ol>
		* <p>
		*  <strong>Precondition 2:</strong>
		* </p>
		* <ol style="list-style-type: lower-alpha">
		* <li>	The class invariant is satisfied </li>
		* <li>	There exists at least one node in the map that has
		*	not yet been traversed </li>
		* </ol>	
		* <p>
		*  <strong>Postcondition 2:</strong>
		* </p>
		* <ol style="list-style-type: lower-alpha">
		* <li> The map is not changed (so the class invariant
		*      is still satisfied) </li>
		* <li> <code>true</code> is returned
		* </ol>
		*
		* @return <code>boolean:</code> nextNode is not null
		*
		*/
		
		public boolean hasNext()
		{
			if (currentChain == null || currentChain.getNext() == null && nextNode == null)
				return false;
			else
				return true;
		}

		/**
		*
		* Returns the next node in the chain currently being iterated
		* through, or the first node on the next chain to be iterated through.
		* <br />
		*
		* <p>
		*  <strong>Precondition 1:</strong>
		* </p>
		* <ol style="list-style-type: lower-alpha">
		* <li>	The class invariant is satisfied </li>
		* <li>	There exists no node in the map that has not
		*	been traversed. </li>
		* </ol>	
		* <p>
		*  <strong>Postcondition 1:</strong>
		* </p>
		* <ol style="list-style-type: lower-alpha">
		* <li> The map is not changed (so the class invariant
		*      is still satisfied) </li>
		* <li> NoSuchElementException is thrown
		* </ol>
		* <p>
		*  <strong>Precondition 2:</strong>
		* </p>
		* <ol style="list-style-type: lower-alpha">
		* <li>	The class invariant is satisfied </li>
		* <li>	There exists at least one node in the map that has
		*	not yet been traversed </li>
		* </ol>	
		* <p>
		*  <strong>Postcondition 2:</strong>
		* </p>
		* <ol style="list-style-type: lower-alpha">
		* <li> The map is not changed (so the class invariant
		*      is still satisfied) </li>
		* <li> nextNode is returned and nextNode is set either to the
		*	next node in the chain or the first node of the next chain
		* </ol>
		*
		* @return the next node
		* @throws NoSuchElementException if there exists no node that 
		*	has not yet been traversed
		*
		*/

		public Pair<K, V> next()
		{
			if (nextNode != null)
			{
				Pair<K,V> current = new Pair<K,V>(nextNode.getKey(),nextNode.getValue());
				if (nextNode.getNext() == null && currentChain.getNext() != null)
				{
					nextNode = ((currentChain.getNext()).getTableIndex()).getRoot();
					currentChain = currentChain.getNext();
				}
				else if (nextNode.getNext() == null && currentChain.getNext() == null)
				{
					nextNode = null;
				}
				else
				{
					nextNode = nextNode.getNext();
				}
				return current;
			}
			else
			{
				throw new NoSuchElementException();
			}
		}

		/**
		*
		* This operation is not supported.
		*
		*/

		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}

	private ArrayList<LinkedList> hashTable;	// A hash table that supports chaining
	private ArrayList<Integer> lengthList;		// An ArrayList that keeps track of the various lengths of the chains
	protected LinkedList occupied;			// A LinkedList that keeps track of the occupied buckets in the hash table
	private int successSum;				// A sum that when divided by the number of pairs will return eSuccess
	private int storedValues;			// The number of key/value pairs stored in the map

  	public HashStructureWithChaining(int numberPositions)
	{
		hashTable = new ArrayList<LinkedList>(numberPositions);
		for (int i = 0; i < numberPositions; i++)
		{
			hashTable.add(new LinkedList());
		}
		lengthList = new ArrayList<Integer>();
		lengthList.add(0,numberPositions);
		occupied = new LinkedList();
		successSum = 0;
		storedValues = 0;
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
	* <li> The hash structure has not been changed, so the class
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

	public int numberPositions()
	{
		return hashTable.size();
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
	* <li> The hash structure has not been changed, so the class
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

	public int size()
	{
		return storedValues;
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
	* <li> The structure is not empty, that is, at least
	*      one key-value pair is stored in it. </li>
	* </ol>
	*
	* <p>
	*  <strong>Postcondition 1:</strong>
	* </p>
	* <ol style="list-style-type: lower-alpha">
	* <li> The hash structure has not been changed, so the class
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
	* <li> The class invariant is satisfied. </li>
	* <li> The structure is empty, that is, no key-value pairs
	*      are stored in it. </li>
	* </ol>
	*
	* <p>
	*  <strong>Postcondition 2:</strong>
	* </p>
	* <ol style="list-style-type: lower-alpha">
	* <li> The hash structure has not changed, so the class
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

	public int maxAccess()
	{
		if (storedValues > 0)
		{
			return lengthList.size()-1;
		}
		else
		{
			throw new IllegalStateException();
		}
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
	* <li> The structure is not empty, that is, at least
	*      one key-value pair is stored in it. </li>
	* </ol>
	*
	* <p>
	*  <strong>Postcondition 1:</strong>
	* </p>
	* <ol style="list-style-type: lower-alpha">
	* <li> The hash structure has not been changed, so the class
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
	* <li> The structure is empty, that is, no key-value pairs
	*      are stored in it. </li>
	* </ol>
	*
	* <p>
	*  <strong>Postcondition 2:</strong>
	* </p>
	* <ol style="list-style-type: lower-alpha">
	* <li> The hash structure has not changed, so the class
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

	public float eSuccess()
	{
		if (storedValues > 0)
		{
			float values = storedValues;
			return successSum / values;
		}
		else
		{
			throw new IllegalStateException();
		}
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
	* <li> The hash structure has not been changed, so the class
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

	public float eFail()
	{
		if (hashTable.size() > 0)
		{
			float tableSize = hashTable.size();
			float values = storedValues;
			return (tableSize + values) / tableSize;
		}
		else
		{
			float zero = 0;
			return zero;
		}
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
	* <li> the hash structure is not changed, so the class
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
	* <li> the class invariant is satisfied </li>
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
	* <li> the hash structure is not changed, so the class
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
	* <li> the hash structure is not changed, so the class
	*      invariant is still satisfied </li>
	* <li> a <code>NullPointerException</code> is thrown </li>
	* </ol>
	*
	* <p>
	*  <strong>Precondition 4:</strong>
	* </p>
	* <ol style="list-style-type: lower-alpha">
	* <li> the class invariant is satisfied </li>
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
	* <li> the hash structure is not changed, so the class
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

	public V get(K key, int location)
	{
		if (key == null)
		{
			throw new NullPointerException();
		}
		else if (location < 0 || location >= hashTable.size())
		{
			throw new IndexOutOfBoundsException();
		}
		else
		{
			return (hashTable.get(location)).getNode(key);
		}
	}

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
	* <li> the class invariant is satisfied </li>
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
	*      the class invariant is still satisfied </li>
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
	* <li> the class invariant is satisfied </li>
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
	* <li> no other changes are made, so that the class
	*      invariant is still satisfied </li>
	* <li> <code>null</code> is returned as output </li>
	* </ol>
	*
	* <p>
	*  <strong>Precondition 3:</strong>
	* </p>
	* <ol style="list-style-type: lower-alpha">
	* <li> the class invariant is satisfied </li>
	* <li> one or both of the given <code>key</code> and the
	*      given <code>value</code> is <code>null</code> </li>
	* </ol>
	* 
	* <p>
	*  <strong>Postcondition 3:</strong>
	* </p>
	* <ol style="list-style-type: lower-alpha">
	* <li> the hash structure is not changed, so the class
	*      invariant is still satisfied </li>
	* <li> a <code>NullPointerException</code> is thrown </li>
	* </ol>
	*
	* <p>
	*  <strong>Precondition 4:</strong>
	* </p>
	* <ol style="list-style-type: lower-alpha">
	* <li> the class invariant is satisfied </li>
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
	* <li> the hash structure is not changed, so the class
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

	public V put(K key, int location, V value)
	{	
		if (key == null || value == null)
		{
			throw new NullPointerException();
		}
		else if (location < 0 || location >= numberPositions())
		{
			throw new IndexOutOfBoundsException();
		}
		else
		{
			LinkedList chain = hashTable.get(location); // Creates a pointer to the chain
			int oldLength = chain.getSize(); // Records the original length of the chain
			V oldValue = chain.putNode(key, value); // Attempts to add a pair to the chain
			if (oldValue == null) // True if a node was added to the chain
			{
				// Increases the counter of key/value pairs in the map by one
				storedValues++;
				
				// Updates successSum, the numerator of the equation used in eSuccess.
				int newLength = chain.getSize();
				successSum = successSum + newLength;
				
				// Updates the lengthList array
				lengthList.set(oldLength,lengthList.get(oldLength)-1);
				if (oldLength == lengthList.size()-1)
				{
					lengthList.add(1);
				}
				else
				{
					lengthList.set(oldLength+1,lengthList.get(oldLength+1)+1);
				}
				
				// Updates the list of occupied buckets
				Node currentNode = chain.getOccupiedIndex();
				if (currentNode == null) // Adds the chain to occupied if it is not yet in it
				{
					currentNode = occupied.putNode(chain);
					chain.setOccupiedIndex(currentNode);
				}
			}
			return oldValue;
		}
	}

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
	* <li> the class invariant is satisfied </li>
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
	*      class invariant is still satisfied </li>
	* </ol>
	*
	* <p>
	*  <strong>Precondition 2:</strong>
	* </p>
	* <ol style="list-style-type: lower-alpha">
	* <li> the class invariant is satisfied </li>
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
	* <li> the hash structure is not changed, so the class
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
	* <li> the hash structure is not changed, so the class
	*      invariant is still satisfied </li>
	* <li> a <code>NullPointerException</code> is thrown </li>
	* </ol>
	*
	* <p>
	*  <strong>Precondition 4:</strong>
	* </p>
	* <ol style="list-style-type: lower-alpha">
	* <li> the class invariant is satisfied </li>
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
	* <li> the hash structure is not changed, so the class
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

	public V remove(K key, int location)
	{
		if (key == null)
		{
			throw new NullPointerException();
		}
		else if (location < 0 || location >= numberPositions())
		{
			throw new IndexOutOfBoundsException();
		}
		else
		{
			LinkedList chain = hashTable.get(location); // Creates a pointer to the chain
			int oldLength = chain.getSize(); // Records the original length of the chain
			V oldValue = chain.removeNode(key); // Attempts to remove a pair from the chain
			if (oldValue != null) // True if a node was successfully removed from the chain
			{
				// Decreases the counter of key/value pairs in the map by one
				storedValues--;
				
				// Updates successSum, the numerator of the equation used in eSuccess.
				successSum = successSum - oldLength;
				
				// Updates the lengthList array
				lengthList.set(oldLength,lengthList.get(oldLength)-1);
				if (oldLength == lengthList.size()-1 && lengthList.get(oldLength) == 0)
				{
					lengthList.remove(oldLength);
				}
				lengthList.set(oldLength-1,lengthList.get(oldLength-1)+1);
				
				// Updates the list of occupied buckets
				Node currentNode = chain.getOccupiedIndex();
				if (chain.getSize() == 0) // Removes the chain from occupied if it is empty
				{
					occupied.removeNode(currentNode);
					chain.setOccupiedIndex(null);
				}
			}
			return oldValue;
		}
	}
	
	/**
	*
	* Returns an iterator for the key-value pairs stored in this
	* hash table
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
	
	public ChainingIterator iterator()
	{
    		return new ChainingIterator();
	}
}
