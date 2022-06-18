//import java.io.*;
//import java.nio.file.*;
import java.util.zip.*;

import java.util.Scanner;
import java.util.stream.Stream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;

public class Tester{
    private static double jdocScore =0;
    private static boolean vfound, pvfound, tvfound, svfound;
    // instance variables - replace the example below with your own
    private int caseNo;
    private static boolean invalidCase = false;

    private static double [] caseScores={5, 15,15,10,10,5,5,15, 15,5};
    static ArrayList<Integer> manualTests;
    /**
     * Constructor for objects of class TestCase
     */
    public Tester(int caseNo)
    {
        this.caseNo= caseNo;

        manualTests=new ArrayList<Integer>();
        manualTests.add(1);
        manualTests.add(2);
        manualTests.add(4);
        manualTests.add(6);
        manualTests.add(8);
    }

    public int getCaseNo()
    {
        return caseNo;

    }

    public String getTestInFile()
    {
        return "./cases/Entry"+caseNo+".txt";

    }

    public String getTestOutFile()
    {
        return "./cases/TestCase"+caseNo+".myOutput.txt";
    }

    private static double moderate(double score, String stuid){
        return score - Double.parseDouble(new String(new StringBuilder(stuid).reverse()))/1000000000*5;

    }

    public boolean testFileExists(){

        String testfile = "./cases/TestCase"+caseNo+".myOutput.txt";
        File file = new File(testfile);
        return file.exists();
    }

    public double score()
    {
        String testfile = "./cases/TestCase"+caseNo+".myOutput.txt";
        String valfile = "./cases/output"+caseNo+".txt";
        String tString="", vString=""; 
        try{
            Scanner tscan = new Scanner(new File(testfile));
            while (tscan.hasNext())
                tString +=tscan.nextLine();
            tString = tString.substring(1,tString.length());
        }
        catch(IOException ioe)
        {}
        try{
            Scanner vscan = new Scanner(new File(valfile));
            while (vscan.hasNext())
                vString +=vscan.nextLine();
        }
        catch(IOException ioe)
        {}
        //System.out.println("Case" + getCaseNo());//+":Comparing ["+tString+"]");
        //System.out.println(" with ["+vString+"]");

        double sc = caseScores[getCaseNo()-1]*similarity(tString.substring(1), vString.substring(1));
        return  sc; 

    }

