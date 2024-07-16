forEach: Aggregate
path: {{boundedContext.name}}/src/test/java/com/example/template
except: {{#if commands}}{{#attached "Command" this}}{{#checkExample examples type}}{{/checkExample}}{{/attached}}{{else}}{{#attached "View" this}}{{#checkExample examples type}}{{/checkExample}}{{/attached}}{{/if}}
---
package com.example.template;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import {{options.package}}.domain.{{aggregate.namePascalCase}};
import {{options.package}}.domain.{{aggregate.namePascalCase}}Repository;


@TestConfiguration
public class TestDataConfig {

    @Bean
    public CommandLineRunner initData({{aggregate.namePascalCase}}Repository repository) {
        return args -> {
            {{aggregate.namePascalCase}} {{aggregate.nameCamelCase}} = new {{aggregate.namePascalCase}}();
            {{#examples}}
            {{#given}}
            {{#each value}}
            {{../../../aggregate.nameCamelCase}}.set{{pascalCase @key}}({{#compareAndSetType @key this ../../../aggregate.aggregateRoot.fieldDescriptors}}{{/compareAndSetType}});
            {{/each}}
            {{/given}}
            {{/examples}}
            repository.save({{aggregate.nameCamelCase}});
        };
    }
}

<function>
    window.$HandleBars.registerHelper('checkExample', function (example, type) {
        if(exampe && type != 'Policy'){
            return false;
        }else{
            return true;
        }
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