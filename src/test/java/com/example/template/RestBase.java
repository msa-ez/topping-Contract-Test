forEach: Aggregate
path: {{boundedContext.name}}/src/test/java/com/example/template
except: {{#attached "Event" this}}{{#incomingRelations}}{{#source}}{{#checkIncoming examples}}{{/checkIncoming}}{{/source}}{{/incomingRelations}}{{/attached}}
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
import edittemplate.infra.{{namePascalCase}}Controller;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class RestBase {

    @Autowired
    private {{namePascalCase}}Controller {{nameCamelCase}}Controller;

    @Before
    public void setup() {
        StandaloneMockMvcBuilder standaloneMockMvcBuilder = MockMvcBuilders.standaloneSetup({{nameCamelCase}}Controller);
        RestAssuredMockMvc.standaloneSetup(standaloneMockMvcBuilder);

        
    }
}
<function>
    window.$HandleBars.registerHelper('checkIncoming', function (examples) {
        if(!examples) return true;
    })
</function>