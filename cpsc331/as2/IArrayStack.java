 package CPSC331Assignment2;
 
 import java.util.EmptyStackException;
 
/**
 *
 * Array-based stack of integers, for Assignment&nbsp;2
 * in CPSC&nbsp;331.
 * <br /><br />
 *
 * <p>
 *  <strong>Class Invariant:</strong>
 *  <ol style="list-style-type: lower-alpha">
 *  <li> <p>
 *        Access is provided to a <code>stack</code> of values that are each
 *        of type <code>int:</code> The integer that is visible at the top
 *        of the <code>stack</code> is the integer that has been most
 *        recently pushed onto it (and has not yet been removed).
 *       </p>
 *  </li>
 *
 *  <li> <p>
 *        The contents of the stack are stored in a static array
 *        <code>StackContents</code> (which may be replaced as
 *        needed to ensure that the following constraints on the
 *        capacity of the stack are satisfied), and the integer
 *        <code>top</code> is used to store the index of the top of
 *        the stack, so that <code>top</code>=&minus;1 if the stack
 *        is empty, and so that the elments of the stack are stored
 *        in positions
 *       </p>
 *       <p style="text-align: center">
 *        <code>StackContents[0]</code>,
 *        <code>StackContents[1]</code>, &hellip;
 *        <code>StackContents[top]</code
 *       </p>
 *       <p>
 *        otherwise, with the element at the bottom of stack stored
 *        at position&nbsp;0 and with the element at the top of the
 *        stack stored at position&nbsp;<code>top</code>
 *       </p>
 *
 *       <p>
 *        Thus the size of the stack is equal to <code>top</code>+1.
 *       </p>
 *  </li>
 *
 *  <li> <p>
 *        The <code>length</code> of the array <code>StackContents</code>
 *        is greater than or equal to 8 (the value of the constant
 *        <code>INITIAL_CAPACITY</code>) and is a power of two.
 *       </p>
 *
 *       <p>
 *        Furthermore, if the <code>length</code> is <i>greater than</i>
 *        the <code>INITIAL_CAPACITY</code> then the size of the stack
 *        is greater to or equal to one-quarter of the
 *        <code>length</code>, that is,
 *       </p>
 *       <p style="text-align: center">
 *        if <code>length</code>&gt;<code>INITIAL_CAPACITY</code> then
 *          4&times;<code>size</code>&ge;<code>length</code>
 *       </p>
 *       <p>
 *        where <code>length</code> is the length of the array
 *        (that is, the current capacity of the stack) and where
 *        <code>size=top+1</code> is the number of entries that
 *        are currently stored on the stack.
 *       </p>
 *  </li>
 *  </ol>
 *
 */
 
 public class IArrayStack implements IStackInterface {

  // Data Fields

  /**
   *
   * The detault initial capacity
   */
   private static final int INITIAL_CAPACITY = 8;

  /**
   *
   * The underlying array of integers used to store stack contents
   *
   */
   private int[] StackContents;

  /**
   *
   * The position in the array of the current top of the stack;
   * set to &minus;1 if the stack is currently empty
   */
   private int top;

   /**
    *
    * This constructor produces an empty stack. <br />
    *
    * <p>
    *  <strong>Precondition:</strong> None </strong>
    * </p>
    * <p>
    *  <strong>Postcondition:</strong>
    * </p>
    * <ol style="list-style-type: lower-alpha">
    * <li> The Class Invariant is satisfied. </li>
    * <li> <code>StackContents.length</code> = <code>INITIAL_CAPACITY</code>
    *      </li>
    * <li> <code>top</code> = &minus;1 </code>
    * </ol>
    *
    */

   public IArrayStack () {

     StackContents = new int[INITIAL_CAPACITY];
     top = -1;
   
   }

   /**
    *
    * This method pushes an object onto the top of the stack..
    * <br /><br />
    *
    * <strong>Precondition:</strong>
    * The Class Invariant is satisfied.
    * <br /><br />
    *
    * <strong>Postcondition:</strong>
    * <ol style="list-style-type: lower-alpha">
    * <li> The Class Invariant is satisfied. </li>
    * <li> The input integer has been pushed onto the top of the
    *       stack (whose contents have not been changed in any
    *       other way). </li>
    * <li> The value returned is the value that was provided
    *      as input. </li>
    * </ol>
    *
    * @param obj The integer to be pushed onto the stack
    * @return The object inserted
    *
    */
   
	public int push (int obj)
	{
		if (top == StackContents.length-1)
		{
			expandStack();
		}
		StackContents[top+1] = obj;
		top += 1;
		return obj;
   	}

   /**
    *
    * This method returns the object at the top of the stack and removes it.
    * <br /><br />
    *
    * <strong>Precondition 1:</strong>
    * <ol style="list-style-type: lower-alpha">
    * <li> The Class Invariant is satisfied. </li>
    * <li> The stack is not empty, so that <code>top</code>&ge;0 <li>
    * </ol>
    * <br />
    *
    * <strong>Postcondition 1:</strong>
    * <ol style="list-style-type: lower-alpha">
    * <li> The Class Invariant is satisfied. </li>
    * <li> The value returned is the integer that was at the top
    *      of the stack when this method was called. </li>
    * <li> The value returned has been removed from the top of the
    *      stack; the contents of the stack have not been changed
    *      in any other way. </li>
    * </ol>
    * <br />
    *
    * <strong>Precondition 2:</strong>
    * <ol style="list-style-type: lower-alpha">
    * <li> The Class Invariant is satisfied. </li>
    * <li> The stack is empty, so that <code>top</code>=&minus;1. </li>
    * </ol>
    * <br />
    *
    * <strong>Postcondition 2:</strong>
    * <ol style="list-style-type: lower-alpha">
    * <li> The Class Invariant is satisfied. </li>
    * <li> An <code>EmptyStackException</code> is thrown. </li>
    * <li> The contents of the stack have not been changed. </li>
    * </ol>
    *
    * @return The integer at the top of the stack
    * @throws EmptyStackException if the stack is already empty
    *
    */
   
   
	public int pop ()
	{
		if (top > -1)
		{
			int element = StackContents[top];
			top -= 1;
			if (StackContents.length > INITIAL_CAPACITY && top+1 < StackContents.length/4)
			{
				contractStack();
			}	
			return element;
		}
		else
		{
			throw new EmptyStackException();
		}
	}

   /**
    *
    * This method reports the value at the top of the stack
    * without changing it, or throws an
    * <code>EmptyStackException</code> if the stack is empty.
    * <br /><br />
    *
    * <strong>Precondition 1:</strong>
    * <ol style="list-style-type: lower-alpha">
    * <li> The Class Invariant is satisfied. </li>
    * <li> The stack is not empty, so that <code>top</code>&ge;0 <li>
    * </ol>
    * <br />
    * 
    * <strong>Postcondition 1:</strong>
    * <ol style="list-style-type: lower-alpha">
    * <li> The Class Invariant is satisfied. </li>
    * <li> The value returned is the value at the top of
    *      the stack. </li>
    * <li> The stack has not been changed. </li>
    * </ol>
    * <br />
    *
    * <strong>Precondition 2:</strong>
    * <ol style="list-style-type: lower-alpha">
    * <li> The Class Invariant is satisfied. </li>
    * <li> The stack is empty, so that <code>top</code>=&minus;1. </li>
    * </ol>
    * <br />
    *
    * <strong>Postcondition 2:</strong>
    * <ol style="list-style-type: lower-alpha">
    * <li> The Class Invariant is satisfied. </li>
    * <li> An <code>EmptyStackException</code> is thrown. </li>
    * <li> The contents of the stack have not been changed. </li>
    * </ol>
    *
    * @return The integer at the top of the stack
    * @throws EmptyStackException if the stack is already empty
    *
    */

   
	public int peek ()
	{
		if (top > -1)
		{
			return StackContents[top];
		}
		else
		{
			throw new EmptyStackException();
		}
   	}

   /**
    *
    * This method reports whether the stack is empty.
    * <br /><br />
    *
    * <strong>Precondition:</strong>
    * The Class Invariant is satisfied.
    * <br /><br />
    *
    * <strong>Postcondition:</strong>
    * <ol style="list-style-type: lower-alpha">
    * <li> The Class Invariant is satisfied. </li>
    * <li> Value returned is <code>true</code> if the stack
    *      is empty, and <code>false</code> otherwise. </li>
    * <li> The stack has not been changed. </li>
    * </ol>
    *
    * @return <code>true</code> if the stack is empty and
    *         <code>false</code> otherwise
    *
    */
   
   	public boolean empty ()
	{
		if (top > -1)
		{
			return false;
		}
		else
		{
			return true;
		}
   	}

   /**
    *
    * This method reports the current capacity of the stack.
    *
    */

   /*
    *
    * Precondition: The Class Invariant is satisfied.
    *
    * Postconditon:
    * a) The Class Invariant is satisfied.
    * b) The value returned is the capacity of the stack,
    *    that is, the length of the array StackContents.
    * c) Neither the contents of the stack nor the
    *    StackContents array have been changed.
    *
    */

   	protected int getCapacity ()
	{
		return StackContents.length;
   	}

   /**
    *
    * This method reports the current size of the stack.
    *
    */

   /*
    *
    * Precondition: The Class Invariant is satisfied.
    *
    * Postcondition:
    * a) The Class Invariant is satisfied.
    * b) The value returned is the size of the stack.
    * c) Neither the contents of the stack nor the 
    *    StackContents array have been changed.
    *
    */

   	protected int getSize ()
	{
		return top+1;
	}
   /*
    *
    * This method replaces the StackContents array when
    * it is full, in order to double the capacity of the
    * stack.
    *
    * Precondition:
    * a) The Class Invariant is satisifed.
    * b) The stack is full, that is, its size is equal to
    *    its capacity.
    *
    * Postcondition:
    * a) The Class Invariant is satisfied.
    * b) The contents of the stack have not been changed.
    * c) The StackContents has been replaced with an integer
    *    array whose length is twice the length of the array
    *    that was used before the method was called (so that
    *    the capacity of the stack has been doubled).
    *
    */
   
   	private void expandStack ()
	{
		int[] ExpandedArray = new int[StackContents.length*2];
		/*
		* Loop Invariant:
		* i is an integer representing the index of an array such that 0 <= i <= top,
		* where top is the index of the top element of the stack.
		*
		* Loop Variant:
		* f = top - i + 1
		*/
		for (int i = 0; i <= top; i++)
		{
			ExpandedArray[i] = StackContents[i];
		}
		StackContents = ExpandedArray;
	}

   /*
    *
    * This method replaces the StackContents array when
    * the size of the stack is (slightly) less than
    * one-quarter of the length of the array and the
    * length of this array is greater than INITIAL_CAPACITY.
    *
    * In particular, it replaces this array with a new arrray
    * whose length is one-half of the length of the one being replaced.
    *
    * Precondition:
    *  The Class Invariant is satisfied, except for the
    *  condition that "if the capacity of the stack is greater than
    *  INITIAL_CAPACITY, then the size of the stack is is greater than
    *  or equal to the one/quarter of the  capacity of the stack.
    *  However, is is true that (size + 1) is greater than or equal
    *  to capacity/4
    *
    * Postcondition:
    * a) The Class Invariant is now satisfied.
    * b) The contents of the stack have not been changed.
    * c) The original StackElements array has been replaced by
    *    one whose length is one/half of that of the original
    *    (so that the capacity of the stack has also been divided
    *    by two)
    *
    */
 
   	private void contractStack ()	{
 		int[] ContractedArray = new int[StackContents.length/2];
		/*
		* Loop Invariant:
		* i is an integer representing the index of an array such that 0 <= i <= top,
		* where top is the index of the top element of the stack.
		*
		* Loop Variant:
		* f = top - i + 1
		*/
		for (int i = 0; i <= top; i++)
		{
			ContractedArray[i] = StackContents[i];
		}
		StackContents = ContractedArray;
	}
 }
