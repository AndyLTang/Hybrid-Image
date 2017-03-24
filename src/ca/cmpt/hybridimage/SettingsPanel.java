package ca.cmpt.hybridimage;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;

public class SettingsPanel extends AbstractPanel<HybridModel>{

	private JComboBox<String> filterList;
	private ArrayList<JButton> browseBtns = new ArrayList<JButton>();
	private JSlider dissolveSlider;
	
	public SettingsPanel(HybridModel m) {
		super(m);
		this.setLayout(new FlowLayout());
		
		// create and add labels, filters, buttons, and slider to panel
		this.add(new JLabel("Filters:"));
		addFilterList();
		addButtons();
		this.add(new JLabel("Dissolve Amount:"));
		addDissolveSlider();
	}
	
	/*
	 * Getters for ui elements
	 */
	public ArrayList<JButton> getButtons() {
		return this.browseBtns;
	}
    
    public JComboBox<String> getFilterList(){
    	return this.filterList;
    }

    public JSlider getDissolveSlider(){
    	return this.dissolveSlider;
    }
    
    /*
     * Methods to create and add elements into panel
     */
    private void addFilterList(){
		filterList = new JComboBox<String>(model.FILTERS);
		this.add(filterList);
    }
    
    private void addButtons(){
		for (int i = 0; i < model.inputs.size(); i++){
			JButton btn =  new JButton("Change Image " + (i+1)) ;
			browseBtns.add(btn);
			this.add(btn);
		}
    }

	private void addDissolveSlider() {
		dissolveSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
		dissolveSlider.setMajorTickSpacing(10);
		dissolveSlider.setMinorTickSpacing(5);
		dissolveSlider.setPaintTicks(true);
		dissolveSlider.setPaintLabels(true);
		this.add(dissolveSlider);
	}
    
	/*
	 * Methods to add event listeners to ui
	 */
    public void addBrowseListener(ActionListener listener, int i) {
        browseBtns.get(i).addActionListener(listener);
    }
    
    public void addFilterListener(ActionListener listener) {
        filterList.addActionListener(listener);
    }
    
    public void addDissolveListener(ChangeListener listener){
    	dissolveSlider.addChangeListener(listener);
    }
}
