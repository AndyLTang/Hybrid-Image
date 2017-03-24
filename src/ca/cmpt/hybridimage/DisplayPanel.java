package ca.cmpt.hybridimage;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class DisplayPanel extends AbstractPanel<HybridModel> {
	
	public DisplayPanel(HybridModel m){
		super(m);
		this.WIN_WIDTH = 1000;
		this.WIN_HEIGHT = 740;
		this.setPreferredSize(new Dimension( WIN_WIDTH, WIN_HEIGHT ));
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		
		int posY = 0;
		int gutter = 10;
		int imgW, imgH;
		
		// draw output images in a pyramid (half size each time)
		for (BufferedImage img : model.outputs){
			imgW = img.getWidth();
			imgH = img.getHeight();
			
			g.drawImage(img, 0, posY, this);
			g.drawImage(img, imgW + gutter, posY, imgW/2, imgH/2, this);
			g.drawImage(img, (int) (1.5f*imgW + 2*gutter), posY, imgW/4, imgH/4, this);
			
			posY += img.getHeight() + gutter;
		}
	}
}
