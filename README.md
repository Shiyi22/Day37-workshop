# Workshop 37 

## Server Side

### Windows [Digital Ocean]
```
set DO_STORAGE_KEY=
set DO_STORAGE_SECRETKEY=
set DO_STORAGE_ENDPOINT=sgp1.digitaloceanspaces.com
set DO_STORAGE_ENDPOINT_REGION=sgp1
```

```
mvn clean spring-boot:run
```

## Client Side
```
ng serve --proxy-config proxy-config.js
```

## Railway Deployment 
```
ng build
```

```
copy all the files on the dist folder into static folder in server 