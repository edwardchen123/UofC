package CPSC331Assignment3;

/**
 * All documentation for this class is located in the writeup for this assignment.
 */


import org.junit.*;
import CPSC331Assignment3.BST.Node;
import static org.junit.Assert.*;
import CPSC331Assignment3.SimpleMap;
import CPSC331Assignment3.Pair;

public class TestBST
{
	@Test
	public void test_constructor()
	{
		System.out.println("Test: constructor of DST");
		BST<Integer, Integer> tree = new BST<Integer, Integer>();
		assertTrue(tree.root == null && tree.first == null && tree.size == 0);
	}

	@Test
	public void test_size()
	{
		System.out.println("Test: size() returns correct size");
		BST<Integer, Integer> tree = new BST<Integer, Integer>();
		assertTrue(tree.size() == 0);
	}

	@Test(expected = NullPointerException.class)
	public void put_NullKey()
	{
		System.out.println("Test: put() throws a NullPointerException when passed a null key");
		BST<Integer, Integer> tree = new BST<Integer, Integer>();
		Integer newValue = new Integer(0);
		tree.put(null, newValue);
	}
	
	@Test(expected = NullPointerException.class)
	public void put_NullValue()
	{
		System.out.println("Test: put() throws a NullPointerException when passed a null value");
		BST<Integer, Integer> tree = new BST<Integer, Integer>();
		Integer newKey = new Integer(5);
		tree.put(newKey, null);
	}
	
	@Test(expected = NullPointerException.class)
	public void put_NullKeyValue()
	{
		System.out.println("Test: put() throws a NullPointerException when passed both a null key and value");
		BST<Integer, Integer> tree = new BST<Integer, Integer>();
		tree.put(null, null);
	}
	
	@Test
	public void put_EmptyTree()
	{
		System.out.println("Test: put() correctly adds a node to an empty tree");
		BST<Integer, Integer> tree = new BST<Integer, Integer>();
		int treeSize = tree.size();
		Integer newKey = new Integer(5);
		Integer newValue = new Integer(0);
		Integer value = tree.put(newKey, newValue);
		assertTrue(value == null &&
				(tree.root).key == newKey &&
				(tree.root).value == newValue &&
				(tree.size()-treeSize) == 1);
	}
	
	@Test
	public void put_LeftChild()
	{
		System.out.println("Test: put() correctly adds a left child to a tree with one node");
		BST<Integer, Integer> tree = new BST<Integer, Integer>();
		Integer newKey = new Integer(5);
		Integer newValue = new Integer(0);
		tree.put(newKey, newValue);
		int treeSize = tree.size();
		newKey = new Integer(3);
		newValue = new Integer(1);
		Integer value = tree.put(newKey, newValue);
		assertTrue(((tree.root).left).key == newKey &&
				((tree.root).left).value == newValue &&
				((tree.root).left).parent == tree.root &&
				((tree.root).left).predecessor == null &&
				((tree.root).left).successor == ((tree.root).left).parent &&
				(((tree.root).left).successor).predecessor == ((tree.root).left) &&
				((tree.root).left) == tree.first &&
				value == null &&
				(tree.size()-treeSize) == 1);			
	}
	
	@Test
	public void put_RightChild()
	{
		System.out.println("Test: put() correctly adds a right child to a tree with one node");
		BST<Integer, Integer> tree = new BST<Integer, Integer>();
		Integer newKey = new Integer(5);
		Integer newValue = new Integer(0);
		tree.put(newKey, newValue);
		int treeSize = tree.size();
		newKey = new Integer(7);
		newValue = new Integer(1);
		Integer value = tree.put(newKey, newValue);
		assertTrue(((tree.root).right).key == newKey &&
				((tree.root).right).value == newValue &&
				((tree.root).right).parent == tree.root &&
				((tree.root).right).successor == null &&
				((tree.root).right).predecessor == ((tree.root).right).parent &&
				(((tree.root).right).predecessor).successor == ((tree.root).right) &&
				value == null &&
				(tree.size()-treeSize) == 1);
	}
	
