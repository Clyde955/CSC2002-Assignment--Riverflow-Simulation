import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

public class FlowPanel extends JPanel implements Runnable {
	Terrain land;
	Graphics graphics;
	FlowPanel(Terrain terrain) {

		land=terrain;
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);

				fillNeighbors(e.getX(),e.getY());

			}
		});

	}
		
	// responsible for painting the terrain and water as images
	@Override
    protected void paintComponent(Graphics g) {

		int width = getWidth();
		int height = getHeight();
		  
		super.paintComponent(g);
		
		// draw the landscape in greyscale as an image
		if (land.getImage() != null){
      
      
			g.drawImage(land.getImage(), 0, 0, null);
			graphics = getGraphics();
			//draw the grid


		}

	}

	
	 // Fills a 7x7 selection of gridItems with water adn repaints
	 // x column index
	 // y row index
	public void fillNeighbors(int x, int y){
   
   
		for(int i = x-3; i <= x+3; i++){
			for(int j = y-3; j <= y+3; j++){

				synchronized (land){
            
					land.img.setRGB(i,j,Color.BLUE.getRGB());
					land.items[i][j].addH2O(3);
				}


			}
		}
		this.repaint();
	}

	
	public void run() {	
		// displays loop here
		// to do: this should be controlled by the GUI
		// to allow stopping and starting
		while (true){
			while(!Flow.paused){
         
         
				if(Flow.finishedStep.get(0) == 1 && Flow.finishedStep.get(1) == 1 && Flow.finishedStep.get(2) == 1 && Flow.finishedStep.get(3) == 1 ){
					synchronized (Flow.finishedStep){
               
               
						for (int i = 0; i < 4; i++) {
							Flow.finishedStep.set(i,0);
							
                     						}
					}
               
               
               
               
					Flow.counter.incrementAndGet();
					Flow.count.setText(Integer.toString(Flow.counter.get()));
					
				}

			}
		}

	}
}