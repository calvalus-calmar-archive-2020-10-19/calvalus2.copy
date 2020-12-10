package com.bc.calvalus.processing.ma;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * @author MarcoZ
 * @author Norman
 */
public class PixelExtractorTest {

    @Test
    public void testFlipIntArray() throws Exception {
        int[] original = {
                0, 1,
                2, 3,
                4, 5
        };
        int[] flipY = {
                1, 0,
                3, 2,
                5, 4
        };
        int[] flipX = {
                4, 5,
                2, 3,
                0, 1
        };
        int[] flipBoth = {
                5, 4,
                3, 2,
                1, 0
        };

        assertArrayEquals(original, PixelExtractor.flipIntArray(new int[]{0, 1, 2, 3, 4, 5}, 2, 3, false, false));
        assertArrayEquals(flipY, PixelExtractor.flipIntArray(new int[]{0, 1, 2, 3, 4, 5}, 2, 3, false, true));
        assertArrayEquals(flipX, PixelExtractor.flipIntArray(new int[]{0, 1, 2, 3, 4, 5}, 2, 3, true, false));
        assertArrayEquals(flipBoth, PixelExtractor.flipIntArray(new int[]{0, 1, 2, 3, 4, 5}, 2, 3, true, true));

        assertArrayEquals(new int[]{1, 0}, PixelExtractor.flipIntArray(new int[]{0, 1}, 1, 2, true, true));
        assertArrayEquals(new int[]{3, 2, 1, 0}, PixelExtractor.flipIntArray(new int[]{0, 1, 2, 3}, 2, 2, true, true));
    }
}
