package com.example.drive.roboapp.test;

import android.app.Activity;
import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;

import com.example.drive.roboapp.MainActivity;
import com.example.drive.roboapp.R;

import org.junit.Assert;
import org.junit.Rule;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class UpdateServoRotationSteps {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    private Activity activity;

    @Before("@updateServoRotation-feature")
    public void setup() {
        activityTestRule.launchActivity(new Intent());
        activity = activityTestRule.getActivity();
    }

    @After("@updateServoRotation-feature")
    public void tearDown() {
        activityTestRule.finishActivity();
    }



    @Given("^I am in the robot controller activity$")
    public void iAmInTheRobotControllerActivity() throws Throwable {
        Assert.assertNotNull(activity);
    }

    @And("^I press the 'send all' button$")
    public void iPressTheSendAllButton() throws Throwable {
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btnSendAll)).perform(click());
    }


    @And("^I input rotation \"([^\"]*)\" in the fields of the other joints$")
    public void iInputRotationInTheFieldsOfTheOtherJoints(String rotation) throws Throwable {
        onView(withId(R.id.numSetting2)).perform(replaceText(rotation));
        onView(withId(R.id.numSetting3)).perform(replaceText(rotation));
        onView(withId(R.id.numSetting4)).perform(replaceText(rotation));
        onView(withId(R.id.numSetting5)).perform(replaceText(rotation));
    }

    @And("^I press the 'foot' button$")
    public void iPressTheFootButton() throws Throwable {
        onView(withId(R.id.btnSetting1)).perform(click());
    }

    @When("^No value is in the foot joints rotation fields$")
    public void noValueIsInTheFootJointsRotationFields() throws Throwable {
        onView(withId(R.id.numSetting1)).perform(replaceText(""));
    }

    @When("^I input rotation \"([^\"]*)\" in all fields$")
    public void iInputRotationInAllFields(String rotation) throws Throwable {
        onView(withId(R.id.numSetting1)).perform(replaceText(rotation));
        onView(withId(R.id.numSetting2)).perform(replaceText(rotation));
        onView(withId(R.id.numSetting3)).perform(replaceText(rotation));
        onView(withId(R.id.numSetting4)).perform(replaceText(rotation));
        onView(withId(R.id.numSetting5)).perform(replaceText(rotation));
    }

    @When("^No value is in at least one of the rotation fields$")
    public void noValueIsInAtLeastOneOfTheRotationFields() throws Throwable {
        onView(withId(R.id.numSetting1)).perform(replaceText(""));
    }

    @When("^I input rotation \"([^\"]*)\" in the field of foot joint$")
    public void iInputRotationInTheFieldOfFootJoint(String rotation) throws Throwable {
        onView(withId(R.id.numSetting1)).perform(replaceText(rotation));
    }

    @Then("^I should see an error toast saying 'please add a number'$")
    public void iShouldSeeAnErrorToastSayingPleaseAddANumber() throws Throwable {
        onView(withText(R.string.pleaseAddNumber)).inRoot(new ToastMatcher())
                .check(matches(withText("Please add a number")));
    }

    @Then("^I should see a successful toast saying 'updated successfully'$")
    public void iShouldSeeASuccessfulToastSayingUpdatedSuccessfully() throws Throwable {
        //Sleep needed for the other toast to go away. We need a new fix for this, or stop testing for toasts.
        Thread.sleep(500);
        onView(withText(R.string.updatedSuccessfully)).inRoot(new ToastMatcher())
                .check(matches(withText("Updated successfully!")));
    }

    @Then("^I should see an error toast$")
    public void iShouldSeeAnErrorToast() throws Throwable {
        onView(withText(R.string.putNumberInAllFields)).inRoot(new ToastMatcher())
                .check(matches(withText("Please put numbers in all fields")));
    }

    @Then("^I should see a successful toast$")
    public void iShouldSeeASuccessfulToast() throws Throwable {
        onView(withText(R.string.allSettingsUpdated)).inRoot(new ToastMatcher())
                .check(matches(withText("All setting updated!")));
    }
}