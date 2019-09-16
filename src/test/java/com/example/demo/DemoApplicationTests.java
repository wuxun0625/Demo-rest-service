package com.example.demo;

import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest(classes = Application.class)
public class DemoApplicationTests extends CamelTestSupport {

    private final String RESULT_STR = "{\"user\":\"test11\",\"age\":22,\"response\":\"response\"}";

    @Autowired
    private CamelContext context;

    @Autowired
    private ProducerTemplate template;

    @Test
    public void contextLoads() {
        try {
            context.getRouteDefinition("route2").adviceWith(context, new AdviceWithRouteBuilder() {
                @Override
                public void configure() throws Exception {
                    // this.replaceFromWith(startUri);// direct:INT001
                    // this.weaveById("_ubip-demo-rest-2-soap-route-to").replace().to("mock:RESULT_gs_soap");
                    this.weaveById("transformer3").replace().to("mock:RESULT_outbound");
                    //.process(new MyBookProcessor2()).to("mock:RESULT_outbound2");
                }
            });

            template.sendBody("jetty:http://0.0.0.0:8088/myapp/myservice", ExchangePattern.InOut,
                    "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><restRequest><age>22</age><user>test11</user></restRequest>");
            MockEndpoint mock_outbound = context.getEndpoint("mock:RESULT_outbound", MockEndpoint.class);
            List<Exchange> list_exchanges_soap = mock_outbound.getReceivedExchanges();
            assertMockEndpointsSatisfied();
            for (Exchange exchange : list_exchanges_soap) {
                // System.out.println("inputContext : " + exchange.getIn().getBody());
                assertEquals((String) exchange.getIn().getBody(), RESULT_STR);
            }
//	    	HttpMessage httpMessage = exchange.getIn(HttpMessage.class);
//	    	//String name = httpMessage.getRequest().getParameter("name");
//	    	
//	    	InputStream bodyStream = (InputStream) httpMessage.getBody();
//			String inputContext = IOUtils.toString(bodyStream, "UTF-8");
//	//
//			System.out.println("inputContext : " + inputContext);
//			assertMockEndpointsSatisfied();
//			context.getRouteDefinition("route2").getOutputs().get(0).get;
//			List<Exchange> list = mock.getReceivedExchanges();
//			String body1 = list.get(0).getIn().getBody(String.class);
//			String body2 = list.get(1).getIn().getBody(String.class);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
