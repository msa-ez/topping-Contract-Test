forEach: Aggregate
fileName: {{nameCamelCase}}Get.groovy
except: {{#attached "Event" this}}{{#checkOutgoing outgoingRelations}}{{/checkOutgoing}}{{/attached}}
---
package contracts.rest

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'GET'
        url ('/{{#attached "Event" this}}{{#outgoingRelations}}{{#target}}{{#aggregateList}}{namePlural}}{{/aggregateList}}{{/target}}{{/outgoingRelations}}{{/attached}}/1')
        headers {
            contentType(applicationJson())
        }
    }
    response {
        status 200
        body(
            {{#attached "Event" this}}
            {{#outgoingRelations}}
            {{#target}}
            {{#examples}}
            {{#then}}
            {{#each value}}
                {{@key}}: {{this}}
            {{/each}}
            {{/then}}
            {{/examples}}
            {{/target}}
            {{/outgoingRelations}}
            {{/attached}}
        )
        bodyMatchers {
            {{#attached "Event" this}}
            {{#outgoingRelations}}
            {{#target}}
            {{#examples}}
            {{#then}}
            {{#each value}}
            jsonPath('$.{{camelCase @key}}', byRegex(nonEmpty()).as{{#setExampleType @key this ../../../aggregateList}}{{/setExampleType}}())
            {{/each}}
            {{/then}}
            {{/examples}}
            {{/target}}
            {{/outgoingRelations}}
            {{/attached}}
        }
        headers {
            contentType(applicationJson())
        }
    }
}

<function>
    window.$HandleBars.registerHelper('checkOutgoing', function (relation) {
        if(!relation) return true;
    })
</function>

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