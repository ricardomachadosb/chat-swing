package br.com.chat.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import br.com.chat.interfaces.BaseInterface;


public class ClienteChat extends BaseInterface {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField endereco;
	private JTextField nrPorta;
	
	public ClienteChat() {
		
		setBounds( 400, 100, 320, 180 );
		setLayout( null );
		
		getContentPane().add( getJLabel( 20, "Endereço" ) );
		getContentPane().add( endereco = getJTextField( 150 ) );
		
		getContentPane().add( getJLabel( "Porta" ) );
		getContentPane().add( nrPorta = getJTextField( 80 ) );

		saltaLinha();
		saltaLinha();
		
		JButton bt = new JButton( "Conectar" );
		bt.setBounds( 120, linha, 100, 23 );
		getContentPane().add( bt );
		
		bt.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				conectar();
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
	
}