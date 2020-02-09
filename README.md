Use enmasse based on this branch https://github.com/famartinrh/enmasse/tree/export-json-connection-file
That enmasse version can generate a json with the connection details to connect to enmasse amqp endpoints

The folder `proton-connect-file-loader` contains the library for parsing the json and configure and prepare quarkus to connect to amqp endpoint

First create namespace `usernamespace`
```
oc new-project usernamespace
```

Then apply enmasse configuration
```
oc apply -f k8s
```

Then deploy the app
```
oc apply -f deployment.yaml
```