package br.com.chat.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.json.JSONObject;

import br.com.chat.interfaces.BaseInterface;


public class TelaIdentificacao extends BaseInterface implements WindowListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Socket socket;
	private JTextField nomeUsuario;
	
	public TelaIdentificacao( Socket socket, int coluna, String titulo ) {
		
		setBounds( 400, 100, 320, 180 );
		setLayout( null );
		
		getContentPane().add( getJLabel( 20, "Nome" ) );
		getContentPane().add( nomeUsuario = getJTextField( 150 ) );
		
		saltoLinha();
		saltoLinha();
		
		JButton bt = new JButton( "Conectar" );
		bt.setBounds( 120, linha, 100, 23 );
		getContentPane().add( bt );
		
		bt.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				solicitaAutorizacao();
			}
		});
		
		this.socket = socket;
		
		setVisible( true );
	}
	
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













