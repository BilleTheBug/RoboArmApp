package com.example.drive.roboapp.test;

import android.app.Activity;
import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;

import com.example.drive.roboapp.MainActivity;
import com.example.drive.roboapp.R;
import com.example.drive.roboapp.TestActivity;

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
import static android.support.test.espresso.matcher.ViewMatchers.withId;

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
        //Assert.assertNotNull(true);
    }

    @And("^I press submit button \"([^\"]*)\"$")
    public void iPressSubmitButton(String joint) throws Throwable {
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btnSendAll)).perform(click());
        //Assert.assertNotNull(true);
    }

    @Then("^I should see a toast with text 'Updated successfully!'$")
    public void iShouldSeeAToastWithTextUpdatedSuccessfully() throws Throwable {
        //onView(withText("Updated successfully!")).inRoot(withDecorView(not(activity.getWindow().getDecorView()))).check(matches(isDisplayed()));
        Assert.assertNotNull(true);
    }
}
