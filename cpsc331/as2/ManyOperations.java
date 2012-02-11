package CPSC331Assignment2;

import CPSC331Assignment2.IArrayStack;

/**
 *
 * A program that includes the use of a large number of stack
 * operations, reporting the number of such operations used at the end.
 *
 * @author Wayne Eberly
 *
 */

public class ManyOperations {

    /**
     *
     * Main method causes a large number of stack operations
     * to be performed.
     *
     */

    public static void main(String[] args) {

	IArrayStack s = new IArrayStack();
	int opCount = 0;      // Number of operations used

	// Start by creating a fairly large stack

	for (int i=0; i <= 4096; i++ ) {

	    s.push(i);
	    opCount++;

	};

	// Repeatedly remove enough elements to cause a contraction,
	// and then add them back in again to cause an expansion

	for (int j=0; j < 1000; j++) {

	    for (int i = 4097; i >= 2048; i--) {

		s.pop();
		opCount++;

	    };

	    for (int i=2047; i <= 4096; i++) {

		s.push(i);
		opCount++;

	    }

	};

	System.out.print("Number of stack operations: ");
	System.out.println(opCount);

    }


}