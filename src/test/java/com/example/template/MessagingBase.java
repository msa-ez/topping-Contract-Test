forEach: Policy
except: {{#checkExample examples}}{{/checkExample}}
path: {{#incoming "Event" this}}{{aggregate.nameCamelCase}}{{/incoming}}/src/test/java/com/example/template
---
package com.example.template;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifier;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MimeTypeUtils;
import {{options.package}}.{{namePascalCase}}Application;
import {{options.package}}.infra.{{namePascalCase}}Controller;
import {{options.package}}.domain.*;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class) 
@SpringBootTest(classes = {{#incoming "Event" this}}{{aggregate.namePascalCase}}{{/incoming}}Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureMessageVerifier
public abstract class MessagingBase {

    private Logger logger = LoggerFactory.getLogger(MessagingBase.class);

    @Autowired
    {{#incoming "Event" this}}{{aggregate.namePascalCase}}Controller {{aggregate.nameCamelCase}}Controller;{{/incoming}}

    @Autowired
    // Message interface to verify Contracts between services.
    MessageVerifier<Message<?>> messaging;

    @Before
    public void setup() {
        // any remaining messages on the "eventTopic" channel are cleared
        // makes that each test starts with a clean slate
        this.messaging.receive("eventTopic", 100, TimeUnit.MILLISECONDS);
    }

    public void {{#incoming "Event" this}}{{nameCamelCase}}{{/incoming}}() {
        {{#incoming "Event" this}}
        {{#aggregate}}
        {{namePascalCase}} {{nameCamelCase}} = new {{namePascalCase}}();

        {{#../../examples}}
        {{#when}}
        {{#each value}}
        {{#setAggExample @key this ../../../../../incomingRelations}}{{/setAggExample}};
        {{/each}}
        {{/when}}
        {{/../../examples}}

        {{../namePascalCase}} {{../nameCamelCase}} = new {{../namePascalCase}}({{nameCamelCase}});
            
        serializedJson = {{../nameCamelCase}}.toJson();
        {{/aggregate}}
        {{/incoming}}

        logger.info("Sending message: {}", serializedJson);
        this.messaging.send(MessageBuilder
                .withPayload(serializedJson)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build(), "eventTopic");
    }
}
<function>
    window.$HandleBars.registerHelper('checkExample', function (examples) {
        if(examples) return false;
    })
    window.$HandleBars.registerHelper('setAggExample', function (key, value, incoming) {
        var examplesType = 'String';
        var doubleQuote = "\"";
        var aggName = null;
        for(var i = 0; i < incoming.length; i++){
            aggName = incoming[i].source.aggregate.nameCamelCase + ".set" + key.substring(0, 1).toUpperCase() + key.substring(1)
            for(var j = 0; j< incoming[i].source.aggregate.aggregateRoot.fieldDescriptors.length; j++){
                if(incoming[i].source.aggregate.aggregateRoot.fieldDescriptors[j].name == key){
                    examplesType = incoming[i].source.aggregate.aggregateRoot.fieldDescriptors[j].className
                }
            }
        }
        
        switch (examplesType) {
            case 'String':
            return aggName + '(' + doubleQuote + value + doubleQuote + ')'; 
            case 'Long':
                return aggName + '(' + `${value}L` + ')'; 
            case 'Integer':
                return aggName + '(' + `${value}` + ')'; 
            case 'Boolean':
            return aggName + '(' + value.toString() + ')';
            case 'Object':
            if (value instanceof Date) {
                return `new Date(${value.getTime()}L)`; 
            } else if (value === null) {
                return aggName + '(' + 'null' + ')';
            } else if (Array.isArray(value)) {
                // 배열의 경우 더 복잡한 로직이 필요할 수 있으며, 이는 예시로만 제공됩니다.
                const elements = value.map((element) => convertToJavaSyntax(element)).join(', ');
                return `new Object[]{${elements}}`; // Object 배열로 간주합니다.
            } else {
                // 다른 종류의 객체에 대한 처리가 필요할 수 있습니다.
                // 이 경우 해당 객체를 적절한 Java 표현으로 변환하는 로직이 필요합니다.
                return value.toString(); // 기본적인 toString 반환을 사용합니다.
            }
            default:
            throw new Error(`Unsupported type: ${examplesType}`);
        }
    })
    window.$HandleBars.registerHelper('setExampleType', function (key, value, incoming) {
        var type = 'String'
        for(var i = 0; i < incoming.length; i++){
            for(var j = 0; j< incoming[i].source.aggregate.aggregateRoot.fieldDescriptors.length; j++){
                if(incoming[i].source.aggregate.aggregateRoot.fieldDescriptors[j].name == key){
                    type = incoming[i].source.aggregate.aggregateRoot.fieldDescriptors[j].className
                }
            }
        }
        return type;
    })
</function>