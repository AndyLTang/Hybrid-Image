package ca.cmpt.hybridimage;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class InputPanel extends AbstractPanel<HybridModel> {
	
	public InputPanel(HybridModel m){
		super(m);
		this.WIN_WIDTH = 300;
		this.WIN_HEIGHT = 740;
		this.setPreferredSize( new Dimension(WIN_WIDTH, WIN_HEIGHT) );
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		
		int posY = 0;
		int gutter = 20;
		int margin = 20;
		int padding = 5;
		int imgW = 304;
		int imgH = 200;
		int i = 1;
		
		g.setFont(new Font("Verdana", Font.PLAIN, 20));

		// draw each input image
		for (BufferedImage img : model.inputs){
			g.drawString("Image " + i, 0, posY + gutter);
			g.drawImage(img, 0, posY + gutter + padding, imgW, imgH, this);

			posY += 200 + gutter + margin;
			i++;
		}
	}
	
}
