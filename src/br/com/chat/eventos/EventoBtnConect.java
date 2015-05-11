package br.com.chat.eventos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import br.com.chat.client.ClienteChat;

/**
 * @author ricardo
 *
 */
public class EventoBtnConect implements ActionListener{
	
	ClienteChat clienteChat;
	
	/**
	 * @param clienteChat
	 */
	public EventoBtnConect(ClienteChat clienteChat) {
		this.clienteChat = clienteChat;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		clienteChat.conectar();
	}

}
