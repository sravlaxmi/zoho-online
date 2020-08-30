package dfPack.tests;

import java.util.HashMap;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.LogStatus;

import dfPack.base.BaseTest;
import dfPack.util.DataUtil;
import dfPack.util.ExtentManager;
import dfPack.util.MyXLSReader;

public class LoginTest extends BaseTest {
	
	
	@BeforeClass
	public void init() {
		
		initialize();
		
	}
	
	@DataProvider
	public Object[][] getData() {
		
		Object[][] obj = null;
		
		//Get the data from the excel file
		
		try {
		
			xlsx = new MyXLSReader(prop.getProperty("xlsxFilePath"));
			
			obj = DataUtil.getTestData(xlsx,"LoginTest","Data");
		
		}catch(Exception e) {
			
			e.printStackTrace();
			
		}
		
		return obj;
		
		
	}
	
	@Test(dataProvider="getData")
	public void doLoginTest(HashMap<String,String> map) {
		
		eReport = ExtentManager.getInstance();
		
		eTest = eReport.startTest("LoginTest");
		
		eTest.log(LogStatus.INFO,"Login test started");
		
		if(!DataUtil.isRunnable(xlsx,"LoginTest","Testcases") || map.get("Runmode").equals("N")) {
			
			eTest.log(LogStatus.INFO,"Skipping the test as the run mode is set to N");
			throw new SkipException("Skipping the test as the run mode is set to N");
			
		}
		
		//Automation code - starts here
		openBrowser(map.get("Browser"));
		
		navigate("appURL");
		
		boolean actualResult = doLogin(map.get("Username"),map.get("Password"));
		
		String expectedRes = map.get("ExpectedResult");
		
		boolean expectedResult = false;
		
		if(expectedRes.equalsIgnoreCase("Failure")) {
			
			expectedResult = false;
			
		}else if(expectedRes.equalsIgnoreCase("Success")) {
			
			expectedResult = true;
			
		}
		
		if(actualResult==expectedResult) {
			
			reportPass("LoginTest got passed");
			
		}else {
			
			reportFail("LoginTest got failed");
			
		}
		
	}

	@AfterMethod
	public void testClosure() {
		
		if(eReport!=null) {
		
			eReport.endTest(eTest);
			
			eReport.flush();
		
		}
		
		if(driver!=null) {
			
			driver.quit();
			
		}
		
	}
	
	
}
