## Running the Spring Service Application on AppEngine

__Very Important: Read this First__

This example is built on top of Maven rather than Gradle. You will need to 
install the Eclipse Maven plugin before continuing: http://www.eclipse.org/m2e/download/

This project is build over: 
1) it uses Maven rather than Gradle 
2) it uses JDO rather than JPA. 
Maven AppEngine plugin and JDO datastore API is better supported on
AppEngine. Setting up and using Maven + JDO is usually much easier than getting JPA +
Spring working correctly on AppEngine. However, the Spring Data APIs that you have
learned can use AppEngine's datastore with the right configuration. Google's guide
to using JPA is here: https://developers.google.com/appengine/docs/java/datastore/jpa/overview
if you would like to setup Spring Data on AppEngine.
3) it uses OAuth2 provided by Spring via java classes and configuration
4) Https is provided by web.xml <security-constraint>...</> tag section

After importing the project, to run the application:

Right-click on pom.xml file and Run-As->Maven build...-> and use the "appengine:devserver"
goal. 

To access the local AppEngine admin API:

http://localhost:8080/_ah/admin

To stop the application:

Open the Eclipse Debug Perspective (Window->Open Perspective->Debug), right-click on
the application in the "Debug" view (if it isn't open, Window->Show View->Debug) and
select Terminate

To deploy the application to AppEngine:

1. Go to appspot.com and create a new application and take note of the "application identifier"
2. Open the appengine-web.xml in /src/main/webapp/WEB-INF and change <application>mobilecloudvideosvc</application> 
   to <application>Your-Application-Identifier</application>
3. Run-As->Maven build...-> and use the "appengine:update"
4. Maven will build your application and upload it to AppEngine
5. When the upload is complete, you should be able to access the spring service at:
   http://spring-mvc-capstone-test.appspot.com   
6. AppEngine provides a comprehensive administrative API that you should explore after launching your application.

## Accessing the Service

To check if project is running correctly, open your browser to the following
URL:

http://localhost:8080/init

N.B. this url is accessible because excluded by OAuth2 control

## What to Pay Attention to

In this version of the application, we have built on top of Google AppEngine,
JDO, and Maven.

1. The Repositories are being manually implemented on top of JDOCrudRepository rather
   than using Spring Data
2. Maven provides automation to deploy the application to the cloud
3. The src/main/webapp/WEB-INF/web.xml file is being used to launch Spring and ensure that
   the Application class is used to configure the server. 
4. The src/main/webapp/WEB-INF/appengine-web.xml has to include the <sessions-enabled>true</sessions-enabled> 
   in order to enable the session, otherwise the OAuth over Spring won't work.