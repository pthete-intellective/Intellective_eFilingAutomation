package com.stateofconnecticut.selenium.Application.Modules;

import com.aventstack.extentreports.Status;
import com.stateofconnecticut.selenium.config.Configuration;
import com.stateofconnecticut.selenium.utilities.ConfigTestRunner;
import com.stateofconnecticut.selenium.utilities.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.WebDriver;
public class LoginAction extends  BaseAction{
    protected static final Log logger = LogFactory.getLog(LoginAction.class);
    public LoginAction(WebDriver driver, Configuration conf) {
        super(driver, conf);
    }
    @Override
    public String getFormName() {
        return "Login";
    }
    public void fnLoginApplication(WebDriver driver, String  url,String userId, String password, ConfigTestRunner configTestRunner){
        configTestRunner.setChildTest(configTestRunner.getParentTest().createNode("Login To The eFiling External Application Verification"));
        driver.get(url);
        if(configTestRunner.getCommonMethods().getspanWithText("Submit FOIA").isDisplayed()){
            configTestRunner.getChildTest().log(Status.PASS, "Login to the eFiling External Application is successfully.");
        }
        else {
            try {
                String name = new Object() {
                }.getClass().getEnclosingMethod().getName();
                configTestRunner.getChildTest().log(Status.FAIL, "Login to the eFiling External Application is not- successfully" + configTestRunner.getChildTest().addScreenCaptureFromPath(configTestRunner.screenShotName(name )));

            } catch (Exception e) {

            }
        }
    }
    public void fnLoginInternalApplication( String url,String userId, String password, ConfigTestRunner configTestRunner){
        configTestRunner.setChildTest(configTestRunner.getParentTest().createNode("Login To The Internal Verification"));
        configTestRunner.getDriver().get(url);
        configTestRunner.getCommonMethods().fnWaitForVisibility(getWebElement(getFormName(),"userIdText"),Constants.waitForEleLoad);
        getWebElement(getFormName(),"userIdText").sendKeys(userId);
        configTestRunner.getChildTest().log(Status.INFO,"Log with user : "+userId);
        getWebElement(getFormName(),"passwordText").clear();
        getWebElement(getFormName(),"passwordText").sendKeys(password);
        configTestRunner.getChildTest().log(Status.INFO,"Log with password : "+password);
        configTestRunner.getCommonMethods().sendKeysEnter(getspanWithText("Sign In"));
        sleep(3000);
        //configTestRunner.getCommonMethods().fnWaitForVisibility(configTestRunner.getCommonMethods().getspanWithText(configTestRunner,"Search"),120);
        if(configTestRunner.getCommonMethods().getspanWithText("Submit FOIA").isDisplayed()){
            configTestRunner.getChildTest().log(Status.PASS, "Login to the internal eFiling application is successfully.");
        }
        else {
            try {
                String name = new Object() {
                }.getClass().getEnclosingMethod().getName();
                configTestRunner.getChildTest().log(Status.FAIL, "Login to the internal eFiling application is not- successfully" + configTestRunner.getChildTest().addScreenCaptureFromPath(configTestRunner.screenShotName(name )));

            } catch (Exception e) {

            }
        }
    }
    public void fnLoghOut(ConfigTestRunner configTestRunner) {
        configTestRunner.setChildTest(configTestRunner.getParentTest().createNode("LogOut from the external application"));
        if (!(configTestRunner.getDriver().getCurrentUrl().contains("external"))) {
            sleep(500);
            try {
                if(fnWaitForVisibility(getWebElement("profile"),Constants.waitForEleLoad)) {
                    configTestRunner.getCommonMethods().waitAndClick(getWebElement("profile"),Constants.waitForEleLoad);
                    try {
                        if (fnWaitForVisibility(getWebElement("logoutbtn"), Constants.waitForEleLoad)) {
                            configTestRunner.getCommonMethods().waitAndClick(getWebElement("logoutbtn"), Constants.waitForEleLoad);
                            try {
                                if (fnWaitForVisibility(getWebElement("passwordText"), Constants.waitForEleLoad)) {
                                    configTestRunner.getChildTest().log(Status.PASS, "Logout from the internal application is successfully.");
                                }
                            } catch (Exception e) {
                                configTestRunner.getChildTest().log(Status.FAIL, "Logout from the internal application is not successfully.");
                            }
                        }
                    }catch (Exception e1){
                        configTestRunner.getChildTest().log(Status.FAIL, "LogOut button is not present in the profile panel");
                    }
                }
            }catch (Exception e){
                configTestRunner.getChildTest().log(Status.FAIL, "Profile icon is not display in the application to logout");
            }
        } else
            configTestRunner.getChildTest().log(Status.PASS, "Logout from the external application is successfully.");

    }

}
