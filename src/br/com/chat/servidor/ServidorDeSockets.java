package br.com.chat.servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorDeSockets extends Thread {
	
	private ServerSocket serverSoket;
	private boolean continua;
	private EventosDoServidorDeSockets eventos;
	
	public ServidorDeSockets( int nroPorta, EventosDoServidorDeSockets eventos) throws IOException {
		serverSoket = new ServerSocket( nroPorta );
		this.eventos = eventos;
	}
	
	private Socket getSocket() throws IOException {
		
		Socket socket = serverSoket.accept();
		return socket;
	}
	
	@Override
	public void run() {

		eventos.aoIniciarServidor();
		
		continua = true;
		while( continua ) {

			try {
				final Socket s = getSocket();
				
				new Thread() {
					public void run() {
						eventos.aoReceberSocket(s);
					};
				}.start();
				
			} catch (IOException e) {
				if( continua ) {
					eventos.reportDeErro(e);
				}
			}
		}

		eventos.aoFinalizarServidor();
	}
	
	public void finaliza() {
		
		continua = false;
		try {
			serverSoket.close();
		} catch (IOException e) {
		}
	}
	
}









