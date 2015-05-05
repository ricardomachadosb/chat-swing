package br.com.chat.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * @author ricardo
 *
 */
public class ImageUtil {
	public static int foi = 0;
	/**
	 * @param icon
	 * @return
	 * @throws IOException 
	 * @throws JSONException 
	 */
	public static byte[] iconToByte(Icon icon) throws IOException, JSONException {
		ImageIcon imgIcon = (ImageIcon)icon;
		BufferedImage image = (BufferedImage)((Image) imgIcon.getImage());
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
		ImageIO.write(image, "png", baos);
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
	
	public static HashMap<String, Object> getJframe(){
		JTextField nomeUsuario;
		final JButton btOK;
		JButton btImg;
		final JLabel jLabelIcon = new JLabel();
		final JLabel imgPath;
		foi = 0;

		final BaseInterface bi = new BaseInterface("Informe seu nome");
		
		bi.add( bi.getJLabel("Nome de usuário", 130 ) );
		bi.add( nomeUsuario = bi.getJTextField( 150 ) );
		bi.add( btOK = bi.getJbutton( "OK", 115, 160, 60, 23 ) );
		bi.add( btImg = bi.getJbutton( "Escolhe imagem", 110, 100, 150, 23 ) );
		bi.add( imgPath = bi.getJLabel("", 150 ));
		jLabelIcon.setBounds(120, 120, 120, 120);
		bi.add( jLabelIcon );
		bi.setVisible( true );
		
		btImg.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
			    chooser.showOpenDialog(null);
			    File f = chooser.getSelectedFile();
			    String filename = f.getAbsolutePath();
			    imgPath.setText(filename);
			    try {
			        ImageIcon ii =new ImageIcon(ImageUtil.scaleImage(120, 120, ImageIO.read(new File(f.getAbsolutePath()))));//get the image from file chooser and scale it to match JLabel size
			        jLabelIcon.setIcon(ii);
			        bi.setBounds(400, 100, 350, 350);
			        btOK.setBounds( 115, 250, 60, 23);
			    } catch (Exception ex) {
			        ex.printStackTrace();
			    }
			}
				
			
		});
		while( foi != 2 ){
			btOK.addActionListener( new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
						foi = 2;
						bi.setVisible(false);
				}
			});

		} 
		HashMap<String, Object> hp = new HashMap<String, Object>();
		hp.put( "nome", nomeUsuario.getText());
		hp.put( "imagem", jLabelIcon.getIcon() );
		
		return hp;
	}
}
