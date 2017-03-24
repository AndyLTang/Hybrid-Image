package ca.cmpt.hybridimage;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class HybridModel {

	// default values
	private final float[] BLUR = {
			1/9f, 1/9f, 1/9f,
			1/9f, 1/9f, 1/9f,
			1/9f, 1/9f, 1/9f
	};
	
	private final float[] SHARPEN = {
			0f, -1f,  0f,
			-1f, 5f, -1f,
			0f, -1f,  0f
	};
	
	private final float[] SOBEL = {
			 1f,  0f, -1f,
			 2f,  0f, -2f,
			 1f,  0f, -1f
	};
	
	private final float[] EMBOSS = {
			-2f, -1f,  0f, 
			-1f,  1f,  0f, 
			 0f,  1f,  2f
	};
	
	private final float[] EDGE = {
			-1f, -1f, -1f,
			-1f,  8f, -1f,
			-1f, -1f, -1f,
	};
	
	private final String DEFAULT_IMG_PATH_1 = "images/lion.jpg";
	private final String DEFAULT_IMG_PATH_2 = "images/tiger.jpg";
	public final int IMG_WIDTH = 546;
	public final int IMG_HEIGHT = 357;
	public final String[] FILTERS = {"SHARPEN", "SOBEL", "EMBOSS", "EDGE"};
	
	// data structures to hold io
	public ArrayList<String> paths = new ArrayList<String>();
	public ArrayList<BufferedImage> inputs = new ArrayList<BufferedImage>();
	public ArrayList<BufferedImage> outputs = new ArrayList<BufferedImage>();
	public String filter = FILTERS[0];
	public float dissolveAmt = 0.5f;
	
	/*
	 * Constructor
	 */
	public HybridModel() {
		reset();
	}

	
	/*
	 * Functions for image processing
	 */
	private int clip(int v) {
		v = v > 255 ? 255 : v;
		v = v < 0 ? 0 : v;
		return v;
	}
	
	private int getRed(int rgb) {
		return new Color(rgb).getRed();
	}

	private int getGreen(int rgb) {
		return new Color(rgb).getGreen();
	}

	private int getBlue(int rgb) {
		return new Color(rgb).getBlue();
	}
	
	// return a grey scale version of the image
	private BufferedImage greyscale(BufferedImage img) {
		BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

		for (int x = 0; x < result.getWidth(); x++) {
			for (int y = 0; y < result.getHeight(); y++) {
				int rgb = img.getRGB(x, y);
				float[] hsv = new float[3];
				Color.RGBtoHSB(getRed(rgb), getGreen(rgb), getBlue(rgb), hsv);
				result.setRGB(x, y, Color.HSBtoRGB(0, 0, hsv[2]));
			}
		}

		return result;
	}

	// return an image where one image is the other image's opacity's compliment added together
	private BufferedImage dissolve(BufferedImage imgA, BufferedImage imgB, float mixVal) {
		BufferedImage result = new BufferedImage(imgA.getWidth(), imgA.getHeight(), imgA.getType());

		for (int x = 0; x < result.getWidth(); x++) {
			for (int y = 0; y < result.getHeight(); y++) {
				int rgbA = imgA.getRGB(x, y);
				int rgbB = imgB.getRGB(x, y);

				// O = (MV * A) + [(1 – MV) * B]
				int r = clip((int) ((mixVal * getRed(rgbA)) + (1 - mixVal) * getRed(rgbB)));
				int g = clip((int) ((mixVal * getGreen(rgbA)) + (1 - mixVal) * getGreen(rgbB)));
				int b = clip((int) ((mixVal * getBlue(rgbA)) + (1 - mixVal) * getBlue(rgbB)));

				result.setRGB(x, y, new Color(r, g, b).getRGB());
			}
		}

		return result;
	}
	
	// perform filter on image and return result
	private BufferedImage filterImage(BufferedImage img, float[] filter, int filterW, int filterH){
		BufferedImage result;
		Kernel k = new Kernel(filterW, filterH, filter);
		ConvolveOp op = new ConvolveOp(k, ConvolveOp.EDGE_NO_OP, null);
		result = op.filter(img, null);
		
		return result;
	}
	
	// create hybrid image and return it
	private BufferedImage createHybridImage(BufferedImage imgA, BufferedImage imgB, float[] filter, float dissolveAmt) {
		BufferedImage result;
		BufferedImage highpass, lowpass;
		
		highpass = filterImage(imgA, BLUR, 3, 3);
		lowpass = filterImage(imgB, filter, 3, 3);
		
		result = dissolve(lowpass, highpass, dissolveAmt);
		return result;
	}

	// read image from a filepath, then return the image
	private BufferedImage readImage(String path){
		BufferedImage img = null;
		
		try{
			img = ImageIO.read(new File(path));
		} catch (IOException e){
			e.printStackTrace();
		}
		
		return img;
	}
	
	// resize an image to specified width and height
	public Image scaleImage(BufferedImage img, int width, int height){
		return img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	}
	
	// convert Image to BufferedImage
	public BufferedImage toBufferedImage(Image img){
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimg = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimg.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimg;
	}
	
	/*
	 * Methods
	 */
	
	public void runReadImages(){
		
		for (int i = 0; i < paths.size(); i++){
			inputs.add( readImage(paths.get(i)) );
		}
		
	}
	
	public void runHybridImages() {
		
		int skip = 2;	// hybrid image uses 2 images, so skip the next image after creating 1 output
		
		float[] selectedFilter = switchFilters();
		
		// run hybrid image alg' on each pair of image throughout inputs
		// create a greyscale version and one with color, then append to outputs
		for (int i = 0; i < inputs.size(); i = i + skip){
			BufferedImage result = createHybridImage(inputs.get(i), inputs.get(i+1), selectedFilter, dissolveAmt);
			outputs.add( greyscale(result) );
			outputs.add( result );
		}
		
	}

	private float[] switchFilters() {
		// default filter (identity matrix)
		float[] selectedFilter = { 0,0,0, 0,1,0, 0,0,0 };

		// change filter
		switch(filter){
		case "SHARPEN":
			selectedFilter = SHARPEN;
			break;
		case "SOBEL":
			selectedFilter = SOBEL;
			break;
		case "EMBOSS":
			selectedFilter = EMBOSS;
			break;
		case "EDGE":
			selectedFilter = EDGE;
			break;
		default:
			break;
		}
		
		return selectedFilter;
	}
	
	public void reset(){
		paths.clear();
		inputs.clear();
		outputs.clear();
		
		paths.add(DEFAULT_IMG_PATH_1);
		paths.add(DEFAULT_IMG_PATH_2);
		
		runReadImages();
		runHybridImages();
	}
	
}
