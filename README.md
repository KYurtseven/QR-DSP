# QR-DSP, QR Data Sharing Platform

Our senior design project. We have developed a mobile and web application for automative prototype part producers. 

# The Problem

Different companies have difficult time to communicate each other in the prototyping process. Every new design has their own different sub-parts that needs to be produced. Sometimes, these parts are needed from a 3rd party company. The challange is, the documentation for sub-part might not be good, sometimes have flaws such as wrong data. The communication between those 3rd party companies and the product producer is often very lengthy. When a flaw has been noticed in the production environment, the person needs to communicate with his supervisor, than that supervisor needs to send a e-mail, this email needs to be received by the other company and needs to travel for certain people until the engineer or the guy who wrote the documentation is reached. The last problem is, sometimes the documentation could be stolen or lost. Thus, it could damage the company profits.

# Solution

Our solution is, a shared mobile and web application will solve these issues. 

Whenever a part is being send from a company to another, the sending party will upload the data to QR-DSP. Then, they will arrange permissions such that only allowed people can access to the document. This will prevent possible data lost during transportational errors or stolen hard-copy documentation.

As our web application supports excel and the main companies use Excel for documentation purposes, they can use their own macros without any problem. Any macro runs on the QR-DSP can run on any company that uses our application. Thus, integration.

People can chat in QR-DSP based on the documentation. So, if a document is shared with you and you notice a problem, just write the problem to the chat and others will receive a notification. In this way, there is no need to talk to the supervisor or sending email and waiting the response for days. The recepient receives the notification immediately and can update the other party.

(This part)[QR-DSP/src/main/java/com/qrsynergy/] has majority of the code for web application.
