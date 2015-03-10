package demo.service.client;

import ihe.iti.xds_b._2007.DocumentRepositoryPortType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document;

import java.io.File;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demo.spring.service.HelloWorld;

public class ClientDemo {

    public static void main(String args[]) {
        // demonstrateHelloWorld();
        demonstrateXdsB();

    }

    private static void demonstrateXdsB() {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setAddress("http://localhost:8080/api/xdsb");
        factory.setServiceClass(DocumentRepositoryPortType.class);

        DocumentRepositoryPortType client = (DocumentRepositoryPortType) factory.create();
        ProvideAndRegisterDocumentSetRequestType requestType = new ProvideAndRegisterDocumentSetRequestType();

        constructRequest(requestType);
        RegistryResponseType response = client.documentRepositoryProvideAndRegisterDocumentSetB(requestType);
        System.out.println("Status from server's response: " + response.getStatus());
    }

    private static void constructRequest(ProvideAndRegisterDocumentSetRequestType requestType) {
        DataHandler dataHandler = new DataHandler(new FileDataSource(new File(ClientDemo.class.getResource("/dummy.txt").getPath())));
        Document document = new Document();
        document.setId("docId");
        document.setValue(dataHandler);

        requestType.getDocument().add(document);
        SubmitObjectsRequest submitObjects = new SubmitObjectsRequest();
        submitObjects.setId("abc123");
        submitObjects.setComment("This is the first demo request");

        requestType.setSubmitObjectsRequest(submitObjects);
    }

    private static void demonstrateHelloWorld() {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setAddress("http://localhost:8080/api/hello");
        factory.setServiceClass(HelloWorld.class);

        HelloWorld client = (HelloWorld) factory.create();
        String response = client.sayHi("hi, you beautiful world!");
        System.out.println("Response from server: " + response);
    }
}
