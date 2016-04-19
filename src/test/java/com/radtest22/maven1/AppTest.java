package com.radtest22.maven1;
import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import jxl.Workbook;

public class AppTest {
	
  public static enum Mode {
		 ALPHA, ALPHANUMERIC, NUMERIC 
	  }
	  public static String generateRandomString(int length, Mode mode) throws Exception {
  StringBuffer buffer = new StringBuffer();
	  String characters = "";
  switch(mode){	
	  case ALPHA:
	  characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	  break;
	  case ALPHANUMERIC:
	  characters = "abcdefghijklmnopqrstuvwxyz1234567890";
	  break;
	  case NUMERIC:
	  characters = "1234567890";
	  break;
	  }
	  int charactersLength = characters.length();
  for (int i = 0; i < length; i++) {
	  double index = Math.random() * charactersLength;
	  buffer.append(characters.charAt((int) index));
	  }
	  return buffer.toString();
	  }
	private WebDriver driver;
	  private String baseUrl;
	  private StringBuffer verificationErrors = new StringBuffer();
	  
	  @Before
	  public void setUp() throws Exception {
		  driver = new FirefoxDriver();
		    Properties prop = new Properties();
	        FileInputStream ip = new FileInputStream("reference/config.properties");
	        prop.load(ip);
	        String url=prop.getProperty("url");
	        String username=prop.getProperty("username");
	        String password=prop.getProperty("password");	    
	        driver.get(url);
	        baseUrl = url;
	        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	        driver.manage().window().maximize();
		    driver.get(baseUrl + "/");
	        driver.manage().window().maximize();
	        driver.findElement(By.id("wm_login-username")).clear();
	       	driver.findElement(By.id("wm_login-username")).sendKeys(username);
	        driver.findElement(By.id("wm_login-password")).clear();
	        driver.findElement(By.id("wm_login-password")).sendKeys(password);
	        driver.findElement(By.id("submit_login")).click();
	  }
	  
