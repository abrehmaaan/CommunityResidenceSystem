import java.util.Scanner;

public class Resident implements Comparable<Resident>
{
    protected String name;
    private Community comm;

    public Resident(String name, Community cm)
    {
        this.name = name;
        comm = cm;
    }


    public String getName()
    {
        return name;
    }

    private Community getCommunity(){
        return comm;
    }


    public String toString(){
        return name;

    }
    
        
    public void recordReport(String criminalName)
    {
        comm.addCriminal(criminalName);
    }
    
    public void updateLocalData(IO io){
    	Scanner scan = io.getScanner();
    	Scanner ss = io.getScanner();
        String newname="";
        System.out.println("Please enter new name of the resident");
        newname = ss.nextLine();
        newname = scan.nextLine();
        this.name = newname;
    }

    @Override
    public int compareTo(Resident rhs) {
    	return this.name.compareTo(rhs.name);
    }
    
}
