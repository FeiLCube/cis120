import static org.junit.Assert.*;

import org.junit.Test;

/**
 * This file tests the various image manipulations on the default
 * image (a view of Positano, Italy). 
 *
 * Note though that due to the imprecise nature of floating point
 * arithmetic, your code may be correct yet not pass these tests.  (We
 * won't be using these tests to grade your assignment! Our tests take
 * this imprecision into account.) Instead, these tests give you an
 * idea of how close your answers are to *our* solution. Just looking
 * at the output of your code in the GUI may not be enough to see the
 * differences.
 *
 * This file also gives you a place to add your own JUnit test cases.
 * Do *not* add them to ManipulateTest.java as you will not be
 * submitting that file.
 */

public class MyTest {

    /*
     * IMPORTANT: Make sure that this points to the correct directory before
     * running the tests! You will need to download and extract the provided
     * images from the homework instructions for this to work. (And don't forget
     * that the string should end with a '/' character!)
     */
    static final String LOCATION = "/Users/FeiRubiksCube/Desktop/images/";

    static final PixelPicture italy = new PixelPicture(LOCATION + "Italy.png");

    @Test
    public void testRotateCW() {
        assertEquals("Rotate CW", 0,
                PixelPicture.diff(new PixelPicture(LOCATION + "ItalyCW.png"),
                        SimpleManipulations.rotateCW(italy)));
    }

    @Test
    public void testRotateCCW() {
        assertEquals("Rotate CCW", 0,
                PixelPicture.diff(new PixelPicture(LOCATION + "ItalyCCW.png"),
                        SimpleManipulations.rotateCCW(italy)));

    }

    @Test
    public void testBorder() {
        assertEquals("Border", 0,
                PixelPicture.diff(new PixelPicture(LOCATION + "ItalyBorder.png"),
                        SimpleManipulations.border(italy, 10, Pixel.BLACK)));
    }

    @Test
    public void testColorInvert() {
        assertEquals("ColorInversion", 0,
                PixelPicture.diff(new PixelPicture(LOCATION + "ItalyColorInvert.png"),
                        SimpleManipulations.invertColors(italy)));
    }

    @Test
    public void testGrayScaleAverage() {
        assertEquals("Gray Scale Average", 0,
                PixelPicture.diff(new PixelPicture(LOCATION + "ItalyGrayScaleAverage.png"),
                        SimpleManipulations.grayScaleAverage(italy)));
    }

    @Test
    public void testColorScale() {
        assertEquals("Color Scale", 0,
                PixelPicture.diff(new PixelPicture(LOCATION + "ItalyRedTint.png"),
                        SimpleManipulations.scaleColors(italy, 1.0, 0.5, 0.5)));
    }

    @Test
    public void testAlphaBlend() {
        PixelPicture p = SimpleManipulations.grayScaleAverage(italy);
        assertEquals("alpha-Blend", 0,
                PixelPicture.diff(new PixelPicture(LOCATION + "ItalyBlendGrayScaleAvg.png"),
                        SimpleManipulations.alphaBlend(0.5, italy, p)));
    }

    @Test
    public void testContrast() {
        assertEquals("Contrast", 0,
                PixelPicture.diff(new PixelPicture(LOCATION + "ItalyContrast2.png"),
                        AdvancedManipulations.adjustContrast(italy, 2.0)));
    }

    @Test
    public void testReducePalette() {
        assertEquals("Reduce Palette 512", 0,
                PixelPicture.diff(new PixelPicture(LOCATION + "ItalyRP512.png"),
                        AdvancedManipulations.reducePalette(italy, 512)));
    }

    @Test
    public void testVignette() {
        assertEquals("Vignette", 0,
                PixelPicture.diff(new PixelPicture(LOCATION + "ItalyVignette.png"),
                        SimpleManipulations.vignette(italy)));
    }

    @Test
    public void testBlur() {
        assertEquals("blur 2", 0,
                PixelPicture.diff(new PixelPicture(LOCATION + "ItalyBlur2.png"),
                        AdvancedManipulations.blur(italy,2)));
    }


}
