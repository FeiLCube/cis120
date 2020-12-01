import org.junit.Test;
import static org.junit.Assert.*;


/** 
 *  Use this file to test your implementation of Pixel.
 * 
 *  We will manually grade this file to give you feedback
 *  about the completeness of your test cases.
 */

public class MyPixelTest {

    /*
     * Remember, UNIT tests should ideally have one point of failure. Below we
     * give you an example of a unit test for the Pixel constructor that takes
     * in three ints as arguments. We use the getRed(), getGreen(), and
     * getBlue() methods to check that the values were set correctly. This one
     * test does not comprehensively test all of Pixel so you must add your own.
     *
     * You might want to look into assertEquals, assertTrue, assertFalse, and
     * assertArrayEquals at the following:
     * http://junit.sourceforge.net/javadoc/org/junit/Assert.html
     *
     * Note, if you want to add global variables so that you can reuse Pixels
     * in multiple tests, feel free to do so.
     */
	
	Pixel testPixel = new Pixel(10, 20, 30);
	
    @Test
    public void testConstructInBounds() {
        Pixel p = new Pixel(40, 50, 60);
        assertEquals(40, p.getRed());
        assertEquals(50, p.getGreen());
        assertEquals(60, p.getBlue());
    }
    /* ADD YOUR OWN TESTS BELOW */
    
    @Test
    public void testConstructOutOfBounds1() {
    	Pixel p = new Pixel(40, 50, 260);
    	assertEquals(40, p.getRed());
        assertEquals(50, p.getGreen());
        assertEquals(255, p.getBlue());
    }
    
    @Test
    public void testConstructOutOfBounds2() {
    	Pixel p = new Pixel(-25, 50, 60);
    	Pixel p1 = new Pixel(10, -25, 50);
    	Pixel p2 = new Pixel(10, 20, -25);
    	Pixel p3 = new Pixel(256, 20, 30);
    	Pixel p4 = new Pixel(10, 256, 30);
    	Pixel p5 = new Pixel(10, 20, 256);
    	int[] arr = {-10,20,30};
    	Pixel p6 = new Pixel(arr);
    	int[] arr1 = {10, -20, 30};
    	Pixel p7 = new Pixel(arr1);
    	int[] arr2 = {10, 20, -30};
    	Pixel p8 = new Pixel(arr2);
    	int[] arr3 = {256, 20, 30};
    	Pixel p9 = new Pixel(arr3);
    	int[] arr4 = {10, 256, 30};
    	Pixel p10 = new Pixel(arr4);
    	int[] arr5 = {10, 20, 256};
    	Pixel p11 = new Pixel(arr5);
    	int[] arr6 = {10, 20, 30, 40};
    	int[] arr7 = {10, 20};
    	Pixel p12 = new Pixel(arr6);
    	Pixel p13 = new Pixel(arr7);
    	Pixel p14 = new Pixel(null);
    	assertEquals(0, p.getRed());
        assertEquals(50, p.getGreen());
        assertEquals(60, p.getBlue());
        assertEquals(0, p1.getGreen());
        assertEquals(0, p2.getBlue());
        assertEquals(255, p3.getRed());
        assertEquals(255, p4.getGreen());
        assertEquals(255, p5.getBlue());
        assertEquals(0, p6.getRed());
        assertEquals(0, p7.getGreen());
        assertEquals(0, p8.getBlue());
        assertEquals(255, p9.getRed());
        assertEquals(255, p10.getGreen());
        assertEquals(255, p11.getBlue());
        assertEquals(10, p12.getRed());
        assertEquals(20, p12.getGreen());
        assertEquals(30, p12.getBlue());
        assertEquals(3, p12.getComponents().length);
        assertEquals(0, p13.getBlue());
        assertEquals(0, p14.getRed());
        assertEquals(0, p14.getGreen());
        assertEquals(0, p14.getBlue());
    }
    
    @Test
    public void testGetters() {
    	assertEquals(10, testPixel.getRed());
    	assertEquals(20, testPixel.getGreen());
    	assertEquals(30, testPixel.getBlue());
    	int[] arr = {10, 20, 30};
    	assertArrayEquals(arr, testPixel.getComponents());
    }
    
    @Test
    public void testDistance() {
    	Pixel p = new Pixel(20, 40, 60);
    	assertEquals(60, p.distance(testPixel));
    	Pixel p3 = new Pixel(5, 15, 25);
    	assertEquals(15, p3.distance(testPixel));
    	Pixel p2 = new Pixel(10, 20, 30);
    	assertEquals(0, p2.distance(testPixel));
    	Pixel p4 = null;
    	assertEquals(-1, testPixel.distance(p4));
    }
    
    @Test
    public void testEquals() {
    	Pixel p = new Pixel(10, 20, 30);
    	Pixel p2 = new Pixel (0, 0, 0);
    	assertEquals(false, p2.equals(testPixel));
    	assertEquals(true, p.equals(testPixel));
    }
    
    @Test
    public void testToString() {
    	assertEquals("(10, 20, 30)", testPixel.toString());
    }

}
