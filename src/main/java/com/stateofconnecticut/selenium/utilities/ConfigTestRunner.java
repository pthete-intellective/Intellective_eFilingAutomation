 package com.stateofconnecticut.selenium.utilities;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.stateofconnecticut.selenium.Application.Modules.*;
import com.stateofconnecticut.selenium.Application.Modules.Internal_eFiling;
import com.stateofconnecticut.selenium.config.Configuration;
import com.stateofconnecticut.selenium.config.Step;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
public class ConfigTestRunner {
    private WebDriver driver;
    private Methods commonMethods;
//    public String ExternaleFiling_url;
    private String login;
    private String browser;
    private String password;
    public String internalPassword;
    public String internalLogin;
    public  readExcelDataFile data;
    private  String destFile;
    public  String MatterNumber;
//    public String FAMatterNumber=null;
    private ConfigTestRunner configTestRunner = null;
    private LoginAction loginAction;
    private Internal_eFiling internal_eFiling;
    private FOIA newFOIA;


     // private com.aventstack.extentreports.ExtentTest test;
     private com.aventstack.extentreports.ExtentTest parentTest;
     private com.aventstack.extentreports.ExtentTest childTest;
    private Map<String, String> testData = new ConcurrentHashMap<String, String>();
    private   Map<String, String> cycle = Collections.synchronizedMap(new LinkedHashMap<String, String>());
    private   Map<String, String> testCase = new ConcurrentHashMap<String, String>();
    private  Map<String, Object> values = new HashMap<String, Object>();
    private  Map<String, Step> stepMap;
    private ExtentReports extent;
    private String tcName;
    public static List<Step> steps;
    public boolean tcExecute=false;

    private void readConfignew(String TcName) {
        steps = new ArrayList<Step>();
        data = new readExcelDataFile(Constants.TestDataPath);
//        data = new readExcelDataFile("TestData.xlsx");

        int RCount = data.RowCount("TestCase");
        int CCount = data.ColCount("TestCase");
        for ( int p = 1; p <= RCount; p++) {

            TestCaseDic(p);
            if (testCase.get("SR_NO").trim().equals(TcName) && testCase.get("RunMode").trim().toUpperCase().equals("YES")) {
                tcExecute=true;
                parentTest = getExtent().createTest(testCase.get("SR_NO") + " " + testCase.get("Description"));
                tcName = testCase.get("SR_NO").trim();
//                ExternaleFiling_url = Constants.eFiling_External;
                login = testCase.get("UserName");
//                browser = testCase.get("Browser");
                password = testCase.get("Password");
                internalLogin = testCase.get("internalLogin");
                internalPassword = testCase.get("internalPassword");
                int RowNo = data.RowNo("Cycle", testCase.get("CycleName").trim(), "CycleName");
                //add cycle from cycle table
                CycleDic(RowNo);
                Set<String> keys = cycle.keySet();
                synchronized (cycle) {
                    for (String key : keys) {
                        String value = key;
                        Step step = stepMap.get(value);
                        if (step == null) {
                            throw new IllegalStateException("Unknown step '" + key + "'. Please check config.");
                        }
                        steps.add(step);
                    }
                }
                int TCRowNo = data.RowNo("TestData", testCase.get("SR_NO").trim(), "SR_NO");
                TestDataDic(TCRowNo);
                Methods methods = new Methods();
                driver = DriverFactory.getDriver();
                setDriver(driver);
                setCommonMethods(methods);
                break;
            }
        }
        if(tcExecute==false){
            parentTest = getExtent().createTest(TcName+" is not consider for execution");
            parentTest.log(Status.SKIP,"Test is not consider for execution.");
            driver.quit();
        }
    }

