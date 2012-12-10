package CPSC331Assignment3;

import CPSC331Assignment3.SimpleMap;
import CPSC331Assignment3.Pair;
import java.util.NoSuchElementException;
import java.util.Iterator;

/**
* <p>
*  <strong>Class Invariant:</strong>
* </p>
* <ul>
* <li>	This class creates a binary search tree, which is
*	composed of nodes. Nodes have the key field, which is
*	used to define and sort them, the value field, which 
*	is the value the node stores, the left field, which is
*	a pointer to the left child of the node (the left child
*	is a node with a key less than that of the parent node),
*	the right field, which is a pointer to the right child
*	of the node (the right child is a node with a key greater
*	than that of the parent node), the parent field, which is
*	a pointer to a node located 'above' the node in the structure,
*	the predecessor field, which is a pointer to a node with
*	the next-smallest key value, and the successor field, which
*	is a pointer to a node with the next-largest key value.
*	The root node is the node which has no parent, the first node
* 	is the node with the smallest key value, and the size of
*	the tree is given by the number of nodes in the tree.
* </li>
* </ol>
*
*/

public class BST<K extends Comparable<K>, V> implements SimpleMap<K, V>
{
	protected class Node
	{

		/*
		* Instance variables for the key and value stored at this
		* node, as well as references to its left child, right child,
		* parent, and nodes storing keys immediately before and after
		* the key stored at this node, in the set of keys currently
		* used (and when listed in order)
		*
		*/

		protected K key;             // The key stored at this node
		protected V value;           // The value stored at this node
		protected Node left;         // The left child of this node
		protected Node right;        // The right child of this node
		protected Node parent;       // The parent of this node
		protected Node predecessor;  // Contains key immediately before this one
		protected Node successor;    // Contains key immediately after this one

		// Constructor to produce a Node with a specified non-null
		// key and value

		protected Node(K key, V value)
		{
			if ((key == null) || (value == null))
			{
				throw new NullPointerException();
			}
			else
			{
				this.key = key;
				this.value = value;
				left = null;
				right = null;
				parent = null;
				predecessor = null;
				successor = null;
			}
		}
	}
  
	public class BSTIterator implements Iterator<Pair<K,V>>
	{
		protected Node nextNode;	//The next node that will be iterated to

		public BSTIterator()
		{
			nextNode = first;	//Sets the nextNode equal to the first node in the tree
		}
	
		/**
		*
		* Returns a boolean value that depends on whether there exists
		* a node with a key greater than the current node in the map
		* <br />
		*
		* <p>
		*  <strong>Precondition 1:</strong>
		* </p>
		* <ol style="list-style-type: lower-alpha">
		* <li>	The class invariant is satisfied </li>
		* <li>	There exists no node in the map with a key greater than
		*	the key of the current node </li>
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
		* <li>	There exists at least one node in the map with a key
		*	greater than the key of the current node </li>
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
			if (nextNode == null)
				return false;
			else
				return true;
		}

		public void remove()
		{
			throw new UnsupportedOperationException();
		}

		/**
		*
		* Returns the node with the next-highest key in the map and
		* updates nextNode to point to the node with the next-next-highest
		* key.
		* <br />
		*
		* <p>
		*  <strong>Precondition 1:</strong>
		* </p>
		* <ol style="list-style-type: lower-alpha">
		* <li>	The class invariant is satisfied </li>
		* <li>	There exists no node in the map with a key greater than
		*	the key of the current node </li>
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
		* <li>	There exists at least one node in the map with a key
		*	greater than the key of the current node </li>
		* </ol>	
		* <p>
		*  <strong>Postcondition 2:</strong>
		* </p>
		* <ol style="list-style-type: lower-alpha">
		* <li> The map is not changed (so the class invariant
		*      is still satisfied) </li>
		* <li> nextNode is returned and nextNode is set to the successor
		*	of the node it pointed to before the method was called
		* </ol>
		*
		* @return the node with the next highest key in the map
		* @throws NoSuchElementException if there exists no node with a 
		*	key greater than that of the current node
		*
		*/

