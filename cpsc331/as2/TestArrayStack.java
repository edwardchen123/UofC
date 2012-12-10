/*
 * For documentation, please see "Assignment 2 Written Answers", question 4.
 */

package CPSC331Assignment2;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.EmptyStackException;

public class TestArrayStack
{ 
	@Test
	public void test_Constructor()
	{
		System.out.println("Testing constructor...");
		IArrayStack s = new IArrayStack();
		assertTrue(s.getSize() == 0 && s.getCapacity() == 8);
	}

	@Test
	public void test_Push_Basic()
	{
		System.out.println("Testing basic use of push() method...");
		IArrayStack s = new IArrayStack();
		int n = 5;
		int returnedValue = s.push(n);
		assertTrue((returnedValue == n && s.getSize() == 1) && s.getCapacity() == 8);
	}

	@Test
	public void test_Push_Expand()
	{
		System.out.println("Testing push() method when expansion is required...");
		IArrayStack s = new IArrayStack();
		int n = 5;
		for (int i = 0; i < 8; i++)
		{
			s.push(n);
		}
		int returnedValue = s.push(n);
		assertTrue((returnedValue == n && s.getSize() == 9) && s.getCapacity() == 16);
	}

	@Test
	public void test_Pop_Basic()
	{
		System.out.println("Testing basic use of pop() method...");
		IArrayStack s = new IArrayStack();
		int n = 5;
		s.push(n);
		int returnedValue = s.pop();
		assertTrue((returnedValue == n && s.getSize() == 0) && s.getCapacity() == 8);
	}

	@Test(expected = EmptyStackException.class)
	public void test_Pop_Empty()
	{
		System.out.println("Testing use of pop() method on an empty stack...");
		IArrayStack s = new IArrayStack();
		s.pop();
	}

	@Test
	public void test_Pop_Contract()
	{
		System.out.println("Testing pop() method when contraction is required...");
		IArrayStack s = new IArrayStack();
		int n = 5;
		for (int i = 0; i <= 8; i++) //after loop concludes, s.getCapacity() should equal 64
		{
			s.push(n);
		}
		for (int i = 0; i <= 4; i++) //after loop concludes, s.getCapacity() should equal 32
		{
			s.pop();
		}
		int returnedValue = s.pop();
		assertTrue((returnedValue == n && s.getSize() == 3) && s.getCapacity() == 8);
	}

	@Test
	public void test_Peek_Basic()
	{
		System.out.println("Testing basic use of peek() method...");
		IArrayStack s = new IArrayStack();
		int n = 5;
		s.push(n);
		assertTrue((s.peek() == n && s.getSize() == 1) && s.getCapacity() == 8);
	}

	@Test(expected = EmptyStackException.class)
	public void test_Peek_Empty()
	{
		System.out.println("Testing use of peek() method on an empty stack...");
		IArrayStack s = new IArrayStack();
		s.peek();
	}

	@Test
	public void test_Empty_NonEmptyStack()
	{
		System.out.println("Testing use of empty() method on a nonempty stack...");
		IArrayStack s = new IArrayStack();
		s.push(5);
		assertFalse(s.empty());
	}

	@Test
	public void test_Empty_EmptyStack()
	{
		System.out.println("Testing use of empty() method on an empty stack...");
		IArrayStack s = new IArrayStack();
		assertTrue(s.empty());
	}
}
