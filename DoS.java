import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;

public class DoS
{
    private ArrayList<Community> communities;
    private ArrayList<Operation> ops;
    private boolean assessed;
    private Services activeService;
    private  int forceMultiplier, gangLimit, rehabRate;
    private int testCase;

    public DoS(int forceMultiplier, int gangLimit,int rehabRate, int numPolice, int numSoldiers,int numEquipment,
    int numSocial, int numSupplies, int testCase)
    {
        Services.reset();
        Operation.resetId();
        UnderCover.resetCount();

        activeService = Services.getService(numSoldiers, numEquipment,
            numPolice, numSocial, numSupplies);
        communities= new ArrayList<Community>();
        ops = new ArrayList<Operation>();
        this.forceMultiplier = forceMultiplier;
        this.gangLimit = gangLimit;
        this.rehabRate = rehabRate;
        assessed = false;
        this.testCase = testCase;
        Operation.resetId();
    }

    public Services getService(){
        return activeService;
    }

    public int getForceMultiplier(){
        return forceMultiplier;
    }

    public int getGangLimit(){
        return gangLimit;
    }

    public void takeReport (String report)
    {

    }

    public String deploymentPlan()
    {
        return "";

    }

    public void assessForOps(){
        double emergencyRatio = 0.3;
        for (Community cm:communities)
        //cm.assessForOp();
        {
            int numCriminals=cm.countCriminals();
            if (numCriminals >0){
                if (numCriminals<gangLimit)
                {
                    if (Raid.canDeploy(numCriminals*forceMultiplier))
                        ops.add(new Raid(cm, forceMultiplier));
                    else if (UnderCover.canDeploy())
                        ops.add(new UnderCover(cm));

                }
                else
                {
                    if (numCriminals< emergencyRatio*cm.countResidents())
                    {
                        if (ZOSO.canDeploy(numCriminals*forceMultiplier))
                            ops.add(new ZOSO(cm, numCriminals*forceMultiplier));
                        else if (UnderCover.canDeploy())
                            ops.add(new UnderCover(cm));
                    }
                    else
                    {
                        if (SOE.canDeploy(numCriminals*forceMultiplier))
                            ops.add(new SOE(cm, numCriminals*forceMultiplier,  rehabRate));
                        else if (UnderCover.canDeploy())
                            ops.add(new UnderCover(cm));
                    }

                }
            }

        }
    }

    public void publicPolicyReport(){
        //Data for release to politicians.
        System.out.println("===============CASE "+testCase+"====SUMMARY FOR POLICY MAKERS=============================");
        int totOps =0;
        int totArrests =0;
        int totRehabs =0;
        for(Operation op:ops)
        {
            if (op instanceof SOE){
                totRehabs +=((SOE) op).countRehabs();
                totArrests+=((SOE) op).countArrests();
                totOps+=1;
            }
            else if (op instanceof ZOSO){
                totArrests+=((ZOSO) op).countArrests();
                totOps+=1;
            }
            else if (op instanceof Raid){
                totArrests+=((Raid) op).countArrests();
                totOps+=1;
            } 

        }
        String str = "We expect " + totOps +" operation(s), yielding " +totArrests + " arrest(s)";
        if (totRehabs >0)
            str+= " and "+ totRehabs +" rehab(s)";
        str +=".";
        System.out.println(str);
        System.out.println("");

    }

    public void classifiedInformationBrief(){
        System.out.println("===============CASE "+testCase+"===CLASSIFIED INTERNAL OPERATION BRIEF=======================");
        //Security Officers Internal Brief
        for(int i=0; i<ops.size(); i++)
        {
            Operation op = ops.get(i);
            if (op instanceof SOE)
                System.out.println((SOE) op);   
            else if (op instanceof ZOSO)
                System.out.println((ZOSO) op);   
            else if (op instanceof Raid)
                System.out.println((Raid) op);   
            else if (op instanceof UnderCover)
                System.out.println((UnderCover) op);   

        }   
    }

    public ArrayList<Community> getCommunities(){
        return communities;
    }

    public void setCommunities(ArrayList<Community>  cs){
        communities = cs;
    }

    
    public ArrayList<Operation> getOps(){
        return ops;
    }

    public Community findCommunity(String name){
        Community retval = null;
        for (Community c:communities){
            if (name.equals(c.getName())){
                retval =c;
                break;
            }
        }
        return retval;
    }

    public void removeCommunity(Community cm){
        int dx=-1; 
        for (int i=0; i<communities.size(); i++){
            if (cm==communities.get(i)){
                dx=i;
                break;
            }
        }     
        if (dx>=0){
            communities.remove(dx);
        }

    }

    public void listCriminalsAtLarge(){
        for (Community c: communities){
            if (c.countArrested()<c.countCriminals()){
                System.out.println("<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>");
                System.out.println(c.countCriminals() +" reports from "+ c.getName());
                System.out.println("------------------------------------------");
                for(Criminal cr:c.getCriminals())
                    System.out.println(cr.getName());

            }
        }
    }


    public void showInfo()
    {
        System.out.println("------------CASE "+testCase+"---DoS Operating Data-------------------------");
        System.out.println("Force multiplier:"+forceMultiplier);
        System.out.println("Gang Limit:"+gangLimit);
        System.out.println("Rehab. rate:"+rehabRate+"%");
        System.out.println("--------------------Service Inventory-----------------------------");
        System.out.println("Num Police:"+activeService.countPolice());
        System.out.println("Num Soldiers:"+activeService.countSoldiers());
        System.out.println("Num Equipment:"+activeService.countEquipment());
        System.out.println("Num Social:"+activeService.countSocial());
        System.out.println("Num Supplies:"+activeService.countSupplies());    

    }

