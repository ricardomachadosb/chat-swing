package br.com.chat.client;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.json.JSONObject;

import br.com.chat.eventos.EventoBtnConect;
import br.com.chat.eventos.EventoBtnImg;
import br.com.chat.eventos.EventoBtnInitServer;
import br.com.chat.interfaces.BaseInterface;
import br.com.chat.servidor.Servidor;
import br.com.chat.util.Utilities;


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
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		
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
		
		btConnect.addActionListener(new EventoBtnConect(this));
		btInitServer.addActionListener(new EventoBtnInitServer(servidor, nrPorta, btInitServer));
		btImg.addActionListener(new EventoBtnImg(imgPath, jLabelIcon, btConnect, btInitServer, linha, this));
		
		setVisible( true );
	}
	
	public void conectar() {

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
	
	/**
	 * 
	 */
	private void solicitaAutorizacao() {
		try {
			OutputStream os = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream( os );

			JSONObject transacao = new JSONObject();
			transacao.put( "nroTransacao", 1 );
			transacao.put( "nome", nomeUsuario.getText());
			
			if( jLabelIcon.getIcon() != null ){
				transacao.put( "imagem", Utilities.iconToByte(jLabelIcon.getIcon(), Utilities.getFileExtension(imgPath.getText())));
				this.recebedor.setImagemCliente( (ImageIcon)jLabelIcon.getIcon());
			}

			dos.writeUTF( transacao.toString() );
			
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog( this, "Não foi possível enviar sua mensagem: " + e.getMessage() );
		}
	}
}