	@Test
	public void put_LeftChildInside()
	{
		System.out.println("Test: put() correctly adds a left child to the right branch of a tree of depth two");
		BST<Integer, Integer> tree = new BST<Integer, Integer>();
		Integer newKey = new Integer(5);
		Integer newValue = new Integer(0);
		tree.put(newKey, newValue);
		newKey = new Integer(7);
		newValue = new Integer(1);
		tree.put(newKey, newValue);
		newKey = new Integer(6);
		newValue = new Integer(2);
		int treeSize = tree.size();
		tree.put(newKey, newValue);
		assertTrue(((((tree.root).right).left).parent).left == (((tree.root).right).left) &&
				(((tree.root).right).left).successor == (((tree.root).right).left).parent &&
				(((tree.root).right).left).predecessor == tree.root &&
				((((tree.root).right).left).successor).predecessor == (((tree.root).right).left) &&
				((((tree.root).right).left).predecessor).successor == (((tree.root).right).left) &&
				(tree.size()-treeSize) == 1);
	}
	
	@Test
	public void put_RightChildInside()
	{
		System.out.println("Test: put() correctly adds a right child to the left branch of a tree of depth two");
		BST<Integer, Integer> tree = new BST<Integer, Integer>();
		Integer newKey = new Integer(5);
		Integer newValue = new Integer(0);
		tree.put(newKey, newValue);
		newKey = new Integer(3);
		newValue = new Integer(1);
		tree.put(newKey, newValue);
		newKey = new Integer(4);
		newValue = new Integer(2);
		int treeSize = tree.size();
		tree.put(newKey, newValue);
		assertTrue(((((tree.root).left).right).parent).right == (((tree.root).left).right) &&
				(((tree.root).left).right).predecessor == (((tree.root).left).right).parent &&
				(((tree.root).left).right).successor == tree.root &&
				((((tree.root).left).right).successor).predecessor == (((tree.root).left).right) &&
				((((tree.root).left).right).predecessor).successor == (((tree.root).left).right) &&
				(tree.size()-treeSize) == 1);
	}
	
	@Test
	public void put_ChangeValue()
	{
		System.out.println("Test: put() correctly replaces the value of an existing node");
		BST<Integer, Integer> tree = new BST<Integer, Integer>();
		Integer newKey = new Integer(5);
		Integer newValue = new Integer(0);
		tree.put(newKey, newValue);
		newValue = new Integer(0);
		int treeSize = tree.size();
		Integer value = tree.put(newKey, newValue);
		assertTrue((treeSize-tree.size()) == 0 &&
				(tree.root).right == null &&
				(tree.root).left == null &&
				(tree.root).value == newValue);
	}
	
	@Test(expected = NullPointerException.class)
	public void get_nullKey()
	{
		System.out.println("Test: get() throws a NullPointerException when passed a null key");
		BST<Integer, Integer> tree = new BST<Integer, Integer>();
		tree.get(null);
	}
	
	@Test
	public void get_EmptyTree()
	{
		System.out.println("Test: get() returns null when the tree is empty");
		BST<Integer, Integer> tree = new BST<Integer, Integer>();
		Integer newKey = new Integer(5);
		assertTrue(tree.get(newKey) == null);
	}
	
	@Test
	public void get_NodeNonexistent()
	{
		System.out.println("Test: get() returns null if the requested node does not exist");
		BST<Integer, Integer> tree = new BST<Integer, Integer>();
		Integer newKey = new Integer(5);
		Integer newValue = new Integer(0);
		tree.put(newKey, newValue);
		newKey = new Integer(7);
		assertTrue(tree.get(newKey) == null);
	}
	
	@Test
	public void get_NodeExists()
	{
		System.out.println("Test: get() returns the correct value of an existing node");
		BST<Integer, Integer> tree = new BST<Integer, Integer>();
		Integer newKey = new Integer(5);
		Integer newValue = new Integer(0);
		tree.put(newKey, newValue);
		Integer value = tree.get(newKey);
		assertTrue(value == newValue);
	}
	
	@Test(expected = NullPointerException.class)
	public void remove_NullKey()
	{
		System.out.println("Test: remove() throws a NullPointerException when passed a null key");
		BST<Integer, Integer> tree = new BST<Integer, Integer>();
		tree.remove(null);
	}
	
