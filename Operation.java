import java.util.ArrayList;
public class Operation
{
    // instance variables - replace the example below with your own
    private String callSign;
    private static int opTracker=0;
    protected Community cm;
    protected  int numPolice;
   
    public Operation(Community community)
    {
        cm = community;
        callSign = "DS"+opTracker++; 
    }

    public String getCallSign()
    {
        return callSign;
        
    }
    
    
    
    public static void resetId(){
        opTracker=0;
    }
       public String toString()
    {
        return getCallSign();
        
    }
    
        public String toFile()
    {
        return getCallSign();
        
    }
    

}
