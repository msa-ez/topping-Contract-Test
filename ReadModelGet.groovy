forEach: View
fileName: {{nameCamelCase}}.groovy
path: {{#incomingRelations}}{{#source}}{{boundedContext.name}}{{/source}}{{/incomingRelations}}
except: {{#checkExamples examples}}{{/checkExamples}}
---
package contracts.rest

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'GET'
        url ('/{{aggregate.namePlural}}/search/findBy{{#if useDefaultUri}}{{queryOption.apiPath}}{{else}}{{namePascalCase}}{{/if}}')
        headers {
            contentType(applicationJson())
        }
    }
    response {
        status 200
        body(
            {{#examples}}
            {{#then}}
            {{#each value}}
                {{@key}}: {{this}},
            {{/each}}
            {{/then}}
            {{/examples}}
        )
        bodyMatchers {
            {{#examples}}
            {{#then}}
            {{#each value}}
            jsonPath('$[0].{{camelCase @key}}', byRegex(nonEmpty()).as{{#setExampleType @key this ../../../aggregate}}{{/setExampleType}}())
            {{/each}}
            {{/then}}
            {{/examples}}
        }
        headers {
            contentType(applicationJson())
        }
    }
}

<function>
    window.$HandleBars.registerHelper('checkExamples', function (examples) {
        if(examples) return false;
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
</function>