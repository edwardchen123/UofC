import org.junit.*;
import static org.junit.Assert.*;

/**
 * The class being tested uses a recursive algorithm to calculate the number of different
 * routes from coordinate (0, 0) to (n, m), given that you can only move east and north.
 *
 * Test Cases:
 *
 * The method used to calculate the number of routes should throw an IllegalArgumentException
 * if either of the inputs are less than zero.
 *
 * The method should return 1 if either of the inputs are equal to zero.
 *
 * The method should return 20 if the generic case (3, 3) is calculated.
 */

public class TestSNumberRoutes
{

	@Test(expected = IllegalArgumentException.class)
	public void test_bothNegativeValues()
	{
		System.out.println("Test case n and m are negative...");
		int n = -1;
		int m = -1;
		CountRoutes1.sNumberRoutes(n, m);
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_mNegativeValue()
	{
		System.out.println("Test case n is positive and m is negative...");
		int n = 1;
		int m = -1;
		CountRoutes1.sNumberRoutes(n, m);
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_nNegativeValue()
	{
		System.out.println("Test case n is negative and m is positive...");
		int n = -1;
		int m = 1;
		CountRoutes1.sNumberRoutes(n, m);
	}

	@Test
	public void test_boundaryCase()
	{
		System.out.println("Test case n and m are equal to zero...");
		int n = 0;
		int m = 0;
		assertEquals(CountRoutes1.sNumberRoutes(n, m), 1);
	}

	@Test
	public void test_nZero()
	{
		System.out.println("Test case n is zero and m is greater than zero...");
		int n = 0;
		int m = 10;
		assertEquals(CountRoutes1.sNumberRoutes(n, m), 1);
	}

	@Test
	public void test_mZero()
	{
		System.out.println("Test case n is greater than zero and m is zero...");
		int n = 10;
		int m = 0;
		assertEquals(CountRoutes1.sNumberRoutes(n, m), 1);
	}

	@Test
	public void test_genericCase()
	{
		System.out.println("Test case n and m are greater than zero...");
		int n = 3;
		int m = 3;
		assertEquals(CountRoutes1.sNumberRoutes(n, m), 20);
	}
}
