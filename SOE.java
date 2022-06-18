import java.util.ArrayList;

    public class SOE extends ZOSO
{
   private int numRehab;
   private int numSocial, numSupplies;
   public static boolean canDeploy(int numCriminalsAdjusted){
        Services service =  Services.getService();
        return service.policeAvailable(2) &&
             service.soldiersAvailable(numCriminalsAdjusted)&&
             service.socialAvailable(numCriminalsAdjusted);   
    }

    public SOE(Community cm, int multiplier, int rehabRate)
    {
        super(cm, multiplier);  
        Services service =  Services.getService();
        service.deploySocial(cm.countCriminals()*multiplier);
        numPolice+=1;
        numSocial+=cm.countCriminals()*multiplier;
        numSupplies+=cm.countCriminals()*multiplier;
        
        numRehab = (int)(1.0* cm.countCriminals()*rehabRate/100);
        for (int i = numRehab-1; i>=0; i--)
        {
            cm.addResident(cm.getCriminals().get(i).getName());
            cm.getCriminals().remove(i);
        }

    }
   
      public int countRehabs(){
        return numRehab;
    } 
        public String toString(){
        String str ="Operation " +super.getCallSign() + " to be deployed as a SOE in " +cm.getName()+".\n";
        str += "Expect "+numArrests +" arrest(s)";
        if (numRehab >0)
           str+=" and "+numRehab +" rehabilitation(s).";
                      return str;
          }
    
    
   
}
