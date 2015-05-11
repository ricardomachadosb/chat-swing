package br.com.chat.eventos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextField;

import br.com.chat.servidor.Servidor;

/**
 * @author ricardo
 *
 */
public class EventoBtnInitServer implements ActionListener{
	
	private Servidor servidor;
	private JTextField nrPorta;
	private JButton btInitServer;
	
	/**
	 * @param servidor
	 */
	public EventoBtnInitServer(Servidor servidor, JTextField nrPorta, JButton btInitServer) {
		this.servidor = servidor;
		this.nrPorta = nrPorta;
		this.btInitServer = btInitServer;
	}
	
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		servidor.initServer(nrPorta, btInitServer);
	}

}
