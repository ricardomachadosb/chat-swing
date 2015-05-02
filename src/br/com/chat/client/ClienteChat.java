package br.com.chat.client;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.json.JSONObject;

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
	private Recebedor recebedor;
	
	private Recebedor getRecebedor(){
		recebedor = new Recebedor(socket);
		recebedor.setNomeUsuario(nomeUsuario.getText());
		return recebedor;
	}
	
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
		
		final JButton btConnect = new JButton( "Conectar" );
		btConnect.setBounds( 160, linha, 100, 23 );
		
		final JButton btInitServer = new JButton("Init server");
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
			        ImageIcon ii =new ImageIcon(scaleImage(120, 120, ImageIO.read(new File(f.getAbsolutePath()))));//get the image from file chooser and scale it to match JLabel size
			        jLabelIcon.setIcon(ii);
			        btConnect.setBounds(160, linha + 140, 100, 23 );
			        btInitServer.setBounds(30, linha + 140, 110, 23);
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
				this.socket = new Socket( end, nrPrt );
				comunicaComEsteSocket();
			} catch( Exception e ) {
				JOptionPane.showMessageDialog( this, "Erro: " + e.getMessage()  );
			}
		} catch( Exception e ) {
			JOptionPane.showMessageDialog( this, "Defina o número da porta para conexão" );
			nrPorta.requestFocusInWindow();
			return;
		}
	}
	
	/**
	 * 
	 */
	private void comunicaComEsteSocket(){
		Recebedor recebedor = getRecebedor();
		recebedor.start();
		solicitaAutorizacao();
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
	
	/**
	 * 
	 */
	private void solicitaAutorizacao() {
		try {
			OutputStream os = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream( os );

			JSONObject transacao = new JSONObject();
			transacao.put( "nroTransacao", 1 );
			transacao.put( "mensagem", nomeUsuario.getText());
			
			dos.writeUTF( transacao.toString() );
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog( this, "Não foi possível enviar sua mensagem: " + e.getMessage() );
		}
	}
}