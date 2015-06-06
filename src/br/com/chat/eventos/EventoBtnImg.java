package br.com.chat.eventos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

import br.com.chat.client.ClienteChat;
import br.com.chat.util.Utilities;

/**
 * @author ricardo
 *
 */
public class EventoBtnImg implements ActionListener{
	private JLabel imgPath;
	private JLabel jLabelIcon;
	private JButton btConnect;
	private JButton btInitServer;
	private int linha;
	private ClienteChat clienteChat;
	
	/**
	 * @param imgPath
	 * @param jLabelIcon
	 * @param btConnect
	 * @param btInitServer
	 * @param linha
	 * @param jFrame
	 */
	public EventoBtnImg(JLabel imgPath, JLabel jLabelIcon, JButton btConnect, JButton btInitServer, int linha, ClienteChat clienteChat) {
		this.imgPath = imgPath;
		this.jLabelIcon = jLabelIcon;
		this.btConnect = btConnect;
		this.btInitServer = btInitServer;
		this.linha = linha;
		this.clienteChat = clienteChat;
	}
	
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
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
	        btConnect.setBounds(160, linha + 140, 100, 23 );
	        btInitServer.setBounds(30, linha + 140, 110, 23);
	        clienteChat.setBounds(400, 100, 350, 350);
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	}

}
