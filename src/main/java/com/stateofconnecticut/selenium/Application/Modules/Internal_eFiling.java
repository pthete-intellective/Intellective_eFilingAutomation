package com.stateofconnecticut.selenium.Application.Modules;

import com.aventstack.extentreports.Status;
import com.stateofconnecticut.selenium.Application.Modules.BaseAction;
import com.stateofconnecticut.selenium.config.Configuration;
import com.stateofconnecticut.selenium.utilities.ConfigTestRunner;
import com.stateofconnecticut.selenium.utilities.Constants;
import org.openqa.selenium.WebDriver;

public class Internal_eFiling extends BaseAction {

    public Internal_eFiling(WebDriver driver, Configuration conf) {
        super(driver, conf);
    }

    @Override
    public String getFormName() {
        return "Internal";
    }

    public void selectRole(ConfigTestRunner configTestRunner, String role) {
        configTestRunner.setChildTest(configTestRunner.getParentTest().createNode(role +" :Role Selection Verification"));
        try {
            configTestRunner.getCommonMethods().fnWaitForVisibility(getWebElement("Login", "profile"), Constants.waitForEleLoad);
            configTestRunner.getCommonMethods().waitAndClick(getWebElement("Login", "profile"), Constants.waitForEleLoad);
//            configTestRunner.getChildTest().log(Status.INFO, "Profile icon is click after login into the internal application.");
            try {
                configTestRunner.getCommonMethods().fnWaitForVisibility(getWebElement("Login", "roleBtn"), Constants.waitForEleLoad);
                configTestRunner.getCommonMethods().waitAndClick(getWebElement("Login", "roleBtn"), Constants.waitForEleLoad);
//                configTestRunner.getChildTest().log(Status.INFO, "Role button is click after login into the internal application from profile panel.");
                try {
                    configTestRunner.getCommonMethods().fnWaitForVisibility(getRole(role), Constants.waitForEleLoad);
                    configTestRunner.getCommonMethods().waitAndClick(getRole(role), Constants.waitForEleLoad);
                    sleep(2000);
                    configTestRunner.getChildTest().log(Status.PASS, role + " is selected successfully");
                } catch (Exception e) {
                    configTestRunner.getChildTest().log(Status.FAIL, "Role is not selected successfully.");
                }
            } catch (Exception e) {
                configTestRunner.getChildTest().log(Status.FAIL, "Roles drop down is not present on the application to change the role");
            }
        } catch (Exception e) {
            configTestRunner.getChildTest().log(Status.FAIL, "Profile icon is not present on the internal application to change the role.");
        }
    }

