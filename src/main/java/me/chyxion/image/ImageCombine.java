package me.chyxion.image;

import java.awt.Color;
import java.awt.Polygon;
import org.slf4j.Logger;
import org.imgscalr.Scalr;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.AlphaComposite;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import org.slf4j.LoggerFactory;
import org.imgscalr.Scalr.Method;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * @version 0.0.1
 * @since 0.0.1
 * @author Shaun Chyxion <br />
 * chyxion@163.com <br />
 * Dec 20, 2015 7:45:33 PM
 */
public class ImageCombine {
	protected static final Logger log = 
		LoggerFactory.getLogger(ImageCombine.class);

	/**
	 * combine 2 images
	 * @param left
	 * @param right
	 * @param size
	 * @return
	 */
	public BufferedImage combine(
			final BufferedImage left, 
			final BufferedImage right, 
			final int size) {
		log.info("Combine 2 Images To Size [{}].", size);
		return combine(new Op() {
			public int size() {
				return size;
			}

			public int canvasSize() {
				return ceilInt((size / sinDegree(45) - size + 
					2 * size - crossLength(size)) * sinDegree(45));
			}

			public void process(Graphics2D g2d) {
				int canvasSize = canvasSize();
				// right
				int x = canvasSize - size;
				int y = canvasSize - size;
				log.debug("Draw Right Image To [{}]:[{}].", x, y);
				g2d.drawImage(wrapWhiteCircle(right, size), 
					x, y, null);
				// left
				x = 0;
				y = 0;
				log.debug("Draw Left Image To [{}]:[{}].", x, y);
				g2d.drawImage(wrapWhiteCircle(left, size), 
					x, y, null);
			}
		});
	}

