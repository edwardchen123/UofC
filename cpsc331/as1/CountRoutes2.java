/**
 * Prints the number of routes between (0, 0) and (n, m), given that n and m are nonnegative
 * integers and only movement to the north or east is possible.
 */

/**
 * @author Andrew Helwer (10023875)
 */

import java.math.BigInteger;

public class CountRoutes2
{

	/**
	 * Prints the number of routes between (0, 0) and (n, m), given that n and m are nonnegative
	 * integers and only movement to the north or east is possible.
	 */

	public static void main(String[] args)
	{
		int n = 0;
		int m = 0;
		try
		{
			n = Integer.parseInt(args[0]);
			m = Integer.parseInt(args[1]);
		}
		catch (NumberFormatException x)
		{
			System.err.println("Sorry! You must provide exactly two nonnegative integers as input.");
			System.exit(1);
		}
		catch (ArrayIndexOutOfBoundsException x)
		{
			System.err.println("Sorry! You must provide exactly two nonnegative integers as input.");
			System.exit(1);
		}
		
		
		BigInteger r = BigInteger.valueOf(0);
		try
		{
			r = fNumberRoutes(n, m);
		}
		catch (IllegalArgumentException x)
		{
			System.err.println("Sorry! You must provide exactly two nonnegative integers as input.");
			System.exit(1);
		}

		System.out.println(r);
	}

	/**
	 * Receives coordinate (n, m) and returns the number of routes to that location from (0, 0)
	 * This method accepts integers n and m, where n is the number of streets to the east the destination
	 * is, and m is the number of streets to the north the destination is. The method then calculates the
	 * number of possible routes between (0, 0) and (n, m), given you can only travel to the east or north.
	 * The method does this by making a two-dimensional array A, and then filling in the appropriate values
	 * based on the array index (for instance, the coordinate (3, 4) is A[3][4]). If the coordinate being
	 * filled in has either of its values as zero, then it will be filled with a value of one. Otherwise the
	 * array is filled according to the formula A[i][j] = A[i-1][j] + A[i][j-1].
	 *
	 * <p>
	 * <strong>Precondition:</strong>
	 * 	<code>n</code> is a nonnegative integer.<br />
	 * 	<code>m</code> is a nonnegative integer.<br />
	 * <strong>Postcondition:</strong>
	 * 	Value returned is the number of routes between intersections (0, 0) and (n, m), given it is
	 *	only possible to travel north or east
	 * </p>
	 *
	 * @param n	nonnegative integer indicating number of streets to the east the destination is
	 * @param m	nonnegative integer indicating number of streets to the north the destination is
	 * @return	the number of routes between (0, 0) and (n, m)
	 * @throws IllegalArgumentException	thrown if either n or m have values less than zero
	 */

	public static BigInteger fNumberRoutes(int n, int m)
	{
		if (n >= 0 && m >= 0) // Checks that n and m are both nonnegative
		{
		
			BigInteger[][] A = new BigInteger[n+1][m+1]; // Creates a new (n+1)x(m+1) two-dimensional array

			for (int i = 0; i < n+1; i++)
			{
				/*
				* Loop Invariant: i is an integer such that 0 <= i < n+1
				* Loop Variant: (n+1)-(i+1)
				*/
				for (int j = 0; j < m+1; j++)
				{
					/*
					* Loop Invariant: j is an integer such that 0 <= i < m+1
					* Loop Variant: (m+1)-(j+1)
					*/
					if (i == 0 || j == 0)
					{	
						A[i][j] = BigInteger.valueOf(1);
					}
					else
					{
						A[i][j] = A[i-1][j].add(A[i][j-1]);
					}
				}
			}

			return A[n][m];
		}
		else // Throws an IllegalArgumentException if n or m is nonnegative
		{
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Receives coordinate (n, m) and returns the number of routes to that location from (0, 0).
	 * This method accepts integers n and m, where n is the number of streets to the east the destination
	 * is, and m is the number of streets to the north the destination is. The method then calculates the
	 * number of possible routes between (0, 0) and (n, m), given you can only travel to the east or north.
	 * The method does this using recursion, since the number of routes to a coordinate (i, j) is given
 	 * by the sum of the routes to coordinates (i-1, j) and (i, j-1). The base case of the recursive function
	 * is R(i, 0) = 1, where 0 <= i <= n, and R(0, j) = 1, where 0 <= j <= m.
	 *
	 * <p>
	 * <strong>Precondition:</strong>
	 * 	<code>n</code> is a nonnegative integer.<br />
	 * 	<code>m</code> is a nonnegative integer.<br />
	 * <strong>Postcondition:</strong>
	 * 	Value returned is the number of routes between intersections (0, 0) and (n, m), given it is
	 *	only possible to travel north or east
	 * </p>
	 *
	 * @param n	nonnegative integer indicating number of streets to the east the destination is
	 * @param m	nonnegative integer indicating number of streets to the north the destination is
	 * @return	the number of routes between (0, 0) and (n, m)
	 * @throws IllegalArgumentException	thrown if either n or m have values less than zero
	 */

	public static BigInteger sNumberRoutes(int n, int m)
	{
		if (n > 0 && m > 0) // General case
		{
			return (sNumberRoutes(n-1, m).add(sNumberRoutes(n, m-1)));
		}
		else if (n == 0 || m == 0) // Base case
		{
			return (BigInteger.valueOf(1));
		}

		/**
		 * One of the above if statements will be true if n and m are nonnegative, and conversely
		 * if n and m are nonnegative, one of the above if statements will be true. If none of the
		 * above if statements evaluated to true, then n and m cannot be nonnegative, and an
		 * IllegalArgumentException is thrown by the 'else' case.
		 */

		else
		{
			throw new IllegalArgumentException();
		}
	}
}
