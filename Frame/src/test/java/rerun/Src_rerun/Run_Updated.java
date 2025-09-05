package rerun.src_rerun;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class Run_Updated {
                public static String  filePath =System.getProperty("user.dir");
                public static String  fileName ="Run Manager.xlsx";
                public static File file ;
                public static ArrayList<String> PassedApplication= new  ArrayList<String>();
                public static ArrayList<String> PassedApplicationList= new  ArrayList<String>();
                public static ArrayList<String> FailedApplication= new  ArrayList<String>();
                public static ArrayList<String> FailedApplicationList= new  ArrayList<String>();

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
                                /*public static void main(String args[]) throws IOException{
//                                                  String[] valueToWrite = {"NonGA,Simon|ALL|ALL"};
                                                     Workbook spreadsheet = writeExcel();
                                                     Sheet worksheet = spreadsheet.getSheetAt(0);
                                                     int hrow = worksheet.getLastRowNum();
                                                     int colno =worksheet.getRow(0).getPhysicalNumberOfCells();
                                                     String[] sceanrio_testcase = args[0].split(Pattern.quote("|"));
                                                     System.out.println("-------------------------Execution Started-----------------------");
                                                     iterator_new(worksheet,colno,hrow,sceanrio_testcase[0],sceanrio_testcase[1],sceanrio_testcase[2]);
                                                     FileOutputStream outputStream = new FileOutputStream(file);
                                                                spreadsheet.write(outputStream);
                                         outputStream.close();
                                         File Runcheck = new File(System.getProperty("user.dir")+\\Run_2.txt);
                                         if(Runcheck.exists()){
                                                 Runcheck.delete();
                                         }
                                                     
                                }*/
                                public static void iterator_new(Sheet worksheet,int colno,int hrow,String sceanrio_arr,String app_arr,String Tcase_arr){
                                                String[] Scenario = sceanrio_arr.split(",");
                                                String[] AppName = app_arr.split(",");
                                                String[] Tcase = Tcase_arr.split(",");
                                                executechange(worksheet,colno,hrow,"None","TestScenario",false);
                                                if((Scenario.length==1)&(Scenario[0].equalsIgnoreCase("ALL"))){
                                                                executechange(worksheet,colno,hrow,Scenario[0],"TestScenario",false);
                                                }else if((Scenario.length==1)&!(Scenario[0].equalsIgnoreCase("ALL"))){
                                                                if((AppName.length==1)&(AppName[0].equalsIgnoreCase("ALL"))){
                                                                                executechange(worksheet,colno,hrow,Scenario[0],"TestScenario",false);
                                                                }else if((AppName.length==1)&!(AppName[0].equalsIgnoreCase("ALL"))){
                                                                                if((Tcase.length==1)&(Tcase[0].equalsIgnoreCase("ALL"))){
                                                                                                executechange(worksheet,colno,hrow,AppName[0],"ApplicationName",false);
                                                                                }else if((Tcase.length==1)&!(Tcase[0].equalsIgnoreCase("ALL"))){
                                                                                                executechange(worksheet,colno,hrow,Tcase[0],"TestCase",false);
                                                                                }else if((Tcase.length>1)){
                                                                                                for(int y=0;y<Tcase.length;y++){
                                                                                                                executechange(worksheet,colno,hrow,Tcase[y],"TestCase",true);
                                                                                                }
                                                                                }
                                                                                
                                                                }else if(AppName.length>1){
                                                                                for(int y=0;y<AppName.length;y++){
                                                                                                executechange(worksheet,colno,hrow,AppName[y],"ApplicationName",true);
                                                                                }
                                                                }
                                                }else if(Scenario.length>1){
                                                                for(int y=0;y<Scenario.length;y++){
                                                                                executechange(worksheet,colno,hrow,Scenario[y],"TestScenario",true);
                                                                }
                                                }
                                                
                                
                                
                
     }
                                public static void executechange (Sheet worksheet,int colno,int hrow,String compare,String COlname,boolean value){
                                                
                                                switch (compare) {
                                                case "ALL":
                                                                boolean flag =false;
                                                                int Execute = executecol(worksheet,colno,hrow,COlname,"Execute");
                                                                int Testcasename = executecol(worksheet,colno,hrow,COlname,"TestCase");
                                                                colloop:for(int j=0;j<colno;j++){
                                                                rowloop:   for(int i=1;i<=hrow;i++){
                                                                                                                                if(worksheet.getRow(0).getCell(j).getStringCellValue().trim().equals(COlname)){
                                                                                                                                                worksheet.getRow(i).getCell(j+Execute).setCellValue("Yes");
                                                                                                                                                System.out.println(worksheet.getRow(i).getCell(j+Testcasename).getStringCellValue()+" is Selected for Execution");
                                                                                                                                }else{
                                                                                                                                                break rowloop;
                                                                                                                                }
                                                                                                                                flag = true;
                                                                                                                }
                                                                                                                if(flag){
                                                                                                                                break colloop;
                                                                                                                }
                                                                                                }
                                                                break;

                                                default:
                                                                flag =false;
                                                                Execute = executecol(worksheet,colno,hrow,COlname,"Execute");
                                                                Testcasename = executecol(worksheet,colno,hrow,COlname,"TestCase");
                                                                colloop:for(int j=0;j<colno;j++){
                                                                rowloop:   for(int i=1;i<=hrow;i++){
                                                                                                                                if(worksheet.getRow(0).getCell(j).getStringCellValue().trim().equals(COlname)){
                                                                                                                                                if(                worksheet.getRow(i).getCell(j).getStringCellValue().trim().equals(compare) ){
                                                                                                                                                worksheet.getRow(i).getCell(j+Execute).setCellValue("Yes");
                                                                                                                                                System.out.println(worksheet.getRow(i).getCell(j+Testcasename).getStringCellValue()+" is Selected for Execution");
                                                                                                                                }else{
                                                                                                                                                if(!value){
                                                                                                                                                                worksheet.getRow(i).getCell(j+Execute).setCellValue("No");
                                                                                                                                                }
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
                                                                break;
                                                }
                                }
                @SuppressWarnings("unused")
                public static void executeYes(Sheet worksheet,int colno,int hrow,String compare,String COlname,Boolean EmailFlag){
                                                
                                                int  flag =0;
                                                int  flag1 =0;
                                                int Execute = executecol(worksheet,colno,hrow,COlname,"Execute");
                                                int ApplicationName = executecol(worksheet,colno,hrow,COlname,"ApplicationName");
                                                colloop:for(int j=0;j<colno;j++){
                                                rowloop:   for(int i=1;i<=hrow;i++){
                                                                                                                if(worksheet.getRow(i).getCell(j+Execute).getStringCellValue().trim().equalsIgnoreCase("Yes")){
                                                                                                                                PassedApplication.add(worksheet.getRow(i).getCell(j+ApplicationName).getStringCellValue().trim());
                                                                                                                                flag = flag+1;
                                                                                                                                if(flag==hrow) {
                                                                                                                                                break colloop;
                                                                                                                                }
                                                                                                                }else {
                                                                                                                                flag1 =flag1+1;
                                                                                                                                if(flag1==hrow) {
                                                                                                                                                break colloop;
                                                                                                                                }
                                                                                                                }
                                                                                                }
                                                                                }
                                                if(EmailFlag) {
                                                                PassedApplicationList = removeDuplicates(PassedApplication);
                                                                PrintWriter writer;
                                                                try {
                                                                                writer = new PrintWriter("ApplicationExecuted.txt", "UTF-8");
                                                                                writer.println(PassedApplicationList.size());
                                                                                writer.close();
                                                                } catch (FileNotFoundException e) {
                                                                                e.printStackTrace();
                                                                } catch (UnsupportedEncodingException e) {
                                                                                e.printStackTrace();
                                                                }
                                                }
                                                
                } 
                @SuppressWarnings("unused")
                public static void failureexecuteYes(Sheet worksheet,int colno,int hrow,String compare,String COlname,Boolean EmailFlag){
                                
                                int  flag =0;
                                int  flag1 =0;
                                int Execute = executecol(worksheet,colno,hrow,COlname,"Execute");
                                int ApplicationName = executecol(worksheet,colno,hrow,COlname,"ApplicationName");
                                colloop:for(int j=0;j<colno;j++){
                                rowloop:   for(int i=1;i<=hrow;i++){
                                                                                                if(worksheet.getRow(i).getCell(j+Execute).getStringCellValue().trim().equalsIgnoreCase("Yes")){
                                                                                                                FailedApplication.add(worksheet.getRow(i).getCell(j+ApplicationName).getStringCellValue().trim());
                                                                                                                flag = flag+1;
                                                                                                                if(flag==hrow) {
                                                                                                                                break colloop;
                                                                                                                }
                                                                                                }else {
                                                                                                                flag1 =flag1+1;
                                                                                                                if(flag1==hrow) {
                                                                                                                                break colloop;
                                                                                                                }
                                                                                                }
                                                                                }
                                                                }
                                if(EmailFlag) {
                                                PrintWriter writer,writer_1;
                                                FailedApplicationList = removeDuplicates(FailedApplication);
                                                String Data=null;
                                                if(FailedApplicationList.size()>0) {
                                                                for(int g=0;g<FailedApplicationList.size();g++) {
                                                                                if(g==0) {
                                                                                                Data = (g+1)+". "+FailedApplicationList.get(g);
                                                                                }else {
                                                                                                Data = Data+System.getProperty("line.separator")+(g+1)+". "+FailedApplicationList.get(g);
                                                                                }
                                                                }
                                                }else {
                                                                Data = "None";
                                                }
                                                
                                                FailedApplicationList = removeDuplicates(FailedApplication);
                                                try {
                                                                writer = new PrintWriter("ApplicationFailed.txt", "UTF-8");
                                                                writer_1 = new PrintWriter("FailedApplicationName.txt", "UTF-8");
                                                                writer_1.println(Data);
                                                                writer.println(FailedApplicationList.size());
                                                                writer.close();
                                                                writer_1.close();
                                                } catch (FileNotFoundException e) {
                                                                e.printStackTrace();
                                                } catch (UnsupportedEncodingException e) {
                                                                e.printStackTrace();
                                                }
                                                try {
                                                                writer = new PrintWriter("ApplicationPassed.txt", "UTF-8");
                                                                writer.println(PassedApplicationList.size() - FailedApplicationList.size());
                                                                writer.close();
                                                } catch (FileNotFoundException e) {
                                                                e.printStackTrace();
                                                } catch (UnsupportedEncodingException e) {
                                                                e.printStackTrace();
                                                }
                                }
                                
} 
                    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list) 
                    { 
                  
                        ArrayList<T> newList = new ArrayList<T>(); 
                  
                        for (T element : list) { 
                            if (!newList.contains(element)) { 
                                newList.add(element); 
                            } 
                        } 
                        return newList; 
                    } 
                                
                                
                    public static int executecol(Sheet worksheet,int colno,int hrow,String colvalue,String findCol){
                                                try{
                                                                int executecolvalue =0;
                                                                int startflag = 0;
                                                                                                for(int j=0;j<colno;j++){
                                                                                rowloop:for(int i=1;i<=hrow;){
                                                                                                                if((worksheet.getRow(0).getCell(j).getStringCellValue().equals(colvalue))|(worksheet.getRow(0).getCell(j).getStringCellValue().equals(findCol))){
                                                                                                                                                startflag=startflag+1;
                                                                                                                                                if(startflag>1){
                                                                                                                                                                return executecolvalue+1;
                                                                                                                                                }
                                                                                                                                                break rowloop;
                                                                                                                                }else{
                                                                                                                                                executecolvalue+=startflag;
                                                                                                                                                break rowloop;
                                                                                                                                }
                                                                                                                }
                                                                                                }
                                                                                                                                
                                                                                                
                                                }catch(Exception e){
                                                                System.out.println("Excel Exception "+e.toString());
                                                                return 0;
                                                }
                                                return 0;
                
                                                
                                }
                                     
                                   
                }


                
                

                

