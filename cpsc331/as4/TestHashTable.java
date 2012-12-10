package CPSC331Assignment4;

/* * *
 * See written Answers for documentation of tests
 * * */

import CPSC331Assignment3.Pair;
import CPSC331Assignment4.ComparePair;
import CPSC331Assignment4.HashStructureWithChaining.ChainingIterator;
import org.junit.Test;
import java.util.NoSuchElementException;
import static org.junit.Assert.*;

public class TestHashTable
{
	@Test
	public void test_tableSize()
	{
		System.out.println("Test: tableSize() returns the correct size of the table");
		int m = 10;
		SimpleHashFunction hashFunction = new SimpleHashFunction(m);
		assertTrue(hashFunction.tableSize() == m);
	}
	
	@Test (expected = NullPointerException.class)
	public void value_nullKey()
	{
		System.out.println("Test: value() throws an exception when passed a null key");
		int m = 10;
		SimpleHashFunction hashFunction = new SimpleHashFunction(m);
		hashFunction.value(null);
	}
	
	@Test
	public void test_value()
	{
		System.out.println("Test: value() returns an integer between 0 and m for some arbitrary key");
		int m = 10;
		SimpleHashFunction hashFunction = new SimpleHashFunction(m);
		Integer newKey = new Integer(50);
		int hash = hashFunction.value(newKey);
		assertTrue(0 <= hash && hash <= m);
	}

	@Test
	public void test_numberPositions()
	{
		System.out.println("Test: numberPositions() returns the correct size of the hash table");
		int tableSize = 100;
		HashStructureWithChaining hashTable = new HashStructureWithChaining(100);
		assertTrue(hashTable.numberPositions() == 100);
	}
	
	@Test (expected = NullPointerException.class)
	public void put_nullKey()
	{
		System.out.println("Test: put() throws an exception when passed a null key");
		HashStructureWithChaining hashTable = new HashStructureWithChaining(100);
		Integer newValue = new Integer(5);
		int tableIndex = 5;
		hashTable.put(null, tableIndex, newValue);
	}
	
	@Test (expected = NullPointerException.class)
	public void put_nullValue()
	{
		System.out.println("Test: put() throws an exception when passed a null value");
		HashStructureWithChaining hashTable = new HashStructureWithChaining(100);
		Integer newKey = new Integer(5);
		int tableIndex = 5;
		hashTable.put(newKey, tableIndex, null);
	}
	
	@Test (expected = NullPointerException.class)
	public void put_nullKeyValue()
	{
		System.out.println("Test: put() throws an exception when passed a null key and null value");
		HashStructureWithChaining hashTable = new HashStructureWithChaining(100);
		int tableIndex = 5;
		hashTable.put(null, tableIndex, null);
	}
	
	@Test (expected = IndexOutOfBoundsException.class)
	public void put_negativeIndex()
	{
		System.out.println("Test: put() throws an exception when passed a negative table index");
		HashStructureWithChaining hashTable = new HashStructureWithChaining(100);
		Integer newKey = new Integer(5);
		Integer newValue = new Integer(5);
		int tableIndex = -1;
		hashTable.put(newKey, tableIndex, newValue);
	}
	
	@Test (expected = IndexOutOfBoundsException.class)
	public void put_invalidIndex()
	{
		System.out.println("Test: put() throws an exception when passed an invalid table index");
		HashStructureWithChaining hashTable = new HashStructureWithChaining(100);
		Integer newKey = new Integer(5);
		Integer newValue = new Integer(5);
		int tableIndex = 150;
		hashTable.put(newKey, tableIndex, newValue);
	}
	
	@Test
	public void put_oneNode()
	{
		System.out.println("Test: put() adds a key/value pair to the hash table");
		HashStructureWithChaining hashTable = new HashStructureWithChaining(100);
		Integer newKey = new Integer(5);
		Integer newValue = new Integer(5);
		int tableIndex = 5;
		int oldSize = hashTable.size();
		assertTrue(hashTable.put(newKey, tableIndex, newValue) == null &&
			(hashTable.size()-oldSize) == 1 &&
			(hashTable.get(newKey, tableIndex)).equals(newValue));
	}
	
