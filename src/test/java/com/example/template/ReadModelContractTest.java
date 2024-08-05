forEach: View
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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

   @Autowired
    RestTemplate restTemplate;

    @Test
    public void get{{aggregate.namePascalCase}}_stub_test() throws Exception {

        String url = "http://localhost:8090/{{aggregate.namePlural}}/search/findBy{{#if queryOption.useDefaultUri}}findBy{{namePascalCase}}{{else}}{{#changeUpper queryOption.apiPath}}{{/changeUpper}}{{/if}}/1";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
            url, 
            HttpMethod.GET, 
            entity, 
            String.class
        );
        String responseString = response.getBody();
        DocumentContext parsedJson = JsonPath.parse(responseString);
        // and:
        // examples
        {{#examples}}
        {{#then}}
        {{#each value}}
        Assertions.assertThat(parsedJson.read("$.{{camelCase @key}}", {{#setExampleType @key this  ../../../queryParameters}}{{/setExampleType}}.class)).{{#setAssertion @key this  ../../../queryParameters}}{{/setAssertion}}
        {{/each}}
        {{/then}}
        {{/examples}}
    }

}

<function>
    window.$HandleBars.registerHelper('checkIncoming', function (example, incoming) {
        if(incoming){
            for(var i = 0; i < incoming.length; i++){
                if(example && incoming[i].source.type == 'Command'){
                    return false;
                }else{
                    return true;
                }
            }
        }else{
            return true;
        }
    })

    window.$HandleBars.registerHelper('setExampleType', function (key, value, parameters) {
        var type = 'String'
        for(var i = 0; i < parameters.length; i++){
            if(parameters[i].name == key){
                type = parameters[i].className
            }
        }
        return type;
    })

    window.$HandleBars.registerHelper('setAssertion', function (key, value, parameters) {
        var type = 'String'
        
        for(var i = 0; i < parameters.length; i++){
            if(parameters[i].name == key){
                type = parameters[i].className
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
    window.$HandleBars.registerHelper('changeUpper', function (name) {
        return name.charAt(0).toUpperCase() + name.slice(1);
    });
</function>