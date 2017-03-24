package ca.cmpt.hybridimage;

public class MainApp {
	
	public static void main(String[] args){
		
		HybridModel model = new HybridModel();
		HybridView view = new HybridView(model);
		HybridController controller = new HybridController(model, view);
		
		view.setVisible(true);
		
	}
	
}