    public void showDosMenu(){
        String response  = " ";
        while ((response.charAt(0)!='x')&&(response.charAt(0)!='X')){
            System.out.println("------------------------------------------------------------------");
            System.out.print("--------------------------DoS Submenu-----------------------------\n");
            System.out.println("------------------------------------------------------------------");
            System.out.println("[C]riminals at large");
            if (!assessed)
                System.out.println("[A]ssess operations");
            System.out.println("[P]ublic Information Report");
            System.out.println("[O]perational Internal Brief");
            System.out.println("E[x]it to previous menu");
            Scanner scan = new Scanner(System.in);
            response  = scan.next(); 
            if ((response.charAt(0)!='x')&&(response.charAt(0)!='X'))
                showSelectedOps(response.charAt(0));
        }

    }

    
    public void showSelectedOps(char option){
        switch(option){
            case 'C':{
                    listCriminalsAtLarge();
                    break;     
                }
            case 'A':{
                    assessForOps();
                    assessed =true;
                    break;     
                }
            case 'P':{
                    if (assessed)
                        publicPolicyReport();
                    else
                        System.out.println("Investigations are in progress.");
                    break;     
                }
            case 'O':{
                    if (assessed)
                        classifiedInformationBrief();
                    else
                        System.out.println("Operations have not yet been assessed.");
                    break;     
                }     
        }

    }

    public void listCommunities(IO io,  PrintStream outStream)
    {
        if (outStream==System.out)
        {
            outStream.println("-------------------------Community List------------------------");
            outStream.println("\t\tDistance(km) to");
            outStream.println("Name\t\tnearest station\t #Residents \t#At Large");
            outStream.println("---------------------------------------------------------------");
        }

        for (Community c:communities)
        {
            String outline = c.getName();
            if (outStream==System.out)
                outline = outline.replace(';','\t');
            if (c.getName().length()<=7)
                outline+="\t"; 

            outline+="\t\t"+c.getDist()+"\t\t"+c.countResidents()+"\t\t"+c.countAtLarge();

            outStream.println(outline);
        }
        outStream.println("----------------------------------------------------------------");

    }

    public void listInfo()
    {

        for (Community c:communities)
        {
            System.out.println(c);
        }
    }

    public void listOps(IO io, PrintStream outStream)
    {
        if (outStream==System.out)
            outStream.println("------Operation List--------");

        for (Operation o:ops)
            if (outStream==System.out)            
                outStream.println(o);
            else
                outStream.println(o.toFile());
    }

    public void manageCommunities(IO io) throws NumberFormatException
    {

        Scanner scan = io.getScanner();

        char mchoice = 'c';
        String menu="";
        while ((mchoice!='x')&&(mchoice!='X'))
        {
            String menuOptions = "--------Case "+testCase+":Community Menu Options--------\n";

            menuOptions+= "[C]reate community\n[L]ist communities\n";
            menuOptions+="[U]pdate/Edit community\n[D]elete community\nManage [R]esidents in a community\nE[x]it\n";
            System.out.println(menuOptions);
            menu = scan.next().toUpperCase();
            mchoice = menu.charAt(0);
            switch(mchoice)
            {
                case 'C':{
                        System.out.print("Please enter name of new community(for early tests, one word only):");
                        String cname = scan.next();
                        int dist = -1;
                        while (dist <0){

                            try{
                                System.out.print("Please enter distance of "+cname+ " to nearest police station:");
                                dist = Integer.parseInt(scan.next());
                            }
                            catch (NumberFormatException nfe)
                            {
                                System.out.println("You should enter a number... please try again");
                            }
                        }
                        communities.add(new Community(cname, dist));
                        break;
                    }
                case 'L':{
                        Collections.sort(communities);
                        listCommunities(io, System.out);
                        break;
                    }
                case 'U':{
                        System.out.println("Please enter the name of the community to be updated:");
                        String cname = scan.next();
                        Community cm = findCommunity(cname);
                        if (cm!=null)
                            cm.updateLocalData(io);
                        else
                            System.out.println("Community with name "+cname+ " not found.");
                        break;
                    }
                case 'D':{
                        System.out.println("Please enter the name of the community to be deleted:");
                        String cname = scan.next();
                        Community cm = findCommunity(cname);

                        if (cm!=null)
                            removeCommunity(cm);
                        else
                            System.out.println("Community with name "+cname+ " not found.");
                        break;
                    }
                case 'R':{
                        System.out.println("Type the name of the community in which you are interested.");
                        String cname = scan.next();
                        Community cm = findCommunity(cname);
                        if (cm!=null)
                            cm.manageResidents(io, cm, cname);
                        else
                            System.out.println("There is no community named " +cname);

                    }

            }

        }
    }

    public void clearData()
    {
        communities.clear();
        ops.clear();
        Operation.resetId();
    }

    private String getCFile(int caseNo)
    {
        return "cases/C"+caseNo+".txt";

    }

    private String getDFile(int caseNo)
    {
        return "cases/D"+caseNo+".txt";
    }

    private String getRFile(int caseNo)
    {
        return "cases/R"+caseNo+".txt";
    }

    public  void loadTestCase(int caseNo, IO io ) 
    {
        io.loadData(caseNo);
        System.out.print("Test case " + caseNo+ " loaded.");

    }

}
