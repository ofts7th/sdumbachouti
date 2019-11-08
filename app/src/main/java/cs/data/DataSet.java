package cs.data;

import java.util.ArrayList;

public class DataSet {
	public ArrayList<DataTable> tables;

    public DataSet() {
        this.tables = new ArrayList<DataTable>();
    }
    
    public DataTable getFirstTable(){
    	return this.tables.get(0);
    }
    
    public DataTable getTable(int i){
    	return this.tables.get(i);
    }
}