	  @Test
	  public void testJavapo() throws Exception {
		  //System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.Jdk14Logger");
		    FileInputStream fsIP= new FileInputStream(new File("reference/Input/To Create EO.xls")); 
		    HSSFWorkbook wb = new HSSFWorkbook(fsIP); 
	        HSSFSheet worksheet = wb.getSheetAt(0); 
	        Cell cell = null; 
	        cell = worksheet.getRow(1).getCell(15);
	        String randnum1 =	generateRandomString(5,Mode.NUMERIC);
	        String s="Rad-"+randnum1;
	        cell.setCellValue(s);  
	        fsIP.close();    
	        FileOutputStream output_file =new FileOutputStream(new File("reference/Input/To Create EO.xls"));  
	        wb.write(output_file); 
	        output_file.close();
	        File src=new File("reference/Input/To Create EO.xls");
	        Workbook wb1=Workbook.getWorkbook(src);
		    String data00 =wb1.getSheet(0).getCell(15, 1).getContents();
		    try{
		    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		    Document doc = docBuilder.parse(new File("reference/Input/To Create Eo.xml"));
		    NodeList nodes1 =  doc.getElementsByTagName("aex:AEX_ServiceOrder");
		    for(int j=0;j<((NodeList) nodes1).getLength();j++){
		    Node nodes = doc.getElementsByTagName("aex:OrderDetail").item(j);
		    NodeList list = nodes.getChildNodes();
		    for (int i = 0; i != list.getLength(); ++i){
		    Node child = list.item(i);
		    if (child.getNodeName().equals("aex:ServiceOrderNumber")){
		    child.getFirstChild().setNodeValue(data00) ;
		    System.out.println("data is "+data00);
		    }
	        }
	        }
		    TransformerFactory transformerFactory = TransformerFactory.newInstance();
		    Transformer transformer = transformerFactory.newTransformer();
		    DOMSource source = new DOMSource(doc);
		    StreamResult result = new StreamResult("reference/Input/To Create Eo.xml");
		    transformer.transform(source, result);
		    }
		    catch (Exception e){
		    e.printStackTrace();
		    }
	        String TestFile = "reference/Input/To Create Eo.xml";
		    File FC = new File(TestFile);
		    FC.createNewFile();
		    FileReader FR = new FileReader(TestFile);
		    BufferedReader BR = new BufferedReader(FR);
		    StringBuffer fileContents = new StringBuffer();
		    String line = BR.readLine();
		    while (line != null) {
		    fileContents.append(line);
		    line = BR.readLine();
		    }
		    BR.close();
		    driver.findElement(By.id("wmp4981:__rowu_002f_metau_002f_defaultu_002f_wm_xt_fabricfolderu_002f_0000005449:hotspot")).click();
		    driver.findElement(By.id("jsfwmp5565:defaultForm:htmlInputTextarea")).clear();
		    ((RemoteWebDriver) driver).executeScript("var t = document.getElementById('jsfwmp5565:defaultForm:htmlInputTextarea'); "+"t.value = arguments[0];",fileContents.toString());
		    Thread.sleep(3000);
		    driver.findElement(By.id("jsfwmp5565:defaultForm:htmlCommandButton")).click();
	        Thread.sleep(3000);
	        driver.findElement(By.id("wmp4981:__rowu_002f_metau_002f_defaultu_002f_wm_xt_fabricfolderu_002f_0000005393:hotspot")).click();
	        Thread.sleep(3000);
	        Select select = new Select(driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/div[3]/div/div/div/div[3]/div/div[2]/div[6]/div/div/form[1]/div[3]/div/div/div/div[2]/div/div[1]/fieldset[1]/div/div[2]/span/ol/li[2]/span/div/div[1]/select")));
	        select.selectByVisibleText("Document ID");
	        Thread.sleep(3000);
	        Select select1 = new Select(driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/div[3]/div/div/div/div[3]/div/div[2]/div[6]/div/div/form[1]/div[3]/div/div/div/div[2]/div/div[1]/fieldset[1]/div/div[2]/span/ol/li[2]/span/div/div[2]/span/span/span/div/div[1]/select")));
	        select1.selectByVisibleText("Equals");
	        Thread.sleep(3000);
	        driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/div[3]/div/div/div/div[3]/div/div[2]/div[6]/div/div/form[1]/div[3]/div/div/div/div[2]/div/div[1]/fieldset[1]/div/div[2]/span/ol/li[2]/span/div/div[2]/span/span/span/div/div[2]/input")).clear();
	        driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/div[3]/div/div/div/div[3]/div/div[2]/div[6]/div/div/form[1]/div[3]/div/div/div/div[2]/div/div[1]/fieldset[1]/div/div[2]/span/ol/li[2]/span/div/div[2]/span/span/span/div/div[2]/input")).sendKeys(data00);
	        driver.findElement(By.id("jsfwmp5465:searchBarForm:searchBarControl:refinedSearchGoButton")).click();
	        Thread.sleep(3000);
	        WebElement we_table = driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/div[3]/div/div/div/div[4]/div/div[2]/div[6]/div/div/form[1]/div[2]/div/table/tbody"));
			List<WebElement> a1 = we_table.findElements(By.tagName("tr"));
			for (int i = 1; i <= a1.size(); i++) {
			FileInputStream fsIP1= new FileInputStream(new File("reference/Output/Result.xls")); 
			@SuppressWarnings("resource")
			HSSFWorkbook wb2 = new HSSFWorkbook(fsIP1); 
		    HSSFSheet worksheet1 = wb2.getSheetAt(0); 
		    Cell cell1 = null; 
		    Thread.sleep(3000);
		    String text = driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/div[3]/div/div/div/div[4]/div/div[2]/div[6]/div/div/form[1]/div[2]/div/table/tbody/tr["+i+"]")).getText();
		    if(text.contains("AEX_ServiceOrder_Component" ) && text.contains("DONE" ) && !text.contains("DONE W/ ERRORS" ) && text.contains(data00)){
			cell1 = worksheet1.getRow(1).getCell(1);   
		    cell1.setCellValue(data00);
			cell1 = worksheet1.getRow(1).getCell(2);
		    cell1.setCellValue("pass");
			}	
			else{
			cell1 = worksheet1.getRow(1).getCell(1);   
			cell1.setCellValue(data00);
			cell1 = worksheet1.getRow(1).getCell(2);
			cell1.setCellValue("Fail");
		    }
			fsIP1.close(); 
		    FileOutputStream output_file1 =new FileOutputStream(new File("reference/Output/Result.xls")); 
		    wb2.write(output_file1); 
		    output_file1.close();
			}
			Thread.sleep(6000);
			try{
			    DocumentBuilderFactory docFactory1 = DocumentBuilderFactory.newInstance();
			    DocumentBuilder docBuilder1 = docFactory1.newDocumentBuilder();
			    Document doc1 = docBuilder1.parse(new File("reference/Input/EO ACK.xml"));
			    NodeList nodes2 =  doc1.getElementsByTagName("aex:AEX_ServiceOrderAcceptance");
			    for(int j1=0;j1<((NodeList) nodes2).getLength();j1++){
			    Node nodes1 = doc1.getElementsByTagName("aex:AcceptanceDetail").item(j1);
			    NodeList list1 = nodes1.getChildNodes();
			    for (int i1 = 0; i1 != list1.getLength(); ++i1){
			    Node child1 = list1.item(i1);
			    if (child1.getNodeName().equals("aex:ServiceOrderNumber")){
			    child1.getFirstChild().setNodeValue(data00) ;
			    System.out.println("data is "+data00);
			    }
		        }
		        }
			    TransformerFactory transformerFactory1 = TransformerFactory.newInstance();
			    Transformer transformer1 = transformerFactory1.newTransformer();
			    DOMSource source1 = new DOMSource(doc1);
			    StreamResult result1 = new StreamResult("reference/Input/EO ACK.xml");
			    transformer1.transform(source1, result1);
			    }
			    catch (Exception e){
			    e.printStackTrace();
			    }
		        String TestFile1 = "reference/Input/EO ACK.xml";
			    File FC1 = new File(TestFile1);
			    FC1.createNewFile();
			    FileReader FR1 = new FileReader(TestFile1);
			    BufferedReader BR1 = new BufferedReader(FR1);
			    StringBuffer fileContents1 = new StringBuffer();
			    String line1 = BR1.readLine();
			    while (line1 != null) {
			    fileContents1.append(line1);
			    line1 = BR1.readLine();
			    }
			    BR1.close();
			    driver.findElement(By.id("wmp4981:__rowu_002f_metau_002f_defaultu_002f_wm_xt_fabricfolderu_002f_0000005449:hotspot")).click();
			    driver.findElement(By.id("jsfwmp5565:defaultForm:htmlInputTextarea")).clear();
			    ((RemoteWebDriver) driver).executeScript("var t = document.getElementById('jsfwmp5565:defaultForm:htmlInputTextarea'); "+"t.value = arguments[0];",fileContents1.toString());
			    Thread.sleep(3000);
			    driver.findElement(By.id("jsfwmp5565:defaultForm:htmlCommandButton")).click();
		        Thread.sleep(3000);
		        driver.findElement(By.id("wmp4981:__rowu_002f_metau_002f_defaultu_002f_wm_xt_fabricfolderu_002f_0000005393:hotspot")).click();
		        Thread.sleep(3000);
		        Select select2 = new Select(driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/div[3]/div/div/div/div[3]/div/div[2]/div[6]/div/div/form[1]/div[3]/div/div/div/div[2]/div/div[1]/fieldset[1]/div/div[2]/span/ol/li[2]/span/div/div[1]/select")));
		        select2.selectByVisibleText("Document ID");
		        Thread.sleep(3000);
		        Select select3 = new Select(driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/div[3]/div/div/div/div[3]/div/div[2]/div[6]/div/div/form[1]/div[3]/div/div/div/div[2]/div/div[1]/fieldset[1]/div/div[2]/span/ol/li[2]/span/div/div[2]/span/span/span/div/div[1]/select")));
		        select3.selectByVisibleText("Equals");
		        Thread.sleep(3000);
		        driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/div[3]/div/div/div/div[3]/div/div[2]/div[6]/div/div/form[1]/div[3]/div/div/div/div[2]/div/div[1]/fieldset[1]/div/div[2]/span/ol/li[2]/span/div/div[2]/span/span/span/div/div[2]/input")).clear();
		        driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/div[3]/div/div/div/div[3]/div/div[2]/div[6]/div/div/form[1]/div[3]/div/div/div/div[2]/div/div[1]/fieldset[1]/div/div[2]/span/ol/li[2]/span/div/div[2]/span/span/span/div/div[2]/input")).sendKeys(data00);
		        driver.findElement(By.id("jsfwmp5465:searchBarForm:searchBarControl:refinedSearchGoButton")).click();
		        Thread.sleep(3000);
		        WebElement we_tabl = driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/div[3]/div/div/div/div[4]/div/div[2]/div[6]/div/div/form[1]/div[2]/div/table/tbody"));
				List<WebElement> a2 = we_tabl.findElements(By.tagName("tr"));
				for (int i = 1; i <= a2.size(); i++) {
				FileInputStream fsIP1= new FileInputStream(new File("reference/Output/Result.xls")); 
				@SuppressWarnings("resource")
				HSSFWorkbook wb2 = new HSSFWorkbook(fsIP1); 
			    HSSFSheet worksheet1 = wb2.getSheetAt(0); 
			    Cell cell1 = null; 
			    Thread.sleep(3000);
			    String text = driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/div[3]/div/div/div/div[4]/div/div[2]/div[6]/div/div/form[1]/div[2]/div/table/tbody/tr["+i+"]")).getText();
			    if(text.contains("AEX_ServiceOrderAcceptance_Component" ) && text.contains("DONE" ) && !text.contains("DONE W/ ERRORS" ) && text.contains(data00)){
				cell1 = worksheet1.getRow(2).getCell(1);   
			    cell1.setCellValue(data00);
				cell1 = worksheet1.getRow(2).getCell(2);
			    cell1.setCellValue("pass");
				}	
				else{
				cell1 = worksheet1.getRow(2).getCell(1);   
				cell1.setCellValue(data00);
				cell1 = worksheet1.getRow(2).getCell(2);
				cell1.setCellValue("pass");
			    }
				fsIP1.close(); 
			    FileOutputStream output_file1 =new FileOutputStream(new File("reference/Output/Result.xls")); 
			    wb2.write(output_file1); 
			    output_file1.close();
				}
	   	         driver.close();
	             driver.quit();
	   	    }
		    @After
		    public void tearDown() throws Exception {
		    String verificationErrorString = verificationErrors.toString();
		    if (!"".equals(verificationErrorString)) {
		    fail(verificationErrorString);
		    }

		}

		    
		  
	  }

