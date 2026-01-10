**PHASE 1️⃣ – Backend Foundation**

Spring Boot project setup

MySQL connection

Entities (Visitor, GatePass, User)

Basic CRUD APIs

JWT Authentication (Admin \& Guard)



**PHASE 2️⃣ – Visitor Registration + QR**

Visitor registration API

QR Code generation

Gate pass validation API



**PHASE 3️⃣ – Guard Verification**

OTP generation \& verification

Entry / Exit marking

Scan QR → Verify → Allow/Deny



**PHASE 4️⃣ – Frontend (React)**

Visitor registration form

QR display

Guard scan page

Admin dashboard (live logs)







**API Endpoints**


1. Login

POST

http://localhost:8080/api/auth/login

{

  "username": "guard",

  "password": "guard123"

}





2\. Register

POST

http://localhost:8080/api/visitors/register

{

  "name": "Ragul",

  "phone": "9999999999",

  "purpose": "Meeting",

  "hostName": "Admin"

}





3\. Get all visitors

GET

http://localhost:8080/api/visitors





4\. Scan QR Code (Guard)

POST

http://localhost:8080/api/guard/scan?qrData=GATEPASS-xxxx

Params

qrData = GATEPASS-xxxx





5\. Verify OTP

POST

http://localhost:8080/api/guard/verify



Params

qrData = GATEPASS-xxxx

otp    = 123456

action = ENTRY | EXIT





6\. Get all Entry/Exit Logs(Admin)

GET

http://localhost:8080/api/admin/logs



Params

Authorization: Bearer <JWT>





7\. Filter logs by Entry/Exit (Admin)

GET

http://localhost:8080/api/admin/logs/filter?action=ENTRY

http://localhost:8080/api/admin/logs/filter?action=EXIT





8\. View visitors inside the premises

GET

http://localhost:8080/api/admin/inside