	@Test
	public void put_twoNodes()
	{
		System.out.println("Test: put() adds two key/value pairs to the hash table at the same location");
		HashStructureWithChaining hashTable = new HashStructureWithChaining(100);
		Integer keyOne = new Integer(5);
		Integer valueOne = new Integer(5);
		Integer keyTwo = new Integer(10);
		Integer valueTwo = new Integer(10);
		int tableIndex = 5;
		int oldSize = hashTable.size();
		hashTable.put(keyOne, tableIndex, valueOne);
		assertTrue(hashTable.put(keyTwo, tableIndex, valueTwo) == null &&
			(hashTable.size()-oldSize) == 2 &&
			(hashTable.get(keyOne, tableIndex)).equals(valueOne) &&
			(hashTable.get(keyTwo, tableIndex)).equals(valueTwo));
	}
	
	@Test
	public void put_existingNode()
	{
		System.out.println("Test: put() replaces the value of an existing key/value pair");
		HashStructureWithChaining hashTable = new HashStructureWithChaining(100);
		Integer newKey = new Integer(5);
		Integer valueOne = new Integer(5);
		int tableIndex = 5;
		hashTable.put(newKey, tableIndex, valueOne);
		Integer valueTwo = new Integer(10);
		int oldSize = hashTable.size();
		assertTrue((hashTable.put(newKey, tableIndex, valueTwo)).equals(valueOne) &&
			(hashTable.size()-oldSize) == 0 &&
			(hashTable.get(newKey, tableIndex)).equals(valueTwo));
	}
	
	@Test (expected = NullPointerException.class)
	public void get_nullKey()
	{
		System.out.println("Test: get() throws an exception when passed a null key");
		HashStructureWithChaining hashTable = new HashStructureWithChaining(100);
		int tableIndex = 5;
		hashTable.get(null, tableIndex);
	}
	
	@Test (expected = IndexOutOfBoundsException.class)
	public void get_negativeIndex()
	{
		System.out.println("Test: get() throws an exception when passed a negative table index");
		HashStructureWithChaining hashTable = new HashStructureWithChaining(100);
		Integer newKey = new Integer(5);
		int tableIndex = -1;
		hashTable.get(newKey, tableIndex);
	}
	
	@Test (expected = IndexOutOfBoundsException.class)
	public void get_invalidIndex()
	{
		System.out.println("Test: get() throws an exception when passed an invalid table index");
		HashStructureWithChaining hashTable = new HashStructureWithChaining(100);
		Integer newKey = new Integer(5);
		int tableIndex = 150;
		hashTable.get(newKey, tableIndex);
	}
	
	@Test
	public void get_nonexistent()
	{
		System.out.println("Test: get() returns null when asked to find a key/value pair not in the map");
		HashStructureWithChaining hashTable = new HashStructureWithChaining(100);
		Integer newKey = new Integer(5);
		int tableIndex = 5;
		assertTrue(hashTable.get(newKey, tableIndex) == null);
	}
	
	@Test (expected = NullPointerException.class)
	public void remove_nullKey()
	{
		System.out.println("Test: remove() throws an exception when passed a null key");
		HashStructureWithChaining hashTable = new HashStructureWithChaining(100);
		int tableIndex = 5;
		hashTable.remove(null, tableIndex);
	}
	
	@Test (expected = IndexOutOfBoundsException.class)
	public void remove_negativeIndex()
	{
		System.out.println("Test: remove() throws an exception when passed a negative table index");
		HashStructureWithChaining hashTable = new HashStructureWithChaining(100);
		Integer newKey = new Integer(5);
		int tableIndex = -1;
		hashTable.remove(newKey, tableIndex);
	}
	
