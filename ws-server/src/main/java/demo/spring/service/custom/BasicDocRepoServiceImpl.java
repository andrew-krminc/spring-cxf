package demo.spring.service.custom;

import java.io.ByteArrayInputStream;

import ihe.iti.xds_b._2007.DocumentRepository_Port_Soap12Impl;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.apache.cxf.helpers.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicDocRepoServiceImpl extends DocumentRepository_Port_Soap12Impl {

    private static final Logger log = LoggerFactory.getLogger(BasicDocRepoServiceImpl.class);

    @Override
    public RegistryResponseType documentRepositoryProvideAndRegisterDocumentSetB(ProvideAndRegisterDocumentSetRequestType document) {
        log.info("Acknowledge receipt of document!");

        document.getDocument().forEach(doc -> {
            log.info("...document found with id: " + doc.getId());
            try {
                Object content = doc.getValue().getContent();
                if (content instanceof ByteArrayInputStream) {
                    String contents = IOUtils.toString((ByteArrayInputStream) content);
                    log.info("Retrieved contents of file: " + contents);
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            log.info(".......and " + doc.getValue());
        });

        RegistryResponseType response = new RegistryResponseType();
        response.setRequestId(document.getSubmitObjectsRequest().getId());
        response.setStatus("Super- Thanks for the document");
        return response;
    }
}
