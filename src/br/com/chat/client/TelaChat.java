package br.com.chat.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.json.JSONArray;
import org.json.JSONObject;

import br.com.chat.util.BaseInterface;
import br.com.chat.util.ImageUtil;


public class TelaChat extends BaseInterface implements WindowListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Socket socket;
	private Recebedor recebedor;
	
	private JTextArea areaChat;
	private JTextField texto;
	private JButton btEnviar;
	
	public TelaChat( Socket socket, int coluna, String titulo, Recebedor recebedor, ImageIcon imagemPessoal, ImageIcon imagemRemota ) {
		
		this.socket = socket;
		
		setTitle( titulo );
		setBounds( coluna, 200, 650, 400 );
		setLayout( null );
		
		JLabel jLabelIcon = new JLabel();
		jLabelIcon.setBounds( 500, 30, 120, 120);
		jLabelIcon.setIcon( imagemPessoal );
		getContentPane().add( jLabelIcon );
		
		JLabel jLabelIconRemoto = new JLabel();
		jLabelIconRemoto.setBounds( 500, 180, 120, 120);
		jLabelIconRemoto.setIcon( imagemRemota );
		getContentPane().add( jLabelIconRemoto );
		
		areaChat = new JTextArea();
		JScrollPane sp = new JScrollPane( areaChat );
		sp.setBounds( 5, 10, 470, 300 );
		areaChat.setEditable( false );
		
		getContentPane().add( sp );
		
		texto = new JTextField();
		texto.setBounds( 5, 320, 380, 23 );
		getContentPane().add( texto );
		
		
		JButton bt = new JButton( "Enviar" );
		bt.setBounds( 390, 320, 80, 23 );
		getContentPane().add( bt );
		btEnviar = bt;
		
		bt.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				enviaTexto();
			}
		});

		setVisible( true );
		addWindowListener( this );
		
		recebedor.setAreaChat(this.areaChat);
		recebedor.setBtEnviar(btEnviar);
		recebedor.setjFrame(this);
		recebedor.setTexto(texto);
		
		this.recebedor = recebedor;

	}
	
	private void enviaTexto() {
		
		String txt = texto.getText();
		
		if( txt.length() > 0 ) {
			
			areaChat.setText( areaChat.getText() + recebedor.getNomeUsuario() + " diz: " + txt + "\n" );
			texto.setText( "" );
			texto.requestFocusInWindow();
			
			enviaPeloSocket( txt );
		}
	}
	
	/**
	 * @param txt
	 */
	private void enviaPeloSocket( String txt ) {
		try {
			OutputStream os = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream( os );

			JSONObject transacao = new JSONObject();
			transacao.put( "nroTransacao", 2 );
			transacao.put( "mensagem", txt );
			
			dos.writeUTF( transacao.toString() );
			
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog( this, "Não foi possível enviar sua mensagem: " + e.getMessage() );
		}
	}
	
	public void informaConexaoAceita(String nome, ImageIcon imagemServidor, JSONArray ImagemClienteJasonFormat, String nomeImagemServidor){
		try {
			OutputStream os = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream( os );

			JSONObject transacao = new JSONObject();
			transacao.put( "nroTransacao", 3);
			transacao.put( "mensagem", nome );
				if( imagemServidor != null ) transacao.put( "imagemServidor", ImageUtil.iconToByte( imagemServidor, ImageUtil.getFileExtension(nomeImagemServidor)));
				if( ImagemClienteJasonFormat != null) transacao.put( "imagemCliente", ImagemClienteJasonFormat );
			
			dos.writeUTF( transacao.toString() );
			
		} catch (Exception e) {
			e.printStackTrace();
			
			JOptionPane.showMessageDialog( this, "Não foi possível enviar sua mensagem: " + e.getMessage() );
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {

		try {
			OutputStream os = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream( os );

			JSONObject transacao = new JSONObject();
			transacao.put( "nroTransacao", 11 );
			
			dos.writeUTF( transacao.toString() );
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog( this, "Não foi possível enviar sua mensagem: " + e.getMessage() );
		}
	}

	@Override
	public void windowClosing(WindowEvent arg0) {}

	@Override
	public void windowDeactivated(WindowEvent arg0) {}

	@Override
	public void windowDeiconified(WindowEvent arg0) {}

	@Override
	public void windowIconified(WindowEvent arg0) {}

	@Override
	public void windowOpened(WindowEvent arg0) {}
}