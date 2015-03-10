# spring-cxf

Demonstrates ability to host a JAX-WS service using Spring Boot and Apache CXF with zero XML configuration.

## Run the Service
In a terminal, execute: `./gradlew bootRun`

Alternatively can be run in your IDE of choice by running `demo.spring.service.Application.java` as a normal Java application.

### Service endpoint
The service endpoints are located at `api/`.  Assuming the embedded Tomcat is being used, point a browser to:

    http://localhost:8080/api/
    
The WSDLs are available at a service endpoint by appending `?wsdl` after the service endpoint, such as:

    http://localhost:8080/api/xdsb?wsdl    


## Execute the client
The service can be tested with any standard WS tool, such as SoapUI or a client using classes compiled against the WSDL.

The `ws-client ` module includes an example of accessing the service using code generated using wsdl2java as described above.  

With the server running, execute the class `demo.service.client.ClientDemo.java` which contains a `main()` method.  Upon running, the XDS.b example will upload a file from the resources directory.

The server prints out the following message to acknowledge receipt of the uploaded document:
     
    d.s.s.custom.BasicDocRepoServiceImpl     : Acknowledge receipt of document!
    d.s.s.custom.BasicDocRepoServiceImpl     : ...document found with id: docId
    d.s.s.custom.BasicDocRepoServiceImpl     : Retrieved contents of file: some dummy file
    d.s.s.custom.BasicDocRepoServiceImpl     : .......and javax.activation.DataHandler@21a85088

And the client prints out the response returned by the server:

    Status from server's response: Super- Thanks for the document
    
Logging interceptors are also configured to demonstrate the raw SOAP payloads.  Corresponding the the above interaction this gives:

    Request:
    <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><ns4:ProvideAndRegisterDocumentSetRequest xmlns="urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0" xmlns:ns2="urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0" xmlns:ns3="urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0" xmlns:ns4="urn:ihe:iti:xds-b:2007" xmlns:ns5="urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0"><ns3:SubmitObjectsRequest id="abc123" comment="This is the first demo request"/><ns4:Document id="docId">c29tZSBkdW1teSBmaWxl</ns4:Document></ns4:ProvideAndRegisterDocumentSetRequest></soap:Body></soap:Envelope>
    
    Response:
    <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><ns2:RegistryResponse xmlns="urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0" xmlns:ns2="urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0" xmlns:ns3="urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0" xmlns:ns4="urn:ihe:iti:xds-b:2007" xmlns:ns5="urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0" status="Super- Thanks for the document" requestId="abc123"/></soap:Body></soap:Envelope>

## Generate client 
The same WSDL available at `api/hello?wsdl` is included as a resource for code generation purposes.  To compile code from the WSDL definition, in `build.gradle`, set the property `wsdl2java.enabled = ` to `true`, then execute:

    ./gradlew ws-client:wsdl2java
    
This will output the compiled classes in `ws-client/generatedsources`, which can then be used to hit a running endpoint.  Suggest copying the outputted classes to a source folder and importing it as a separate library.  This generation only needs to occur when the service interface changes.

For convenience, the classes have been compiled and added as local gradle dependencies in the `lib` folder.  There should be no need to update these.

