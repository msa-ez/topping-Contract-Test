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
            {{#attached "Event" this}}
            {{#incomingRelations}}
            {{#source}}
            {{#examples}}
            {{#given}}
            {{#each value}}
            {{../../../../../../nameCamelCase}}.set{{pascalCase @key}}({{#compareAndSetType @key this ../../../../../../aggregateRoot.fieldDescriptors}}{{/compareAndSetType}})
            {{/each}}
            {{/given}}
            {{/examples}}
            {{/source}}
            {{/incomingRelations}}
            {{/attached}}
            repository.save({{nameCamelCase}});
        };
    }
}

<function>
    window.$HandleBars.registerHelper('checkIncoming', function (examples) {
        if(!examples) return true;
    })

    window.$HandleBars.registerHelper('compareAndSetType', function (key, value, field) {
        var type = 'String'
        for(var i = 0; i < field.length; i++){
            if(field[i].name == key){
                type = field[i].className
            }
        }
        switch (type) {
            case 'String':
                return `"${value}"`; // Java에서 문자열은 큰따옴표를 사용합니다.
            case 'Long':
                return `${value}L`;
            case 'Integer':
                return `${value}`;
            case 'Boolean':
            return value.toString();
            default:
            throw new Error(`Unsupported type: ${type}`);
        }
    })
</function>