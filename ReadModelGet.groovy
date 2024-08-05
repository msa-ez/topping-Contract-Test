forEach: View
fileName: {{nameCamelCase}}.groovy
path: {{#incomingRelations}}{{#source}}{{boundedContext.name}}{{/source}}{{/incomingRelations}}
except: {{#checkExamples examples}}{{/checkExamples}}
---
package contracts.rest

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'GET'
        url ('/{{aggregate.namePlural}}/search/findBy{{#if queryOption.useDefaultUri}}findBy{{namePascalCase}}{{else}}{{#changeUpper queryOption.apiPath}}{{/changeUpper}}{{/if}}')
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
            jsonPath('$[0].{{camelCase @key}}', byRegex(nonEmpty()).as{{#setExampleType @key this ../../../queryParameters}}{{/setExampleType}}())
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

    window.$HandleBars.registerHelper('setExampleType', function (key, value, parameters) {
        var type = 'String'
        for(var i = 0; i < parameters.length; i++){
            if(parameters[i].name == key){
                type = parameters[i].className
            }
        }
        
        return type;
    })
    window.$HandleBars.registerHelper('changeUpper', function (name) {
        return name.charAt(0).toUpperCase() + name.slice(1);
    });
</function>