    public ConfigTestRunner(ExtentReports extent) {
        setExtent(extent);
        stepMap = new HashMap<String, Step>();

        stepMap.put("createFOIA", new Step() {
            @Override
            public void run() {
                newFOIA.fnAddNewFOIA(configTestRunner);
                newFOIA.searchFOIA(configTestRunner);
                loginAction.fnLoginInternalApplication( Constants.eFiling_Internal,internalLogin,internalPassword,configTestRunner);
                internal_eFiling.selectRole(configTestRunner,"pura foia reviewer");
                newFOIA.searchFOIA(configTestRunner);
                internal_eFiling.reviewer_ReviewApplication(configTestRunner,"Draft");
                internal_eFiling.selectRole(configTestRunner,"pura foia processor");
                newFOIA.searchFOIA(configTestRunner);
                internal_eFiling.processor_ProcessApplication(configTestRunner,"Reviewed","Complete");
                internal_eFiling.selectRole(configTestRunner,"pura foia requestor");
                newFOIA.searchFOIA(configTestRunner);
                internal_eFiling.CompletedApplication(configTestRunner,"Completed");


            }
        });
        stepMap.put("createFOIA_Deep", new Step() {
            @Override
            public void run() {
                internal_eFiling.selectRole(configTestRunner,"deep foia requestor");
                newFOIA.fnAddNewFOIA_Inernal(configTestRunner);
                newFOIA.searchFOIA(configTestRunner);
                internal_eFiling.selectRole(configTestRunner,"deep foia reviewer");
                newFOIA.searchFOIA(configTestRunner);
                internal_eFiling.reviewer_ReviewApplication(configTestRunner,"Draft");
                internal_eFiling.selectRole(configTestRunner,"deep foia processor");
                newFOIA.searchFOIA(configTestRunner);
                internal_eFiling.processor_ProcessApplication(configTestRunner,"Processed","Complete");
                internal_eFiling.selectRole(configTestRunner,"deep foia requestor");
                newFOIA.searchFOIA(configTestRunner);
                internal_eFiling.CompletedApplication(configTestRunner,"Completed");

            }
        });
        stepMap.put("createFOIA_PURA", new Step() {
            @Override
            public void run() {
                internal_eFiling.selectRole(configTestRunner,"pura foia requestor");
                newFOIA.fnAddNewFOIA_Inernal(configTestRunner);
                newFOIA.searchFOIA(configTestRunner);
                internal_eFiling.selectRole(configTestRunner,"pura foia reviewer");
                newFOIA.searchFOIA(configTestRunner);
                internal_eFiling.reviewer_ReviewApplication(configTestRunner,"Draft");
                internal_eFiling.selectRole(configTestRunner,"pura foia processor");
                newFOIA.searchFOIA(configTestRunner);
                internal_eFiling.processor_ProcessApplication(configTestRunner,"Reviewed","Complete");
                internal_eFiling.selectRole(configTestRunner,"pura foia requestor");
                newFOIA.searchFOIA(configTestRunner);
                internal_eFiling.CompletedApplication(configTestRunner,"Completed");

            }
        });
        stepMap.put("searchFOIA", new Step() {
            @Override
            public void run() {
                newFOIA.searchFOIA(configTestRunner);
            }
        });
        stepMap.put("loginToInternalApplication", new Step() {
            @Override
            public void run() {

                loginAction.fnLoginInternalApplication( Constants.eFiling_Internal,internalLogin,internalPassword,configTestRunner);

            }
        });
        stepMap.put("loginToExternalApplication", new Step() {
            @Override
            public void run() {
                loginAction.fnLoginApplication(driver, Constants.eFiling_External,login, password,configTestRunner);
            }
        });

        stepMap.put("loginToInternalForExtApplication", new Step() {
            @Override
            public void run() {
                loginAction.fnLoginInternalApplication( Constants.eFiling_Internal,internalLogin,internalPassword,configTestRunner);
            }
        });
        stepMap.put("LogoutExternal", new Step() {
            @Override
            public void run() {
                loginAction.fnLoghOut(configTestRunner);
            }
        });

    }

    private  void initActions() {
        Configuration conf = Configuration.read();
        loginAction = new LoginAction(driver,conf);
        newFOIA = new FOIA(driver,conf);
        internal_eFiling = new Internal_eFiling(driver,conf);
    }

