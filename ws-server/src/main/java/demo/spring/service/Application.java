package demo.spring.service;

import javax.xml.ws.soap.SOAPBinding;

import org.apache.cxf.Bus;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import demo.spring.service.custom.BasicDocRepoServiceImpl;

@Configuration
@EnableAutoConfiguration
@ImportResource({ "classpath:META-INF/cxf/cxf.xml" })
public class Application {

    @Autowired
    private ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // Replaces the need for web.xml
    @Bean
    public ServletRegistrationBean servletRegistrationBean(ApplicationContext context) {
        return new ServletRegistrationBean(new CXFServlet(), "/api/*");
    }

    // Replaces cxf-servlet.xml
    @Bean
    // <jaxws:endpoint id="helloWorld" implementor="demo.spring.service.HelloWorldImpl" address="/HelloWorld"/>
    public EndpointImpl xdsBService() {
        Bus bus = (Bus) applicationContext.getBean(Bus.DEFAULT_BUS_ID);
        Object implementor = new BasicDocRepoServiceImpl();
        EndpointImpl endpoint = new EndpointImpl(bus, implementor);
        endpoint.publish("/xdsb");
        endpoint.getServer().getEndpoint().getInInterceptors().add(new LoggingInInterceptor());
        endpoint.getServer().getEndpoint().getOutInterceptors().add(new LoggingOutInterceptor());
        // Enable MTOM for large attachments
        SOAPBinding binding = (SOAPBinding) endpoint.getBinding();
        binding.setMTOMEnabled(true);
        // mtom enabled
        return endpoint;
    }

    // Might be handy when testing/deploying to standalone tomcat, to keep same addresses
    // @Bean
    // public EmbeddedServletContainerFactory embeddedServletContainerFactory() {
    // TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory("", 8080);
    // return factory;
    // }

}