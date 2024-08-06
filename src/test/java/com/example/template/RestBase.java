forEach: Aggregate
fileName: RestBase.java
except: {{#if commands}}{{#attached "Command" this}}{{#checkExample examples type}}{{/checkExample}}{{/attached}}{{else}}{{#attached "View" this}}{{#checkExample examples type}}{{/checkExample}}{{/attached}}{{/if}}
---
package com.example.template;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.web.filter.CharacterEncodingFilter;

import {{options.package}}.infra.{{namePascalCase}}Controller;
import {{options.package}}.{{namePascalCase}}Application;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = {{namePascalCase}}Application.class)
@AutoConfigureMockMvc
@Import(TestDataConfig.class)
public class RestBase {

    @Autowired
    private {{namePascalCase}}Controller {{nameCamelCase}}Controller;

    @Before
    public void setup() {
        StandaloneMockMvcBuilder standaloneMockMvcBuilder = MockMvcBuilders.standaloneSetup(
            {{nameCamelCase}}Controller
        ).addFilters(new CharacterEncodingFilter("UTF-8", true)); 

        RestAssuredMockMvc.standaloneSetup(standaloneMockMvcBuilder);
        RestAssuredMockMvc.given().contentType("application/json;charset=UTF-8"); 
    }
}
<function>
    window.$HandleBars.registerHelper('checkExample', function (example, type) {
        if(example && type != 'Policy'){
            return false;
        }else if(!example || type == "Policy"){
                return true;
        }
    });
</function>