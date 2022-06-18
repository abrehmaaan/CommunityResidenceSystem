import java.util.ArrayList;
import java.io.PrintStream;
import java.util.Scanner;
import java.io.*;

public class IO {
    final int DOS_DATALEN=8;
    //ArrayList<Operation> opList;
    Scanner scanner;
    PrintStream outStream=null;
    public IO() {
        //opList = new ArrayList<Operation>();
        scanner=new Scanner(System.in);
        outStream = System.out;
    }

    public PrintStream getOutStream(){
        return outStream;
    }

    public void setOutStream(PrintStream outStream){
        this.outStream = outStream;
    }

    public Scanner getScanner(){
        return scanner;
    }

    public void setScanner(Scanner scanner){
        this.scanner = scanner;
    }

    public void listOps(ArrayList<Operation> olist,  PrintStream outStream)
    {
        if (outStream==System.out)
        {
            outStream.println("---------------Operation List---------------");
            //outStream.println("ID\tName\tBudget\t#Plans\t#Events");
            outStream.println("--------------------------------------------");
        }

        for (Operation o:olist)
        {
            String outline = o.toString();
            if (outStream==System.out)
                outline = outline.replace(';','\t');
            outStream.println(outline);
        }
    }

    public String readValueFromFile(String filename){
        String stuId="";
        try
        {
            Scanner sc = new Scanner(new File("id.txt"));
            stuId = sc.nextLine();
            sc.close();
        }
        catch (IOException ioe){
            System.out.println("Unable to read ID number. Is the file in the correct location?");
        }
        return stuId;
    }

    public void listCommunities(ArrayList<Community> clist, PrintStream outStream)
    {
        if (outStream==System.out)
            outStream.println("------Community List--------");

        for (Community c:clist)
            if (outStream==System.out)			
                outStream.println(c);
            else
                outStream.println(c.toString().replace("=","").replace("-",""));
    }

    public DoS loadTestCase(int caseNo, Scanner scan )
    {
        DoS dos = loadData(caseNo);

        return dos;
    }

    public DoS loadData(int caseNo)
    {

        int[] dosData = readDosData(getDFile(caseNo));
        DoS dos = new DoS(dosData[0],dosData[1],dosData[2],dosData[3],dosData[4],dosData[5],dosData[6],dosData[7], caseNo);
        dos.setCommunities(loadCommunities(getCFile(caseNo)));
        if (dos.getCommunities().size()>0)
            loadResidents(getRFile(caseNo),dos);

        return dos;
    }

    private String getDFile(int caseNo)
    {
        return "cases/D"+caseNo+".txt";
    }

    private String getCFile(int caseNo)
    {
        return "cases/cm"+caseNo+".txt";
    }

    private String getRFile(int caseNo)
    {
        return "cases/R"+caseNo+".txt";
    }

    public ArrayList<Community> loadCommunities(String cfile) 
    {
        ArrayList<Community> clist = new ArrayList<Community>();
        Scanner cscan = null;
        //System.out.println("reading from "+cfile);
        try
        {
            cscan  = new Scanner(new File(cfile));
            while(cscan.hasNext())
            {
                String [] nextLine = cscan.nextLine().split(":");
                String cname = nextLine[0];
                int dist = Integer.parseInt(nextLine[1]);
                clist.add( new Community(cname, dist));

            }

            cscan.close();
        }
        catch(IOException e)
        { }
        catch(NumberFormatException nfe)
        { System.out.println(nfe.getMessage());}

        return clist;
    }

    public void loadResidents(String rfile, DoS dos)
    {
        Scanner cscan = null;
        //System.out.println("reading from "+cfile);
        try
        {
            cscan  = new Scanner(new File(rfile));
            while(cscan.hasNext())
            {
                String [] nextLine = cscan.nextLine().split(":");
                String rname = nextLine[0];
                String cname = nextLine[1];
                Community cm = dos.findCommunity(cname);
                if (cm!=null) {
                    cm.addResident(rname);
                    if(nextLine.length == 3) {
                    	Resident rs = cm.findResident(rname);
                        if (rs!=null)
                        	rs.recordReport(nextLine[2]);
                    }
                }
                else
                    System.out.println("There is no community named " +cname);

            }

            cscan.close();
        }
        catch(IOException e)
        { }
        catch(NumberFormatException nfe)
        { System.out.println(nfe.getMessage());}
    }

    public int[] readDosData(String datafile){
        int []dosData= null;
        try{
            String[] dosStr=null;
            Scanner dscan =  new Scanner(new File(datafile));
            dosStr =  dscan.nextLine().replaceAll("\\s+$", "").split(" ");
            dscan.close();
            if (dosStr.length!=DOS_DATALEN)
                throw new IOException("Incorrect number of items in DoS file for this test case");
            else{
                dosData= new int[DOS_DATALEN];
                for (int i=0; i<DOS_DATALEN;i++){
                    dosData[i] = Integer.parseInt(dosStr[i]);
                }
            }
        }catch(IOException ioe){
            System.out.println(ioe.getMessage());
        }
        catch (NumberFormatException nfe){
            System.out.println(nfe.getMessage());
        }

        return dosData;
    }

    public void deleteFile(String fileName){
        try{
            File file = new File(fileName);
            file.delete();
        }
        catch(Exception e){
            System.out.println(e.getMessage());

        }
    }

}