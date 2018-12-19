package com.example.drive.roboapp;

import android.support.test.runner.MonitoringInstrumentation;

import java.util.List;

import cucumber.api.CucumberOptions;

@CucumberOptions(/*glue = "com.ControlRobotTest.steps", */plugin = {"junit:/data/data/com.ControlRobotTest/JUnitReport.xml", "json:/data/data/com.ControlRobotTest/JSONReport.json"}, tags = { "~@wip" }, features = "features")
public class ControlRobotTest extends MonitoringInstrumentation {
    // Start feature tests here?
}