	@Test
	public void remove_EmptyTree()
	{
		System.out.println("Test: remove() returns null when the tree is empty");
		BST<Integer, Integer> tree = new BST<Integer, Integer>();
		Integer newKey = new Integer(5);
		assertTrue(tree.remove(newKey) == null);
	}
	
	@Test
	public void remove_NodeNonexistent()
	{
		System.out.println("Test: remove() returns null if the requested node does not exist");
		BST<Integer, Integer> tree = new BST<Integer, Integer>();
		Integer newKey = new Integer(5);
		Integer newValue = new Integer(0);
		tree.put(newKey, newValue);
		newValue = new Integer(7);
		assertTrue(tree.remove(newValue) == null);
	}
	
	@Test
	public void remove_RootNodeNoChildren()
	{
		System.out.println("Test: remove() correctly removes the root node when it is the only node in the tree");
		BST<Integer, Integer> tree = new BST<Integer, Integer>();
		Integer newKey = new Integer(5);
		Integer newValue = new Integer(0);
		tree.put(newKey, newValue);
		Integer value = tree.remove(newKey);
		assertTrue(tree.root == null &&
				tree.first == null &&
				tree.size() == 0);
	}
	
	@Test
	public void remove_RootNodeLeftChild()
	{
		System.out.println("Test: remove() correctly removes the root node when it has a left child");
		BST<Integer, Integer> tree = new BST<Integer, Integer>();
		Integer rootKey = new Integer(5);
		Integer rootValue = new Integer(0);
		tree.put(rootKey, rootValue);
		Integer newKey = new Integer(3);
		Integer newValue = new Integer(1);
		tree.put(newKey, newValue);
		int treeSize = tree.size();
		Integer value = tree.remove(rootKey);
		assertTrue(value == rootValue &&
				(tree.root).key == newKey &&
				(tree.root).value == newValue &&
				tree.first == tree.root &&
				(tree.root).parent == null &&
				(tree.root).left == null &&
				(tree.root).right == null &&
				(treeSize-tree.size()) == 1);
	}			
	
	@Test
	public void remove_RootNodeRightChild()
	{
		System.out.println("Test: remove() correctly removes the root node when it has a right child");
		BST<Integer, Integer> tree = new BST<Integer, Integer>();
		Integer rootKey = new Integer(5);
		Integer rootValue = new Integer(0);
		tree.put(rootKey, rootValue);
		Integer newKey = new Integer(7);
		Integer newValue = new Integer(1);
		tree.put(newKey, newValue);
		int treeSize = tree.size();
		Integer value = tree.remove(rootKey);
		assertTrue(value == rootValue &&
				(tree.root).key == newKey &&
				(tree.root).value == newValue &&
				(tree.root).parent == null &&
				(tree.root).left == null &&
				(tree.root).right == null &&
				(treeSize-tree.size()) == 1);
	}
	
	@Test
	public void remove_RootNodeRightLeftChild()
	{
		System.out.println("Test: remove() correctly removes the root node when it has both a left and right child");
		BST<Integer, Integer> tree = new BST<Integer, Integer>();
		Integer rootKey = new Integer(5);
		Integer rootValue = new Integer(0);
		tree.put(rootKey, rootValue);
		Integer rightKey = new Integer(7);
		Integer rightValue = new Integer(1);
		tree.put(rightKey, rightValue);
		Integer leftKey = new Integer(3);
		Integer leftValue = new Integer(2);
		tree.put(leftKey, leftValue);
		int treeSize = tree.size();
		Integer value = tree.remove(rootKey);
		assertTrue(value == rootValue &&
				(((tree.root).left == null && (tree.root).key == leftKey && (tree.root).value == leftValue) ||
				((tree.root).right == null && (tree.root).key == rightKey && (tree.root).value == rightValue)) &&
				(treeSize-tree.size()) == 1);		
	}
	
