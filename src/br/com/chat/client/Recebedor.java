package br.com.chat.client;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.json.JSONObject;

public class Recebedor extends Thread {
	private Socket socket;
	private JTextArea areaChat;
	private JFrame jFrame;
	private JTextField texto;
	private JButton btEnviar;
	
	public Recebedor(Socket socket, JTextArea areaChat, JFrame jFrame, JTextField texto, JButton btEnviar) {
		this.socket = socket;
		this.areaChat = areaChat;
		this.jFrame = jFrame;
		this.texto = texto;
		this.btEnviar = btEnviar;
	}
	
	public Recebedor(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		
		try {
			InputStream is = socket.getInputStream();
			DataInputStream dis = new DataInputStream( is );

			while( jFrame == null || jFrame.isVisible() ) {
				
				String msg = dis.readUTF();
				if( msg != null ) {
					
					JSONObject rec = new JSONObject( msg );

					switch( rec.getInt( "nroTransacao" ) ) {
						case 1: confirmaChat(rec.getString( "mensagem" ));
							break;
						case 3: new TelaChat(socket, 300, "Messinja"); 
							break;
						case 2: areaChat.setText( areaChat.getText() + "\n Recebido: " + rec.getString( "mensagem" ) );
							    break;
						case 11: areaChat.setText( areaChat.getText() + "\n ATENÇÂO: o usuário remoto desconectou" );
								texto.setEnabled( false );
								btEnviar.setEnabled( false );
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param nomeUsuario
	 * @return
	 */
	public void confirmaChat(String nomeUsuario){
		int resp = JOptionPane.showConfirmDialog(null, "Usuário: " + nomeUsuario, "Confirmação", JOptionPane.YES_NO_OPTION);
		switch(resp){
			case 0: confirmaChatYes();
			//TODO tratar o case nada
		}
	}
	
	public void confirmaChatYes(){
		TelaChat t = new TelaChat(socket, 555, "Messenget");
		
	}
}
