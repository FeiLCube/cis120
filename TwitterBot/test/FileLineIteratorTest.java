/* Tests for FileLineIterator */
import static org.junit.Assert.*;

import org.junit.Test;

public class FileLineIteratorTest {
	
	/* Here's a test to help you out,
	 * but you still need to write your own.
	 * 
	 * You don't need to create any files for your tests though.
	 * (Our submission infrastructure actually won't accept any files you make for testing)
	 */

	@Test
	public void testHasNextAndNext() {
		FileLineIterator li = new FileLineIterator("files/simple_test_data.csv");
		assertTrue(li.hasNext());
		assertEquals("0, The end should come here.", li.next());
		assertTrue(li.hasNext());
		assertEquals("1, This comes from data with no duplicate words!", li.next());
		assertFalse(li.hasNext());
	}
	
	/* **** ****** **** WRITE YOUR TESTS BELOW THIS LINE **** ****** **** */

	@Test
	public void testProperties() {
		FileLineIterator li = new FileLineIterator("files/test_codes.csv");
		assertEquals("First middle middle middle last", li.next());
		assertEquals("First middle middle middle middle middle middle last", li.next());
		assertEquals("First middle middle middle middle middle middle middle middle middle middle middle last", li.next());
		assertFalse(li.hasNext());
	}
}

