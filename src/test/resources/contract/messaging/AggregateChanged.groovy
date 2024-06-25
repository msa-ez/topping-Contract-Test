forEach: Aggregate
fileName: {{namePascalCase}}Changed.groovy
path: {{name}}/src/test/resources/contract/messaging
except: {{#attached "Event" this}}{{#incomingRelations}}{{#checkIncoming source}}{{/checkIncoming}}{{/incomingRelations}}{{/attached}}
---
package contracts.messaging
import org.springframework.cloud.contract.spec.Contract

Contract.make {
    // The Identifier which can be used to identify it later.
    label '{{#attached "Event" this}}{{namePascalCase}}{{/attached}}'
    input {
        // Contract will be triggered by the following method.
        triggeredBy('{{#attached "Event" this}}{{nameCamelCase}}{{/attached}}()')
    }
    outputMessage {
        sentTo 'eventTopic'
        // Consumer Expected Payload spec. that a JSON message must have, 
        // If the Producer-side test is OK, then send the following msg to event-out channel.
        body(
                eventType: "{{#attached "Event" this}}{{namePascalCase}}{{/attached}}",
                {#attached "Event" this}}
                {{#incomingRelations}}
                {{#source}}
                {{#examples}}
                {{#then}}
                {{#each value}}
                    {{@key}}: {{#checkExampleType this}}{{/checkExampleType}},
                {{/each}}
                {{/then}}
                {{/examples}}
                {{/source}}
                {{/incomingRelations}}
                {{/attached}}
                productId: 1,
                productName: "TV",
                productPrice: 10000,
                productStock: 10,
                imageUrl: "tv.jpg"
        )
        bodyMatchers {
            {{#attached "Event" this}}
            {{#outgoingRelations}}
            {{#target}}
            {{#examples}}
            {{#then}}
            {{#each value}}
            jsonPath('$.{{camelCase @key}}', byRegex(nonEmpty()).as{{#setExampleType @key this ../../../aggregateList ../../../aggregate}}{{/setExampleType}}())
            {{/each}}
            {{/then}}
            {{/examples}}
            {{/target}}
            {{/outgoingRelations}}
            {{/attached}}
        }
        headers {
            messagingContentType(applicationJson())
        }
    }
}
<function>
    window.$HandleBars.registerHelper('checkIncoming', function (source) {
        if(!source.aggregateList || !source.examples) return true;
    })
    window.$HandleBars.registerHelper('checkExampleType', function (value) {
        if (/^\d+$/.test(value)) {
            return value.replace(/"/g, ""); 
        }
        return value;
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
</function>

