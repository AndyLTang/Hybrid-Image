package ca.cmpt.hybridimage;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class HybridView extends JFrame{
	
	private HybridModel model;
	
	protected DisplayPanel outputPanel;
	protected InputPanel inputPanel;
	protected SettingsPanel settingsPanel;
	
	public HybridView(HybridModel m){
		model = m;
		
		// setup base content panel
		JPanel content = new JPanel();
		content.setLayout(new BorderLayout());
		
		// add outputs panel
		outputPanel = createDisplayPanel(model);
		JScrollPane outputScroll = createScrollPane(outputPanel);
		content.add(outputScroll, BorderLayout.CENTER);
		
		// add inputs panel
		inputPanel = createInputPanel(model);
		JScrollPane inputScroll = createScrollPane(inputPanel);
		content.add(inputScroll, BorderLayout.EAST);
		
		// add settings panel
		settingsPanel = createSettingsPanel(model);
		content.add(settingsPanel, BorderLayout.NORTH);
		
		this.setContentPane(content);
		this.pack();
		this.setTitle("Hybrid Image App");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/*
	 * Functions to create view panels
	 */
	private DisplayPanel createDisplayPanel(HybridModel m) {
		return new DisplayPanel(m);
	}

	private InputPanel createInputPanel(HybridModel m) {
		return new InputPanel(m);
	}
	
	private SettingsPanel createSettingsPanel(HybridModel m) {
		return new SettingsPanel(m);
	}
	
	private JScrollPane createScrollPane(JPanel p){
		return new JScrollPane(p);
	}
	
	/*
	 * Methods to rerender the view according to updated values in model
	 */
	public void update(){
		model.outputs.clear();
		
		model.runHybridImages();
		rerender(inputPanel);
		rerender(outputPanel);
	}
	
	private void rerender(JPanel p){
		p.revalidate();
		p.repaint();
	}
}
