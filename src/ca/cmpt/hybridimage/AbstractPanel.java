package ca.cmpt.hybridimage;

import javax.swing.JPanel;

public abstract class AbstractPanel<M> extends JPanel {
	
	protected M model;
	
	protected int WIN_WIDTH;
	protected int WIN_HEIGHT;
	
	public AbstractPanel(M m){
		this.model = m;
	}
	
}
