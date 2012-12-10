package CPSC331Assignment2;

/**
 *
 * Interface for a simple stack of integers, for use in CPSC&nbsp;331.
 * <br />
 *
 * <p>
 *  <strong>Interface Invariant:</strong>
 *  Access is provided to a <code>stack</code> of values that are each
 *  of type <code>int:</code> The integer that is visible at the top
 *  of the <code>stack</code> is the integer that has been most
 *  recently pushed onto it (and not yet removed).
 * </p>
 *
 * <p>
 *  <strong>Reference:</strong>
 *  This is based on the interface <code>StackInt.java</code>
 *  that is listed on page&nbsp;259 of Elliot B. Koffman and
 *  Paul A. T. Wolfgang&rsquo;s text, &ldquo;Objects,
 *  Abstraction, Data Structures and Design using Java
 *  Version&nbsp;5.0,&rdquo; John Wiley &amp; Sons, 2005.
 *  </p>
 *
 * @author Wayne Eberly
 *
 */
 
/*
 * Additional Notes:
 *
 * - A constructor should be included (as part of any class that
 *   implements this interface) that creates an empty stack.
 *
 * - See the lecture notes on stacks, as well as information in
 *   Chapter 5 of the textbook, for information about this
 *   abstract data type.
 *
 */

public interface IStackInterface{

    /**
     *
     * This method pushes an integer onto the top of the stack.
     * <br /><br />
     *
     * <strong>Precondition:</strong>
     * The Interface invariant is satisfied.
     * <br /> <br />
     *
     * <strong>Postcondition:</strong>
     * <ol style="list-style-type: lower-alpha">
     * <li> The Interface Invariant is satisfied. </li>
     * <li> The input integer has been pushed onto the top of the
     *      stack (which has otherwise been unchanged). </li>
     * <li> The value returned is the value that was provided
     *      as input. </li>
     * </ol>
     *
     * @param obj The integer to be pushed onto the stack <br /><br />
     * @return The object inserted
     *
     */

    public int push (int obj);

    /**
     *
     * This method returns the object at the stack and removes it.
     * <br /><br />
     *
     * <strong>Precondition 1:</strong>
     * <ol style="list-style-type: lower-alpha">
     * <li> The Interface invariant is satisfied.</li>
     * <li> The stack is not empty. </li>
     * </ol>
     * <br />
     * 
     * <strong>Postcondition 1:</strong>
     * <ol style="list-style-type: lower-alpha">
     * <li> The Interface Invariant is satisfied. </li>
     * <li> The value returned is the integer that was at the top of
     *      the stack when this method was called. </li>
     * <li> The value returned has been removed from the top of the
     *      stack, which has otherwise been unchanged. </li>
     * </ol>
     * <br />
     *
     * <strong>Precondition 2:</strong>
     * <ol style="list-style-type: lower-alpha">
     * <li> The Interface invariant is satisfied.</li>
     * <li> The stack is empty. </li>
     * </ol>
     * <br />
     *
     * <strong>Postcondition 2:</strong>
     * <ol style="list-style-type: lower-alpha">
     * <li> The Interface Invariant is satisfied. </li>
     * <li> An <code>EmptyStackException</code> is thrown. </li>
     * <li> The stack has not been changed. </li>
     * </ol>
     *
     * @return The integer at the top of the stack <br /><br />
     * @throws EmptyStackException if the stack is already empty
     *
     */

    public int pop ();


    /**
     *
     * This methods reports the value at the top of the stack
     * without changing it, or throws an
     * <code>EmptyStackException</code> if the stack is empty.
     * <br /><br />
     *
     * <strong>Precondition 1:</strong>
     * <ol style="list-style-type: lower-alpha">
     * <li> The Interface invariant is satisfied. </li>
     * <li> The stack is not empty. </li>
     * </ol>
     * <br />
     *
     * <strong>Postcondition 1:</strong>
     * <ol style="list-style-type: lower-alpha">
     * <li> The Interface Invariant is satisfied. </li>
     * <li> The value returned is the integer at the top of
     *      the stack. </li>
     * <li> The stack has not been changed. </li>
     * </ol>
     * <br />
     *
     * <strong>Precondition 2:</strong>
     * <ol style="list-style-type: lower-alpha">
     * <li> The Interface invariant is satisfied. </li>
     * <li> The stack is empty. </li>
     * </ol>
     * <br />
     *
     * <strong>Postcondition 2:</strong>
     * <ol style="list-style-type: lower-alpha">
     * <li> The Interface Invariant is satisfied. </li>
     * <li> An <code>EmptyStackException</code> is thrown. </li>
     * <li> The stack has not been changed. </li>
     * </ol>
     *
     * @return The integer at the top of the stack <br /><br /> 
     * @throws EmptyStackException if the stack is already empty
     *
     */

    public int peek ();

    /**
     *
     * This method reports whether the stack is empty.
     * <br /><br />
     *
     * <strong>Precondition:</strong>
     * The Interface Invariant is satisfied.
     * <br /><br />
     *
     * <strong>Postcondition:</strong>
     * <ol style="list-style-type: lower-alpha">
     * <li> The Intervace Invariant is satisfied. </li>
     * <li> Value returned is <code>true</code> if the stack
     *      is empty, and <code>false</cose> otherwise </li>
     * <li> The stack has not been changed. </li>
     * </ol>
     *
     * @return <code>true</code> if the stack is empty and
     *         <code>false</code> otherwise
     *
     */

    public boolean empty();
    
}
