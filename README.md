# javaPersonalityInsights

- Sample Java application for IBM Bluemix with IBM Watson Personality Insights API(in Japanese)

## Overview

- Sample Java source code to use IBM Watson Personality Insights API(https://new-console.ng.bluemix.net/docs/services/PersonalityInsights/index.html), especially with Japanese input text.

- This code would be run with IBM Bluemix Java runtime and Personality Insights API.

## Files

- src/my/InsightsServlet.java : Servlet to access to Personality Insights API

- WebContent/WEB-INF/lib/ : Folder for external jars

- WebContent/WEB-INF/web.xml : Configuration file

- WebContent/index.jsp : Base JSP

- README.md : This file


## Requirement

- IBM Bluemix account(http://bluemix.net/)

- JSON simple(https://json-simple.googlecode.com/files/json-simple-1.1.1.jar)

- Apache Commons Codec(https://commons.apache.org/proper/commons-codec/)

## Install

- Get json-simple-1.1.1.jar and commons-codec-1.10.jar, and put them under WebContainer/WEB-INF/lib/ .

- Access to IBM Bluemix with your ID & password, and create one Java(Liberty for Java) runtime with Personality Insights API binded.

- Push java application into IBM Bluemix Java runtime above.

## How to use

- Access to WebContext root(ex. http://xxxx.mybluemix.net/) of IBM Bluemix Java runtime above.

- Put Japanese text in textarea as much as possible. You need approximately 500 words to work fine.

- Submit your text, and you will see result of Personality Insights API with your text.


