package br.com.chat.client;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Recebedor extends Thread {
	private Socket socket;
	private JTextArea areaChat;
	private JFrame jFrame;
	private JTextField texto;
	private JButton btEnviar;
	private String nomeUsuario;
	private String nomeContato;
	
	public void setNomeUsuario( String nomeUsuario ){
		this.nomeUsuario = nomeUsuario;
	}
	
	public String getNomeUsuario(){
		return this.nomeUsuario;
	}
	
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
						case 1: System.out.println( rec.get( "imagem" ) );
						JOptionPane.showConfirmDialog( null, "Imagem", "Imagem", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
								new ImageIcon(ImageIO.read(new ByteArrayInputStream(objectToBytArray(rec.get( "imagem"))))));
								//new ImageIcon((byte[])rec.get( "imagem" )));
							confirmaChat(rec.getString( "mensagem" ));
							break;
						case 3:
							this.nomeContato = rec.getString( "mensagem" );
							new TelaChat(socket, 300, nomeUsuario, this); 
							break;
						case 2: areaChat.setText( areaChat.getText() + nomeContato + " diz:" + rec.getString( "mensagem" ) + "\n" );
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
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public void setAreaChat(JTextArea areaChat) {
		this.areaChat = areaChat;
	}

	public void setjFrame(JFrame jFrame) {
		this.jFrame = jFrame;
	}

	public void setTexto(JTextField texto) {
		this.texto = texto;
	}

	public void setBtEnviar(JButton btEnviar) {
		this.btEnviar = btEnviar;
	}

	/**
	 * @param nomeUsuario
	 * @return
	 */
	public void confirmaChat(String nomeUsuario){
		int resp = JOptionPane.showConfirmDialog(null, "Usuário: " + nomeUsuario, "Confirmação", JOptionPane.YES_NO_OPTION);
		switch(resp){
			case 0: confirmaChatYes(nomeUsuario);
				break;
			//TODO tratar o case nada
		}
	}
	
	/**
	 * 
	 */
	public void confirmaChatYes(String nomeUsuario){
		String nome = JOptionPane.showInputDialog("Informe seu nome");
		TelaChat t = new TelaChat(socket, 555, nome, this);
		nomeContato = nomeUsuario;
		this.nomeUsuario = nome;
		t.informaConexaoAceita(nome);
	}
	
	public byte[] objectToBytArray( Object ob ) throws JSONException{
		JSONArray j = (JSONArray) ob;
		byte[] novoB = new byte[j.length()];
		for(int i = 0; i < j.length(); i++){
			novoB[i] = ((Integer)j.get(i)).byteValue();
		}
		return novoB;
	}
}
