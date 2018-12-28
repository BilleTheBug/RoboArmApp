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
import static android.support.test.espresso.action.ViewActions.typeText;
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

    @When("^I input rotation \"([^\"]*)\"$")
    public void iInputRotation(String rotation) throws Throwable {
        onView(withId(R.id.numSetting1)).perform(typeText(rotation));
        onView(withId(R.id.numSetting2)).perform(typeText(rotation));
        onView(withId(R.id.numSetting3)).perform(typeText(rotation));
        onView(withId(R.id.numSetting4)).perform(typeText(rotation));
        onView(withId(R.id.numSetting5)).perform(typeText(rotation));
    }

    @And("^I press submit button \"([^\"]*)\"$")
    public void iPressSubmitButton(String joint) throws Throwable {
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btnSendAll)).perform(click());
    }

    @Then("^I should see a toast\"([^\"]*)\"$")
    public void iShouldSeeAToast(String expected) throws Throwable {
        if(expected.equals("success")) {
            onView(withText(R.string.allSettingsUpdated)).inRoot(new ToastMatcher())
                    .check(matches(withText("All setting updated!")));
        }
        else{
            onView(withText(R.string.putNumberInAllFields)).inRoot(new ToastMatcher())
                    .check(matches(withText("Please put numbers in all fields")));
        }
    }
}
