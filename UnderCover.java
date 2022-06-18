import java.util.ArrayList;

public class UnderCover extends Operation
{
    static int ucount=0;
    public static void resetCount(){
        ucount=0;
    }
    
    public static boolean canDeploy(){
        Services service =  Services.getService();
        return service.policeAvailable(2);   
    }

    public UnderCover(Community community)
    {
        super(community);
   
        community.addResident("UnderCover"+ucount++);
        community.addResident("UnderCover"+ucount++);
        Services service =  Services.getService();
        service.deployPolice(2);
        numPolice+=2;
    }
    
    public String toString(){
       String str = "Operation " +super.getCallSign() + " to be deployed as an undercover surveillance operation in " +cm.getName()+".";
       return str;
    }
   
}