		public Pair<K, V> next()
		{
			if (nextNode != null)
			{
				Pair<K,V> current = new Pair<K,V>(nextNode.key,nextNode.value);
				nextNode = nextNode.successor;
				return current;
			}
			else
			{
				throw new NoSuchElementException();
			}
		}
	}
  
	/*
	* Instance variables for the root of the binary search tree
	* used to represent this map, as well as its size
	*
	*/
   
	protected Node root;	// Root of the binary search tree
	protected Node first;   // Node storing the smallest key
	protected int size;     // Size of this tree
	  
	public BST() 
	{
		root = null;	// Sets the root of the tree to null
		first = null;	// Sets the first element of the tree to null
		size = 0;	// Sets the size of the tree to be zero
	}

	/**
	*
	* Returns the number of key-value pairs in this map.
	* <br />
	*
	* <p>
	*  <strong>Precondition:</strong> The class invariant is satisfied
	* </p>
	* <p>
	*  <strong>Postcondition:</strong>
	* </p>
	* <ol style="list-style-type: lower-alpha">
	* <li> The map is not changed (so the class invariant
	*      is still satisfied) </li>
	* <li> The value returned is the number of key-value pairs
	*      in this map </li>
	* </ol>
	*
	* @return the number of key-value pairs in this map
	*
	*/

	public int size()
	{
		return size;
  	}

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
	* <li> The class invariant is satisfied </li>
	* <li> The map includes a key-value pair for
	*      the specified non-<code>null</code>
	*      <code>key</code> </li>
	* </ol>
	* <p>
	*  <strong>Postcondition 1:</strong>
	* </p>
	* <ol style="list-style-type: lower-alpha">
	* <li> The map is not changed (so the class invariant
	*      is still satisfied) </li>
	* <li> The value, that is mapped to the specified
	*      <code>key</code>, is returned. </li>
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
	* <li> <code>null</code> is returned </li>
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
	* @param key the key whose associated value is to be returned
	* @return the value associated with the input <code>key</code>,
	*         or <code>null</code> if no value is associated with it
	* @throws NullPointerException if the specified key is <code>null</code>
	*
	*/

