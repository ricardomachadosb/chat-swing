package br.com.chat.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.chat.interfaces.BaseInterface;

/**
 * @author ricardo
 *
 */
public class Utilities {
	public static int foi = 0;
	/**
	 * @param icon
	 * @return
	 * @throws IOException 
	 * @throws JSONException 
	 */
	public static byte[] iconToByte(Icon icon, String imgFormat) throws IOException, JSONException {
		ImageIcon imgIcon = (ImageIcon)icon;
		BufferedImage image = (BufferedImage)((Image) imgIcon.getImage());
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
		imgFormat = (imgFormat == null ? "png" : imgFormat);
		ImageIO.write(image, imgFormat, baos);
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
	 */
	public static byte[] objectToBytArray( JSONArray ob ){
		JSONArray j = (JSONArray) ob;
		byte[] novoB = new byte[j.length()];
		for(int i = 0; i < j.length(); i++){
			try {
				novoB[i] = ((Integer)j.get(i)).byteValue();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	
	/**
	 * @param bytes
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	public static ImageIcon getImageIcon(byte[] bytes) throws IOException, JSONException{
		return new ImageIcon(ImageIO.read(new ByteArrayInputStream(bytes)));
	}
	
	/**
	 * @return
	 */
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
			        ImageIcon ii =new ImageIcon(Utilities.scaleImage(120, 120, ImageIO.read(new File(f.getAbsolutePath()))));//get the image from file chooser and scale it to match JLabel size
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
		hp.put( "nomeImagem", imgPath.getText() );
		
		return hp;
	}
	
	public static String getFileExtension(String fileName){
		String[] splitedFile= fileName.split("\\.");
		return splitedFile[splitedFile.length - 1];
	}
	
	/**
	 * Uses JSON to send packages through a socket.
	 * You can add itens to send using a HashMap.
	 * 
	 * @param socket
	 * @param transationNumber
	 * @param transationItens
	 */
	public static void sendPackage( Socket socket, int transationNumber, HashMap<String, Object> transationItens ) {
		try {
			OutputStream os = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream( os );

			JSONObject transacao = new JSONObject();
			transacao.put( "nroTransacao", transationNumber );

			for (Entry<String, Object> entry : transationItens.entrySet()) {
				transacao.put( entry.getKey(), entry.getValue() );
			}
			dos.writeUTF( transacao.toString() );
			
		} catch (Exception ex) {
			JOptionPane.showMessageDialog( null, "Não foi possível enviar sua mensagem: " + ex.getMessage() );
		}
	}
	
	/**
	 * Uses JSON to send packages through a socket.
	 * 
	 * @param socket
	 * @param transationNumber
	 */
	public static void sendPackage( Socket socket, int transationNumber ){
		try {
			OutputStream os = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream( os );

			JSONObject transacao = new JSONObject();
			transacao.put( "nroTransacao", transationNumber );

			dos.writeUTF( transacao.toString() );
			
		} catch (Exception ex) {
			JOptionPane.showMessageDialog( null, "Não foi possível enviar sua mensagem: " + ex.getMessage() );
		}
	}
	
	
	/**
	 * @param fileName
	 * @param file
	 * @return
	 */
	public static boolean downloadFile(String fileName, JSONArray file) {
		byte[] fileBytes = objectToBytArray(file);
		
		try {
			File dir = new File(System.getProperty("user.home") + "/chatFiles");
			  if (!dir.exists()) dir.mkdir();

			
			FileOutputStream fos = new FileOutputStream(dir +"/" + fileName);
			fos.write(fileBytes);
			fos.close();
			return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Problemas para salvar o arquivo: " + fileName + " no diret�rio: " + System.getProperty("user.home"));
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Send a selected file through a socket. 
	 * 
	 * @param filePath
	 * @param socket
	 */
	public static void uploadFile( final String filePath, final Socket socket ) {
		Thread thread = new Thread() {
			public void run() {

				byte[] fileBytes = fileToByte( filePath );
				byte[] uploadedFileBytes = new byte[fileBytes.length];
				int valFinal = 3000;
				int valInicial = 0;

				try {
					OutputStream os = socket.getOutputStream();
					DataOutputStream dos = new DataOutputStream(os);

					JSONObject transacao = new JSONObject();
					transacao.put("nroTransacao", 4);

					if (fileBytes.length < 3000) {
						uploadedFileBytes = Arrays.copyOfRange(fileBytes, 0,
								fileBytes.length - 1);
						transacao.put("files", uploadedFileBytes);
						valFinal = fileBytes.length;
						dos.writeUTF(transacao.toString());
					} else {

						while (valInicial <= fileBytes.length) {
							uploadedFileBytes = Arrays.copyOfRange(fileBytes,
									valInicial, valFinal);

							if (valFinal + 3000 > fileBytes.length) {
								valFinal = fileBytes.length;
							} else {
								valFinal += 3000;
							}
							transacao.put("files", uploadedFileBytes);
							dos.writeUTF(transacao.toString());
							valInicial += 3000;
						}
						interrupt();
						valFinal = fileBytes.length;
					}

				} catch (Exception ex) {
					interrupt();
					JOptionPane.showMessageDialog(
							null,
							"Não foi possível enviar sua mensagem: "
									+ ex.getMessage());
				}
				sendPackage(socket, 9);
			}
		};
		thread.start();
	}
	
	/**
	 * Convert file to byte[].
	 * 
	 * @param filePath
	 * @return
	 */
	public static byte[] fileToByte( String filePath ) {
		 Path path = Paths.get( filePath );
		 byte[] data = null;
		 try {
			data = Files.readAllBytes( path );
		} catch ( IOException e ) {
			JOptionPane.showMessageDialog(null, "Problemas para ler o arquivo:" + filePath);
			e.printStackTrace();
		}
		 return data;
	}
}
