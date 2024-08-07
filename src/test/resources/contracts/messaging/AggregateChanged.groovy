forEach: Policy
fileName: {{#incoming "Event" this}}{{namePascalCase}}{{/incoming}}.groovy
except: {{#checkExample examples}}{{/checkExample}}
path: {{#aggregateList}}{{nameCamelCase}}{{/aggregateList}}/src/test/resources/contracts/messaging
---
package contracts.messaging
import org.springframework.cloud.contract.spec.Contract

Contract.make {
    // The Identifier which can be used to identify it later.
    label '{{#incoming "Event" this}}{{namePascalCase}}{{/incoming}}'
    input {
        // Contract will be triggered by the following method.
        triggeredBy('{{#incoming "Event" this}}{{nameCamelCase}}{{/incoming}}()')
    }
    outputMessage {
        sentTo 'eventTopic'
        // Consumer Expected Payload spec. that a JSON message must have, 
        // If the Producer-side test is OK, then send the following msg to event-out channel.
        body(
            eventType: "{{#incoming "Event" this}}{{namePascalCase}}{{/incoming}}",
            {{#examples}}
            {{#when}}
            {{#each value}}
                {{@key}}: {{#checkExampleType @key this ../../../incomingRelations}}{{/checkExampleType}},
            {{/each}}
            {{/when}}
            {{/examples}}
        )
        bodyMatchers {
            {{#examples}}
            {{#when}}
            {{#each value}}
            jsonPath('$.{{camelCase @key}}', byRegex(nonEmpty()).as{{#setExampleType @key this ../../../incomingRelations}}{{/setExampleType}}())
            {{/each}}
            {{/when}}
            {{/examples}}
        }
        headers {
            messagingContentType(applicationJson())
        }
    }
}
<function>
    window.$HandleBars.registerHelper('checkExample', function (examples) {
        if(examples) return false;
    })
    window.$HandleBars.registerHelper('checkExampleType', function (key, value, incoming) {
        var type = 'String'
        var doubleQuote = "\"";
        for(var i = 0; i < incoming.length; i++){
            for(var j = 0; j< incoming[i].source.aggregate.aggregateRoot.fieldDescriptors.length; j++){
                if(incoming[i].source.aggregate.aggregateRoot.fieldDescriptors[j].name == key){
                    type = incoming[i].source.aggregate.aggregateRoot.fieldDescriptors[j].className
                }
            }
        }
        switch (type) {
            case 'String':
            return doubleQuote + value + doubleQuote; // Java에서 문자열은 큰따옴표를 사용합니다.
            case 'Long':
            // JavaScript의 숫자는 정수 또는 부동소수점일 수 있으므로 이를 구분해야 할 수도 있습니다.
                return `${value}`; // long 타입으로 간주할 수 있습니다.
            case 'Integer':
                return `${value}`; 
            case 'Boolean':
            return value.toString();
            case 'Object':
            if (value instanceof Date) {
                return `new Date(${value.getTime()}L)`; // Java의 Date 생성자를 사용합니다.
            } else if (value === null) {
                return 'null';
            } else if (Array.isArray(value)) {
                // 배열의 경우 더 복잡한 로직이 필요할 수 있으며, 이는 예시로만 제공됩니다.
                const elements = value.map((element) => convertToJavaSyntax(element)).join(', ');
                return `new Object[]{${elements}}`; // Object 배열로 간주합니다.
            } else {
                // 다른 종류의 객체에 대한 처리가 필요할 수 있습니다.
                // 이 경우 해당 객체를 적절한 Java 표현으로 변환하는 로직이 필요합니다.
                return value.toString(); // 기본적인 toString 반환을 사용합니다.
            }
            default:
            throw new Error(`Unsupported type: ${type}`);
        }
    })
    window.$HandleBars.registerHelper('setExampleType', function (key, value, incoming) {
        var type = 'String'
        for(var i = 0; i < incoming.length; i++){
            for(var j = 0; j< incoming[i].source.aggregate.aggregateRoot.fieldDescriptors.length; j++){
                if(incoming[i].source.aggregate.aggregateRoot.fieldDescriptors[j].name == key){
                    type = incoming[i].source.aggregate.aggregateRoot.fieldDescriptors[j].className
                }
            }
        }
        return type;
    })
</function>

