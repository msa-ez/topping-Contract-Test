forEach: Aggregate
fileName: {{#attached "Event" this}}{{#outgoingRelations}}{{#target}}{{#attached "Aggregate" this}}{{namePascalCase}}{{/attached}}{{/target}}{{/outgoingRelations}}{{/attached}}ContractTest.java
path: {{boundedContext.name}}/src/test/java/com/example/template
except: {{#attached "Event" this}}{{#checkOutgoing outgoingRelations}}{{/checkOutgoing}}{{/attached}}
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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@AutoConfigureStubRunner(stubsMode = StubRunnerProperties.StubsMode.LOCAL, 
                         ids = "com.example:{{#attached "Event" this}}{{#outgoingRelations}}{{#target}}{{#attached "Aggregate" this}}{{namePlural}}{{/attached}}{{/target}}{{/outgoingRelations}}{{/attached}}:+:stubs:8090")
@ActiveProfiles("test")                      
public class {{#attached "Event" this}}{{#outgoingRelations}}{{#target}}{{#attached "Aggregate" this}}{{namePascalCase}}{{/attached}}{{/target}}{{/outgoingRelations}}{{/attached}}ContractTest {

   @Autowired
   MockMvc mockMvc;

    @Test
    public void getProduct_stub_test() throws Exception {

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
        Assertions.assertThat(parsedJson.read("$.{{camelCase @key}}", {{#setExampleType @key this ../../../aggregateList}}{{/setExampleType}}()).class)).{{#setAssertion @key this ../../../aggregateList}}{{/setAssertion}}
        {{/each}}
        {{/then}}
        {{/examples}}
        {{/target}}
        {{/outgoingRelations}}
        {{/attached}}
    }

}

<function>
    window.$HandleBars.registerHelper('checkOutgoing', function (relation) {
        if(!relation) return true;
    })

    window.$HandleBars.registerHelper('setExampleType', function (key, value, field) {
        var type = 'String'
        for(var i = 0; i < field.length; i++){
            for(var j = 0; j< field[i].aggregateRoot.fieldDescriptors.length; j++){
                if(field[i].aggregateRoot.fieldDescriptors[j].name == key){
                    type = field[i].aggregateRoot.fieldDescriptors[j].className
                }
            }
            
        }
        return type;
    })

    window.$HandleBars.registerHelper('setAssertion', function (key, value, field) {
        var type = 'String'
        for(var i = 0; i < field.length; i++){
            for(var j = 0; j< field[i].aggregateRoot.fieldDescriptors.length; j++){
                if(field[i].aggregateRoot.fieldDescriptors[j].name == key){
                    type = field[i].aggregateRoot.fieldDescriptors[j].className
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