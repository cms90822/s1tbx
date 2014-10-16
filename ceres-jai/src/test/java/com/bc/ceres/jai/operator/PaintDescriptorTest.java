/*
 * Copyright (C) 2010 Brockmann Consult GmbH (info@brockmann-consult.de)
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option)
 * any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, see http://www.gnu.org/licenses/
 */

package com.bc.ceres.jai.operator;

import org.junit.Before;
import org.junit.Test;

import javax.media.jai.RenderedOp;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class PaintDescriptorTest {

    private BufferedImage source0rgb;
    private BufferedImage source0rgba;
    private BufferedImage source1;

    @Before
    public void setUp() throws Exception {
        source0rgb = new BufferedImage(2, 2, BufferedImage.TYPE_3BYTE_BGR);
        WritableRaster source0rgbData = source0rgb.getWritableTile(0, 0);
        source0rgbData.setPixel(0, 0, new int[]{255, 255, 255});
        source0rgbData.setPixel(1, 0, new int[]{255, 255, 255});
        source0rgbData.setPixel(0, 1, new int[]{255, 255, 255});
        source0rgbData.setPixel(1, 1, new int[]{255, 255, 255});

        source0rgba = new BufferedImage(2, 2, BufferedImage.TYPE_4BYTE_ABGR);
        WritableRaster source0rgbaData = source0rgba.getWritableTile(0, 0);
        source0rgbaData.setPixel(0, 0, new int[]{255, 255, 255, 103});
        source0rgbaData.setPixel(1, 0, new int[]{255, 255, 255, 255}); // !
        source0rgbaData.setPixel(0, 1, new int[]{255, 255, 255, 255});
        source0rgbaData.setPixel(1, 1, new int[]{255, 255, 255, 255});

        source1 = new BufferedImage(2, 2, BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster source1Data = source1.getWritableTile(0, 0);
        source1Data.setSample(0, 0, 0, 255);
        source1Data.setSample(1, 0, 0, 0);
        source1Data.setSample(0, 1, 0, 0);
        source1Data.setSample(1, 1, 0, 127);
    }

    @Test
    public void testRgbBasics() {
        RenderedOp paintedImage = PaintDescriptor.create(source0rgb, source1, Color.RED, null);
        assertNotNull(paintedImage);

        Raster data = paintedImage.getData();
        assertNotNull(data);

        assertEquals(DataBuffer.TYPE_BYTE, data.getSampleModel().getDataType());
        assertEquals(3, data.getSampleModel().getNumBands());
        assertEquals(2, data.getWidth());
        assertEquals(2, data.getHeight());
    }

    @Test
    public void testRgbBasicsWithSemiTransparentColor() {
        RenderedOp paintedImage = PaintDescriptor.create(source0rgb, source1, new Color(255, 0, 0, 127), null);
        assertNotNull(paintedImage);

        Raster data = paintedImage.getData();
        assertNotNull(data);

        assertEquals(DataBuffer.TYPE_BYTE, data.getSampleModel().getDataType());
        assertEquals(4, data.getSampleModel().getNumBands());
        assertEquals(2, data.getWidth());
        assertEquals(2, data.getHeight());
    }

    @Test
    public void testRgbaBasics() {
        RenderedOp paintedImage = PaintDescriptor.create(source0rgba, source1, Color.RED, null);
        assertNotNull(paintedImage);

        Raster data = paintedImage.getData();
        assertNotNull(data);

        assertEquals(DataBuffer.TYPE_BYTE, data.getSampleModel().getDataType());
        assertEquals(4, data.getSampleModel().getNumBands());
        assertEquals(2, data.getWidth());
        assertEquals(2, data.getHeight());
    }

    @Test
    public void testPaintOpaqueRed() {
        Color paintColor = new Color(255, 0, 0);
        RenderedOp paintedImage = PaintDescriptor.create(source0rgba, source1, paintColor, null);
        assertNotNull(paintedImage);

        Raster destData = paintedImage.getData();
        assertArrayEquals(new int[]{255, 255, 255, 103}, destData.getPixel(0, 0, (int[]) null));
        assertArrayEquals(new int[]{255, 0, 0, 255}, destData.getPixel(1, 0, (int[]) null));
        assertArrayEquals(new int[]{255, 0, 0, 255}, destData.getPixel(0, 1, (int[]) null));
        assertArrayEquals(new int[]{255, 127, 127, 255}, destData.getPixel(1, 1, (int[]) null));
    }

    @Test
    public void testPaintSemiTransparentRed() {
        Color paintColor = new Color(255, 0, 0, 127);
        RenderedOp paintedImage = PaintDescriptor.create(source0rgba, source1, paintColor, null);
        assertNotNull(paintedImage);

        Raster destData = paintedImage.getData();
        assertArrayEquals(new int[]{255, 255, 255, 103}, destData.getPixel(0, 0, (int[]) null));
        assertArrayEquals(new int[]{255, 0, 0, 127}, destData.getPixel(1, 0, (int[]) null));
        assertArrayEquals(new int[]{255, 0, 0, 127}, destData.getPixel(0, 1, (int[]) null));
        assertArrayEquals(new int[]{255, 127, 127, 190}, destData.getPixel(1, 1, (int[]) null));
    }

    @Test
    public void testPaintFullyTransparentColor() {
        Color paintColor = new Color(0, 0, 0, 0);
        RenderedOp paintedImage = PaintDescriptor.create(source0rgba, source1, paintColor, null);
        assertNotNull(paintedImage);

        Raster destData = paintedImage.getData();
        assertArrayEquals(new int[]{255, 255, 255, 103}, destData.getPixel(0, 0, (int[]) null));
        assertArrayEquals(new int[]{0, 0, 0, 0}, destData.getPixel(1, 0, (int[]) null));
        assertArrayEquals(new int[]{0, 0, 0, 0}, destData.getPixel(0, 1, (int[]) null));
        assertArrayEquals(new int[]{127, 127, 127, 127}, destData.getPixel(1, 1, (int[]) null));
    }
}