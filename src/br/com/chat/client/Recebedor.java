package br.com.chat.client;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.json.JSONArray;
import org.json.JSONObject;

import br.com.chat.util.ImageUtil;

public class Recebedor extends Thread {
	private Socket socket;
	private JTextArea areaChat;
	private JFrame jFrame;
	private JTextField texto;
	private JButton btEnviar;
	private String nomeUsuario;
	private String nomeContato;
	private ImageIcon servidor;
	private ImageIcon cliente;
	private ImageIcon imagemCliente;
	private ImageIcon imagemServidor;
	
	private JSONArray imagemClienteJsonFormat;
	
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
						case 1: if( rec.has( "imagem" ) ){
									imagemClienteJsonFormat  = (JSONArray) rec.get( "imagem" );
									setCliente( ImageUtil.getImageIcon(imagemClienteJsonFormat));
									confirmaChat(rec.getString( "mensagem" ) );
								}else{
									confirmaChat(rec.getString( "mensagem" ));
								}
							break;
						case 3:
							this.nomeContato = rec.getString( "mensagem" );
							if( rec.has("imagemServidor")){
								
								setImagemServidor(ImageUtil.getImageIcon((JSONArray) rec.get( "imagemServidor" )));
							}
							if( rec.has( "imagemCliente" ) ){
								imagemClienteJsonFormat  = (JSONArray) rec.get( "imagemCliente" );
								setImagemCliente(ImageUtil.getImageIcon(imagemClienteJsonFormat));
							}
							new TelaChat(socket, 300, nomeUsuario, this,  this.imagemServidor,  this.imagemCliente); 
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

	/**
	 * @param nomeUsuario
	 * @return
	 */
	public void confirmaChat(String nomeUsuario){
		int resp = JOptionPane.showConfirmDialog(null, "O usuário " + nomeUsuario + ",\ndeseja iniciar uma conversa com você.\nVocê aceita?", "Confirmação", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, this.cliente);
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
		HashMap<String, Object> coisas = ImageUtil.getJframe();
		setServidor( (ImageIcon) coisas.get( "imagem" ) );
		String nome = (String) coisas.get("nome");
		String nomeImg = (String) coisas.get("nomeImagem");
		TelaChat t = new TelaChat(socket, 555, nome, this, this.cliente, this.servidor);
		nomeContato = nomeUsuario;
		this.nomeUsuario = nome;
		t.informaConexaoAceita(nome, servidor, imagemClienteJsonFormat, nomeImg);
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
	
	public void setImagemCliente(ImageIcon imagemCliente) {
		this.imagemCliente = imagemCliente;
	}

	public void setImagemServidor(ImageIcon imagemServidor) {
		this.imagemServidor = imagemServidor;
	}

	public void setServidor(ImageIcon servidor) {
		this.servidor = servidor;
	}

	public void setCliente(ImageIcon cliente) {
		this.cliente = cliente;
	}

	public void setNomeUsuario( String nomeUsuario ){
		this.nomeUsuario = nomeUsuario;
	}
	
	public String getNomeUsuario(){
		return this.nomeUsuario;
	}

	public Recebedor(Socket socket) {
		this.socket = socket;
	}
}