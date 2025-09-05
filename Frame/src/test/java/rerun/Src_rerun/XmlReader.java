package rerun.src_rerun;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarException;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import rerun.*;


@SuppressWarnings("unused")
public class XmlReader {
                public static String currentDir = System.getProperty("user.dir")+\\Results;
                public static String fileseperator = System.getProperty("file.separator");
                public static String  filePath =System.getProperty("user.dir")+fileseperator+"src"+fileseperator+"test"+fileseperator+"resources";
                public static String  filePath_RI =System.getProperty("user.dir")+fileseperator+"src"+fileseperator+"test"+fileseperator+"java"+fileseperator+"rerun"+fileseperator;
                public static String  fileName ="Run Manager.xlsx";
                //private  final static RI_Parser RiBot = new RI_Parser();
                public static File file ;
                static Fillo fillo = new Fillo();
                static Connection connection;
                static Connection connection1;
                static Recordset recordset;
                static String excelFilePath=filePath_RI+fileseperator+"RI_Parser"+fileseperator;
                private BufferedReader error;
                private BufferedReader op;
                private int exitVal;
                
                @SuppressWarnings("static-access")
                public void mainClass() {
                                Document doc;
                    try {
                                 InputStream input_prop = null;
                                 Properties properties = new Properties();
                                 input_prop = new FileInputStream(filePath+fileseperator+"Global Settings.properties");
                         properties.load(input_prop);
                                 Workbook spreadsheet = writeExcel();
                                                Sheet worksheet = spreadsheet.getSheetAt(0);
                                     int hrow = worksheet.getLastRowNum();
                                     int colno =worksheet.getRow(0).getPhysicalNumberOfCells();
                                     Run_Updated.executeYes(worksheet,colno,hrow,"None", "TestScenario",Boolean.parseBoolean(properties.getProperty("EmailJenk")));
                                     if(Boolean.parseBoolean(properties.getProperty("RIBOT"))) {
                                                 riWrite();
                                                 //RiBot.riBot(null);
                                     }
                                file = new File(filePath+ "\\"+ fileName);  
                                                String reportpath = currentDir+fileseperator+"Summary"+fileseperator+"HTMLResults"+fileseperator+"Summary.html";
                                File input = new File(reportpath);
                                String reportpathjenkins = currentDir+fileseperator+"Summary"+fileseperator+"HTMLResults"+fileseperator+"Jenkins_Summary.html";
                                File input1 = new File(reportpathjenkins);
                                doc = Jsoup.parse(input, "UTF-8", http://example.com/);
                        Elements links = doc.select("tr[class=content]");
                        ArrayList<String> newarrayRun = fromRunmamager(worksheet,colno,hrow);
                        ArrayList<String> newarrayhtml =new ArrayList<String>();
                        ArrayList<String> FailureTestcase =new ArrayList<String>();
                        int passed=0,failed=0;
                        for(int i=0;i < links.size()  ;i++ ) {
                                Elements summary =   links.get(i).select("td");
                                if(summary.get(6).text().contains("Passed")){
                                                passed++;
                                }else{
                                                failed++;
                                }
                                newarrayhtml.add(summary.get(1).text()+"|"+summary.get(6).text());
                        }
run:        for(int i=0;i<newarrayRun.size();i++){
                                                                boolean runfalg = false;
html:                     for(int j=0;j<newarrayhtml.size();j++){
                                                String[] singleTestcase = newarrayhtml.get(j).split(Pattern.quote("|"));
                                                if(singleTestcase[0].equalsIgnoreCase(newarrayRun.get(i))){
                                                                if(singleTestcase[1].equalsIgnoreCase("Failed")){
                                                                                FailureTestcase.add(singleTestcase[0]);
                                                                                runfalg=true;
                                                                                break html;
                                                                }else{
                                                                                runfalg=true;
                                                                                break html;
                                                                }
                                                }
                                }
                                                                if(!runfalg){
                                                                                FailureTestcase.add(newarrayRun.get(i));
                                                                }
                        }
                        Run_Updated.executechange(worksheet,colno,hrow,"None","TestScenario",false);
                        
                        if(FailureTestcase.size()>0) {
                                System.out.println("---------------------Failure ReExecution Started-------------------------");
                                        for(int h=0;h<FailureTestcase.size();h++){
                                                Run_Updated.executechange(worksheet, colno, hrow, FailureTestcase.get(h), "TestCase", true);
                                        }
                        }else {
                                  System.out.println("--------------------All TestCase Passed -------------------------");         
                        }
                        FileOutputStream outputStream = new FileOutputStream(file);
                                                spreadsheet.write(outputStream);
                        outputStream.close();
                        try {
                                                                PrintWriter writer = new PrintWriter("Run_2.txt", "UTF-8");
//                                                             System.out.println("Run_2");
                                                                writer.close();
                                                } catch (FileNotFoundException e) {
                                                                e.printStackTrace();
                                                } catch (UnsupportedEncodingException e) {
                                                                e.printStackTrace();
                                                }
                        if(input.exists()) {
                                input.delete();
                        }
                        File reportpathnew =new File( currentDir+fileseperator+"Summary"+fileseperator+"HTMLResults"+fileseperator+"Summary.html");
                        input1.renameTo(reportpathnew);
                    }catch(Exception e){
                                System.out.println(e.toString());
                    }

}
                @SuppressWarnings("static-access")
                public void FailuremainClass() {
                                Document doc;
                    try {
                                 InputStream input_prop = null;
                                 Properties properties = new Properties();
                                 input_prop = new FileInputStream(filePath+fileseperator+"Global Settings.properties");
                         properties.load(input_prop);
                                 Workbook spreadsheet = writeExcel();
                                                Sheet worksheet = spreadsheet.getSheetAt(0);
                                     int hrow = worksheet.getLastRowNum();
                                     int colno =worksheet.getRow(0).getPhysicalNumberOfCells();
                                file = new File(filePath+ "\\"+ fileName);  
                                String reportpath = currentDir+fileseperator+"Summary_2"+fileseperator+"HTMLResults"+fileseperator+"Summary.html";
                                File input = new File(reportpath);
                                String reportpathJenkins = currentDir+fileseperator+"Summary_2"+fileseperator+"HTMLResults"+fileseperator+"Jenkins_Summary.html";
                                File input1 = new File(reportpathJenkins);
                                doc = Jsoup.parse(input, "UTF-8", http://example.com/);
                        Elements links = doc.select("tr[class=content]");
                        ArrayList<String> newarrayRun = fromRunmamager(worksheet,colno,hrow);
                        ArrayList<String> newarrayhtml =new ArrayList<String>();
                        ArrayList<String> FailureTestcase =new ArrayList<String>();
                        int passed=0,failed=0;
                        for(int i=0;i < links.size()  ;i++ ) {
                                Elements summary =   links.get(i).select("td");
                                if(summary.get(6).text().contains("Passed")){
                                                passed++;
                                }else{
                                                failed++;
                                }
                                newarrayhtml.add(summary.get(1).text()+"|"+summary.get(6).text());
                        }
run:        for(int i=0;i<newarrayRun.size();i++){
                                                                boolean runfalg = false;
html:                     for(int j=0;j<newarrayhtml.size();j++){
                                                String[] singleTestcase = newarrayhtml.get(j).split(Pattern.quote("|"));
                                                if(singleTestcase[0].equalsIgnoreCase(newarrayRun.get(i))){
                                                                if(singleTestcase[1].equalsIgnoreCase("Failed")){
                                                                                FailureTestcase.add(singleTestcase[0]);
                                                                                runfalg=true;
                                                                                break html;
                                                                }else{
                                                                                runfalg=true;
                                                                                break html;
                                                                }
                                                }
                                }
                                                                if(!runfalg){
                                                                                FailureTestcase.add(newarrayRun.get(i));
                                                                }
                        }
                        if(Boolean.parseBoolean(properties.getProperty("RIBOT"))) {
                                //RiBot.riBot(null);
                                     }
                        Run_Updated.executechange(worksheet,colno,hrow,"None","TestScenario",false);
                        if(FailureTestcase.size()>0) {
                                
                                        for(int h=0;h<FailureTestcase.size();h++){
                                                Run_Updated.executechange(worksheet, colno, hrow, FailureTestcase.get(h), "TestCase", true);
                                        }
                        }
                        FileOutputStream outputStream = new FileOutputStream(file);
                                                spreadsheet.write(outputStream);
                                                Run_Updated.failureexecuteYes(worksheet, colno, hrow, "None", "TestScenario",Boolean.parseBoolean(properties.getProperty("EmailJenk")));
                        outputStream.close();
                        File reportpathnew =new File( currentDir+fileseperator+"Summary_2"+fileseperator+"HTMLResults"+fileseperator+"RerunSummary.html");
                        input1.renameTo(reportpathnew);
                    }catch(Exception e){
                                System.out.println(e.toString());
                    }

}
                public static ArrayList<String> fromRunmamager(Sheet worksheet,int colno,int hrow){
                                ArrayList<String> runexecute = new ArrayList<String>();
                                boolean flag=false;
                                int testcasecol = Run_Updated.executecol(worksheet,colno,hrow,"TestCase","Execute");
                                colloop:for(int j=0;j<colno;j++){
                                rowloop:   for(int i=1;i<=hrow;i++){
                                                                                                if(worksheet.getRow(0).getCell(j).getStringCellValue().trim().equals("Execute")){
                                                                                                                if(                worksheet.getRow(i).getCell(j).getStringCellValue().trim().equals("Yes") ){
                                                                                                                                runexecute.add(worksheet.getRow(i).getCell(j-testcasecol).getStringCellValue().trim());
                                                                                                                }
                                                                                                }else{    
                                                                                                                break rowloop;
                                                                                                }
                                                                                flag = true;
                                                                                }
                                                                                if(flag){
                                                                                                break colloop;
                                                                                }
                                                               }
                                return runexecute;
                    
                }


    public static  Workbook writeExcel() throws IOException
                {
       file = new File(filePath+ "\\"+ fileName);  
                   FileInputStream inputStream = new FileInputStream(file);
                   Workbook writeWorkbook = null;
                   String fileExtensionName = fileName.substring(fileName.indexOf("."));
                   if(fileExtensionName.equals(".xlsx"))
                    {
                      writeWorkbook = new XSSFWorkbook(inputStream);
                    }
                    else if(fileExtensionName.equals(".xls"))
                    {
                    writeWorkbook = new HSSFWorkbook(inputStream);
                    }
                    return writeWorkbook;
                }
    public void riWrite() {
                try {
                                connection = fillo.getConnection(filePath_RI+"RIManager.xls");
                                InputStream input = null;
                                String ReprotPath =null;
                                Properties properties = new Properties();
                                input = new FileInputStream(filePath+fileseperator+"Global Settings.properties");
                                properties.load(input);
                                String projectname = properties.getProperty("ProjectNameRI");
                                String applicationName = properties.getProperty("Application");
                                String automationFramework = properties.getProperty("Automation_Framework");
                                String platform = properties.getProperty("Platform");
                                String toolname = properties.getProperty("Tool_Name");
                                String reportName = properties.getProperty("Report_Name");
                                String bugsheet = properties.getProperty("Bug_Category_Sheet_Name");
                                String environment = properties.getProperty("ENVRI");
                                String dbaccess = properties.getProperty("DB_Access");
                                String Path = System.getProperty("user.dir");
                                String[] jenkins = System.getProperty("user.dir").split(Pattern.quote(File.separator));
                                String JenkinsJobName = jenkins[jenkins.length-1];
                                PrintWriter writer;
                                try {
                                                writer = new PrintWriter(filePath_RI+"JobName.txt", "UTF-8");
                                                writer.println(JenkinsJobName);
                                                writer.close();
                                } catch (FileNotFoundException e) {
                                                e.printStackTrace();
                                } catch (UnsupportedEncodingException e) {
                                                e.printStackTrace();
                                }
                                String query = "Select * FROM Application_Details";
                                recordset = connection.executeQuery(query);
                                while(recordset.next()) {
                                                if(recordset.getField("Jenkins_JobName").equalsIgnoreCase(JenkinsJobName)) {
                                                                query = "DELETE FROM Application_Details WHERE Jenkins_JobName ='"+JenkinsJobName+"'";
                                                connection.executeUpdate(query);
                                                break;
                                                }
                                }
                                if (System.getProperty("ReportPath") != null) {
                                                ReprotPath = System.getProperty("user.dir")+fileseperator+"target"+fileseperator+"CRAFTReports";
                                }else {
                                                ReprotPath = System.getProperty("user.dir")+fileseperator+"Results"+fileseperator+properties.getProperty("RunConfiguration");
                                }
                                query = "INSERT INTO Application_Details(Jenkins_JobName,Project_Name,Application_Name,Report_Location,Automation_Framework,Platform,Environment,Tool_Name,Report_Name,Bug_Category_Sheet_Name,DB_Access) "
                                                                + "VALUES('"+JenkinsJobName+"','"+projectname+"','"+applicationName+"','"+ReprotPath+"','"+automationFramework+"','"+platform+"','"+environment+"','"+toolname+"','"+reportName+"','"+bugsheet+"','"+dbaccess+"')";
                                                connection.executeUpdate(query);
                }catch(Exception e) {
                                System.out.println(e.toString());
                }
                
    }
}

