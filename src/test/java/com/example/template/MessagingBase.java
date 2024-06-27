forEach: Policy
except: {{#checkExample examples}}{{/checkExample}}
path: {{#incoming "Event" this}}{{aggregate.nameCamelCase}}{{/incoming}}/src/test/java/com/example/template
---
package com.example.template;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifier;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MimeTypeUtils;
import {{options.package}}.{{namePascalCase}}Application;
import {{options.package}}.infra.{{namePascalCase}}Controller;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class) 
@SpringBootTest(classes = {{#incoming "Event" this}}{{aggregate.namePascalCase}}{{/incoming}}Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureMessageVerifier
public abstract class MessagingBase {

    @Autowired
    {{#incoming "Event" this}}{{aggregate.namePascalCase}}Controller {{aggregate.nameCamelCase}}Controller;{{/incoming}}

    @Autowired
    // Message interface to verify Contracts between services.
    MessageVerifier messaging;

    @Before
    public void setup() {
        // any remaining messages on the "eventTopic" channel are cleared
        // makes that each test starts with a clean slate
        this.messaging.receive("eventTopic", 100, TimeUnit.MILLISECONDS);
    }

    public void {{#incoming "Event" this}}{{namePascalCase}}{{/incoming}}() {
        {{#incoming "Event" this}}
        {{#aggregate}}
        {{namePascalCase}} {{nameCamelCase}} = new {{namePascalCase}}();

        {{#aggregateRoot.fieldDescriptors}}
        {{#../../../examples}}
        {{#when}}
        {{../../../nameCamelCase}}.set{{../../namePascalCase}}({{#checkExampleType value ../../../../../incomingRelations}}{{/checkExampleType}});
        {{/when}}
        {{/../../../examples}}
        {{/aggregateRoot.fieldDescriptors}}

        {{../namePascalCase}} {{../nameCamelCase}} = new {{../namePascalCase}}({{nameCamelCase}});
        // orderPlaced.setEventType("OrderPlaced");
            
        serializedJson = {{../nameCamelCase}}.toJson();
        {{/aggregate}}
        {{/incoming}}

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
    window.$HandleBars.registerHelper('checkExampleType', function (value, incoming) {
        var type = 'String';
        var quote = "'";
        var result = {};
    
        Object.keys(value).forEach(function(key) {
            for(var i = 0; i < incoming.length; i++){
                for(var j = 0; j < incoming[i].source.aggregate.aggregateRoot.fieldDescriptors.length; j++){
                    if(incoming[i].source.aggregate.aggregateRoot.fieldDescriptors[j].name == key){
                        type = incoming[i].source.aggregate.aggregateRoot.fieldDescriptors[j].className;
                        break;
                    }
                }
            }
    
            switch (type) {
                case 'String':
                    result[key] = quote + value[key] + quote; // Java에서 문자열은 큰따옴표를 사용합니다.
                    break;
                case 'Long':
                    result[key] = `${value[key]}L`; // long 타입으로 간주할 수 있습니다.
                    break;
                case 'Integer':
                    result[key] = `${value[key]}`;
                    break;
                case 'Boolean':
                    result[key] = value[key].toString();
                    break;
                case 'Object':
                    if (value[key] instanceof Date) {
                        result[key] = `new Date(${value[key].getTime()}L)`; // Java의 Date 생성자를 사용합니다.
                    } else if (value[key] === null) {
                        result[key] = 'null';
                    } else if (Array.isArray(value[key])) {
                        const elements = value[key].map((element) => convertToJavaSyntax(element)).join(', ');
                        result[key] = `new Object[]{${elements}}`; // Object 배열로 간주합니다.
                    } else if (typeof value[key] === 'object') {
                        // 객체를 JSON 문자열로 변환
                        result[key] = JSON.stringify(value[key]);
                    } else {
                        result[key] = value[key].toString(); // 기본적인 toString 반환을 사용합니다.
                    }
                    break;
                default:
                    throw new Error(`Unsupported type: ${type}`);
            }
        });
    
        return result;
    });
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