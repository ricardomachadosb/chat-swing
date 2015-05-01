package br.com.chat.servidor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import br.com.chat.client.Recebedor;
import br.com.chat.client.TelaChat;

public class Servidor extends JFrame implements ActionListener, EventosDoServidorDeSockets, WindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton botao;
	private ServidorDeSockets servidor;
	
	public static void main(String[] args) {
		new Servidor();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if( servidor == null ) {
			try {
				servidor = new ServidorDeSockets( 1843, this );
				servidor.start();
				botao.setText( "Finalizar" );
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} else {
			servidor.finaliza();
			botao.setText( "Iniciar" );
			servidor = null;
		}
	}

	@Override
	public void aoIniciarServidor() {}

	@Override
	public void aoFinalizarServidor() {}

	@Override
	public void aoReceberSocket(Socket s) {
		Recebedor recebedor = new Recebedor(s);
		recebedor.start();
	}

	@Override
	public void reportDeErro(IOException e) {
		JOptionPane.showMessageDialog( this, "Erro: " + e.getMessage()  );
	}

	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {
		
		if( servidor != null ) {
			servidor.finaliza();
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
	
	public void initServer(JTextField nrPorta, JButton btnControll){
		String prt = nrPorta.getText().trim();
		
		try {
			int nrPrt = Integer.parseInt( prt );

			try {
				if( servidor == null ) {
					try {
						servidor = new ServidorDeSockets( nrPrt, this );
						servidor.start();
						btnControll.setText( "Finalizar" );
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					servidor.finaliza();
					btnControll.setText( "Init Server" );
					servidor = null;
				}
			} catch( Exception e ) {
				JOptionPane.showMessageDialog( this, "Erro: " + e.getMessage()  );
			}
		} catch( Exception e ) {
			JOptionPane.showMessageDialog( this, "Defina o número da porta para conexão" );
			nrPorta.requestFocusInWindow();
			return;
		}
	}
}










