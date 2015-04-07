package demo.service.client;

import ihe.iti.xds_b._2007.DocumentRepositoryPortType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientDemo {

    public static void main(String args[]) {
        SpringApplication.run(ClientDemo.class, args);
        demonstrateXdsB();
    }

    private static void demonstrateXdsB() {
        // enable mtom for client
        Map<String,Object> props = new HashMap<String, Object>();
        // Boolean.TRUE or "true" will work as the property value below
        props.put("mtom-enabled", Boolean.TRUE);
     
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setProperties(props);
        // mtom enabled
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
//        DataHandler dataHandler = new DataHandler(new FileDataSource(new File(ClientDemo.class.getResource("/jdk-8u25-macosx-x64.dmg").getPath())));
        
        Document document = new Document();
        document.setId("docId");
        document.setValue(dataHandler);

        requestType.getDocument().add(document);
        SubmitObjectsRequest submitObjects = new SubmitObjectsRequest();
        submitObjects.setId("abc123");
        submitObjects.setComment("This is the first demo request");

        requestType.setSubmitObjectsRequest(submitObjects);
    }
}
