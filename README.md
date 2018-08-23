Mongo Workshop (maven version)
===============================

Exercise: write a code responsible for fetching and changing data in MongoDB. They are a few ```TODO``` in class 
 ```wareq.mongoworkshop.repository.DriverRepository``` that should be resolved. Good luck! 

Check your solutions: ```mvn clean test```

You can run application: ```mvn clean spring-boot:run```

### Working endpoints
* http://localhost:8080/drivers?first-name=Jan&last-name=Nowak
* http://localhost:8080/drivers/5b6df04211561d86bfe9705f
* http://localhost:8080/elders?skip=10&limit=100

* http://localhost:8080/commands/generate-driver
* http://localhost:8080/commands/increase-point/5b6df04211561d86bfe9705f
* http://localhost:8080/commands/increase-age/20

* http://localhost:8080/vehicles/PZ32382

### Hints
It's useful to have JSON formatter plugin in a browser
* [Chrome]( 
https://chrome.google.com/webstore/detail/json-formatter/bcjindcccaagfpapjjmafapmmgkkhgoa?hl=en)
* [Firefox](https://addons.mozilla.org/en-US/firefox/addon/basic-json-formatter/?src=search)