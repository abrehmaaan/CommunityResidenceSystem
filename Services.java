
public class Services
{
    private  int numSoldiers;
    private  int numEquipment;
    private  int numPolice;
    private int numSocial;
    private int numSupplies;
    private static Services theService;

    private Services(int numSoldiers,int numEquipment,int numPolice,
    int numSocial, int numSupplies){
        this.numSoldiers = numSoldiers;
        this.numEquipment = numEquipment;
        this.numPolice = numPolice;
        this.numSocial = numSocial;
        this.numSupplies = numSupplies;

    }

    public static Services getService(int numSoldiers,int numEquipment,int numPolice,
    int numSocial, int numSupplies){
        if (theService==null)
            theService = new Services(numSoldiers, numEquipment,
                numPolice, numSocial, numSupplies);

        return theService;
    }
    
    public static void reset(){
        theService = null;
    }

            
    public static Services getService(){                          
        return theService;
    }

        

    public boolean policeAvailable(int req)
    {
        return numPolice>=req;
    }

    public boolean soldiersAvailable(int req)
    {
        return ((numSoldiers>=req) &&
            (numEquipment >= req));
    }

    public boolean socialAvailable(int req)
    {
        return ((numSocial>=req) &&
            (numSupplies >= req));
    }

    public void deployPolice(int numNeeded){
        numPolice-=numNeeded;
    }

    public void deploySoldiers(int numNeeded){
        numPolice-=1;
        numSoldiers -=numNeeded;
        numEquipment -= numNeeded;
    }

    public void deploySocial(int numNeeded){
        numPolice-=1;
        numSocial -=numNeeded;
        numSupplies -= numNeeded;
    }
    
    public int countPolice(){
        return numPolice;
    }
    
    public int countSoldiers(){
        return numSoldiers;
    }
    
    public int countEquipment(){
        return numEquipment;
    }
    
    public int countSocial(){
        return numSocial;
    }
    
    public int countSupplies(){
        return numSupplies;
    }

}
