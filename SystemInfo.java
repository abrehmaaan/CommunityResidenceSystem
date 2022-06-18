 

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

public class SystemInfo {

	private String codeFolder="Not detected";
	private String testCaseFolder="Not detected";
	private String javaDocFolder="Not detected";

	public SystemInfo()
	{
		  try
          {
              Scanner sc = new Scanner(new File("Sysinfo.txt"));
              if (sc.hasNextLine())
            	  codeFolder = sc.nextLine();
              if (sc.hasNextLine())
            	  testCaseFolder = sc.nextLine();
              if (sc.hasNextLine())
            	  javaDocFolder = sc.nextLine();
              sc.close();
          }
          catch (IOException ioe){System.out.print("sysinfo not detected");}

      if (codeFolder.equals("Not detected"))

      {
		try (Stream<Path> walkStream = Files.walk(Paths.get((System.getProperty("user.dir"))))) {
			walkStream.filter(p -> p.toFile().isFile()).forEach(f -> {
				if (f.toString().endsWith(".java")) {
					codeFolder = f.getParent().toString();
				}
			});
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try (Stream<Path> walkStream = Files.walk(Paths.get((System.getProperty("user.dir"))))) {
			walkStream.filter(p -> p.toFile().isFile()).forEach(f -> {
				if (f.toString().endsWith("0.txt")) {
					testCaseFolder = f.getParent().toString();
				}
			});
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try (Stream<Path> walkStream = Files.walk(Paths.get((System.getProperty("user.dir"))))) {
			walkStream.filter(p -> p.toFile().isFile()).forEach(f -> {
				if (f.toString().endsWith(".html")) {
					javaDocFolder = f.getParent().toString();
				}
			});
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (!(codeFolder.equals("Not Detected")))
		{
			//save system data
			try{    
				PrintStream p=new PrintStream(new FileOutputStream("SysInfo.txt"));
				p.println(codeFolder);
				p.println(testCaseFolder);
				p.println(javaDocFolder);
				//p.println(sumScore);
				p.close();
			}
			catch(IOException ioe){}
		
			
		}
			
      }
	}
	
	public String getCodeFolder()
	{
		return codeFolder;
		
	}
	public String getTestCaseFolder()
	{
		return testCaseFolder;
		
	}
	public String getJavaDocFolder()
	{
		return javaDocFolder;
		
	}
	
	
	
	public void readFromScreen(Scanner scan)
	{
		scan.nextLine();// to clear buffer
		System.out.print("Hit enter to keep code folder as ["+codeFolder+"] or enter new code folder:");
		String dataval = scan.nextLine();
		if (dataval.length()>0)
				codeFolder = dataval;
		System.out.print("Hit enter to keep test case folder as ["+testCaseFolder+"] or enter new test case folder:");
		 dataval = scan.nextLine();
		 if (dataval.length()>0)
				testCaseFolder = dataval;
		System.out.print("Hit enter to keep javadoc folder as ["+javaDocFolder+"] or enter new javadoc folder:");
		 dataval = scan.nextLine();
		 if (dataval.length()>0)
				javaDocFolder = dataval;

			try{    
				PrintStream p=new PrintStream(new FileOutputStream("SysInfo.txt"));
				p.println(codeFolder);
				p.println(testCaseFolder);
				p.println(javaDocFolder);
				//p.println(sumScore);
				p.close();
			}
			catch(IOException ioe){}
		
	}

}
