package br.com.chat.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * @author ricardo
 *
 */
public class ImageUtil {
	
	/**
	 * @param icon
	 * @return
	 * @throws IOException 
	 * @throws JSONException 
	 */
	public static byte[] iconToByte(Icon icon) throws IOException, JSONException {
		ImageIcon imgIcon = (ImageIcon)icon;
		BufferedImage image = (BufferedImage)((Image) imgIcon.getImage());
		//BufferedImage image = ImageIO.read(new File("icon.png")); 
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
		ImageIO.write(image, "jpg", baos);
		byte[] u = baos.toByteArray();
		return u;
		
	}
	
	/**
	 * @param w
	 * @param h
	 * @param img
	 * @return
	 * @throws Exception
	 */
	public static BufferedImage scaleImage(int w, int h, BufferedImage img) throws Exception {
	    BufferedImage bi;
	    bi = new BufferedImage(w, h, BufferedImage.TRANSLUCENT);
	    Graphics2D g2d = (Graphics2D) bi.createGraphics();
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
	    g2d.drawImage(img, 0, 0, w, h, null);
	    g2d.dispose();
	    return bi;
	}
	
	/**
	 * @param ob
	 * @return
	 * @throws JSONException
	 */
	public static byte[] objectToBytArray( JSONArray ob ) throws JSONException{
		JSONArray j = (JSONArray) ob;
		byte[] novoB = new byte[j.length()];
		for(int i = 0; i < j.length(); i++){
			novoB[i] = ((Integer)j.get(i)).byteValue();
		}
		return novoB;
	}
	
	/**
	 * @param j
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	public static ImageIcon getImageIcon(JSONArray j) throws IOException, JSONException{
		return new ImageIcon(ImageIO.read(new ByteArrayInputStream(objectToBytArray(j))));
	}
}
