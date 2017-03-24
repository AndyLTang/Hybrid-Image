package ca.cmpt.hybridimage;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class HybridController {
	
	private HybridModel model;
	private HybridView view;
	
	public HybridController(HybridModel m, HybridView v){
		this.model = m;
		this.view = v;
		
		SettingsPanel sp = view.settingsPanel;	// get handle on settings panel
		
		// add action listeners to interface elements in settings panel
		// 		1. filter combobox
		sp.addFilterListener(new FilterListener());
		
		// 		2. browse buttons
		for(int i = 0; i < sp.getButtons().size(); i++){
			sp.addBrowseListener(new BrowseListener(i), i);
		}
		
		// 		3. dissolve amount slider
		sp.addDissolveListener(new DissolveListener());
	}
	
	/*
	 * Event listeners inner classes
	 */
	
	// allows buttons to browse for another image
	class BrowseListener implements ActionListener {

		private int index;	// corresponds to the index of the input image to bind button to correct image
		
		private BrowseListener(int i){
			this.index = i;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// open dialog to allow users to choose their image
			JFileChooser fc = new JFileChooser();
			FileNameExtensionFilter f = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif");
			fc.setFileFilter(f);
			
			int result = fc.showOpenDialog(null);
			
			if (result == JFileChooser.APPROVE_OPTION) {
			    File img = fc.getSelectedFile(); 
			    
				// try and read image from chosen destination
			    try {
					BufferedImage newInput = ImageIO.read(img);
					
					// scale image in case it does not have appropriate dimension
					Image scaledInput = model.scaleImage(newInput, model.IMG_WIDTH, model.IMG_HEIGHT);
					newInput = model.toBufferedImage(scaledInput);

					// update model and view
					model.inputs.set(index, newInput);
					view.update();
				
				// error reading image
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "Error reading file.");
					e1.printStackTrace();
				}
			}//end if
		} // end actionPerformed
		
	}
	
	// change the filter that hybrid image algorithm applies using combobox
	class FilterListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String selection = (String) view.settingsPanel.getFilterList().getSelectedItem();
			
			// update model with new filter
			model.filter = selection;
			view.update();
		}
		
	}
	
	// change the dissolve ratio that hybrid image algorithm applies using slider
	class DissolveListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent arg0) {
			float val = (float) view.settingsPanel.getDissolveSlider().getValue()/100;
			
			// update model with new dissolve amount
			model.dissolveAmt = val;
			view.update();
		}
		
	}
	
}
