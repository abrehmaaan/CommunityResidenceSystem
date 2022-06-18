import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;

public class Community implements Comparable<Community>{
    private int distToStation; //not relevant to this project - just here to show possibilities
    private String name;
    private ArrayList<Resident> residents;
    private ArrayList<Criminal> criminals;

    public Community(String name, int dist)
    {
        // initialise instance variables
        this.name = name;
        distToStation = dist;
        residents = new ArrayList<Resident>();
        criminals = new ArrayList<Criminal>();

    }

    public int compareTo(Community other)
    {
        return this.getDist()- other.getDist();
    }

    public int countResidents(){
        return residents.size();
    }

    public int countCriminals(){
        return criminals.size();
    }
    
    public int countArrested(){
        int num=0;
        for (Criminal cr:criminals)
           num+=cr.arrested()?1:0;
        return num;
    }
    
     public int countAtLarge(){
        return countCriminals()- countArrested();
    }
    

    public ArrayList<Criminal> getCriminals(){
        return criminals;
    }
    public ArrayList<Resident> getResidents(){
        return residents;
    }

    public void addResident(String name)
    {
        residents.add(new Resident(name,this));
    }

    public Resident findResident(String name)
    {
        Resident retval =  null;
        for (Resident g:residents)
            if (g.getName().equals(name)){
                retval = g;
                break;
            }
        return retval;
    }

    public void addCriminal(String name)
    {
        if(!criminals.contains(name))
           criminals.add(new Criminal(name));
    }

    public Criminal findCriminal(String name)
    {
        Criminal retval =  null;
        for (Criminal c:criminals)
            if (c.getName().equals(name)){
                retval = c;
                break;
            }
        return retval;
    }

    public String getName(){
        return name;
    }

    public int getDist(){
        return distToStation;
    }

    public void updateLocalData(IO io)
    {   
        //Complete this section
        Scanner scan = io.getScanner();
        String newname="";
        int newDist = this.distToStation;
        String distStr = "";
        System.out.println("Please enter new name of the community");
        newname = scan.nextLine();
        if (newname.length() >0)
            this.name = newname;
        System.out.println("Please enter updated distance to nearest station from community");
        distStr = scan.nextLine();
             boolean success = false;
            while(!success){
                try{
                    this.distToStation = Integer.parseInt(distStr);
                    success = true;
                }
                catch(NumberFormatException nfe){
                    System.out.println("Please enter a number");
                }
            }
        

    }
    public void listResidents(IO io){
        String str ="";
        str = "=============================================\n";
        str+="======"+name+"("+residents.size()+")=========\n";     
        str+="=============================================\n";
        //Collections.sort(residents); 
        for(Resident r:residents)
             str+=r+"\n";
        str+="----------------------\n";

        if (io.getOutStream()!=System.out){
             str = str.replace("=","").replace("-","");
               }
        io.getOutStream().println(str);

    }

    public String toString(){
        String str = "=======================\n";
        str+="======"+name+"("+residents.size()+")=========\n";     
        str+="=======================\n";
        for(Resident r:residents)
            str+=r+"\n";
        str+="----------------------\n";
        for(Criminal c:criminals)
            str+=c+"\n";
        str+="----------------------\n";        

        return str;
    }

    
    public void removeResident(Resident r){
        int dx=-1; 
        for (int i=0; i<residents.size(); i++){
            if (r==residents.get(i)){
                dx=i;
                break;
            }
        }     
        if (dx>=0){
            residents.remove(dx);
        }

    }

    public void manageResidents(IO io, Community cm, String cname)
    {
    	Scanner scan = io.getScanner();
    	char mchoice = 'c';
    	String menu;
    	String menuOptions = "=======";
        menuOptions += "Resident Menu Options for "+cname;
        menuOptions+= "=======\n";
        menuOptions+="[N]ew Resident\n[L]ist Residents\n[U]pdate/Edit Resident\n[D]elete Resident\n[R]eport Criminal\n[E]xit\n";
        System.out.println(menuOptions);
        menu = scan.next().toUpperCase();
        mchoice = menu.charAt(0);
        switch(mchoice)
        {
            case 'N':{
                    System.out.print("Please enter name of new resident:");
                    String rname = scan.next();
                    residents.add(new Resident(rname, cm));
                    break;
                }
            case 'L':{
            	Collections.sort(residents);
                listResidents(io);
                break;
            }
            case 'U':{
                System.out.println("Please enter the name of the resident to be updated:");
                String rname = scan.next();
                Resident rs = findResident(rname);
                if (rs!=null)
                    rs.updateLocalData(io);
                else
                    System.out.println("Resident with name "+rname+ " not found.");
                break;
            }
            case 'D':{
                System.out.println("Please enter the name of the resident to be deleted:");
                String rname = scan.next();
                Resident rs = findResident(rname);

                if (rs!=null)
                    removeResident(rs);
                else
                    System.out.println("Resident with name "+rname+ " not found.");
                break;
            }
            case 'R':{
                System.out.println("Please enter the name of the resident making the report:");
                String rname = scan.next();
                System.out.println("Please enter the name of the criminal:");
                String criminalName = scan.next();
                Resident rs = findResident(rname);
                if (rs!=null)
                	rs.recordReport(criminalName);
                else
                    System.out.println("Resident with name "+rname+ " not found.");
                break;
            }
        }
    }


}
