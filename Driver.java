
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
//import project.Tester;
public class Driver {
    public static void main(String[] args)  {

        Scanner scan = new Scanner(System.in);

        IO io = new IO();
        int selCase =1;

        SystemInfo sys = new SystemInfo();
        DoS dos = io.loadTestCase(1, scan);
        // s;

        char mchoice = 'c';
        boolean listdata = true;
        String menu="";
        String stuId="";

        while (mchoice!='X')
        {
            try{

                stuId = io.readValueFromFile("id.txt");
                String menuOptions = "=======";
                if (selCase!=-1)
                    menuOptions+="Case "+ selCase+"::";
                menuOptions += "Main Menu Options";
                menuOptions+= "=======\n";

                menuOptions+="[C]Community data\n[D]oS interface\n";
                menuOptions+="[L]oad test case\n[T]est all cases\n[S]ave test case\n";
                menuOptions+="=========System settings=========\n[I]D:";

                if (stuId.length()==0)
                    menuOptions+="Not yet set.\n";
                else
                    menuOptions+=stuId+"\n";

                menuOptions+="User directory:"+System.getProperty("user.dir")+"\n";
                menuOptions+="Test case folder:"+sys.getTestCaseFolder()+"\n";
                menuOptions+="Code folder:"+sys.getCodeFolder()+"\n";
                menuOptions+="Javadoc folder:"+sys.getJavaDocFolder()+"\n";
                menuOptions+="[A]utodetect system settings\n";
                menuOptions+="E[x]it\n";
                menuOptions+="====================================";

                System.out.println(menuOptions);
                if (!(System.getProperty("user.dir")+"\\cases").equals(sys.getTestCaseFolder()))
                {
                    System.out.println("WARNING:::POSSIBLE TEST CASE LOCATION MISMATCH:" );
                    System.out.println("Please move the cases folder from ");
                    System.out.println(sys.getTestCaseFolder()   + " to ");
                    System.out.println(System.getProperty("user.dir")+ " then hit A+<Enter> to detect locations");
                }            

                menu = scan.next().toUpperCase();
                mchoice = menu.charAt(0);
                switch(mchoice){
                    case 'C':{
                            dos.manageCommunities(io);
                            break;

                        }
                    case 'D':{
                            dos.showDosMenu();
                            break;
                        }
                    case 'L':{
                            boolean success = false;
                            while (!success){
                                try{
                                    System.out.println("Please enter the number of the test case to be loaded[1..10]:");
                                    int caseNo = Integer.parseInt(scan.next());
                                    selCase=caseNo;
                                    dos = io.loadTestCase(selCase,scan);
                                    success = true;
                                }catch (NumberFormatException nfe){
                                    System.out.println("That wasn't a number...");

                                }

                            }
                            break;
                        }
                    case 'T':{
                            Tester.runCases(io, dos,  sys);

                            // s = e.editSystemInfo(s);
                            break;

                        }
                    case 'S':{
                            Tester.saveTest(io, dos);

                            // s = e.editSystemInfo(s);
                            break;

                        }
                    case 'A':{
                            io.deleteFile("SysInfo.txt");
                            sys = new SystemInfo();

                            // s = e.editSystemInfo(s);
                            break;

                        }

                    case 'I':
                        {
                            System.out.println("Please enter your student ID number:");
                            String stuid = scan.next();

                            try{    
                                PrintStream p=new PrintStream(new FileOutputStream("id.txt"));
                                p.println(stuid);
                                p.close();
                            }
                            catch(IOException ioe){}
                            break;
                        } 

                }
            }
            catch(Exception e){
                System.out.println("This message was unexpected. Please contact the lecturer with the information below and stack trace data");
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }

    }
}
