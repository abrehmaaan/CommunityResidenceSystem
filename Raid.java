import java.util.ArrayList;

public class Raid extends Operation
{
    int numArrests;
     public static boolean canDeploy(int numCriminalsAdjusted){
        Services service =  Services.getService();
        return service.policeAvailable(numCriminalsAdjusted);   
    }

    public Raid(Community community, int multiplier)
    {
        super(community);
        numArrests=0;
        Services service =  Services.getService();
        service.deployPolice(community.countCriminals()*multiplier);
        numPolice+=community.countCriminals()*multiplier;
        for (Criminal c:community.getCriminals())
        {
            c.arrest();
            numArrests++;
        }
    
    }
    public int countArrests(){
        return numArrests;
    }
    public String toString(){
            String str = "Operation " +super.getCallSign() + " to be deployed as a Raid in " +cm.getName()+ 
                           ".\nExpect "+countArrests() +" arrest(s).";
                           
                           return str;
                        }
   
}
