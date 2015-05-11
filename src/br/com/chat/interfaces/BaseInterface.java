package br.com.chat.interfaces;

import java.awt.Container;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class BaseInterface extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int linha;
	private int saltoLinha = 27;
	protected Container container;
	
	public BaseInterface() {
		this( "FEEVALE" );
	}

	public BaseInterface( String titulo ) {
		
		setBounds( 120, 200, 700, 550 );
		
		setLayout( null );
		container = getContentPane();
		
		setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
	}
	
	protected void setLinha(int linha) {
		this.linha = linha;
	}
	
	protected int getLinha() {
		return linha;
	}
	 
	protected void setSaltoLinha(int saltoLinha) {
		this.saltoLinha = saltoLinha;
	}
	
	protected int getSaltoLinha() {
		return saltoLinha;
	}
	
	public JTextField getJTextField( int largura ) {

		JTextField tf = new JTextField();
		tf.setBounds( 160, linha, largura, 23 );
		
		return tf;
	}

	public int saltoLinha() {
		return linha += saltoLinha;
	}
	
	public JLabel getJLabel( String label ) {
		return( getJLabel( linha + saltoLinha, label ) );
	}
	
	public JLabel getJLabel( String label, int largura ) {
		return( getJLabel( linha + saltoLinha, label, largura ) );
	}

	public JLabel getJLabel( int linha, String label ) {

		this.linha = linha;
		JLabel lbl = new JLabel( label );
		lbl.setBounds( 30, linha, 100, 23 );
		
		return lbl;
	}
	
	protected JLabel getJLabel( int linha, String label, int largura ) {

		this.linha = linha;
		JLabel lbl = new JLabel( label );
		lbl.setBounds( 30, linha, largura, 23 );
		
		return lbl;
	}

	public JButton getJbutton(String string, int x, int y, int largura, int altura ) {
		JButton bi = new JButton( string );
		bi.setBounds( x, y, largura, altura);
		return bi;
	}


}