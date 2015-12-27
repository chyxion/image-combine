package me.chyxion.image;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static me.chyxion.image.ImageCombine.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

/**
 * @version 0.0.1
 * @since 0.0.1
 * @author Shaun Chyxion <br />
 * chyxion@163.com <br />
 * Dec 26, 2015 8:22:03 PM
 */
public class TestDriver {
	private static final Logger log = 
		LoggerFactory.getLogger(TestDriver.class);
	private ImageCombine ic = new ImageCombine();

	@Test
	public void testCombineImage2() throws Exception {
		saveImage(ic.combine(
			readImage("joker.jpg"), 
			readImage("lufy.png"), 
			512),
		"combine-2.png");
	}

	@Test
	public void testCombineImage3() throws Exception {
		saveImage(ic.combine(
			readImage("joker.png"), 
			readImage("lufy.png"), 
			readImage("zoro.png"), 
			512),
		"combine-3.png");
	}

	@Test
	public void testCombineImage4() throws Exception {
		saveImage(ic.combine(
			readImage("ace.png"), 
			readImage("gemily.png"), 
			readImage("lufy.png"), 
			readImage("zoro.png"), 
			512),
		"combine-4.png");
	}

	@Test
	public void testCombineImage5() throws Exception {
		saveImage(ic.combine(
			readImage("ace.png"), 
			readImage("gemily.png"), 
			readImage("joker.jpg"), 
			readImage("lufy.png"), 
			readImage("zoro.png"), 
			512),
		"combine-5.png");
	}

	@Test
	public void testCropToCircle() throws Exception {
		saveImage(ic.cropToCircle(readImage("joker.jpg")), 
			"joker-circle.png");
	}

	@Test
	public void testCropImageToSquare() throws Exception {
		saveImage(ic.cropToSquare(readImage("joker.jpg")), 
			"joker-square.png");
	}

	@Test
	public void testWrapWhiteCircle() throws Exception {
		saveImage(ic.wrapWhiteCircle(readImage("lufy.png"), 256), 
			"lufy-white-circle.png");
	}
	
	private BufferedImage readImage(String name) {
		InputStream ins = null;
		try {
			ins = this.getClass().getResourceAsStream("/images/" + name);
			if (ins == null) {
				throw new IllegalStateException(
					"No Class Path Image [" + name + "] Found");
			}
			return ImageIO.read(ins);
		}
		catch (IOException e) {
			throw new IllegalStateException(
				"Read Image [" + name + "] Error Caused", e);
		}
		finally {
			if (ins != null) {
				try {
					ins.close();
				}
				catch (IOException e) {
					log.error("Close Class Path Image Input Stream [{}] Error Caused.", 
						name, e);
				}
			}
		}
	}
	
	private void saveImage(BufferedImage image, String name) {
		try {
			File file = new File("target", name);
			ImageIO.write(image, "png", file);
			log.info("Save Image [{}].", file.getAbsolutePath());
		}
		catch (IOException e) {
			throw new IllegalStateException(
				"Save png Image Error Caused", e);
		}
	}
}
