package com.stateofconnecticut.selenium.Application.Modules;

import com.aventstack.extentreports.Status;
import com.stateofconnecticut.selenium.config.Configuration;
import com.stateofconnecticut.selenium.utilities.ConfigTestRunner;
import com.stateofconnecticut.selenium.utilities.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FOIA extends BaseAction {
    protected static final Log logger = LogFactory.getLog(LoginAction.class);

    public FOIA(WebDriver driver, Configuration conf) {
        super(driver, conf);
    }

    @Override
    public String getFormName() {
        return "FOIA";
    }

    public void fnAddNewFOIA(ConfigTestRunner configTestRunner) {
        configTestRunner.setChildTest(configTestRunner.getParentTest().createNode("FOIA Submit form"));
        fnWaitForVisibility(getWebElement("firstName"),Constants.waitForEleLoad);
        List<String> fields = new ArrayList<>(Arrays.asList(configTestRunner.getTestData().get("Fields").split(",")));
        fillForm(fields, getFormName(), configTestRunner);
        configTestRunner.getChildTest().log(Status.PASS, "Fill FOIA Submit form - successful");
        //click on attachment document
        if (configTestRunner.getCommonMethods().getspanWithText("Create").isEnabled()) {
            configTestRunner.getCommonMethods().waitAndClick(configTestRunner.getCommonMethods().getspanWithText("Create"), Constants.waitForEleLoad);
//            try{
//                    fnWaitForVisibility(configTestRunner.getCommonMethods().getspanWithText("addressValidation"),30);
//                    configTestRunner.getCommonMethods().waitAndClick(configTestRunner.getCommonMethods().getspanWithText("addressValidation"),Constants.waitForEleLoad);
//            }catch (Exception e){
//                configTestRunner.getChildTest().log(Status.FAIL,"Address Validation pop up is not opended.");
//            }

        }
        configTestRunner.setChildTest(configTestRunner.getParentTest().createNode("Matter Confirmation Verification"));
        try {
            configTestRunner.getCommonMethods().fnWaitForVisibility(getWebElement("confirmationMessage"), Constants.waitForEleLoad);
            Assert.assertTrue(getWebElement("confirmationMessage").isDisplayed());
            if (getWebElement("confirmationMessage").isDisplayed()) {
                String[] matterno = getWebElement("confirmationMessage").getText().split(" ");
                configTestRunner.MatterNumber = matterno[matterno.length - 1];
                System.out.println(configTestRunner.MatterNumber);
                configTestRunner.data.setCellData(configTestRunner.MatterNumber, configTestRunner.data.rowValue(configTestRunner.getTestCase().get("SR_NO"), configTestRunner.data.ColumnValue("SR_NO", "TestCase")), configTestRunner.data.ColumnValue("FOIA_Matter_No", "TestCase"));
                configTestRunner.getChildTest().log(Status.PASS, "Matter is created successfully & the matter number is :" + configTestRunner.MatterNumber);
                try {
                    String name = new Object() {
                    }.getClass().getEnclosingMethod().getName();
                    configTestRunner.getChildTest().log(Status.PASS, "Matter is created successfully " + configTestRunner.getChildTest().addScreenCaptureFromPath(configTestRunner.screenShotName("FOIACreated_")));

                } catch (Exception e) {

                }
                logger.info("Matter is created with matter no :" + configTestRunner.MatterNumber);
            }
        } catch (Exception e) {
            configTestRunner.getChildTest().log(Status.FAIL, "Confirmation message is not displayed in the application.");
        }
        sleep(1600);
//        configTestRunner.getCommonMethods().fnWaitForInVisibility(getWebElement("confirmationMessage"), 30);
        //click on next button
        configTestRunner.getCommonMethods().waitAndClick(configTestRunner.getCommonMethods().getspanWithText("Next"), Constants.waitForEleLoad);
        //attach documnte
        attachDocument(configTestRunner);
        configTestRunner.getCommonMethods().waitAndClick(configTestRunner.getCommonMethods().getspanWithText("Complete"), Constants.waitForEleLoad);
        //click on user enter address field
        try {
            configTestRunner.getCommonMethods().fnWaitForVisibility(getWebElement("attachmentconfirmationMessage"),Constants.waitForEleLoad);
            System.out.println(getWebElement("attachmentconfirmationMessage").getText());
            configTestRunner.getChildTest().log(Status.PASS, "FOIA is completed successfully");
        } catch (Exception e) {
//            configTestRunner.getCommonMethods().waitAndClick(userEnterAddress, 30);
            configTestRunner.getChildTest().log(Status.FAIL, "FOIA is not completed successfully");
        }
    }


    public void attachDocument(ConfigTestRunner configTestRunner) {
        //click on attach button
        configTestRunner.setChildTest(configTestRunner.getParentTest().createNode("Document upload verification"));
        configTestRunner.getCommonMethods().fnWaitForVisibility(configTestRunner.getCommonMethods().getspanWithText("Attach"), Constants.waitForEleLoad);
        executeExtJsClick(configTestRunner.getDriver(),configTestRunner.getCommonMethods().getspanWithText("Attach"));
        sleep(2000);
        WebElement browser = driver.findElement(By.cssSelector("input[type='file']"));
        String js = "arguments[0].style.height='auto';arguments[0].style.visibility='visible';";
        ((JavascriptExecutor) driver).executeScript(js, browser);
//        String filePath = Constants.FileUploadPath + "PDF_TestAttachment.pdf";
//        String filePath = "PDF_TestAttachment.pdf";
        ClassLoader classLoader = getClass().getClassLoader();
        File file =new File(classLoader.getResource(Constants.FileUploadPath).getFile());
        browser.sendKeys(file.getAbsolutePath());

        sleep(3000);
        try {
            configTestRunner.getCommonMethods().fnWaitForVisibility(getWebElement("attachBtn"), Constants.waitForEleLoad);
            executeExtJsClick(configTestRunner.getDriver(),getWebElement("attachBtn"));
            configTestRunner.getCommonMethods().fnWaitForVisibility(getWebElement("attachmentconfirmationMessage"),Constants.waitForEleLoad);
            sleep(3000);
            ;
            try {
                fnWaitForVisibility(configTestRunner.getCommonMethods().getPWithText(Constants.FileUploadPath),Constants.waitForEleLoad);
                    configTestRunner.getChildTest().log(Status.PASS, "Document is uploaded successfully.");

            } catch (Exception e) {
                configTestRunner.getChildTest().log(Status.FAIL, "Document is not uploaded successfully.");
            }
        } catch (Exception e) {
            configTestRunner.getChildTest().log(Status.INFO, "Attah button is not present to upload document after selecting document");
        }
    }
    public boolean searchFOIA(ConfigTestRunner configTestRunner){
        //configTestRunner.setChildTest(configTestRunner.getParentTest().createNode("Search FOIA matter verification"));
        boolean isPresent = false;
        fnWaitForVisibility(getWebElement("FOIA","searchInput"),Constants.waitForEleLoad);
        getWebElement("FOIA","searchInput").sendKeys(configTestRunner.MatterNumber);
        try{
            fnWaitForVisibility(configTestRunner.getCommonMethods().searchMatterExist(configTestRunner.MatterNumber),Constants.waitForEleLoad);
            isPresent=true;
        }catch (Exception e){
            configTestRunner.getChildTest().log(Status.FAIL, "FOIA is not search successfully in external application");
        }
        return isPresent;
    }
    public void fnAddNewFOIA_Inernal(ConfigTestRunner configTestRunner) {
        configTestRunner.setChildTest(configTestRunner.getParentTest().createNode("FOIA Submit form"));
        try{
            sleep(1000);
            fnWaitForVisibility(getspanWithText("Submit FOIA"),Constants.waitForEleLoad);
            configTestRunner.getCommonMethods().waitAndClick(getspanWithText("Submit FOIA"),Constants.waitForEleLoad);
        }catch (Exception e){

        }
        sleep(3000);
        fnWaitForVisibility(getWebElement("firstName"),Constants.waitForEleLoad);
        List<String> fields = new ArrayList<>(Arrays.asList(configTestRunner.getTestData().get("Fields").split(",")));
        fillForm(fields, getFormName(), configTestRunner);
        configTestRunner.getChildTest().log(Status.PASS, "Fill FOIA Submit form - successful");
        //click on attachment document
        if (configTestRunner.getCommonMethods().getspanWithText("Create").isEnabled()) {
            configTestRunner.getCommonMethods().waitAndClick(configTestRunner.getCommonMethods().getspanWithText("Create"), Constants.waitForEleLoad);
//            try{
//                    fnWaitForVisibility(configTestRunner.getCommonMethods().getspanWithText("addressValidation"),30);
//                    configTestRunner.getCommonMethods().waitAndClick(configTestRunner.getCommonMethods().getspanWithText("addressValidation"),Constants.waitForEleLoad);
//            }catch (Exception e){
//                configTestRunner.getChildTest().log(Status.FAIL,"Address Validation pop up is not opended.");
//            }

        }
        configTestRunner.setChildTest(configTestRunner.getParentTest().createNode("Matter Confirmation Verification"));
        try {
            configTestRunner.getCommonMethods().fnWaitForVisibility(getWebElement("confirmationMessage"), Constants.waitForEleLoad);
            Assert.assertTrue(getWebElement("confirmationMessage").isDisplayed());
            if (getWebElement("confirmationMessage").isDisplayed()) {
                String[] matterno = getWebElement("confirmationMessage").getText().split(" ");
                configTestRunner.MatterNumber = matterno[matterno.length - 1];
                System.out.println(configTestRunner.MatterNumber);
                configTestRunner.data.setCellData(configTestRunner.MatterNumber, configTestRunner.data.rowValue(configTestRunner.getTestCase().get("SR_NO"), configTestRunner.data.ColumnValue("SR_NO", "TestCase")), configTestRunner.data.ColumnValue("FOIA_Matter_No", "TestCase"));
                configTestRunner.getChildTest().log(Status.PASS, "Matter is created successfully & the matter number is :" + configTestRunner.MatterNumber);
                try {
                    String name = new Object() {
                    }.getClass().getEnclosingMethod().getName();
                    configTestRunner.getChildTest().log(Status.PASS, "Matter is created successfully " + configTestRunner.getChildTest().addScreenCaptureFromPath(configTestRunner.screenShotName("FOIACreated_")));

                } catch (Exception e) {

                }
                logger.info("Matter is created with matter no :" + configTestRunner.MatterNumber);
            }
        } catch (Exception e) {
            configTestRunner.getChildTest().log(Status.FAIL, "Confirmation message is not displayed in the application.");
        }
        sleep(1600);
//        configTestRunner.getCommonMethods().fnWaitForInVisibility(getWebElement("confirmationMessage"), 30);

        //configTestRunner.getCommonMethods().waitAndClick(configTestRunner.getCommonMethods().getspanWithText("Next"), Constants.waitForEleLoad);

        //attachDocument(configTestRunner);
        configTestRunner.getCommonMethods().waitAndClick(configTestRunner.getCommonMethods().getspanWithText("Complete"), Constants.waitForEleLoad);
        //click on user enter address field
        try {
            configTestRunner.getCommonMethods().fnWaitForVisibility(getWebElement("attachmentconfirmationMessage"),Constants.waitForEleLoad);
            System.out.println(getWebElement("attachmentconfirmationMessage").getText());
            configTestRunner.getChildTest().log(Status.PASS, "FOIA is completed successfully");
        } catch (Exception e) {
//            configTestRunner.getCommonMethods().waitAndClick(userEnterAddress, 30);
            configTestRunner.getChildTest().log(Status.FAIL, "FOIA is not completed successfully");
        }
    }


}


