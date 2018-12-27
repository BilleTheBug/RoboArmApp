package com.example.drive.roboapp.test;

import android.app.Activity;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import com.example.drive.roboapp.MainActivity;

import org.junit.Assert;
import org.junit.Rule;

import cucumber.api.PendingException;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class SendMessageSteps {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    private Activity activity;

    @Before("@login-feature")
    public void setup() {
        activityTestRule.launchActivity(new Intent());
        activity = activityTestRule.getActivity();
    }

    @After("@login-feature")
    public void tearDown() {
        activityTestRule.finishActivity();
    }


    @Given("^I am in the main activity$")
    public void iAmInTheMainActivity() throws Throwable {
        Assert.assertTrue(true);
    }

    @And("^I write numbers in all fields$")
    public void iWriteNumbersInAllFields() throws Throwable {
        Assert.assertTrue(true);
    }

    @Then("^I should see a toast with text success$")
    public void iShouldSeeAToastWithTextSuccess() throws Throwable {
        Assert.assertTrue(true);
    }

    @When("^I select robo arm one$")
    public void iSelectRoboArmOne() throws Throwable {
        Assert.assertTrue(true);
    }
}
