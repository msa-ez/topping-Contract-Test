forEach: Command
fileName: {{aggregate.namePascalCase}}ContractTest.java
path: {{#incomingRelations}}{{#source}}{{boundedContext.name}}{{/source}}{{/incomingRelations}}/src/test/java/com/example/template
except: {{#checkIncoming examples incomingRelations}}{{/checkIncoming}}
---
package com.example.template;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import {{options.package}}.{{#incomingRelations}}{{#source}}{{aggregate.namePascalCase}}{{/source}}{{/incomingRelations}}Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {{options.package}}.{{#incomingRelations}}{{#source}}{{aggregate.namePascalCase}}{{/source}}{{/incomingRelations}}Application.class)
@AutoConfigureMockMvc
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL, 
                         ids = "{{options.package}}:{{aggregate.nameCamelCase}}:+:stubs:8090")
@ActiveProfiles("test")                      
public class {{aggregate.namePascalCase}}ContractTest {

   @Autowired
   MockMvc mockMvc;

    @Test
    public void get{{aggregate.namePascalCase}}_stub_test() throws Exception {

        MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders.get("/{{#incomingRelations}}{{#source}}{{aggregate.nameCamelCase}}{{/source}}{{/incomingRelations}}/validate{{aggregate.namePascalCase}}/1")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

        String responseString = result.getResponse().getContentAsString();
        DocumentContext parsedJson = JsonPath.parse(responseString);
        // and:
        // examples
        {{#examples}}
        {{#then}}
        {{#each value}}
        Assertions.assertThat(parsedJson.read("$.{{camelCase @key}}", {{#setExampleType @key this  ../../../aggregate}}{{/setExampleType}}.class)).{{#setAssertion @key this  ../../../aggregate}}{{/setAssertion}}
        {{/each}}
        {{/then}}
        {{/examples}}
    }

}

<function>
    window.$HandleBars.registerHelper('checkIncoming', function (example, incoming) {
        if(incoming){
            for(var i = 0; i < incoming.length; i++){
                if(example && incoming[i].source.type == 'Event'){
                    return false;
                }else{
                    return true;
                }
            }
        }else{
            return true;
        }
    })

    window.$HandleBars.registerHelper('setExampleType', function (key, value, aggregate) {
        var type = 'String'
        for(var i = 0; i < aggregate.aggregateRoot.fieldDescriptors.length; i++){
            if(aggregate.aggregateRoot.fieldDescriptors[i].name == key){
                type = aggregate.aggregateRoot.fieldDescriptors[i].className
            }
        }
        return type;
    })

    window.$HandleBars.registerHelper('setAssertion', function (key, value, aggregate) {
        var type = 'String'
        
        for(var i = 0; i < aggregate.aggregateRoot.fieldDescriptors.length; i++){
            if(aggregate.aggregateRoot.fieldDescriptors[i].name == key){
                type = aggregate.aggregateRoot.fieldDescriptors[i].className
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
    })
</function>