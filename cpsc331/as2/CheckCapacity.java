package CPSC331Assignment2;

import CPSC331Assignment2.IArrayStack;

/**
 *
 * A simple check of the size and capacity of an IArrayStack.
 *
 * @author Wayne Eberly
 *
 */

public class CheckCapacity {

    /**
     *
     * Main method creates an empty stack, pushes 1025 elements
     * onto it, and then removes them all again.
     */

    public static void main(String[] args) {

	IArrayStack s = new IArrayStack();
	System.out.print("Initial size:     ");
        System.out.println(s.getSize());
	System.out.print("Initial capacity: ");
        System.out.println(s.getCapacity());
        System.out.println();

        for (int i = 0; i <= 128; i++) {

	    s.push(i);
	    System.out.print("Added:   ");
            System.out.printf("%3d", i);
            System.out.print("     Size: ");
            System.out.printf("%3d", s.getSize());
            System.out.print("     Capacity: ");
            System.out.printf("%3d", s.getCapacity());
            System.out.println();

	};
	System.out.println();

	for (int i = 0; i <= 128; i++) {

	    System.out.print("Removed: ");
            System.out.printf("%3d", s.pop());
            System.out.print("     Size: ");
            System.out.printf("%3d", s.getSize());
            System.out.print("     Capacity: ");
            System.out.printf("%3d", s.getCapacity());
            System.out.println();

	}

    }

}