	@Test (expected = IndexOutOfBoundsException.class)
	public void remove_invalidIndex()
	{
		System.out.println("Test: remove() throws an exception when passed an invalid table index");
		HashStructureWithChaining hashTable = new HashStructureWithChaining(100);
		Integer newKey = new Integer(5);
		int tableIndex = 150;
		hashTable.remove(newKey, tableIndex);
	}
	
	@Test
	public void remove_lengthOne()
	{
		System.out.println("Test: remove() correctly removes a key/value pair from a chain of length one");
		HashStructureWithChaining hashTable = new HashStructureWithChaining(100);
		Integer newKey = new Integer(5);
		Integer newValue = new Integer(5);
		int tableIndex = 5;
		hashTable.put(newKey, tableIndex, newValue);
		int oldSize = hashTable.size();
		assertTrue((hashTable.remove(newKey, tableIndex)).equals(newValue) &&
			(oldSize-hashTable.size()) == 1 &&
			hashTable.get(newKey, tableIndex) == null);
	}
	
	@Test
	public void remove_lengthTwo()
	{
		System.out.println("Test: remove() correctly removes both key/value pairs from a chain of length two");
		HashStructureWithChaining hashTable = new HashStructureWithChaining(100);
		Integer keyOne = new Integer(5);
		Integer valueOne = new Integer(5);
		Integer keyTwo = new Integer(10);
		Integer valueTwo = new Integer(10);
		int tableIndex = 5;
		hashTable.put(keyOne, tableIndex, valueOne);
		hashTable.put(keyTwo, tableIndex, valueTwo);
		int oldSize = hashTable.size();
		assertTrue((hashTable.remove(keyOne, tableIndex)).equals(valueOne) &&
			(hashTable.remove(keyTwo, tableIndex)).equals(valueTwo) &&
			(oldSize-hashTable.size()) == 2 &&
			hashTable.get(keyOne, tableIndex) == null &&
			hashTable.get(keyTwo, tableIndex) == null);
	}
	
	@Test
	public void remove_nonexistent()
	{
		System.out.println("Test: remove() returns null when asked to find a key/value pair not in the map");
		HashStructureWithChaining hashTable = new HashStructureWithChaining(100);
		Integer newKey = new Integer(5);
		int tableIndex = 5;
		assertTrue(hashTable.remove(newKey, tableIndex) == null);
	}
	
	@Test
	public void hasNext_emptyMap()
	{
		System.out.println("Test: hasNext() returns false when the map is empty");
		HashStructureWithChaining hashTable = new HashStructureWithChaining(100);
		ChainingIterator iterator = hashTable.iterator();
		assertFalse(iterator.hasNext());
	}
	
	@Test
	public void hasNext_nullNextNode()
	{
		System.out.println("Test: hasNext() returns false when nextNode is null");
		HashStructureWithChaining hashTable = new HashStructureWithChaining(100);
		Integer newKey = new Integer(5);
		Integer valueOne = new Integer(5);
		int tableIndex = 5;
		hashTable.put(newKey, tableIndex, valueOne);
		ChainingIterator iterator = hashTable.iterator();
		iterator.next();
		assertFalse(iterator.hasNext());
	}
	
	@Test
	public void hasNext_nonNullNextNode()
	{
		System.out.println("Test: hasNext() returns true when nextNode is not null");
		HashStructureWithChaining hashTable = new HashStructureWithChaining(100);
		Integer newKey = new Integer(5);
		Integer valueOne = new Integer(5);
		int tableIndex = 5;
		hashTable.put(newKey, tableIndex, valueOne);
		ChainingIterator iterator = hashTable.iterator();
		assertTrue(iterator.hasNext());
	}
	
	@Test (expected = NoSuchElementException.class)
	public void next_nullNextNode()
	{
		System.out.println("Test: next() throws an exception when nextNode is null");
		HashStructureWithChaining hashTable = new HashStructureWithChaining(100);
		ChainingIterator iterator = hashTable.iterator();
		iterator.next();
	}
	
