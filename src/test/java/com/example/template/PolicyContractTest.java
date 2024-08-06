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

        {{#examples}}
        {{#when}}
        {{#each value}}
        assertThat(parsedJson.read("$.{{#camelCase @key}}{{/camelCase}}", {{#setExampleType @key this  ../../../aggregateList}}{{/setExampleType}}.class)).{{#setAssertion @key this  ../../../aggregateList}}{{/setAssertion}}
        {{/each}}
        {{/when}}
        {{/examples}}

    }
}
<function>
    window.$HandleBars.registerHelper('checkExample', function (example) {
        if(example) return false;
    });

    window.$HandleBars.registerHelper('setExampleType', function (key, value, aggregateList) {
        var type = 'String'
        for(var i = 0; i < aggregateList.length; i++){
            for(var j = 0 j < aggregateList[i].aggregateRoot.fieldDescriptors.length; j++){
                if(aggregateList[i].aggregateRoot.fieldDescriptors[j].name == key){
                    type = aggregateList[i].aggregateRoot.fieldDescriptors[j].className
                }
            }
        }
        return type;
    });

    window.$HandleBars.registerHelper('setAssertion', function (key, value, aggregate) {
        var type = 'String'
        
        for(var i = 0; i < aggregateList.length; i++){
            for(var j = 0 j < aggregateList[i].aggregateRoot.fieldDescriptors.length; j++){
                if(aggregateList[i].aggregateRoot.fieldDescriptors[j].name == key){
                    type = aggregateList[i].aggregateRoot.fieldDescriptors[j].className
                }
            }
        }

        switch (type) {
            case 'String':
                return 'matches("[\\S\\s]+");'
            case 'Long':
                return 'isGreaterThan(0L);'
            case 'Integer':
                return 'isGreaterThan(0);'
            case 'Boolean':
            return 'isTrue();'
            default:
            throw new Error(`Unsupported type: ${type}`);
        }
    });
</function>