	@Test
	public void remove_NodeNoChildren() //TODO
	{
		System.out.println("Test: remove() correctly removes a node when it has no children");
		BST<Integer, Integer> tree = new BST<Integer, Integer>();
		Integer rootKey = new Integer(5);
		Integer rootValue = new Integer(0);
		tree.put(rootKey, rootValue);
		Integer newKey = new Integer(3);
		Integer newValue = new Integer(1);
		tree.put(newKey, newValue);
		int treeSize = tree.size();
		Integer value = tree.remove(newKey);
		assertTrue(value == newValue &&
				(tree.root).left == null &&
				(tree.root).predecessor == null &&
				tree.first == tree.root &&
				(treeSize-tree.size()) == 1);
	}
	
	@Test
	public void remove_NodeLeftChild() //TODO
	{
		System.out.println("Test: remove() correctly removes a node when it has a left child");
		BST<Integer, Integer> tree = new BST<Integer, Integer>();
		Integer rootKey = new Integer(5);
		Integer rootValue = new Integer(0);
		tree.put(rootKey, rootValue);
		Integer parentKey = new Integer(3);
		Integer parentValue = new Integer(1);
		tree.put(parentKey, parentValue);
		Integer childKey = new Integer(2);
		Integer childValue = new Integer(2);
		tree.put(childKey, childValue);
		int treeSize = tree.size();
		Integer value = tree.remove(parentKey);
		assertTrue(value == parentValue &&
				((tree.root).left).key == childKey &&
				((tree.root).left).value == childValue &&
				((tree.root).predecessor).key == childKey &&
				((tree.root).predecessor).value == childValue &&
				(treeSize-tree.size()) == 1);
	}
	
	@Test
	public void remove_NodeRightChild() //TODO
	{
		System.out.println("Test: remove() correctly removes a node when it has a right child");
		BST<Integer, Integer> tree = new BST<Integer, Integer>();
		Integer rootKey = new Integer(5);
		Integer rootValue = new Integer(0);
		tree.put(rootKey, rootValue);
		Integer parentKey = new Integer(7);
		Integer parentValue = new Integer(1);
		tree.put(parentKey, parentValue);
		Integer childKey = new Integer(8);
		Integer childValue = new Integer(2);
		tree.put(childKey, childValue);
		int treeSize = tree.size();
		Integer value = tree.remove(parentKey);
		assertTrue(value == parentValue &&
				((tree.root).right).key == childKey &&
				((tree.root).right).value == childValue &&
				((tree.root).successor).key == childKey &&
				((tree.root).successor).value == childValue &&
				(treeSize-tree.size()) == 1);
	}
	
	@Test
	public void remove_NodeRightLeftChild() //TODO
	{
		System.out.println("Test: remove() correctly removes a node when it has both a left and right child");
		BST<Integer, Integer> tree = new BST<Integer, Integer>();
		Integer rootKey = new Integer(5);
		Integer rootValue = new Integer(0);
		tree.put(rootKey, rootValue);
		Integer parentKey = new Integer(8);
		Integer parentValue = new Integer(1);
		tree.put(parentKey, parentValue);
		Integer leftChildKey = new Integer(7);
		Integer leftChildValue = new Integer(2);
		tree.put(leftChildKey, leftChildValue);
		Integer rightChildKey = new Integer(9);
		Integer rightChildValue = new Integer(3);
		tree.put(rightChildKey, rightChildValue);
		Integer rightChildChildKey = new Integer(10);
		Integer rightChildChildValue = new Integer(4);
		tree.put(rightChildChildKey, rightChildChildValue);
		Integer leftChildChildKey = new Integer(6);
		Integer leftChildChildValue = new Integer(5);
		tree.put(leftChildChildKey, leftChildChildValue);
		int treeSize = tree.size();
		Integer value = tree.remove(parentKey);
		assertTrue(value == parentValue &&
				(((((tree.root).right).left).key == leftChildChildKey && ((tree.root).right).key == leftChildKey && ((tree.root).right).value == leftChildValue) ||
				((((tree.root).right).right).key == rightChildChildKey && ((tree.root).right).key == rightChildKey && ((tree.root).right).value == rightChildValue)) &&
				(treeSize-tree.size()) == 1);
	}
}
