
public class GridTings {

    private int H2OUnits;//water infomation specifics
    private  float height;
    private int RowIx;// grid position points
    private int ColIx;
    

    
            
     
    public GridTings(int RowIx, int ColIx, float height){ // Constructor for the GridTings class which takes in the following parameters
        this.RowIx = RowIx; // RowIx: row number
        this.ColIx = ColIx; // ColIx: column number
        this.height = height;// height: height value
        this.H2OUnits = 0; 
    }

    // methods safe to keep in the normal way since their values are fixed
    // Get method for the row index which returns the row number
    public int getRowIx(){
        return this.RowIx;
    }

   // Get method for the column index which return the column number
    public int getColIx(){
        return this.ColIx;
    }
 
     // Get method for the waterUnits which returns the number of water units the current grid item has
     //Thread needs safety
    synchronized int getH2OUnits(){
        return this.H2OUnits;
    }

    //Method that allows a specified number of water units(numUnits) to be added to the current count   
    synchronized void addH2O(int numUnits){
        this.H2OUnits += numUnits;
    }

    
     //Method that allows a specified number of water units(numUnits) to be removed from the current count
    synchronized void removeH2O(int numUnits){
        this.H2OUnits -= numUnits;
    }

    // Get method for the current water surface which returns the water surface
    // it is calculated by summing the height with the result of water units multipled by 0.01
    synchronized float getH2OSurface(){
        return this.height + this.H2OUnits * (0.01f);
    }

     //Sets the waterUnits back to 0
    synchronized void resetH2O(){
        this.H2OUnits = 0;
    }

    // Get method for the height which returns the height of the current grid item
     public float getHeight() {
        return height;
    }

        public String toString(){
        return "Row Index: "+Integer.toString(RowIx) + "\n Column Index: "+Integer.toString(ColIx)+
                "\n Height: "+Float.toString(height)+ "\n Water Units: "+Integer.toString(H2OUnits)+
                "\n Water Surface: "+ Float.toString(getH2OSurface());
    }


}