	@Test
	public void next_singleChain()
	{
		System.out.println("Test: next() iterates correctly when pairs are on one chain");
		HashStructureWithChaining hashTable = new HashStructureWithChaining(100);
		ComparePair compare = new ComparePair();
		Integer keyOne = new Integer(5);
		Integer valueOne = new Integer(5);
		Pair<Integer,Integer> firstPair = new Pair<Integer,Integer>(keyOne,valueOne);
		Integer keyTwo = new Integer(10);
		Integer valueTwo = new Integer(10);
		Pair<Integer,Integer> secondPair = new Pair<Integer,Integer>(keyTwo,valueTwo);
		Integer keyThree = new Integer(15);
		Integer valueThree = new Integer(15);
		Pair<Integer,Integer> thirdPair = new Pair<Integer,Integer>(keyThree,valueThree);
		int tableIndex = 5;
		hashTable.put(keyOne, tableIndex, valueOne);
		hashTable.put(keyTwo, tableIndex, valueTwo);
		hashTable.put(keyThree, tableIndex, valueThree);
		ChainingIterator iterator = hashTable.iterator();
		Pair<Integer,Integer> firstIterator = iterator.next();
		Pair<Integer,Integer> secondIterator = iterator.next();
		Pair<Integer,Integer> thirdIterator = iterator.next();
		assertTrue((compare.equals(firstIterator, firstPair) && compare.equals(secondIterator, secondPair) && compare.equals(thirdIterator, thirdPair)) || //123
			(compare.equals(firstIterator, firstPair) && compare.equals(secondIterator, thirdPair) && compare.equals(thirdIterator, secondPair)) || //132
			(compare.equals(firstIterator, secondPair) && compare.equals(secondIterator, thirdPair) && compare.equals(thirdIterator, firstPair)) || //231
			(compare.equals(firstIterator, secondPair) && compare.equals(secondIterator, firstPair) && compare.equals(thirdIterator, thirdPair)) || //213
			(compare.equals(firstIterator, thirdPair) && compare.equals(secondIterator, secondPair) && compare.equals(thirdIterator, firstPair)) || //321
			(compare.equals(firstIterator, thirdPair) && compare.equals(secondIterator, firstPair) && compare.equals(thirdIterator, secondPair))); //312
	}
		
	@Test
	public void next_threeChains()
	{
		
		System.out.println("Test: next() iterates correctly when pairs are on two different chains");
		HashStructureWithChaining hashTable = new HashStructureWithChaining(100);
		ComparePair compare = new ComparePair();
		Integer keyOne = new Integer(5);
		Integer valueOne = new Integer(5);
		Pair<Integer,Integer> firstPair = new Pair<Integer,Integer>(keyOne,valueOne);
		Integer keyTwo = new Integer(10);
		Integer valueTwo = new Integer(10);
		Pair<Integer,Integer> secondPair = new Pair<Integer,Integer>(keyTwo,valueTwo);
		Integer keyThree = new Integer(15);
		Integer valueThree = new Integer(15);
		Pair<Integer,Integer> thirdPair = new Pair<Integer,Integer>(keyThree,valueThree);
		int tableIndex = 5;
		hashTable.put(keyOne, tableIndex, valueOne);
		tableIndex = 10;
		hashTable.put(keyTwo, tableIndex, valueTwo);
		tableIndex = 15;
		hashTable.put(keyThree, tableIndex, valueThree);
		ChainingIterator iterator = hashTable.iterator();
		Pair<Integer,Integer> firstIterator = iterator.next();
		Pair<Integer,Integer> secondIterator = iterator.next();
		Pair<Integer,Integer> thirdIterator = iterator.next();
		assertTrue((compare.equals(firstIterator, firstPair) && compare.equals(secondIterator, secondPair) && compare.equals(thirdIterator, thirdPair)) || //123
			(compare.equals(firstIterator, firstPair) && compare.equals(secondIterator, thirdPair) && compare.equals(thirdIterator, secondPair)) || //132
			(compare.equals(firstIterator, secondPair) && compare.equals(secondIterator, thirdPair) && compare.equals(thirdIterator, firstPair)) || //231
			(compare.equals(firstIterator, secondPair) && compare.equals(secondIterator, firstPair) && compare.equals(thirdIterator, thirdPair)) || //213
			(compare.equals(firstIterator, thirdPair) && compare.equals(secondIterator, secondPair) && compare.equals(thirdIterator, firstPair)) || //321
			(compare.equals(firstIterator, thirdPair) && compare.equals(secondIterator, firstPair) && compare.equals(thirdIterator, secondPair))); //312
	}
	
