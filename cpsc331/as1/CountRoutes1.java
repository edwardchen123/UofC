/**
 * Prints the number of routes between (0, 0) and (n, m), given that n and m are nonnegative
 * integers and only movement to the north or east is possible.
 */

/**
 * @author Andrew Helwer (10023875)
 */

public class CountRoutes1
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
		
		long r = 0;
		try
		{
			r = sNumberRoutes(n, m);
		}
		catch (IllegalArgumentException x)
		{
			System.err.println("Sorry! You must provide exactly two nonnegative integers as input.");
			System.exit(1);
		}

		System.out.println(r);
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

	public static long sNumberRoutes(int n, int m)
	{
		if (n > 0 && m > 0) // General case
		{
			return (sNumberRoutes(n-1, m) + sNumberRoutes(n, m-1));
		}
		else if (n == 0 || m == 0) // Base case
		{
			return (1);
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
