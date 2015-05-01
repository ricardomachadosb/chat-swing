package br.com.chat.client;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import br.com.chat.interfaces.BaseInterface;
import br.com.chat.servidor.Servidor;


public class ClienteChat extends BaseInterface {
	private Socket socket;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField endereco;
	private JTextField nrPorta;
	private JTextField nomeUsuario;
	private JLabel jLabelIcon = new JLabel();
	private JLabel imgPath;
	private Servidor servidor = new Servidor();
	
	public ClienteChat() {
		
		setBounds( 400, 100, 350, 230 );
		setLayout( null );
		
		getContentPane().add( getJLabel( 20, "Endereço" ) );
		getContentPane().add( endereco = getJTextField( 150 ) );
		
		getContentPane().add( getJLabel( "Porta" ) );
		getContentPane().add( nrPorta = getJTextField( 80 ) );
		
		getContentPane().add( getJLabel("Nome de usuário", 130 ) );
		getContentPane().add( nomeUsuario = getJTextField( 150 ) );

		getContentPane().add( imgPath = getJLabel("", 130 ) );
		JButton btImg = new JButton( "Escolher Foto" );
		btImg.setBounds( 160, linha, 150, 23 );
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE );
		
		saltoLinha();
		
		jLabelIcon.setBounds(120, saltoLinha(), 120, 120);
		
		getContentPane().add( jLabelIcon);
		
		JButton btConnect = new JButton( "Conectar" );
		btConnect.setBounds( 160, linha, 100, 23 );
		
		JButton btInitServer = new JButton("Init server");
		btInitServer.setBounds( 30, linha, 110, 23 );
		
		getContentPane().add( btImg );
		getContentPane().add( btConnect );
		getContentPane().add( btInitServer );
		
		btConnect.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				conectar();
			}
		});
		
		btInitServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				servidor.initServer(nrPorta, btInitServer);
			}
		});
		
		btImg.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			    JFileChooser chooser = new JFileChooser();
			    chooser.showOpenDialog(null);
			    File f = chooser.getSelectedFile();
			    String filename = f.getAbsolutePath();
			    imgPath.setText(filename);
			    try {
			        ImageIcon ii=new ImageIcon(scaleImage(120, 120, ImageIO.read(new File(f.getAbsolutePath()))));//get the image from file chooser and scale it to match JLabel size
			        jLabelIcon.setIcon(ii);
			        btConnect.setBounds(160, linha + 140, 100, 23 );
			        setBounds(400, 100, 350, 350);
			    } catch (Exception ex) {
			        ex.printStackTrace();
			    }
			}
		});
		
		setVisible( true );
	}
	
	protected void conectar() {

		String end = endereco.getText().trim();
		String prt = nrPorta.getText().trim();
		
		if( end.length() == 0 ) {
			JOptionPane.showMessageDialog( this, "Defina o endereço para conexão" );
			endereco.requestFocusInWindow();
			return;
		}
		
		try {
			int nrPrt = Integer.parseInt( prt );

			try {
				Socket s = new Socket( end, nrPrt );
				comunicaComEsteSocket( s );
			} catch( Exception e ) {
				JOptionPane.showMessageDialog( this, "Erro: " + e.getMessage()  );
			}
		} catch( Exception e ) {
			JOptionPane.showMessageDialog( this, "Defina o número da porta para conexão" );
			nrPorta.requestFocusInWindow();
			return;
		}
	}
	
	private void comunicaComEsteSocket(Socket s) {
		
		new TelaChat( s, 510, "Cliente" );
	}

	public static void main(String[] args) {
		new ClienteChat();
	}
	
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
	
	private Socket getSocket(String end, int nrPort) throws UnknownHostException, IOException{
		if(this.socket == null){
			this.socket = new Socket(end, nrPort);
		}
		return this.socket;
	}
	
}