# Appointment Reservation System
![alt text](images/EA-Project.jpg)

**1. Generate Token**
- POST http://localhost:8080/auth/token
  <br/>
```
{
    "username": "admin",
    "password": "admin"
}
```
  

**2. Test GET without ADMIN role**

- GET http://localhost:8080/auth/person
```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYyMDU5NDIyNywiaWF0IjoxNjIwNTc2MjI3fQ.sCgSDMBTRZY4DIJAINXDwkfz-LRC65N_eEHJa-yOLUGnYIjCREEgcqP0oxvgreLHMhaQ8itUN5L640cwDc_vuA
```
Response:
``` 
 {
    "timestamp": "2021-05-09T16:54:10.209+00:00",
    "status": 403,
    "error": "Forbidden",
    "message": "Forbidden",
    "path": "/auth/person"
}
```