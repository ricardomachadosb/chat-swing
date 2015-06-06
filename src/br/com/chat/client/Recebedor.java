package br.com.chat.client;

import java.io.DataInputStream;
import java.io.File;
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

import br.com.chat.util.Utilities;

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
	private File file;
	private String fileName;
	private JButton uploadButton;
	
	public JButton getUploadButton() {
		return uploadButton;
	}

	public void setUploadButton(JButton uploadButton) {
		this.uploadButton = uploadButton;
	}

	JSONArray arquivo = new JSONArray();
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	private JSONArray imagemClienteJsonFormat;
	
	public JSONArray getImagemClienteJsonFormat() {
		return imagemClienteJsonFormat;
	}

	public void setImagemClienteJsonFormat(JSONArray imagemClienteJsonFormat) {
		this.imagemClienteJsonFormat = imagemClienteJsonFormat;
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
						case -1: 
							JOptionPane.showMessageDialog( null, "O usuário recusou.");
							break;
						case 1: if( rec.has( "imagem" ) ){
									imagemClienteJsonFormat  = (JSONArray) rec.get( "imagem" );
									setCliente( Utilities.getImageIcon(imagemClienteJsonFormat));
									confirmaChat(rec.getString( "nome" ) );
								}else{
									confirmaChat(rec.getString( "nome" ));
								}
							break;
						case 2:
							this.nomeContato = rec.getString( "nome" );
							if( rec.has("imagem")){
								
								setImagemServidor(Utilities.getImageIcon((JSONArray) rec.get( "imagem" )));
							}
							new TelaChat(socket, 300, nomeUsuario, this,  this.imagemServidor,  this.imagemCliente); 
							break;
						case 3: areaChat.setText( areaChat.getText() + nomeContato + " diz:" + rec.getString( "mensagem" ) + "\n" );
							    break;
						case 4:
							JSONArray file = rec.getJSONArray("files");
							for (int i = 0; i < file.length(); i++) {
								arquivo.put(file.get(i));
						    }
							break;
						case 5:
							this.uploadButton.setText( "Enviar Arquivo" );
							this.uploadButton.setEnabled( true );
							break;
						case 6:
							areaChat.setText(areaChat.getText() + "\n Concluido");
							break;
						case 8:
							receivedFileRequest( rec.getString( "fileName" ) );
							break;
						case 9:
							Utilities.downloadFile(this.fileName, arquivo);
							arquivo = new JSONArray();
							Utilities.sendPackage( this.socket, 5);
							this.uploadButton.setText( "Enviar Arquivo" );
							this.uploadButton.setEnabled( true );
							break;
						case 10:
							Utilities.uploadFile( getFile().getAbsolutePath(), socket );
							this.uploadButton.setText( "Enviando..." );
							this.uploadButton.setEnabled( false );
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
			case -1: Utilities.sendPackage( this.socket, -1);
				break;
		}
	}
	/**
	 * 
	 */
	public void confirmaChatYes(String nomeUsuario){
		HashMap<String, Object> coisas = Utilities.getJframe();
		setServidor( (ImageIcon) coisas.get( "imagem" ) );
		String nome = (String) coisas.get("nome");
		String nomeImg = (String) coisas.get("nomeImagem");
		TelaChat t = new TelaChat(socket, 555, nome, this, this.cliente, this.servidor);
		nomeContato = nomeUsuario;
		this.nomeUsuario = nome;
		t.informaConexaoAceita(nome, servidor, nomeImg);
	}
	
	/**
	 * Used when receive a file upload request.
	 * 
	 * @param name
	 */
	private void receivedFileRequest( String fileName ) {
		int resp = JOptionPane.showConfirmDialog( null, "O usuário " + " solicita a transferiencia do arquivo: " + fileName, null, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		switch (resp) {
		case 0:
			this.fileName = fileName;
			Utilities.sendPackage( this.socket, 10);
			this.uploadButton.setText( "Enviando..." );
			this.uploadButton.setEnabled( false );
			break;
		default:
			Utilities.sendPackage( this.socket, -1);
			break;
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