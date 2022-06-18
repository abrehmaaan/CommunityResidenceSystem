


import java.util.ArrayList;

public class ZOSO extends Operation
{
    protected int numSoldiers, numEquipment;
    int numArrests;
    
   public static boolean canDeploy(int numCriminalsAdjusted){
        Services service =  Services.getService();
        return service.policeAvailable(1) &&
             service.soldiersAvailable(numCriminalsAdjusted);   
    }

    public ZOSO(Community community, int multiplier)
    {
        super(community);
        Services service =  Services.getService();
        service.deploySoldiers(community.countCriminals()*multiplier);
        numPolice+=1;
        numSoldiers+=community.countCriminals()*multiplier;
        numEquipment+=community.countCriminals()*multiplier;
        numArrests=0;
        for (Criminal c:community.getCriminals()){
            c.arrest();
            numArrests++;
        }
       
    }
    
    public int countArrests(){
        return numArrests;
    }
    public String toString(){
            String str = "Operation " +super.getCallSign() + " to be deployed as a ZOSO in " +cm.getName()+ 
                           ".\nExpect "+numArrests +" arrest(s).";
                      return str;
               }

    
    
   
}
