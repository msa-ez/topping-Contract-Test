forEach: Aggregate
fileName: {{#attached "Event" this}}{{#outgoingRelations}}{{#target}}{{#attached "Aggregate" this}}{{namePascalCase}}{{/attached}}{{/target}}{{/outgoingRelations}}{{/attached}}ContractTest.java
path: {{boundedContext.name}}/src/test/java/com/example/template
except: {{#attached "Event" this}}{{#outgoingRelations}}{{#checkTarget target}}{{/checkTarget}}{{/outgoingRelations}}{{/attached}}
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

import {{options.package}}.{{namePascalCase}}Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {{options.package}}.{{namePascalCase}}Application.class)
@AutoConfigureMockMvc
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL, 
                         ids = "{{options.package}}:{{#attached "Event" this}}{{#outgoingRelations}}{{#target}}{{#attached "Aggregate" this}}{{nameCamelCase}}{{/attached}}{{/target}}{{/outgoingRelations}}{{/attached}}:+:stubs:8090")
@ActiveProfiles("test")                      
public class {{#attached "Event" this}}{{#outgoingRelations}}{{#target}}{{#attached "Aggregate" this}}{{namePascalCase}}{{/attached}}{{/target}}{{/outgoingRelations}}{{/attached}}ContractTest {

   @Autowired
   MockMvc mockMvc;

    @Test
    public void get{{#attached "Event" this}}{{#outgoingRelations}}{{#target}}{{#attached "Aggregate" this}}{{namePascalCase}}{{/attached}}{{/target}}{{/outgoingRelations}}{{/attached}}_stub_test() throws Exception {

        MvcResult result = mockMvc
        .perform(MockMvcRequestBuilders.get("/{{nameCamelCase}}/validate{{#attached "Event" this}}{{#outgoingRelations}}{{#target}}{{#attached "Aggregate" this}}{{namePascalCase}}{{/attached}}{{/target}}{{/outgoingRelations}}{{/attached}}/1")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

        String responseString = result.getResponse().getContentAsString();
        DocumentContext parsedJson = JsonPath.parse(responseString);
        // and:
        // examples
        {{#attached "Event" this}}
        {{#outgoingRelations}}
        {{#target}}
        {{#examples}}
        {{#then}}
        {{#each value}}
        Assertions.assertThat(parsedJson.read("$.{{camelCase @key}}", {{#setExampleType @key this ../../../aggregateList ../../../aggregate}}{{/setExampleType}}.class)).{{#setAssertion @key this ../../../aggregateList ../../../aggregate}}{{/setAssertion}}
        {{/each}}
        {{/then}}
        {{/examples}}
        {{/target}}
        {{/outgoingRelations}}
        {{/attached}}
    }

}

<function>
    window.$HandleBars.registerHelper('checkTarget', function (target) {
        if(target.type == 'Policy' && target.examples){
            return true;
        }
    })

    window.$HandleBars.registerHelper('setExampleType', function (key, value, aggregateList, aggregate) {
        var type = 'String'
        if(aggregateList){
            for(var i = 0; i < aggregateList.length; i++){
                for(var j = 0; j< aggregateList[i].aggregateRoot.fieldDescriptors.length; j++){
                    if(aggregateList[i].aggregateRoot.fieldDescriptors[j].name == key){
                        type = aggregateList[i].aggregateRoot.fieldDescriptors[j].className
                    }
                }
            }
        }else if(!aggregateList && aggregate){
            for(var i = 0; i < aggregate.aggregateRoot.fieldDescriptors.length; i++){
                if(aggregate.aggregateRoot.fieldDescriptors[i].name == key){
                    type = aggregate.aggregateRoot.fieldDescriptors[i].className
                }
            }
        }
        return type;
    })

    window.$HandleBars.registerHelper('setAssertion', function (key, value, aggregateList, aggregate) {
        var type = 'String'
        if(aggregateList){
            for(var i = 0; i < aggregateList.length; i++){
                for(var j = 0; j< aggregateList[i].aggregateRoot.fieldDescriptors.length; j++){
                    if(aggregateList[i].aggregateRoot.fieldDescriptors[j].name == key){
                        type = aggregateList[i].aggregateRoot.fieldDescriptors[j].className
                    }
                }
            }
        }else if(!aggregateList && aggregate){
            for(var i = 0; i < aggregate.aggregateRoot.fieldDescriptors.length; i++){
                if(aggregate.aggregateRoot.fieldDescriptors[i].name == key){
                    type = aggregate.aggregateRoot.fieldDescriptors[i].className
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
    })
</function>