package framework.imageFilters;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;

import framework.Image;
import framework.Utils;

/**
 * Randomly modify the RGB values of the image pixels to create a distorted effect.
 */
public class BasicVariance implements ImageFilter{
	/** Maximum amount to change the pixels (min is 0) **/
	private int varAmount;
	
	public BasicVariance()
	{
		this.varAmount = 25;
	}
	public BasicVariance(int varAmount)
	{
		this.varAmount = varAmount;
	}
	
	public void filter(Image img)
	{
		BufferedImage image = img.getImage();
		Random rand = new Random();
		for (int x = 0; x < image.getWidth(); x++)
		{
			for (int y = 0; y < image.getHeight(); y++)
			{
				Color color = new Color(image.getRGB(x, y), true);
				if (color.getRGB() != Color.TRANSLUCENT)
				{
					int red = color.getRed() + (rand.nextInt(varAmount - (varAmount / 2)));
					red = Utils.makeInRange(red, 0, 255);
					int blue = color.getBlue() + (rand.nextInt(varAmount - (varAmount / 2)));
					blue = Utils.makeInRange(blue, 0, 255);
					int green = color.getGreen() + (rand.nextInt(varAmount - (varAmount / 2))); 
					green = Utils.makeInRange(green, 0, 255);
					color = new Color(red, green, blue, color.getAlpha());
					image.setRGB(x, y, color.getRGB());
				}
			}
		}
	}
}