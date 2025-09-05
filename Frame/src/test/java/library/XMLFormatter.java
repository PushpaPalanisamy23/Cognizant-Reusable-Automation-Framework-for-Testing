

/*package library;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileFilter;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

@SuppressWarnings("unused")
public class XMLFormatter   {

                
                static BufferedReader in;
                static StreamResult out;
                static TransformerHandler th;
                static AttributesImpl atts;

                // db details
                // final static String INSERT_SQL =
                // "INSERT INTO IMEDB.T1(id, str) values(?,?)";
                final static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
                final static String DB_URL = "jdbc:mysql://localhost:3306/Mysql"; //properties.getProperty("DB_URL");
                final static String USER = "root";//properties.getProperty("DB_USER");
                final static String PASS = "root";//properties.getProperty("DB_PASS");
                static Connection conn = null;
                public static ResultSet rs;
                public static PreparedStatement ps;
                ReadDataTable dataTable = new ReadDataTable();
                
                // Get The Latest Run from the Test Results Folder
                public String getTheNewestFile(String filePath, String ext) {
                    File theNewestFile = null;
                    File dir = new File(filePath);
                    FileFilter fileFilter = new WildcardFileFilter("*." + ext);
                    File[] files = dir.listFiles(fileFilter);
                String fileName;
                    if (files.length > 0) {
                        *//** The newest file comes first **//*
                        Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
                        theNewestFile = files[0];
                    }
                    fileName = theNewestFile.getName();
                    String directory = System.getProperty("user.dir");
                                String fileseperator = System.getProperty("file.separator");
                                String strlastrun = directory+fileseperator+"Output"+fileseperator+"lastrun.txt"; 
                   
                    try {
                    File lastRun = new File(strlastrun);
                    
                                                BufferedWriter br=new BufferedWriter(new FileWriter(lastRun));
                                                br.write(fileName);
                                                br.close();
                                } catch (IOException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                }
//                 dataTable.putData("General_Data","SourceXMLFile",fileName);
                    return fileName;
                }
                
                
public String getLastExecutedRun(String directory,String fileseperator){
                                
                String strpreviousSourceXMLFile = null;
                try{
                                Scanner buildNumberscanner = new Scanner( new File(directory+fileseperator+"Output"+fileseperator+"lastrun.txt"));
                                strpreviousSourceXMLFile = buildNumberscanner.useDelimiter(\\Z).next();
                                buildNumberscanner.close();
                                }
                                catch(Exception e) {
                                                e.getStackTrace();
                                }
                return strpreviousSourceXMLFile;
}
                
                
                // Flush the intermediate old xml file & old text file 
                public void flushOldxmlFile_OldTxtFile(String OldxmlFile,String OldTxtFile){
                                
                                PrintWriter outputxmlFilewriter;
                                try {
                                                outputxmlFilewriter = new         PrintWriter(OldxmlFile);
                                                outputxmlFilewriter.print("");
                                                outputxmlFilewriter.close();
                                } catch (FileNotFoundException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                } 
                                 
                                PrintWriter txtFilewriter;
                                try {
                                                txtFilewriter = new PrintWriter(OldTxtFile);
                                                txtFilewriter.print(""); 
                                                txtFilewriter.close();      
                                } catch (FileNotFoundException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                }
                }
                
                
                public void xmlparser(String sourceXMLFilePath, String textFile,
                                                String outputxmlFile,String jobName) throws TransformerConfigurationException,
                                                ParserConfigurationException, SAXException, IOException {

                                try {
                                String testId,testName, duration,
                                startTime,endTime,status,methodName,executionID,executionStartTime,executionEndTime,executionDuration;
                                String total = null,executed= null,passed= null,failed= null,error=
                                null,timeout= null,aborted= null,inconclusive=
                                null,passedButRunAborted= null,notRunnable= null,notExecuted=
                                null,disconnected= null,warning= null,completed= null,inProgress=
                                null,pending= null;
                                String[] methodClassName=null;
                                int screenshotpathstartindex,screenshotpathendindex;
                                
                                File fXmlFile = new File(sourceXMLFilePath);
                                
                                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                                Document doc = dBuilder.parse(fXmlFile);
                                doc.getDocumentElement().normalize();
                                
                                NodeList nList = doc.getElementsByTagName("UnitTestResult");
                                NodeList Message = doc.getElementsByTagName("Message");
                                NodeList StackTrace = doc.getElementsByTagName("StackTrace");
                                NodeList Counters = doc.getElementsByTagName("Counters");
                                NodeList TestMethod = doc.getElementsByTagName("TestMethod");
                                
                                NodeList StdOut = null;
                                if(doc.getElementsByTagName("StdOut")!=null) {
                                StdOut = doc.getElementsByTagName("StdOut");
                                }
                                
                                
                                
                                NodeList TestRunList = doc.getElementsByTagName("TestRun");
                                Node testNode = TestRunList.item(0);
                                Element testRunElement = (Element) testNode;
                                executionID = testRunElement.getAttribute("id");
                                
                                NodeList TimesList = doc.getElementsByTagName("Times");
                                Node TimeNode = TimesList.item(0);
                                Element TimeNodeElement = (Element) TimeNode;
                                
                                 executionStartTime = TimeNodeElement.getAttribute("start");
                                int exestartTimeindex = executionStartTime.indexOf(".");
                                executionStartTime= executionStartTime.substring(0,exestartTimeindex);
                                executionStartTime = executionStartTime.replace("T", " ");
                                
                                
                                executionEndTime = TimeNodeElement.getAttribute("finish");
                                int exeEndTimeindex = executionEndTime.indexOf(".");
                                executionEndTime = executionEndTime.substring(0,exeEndTimeindex);
                                executionEndTime = executionEndTime.replace("T", " ");
                                
                                 
                                 executionDuration = executionTimeComputation(executionStartTime,executionEndTime);
                                
                                
                                 String[] steps=new String[nList.getLength()];
                                String[] errorMessage=new String[nList.getLength()];
                                String[] StackTraceMessage=new String[nList.getLength()];
                                String[] screenshotpath=new String[nList.getLength()];
                                
                                for (int temp = 0,j=0,k=0; temp < nList.getLength(); temp++) { //
                                
                                
                                Node nNode = nList.item(temp);
                                Node MessageItem = Message.item(k);
                                Node StackTraceItem = StackTrace.item(k);
                                Node Counter = Counters.item(0);
                                Node method = TestMethod.item(temp);
                                
                                
                                Element CounterElement = (Element) Counter;
                                total = CounterElement.getAttribute("total");
                                executed = CounterElement.getAttribute("executed");
                                passed = CounterElement.getAttribute("passed");
                                failed = CounterElement.getAttribute("failed");
                                error = CounterElement.getAttribute("error");
                                timeout = CounterElement.getAttribute("timeout");
                                aborted = CounterElement.getAttribute("aborted");
                                inconclusive = CounterElement.getAttribute("inconclusive");
                                passedButRunAborted = CounterElement.getAttribute("passedButRunAborted");
                                notRunnable = CounterElement.getAttribute("notRunnable");
                                notExecuted = CounterElement.getAttribute("notExecuted");
                                disconnected = CounterElement.getAttribute("disconnected");
                                warning = CounterElement.getAttribute("warning");
                                completed = CounterElement.getAttribute("completed");
                                disconnected = CounterElement.getAttribute("disconnected");
                                inProgress = CounterElement.getAttribute("inProgress");
                                pending = CounterElement.getAttribute("pending");
                                
                                Element eElement = (Element) nNode;
                                testId = eElement.getAttribute("testId");
                                testName = eElement.getAttribute("testName");
                                
                                duration= eElement.getAttribute("duration");
                                int durationindex = duration.indexOf(".");
                                duration = duration.substring(0,durationindex);
                                
                                startTime=eElement.getAttribute("startTime");
                                int startTimeindex = startTime.indexOf(".");
                                startTime = startTime.substring(0,startTimeindex);
                                startTime = startTime.replace("T", " ");
                                
                                endTime=eElement.getAttribute("endTime");
                                int endTimeindex = endTime.indexOf(".");
                                endTime = endTime.substring(0,endTimeindex);
                                endTime = endTime.replace("T", " ");
                                
                                status =eElement.getAttribute("outcome");
                                Element methodElement = (Element) method;
                                methodName = methodElement.getAttribute("className");
                                methodName = methodName.replace(".", ";");
                                methodClassName =methodName.split(";");
                                methodName = methodClassName[methodClassName.length-2];
                                
                                updateTextLog(textFile,methodName,testId,testName,duration,startTime,endTime,status);
                                
                                if(status.contains("Failed")){
                                if(nNode.getTextContent()!=null){
                                if(MessageItem.getTextContent()!=null &&
                                StackTraceItem.getTextContent()!=null){
                                k++;
                                errorMessage[temp] = MessageItem.getTextContent();
                                StackTraceMessage[temp] = StackTraceItem.getTextContent();
                                }
                                
                                }
                                else{
                                errorMessage[temp]=""; StackTraceMessage[temp]="";
                                }
                                }else { errorMessage[temp]="NA"; StackTraceMessage[temp]="NA"; }
                                
                                if(StdOut.item(j)!=null){
                                Node StdOutItem = StdOut.item(j);
                                
                                if(status.contains("Failed")){
                                if(nNode.getTextContent()!=null){
                                if(StdOutItem.getTextContent()!=null){
                                j++;
                                steps[temp] = StdOutItem.getTextContent();
                                }
                                
                                }
                                else
                                steps[temp]="";
                                }else steps[temp]="NA";
                                
                                if(steps[temp].contains("[[ATTACHMENT|")){
                                screenshotpathstartindex = steps[temp].lastIndexOf("ATTACHMENT|");
                                screenshotpathendindex = steps[temp].lastIndexOf(".png");
                                screenshotpath[temp] =
                                steps[temp].substring(screenshotpathstartindex+11,
                                screenshotpathendindex+4);
                                }else screenshotpath[temp]="NA";
                                }
                                
                                }
                                if(steps!=null) {
                //             converstionToXml(textFile,outputxmlFile,executionID,steps,errorMessage,StackTraceMessage,screenshotpath,
                //                                             total,executed,passed,failed,error,timeout,aborted,inconclusive,passedButRunAborted,notRunnable,notExecuted,disconnected,warning,completed,inProgress,pending,executionStartTime,executionEndTime,executionDuration,jobName);
                                }
                                
                                } catch (Exception e) {
                                System.out.println(e.getMessage()+" in xmlparser");
                                System.out.println("here");
                                                e.printStackTrace();
                                }

                }

                
                
                
                
                
                
                public static void updateTextLog(String textFile, String Module,
                                                String testId, String testName, String duration, String startTime,
                                                String endTime, String status) {

                                try {

                                                File txtFile = new File(textFile);
                                                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(
                                                                                txtFile, true));
                                                String testStepRow = Module + "|";
                                                testStepRow += testId + "|";
                                                testStepRow += testName + "|";
                                                testStepRow += duration + "|";
                                                testStepRow += startTime + "|";
                                                testStepRow += endTime + "|";
                                                testStepRow += status;
                                                bufferedWriter.write(testStepRow);
                                                bufferedWriter.newLine();
                                                bufferedWriter.close();

                                } catch (Exception e) {
                                                System.out.println("Exception in updateTextLog" + e.getClass());
                                }
                }

                public static void converstionToXml(String textFile, String outputxmlFile,
                                                String executionID, String[] steps, String[] errorMessage,
                                                String[] StackTraceMessage, String[] screenshotpath, String total,
                                                String passed, String failed, String notExecuted,
                                    String startTime, String endTime,String executionDuration,String jobName)
                                                throws TransformerConfigurationException,
                                                ParserConfigurationException, SAXException, IOException {

                                
                                try {
                                                in = new BufferedReader(new FileReader(textFile));
                                                StreamResult out = new StreamResult(outputxmlFile);
                                                SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory
                                                                                .newInstance();
                                                th = tf.newTransformerHandler();
                                                Transformer serializer = th.getTransformer();
                                                serializer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
                                                serializer.setOutputProperty(
                                                                                "{http://xml.apache.org/xslt}indent-amount", "8");
                                                serializer.setOutputProperty(OutputKeys.INDENT, "yes");
                                                th.setResult(out);
                                                th.startDocument();
                                                atts = new AttributesImpl();

                                                th.startElement("", "", "TestRun", atts);
                                                
                                                th.startElement("", "", "ExecutionDetails", atts);
                                                
                                                th.startElement("", "", "ExecutionID", atts);
                                                th.characters(executionID.toCharArray(), 0, executionID.length());
                                                th.endElement("", "", "ExecutionID");
                                                
                                                th.startElement("", "", "ExecutionName", atts);
                                                th.characters(jobName.toCharArray(), 0, jobName.length());
                                                th.endElement("", "", "ExecutionName");
                                                
                                                th.endElement("", "", "ExecutionDetails");
                                                
                                                
                                                String str;
                                                int intSNOCounter = 0;

                                                while ((str = in.readLine()) != null) {
                                                                process(str, intSNOCounter, steps[intSNOCounter],
                                                                                                errorMessage[intSNOCounter],
                                                                                                StackTraceMessage[intSNOCounter],
                                                                                                screenshotpath[intSNOCounter]);
                                                                intSNOCounter++;
                                                }
                                                ResultSummary(total,executionDuration, passed, failed,notExecuted,startTime, endTime);
                                                in.close();
                                                closeXML();
                                                
                                                } catch (FileNotFoundException e) {
                                                System.out.println("Exception in updateTextLog" + e.getMessage());

                                }
                }
                
                public String executionTimeComputation(String startTime,String EndTime){
                                
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date start = null;
                                Date end = null;
                                String exeTime = null;
                                
                                 try {
                                                start = format.parse(startTime);
                                                end = format.parse(EndTime);
                                                long timeDiff = end.getTime() - start.getTime();
                                                long diffSeconds = timeDiff / 1000 % 60;
                                                long diffMinutes = timeDiff / (60 * 1000) % 60;
                                                long diffHours = timeDiff / (60 * 60 * 1000);
                                                exeTime = String.format("%d:%d:%d",diffHours,diffMinutes,diffSeconds);
                                                //  http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=979
                                } catch (ParseException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                }
                                return exeTime;
                }
                
                
                public static void process(String s, int sNO, String steps,
                                                String errorMessage, String StackTraceMessage, String screenshot)
                                                throws SAXException {
                                try{
                                
                                s = sNO + "|" + s;
                                String[] elements = s.split("\\|");

                                atts.clear();
                                th.startElement("", "", "Testcase", atts);
                                try{
                                                th.startElement("", "", "SNo", atts);
                                                th.characters(elements[0].toCharArray(), 0, elements[0].length());
                                                th.endElement("", "", "SNo");     
                                }
                                catch(Exception e){
                                                System.out.println("inside Testcase "+e.getMessage());
                                }
                                try{
                                                th.startElement("", "", "Module", atts);
                                                th.characters(elements[1].toCharArray(), 0, elements[1].length());
                                                th.endElement("", "", "Module");
                                }
                                catch(Exception e){
                                                System.out.println("inside module "+e.getMessage());
                                }

                                try{
                                                th.startElement("", "", "testId", atts);
                                                th.characters(elements[2].toCharArray(), 0, elements[2].length());
                                                th.endElement("", "", "testId");
                                }
                                catch(Exception e){
                                                System.out.println("inside testId "+e.getMessage());
                                }
                                try{
                                                th.startElement("", "", "testName", atts);
                                                th.characters(elements[3].toCharArray(), 0, elements[3].length());
                                                th.endElement("", "", "testName");         
                                }
                                catch(Exception e){
                                                System.out.println("inside testName "+e.getMessage());
                                }
                                try{
                                                th.startElement("", "", "duration", atts);
                                                th.characters(elements[4].toCharArray(), 0, elements[4].length());
                                                th.endElement("", "", "duration");
                                }catch(Exception e){
                                                System.out.println("inside duration "+e.getMessage());
                                }
                                
                                try{
                                                th.startElement("", "", "startTime", atts);
                                                th.characters(elements[5].toCharArray(), 0, elements[5].length());
                                                th.endElement("", "", "startTime");
                                }catch(Exception e){
                                                System.out.println("inside startTime "+e.getMessage());
                                }
                                
                                try{
                                                th.startElement("", "", "endTime", atts);
                                                th.characters(elements[6].toCharArray(), 0, elements[6].length());
                                                th.endElement("", "", "endTime");           
                                }catch(Exception e){
                                                System.out.println("inside endTime "+e.getMessage());
                                }
                                
                                try{
                                                if(elements[7]!=null){
                                                                th.startElement("", "", "status", atts);
                                                                th.characters(elements[7].toCharArray(), 0, elements[7].length());
                                                                th.endElement("", "", "status"); 
                                                }               
                                }
                                catch(Exception e){
                                                System.out.println("inside status "+e.getMessage());
                                }
                                
                                try{
                                                if (steps != null) {
                                                                th.startElement("", "", "steps", atts);
                                                                th.characters(steps.toCharArray(), 0, steps.length());
                                                                th.endElement("", "", "steps");
                                                } else {
                                                                th.startElement("", "", "steps", atts);
                                                                th.endElement("", "", "steps");
                                                }
                                }
                                catch(Exception e){
                                                System.out.println("inside steps "+e.getMessage());
                                }
                                try{
                                                if (errorMessage != null) {
                                                                th.startElement("", "", "ErrorMessage", atts);
                                                                th.characters(errorMessage.toCharArray(), 0, errorMessage.length());
                                                                th.endElement("", "", "ErrorMessage");
                                                } else {
                                                                th.startElement("", "", "ErrorMessage", atts);
                                                                th.endElement("", "", "ErrorMessage");
                                                }               
                                }
                                catch(Exception e){
                                                System.out.println("inside ErrorMessage "+e.getMessage());
                                }
                                
                                try{
                                                if (StackTraceMessage != null) {
                                                                th.startElement("", "", "StackTraceMessage", atts);
                                                                th.characters(StackTraceMessage.toCharArray(), 0,
                                                                                                StackTraceMessage.length());
                                                                th.endElement("", "", "StackTraceMessage");
                                                } else {
                                                                th.startElement("", "", "StackTraceMessage", atts);
                                                                th.endElement("", "", "StackTraceMessage");
                                                }               
                                }
                                catch(Exception e){
                                                System.out.println("inside StackTraceMessage "+e.getMessage());
                                }
                                try{
                                                if (screenshot != null) {
                                                                th.startElement("", "", "Screenshot", atts);
                                                                th.characters(screenshot.toCharArray(), 0, screenshot.length());
                                                                th.endElement("", "", "Screenshot");
                                                } else {
                                                                th.startElement("", "", "Screenshot", atts);
                                                                th.endElement("", "", "Screenshot");
                                                }               
                                }
                                catch(Exception e){
                                                System.out.println("inside Screenshot "+e.getMessage());
                                }

                                th.endElement("", "", "Testcase");
                                
                                }catch(Exception e){
                                                System.out.println("Exception in process "+e);
                                }
                }

                public static void ResultSummary(String total, String executionDuration,
                                                String passed, String failed, String notExecuted, String startTime, String endTime)
                                                throws SAXException {

                                atts.clear();
                                th.startElement("", "", "ResultSummary", atts);
                                try{
                                                th.startElement("", "", "Total", atts);
                                                th.characters(total.toCharArray(), 0, total.length());
                                                th.endElement("", "", "Total");   
                                }catch(Exception e){
                                                System.out.println("inside Total "+e.getMessage());
                                }
                                
                                
       try {
                                                th.startElement("", "", "Executed", atts);
                                                th.characters(executed.toCharArray(), 0, executed.length());
                                                th.endElement("", "", "Executed");
                                } catch (Exception e) {
                                                // TODO Auto-generated catch block
                                                System.out.println("inside Executed "+e.getMessage());
                                                e.printStackTrace();
                                }

                                try {
                                                th.startElement("", "", "ExecutionDuration", atts);
                                                th.characters(executionDuration.toCharArray(), 0, executionDuration.length());
                                                th.endElement("", "", "ExecutionDuration");
                                } catch (Exception e) {
                                                // TODO Auto-generated catch block
                                                System.out.println("inside ExecutionDuration "+e.getMessage());
                                                e.printStackTrace();
                                }
                                
                                try {
                                                th.startElement("", "", "Passed", atts);
                                                th.characters(passed.toCharArray(), 0, passed.length());
                                                th.endElement("", "", "Passed");
                                } catch (Exception e) {
                                                // TODO Auto-generated catch block
                                                System.out.println("inside Passed "+e.getMessage());
                                                e.printStackTrace();
                                }

                                try {
                                                th.startElement("", "", "Failed", atts);
                                                th.characters(failed.toCharArray(), 0, failed.length());
                                                th.endElement("", "", "Failed");
                                } catch (Exception e) {
                                                // TODO Auto-generated catch block
                                                System.out.println("inside Failed "+e.getMessage());
                                                e.printStackTrace();
                                }

                                th.startElement("", "", "Error", atts);
                                th.characters(error.toCharArray(), 0, error.length());
                                th.endElement("", "", "Error");

                                th.startElement("", "", "Timeout", atts);
                                th.characters(timeout.toCharArray(), 0, timeout.length());
                                th.endElement("", "", "Timeout");

                                th.startElement("", "", "Aborted", atts);
                                th.characters(aborted.toCharArray(), 0, aborted.length());
                                th.endElement("", "", "Aborted");

                                th.startElement("", "", "Inconclusive", atts);
                                th.characters(inconclusive.toCharArray(), 0, inconclusive.length());
                                th.endElement("", "", "Inconclusive");

                                th.startElement("", "", "PassedButRunAborted", atts);
                                th.characters(passedButRunAborted.toCharArray(), 0,
                                                                passedButRunAborted.length());
                                th.endElement("", "", "PassedButRunAborted");

                                th.startElement("", "", "NotRunnable", atts);
                                th.characters(notRunnable.toCharArray(), 0, notRunnable.length());
                                th.endElement("", "", "NotRunnable"); 

                                th.startElement("", "", "Disconnected", atts);
                                th.characters(disconnected.toCharArray(), 0, disconnected.length());
                                th.endElement("", "", "Disconnected");

                                th.startElement("", "", "Warning", atts);
                                th.characters(warning.toCharArray(), 0, warning.length());
                                th.endElement("", "", "Warning");

                                th.startElement("", "", "Completed", atts);
                                th.characters(completed.toCharArray(), 0, completed.length());
                                th.endElement("", "", "Completed");

                                th.startElement("", "", "InProgress", atts);
                                th.characters(inProgress.toCharArray(), 0, inProgress.length());
                                th.endElement("", "", "InProgress");

                                th.startElement("", "", "Pending", atts);
                                th.characters(pending.toCharArray(), 0, pending.length());
                                th.endElement("", "", "Pending");
                                
                                try {
                                                th.startElement("", "", "NotExecuted", atts);
                                                th.characters(notExecuted.toCharArray(), 0, notExecuted.length());
                                                th.endElement("", "", "NotExecuted");
                                } catch (Exception e) {
                                                // TODO Auto-generated catch block
                                                System.out.println("inside NotExecuted "+e.getMessage());
                                                e.printStackTrace();
                                }

                                

                                try {
                                                th.startElement("", "", "startTime", atts);
                                                th.characters(startTime.toCharArray(), 0, startTime.length());
                                                th.endElement("", "", "startTime");
                                } catch (Exception e) {
                                                // TODO Auto-generated catch block
                                                System.out.println("inside startTime "+e.getMessage());
                                                e.printStackTrace();
                                }

                                try {
                                                th.startElement("", "", "endTime", atts);
                                                th.characters(endTime.toCharArray(), 0, endTime.length());
                                                th.endElement("", "", "endTime");
                                } catch (Exception e) {
                                                // TODO Auto-generated catch block
                                                System.out.println("inside endTime "+e.getMessage());
                                                e.printStackTrace();
                                }

                                th.endElement("", "", "ResultSummary");
                }

                public static void closeXML() throws SAXException {
                                th.endElement("", "", "TestRun");
                                th.endDocument();
                }

                public void putValuesToDB(String outputxmlFile) {
                                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                                factory.setNamespaceAware(true);
                                DocumentBuilder builder;
                                Document doc = null;

                                try {
                                                builder = factory.newDocumentBuilder();
                                                doc = builder.parse(outputxmlFile);

                                                // Create XPathFactory object
                                                XPathFactory xpathFactory = XPathFactory.newInstance();

                                                // Create XPath object
                                                XPath xpath = xpathFactory.newXPath();

                                                // get rundetails value
                                                String exeID = getStringValue(doc, xpath, "//ExecutionID");

                                                // System.out.println("exeID --->" + exeID);
                                                String exeName = getStringValue(doc, xpath,"//ExecutionName");

                                                int tcCount = getCount(doc, xpath, "//Testcase");
                                                // System.out.println("tcCount --->" + tcCount);
                                                // get test details
                                                if (tcCount != 0) {
                                                                for (int i = 1; i <= tcCount; i++) {
                                                                                LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                                                                                // System.out.println("--->"+ getStringValue(doc, xpath,
                                                                                // "//Testcase[" + i + "]/Module"));
                                                                                map.put("moduleName" + i, getStringValue(doc, xpath, "//Testcase[" + i+ "]/Module"));
                                                                                map.put("tcid" + i, getStringValue(doc, xpath, "//Testcase[" + i + "]/testId"));
                                                                                map.put("tcname" + i,getStringValue(doc, xpath, "//Testcase[" + i + "]/testName"));
                                                                                String tcNameVal =getStringValue(doc, xpath, "//Testcase[" + i + "]/testName");
                                                                                map.put("duration" + i,getStringValue(doc, xpath, "//Testcase[" + i+ "]/duration"));
                                                                                map.put("startTime" + i,getStringValue(doc, xpath, "//Testcase[" + i + "]/startTime"));
                                                                                map.put("endTime" + i,getStringValue(doc, xpath, "//Testcase[" + i + "]/endTime"));
                                                                                map.put("status" + i,getStringValue(doc, xpath, "//Testcase[" + i + "]/status"));
                                                                                map.put("steps" + i,getStringValue(doc, xpath, "//Testcase[" + i + "]/steps"));
                                                                                map.put("ErrorInfo" + i, getStringValue(doc, xpath, "//Testcase[" + i + "]/ErrorMessage"));
                                                                                String errorMes = getStringValue(doc, xpath, "//Testcase[" + i + "]/ErrorMessage");
                                                                                if(errorMes!="NA")
                                                                                map.put("bugType"+i ,"Not Classified" );                 //   getBugType(errorMes,tcNameVal));
                                                                                map.put("Exception" + i,getStringValue(doc, xpath, "//Testcase[" + i + "]/StackTraceMessage"));
                                                                                map.put("screenshotpath" + i, getStringValue(doc, xpath, "//Testcase[" + i+ "]/Screenshot"));

                                                                                
                                                                                
                                                                                insertTestDetails(exeID, map, tcCount);
                                                                }
                                                }

                                                // get run details
                                                LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                                                map.put("runID", exeID);
                                                map.put("runName", exeName);
                                                map.put("startTime",getStringValue(doc, xpath, "//ResultSummary/startTime"));
                                                map.put("endTime",getStringValue(doc, xpath, "//ResultSummary/endTime"));
                                                map.put("duration", getStringValue(doc, xpath,"//ResultSummary/ExecutionDuration"));
                                                map.put("total",getStringValue(doc, xpath, "//ResultSummary/Total"));
                                                map.put("passed",getStringValue(doc, xpath, "//ResultSummary/Passed"));
                                                map.put("failed",getStringValue(doc, xpath, "//ResultSummary/Failed"));
                                                map.put("notexecuted",getStringValue(doc, xpath, "//ResultSummary/NotExecuted"));

                                                insertRunDetails(map);

                                //             System.out.println(":::XML Value Persisted in DB:::");

                                } catch (Exception e) {
                                                System.out.println("putValuesToDB"+e.getMessage());
                                                e.printStackTrace();
                                }

                }
                
                private static String getBugType(String errorMes, String testName) throws SQLException {
                                // TODO Auto-generated method stub
                                String errorfromDB = "";
                                String bugType = "";
                                
                                conn = getConnection();
                                try {
                                                
                                                //checks known bug details table for exisiting defects
                                                ps = conn.prepareStatement("select errorinfo, bugtype from reportportal.knownbugdetails where tcname =?");
                                                ps.setString(1, testName);
                                                ResultSet resultSet = ps.executeQuery();
                                                
                                                while (resultSet.next()) {
                                                                errorfromDB=resultSet.getString("errorinfo");
                                                                if(errorMes.equals(errorfromDB)){
                                                                bugType =resultSet.getString("bugtype");
                                                                
                                                                return bugType;
                                                                }
                                                }
                                                
                                                //checks history of runs to classify bugtype on testdetails
                                                ps = conn.prepareStatement("select errorinfo, bugtype from reportportal.testdetails where tcname =?  order by starttime desc LIMIT ?");
                                                ps.setString(1, testName);
                                                
                                                //change this to property file - History 
                                                ps.setInt(2, 10);
                                                
                                                ResultSet resultSet1 = ps.executeQuery();
                                
                                                while (resultSet1.next()) {
                                                                errorfromDB=resultSet1.getString("errorinfo");
                                                                if(errorMes.equals(errorfromDB)){
                                                                bugType =resultSet1.getString("bugtype");
                                                                return bugType;
                                                                }
                                                }
                                                
                                
                                                
                                                //finally checks masterbugdetails to classify bugtype
                                                ps = conn.prepareStatement("select  errorinfo from reportportal.masterbugdetails");
                                                ResultSet resultSet2 = ps.executeQuery();
                                                
                                                while (resultSet2.next()) {
                                                                errorfromDB = resultSet2.getString("errorinfo");
                                                                if(errorMes.contains(errorfromDB)){
                                                                                //System.out.println("Match found" + errorfromDB);
                                                                                ps = conn.prepareStatement("select  bugclassification from reportportal.masterbugdetails where errorinfo = ?");
                                                                                ps.setString(1, errorfromDB);
                                                                                ResultSet resultSet3 = ps.executeQuery();
                                                                                while (resultSet3.next()) {
                                                                                                bugType =resultSet3.getString("bugclassification");
                                                                                //             System.out.println("bugType -- returning" + bugType);
                                                                                                return bugType;
                                                                                }
                                                                                
                                                                }
                                                }
                                                //if no matching value found in master bug classification table return Yet to be analysed
                                                bugType = "Yet to be analysed";
                                                
                                                
                                
                                } catch (SQLException e) {
                                                // TODO Auto-generated catch block
                                                System.out.println("getBugType"+e.getMessage());
                                                e.printStackTrace();
                                                throw e;
                                }
                                
                                return bugType;
                }

                
                public void bugClassification(){
                                
                                conn = getConnection();
                                try {
                                ps = conn.prepareStatement("select tcid,tcname,errorinfo,bugtype from reportportal.testdetails where bugtype ='Not Classified'");
                                ResultSet resultSet = ps.executeQuery();
                                String errorfromDB,tcname,tcid;
                                int tcCount=0;
                                LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                                while (resultSet.next()) {
                                                errorfromDB=resultSet.getString("errorinfo");
                                                tcname = resultSet.getString("tcname");
                                                tcid = resultSet.getString("tcid");
                                                String bugtype = bugClassification(errorfromDB,tcname);
                                                System.out.println(bugtype);
                                                map.put("tcid"+tcCount, tcid);
                                                map.put("bugType"+tcCount,bugtype);   
                                                insertBugDetails(tcid,bugtype);
                                                
                                                tcCount++;
                                                //resultSet.UPDATABLE("bugtype", bugtype);
                                }
                                
                                
                                } catch (SQLException e) {
                                                // TODO Auto-generated catch block
                                                System.out.println("getBugType"+e.getMessage());
                                                e.printStackTrace();
                                }
                }
                
                
                
                
                private static String bugClassification(String errorMes, String testName) throws SQLException {
                                // TODO Auto-generated method stub
                                String errorfromDB = "";
                                String bugType = "";
                                
                                conn = getConnection();
                                try {
                                                //checks known bug details table for exisiting defects testdetails
                                                ps = conn.prepareStatement("select errorinfo, bugtype from reportportal.knownbugdetails where tcname =?");
                                                ps.setString(1, testName);
                                                ResultSet resultSet = ps.executeQuery();
                                                
                                                while (resultSet.next()) {
                                                                errorfromDB=resultSet.getString("errorinfo");
                                                                System.out.println(errorfromDB);
                                                                if(errorMes.equals(errorfromDB)){
                                                                bugType =resultSet.getString("bugtype");
                                                                if(!bugType.equals("Not Classified"))
                                                                return bugType;
                                                                }
                                                }
                                                
                                                //checks history of runs to classify bugtype on testdetails
                                                ps = conn.prepareStatement("select errorinfo, bugtype from reportportal.testdetails where tcname =?  order by starttime desc LIMIT ?");
                                                ps.setString(1, testName);
                                                
                                                //change this to property file - History 
                                                ps.setInt(2, 10);
                                                
                                                ResultSet resultSet1 = ps.executeQuery();
                                
                                                while (resultSet1.next()) {
                                                                errorfromDB=resultSet1.getString("errorinfo");
                                                                if(errorMes.equals(errorfromDB)){
                                                                bugType =resultSet1.getString("bugtype");
                                                                if(!bugType.equals("Not Classified"))
                                                                return bugType;
                                                                }
                                                }
                                                
                                                //finally checks masterbugdetails to classify bugtype
                                                ps = conn.prepareStatement("select  errorinfo from reportportal.masterbugdetails");
                                                ResultSet resultSet2 = ps.executeQuery();
                                                
                                                while (resultSet2.next()) {
                                                                errorfromDB = resultSet2.getString("errorinfo");
                                                                if(errorMes.contains(errorfromDB)){
                                                                                //System.out.println("Match found" + errorfromDB);
                                                                                ps = conn.prepareStatement("select  bugclassification from reportportal.masterbugdetails where errorinfo = ?");
                                                                                ps.setString(1, errorfromDB);
                                                                                ResultSet resultSet3 = ps.executeQuery();
                                                                                while (resultSet3.next()) {
                                                                                                bugType =resultSet3.getString("bugclassification");
                                                                                //             System.out.println("bugType -- returning" + bugType);
                                                                                                if(!bugType.equals("Not Classified"))
                                                                                                return bugType;
                                                                                }
                                                                                
                                                                }
                                                }
                                                //if no matching value found in master bug classification table return Yet to be analysed
                                                bugType = "Yet to be analysed";
                                                
                                } catch (SQLException e) {
                                                // TODO Auto-generated catch block
                                                System.out.println("getBugType"+e.getMessage());
                                                e.printStackTrace();
                                                throw e;
                                }
                                
                                return bugType;
                }
                
                
                private static void insertRunDetails(LinkedHashMap<String, String> testMap) {
                                // TODO Auto-generated method stub
                                try {
                                                conn = getConnection();

                                                ps = conn.prepareStatement("insert into reportportal.rundetails (runid, runname, total, notexecuted, passed, failed, starttime, endtime, duration) values (?,?,?,?,?,?,?,?,?)");

                                                for (Map.Entry<String, String> m : testMap.entrySet()) {
                                                                
                                                                if (m.getKey().equals("runID")) {
                                                                //             System.out.println("Enter into db"+ m.getValue());
                                                                                ps.setString(1, m.getValue());
                                                                }
                                                                if (m.getKey().equals("runName")) {
                                                                //             System.out.println("Enter into db"+ m.getValue());
                                                                                ps.setString(2, m.getValue());
                                                                }
                                                                if (m.getKey().equals("total")) {
                                                                //             System.out.println("Enter into db"+ m.getValue());
                                                                                ps.setString(3, m.getValue());
                                                                }
                                                                if (m.getKey().equals("notexecuted")) {
                                                                //             System.out.println("Enter into db"+ m.getValue());
                                                                                ps.setString(4, m.getValue());
                                                                }
                                                                if (m.getKey().equals("passed")) {
                                                                //             System.out.println("Enter into db"+ m.getValue());
                                                                                ps.setString(5, m.getValue());
                                                                }
                                                                if (m.getKey().equals("failed")) {
                                                                //             System.out.println("Enter into db"+ m.getValue());
                                                                                ps.setString(6, m.getValue());
                                                                }
                                                                if (m.getKey().equals("startTime")) {
                                                                //             System.out.println("Enter into db"+ m.getValue());
                                                                                ps.setString(7, m.getValue());
                                                                }
                                                                if (m.getKey().equals("endTime")) {
                                                                //             System.out.println("Enter into db"+ m.getValue());
                                                                                ps.setString(8, m.getValue());
                                                                }
                                                                if (m.getKey().equals("duration")) {
                                                                //             System.out.println("Enter into db"+ m.getValue());
                                                                                ps.setString(9, m.getValue());
                                                                }

                                                }
                                                ps.executeUpdate();
                                } catch (Exception e) {
                                                System.out.println("insertRunDetails"+e.getMessage());
                                                e.printStackTrace();
                                }

                }

                private static int getCount(Document doc, XPath xpath, String xpathVal) {
                                // TODO Auto-generated method stub
                                int count = 0;
                                try {
                                                // create XPathExpression object
                                                XPathExpression expr = xpath.compile(xpathVal);
                                                // evaluate expression result on XML document
                                                NodeList nodes = (NodeList) expr.evaluate(doc,
                                                                                XPathConstants.NODESET);
                                                count = nodes.getLength();
                                                return count;
                                } catch (Exception e) {
                                                return 0;
                                }

                }

                public static String getStringValue(Document doc, XPath xpath,
                                                String xpathLoc) {
                                String val = null;
                                try {
                                                // "/Employees/Employee/name/text()"
                                                XPathExpression expr = xpath.compile(xpathLoc);
                                                val = (String) expr.evaluate(doc, XPathConstants.STRING);
                                } catch (XPathExpressionException e) {
                                                System.out.println("getStringValue"+e.getMessage());
                                                e.printStackTrace();
                                }

                                return val;
                }

                private static List<String> getListOfTestCase(Document doc, XPath xpath) {
                                List<String> list = new ArrayList<String>();
                                try {
                                                // create XPathExpression object
                                                XPathExpression expr = xpath
                                                                                .compile("/Employees/Employee[gender='Female']/name/text()");
                                                // evaluate expression result on XML document
                                                NodeList nodes = (NodeList) expr.evaluate(doc,
                                                                                XPathConstants.NODESET);
                                                for (int i = 0; i < nodes.getLength(); i++)
                                                                list.add(nodes.item(i).getNodeValue());
                                } catch (XPathExpressionException e) {
                                                e.printStackTrace();
                                }
                                return list;
                }

                public static Connection getConnection() {

                                try {
                                                Class.forName(JDBC_DRIVER).newInstance();
                                                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                                } catch (Exception e) {
                                                System.out.println("getConnection"+e.getMessage());
                                                e.printStackTrace();
                                }
                                return conn;

                }

                public static void insertTestDetails(String exeID,
                                                LinkedHashMap<String, String> runMap, int tcCount) {

                                try {
                                                conn = getConnection();

                                                ps = conn
                                                                                .prepareStatement("insert into reportportal.testdetails (runid,tcid,tcname,duration,starttime,endtime,tcstatus,steps,errorinfo,exception,sspath,modulename,screenshot,bugtype) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

                                                for (Map.Entry<String, String> m : runMap.entrySet()) {
                                                                // System.out.println(m.getKey() + " " + m.getValue());
                                                                for (int i = 1; i <= tcCount; i++) {

                                                                                ps.setString(1, exeID);

                                                                                if (m.getKey().equals("tcid" + i)) {
                                                                                                // System.out.println("Enter into db"+ m.getValue());
                                                                                                ps.setString(2, m.getValue());
                                                                                }
                                                                                if (m.getKey().equals("tcname" + i)) {
                                                                                                // System.out.println("Enter into db"+ m.getValue());
                                                                                                ps.setString(3, m.getValue());
                                                                                }
                                                                                if (m.getKey().equals("duration" + i)) {
                                                                                                // System.out.println("Enter into db"+ m.getValue());
                                                                                                ps.setString(4, m.getValue());
                                                                                }
                                                                                if (m.getKey().equals("startTime" + i)) {
                                                                                                // System.out.println("Enter into db"+ m.getValue());
                                                                                                ps.setString(5, m.getValue());
                                                                                }
                                                                                if (m.getKey().equals("endTime" + i)) {
                                                                                                // System.out.println("Enter into db"+ m.getValue());
                                                                                                ps.setString(6, m.getValue());
                                                                                }
                                                                                if (m.getKey().equals("status" + i)) {
                                                                                                // System.out.println("Enter into db"+ m.getValue());
                                                                                                ps.setString(7, m.getValue());
                                                                                }
                                                                                if (m.getKey().equals("steps" + i)) {
                                                                                                // System.out.println("Enter steps into db"+
                                                                                                // m.getValue());
                                                                                                ps.setString(8, m.getValue());
                                                                                }
                                                                                if (m.getKey().equals("ErrorInfo" + i)) {
                                                                                                // System.out.println("Enter into db"+ m.getValue());
                                                                                                ps.setString(9, m.getValue());
                                                                                }
                                                                                if (m.getKey().equals("Exception" + i)) {
                                                                                                // System.out.println("Enter into db"+ m.getValue());
                                                                                                ps.setString(10, m.getValue());
                                                                                }
                                                                                if (m.getKey().equals("screenshotpath" + i)) {
                                                                                                // System.out.println("Enter into db"+ m.getValue());
                                                                                                ps.setString(11, m.getValue());
                                                                                                if(!m.getValue().equals("NA")){
                                                                                                                System.out.println("came"+m.getValue());
                                                                                                                File imgfile = new File(m.getValue());
                                                                                                                if(imgfile!=null){
                                                                                                                                FileInputStream fin = new FileInputStream(imgfile);
                                                                                                                                ps.setBinaryStream(13,(InputStream)fin,(int)imgfile.length()); 
                                                                                                                 }
                                                                                                }
                                                                                                else
                                                                                                                ps.setBinaryStream(13,null,0);
                                                                                }
                                                                                if (m.getKey().equals("moduleName" + i)) {
                                                                                                // System.out.println("Enter moduleName into db"+
                                                                                                // m.getValue());
                                                                                                ps.setString(12, m.getValue());
                                                                                }
                                                                                
                                                                                
                                                                                if(m.getKey().equals("bugType"+i)){
                                                                                                //             System.out.println(" b4 isnert"+m.getValue());
                                                                                                                ps.setString(14, m.getValue()); 
                                                                                }

                                                                }
                                                }

                                                ps.executeUpdate();
                                } catch (Exception e) {
                                                System.out.println("insertTestDetails"+e.getMessage());
                                                e.printStackTrace();
                                }
                }
                public static void insertBugDetails(String tcid,String bugtype) {

                                try {
                                                conn = getConnection();

                                                ps = conn.prepareStatement("update reportportal.testdetails SET bugtype=? where tcid=?");
                                                ps.setString(1, bugtype);
                                                ps.setString(2, tcid);
                                                ps.executeUpdate();
                                }catch (Exception e) {
                                                System.out.println("insertTestDetails"+e.getMessage());
                                                e.printStackTrace();
                                }
                }
                
                
                public void xmlFormatter(String sourceXMLFilePath, String textFile,
                                                String outputxmlFile,String jobName) throws TransformerConfigurationException,
                                                ParserConfigurationException, SAXException, IOException {

                                try {
                                String testId,testName, duration,
                                startTime,endTime,status,moduleName,executionID,executionStartTime,executionEndTime,executionDuration;
                                String total = null,passed= null,failed= null,notExecuted= null;
                                
                                File fXmlFile = new File(sourceXMLFilePath);
                                
                                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                                Document doc = dBuilder.parse(fXmlFile);
                                doc.getDocumentElement().normalize();
                                
                                
                                 int tcCount = getTestCaseCount(doc,"TestCaseCount");
                                executionID = getStringAttribute(doc,"ExecutionID",0);
                                
                                 String[] steps=new String[tcCount];
                                String[] errorMessage=new String[tcCount];
                                String[] StackTraceMessage=new String[tcCount];
                                String[] screenshotpath=new String[tcCount];
                                
                                 
                                  for (int current_tc = 0; current_tc < tcCount; current_tc++) {
                                                
                                                moduleName = getStringAttribute(doc,"Module",current_tc);
                                                                                
                                                testId = getStringAttribute(doc,"tc_id",current_tc);
                                                                                
                                                testName = getStringAttribute(doc,"tc_name",current_tc);
                                                
                                                startTime = gettime(doc,"starttime",current_tc);
                                                
                                                endTime  =         gettime(doc,"endtime",current_tc);
                                                
                                                duration = executionTimeComputation(startTime,endTime);
                                                
                                                status  = getStringAttribute(doc,"tc_status",current_tc);
                                                                
                                                updateTextLog(textFile,moduleName,testId,testName,duration,startTime,endTime,status);
                                                
                                                steps[current_tc] = getStringAttribute(doc,"steps",current_tc);
                                                                                
                                                errorMessage[current_tc] = getStringAttribute(doc,"ErrorMessage",current_tc);
                                                                                
                                                StackTraceMessage[current_tc] = getStringAttribute(doc,"StackTraceMessage",current_tc);
                                                                                
                                                screenshotpath[current_tc] = getStringAttribute(doc,"Screenshot",current_tc);
                                                                                
                                                }
                                  
                                                total = getStringAttribute(doc,"Total",0);
                                                
                                                passed = getStringAttribute(doc,"Passed",0);
                                                
                                                failed = getStringAttribute(doc,"Failed",0);
                                                
                                                notExecuted = getStringAttribute(doc,"NotExecuted",0);
                                                
                                                executionStartTime =gettime(doc, "exeStarttime", 0);
                                                
                                                executionEndTime =gettime(doc, "exeEndtime", 0);
                                                
                                                executionDuration = executionTimeComputation(executionStartTime,executionEndTime);                
                                  
                                converstionToXml(textFile,outputxmlFile,executionID,steps,errorMessage,StackTraceMessage,screenshotpath,total,passed,failed,notExecuted,executionStartTime,executionEndTime,executionDuration,jobName);
                                
                                                
                                } catch (Exception e) {
                                     System.out.println(e.getMessage()+" in xmlFormatter");
                                   e.printStackTrace();
                                }

                }               
                
                
                public String getStringAttribute(Document doc,String attribute,int testcaseNo){
                                String StrAttribute="NA";
                                try{
                                String attributePresence=dataTable.getData("Attribute_Data", attribute, "Attribute_Presence");
                                if(attributePresence.equals("Yes")){
                                                String Selector=dataTable.getData("Attribute_Data", attribute, "Selector");
                                                switch(Selector){
                                                                case "ParentTagName_ParentAttribute":{
                                                                                StrAttribute = ParentTagName_ParentAttribute(doc,attribute,testcaseNo);
                                                                                break;
                                                                }
                                                                case "TagName_Attribute":{
                                                                                StrAttribute = TagName_Attribute(doc,attribute,testcaseNo);
                                                                                break;
                                                                }
                                                                case "TagName_getText":{
                                                                                StrAttribute = TagName_getText(doc,attribute,testcaseNo);
                                                                                break;
                                                                }
                                    }
                                }
                                else
                                                StrAttribute="NA";
                                
                                
                                }catch (IOException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                }
                                return StrAttribute;
                }
                
                public String gettime(Document doc,String Attribute,int testcaseNo){
                                String time="0000-00-00 00:00:00";
                                try {
                                String methodPresence=dataTable.getData("Attribute_Data", Attribute, "Attribute_Presence");
                                if(methodPresence.equals("Yes")){
                                                String Selector=dataTable.getData("Attribute_Data", Attribute, "Selector");
                                                String DateFormat=dataTable.getData("Attribute_Data", Attribute, "DateFormat");
                                                switch(Selector){
                                                case "ParentTagName_ParentAttribute":{
                                                                
                                                                time = ParentTagName_ParentAttribute(doc,Attribute,testcaseNo);
                                                                time = parseDateToTimeStamp(time,DateFormat);
                                                                break;
                                                }
                                                case "TagName_Attribute":{
                                                                time = TagName_Attribute(doc,Attribute,testcaseNo);
                                                                time = parseDateToTimeStamp(time,DateFormat);
                                                                break;
                                                }
                                  }             
                                
                                }
                                else
                                                time = "0000-00-00 00:00:00";
                                
                                } catch (IOException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                }
                                return time;
                }
                
                
                
                public String TagName_Attribute(Document doc,String attribute,int childno){
                                String attrValue="";
                                try {
                                String TagName=dataTable.getData("Attribute_Data", attribute, "TagName");  
                                String Attribute=dataTable.getData("Attribute_Data", attribute, "Attribute");     
                                NodeList TestRunList = doc.getElementsByTagName(TagName);
                                Node testNode = TestRunList.item(childno);
                                Element testRunElement = (Element) testNode;
                                attrValue = testRunElement.getAttribute(Attribute);
                                }catch (IOException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                }
                                return attrValue;             
                }

                
                 
                public int getTestCaseCount(Document doc,String attribute){
                                int tc_Count=0;
                                try {
                                  String methodPresence=dataTable.getData("Attribute_Data", attribute, "Attribute_Presence");
                                  if(methodPresence.equals("Yes")){
                                                String Selector=dataTable.getData("Attribute_Data", attribute, "Selector");
                                                switch(Selector){
                                                case "TagName":{
                                                                String TagName=dataTable.getData("Attribute_Data", attribute, "TagName");  
                                                                NodeList nList = doc.getElementsByTagName(TagName);
                                                                tc_Count = nList.getLength();
                                                                break;
                                                }
                                  }
                                }
                                else
                                                tc_Count= 0;
                                
                                } catch (IOException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                }
                                return tc_Count;
                                
                }
                
                public String getModule(Document doc,int testcaseNo){
                                String moduleName=null;
                                try {
                                String methodPresence=dataTable.getData("Attribute_Data", "Module", "Attribute_Presence");
                                if(methodPresence.equals("Yes")){
                                                String Selector=dataTable.getData("Attribute_Data", "Module", "Selector");
                                                switch(Selector){
                                                case "ParentTagName_ParentAttribute":{
                                                                moduleName = ParentTagName_ParentAttribute(doc,"Module",testcaseNo);
                                                                break;
                                                }
                                  }             
                                
                                }
                                else
                                                moduleName = "Not Available";
                                
                                } catch (IOException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                }
                                return moduleName;
                                
                }
                
                public String gettestId(Document doc,int testcaseNo){
                                String testId=null;
                                try {
                                String methodPresence=dataTable.getData("Attribute_Data", "tc_id", "Attribute_Presence");
                                if(methodPresence.equals("Yes")){
                                                String Selector=dataTable.getData("Attribute_Data", "tc_id", "Selector");
                                                switch(Selector){
                                                case "ParentTagName_ParentAttribute":{
                                                                testId = ParentTagName_ParentAttribute(doc,"tc_id",testcaseNo);
                                                                break;
                                                }
                                  }             
                                }
                                else
                                                testId = "Not Available";
                                
                                } catch (IOException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                }
                                return testId;
                                
                }
                
                public String gettestName(Document doc,int testcaseNo){
                                String testName=null;
                                try {
                                String methodPresence=dataTable.getData("Attribute_Data", "tc_name", "Attribute_Presence");
                                if(methodPresence.equals("Yes")){
                                                String Selector=dataTable.getData("Attribute_Data", "tc_name", "Selector");
                                                switch(Selector){
                                                case "ParentTagName_ParentAttribute":{
                                                                testName = ParentTagName_ParentAttribute(doc,"tc_name",testcaseNo);
                                                                break;
                                                }
                                  }             
                                
                                }
                                else
                                                testName = "Not Available";
                                
                                } catch (IOException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                }
                                return testName;
                }
                
                public String getstarttime(Document doc,int testcaseNo){
                                String starttime="0000-00-00 00:00:00";
                                try {
                                String methodPresence=dataTable.getData("Attribute_Data", "starttime", "Attribute_Presence");
                                if(methodPresence.equals("Yes")){
                                                String Selector=dataTable.getData("Attribute_Data", "starttime", "Selector");
                                                switch(Selector){
                                                case "ParentTagName_ParentAttribute":{
                                                                String DateFormat=dataTable.getData("Attribute_Data", "starttime", "DateFormat");
                                                                starttime = ParentTagName_ParentAttribute(doc,"starttime",testcaseNo);
                                                                starttime = parseDateToTimeStamp(starttime,DateFormat);
                                                                break;
                                                }
                                  }             
                                
                                }
                                else
                                                starttime = "0000-00-00 00:00:00";
                                
                                } catch (IOException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                }
                                return starttime;
                }
                
                public String getendtime(Document doc,int testcaseNo){
                                String endtime="0000-00-00 00:00:00";
                                try {
                                String methodPresence=dataTable.getData("Attribute_Data", "endtime", "Attribute_Presence");
                                if(methodPresence.equals("Yes")){
                                                String Selector=dataTable.getData("Attribute_Data", "endtime", "Selector");
                                                switch(Selector){
                                                case "ParentTagName_ParentAttribute":{
                                                                String DateFormat=dataTable.getData("Attribute_Data", "endtime", "DateFormat");
                                                                endtime = ParentTagName_ParentAttribute(doc,"endtime",testcaseNo);
                                                                endtime = parseDateToTimeStamp(endtime,DateFormat);
                                                                break;
                                                }
                                  }             
                                
                                }
                                else
                                                endtime = "0000-00-00 00:00:00";
                                
                                } catch (IOException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                }
                                return endtime;
                }
                public String gettc_status(Document doc,int testcaseNo){
                                String tc_status=null;
                                try {
                                String methodPresence=dataTable.getData("Attribute_Data", "tc_status", "Attribute_Presence");
                                if(methodPresence.equals("Yes")){
                                                String Selector=dataTable.getData("Attribute_Data", "tc_status", "Selector");
                                                switch(Selector){
                                                case "ParentTagName_ParentAttribute":{
                                                                tc_status = ParentTagName_ParentAttribute(doc,"tc_status",testcaseNo);
                                                                break;
                                                }
                                  }             
                                }
                                else
                                                tc_status = "Not Available";
                                
                                } catch (IOException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                }
                                return tc_status;
                }
                
                
                public String getstep_status(Document doc,int testcaseNo){
                                String testName=null;
                                try {
                                String methodPresence=dataTable.getData("Attribute_Data", "tc_name", "Attribute_Presence");
                                if(methodPresence.equals("Yes")){
                                                String Selector=dataTable.getData("Attribute_Data", "tc_name", "Selector");
                                                switch(Selector){
                                                case "ParentTagName_ParentAttribute":{
                                                                String ParentTagName=dataTable.getData("Attribute_Data", "tc_name", "ParentTagName");
                                                                String ParentAttribute=dataTable.getData("Attribute_Data", "tc_name", "ParentAttribute");
                                                                testName = ParentTagName_ParentAttribute(doc,"",testcaseNo);
                                                                break;
                                                }
                                  }             
                                }
                                else
                                                testName = "Not Available";
                                
                                } catch (IOException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                }
                                return testName;
                }
                
                public String getsteps(Document doc,int testcaseNo){
                                String steps="NA";
                                try {
                                String methodPresence=dataTable.getData("Attribute_Data", "steps", "Attribute_Presence");
                                if(methodPresence.equals("Yes")){
                                                String Selector=dataTable.getData("Attribute_Data", "steps", "Selector");
                                                switch(Selector){
                                                case "ParentTagName_ParentAttribute":{
                                                                steps = ParentTagName_ParentAttribute(doc,"steps",testcaseNo);
                                                                break;
                                                }
                                                case "TagName_getText":{
                                                                String getText=dataTable.getData("Attribute_Data", "steps", "getText");
                                                                if(getText.equals("Yes")){
                                                                                steps = TagName_getText(doc,"steps",testcaseNo);      
                                                                }
                                                                break;
                                                }
                                  }             
                                }
                                else
                                                steps = "Not Available";
                                
                                } catch (IOException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                }
                                return steps;
                }
                
                public String getErrorMessage(Document doc,int testcaseNo){
                                String steps="NA";
                                try {
                                String methodPresence=dataTable.getData("Attribute_Data", "ErrorMessage", "Attribute_Presence");
                                if(methodPresence.equals("Yes")){
                                                String Selector=dataTable.getData("Attribute_Data", "ErrorMessage", "Selector");
                                                switch(Selector){
                                                case "ParentTagName_ParentAttribute":{
                                                                steps = ParentTagName_ParentAttribute(doc,"ErrorMessage",testcaseNo);
                                                                break;
                                                }
                                                case "TagName_getText":{
                                                                String getText=dataTable.getData("Attribute_Data", "ErrorMessage", "getText");
                                                                if(getText.equals("Yes")){
                                                                                steps = TagName_getText(doc,"steps",testcaseNo);      
                                                                }
                                                                break;
                                                }
                                  }             
                                }
                                else
                                                steps = "Not Available";
                                
                                } catch (IOException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                }
                                return steps;
                }
                
                public String getStackTraceMessage(Document doc,int testcaseNo){
                                String steps="NA";
                                try {
                                String methodPresence=dataTable.getData("Attribute_Data", "StackTraceMessage", "Attribute_Presence");
                                if(methodPresence.equals("Yes")){
                                                String Selector=dataTable.getData("Attribute_Data", "StackTraceMessage", "Selector");
                                                switch(Selector){
                                                case "ParentTagName_ParentAttribute":{
                                                                steps = ParentTagName_ParentAttribute(doc,"StackTraceMessage",testcaseNo);
                                                                break;
                                                }
                                                case "TagName_getText":{
                                                                String getText=dataTable.getData("Attribute_Data", "StackTraceMessage", "getText");
                                                                if(getText.equals("Yes")){
                                                                                steps = TagName_getText(doc,"StackTraceMessage",testcaseNo);       
                                                                }
                                                                break;
                                                }
                                  }             
                                }
                                else
                                                steps = "Not Available";
                                
                                } catch (IOException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                }
                                return steps;
                }
                
                
                public String getscreenshotpath(Document doc,int testcaseNo){
                                String screenshotpath="./NoImageFound.png";
                                try {
                                String methodPresence=dataTable.getData("Attribute_Data", "Screenshot", "Attribute_Presence");
                                if(methodPresence.equals("Yes")){
                                                String Selector=dataTable.getData("Attribute_Data", "Screenshot", "Selector");
                                                switch(Selector){
                                                
                                                case "ParentTagName_ParentAttribute":{
                                                                screenshotpath = ParentTagName_ParentAttribute(doc,"Screenshot",testcaseNo);
                                                                break;
                                                }
                                                case "TagName_getText":{
                                                                String getText=dataTable.getData("Attribute_Data", "Screenshot", "getText");
                                                                if(getText.equals("Yes")){
                                                                                screenshotpath = TagName_getText(doc,"Screenshot",testcaseNo);   
                                                                }
                                                                break;
                                                }
                                  }             
                                }
                                else
                                                screenshotpath = "./NoImageFound.png";
                                
                                } catch (IOException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                }
                                return screenshotpath;
                }
                
                
                
                public String ParentTagName_ParentAttribute(Document doc,String Attribute ,int childNumber){
                String value =null;          
                try {
                String ParentTagName = dataTable.getData("Attribute_Data", Attribute, "ParentTagName");
                String ParentAttribute = dataTable.getData("Attribute_Data", Attribute, "ParentAttribute");
                NodeList TestMethod = doc.getElementsByTagName(ParentTagName);
                Node method = TestMethod.item(childNumber);
                Element methodElement = (Element) method;
                value = methodElement.getAttribute(ParentAttribute);
                } catch (IOException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                }
                return value;
                }
                
                public String TagName_getText(Document doc,String Attribute,int childNumber){
                String value ="NA";        
                String ParentTagName;
                try {
                                ParentTagName = dataTable.getData("Attribute_Data", Attribute, "TagName");
                                NodeList TestMethod = doc.getElementsByTagName(ParentTagName);
                                if(TestMethod!=null)
                                value =  TestMethod.item(childNumber).getTextContent();
                                else
                                value ="NA";     
                } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                }
                return value;
                }
                
                
                
                
                public String parseDateToTimeStamp(String startTime,String format){ 
                                SimpleDateFormat dateFormat = new SimpleDateFormat(format);
                    Date parsedDate;
                    String dateString="";
                    try {
                                                parsedDate = dateFormat.parse(startTime);
                                                Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
                                                dateString = timestamp.toString();
                    } catch (ParseException e1) {
                                                // TODO Auto-generated catch block
                                                e1.printStackTrace();
                                }
                    
                                
                                return dateString;
                }
                
                
                protected Node getNode(String tagName, NodeList nodes) {
                    for ( int x = 0; x < nodes.getLength(); x++ ) {
                        Node node = nodes.item(x);
                        if (node.getNodeName().equalsIgnoreCase(tagName)) {
                            return node;
                        }
                    }
                    return null;
                }
                
                protected String getNodeValue( Node node ) {
                    NodeList childNodes = node.getChildNodes();
                    for (int x = 0; x < childNodes.getLength(); x++ ) {
                        Node data = childNodes.item(x);
                        if (data.getNodeType() == Node.TEXT_NODE)
                            return data.getNodeValue();
                    }
                    return "";
                }
                
                protected String getNodeValue(String tagName, NodeList nodes) {
                    for ( int x = 0; x < nodes.getLength(); x++ ) {
                        Node node = nodes.item(x);
                        if (node.getNodeName().equalsIgnoreCase(tagName)) {
                            NodeList childNodes = node.getChildNodes();
                            for (int y = 0; y < childNodes.getLength(); y++ ) {
                                Node data = childNodes.item(y);
                                if ( data.getNodeType() == Node.TEXT_NODE )
                                    return data.getNodeValue();
                            }
                        }
                    }
                    return "";
                }
                
                protected String getNodeAttr(String attrName, Node node ) {
                    NamedNodeMap attrs = node.getAttributes();
                    for (int y = 0; y < attrs.getLength(); y++ ) {
                        Node attr = attrs.item(y);
                        if (attr.getNodeName().equalsIgnoreCase(attrName)) {
                            return attr.getNodeValue();
                        }
                    }
                    return "";
                }
                
                protected String getNodeAttr(String tagName, String attrName, NodeList nodes ) {
                    for ( int x = 0; x < nodes.getLength(); x++ ) {
                        Node node = nodes.item(x);
                        if (node.getNodeName().equalsIgnoreCase(tagName)) {
                            NodeList childNodes = node.getChildNodes();
                            for (int y = 0; y < childNodes.getLength(); y++ ) {
                                Node data = childNodes.item(y);
                                if ( data.getNodeType() == Node.ATTRIBUTE_NODE ) {
                                    if ( data.getNodeName().equalsIgnoreCase(attrName) )
                                        return data.getNodeValue();
                                }
                            }
                        }
                    }
                
                    return "";
                }
                
                
                
}*/
