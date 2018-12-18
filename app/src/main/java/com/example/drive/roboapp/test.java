package com.example.drive.roboapp;

import cucumber.api.CucumberOptions;

@CucumberOptions(glue = "com.test.steps", format = {"junit:/data/data/com.test/JUnitReport.xml", "json:/data/data/com.test/JSONReport.json"}, tags = { "~@wip" }, features = "features")
public class test extends TestCase {
}
