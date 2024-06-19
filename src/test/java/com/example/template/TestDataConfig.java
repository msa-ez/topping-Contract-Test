forEach: Aggregate
path:  {{boundedContext.name}}/src/test/java/com/example/template
except: {{#attached "Event" this}}{{#incomingRelations}}{{#source}}{{#checkIncoming examples}}{{/checkIncoming}}{{/source}}{{/incomingRelations}}{{/attached}}
---
package com.example.template;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import industrialaccident.domain.{{namePascalCase}};
import industrialaccident.domain.{{namePascalCase}}Repository;


@TestConfiguration
public class TestDataConfig {

    @Bean
    public CommandLineRunner initData({{namePascalCase}}Repository repository) {
        return args -> {
            {{namePascalCase}} {{nameCamelCase}} = new {{namePascalCase}}();     
            repository.save(sickLeave);
        };
    }
}

<function>
    window.$HandleBars.registerHelper('checkIncoming', function (examples) {
        if(!examples) return true;
    })
</function>