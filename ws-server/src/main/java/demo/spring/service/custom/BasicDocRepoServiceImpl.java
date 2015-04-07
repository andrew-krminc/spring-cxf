package demo.spring.service.custom;

import ihe.iti.xds_b._2007.DocumentRepository_Port_Soap12Impl;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.apache.commons.io.FileUtils;
import org.apache.cxf.helpers.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicDocRepoServiceImpl extends DocumentRepository_Port_Soap12Impl {

    private static final Logger log = LoggerFactory.getLogger(BasicDocRepoServiceImpl.class);

    @Override
    public RegistryResponseType documentRepositoryProvideAndRegisterDocumentSetB(ProvideAndRegisterDocumentSetRequestType document) {
        log.info("Acknowledge receipt of document!");

        for (Document doc : document.getDocument()) {
            log.info("...document found with id: " + doc.getId());

            try {
                Object content = doc.getValue().getContent();
                if (content instanceof ByteArrayInputStream) {
                    String contents = IOUtils.toString((ByteArrayInputStream) content);
                    log.info("Retrieved contents of file: " + contents);
                    writeContentsAsFile(doc.getId(), contents);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            log.info(".......and " + doc.getValue());
        }

        RegistryResponseType response = new RegistryResponseType();
        response.setRequestId(document.getSubmitObjectsRequest().getId());
        response.setStatus("Super- Thanks for the document");
        return response;
    }

    private void writeContentsAsFile(String docId, String contents) {
        File temp;
        try {
            temp = File.createTempFile(docId, ".txt");
            FileUtils.writeStringToFile(temp, contents, "UTF-8");
            String path = temp.getAbsolutePath();
            log.info("Stored file with id: " + docId + " to: " + path);
        } catch (IOException e) {
            log.error("Unable to write to file with id: " + docId + ": " + e.getMessage(), e);
        }

    }

    @Override
    public RetrieveDocumentSetResponseType documentRepositoryRetrieveDocumentSet(RetrieveDocumentSetRequestType arg0) {
        log.info("Got to the 'retrieve' method");
        // TODO: return the document from the hashmap here, based on the input parameters
        return super.documentRepositoryRetrieveDocumentSet(arg0);
    }
}