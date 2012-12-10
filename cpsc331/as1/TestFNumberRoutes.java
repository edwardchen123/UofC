import org.junit.*;
import static org.junit.Assert.*;
import java.math.BigInteger;

/**
 * The class being tested uses a two-dimensional array to calculate the number of different
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
 *
 * The number returned by fNumberRoutes should always equal the number returned by sNumberRoutes.
 */

public class TestFNumberRoutes
{
	@Test(expected = IllegalArgumentException.class)
	public void test_bothNegativeValues()
	{
		System.out.println("Test case n and m are negative...");
		int n = -1;
		int m = -1;
		CountRoutes2.fNumberRoutes(n, m);
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_mNegativeValue()
	{
		System.out.println("Test case n is positive and m is negative...");
		int n = 1;
		int m = -1;
		CountRoutes2.fNumberRoutes(n, m);
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_nNegativeValue()
	{
		System.out.println("Test case n is negative and m is positive...");
		int n = -1;
		int m = 1;
		CountRoutes2.fNumberRoutes(n, m);
	}

	@Test
	public void test_boundaryCase()
	{
		System.out.println("Test case n and m are equal to zero...");
		int n = 0;
		int m = 0;
		assertEquals(CountRoutes2.fNumberRoutes(n, m), BigInteger.valueOf(1));
	}

	@Test
	public void test_nZero()
	{
		System.out.println("Test case n is zero and m is greater than zero...");
		int n = 0;
		int m = 10;
		assertEquals(CountRoutes2.fNumberRoutes(n, m), BigInteger.valueOf(1));
	}

	@Test
	public void test_mZero()
	{
		System.out.println("Test case n is greater than zero and m is zero...");
		int n = 10;
		int m = 0;
		assertEquals(CountRoutes2.fNumberRoutes(n, m), BigInteger.valueOf(1));
	}

	@Test
	public void test_genericCase()
	{
		System.out.println("Test case n and m are greater than zero...");
		int n = 3;
		int m = 3;
		assertEquals(CountRoutes2.fNumberRoutes(n, m), BigInteger.valueOf(20));
	}

	@Test
	public void test_compareSF()
	{
		System.out.println("Comparing the results from sNumberRoutes and fNumberRoutes...");
		// Numbers picked were less than ten to reduce runtime from sNumberRoutes
		int n = 9;
		int m = 5;
		assertEquals(CountRoutes2.sNumberRoutes(n, m), CountRoutes2.fNumberRoutes(n, m));
	}
}
