package com.example.drive.roboapp;

import java.util.List;

import cucumber.api.CucumberOptions;
import cucumber.api.TestCase;
import cucumber.api.TestStep;


@CucumberOptions(glue = "com.test.steps", plugin = {"junit:/data/data/com.test/JUnitReport.xml", "json:/data/data/com.test/JSONReport.json"}, tags = { "~@wip" }, features = "features")
public class test implements TestCase {

    @Override
    public int getLine() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getScenarioDesignation() {
        return null;
    }

    @Override
    public List<gherkin.pickles.PickleTag> getTags() {
        return null;
    }

    @Override
    public List<TestStep> getTestSteps() {
        return null;
    }

    @Override
    public String getUri() {
        return null;
    }
}
