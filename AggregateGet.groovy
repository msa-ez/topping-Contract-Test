forEach: Command
fileName: {{nameCamelCase}}Get.groovy
path: {{#incomingRelations}}{{#source}}{{/source}{{/incomingRelations}}
except: {{#checkExample examples}}{{/checkExample}}
---
package contracts.rest

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method '{{controllerInfo.method}}'
        url ('/{{aggregate.namePlural}}{{#checkExtendVerbType controller.method controllerInfo.apiPath}}{{/checkExtendVerbType}}')
        headers {
            contentType(applicationJsonUtf8())
        }
        body(
            {{#attached "Event" this}}
            {{#outgoingRelations}}
            {{#target}}
            {{#examples}}
            {{#when}}
            {{#each value}}
                {{@key}}: {{this}},
            {{/each}}
            {{/when}}
            {{/examples}}
            {{/target}}
            {{/outgoingRelations}}
            {{/attached}}
        )
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
            jsonPath('$.{{camelCase @key}}', byRegex(nonEmpty()).as{{#setExampleType @key this aggregate}}{{/setExampleType}}())
            {{/each}}
            {{/then}}
            {{/examples}}
        }
        headers {
            contentType(applicationJsonUtf8())
        }
    }
}


<function>
    window.$HandleBars.registerHelper('checkExample', function (examples) {
        if(examples){
            return false;
        }
        return true;
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

    window.$HandleBars.registerHelper('checkExtendVerbType', function (type, path) {
        if(type == 'POST'){
            return path;
        }else{
            return '/1/'+ path;
        }
    })
</function>