  	public V get(K key)
	{
		if (key == null) //Checks parameters and throws exception if necessary
		{
			throw new NullPointerException();
		}

		if (size == 0) //Checks if tree is empty and returns null if so
		{
			return null;
		}

		Node destination = traverse(key, root); //Traverses tree looking for corresponding key value
		if (key.equals(destination.key))
		{
			return destination.value;
		}
		else
		{
			return null;
		}
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
	* <li> Either (or both) of the input key or value is <code>null</code> </li>
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

	public V put(K key, V value)
	{
		if (key == null || value == null) //Checks parameters and throws exception if necessary
		{
			throw new NullPointerException();
		}

		if (size == 0) //Checks if tree is empty and adds new node as root if so
		{
			root = new Node(key, value);
			first = root; //Sets the root node as the first node
			size++; //Increases the size of the map
			return null;
		}

		Node destination = traverse(key, root); //Traverses tree looking for Node with corresponding key value
		if (key.equals(destination.key)) //Evaluates to true if the Node already exists
		{
			V old = destination.value;
			destination.value = value; //Sets value of Node to new value
			return old;
		}
		else //Executes if Node does not yet exist (Node returned is soon-to-be parent of nonexistent node)
		{
			Node newNode = new Node(key, value);
			newNode.parent = destination; //Makes Destination Node the parent of the new node
			if (key.compareTo((newNode.parent).key) < 0) //Checks if newNode is left child of Destination Node
			{
				newNode.successor = newNode.parent; //Sets successor to be the parent
				if (destination == first) //Checks if Destination Node was the first node
				{
					first = newNode;
				}
				else
				{
					newNode.predecessor = (newNode.parent).predecessor;
					((newNode.parent).predecessor).successor = newNode;
				}
				(newNode.parent).predecessor = newNode;
				(newNode.parent).left = newNode; //Makes newNode the left child of the Destination Node
			}
			else //Executes if newNode is right child of Destination Node
			{
				newNode.predecessor = newNode.parent; //Sets predecessor to be the parent
				if ((newNode.parent).successor != null)
				{
					newNode.successor = (newNode.parent).successor;
					((newNode.parent).successor).predecessor = newNode;
				}
				(newNode.parent).successor = newNode;
				(newNode.parent).right = newNode; //Makes newNode the right child of the Destination Node
			}
			size++; //Increases the size of the map
			return null;
		}
		
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

  	public V remove(K key)
	{
    	if (key == null) //Checks parameters and throws exception if necessary
		{
			throw new NullPointerException();
		}
    	
    	if (size == 0) //Checks if tree is empty
    	{
    		return null;
    	}

		Node destination = traverse(key, root); //Traverses tree looking for Node
		if (key.equals(destination.key)) //Checks if Node is in the tree
		{
			V old = destination.value;
			if (destination.left == null && destination.right == null) //The Node is a leaf
			{
				if (destination != root) //Checks that destination is not the root node
				{
					if ((destination.parent).left == destination) //Destination is a left child
					{
						if (destination != first) //Checks whether destination is first node
							(destination.predecessor).successor = destination.successor;
						else
							first = destination.successor;
						(destination.successor).predecessor = destination.predecessor;
						(destination.parent).left = null;
					}
					else //Destination is a right child
					{
						if (destination.successor != null)
							(destination.successor).predecessor = destination.predecessor;
						(destination.predecessor).successor = destination.successor;
						(destination.parent).right = null;
					} 
				}
				else //If destination is the root node, removal will make the tree empty
				{
					root = null;
					first = null;
				}
			}
			else if (destination.left != null && destination.right == null) //The Node has a left child
			{
				(destination.left).successor = destination.successor;
				if (destination != root) //Destination is not the root node
				{
					if ((destination.parent).left == destination) //Destination is a left child
						(destination.parent).left = destination.left;
					else //Destination is a right child
						(destination.parent).right = destination.left;
					if (destination.successor != null)
						(destination.successor).predecessor = destination.predecessor;
					(destination.predecessor).successor = destination.successor;
					(destination.left).parent = destination.parent;
				}
				else //Destination is the root node
				{
					(destination.left).parent = null;
					(destination.predecessor).successor = null;
					root = destination.left;
				}
			}
			else if (destination.left == null && destination.right != null) //The Node has a right child
			{
				(destination.right).predecessor = destination.predecessor;
				if (destination != root) //Destination is not the root node
				{
					if ((destination.parent).left == destination) //Destination is a left child
					{
						if (destination == first)
							first = destination.successor;
						(destination.parent).left = destination.right;
					}
					else //Destination is a right child
						(destination.parent).right = destination.right;
					(destination.successor).predecessor = destination.predecessor;
					if (destination.predecessor != null)
						(destination.predecessor).successor = destination.successor;
					(destination.right).parent = destination.parent;
				}
				else //Destination is the root node
				{
					(destination.right).parent = null;
					first = destination.successor;
					root = destination.right;
				}				
			}
			else if (destination.left != null && destination.right != null) //The Node has two children
			{
				K newKey = (destination.successor).key; //Records key of successor node
				V newValue = (destination.successor).value; //Records value of successor node
				remove((destination.successor).key); //Removes successor node
				destination.key = newKey; //Replaces key of Destination
				destination.value = newValue; //Replaces value of Destination
				size++;
				//Destination is now removed and replaced by its successor for all intents and purposes
			}
			size--;
			return old;
		}
		else //The Node is not in the tree
		{
			return null;
		}
  	}

	public BSTIterator iterator()
	{
		return new BSTIterator();
	}

	private Node traverse(K key, Node current) //This is the method used by other methods to locate a node
	{
		if (key.compareTo(current.key) < 0) //Key is less than key of current node
		{
			if (current.left == null)
				return current;
			else
				return traverse(key, current.left);
		}
		else if (key.compareTo(current.key) > 0) //Key is greater than key of current node
		{
			if (current.right == null)
				return current;
			else
				return traverse(key, current.right);
		}
		else //Key is equal to key of current node
		{
			return current;
		}
	}
}