	/**
	 * combine 3 images
	 * @param top
	 * @param right
	 * @param left
	 * @param size
	 * @return
	 */
	public BufferedImage combine(
			final BufferedImage top, 
			final BufferedImage right, 
			final BufferedImage left, 
			final int size) {
		log.info("Combine 3 Images To Size [{}].", size);
		return combine(new Op() {
			public int size() {
				return size;
			}

			public int canvasSize() {
				return ceilInt(size * 2 - crossLength(size));
			}

			public void process(Graphics2D g2d) {
				int canvasSize = canvasSize();
				// 3 circles in a triangle
				int triangleSize = canvasSize - size;
				// triangle's top padding in the canvas
				int topPadding = ceilInt(
					(canvasSize - cosDegree(30) * triangleSize - size) / 2);
				log.debug("Top Padding [{}].", topPadding);
				int bottomY = canvasSize - size - topPadding;
				log.debug("Bottom Y [{}].", bottomY);

				// right
				BufferedImage imageRight = wrapWhiteCircle(right, size);
				int x = canvasSize - size;
				int y = bottomY;
				log.debug("Draw Right Image To [{}]:[{}].", x, y);
				g2d.drawImage(imageRight, x, y, null);
				// top
				x = ceilInt((canvasSize - size) / 2.0);
				y = topPadding;
				log.debug("Draw Top Image To [{}]:[{}].", x, y);
				g2d.drawImage(wrapWhiteCircle(top, size), x, y, null);

				// left
				// draw right on left first
				BufferedImage imageLeft = wrapWhiteCircle(left, size);
				Graphics2D g2dLeft = (Graphics2D) imageLeft.getGraphics();
				g2dLeft.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
					RenderingHints.VALUE_ANTIALIAS_ON);
				g2dLeft.setComposite(AlphaComposite.SrcOver);
				x = canvasSize - size;
				y = 0;
				log.debug("Draw Rigth Image On Left Image [{}]:[{}].", x, y);
				g2dLeft.drawImage(imageRight, x, y, null);
				// draw left to canvas, crop right extra part
				x = 0;
				y = bottomY;
				log.debug("Draw Left Image To [{}]:[{}].", x, y);
				g2d.drawImage(cropToCircle(imageLeft), x, y, null);
			}
		});
	}

	/**
	 * combine 4 images
	 * @param topRight
	 * @param bottomRight
	 * @param bottomLeft
	 * @param topLeft
	 * @param size
	 * @return
	 */
	public BufferedImage combine(
			final BufferedImage topRight, 
			final BufferedImage bottomRight, 
			final BufferedImage bottomLeft, 
			final BufferedImage topLeft, 
			final int size) {
		log.info("Combine 4 Images To Size [{}].", size);
		final int crossLength = ceilInt(crossLength(size));
		return combine(new Op() {
			public int size() {
				return size;
			}

			public int canvasSize() {
				return ceilInt(size * 2 - crossLength);
			}

			public void process(Graphics2D g2d) {
				BufferedImage imageTopLeft = wrapWhiteCircle(topLeft, size);
				int x = 0;
				int y = 0;
				log.debug("Draw Top Left Image To [{}]:[{}].", x, y);
				// top left
				g2d.drawImage(imageTopLeft, x, y, null);

				// bottom left
				x = 0;
				y = size - crossLength;
				log.debug("Draw Bottmon Left Image To [{}]:[{}].", x, y);
				g2d.drawImage(wrapWhiteCircle(bottomLeft, size), 
					x, y, null);
				
				// bottom right
				x = size - crossLength;
				y = size - crossLength;
				log.debug("Draw Bottmon Right Image To [{}]:[{}].", x, y);
				g2d.drawImage(wrapWhiteCircle(bottomRight, size), 
					x, y, null);

				// top right
				x = size - crossLength;
				y = 0;
				log.debug("Draw Top Right Image To [{}]:[{}].", x, y);
				g2d.drawImage(wrapWhiteCircle(topRight, size), 
					x, y, null);

				log.debug("Clear Top Left Image Buttom Left Part.");
				// redraw top left, clear bottom left, cover top right
				Graphics2D g2dTopLeft = 
					(Graphics2D) imageTopLeft.getGraphics();
				g2dTopLeft.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
					RenderingHints.VALUE_ANTIALIAS_ON);
				g2dTopLeft.setComposite(AlphaComposite.Clear);
				g2dTopLeft.fillPolygon(new Polygon(new int[] {0, size, 0}, 
					new int[] {0, 0, size}, 3));
				g2dTopLeft.dispose();
				imageTopLeft.flush();
				log.debug("Redraw Top Left Image To [0]:[0].");
				g2d.drawImage(imageTopLeft, 0, 0, null);
			}
		});
	}

	/**
	 * combine 5 images
	 * @param top
	 * @param topRight
	 * @param bottomRight
	 * @param bottomLeft
	 * @param topLeft
	 * @param size
	 * @return
	 */
	public BufferedImage combine(
			final BufferedImage top, 
			final BufferedImage topRight, 
			final BufferedImage bottomRight,
			final BufferedImage bottomLeft,
			final BufferedImage topLeft,
			final int size) {
		log.info("Combine 5 Images To Size [{}].", size);
		// image center to canvas center radius
		// bottom 2 images distance is [(size - crossSize) / 2], 
		// divide sin(36) is radius
		final int radius = ceilInt((size - crossLength(size)) / 2 / sinDegree(36));
		log.debug("Image Center To Canvase Center Radius [{}].", radius);

		return combine(new Op() {
			public int size() {
				return size;
			}

			public int canvasSize() {
				return ceilInt(cosDegree(18) * radius) * 2 + size;
			}

			public void process(Graphics2D g2d) {
				// top left image offset, add offset to move images to center
				int xOffset = ceilInt(radius - sinDegree(18) / tanDegree(81) * radius);
				log.debug("Move Image To Canvas Center Offset [{}].", xOffset);
				// Top(0, 1)
				BufferedImage imageTop = wrapWhiteCircle(top, size);
				int x = xOffset;
				int y = 0;
				log.debug("Draw Top Image To [{}]:[{}].", x, y);
				g2d.drawImage(imageTop, x, y, null);

				// Top Left(-cos18°, sin18°)
				x = ceilInt(-cosDegree(18) * radius + xOffset);
				y = ceilInt(radius - sinDegree(18) * radius);
				log.debug("Draw Top Left Image To [{}]:[{}].", x, y);
				g2d.drawImage(wrapWhiteCircle(topLeft, size), 
					x, y, null);

				// Bottom Left(-cos54°, -sin54°)
				x = ceilInt(-cosDegree(54) * radius + xOffset);
				y = ceilInt(radius - (-sinDegree(54) * radius));
				log.debug("Draw Bottom Left Image To [{}]:[{}].", x, y);
				g2d.drawImage(wrapWhiteCircle(bottomLeft, size), 
					x, y, null);

				// Bottom Right(cos54°, -sin54°)
				x = ceilInt(cosDegree(54) * radius + xOffset);
				y = ceilInt(radius - (-sinDegree(54) * radius));
				log.debug("Draw Bottom Rigth Image To [{}]:[{}].", x, y);
				g2d.drawImage(wrapWhiteCircle(bottomRight, size), 
					x, y, null);

				// Top Right(cos18°, sin18°)
				x = ceilInt(cosDegree(18) * radius + xOffset);
				y = ceilInt(-sinDegree(18) * radius + radius);
				log.debug("Draw Top Rigth Image To [{}]:[{}].", x, y);
				g2d.drawImage(wrapWhiteCircle(topRight, size), 
					x, y, null);

				// clear top image left part
				log.debug("Clear Top Image Right Part.");
				Graphics2D g2dTop = (Graphics2D) imageTop.getGraphics();
				g2dTop.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
					RenderingHints.VALUE_ANTIALIAS_ON);
				g2dTop.setComposite(AlphaComposite.Clear);
				g2dTop.fill(new Rectangle2D.Double(0, size / 2, size / 2, size));
				g2dTop.dispose();
				imageTop.flush();

				// redraw top image
				log.debug("Redraw Top Image To [{}]:[{}].", xOffset, 0);
				g2d.drawImage(imageTop, xOffset, 0, null);
			}
		});
	}

	/**
	 * wrap image with white circle
	 * @param image
	 * @param size
	 * @return
	 */
	public BufferedImage wrapWhiteCircle(BufferedImage image, int size) {
		log.debug("Wrap White Circle To Size [{}].", size);
	    BufferedImage imageRtn = resize(cropToCircle(image), size);
	    Graphics2D g2d = imageRtn.createGraphics();
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
	    	RenderingHints.VALUE_ANTIALIAS_ON);
	    // draw circle
		g2d.setComposite(AlphaComposite.Src);
		g2d.setColor(Color.WHITE);
		int lineWidth = lineWidth(size);
		log.debug("Draw Circle With Line Width [{}] On Image.", lineWidth);
		g2d.setStroke(new BasicStroke(lineWidth)); 
		int x = lineWidth / 2, y = x;
		int d = size - lineWidth - 1;
		g2d.drawOval(x, y, d, d);
		g2d.dispose();
		imageRtn.flush();

	    return imageRtn;
	}

	/**
	 * crop image to circle
	 * @param image
	 * @return
	 */
	public BufferedImage cropToCircle(BufferedImage image) {
		log.info("Crop Image To Circle, Try To Crop It To Square First.");
		image = cropToSquare(image);
		int size = image.getWidth();
		log.info("Crop Image To Circle By Radius [{}].", size);
	    BufferedImage imageRtn = 
	    	new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2d = imageRtn.createGraphics();

	    // drawing the desired clip shape
	    // in fully opaque white with antialiasing enabled...
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
	    	RenderingHints.VALUE_ANTIALIAS_ON);
	    g2d.setComposite(AlphaComposite.Src);
	    g2d.setColor(Color.WHITE);
	    g2d.fill(new Ellipse2D.Double(0, 0, size, size));

	    // compositing the image on top,
	    // using the white shape from above as alpha source
	    g2d.setComposite(AlphaComposite.SrcAtop);
	    g2d.drawImage(image, 0, 0, null);
		g2d.dispose();
		imageRtn.flush();
	    return imageRtn;
	}

	// -- 
	// protected methods
	
	/**
	 * combine image
	 * @param op
	 * @return
	 */
	protected BufferedImage combine(Op op) {
		int canvasSize = op.canvasSize();
		log.info("Combine Images, Canvas Size [{}].", canvasSize);
		BufferedImage canvas = new BufferedImage(canvasSize, 
			canvasSize, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) canvas.getGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
			RenderingHints.VALUE_ANTIALIAS_ON);
		// clear background
		g2d.setComposite(AlphaComposite.Clear);
		g2d.fillRect(0, 0, canvasSize, canvasSize);
		g2d.setComposite(AlphaComposite.SrcOver);
		op.process(g2d);
		g2d.dispose();
		canvas.flush();
		return resize(canvas, op.size());
	}

	/**
	 * resize image
	 * @param image
	 * @param size
	 * @return
	 */
	protected BufferedImage resize(BufferedImage image, int size) {
		if (size == image.getWidth() && 
			size == image.getHeight()) {
			log.info("Resize Image, Image's Width And Height Equals [{}], Ignore.", size);
			return image;
		}
		log.info("Resize Image To Size [{}]x[{}].", size, size);
		return Scalr.resize(image, Method.QUALITY, size, size);
	}

	/**
	 * crop image to square by shorter side
	 * @param image
	 * @return
	 */
	public BufferedImage cropToSquare(final BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		if (width == height) {
			log.info("Crop Image To Square, Image [{}]x[{}] Is Square Aready, Ignore.", width, height);
			return image;
		}
		int size = height > width ? width : height;
		log.info("Crop Image To Square [{}]x[{}].", size, size);
	    BufferedImage imageRtn = 
	    	new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2d = imageRtn.createGraphics();
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
	    	RenderingHints.VALUE_ANTIALIAS_ON);
	    g2d.setComposite(AlphaComposite.Src);
	    g2d.drawImage(image, ceilInt((size - width) / 2.0), 
	    	ceilInt((size - height) / 2.0), null);
		g2d.dispose();
		imageRtn.flush();
	    return imageRtn;
	}

	/**
	 * return image's around circle line width
	 * @param size
	 * @return
	 */
	protected int lineWidth(int size) {
		int v = size / 11;
		log.debug("Get Image Size [{}] Circle Line Width [{}].", size, v);
		return v;
	}

	/**
	 * return 2 circle images' cross length
	 * @param size
	 * @return
	 */
	protected float crossLength(int size) {
		float v = lineWidth(size) * 2.1f;
		log.debug("Get Size [{}] Cross Length [{}].", size, v);
		return v;
	}
	
	/**
	 * ceil double and cast to int
	 * @param v
	 * @return
	 */
	protected int ceilInt(double v) {
		return (int) Math.ceil(v);
	}

	/**
	 * Math#sin for degree
	 * @param v
	 * @return
	 */
	protected double sinDegree(int v) {
		return Math.sin(degreeToRadian(v));
	}

	/**
	 * Math#cos for degree
	 * @param v
	 * @return
	 */
	protected double cosDegree(int v) {
		return Math.cos(degreeToRadian(v));
	}

	/**
	 * Math#tan for degree
	 * @param v
	 * @return
	 */
	protected double tanDegree(int v) {
		return Math.tan(degreeToRadian(v));
	}

	/**
	 * convert degree to radian
	 * @param v
	 * @return
	 */
	protected double degreeToRadian(double v) {
		double r = 2 * Math.PI / 360 * v;
		log.debug("Convert Degree [{}] To Radian [{}].", v, r);
		return r;
	}

	/**
	 * graphics op
	 */
	protected static interface Op {
		/**
		 * return target size
		 * @return
		 */
		int size();

		/**
		 * return canvas size
		 * @return
		 */
		int canvasSize();
		
		/**
		 * process Graphics2D 
		 * @param g2d
		 */
		void process(Graphics2D g2d);
	}
}