    public static double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2; shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) { return 1.0; /* both strings are zero length */ }
        /* // If you have Apache Commons Text, you can use it to calculate the edit distance:
        LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
        return (longerLength - levenshteinDistance.apply(longer, shorter)) / (double) longerLength; */
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;

    }

    // Example implementation of the Levenshtein Edit Distance
    // See http://rosettacode.org/wiki/Levenshtein_distance#Java
    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();
        int startpos=0;

        int[] costs = new int[s2.length() + 1];
        for (int i = startpos; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = startpos; j <= s2.length(); j++) {
                if (i == startpos)
                    costs[j] = j;
                else {
                    if (j > startpos) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > startpos)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }

    public static void runCases(IO io, DoS dos, SystemInfo sys)
    {
        boolean header = false;

        String stuId ="";
        try
        {
            Scanner sc = new Scanner(new File("id.txt"));
            stuId = sc.nextLine();
            sc.close();
        }
        catch (IOException ioe){}
        if (stuId.length()==0)
        {
            System.out.println("ID not set. Please enter your ID before proceeding.");
            return;
        }
        ArrayList<Tester> tests = new ArrayList<Tester>();
        invalidCase = false;

        for (int i=1; i<= caseScores.length;i++)
        {
            Tester t = new Tester(i);
            dos.clearData();
            dos = io.loadData(i);
            if (i<7)
                if (!(validated(i)))
                {
                    invalidCase=true;
                    System.out.println("Test case "+i+ " is broken:: Please refresh it before running tests");
                    break;
                }                       
            //tc.writeOut(vx.getPlist(),vx.getAPlist(),vx.getFVlist(),
            //    vx.getInitApproved(), outWriter);
            tests.add(t);
            int nonPrintable= getNonPrint(stuId);
            if (manualTests.indexOf(i)<0)
            {
                try{
                    PrintStream outStream = new PrintStream(new FileOutputStream(t.getTestOutFile()));
                    outStream.write(nonPrintable);
                    io.setOutStream(outStream);
                    Scanner fileScan = null;
                    try{
                        fileScan =new Scanner(new File(t.getTestInFile()));
                        io.setScanner(fileScan);
                    }
                    catch(IOException ioe){}

                    switch(i)
                    {case 0:

                        case 3:{
                                for (Community c:dos.getCommunities()){
                                    c.listResidents(io);
                                }

                                //ReportScreen r = new ReportScreen();
                                //r.listPromoters(io.promoters, outStream);
                                break;
                            }

                        case 4:
                        case 5:{
                                for (Community c:dos.getCommunities()){
                                    for (Resident r:c.getResidents())
                                        r.updateLocalData(io);
                                }
                                saveTest(io, dos, i);
                                break;
                            }
                        case 7:
                            {

                                for (Community c:dos.getCommunities()){
                                    while(io.getScanner().hasNextLine()){
                                        String moving = io.getScanner().next();
                                        Resident r = c.findResident(moving);
                                        c.removeResident(r);

                                    }
                                }
                                saveTest(io, dos, i);
                                break;

                            }

                        case 9:{
                                Collections.sort(dos.getCommunities());
                                dos.assessForOps();
                                saveTest(io, dos, i);
                                break;

                                //look for venue-- if found add 
                                //for(Venue v:io.venues)
                                //    v.promoteEvents(outStream);
                                //for(Promoter p:io.promoters)
                                //    p.submitPlans();

                            }

                    }
                    outStream.close();
                }
                catch(IOException ioe){} 
            }
        }
        io.setOutStream(System.out);
        io.setScanner(new Scanner(System.in));

        if (!(invalidCase))
        {
            double sumScore =0;
            for (Tester t:tests){
                //for(int ti=0;ti<tests.size();ti++){ 
                //Tester t = tests.get(ti);

                if (t.getCaseNo() !=tests.size()){
                    if (t.testFileExists()){

                        double tcScore = t.score();
                        sumScore +=tcScore;
                        System.out.println( "Test "+t.getCaseNo() +"=>"+tcScore+"/"+caseScores[t.getCaseNo()-1]+"; Aggregate score is "+sumScore+"/100.0");
                    }else{
                        System.out.println("No file exists for test case " +t.getCaseNo());
                        System.out.println("If it is a manual test(1,2,4,6, or 8) please save test case after updating data");
                    }

                }
            }
            //final case -look for javadocs
            jdocScore = 0;
            vfound=false; pvfound=false; tvfound=false; svfound=false;
            try (Stream<Path> walkStream = Files.walk(Paths.get((System.getProperty("user.dir"))))) {
                walkStream.filter(p -> p.toFile().isFile()).forEach(f -> {
                        if (f.toString().endsWith("Community.html")) {
                            vfound = true;;
                        }
                    });
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try (Stream<Path> walkStream = Files.walk(Paths.get((System.getProperty("user.dir"))))) {
                walkStream.filter(p -> p.toFile().isFile()).forEach(f -> {
                        if (f.toString().endsWith("Resident.html")) {
                            tvfound =true;

                        }
                    });
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try (Stream<Path> walkStream = Files.walk(Paths.get((System.getProperty("user.dir"))))) {
                walkStream.filter(p -> p.toFile().isFile()).forEach(f -> {
                        if (f.toString().endsWith("Operation.html")) {
                            svfound =true;
                        }
                    });
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try (Stream<Path> walkStream = Files.walk(Paths.get((System.getProperty("user.dir"))))) {
                walkStream.filter(p -> p.toFile().isFile()).forEach(f -> {
                        if (f.toString().endsWith("DoS.html")) {
                            pvfound=true;
                        }
                    });
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (vfound) jdocScore+=1.25;
            if (pvfound) jdocScore+=1.25;
            if (svfound) jdocScore+=1.25;
            if (tvfound) jdocScore+=1.25;
            sumScore+=jdocScore;
            //System.out.println( "Score on test "+(tests.size()-1) +" is "+jdocScore+"; Aggregate score is "+sumScore);
            System.out.println( "Test "+(tests.size())  +"=>"+jdocScore+"/"+caseScores[tests.size()-1]+"; Aggregate score is "+sumScore+"/100.0");
            double modScore = moderate(sumScore, stuId);
            System.out.println( "==========Moderated Score:"+modScore+"/100.0===========");

            System.out.println("Prepare submission[y/n]? (You can always do it again)");
            String choice = io.getScanner().next();
            if (choice.equals("y")){
                File dir = new File(System.getProperty("user.dir"));
                File[] scoreFiles = dir.listFiles((dir1, name)->name.startsWith("score"));
                for (File file:scoreFiles)
                    file.delete();

                try{    
                    PrintStream p=new PrintStream(new FileOutputStream("score_"+String.format("%.2f", modScore)+"_"+stuId+".tst"));
                    p.write((int)sumScore);
                    //p.println(sumScore);
                    p.write(getHash(sumScore+""));
                    p.write(getHash(stuId));

                    p.close();
                }
                catch(IOException ioe){}
                zipFiles("score_"+String.format("%.2f", modScore)+"_"+stuId,sys);
            }
        }
    }

    public   static void saveTest(IO io, DoS dos) {

        System.out.println("Please enter the number the test that you would like to save [ 1, 2, 4, 6, 8]");
        int testNo = Integer.parseInt(io.getScanner().next());
        saveTest(io, dos, testNo);
    }

    public   static void saveTest(IO io, DoS dos, int testNo) {

        Scanner scan = io.getScanner();

        String stuId ="";
        try
        {
            Scanner sc = new Scanner(new File("id.txt"));
            stuId = sc.nextLine();
            sc.close();
        }
        catch (IOException ioe){}
        if (stuId.length()==0)
        {
            System.out.println("ID not set. Please enter your ID before proceeding.");
            return;
        }
        Tester t = new Tester(testNo);
        int outkey=25;
        try{
            PrintStream outStream = new PrintStream(new FileOutputStream(t.getTestOutFile()));
            int nonPrintable= getNonPrint(stuId);
            outStream.write(nonPrintable);

            switch(testNo)

            {
                case 1:
                case 2:
                case 4:
                case 5:
                case 6:
                case 7: 
                    {
                        io.listCommunities(dos.getCommunities(), outStream);

                        break;

                    } 

                case 8:
                case 9:{
                        for(Community c:dos.getCommunities()){
                            outStream.print(c.toString().replace("=","").replace("-",""));
                        }
                        break;
                    }

            }
            outStream.close();
        }
        catch(IOException ioe)
        {}
    }

    private static void zipFiles(String zipname, SystemInfo sys) {
        try {
            File firstFile;
            File[] codeFilePaths, docFilePaths, testFilePaths;

            firstFile = new File(zipname);
            File codedir = new File(sys.getCodeFolder());
            File docdir = new File(sys.getJavaDocFolder());
            File testdir = new File(sys.getTestCaseFolder());

            codeFilePaths= codedir.listFiles((dir1, name)->name.endsWith("java"));
            docFilePaths= docdir.listFiles((dir2, name)->name.endsWith("html"));
            testFilePaths= testdir.listFiles((dir3, name)->name.endsWith("myOutput.txt"));
            System.out.println(sys.getTestCaseFolder());

            String zipFileName = firstFile.getName().concat(".zip");

            FileOutputStream fos = new FileOutputStream(zipFileName);
            ZipOutputStream zos = new ZipOutputStream(fos);

            zos.putNextEntry(new ZipEntry(zipname));
            try{
                for (File aFile : codeFilePaths) {

                    zos.putNextEntry(new ZipEntry(aFile.getName()));
                    System.out.println("Adding "+aFile + " to submission");
                    //System.out.println(aFile.getPath()+aFile.getName());
                    byte[] bytes = Files.readAllBytes(Paths.get(aFile.getPath()));
                    //byte[] bytes = Files.readAllBytes(Paths.get(aFile.getName()));
                    zos.write(bytes, 0, bytes.length);
                    zos.closeEntry();
                }
                for (File aFile : docFilePaths) {

                    zos.putNextEntry(new ZipEntry(aFile.getName()));
                    System.out.println("Adding "+aFile + " to submission");
                    //System.out.println(aFile.getPath()+aFile.getName());
                    byte[] bytes = Files.readAllBytes(Paths.get(aFile.getPath()));
                    //byte[] bytes = Files.readAllBytes(Paths.get(aFile.getName()));
                    zos.write(bytes, 0, bytes.length);
                    zos.closeEntry();
                }
           
                for (File aFile : testFilePaths) {

                    zos.putNextEntry(new ZipEntry(aFile.getName()));
                    System.out.println("Adding "+aFile + " to submission");
                    //System.out.println(aFile.getPath()+aFile.getName());
                    byte[] bytes = Files.readAllBytes(Paths.get(aFile.getPath()));
                    //byte[] bytes = Files.readAllBytes(Paths.get(aFile.getName()));
                    zos.write(bytes, 0, bytes.length);
                    zos.closeEntry();
                }

        }catch(NullPointerException npe){}
         finally{
            zos.close();
            System.out.println("================================================");
            System.out.println("=====Successfully created submission file ======");
            System.out.println("=======as "+zipFileName  +"================");
            System.out.println("===============================================");
            System.out.println("Remember you can submit to OurVLE as many times");
            System.out.println("as you wish before the deadline, so do not delay!!!");
            System.out.println("===============================================");
            System.out.println("============Please DO NOT access the ==========");
            System.out.println("======generated file prior to submission=======");
            System.out.println("===============================================");
         }
        } catch (FileNotFoundException ex) {
            System.err.println("A file does not exist: " + ex);
        } catch (IOException ex) {
            System.err.println("I/O error: " + ex);
            ex.printStackTrace();
        }
    }

    private static int getNonPrint(String id)
    {
        int retval = 0;
        for (int i=0; i<id.length();i++)
            try
            {
                retval+=i+Integer.parseInt(id.charAt(i)+"");
            }
            catch (NumberFormatException nfe)
            {
                retval+=i;
            }
        retval = retval%15+15;
        return retval;
    }

    private static int getHash(String txt)
    {
        int sumval = 20;
        int retval =0;
        for (int i=0; i<txt.length();i++)
            sumval+=((int)txt.charAt(i))%1371;
        retval = sumval%15+15;
        return retval;
    }

    private static String readCommunities(String cfile)
    {
        Scanner cscan = null;
        String cdata="";

        try
        {
            cscan  = new Scanner(new File(cfile));
            while(cscan.hasNext())
                cdata += cscan.nextLine();

            cscan.close();
        }
        catch(IOException e)
        {}

        return cdata;

    }

    private static String readDos(String dfile)
    {
        Scanner dscan = null;
        String ddata="";

        try
        {
            dscan  = new Scanner(new File(dfile));
            while(dscan.hasNext())
                ddata += dscan.nextLine();
            dscan.close();
        }
        catch(IOException e)
        {}

        return ddata;

    }

    private static String getCmFile(int caseno){
        return "cases/cm"+caseno+".txt";
    }

    private static String getDFile(int caseno){
        return "cases/D"+caseno+".txt";
    }

    private static String readData(int caseNo)
    {
        String cmdata = readCommunities(getCmFile(caseNo));
        String dosdata  =  readDos(getDFile(caseNo));

        return cmdata+dosdata;	
    }

    private static boolean validated(int i)
    {
        String txt = readData(i);
        int hash = getHash(txt);

        int hashval =0;

        try
        {
            Scanner sc = new Scanner(new File("cases/c"+i+".chk"));
            hashval = Integer.parseInt(sc.nextLine());
            sc.close();
        }
        catch (IOException ioe){}

        return (hash == hashval);

    }
}