    public void reviewer_ReviewApplication(ConfigTestRunner configTestRunner, String status){
        configTestRunner.setChildTest(configTestRunner.getParentTest().createNode("Reviewer Tab Verification"));
        try {
            configTestRunner.getCommonMethods().fnWaitForVisibility(getFOIAWithStatus(configTestRunner.MatterNumber,status), Constants.waitForEleLoad);
            configTestRunner.getCommonMethods().waitAndClick(getFOIAWithStatus(configTestRunner.MatterNumber,status), Constants.waitForEleLoad);
            configTestRunner.getChildTest().log(Status.PASS, configTestRunner.MatterNumber + " is present on the reviewer tab after FOIA creation .");
            try{
                configTestRunner.getCommonMethods().fnWaitForVisibility(getPContainsWithText("Reviewer"), Constants.waitForEleLoad);
                configTestRunner.getCommonMethods().waitAndClick(getPContainsWithText("Reviewer"), Constants.waitForEleLoad);
                configTestRunner.getChildTest().log(Status.PASS, configTestRunner.MatterNumber + " open successfully for review.");
                try {
                    sleep(5000);
                    if (getSpanContainsWithText("FOIA Details").getText().contains("DEEP")) {
                        configTestRunner.getCommonMethods().fnWaitForVisibility(getWebElement("assignDropDown"), Constants.waitForEleLoad);
                        configTestRunner.getCommonMethods().setdataSendKeysEnterkeyDown(getWebElement("assignDropDown"), "p8admin_demo");
                    } else{
                        configTestRunner.getCommonMethods().fnWaitForVisibility(getWebElement("assignDropDownPURA"), Constants.waitForEleLoad);
                        configTestRunner.getCommonMethods().setdataSendKeysEnterkeyDown(getWebElement("assignDropDownPURA"), "p8admin_demo");
                        configTestRunner.getCommonMethods().fnWaitForVisibility(getWebElement("staffassignDropDownPURA"), Constants.waitForEleLoad);
                        configTestRunner.getCommonMethods().setdataSendKeysEnterkeyDown(getWebElement("staffassignDropDownPURA"), "p8admin_demo");
                   }
                    sleep(800);
                    configTestRunner.getCommonMethods().waitAndClick(getspanWithText("Assign Staff"),Constants.waitForEleLoad);
                    configTestRunner.getChildTest().log(Status.PASS,"p8admin_demo staff is assign successfully from the reviewer tab.");
                    try {
                        configTestRunner.getCommonMethods().fnWaitForVisibility(getWebElement( "successMessage"), Constants.waitForEleLoad);
                        configTestRunner.getChildTest().log(Status.PASS,"Staff Assignment is successful from the reviewer tab");
                    }catch (Exception e){
                        configTestRunner.getChildTest().log(Status.FAIL,"Staff is not assign successfully from reviewer tab.");
                    }
                }catch (Exception e){
                    configTestRunner.getChildTest().log(Status.FAIL, configTestRunner.MatterNumber+" is not open successfully from the reviewer tab");
                }
            }catch (Exception e){
                configTestRunner.getChildTest().log(Status.FAIL, configTestRunner.MatterNumber+" is not present in the the reviewer tab");
            }
        } catch (Exception e) {
            configTestRunner.getChildTest().log(Status.FAIL, getFOIAWithStatus(configTestRunner.MatterNumber,status) + " is not present on the reviewer tab after FOIA creation .");
        }
    }
    public void processor_ProcessApplication(ConfigTestRunner configTestRunner, String status,String action){
        configTestRunner.setChildTest(configTestRunner.getParentTest().createNode("Processor Tab Verification"));
        try {
            configTestRunner.getCommonMethods().fnWaitForVisibility(getFOIAWithStatus(configTestRunner.MatterNumber,status), Constants.waitForEleLoad);
            configTestRunner.getCommonMethods().waitAndClick(getFOIAWithStatus(configTestRunner.MatterNumber,status), Constants.waitForEleLoad);
            configTestRunner.getChildTest().log(Status.PASS, configTestRunner.MatterNumber+ " is present on the processor tab after FOIA review & status of FOIA is ."+status);
            try{
                configTestRunner.getCommonMethods().fnWaitForVisibility(getPContainsWithText("Processor"), Constants.waitForEleLoad);
                configTestRunner.getCommonMethods().waitAndClick(getPContainsWithText("Processor"), Constants.waitForEleLoad);
                configTestRunner.getChildTest().log(Status.PASS, configTestRunner.MatterNumber + " open successfully for processing.");
                try{
                    configTestRunner.getCommonMethods().fnWaitForVisibility(getspanWithText(action),Constants.waitForEleLoad);
                    configTestRunner.getCommonMethods().waitAndClick(getspanWithText(action),Constants.waitForEleLoad);
                    configTestRunner.getChildTest().log(Status.PASS,"Processor "+action+" the created FOIA from the processor tab.");
                    try {
                        configTestRunner.getCommonMethods().fnWaitForVisibility(getWebElement("Internal", "successMessage"), Constants.waitForEleLoad);
                        configTestRunner.getChildTest().log(Status.PASS,"FOIA Processing is "+action+" successfully from the processor tab.");
                    }catch (Exception e){
                        configTestRunner.getChildTest().log(Status.FAIL,"FOIA Processing is not "+action+" successfully from the processor tab..");
                    }
                }catch (Exception e){
                    configTestRunner.getChildTest().log(Status.FAIL, configTestRunner.MatterNumber+" is not open successfully from the processor tab");
                }
            }catch (Exception e){
                configTestRunner.getChildTest().log(Status.FAIL, configTestRunner.MatterNumber+" is not present in the the processor tab");
            }
        } catch (Exception e) {
            configTestRunner.getChildTest().log(Status.FAIL, getFOIAWithStatus(configTestRunner.MatterNumber,status) + " is not present on the processor tab after FOIA review & assign successfully .");
        }
    }

    public void CompletedApplication(ConfigTestRunner configTestRunner, String status){
        configTestRunner.setChildTest(configTestRunner.getParentTest().createNode("FOIA is completed Successfully verification"));
        try {
            configTestRunner.getCommonMethods().fnWaitForVisibility(getFOIAWithStatus(configTestRunner.MatterNumber,status), Constants.waitForEleLoad);
            configTestRunner.getCommonMethods().waitAndClick(getFOIAWithStatus(configTestRunner.MatterNumber,status), Constants.waitForEleLoad);
            configTestRunner.getChildTest().log(Status.PASS, configTestRunner.MatterNumber + " is completed successfully.");

        } catch (Exception e) {
            configTestRunner.getChildTest().log(Status.FAIL, getFOIAWithStatus(configTestRunner.MatterNumber,status) + " is not present on the requestor.");
        }
    }


}
