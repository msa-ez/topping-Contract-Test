forEach: Policy
fileName: {{#incomingRelations}}{{#source}}{{namePascalCase}}{{/source}}{{/incomingRelations}}ContractTest.java
path: {{boundedContext.name}}/src/test/java/com/example/template
except: {{#checkExample examples}}{{/checkExample}}
---
package com.example.template;

import {{options.package}}.{{#aggregateList}}{{namePascalCase}}{{/aggregateList}}Application;
import {{options.package}}.config.kafka.KafkaProcessor;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.StubFinder;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.messaging.Message;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {{#aggregateList}}{{namePascalCase}}{{/aggregateList}}Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL, 
                        ids = "{{optiions.package}}:{{#incomingRelations}}{{#source}}{{aggregate.nameCamelCase}}{{/source}}{{/incomingRelations}}:+:stubs")
public class {{#incomingRelations}}{{#source}}{{namePascalCase}}{{/source}}{{/incomingRelations}}ContactTest {

    @Autowired
    StubFinder stubFinder;

    @Autowired
    private KafkaProcessor processor;

    @Autowired
    // collect messages sent by a Spring Cloud Stream application
    private MessageCollector messageCollector;

    @Test
    public void testOnMessageReceived() {
        // Set Contract Label
        stubFinder.trigger("{{#incomingRelations}}{{#source}}{{namePascalCase}}{{/source}}{{/incomingRelations}}");

        Message<String> received = (Message<String>) messageCollector.forChannel(processor.outboundTopic()).poll();
        System.out.println("<<<<<<< " + received.getPayload() + ">>>>>>");

        DocumentContext parsedJson = JsonPath.parse(received.getPayload());

        // Verify received message
        assertThat(parsedJson.read("$.eventType", String.class)).isEqualTo("OrderPlaced");
        assertThat(parsedJson.read("$.id", Long.class)).isGreaterThan(0L);
        assertThat(parsedJson.read("$.customerId", String.class)).matches("[\\S\\s]+");
        assertThat(parsedJson.read("$.productId", String.class)).matches("[\\S\\s]+");
        assertThat(parsedJson.read("$.productName", String.class)).matches("[\\S\\s]+");
        assertThat(parsedJson.read("$.qty", Integer.class)).isGreaterThan(0);

    }
}
<function>
    window.$HandleBars.registerHelper('checkExample', function (example) {
        if(example) return false;
    })
</function>

