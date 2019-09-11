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
### How to use

* Open start page in your browser http://localhost:8080
* Click `Sign in with LinkedIn` button
* Provide your credentials and click `Sign in` button