	@Test (expected = IllegalStateException.class)
	public void maxAccess_emptyMap()
	{
		System.out.println("Test: maxAccess() throws an exception when");
		HashStructureWithChaining hashTable = new HashStructureWithChaining(100);
		hashTable.maxAccess();
	}
	
	@Test
	public void test_maxAccess()
	{
		System.out.println("Test: maxAccess() returns the correct value");
		HashStructureWithChaining hashTable = new HashStructureWithChaining(100);
		Integer keyOne = new Integer(5);
		Integer valueOne = new Integer(5);
		Integer keyTwo = new Integer(10);
		Integer valueTwo = new Integer(10);
		Integer keyThree = new Integer(15);
		Integer valueThree = new Integer(15);
		int tableIndex = 5;
		hashTable.put(keyOne, tableIndex, valueOne);
		hashTable.put(keyTwo, tableIndex, valueTwo);
		tableIndex = 10;
		hashTable.put(keyThree, tableIndex, valueThree);
		assertTrue(hashTable.maxAccess() == 2);
	}
	
	@Test (expected = IllegalStateException.class)
	public void eSuccess_emptyMap()
	{
		System.out.println("Test: eSuccess() throws an exception when");
		HashStructureWithChaining hashTable = new HashStructureWithChaining(100);
		hashTable.eSuccess();
	}
	
	@Test
	public void test_eSuccess()
	{
		System.out.println("Test: eSuccess() returns the correct value");
		HashStructureWithChaining hashTable = new HashStructureWithChaining(100);
		Integer keyOne = new Integer(5);
		Integer valueOne = new Integer(5);
		Integer keyTwo = new Integer(10);
		Integer valueTwo = new Integer(10);
		Integer keyThree = new Integer(15);
		Integer valueThree = new Integer(15);
		Integer keyFour = new Integer(20);
		Integer valueFour = new Integer(20);
		Integer keyFive = new Integer(25);
		Integer valueFive = new Integer(25);
		Integer keySix = new Integer(30);
		Integer valueSix = new Integer(30);
		int tableIndex = 5;
		hashTable.put(keyOne, tableIndex, valueOne);
		hashTable.put(keyTwo, tableIndex, valueTwo);
		hashTable.put(keyThree, tableIndex, valueThree);
		tableIndex = 10;
		hashTable.put(keyFour, tableIndex, valueFour);
		hashTable.put(keyFive, tableIndex, valueFive);
		hashTable.put(keySix, tableIndex, valueSix);
		assertTrue(hashTable.eSuccess() == 2);
	}
	
	@Test
	public void test_eFail()
	{
		System.out.println("Test: eFail() returns the correct value");
		HashStructureWithChaining hashTable = new HashStructureWithChaining(5);
		Integer keyOne = new Integer(5);
		Integer valueOne = new Integer(5);
		Integer keyTwo = new Integer(10);
		Integer valueTwo = new Integer(10);
		Integer keyThree = new Integer(15);
		Integer valueThree = new Integer(15);
		Integer keyFour = new Integer(20);
		Integer valueFour = new Integer(20);
		Integer keyFive = new Integer(25);
		Integer valueFive = new Integer(25);
		int tableIndex = 2;
		hashTable.put(keyOne, tableIndex, valueOne);
		hashTable.put(keyTwo, tableIndex, valueTwo);
		hashTable.put(keyThree, tableIndex, valueThree);
		hashTable.put(keyFour, tableIndex, valueFour);
		hashTable.put(keyFive, tableIndex, valueFive);
		assertTrue(hashTable.eFail() == 2);
	}
}
