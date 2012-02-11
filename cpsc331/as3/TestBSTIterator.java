package CPSC331Assignment3;

/**
 * All documentation for this class is located in the writeup for this assignment.
 */

import org.junit.*;
import CPSC331Assignment3.BST.Node;
import static org.junit.Assert.*;
import CPSC331Assignment3.SimpleMap;
import CPSC331Assignment3.Pair;
import CPSC331Assignment3.BST.BSTIterator;
import java.util.NoSuchElementException;

public class TestBSTIterator
{
	@Test
	public void test_constructor()
	{
		System.out.println("Test: the constructor properly instantiates an iterator");
		BST<Integer, Integer> tree = new BST<Integer, Integer>();
		BST<Integer, Integer>.BSTIterator iter = tree.iterator();
		assertTrue(iter.nextNode == tree.first);
	}

	@Test(expected = NoSuchElementException.class)
	public void next_emptyTree()
	{
		System.out.println("Test: the next() method throws a NoSuchElementException when the tree is empty");
		BST<Integer, Integer> tree = new BST<Integer, Integer>();
		BST<Integer, Integer>.BSTIterator iter = tree.iterator();
		iter.next();
	}

	@Test
	public void next_nonEmptyTree()
	{
		System.out.println("Test: the next() method properly iterates through the tree");
		BST<Integer, Integer> tree = new BST<Integer, Integer>();
		Integer rootKey = new Integer(5);
		Integer rootValue = new Integer(0);
		tree.put(rootKey, rootValue);
		Integer leftKey = new Integer(3);
		Integer leftValue = new Integer(1);
		tree.put(leftKey, leftValue);
		BST<Integer, Integer>.BSTIterator iter = tree.iterator();
		iter.next();
		assertTrue(((iter.nextNode).predecessor).key == leftKey &&
			((iter.nextNode).predecessor).value == leftValue &&
			(iter.nextNode).key == rootKey &&
			(iter.nextNode).value == rootValue);
	}

	@Test(expected = NoSuchElementException.class)
	public void next_end()
	{
		System.out.println("Test: the next() method throws a NoSuchElementException when the iterator is at the end");
		BST<Integer, Integer> tree = new BST<Integer, Integer>();
		BST<Integer, Integer>.BSTIterator iter = tree.iterator();
		Integer rootKey = new Integer(5);
		Integer rootValue = new Integer(0);
		tree.put(rootKey, rootValue);
		iter.next();
		iter.next();
	}

	@Test
	public void hasNext_emptyTree()
	{
		System.out.println("Test: the hasNext() method returns false when the tree is empty");
		BST<Integer, Integer> tree = new BST<Integer, Integer>();
		BST<Integer, Integer>.BSTIterator iter = tree.iterator();
		assertTrue(!(iter.hasNext()));
	}

	@Test
	public void hasNext_nonEmptyTree()
	{
		System.out.println("Test: the hasNext() method returns true when nextNode exists");
		BST<Integer, Integer> tree = new BST<Integer, Integer>();
		Integer rootKey = new Integer(5);
		Integer rootValue = new Integer(0);
		tree.put(rootKey, rootValue);
		BST<Integer, Integer>.BSTIterator iter = tree.iterator();
		assertTrue(iter.hasNext());
	}

	@Test
	public void hasNext_nonEmptyTreeEnd()
	{
		System.out.println("Test: the hasNext() method returns false when the end is reached");
		BST<Integer, Integer> tree = new BST<Integer, Integer>();
		Integer rootKey = new Integer(5);
		Integer rootValue = new Integer(0);
		tree.put(rootKey, rootValue);
		BST<Integer, Integer>.BSTIterator iter = tree.iterator();
		iter.next();
		assertTrue(!(iter.hasNext()));
	}
}