    public  void run(String fileName){
        readConfignew(fileName);
        initActions();
//        childTest = parentTest.createNode("Application Login Verification");
//        loginAction.fnLoginApplication(driver, Constants.stageURL,login, password,configTestRunner);
//        matterCreation.fnMatterCreation(configTestRunner,tcName);
//        internalMatterCreation.internalMatterVerification(configTestRunner);


        for (Step step : steps) {
            step.run();
        }
      //  if(tcExecute)
       // driver.quit();

    }
    private  void TestCaseDic(int m) {
        int CCnt = data.ColCount("TestCase");
        for (int q = 0; q < CCnt; q++) {
            String Key = data.getExcelDataint("TestCase", 0, q).trim();
        try {
            if (Key.equals("")) {
                testCase.put(Key, " ");
            } else {
                String Value = data.getExcelDataint("TestCase", m, q).trim();
                testCase.put(Key, Value);
            }
        }catch (Exception e){

        }
        }
    }
    public String fnAnsSequirityQuestion(String que){
        //Ans the security Question
        String ans=null;
        int rCount =configTestRunner.data.RowCount("SecurityQuestion");
        int cCount =configTestRunner.data.ColCount("SecurityQuestion");
        for(int i = 0 ;1<=cCount ; i++){
            String Value = data.getExcelDataint("SecurityQuestion", 0, i).trim();
            if(Value.trim().equals(testCase.get("UserName"))){
                for(int j =1;j<=rCount; j++){
                    String question = data.getExcelDataint("SecurityQuestion", j, 0).trim();
                    if(question.trim().equals(que)){
                        ans =data.getExcelDataint("SecurityQuestion", j, i).trim();
                        break;
                    }
                }
                break;
            }
        }
        return ans;
    }
    private void CycleDic(int Name) {
        int Ccnt = data.ColCount("Cycle");

        for (int j = 0; j < Ccnt; j++) {
            String key = data.getExcelDataint("Cycle", 0, j).trim();
            if (key.equals("")) {
                cycle.put(key, null);
            } else {
                String value = data.getExcelDataint("Cycle", Name, j).trim();
                if (value.trim().equals("Y")) {
                    cycle.put(key, value);
                }
            }
        }
    }

    private void TestDataDic(int RowNo) {
        int Ccnt = data.ColCount("TestData");
        try {
            for (int j = 0; j < Ccnt; j++) {
                String key = data.getExcelDataint("TestData", 0, j).trim();
                if (key.equals("")) {
                    testData.put(key, null);
                } else {
                    try {
                        String value = data.getExcelDataint("TestData", RowNo, j).trim();
                        testData.put(key, value);
                    } catch (Exception ex) {
//                        ex.printStackTrace();
                    }
                }
            }
        }catch (Exception e){

        }
    }
    public String screenShotName(String screenShotName){
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH-mm-ss");
        String fileName = screenShotName+"_"+dateFormat.format(new Date())+".png";
        capture(fileName );
        return fileName;
    }
    public String capture( String screenShotName)  {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String dest = getConfigTestRunner().getDestFile() +"\\"+screenShotName;
        File destination = new File(dest);
        try {
            FileUtils.copyFile(source, destination);
            byte[] bytes = new byte[(int) source.length()];
            String base64 = new String(Base64.encodeBase64(bytes), "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        return dest;
    }

    public ExtentReports getExtent() {
        return extent;
    }
    public void setExtent(ExtentReports extent) {
        this.extent = extent;
    }
    public ConfigTestRunner getConfigTestRunner() {
        return configTestRunner;
    }

    public void setConfigTestRunner(ConfigTestRunner configTestRunner) {
        this.configTestRunner= configTestRunner;
    }

    public String getDestFile() {
        return destFile;
    }

    public void setDestFile(String destFile) {
        this.destFile = destFile;
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public Methods getCommonMethods() {
        return commonMethods;
    }

    public void setCommonMethods(Methods commonMethods) {
        this.commonMethods = commonMethods;
    }

    public ExtentTest getChildTest() {
        return childTest;
    }

    public void setChildTest(ExtentTest childTest) {
        this.childTest = childTest;
    }

    public Map<String, String> getTestData() {
        return testData;
    }

    public void setTestData(Map<String, String> testData) {
        this.testData = testData;
    }

    public ExtentTest getParentTest() {
        return parentTest;
    }

    public void setParentTest(ExtentTest parentTest) {
        this.parentTest = parentTest;
    }

    public Map<String, String> getTestCase() {
        return testCase;
    }

    public void setTestCase(Map<String, String> testCase) {
        this.testCase = testCase;
    }



    public FOIA getNewFOIA() {
        return newFOIA;
    }

    public void setNewFOIA(FOIA newFOIA) {
        this.newFOIA = newFOIA;
    }
}

