import java.awt.*;

public class TempleRun extends java.lang.Thread{
    //start and end indices
    private int lowlife;
    private int highlife;

    private Terrain LD;// the terrain data
    volatile boolean isRunning;//is meant to check if a thread is running or not
    int cnt;//cnt to keep track of the timing
    int indx;

    
     // Constructor for the thread class which takes in the following parameters:
     // lowlife:  the lower index that is visible to the thread
     // highlife: the upper indx visible to the thread
     // LD: terrain to analyse
     // indx: index the thread has access to in the finishedStep array of FP
       public TempleRun(int lowlife, int highlife, Terrain LD,int indx){
        this.lowlife = lowlife;
        this.highlife = highlife;
        this.LD = LD;
        isRunning = true;
        this.cnt = 0;
        this.indx = indx;

    }

    
     //Overriden Run method handles the thread logic
    @Override
    public void run(){// executes until the user says stop
    
    while (isRunning){        
        if(this.cnt == Flow.counter.get()){//checks to see if the cnt has been incremented

           for (int i = lowlife; i < highlife; i++) {//iterate over certain portion of the grid

              //get the row and column indx
                int[] arr  = new int[2];
                LD.getPermute(i, arr);

                 //get the gridItem in question
                 int x = arr[0];
                 int y = arr[1];

                 // y: the row and x: column
                 GridTings currentItem = LD.items[x][y];

                 
                  if(currentItem.getH2OUnits() > 0) {//only execute if we have a non-zero water level

                  GridTings LN = findLowest(x, y, currentItem);//finds the LN
                          //transfers units from the current item to the lowest neighbor\
                           // note this operation needs to be thread safe hence the synchronized block
                           // note we only do this if there wa a lowest neighbor
                           if (LN != null) {

                               //place a lock on the current item and the lowest neighbor
                               //this ensures thread safety during the operations we perform
                               synchronized (currentItem) {
                                   synchronized (LN) {
                                       //take a water unit from this one and add it to the lowest neighbor
                                       currentItem.removeH2O(1);
                                       LN.addH2O(1);

                                       //change the color of the lowest  neigbor pixel to blue
                                       LD.img.setRGB(LN.getColIx(),LN.getRowIx(), Color.BLUE.getRGB());

                                       //reset the color of the current pixel of theres no water on it
                                       if (currentItem.getH2OUnits() == 0) {
                                           LD.resetPixel(currentItem.getColIx(), currentItem.getRowIx());
                                       } 
                                       Flow.fp.repaint();//repaint the panel

                                   }
                               }// end sync block
                           }else if(!inRange(x,y)){

                               // we are at a boundary
                                  synchronized (currentItem){
                                   currentItem.resetH2O(); //sets the water to 0 and reset pixel
                                   LD.resetPixel(currentItem.getColIx(),currentItem.getRowIx());
                               }
                           }
                       }

                   }

                   //increment the cnt of the current thread
                   this.cnt++;

                   //tell flow that we have finished the step
                   Flow.finishedStep.set(indx,1);


            }
        }
        
    }

   
     // Method that finds the GridTings that has a height strictly lower than the current GridTings
     // x: Column Index
     // y: Row Index
     // Checks Current GridTings
     //return the lowest neighbor or null
    public GridTings findLowest(int x, int y, GridTings Checks){
        GridTings lowest = Checks;

        //make sure we're not at a limit before figuring out where the water goes
        if(inRange(x,y)){
            //iterate the immediate rows
            for (int i = y-1; i <= y+1 ; i++) {

                //iterate the immediate columns
                for(int j = x-1; j <= x+1; j++){
                    // check if this thing is the lowest
                    if(LD.items[j][i].getH2OSurface() < lowest.getH2OSurface()){
                        lowest = LD.items[j][i];
                    }
                }

            }
        }




        //once the lowest has been obtained check if its not the same as Checks
        if(Checks.getH2OSurface() == lowest.getH2OSurface()){
            lowest = null;
        }



        return lowest;
    }

    
     // Method to check if the current grid position is not at a boundary
     //y: column index
     //x: row index
     //return true if not at a boundary and false otherwise 
    public boolean inRange(int x, int y){

        volatile boolean xinRange = x != 0 && x != LD.dimx-1; // check if we are not at the x boundaries
        volatile boolean yinRange = y != 0 && y != LD.dimy-1; // check if we are not at the y boundaries

        return xinRange && yinRange; // combine results
    }



}