# LinkedIn Sign in example
This example demonstrates how your application can obtain user information from LinkedIn and use it for the sign-in procedure

### Configuration
Replace `client-id` and `client-secret` with real ones 

```
linkedin:
  client-id: [YOUR APP CLIENT_ID]
  client-secret: [YOUR APP CLIENT_SECRET]
  redirect-uri: http://localhost:8080/exchange # The `redirect-uri` must be set in the app configuration page
  scope: r_liteprofile r_emailaddress # all these permisssions must be activated in the app configuration page
```
### How to run the application

* `git clone git@github.com:JavatarPro/linkedin-signin.git`
* `cd linkedin-signin`
* `mvn clean install`
* `java -jar target/linkedin-signin-0.0.1-SNAPSHOT.jar`
* Open http://localhost:8080 in your browser
* Click `Sign in with LinkedIn` button
* Provide your credentials and click `Sign in` button

