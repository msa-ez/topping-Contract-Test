forEach: ReadModel
fileName: {{nameCamelCase}}Get.groovy
except: {{^incoming "Command" this}}{{/incoming}}
---
package contracts.rest

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'GET'
        url ('/{{#attached "Command" this}}{{#outgoingRelations}}{{#target}}{{queryOption.apiPath}}{{/target}}{{/outgoingRelations}}{{/attached}}/1')
        headers {
            contentType(applicationJsonUtf8())
        }
        body(
            {{#examples}}
            {{#when}}
            {{#each value}}
                {{@key}}: {{this}},
            {{/each}}
            {{/when}}
            {{/examples}}
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
            jsonPath('$.{{camelCase @key}}', byRegex(nonEmpty()).as{{#setExampleType @key this ../../../aggregateList ../../../aggregate}}{{/setExampleType}}())
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
    window.$HandleBars.registerHelper('checkTarget', function (target) {
    if(!target) return true;
    if(target.type == 'View' || target.type == 'ReadModel'){
        return false;
    }
    return true;
})

    window.$HandleBars.registerHelper('setExampleType', function (key, value, aggregateList, aggregate) {
        var type = 'String'
        if(aggregateList){
            for(var i = 0; i < aggregateList.length; i++){
                for(var j = 0; j< aggregateList[i].aggregateRoot.fieldDescriptors.length; j++){
                    if(aggregateList[i].aggregateRoot.fieldDescriptors[j].name == key){
                        type = aggregateList[i].aggregateRoot.fieldDescriptors[j].className
                    }
                }
                
            }
        }else if(!aggregateList && aggregate){
            for(var i = 0; i < aggregate.aggregateRoot.fieldDescriptors.length; i++){
                if(aggregate.aggregateRoot.fieldDescriptors[i].name == key){
                    type = aggregate.aggregateRoot.fieldDescriptors[i].className
                }
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