forEach: Aggregate
fileName: {{nameCamelCase}}Get.groovy
except: {{#attached "Event" this}}{{#checkOutgoing outgoingRelations}}{{/checkOutgoing}}{{/attached}}
---
package contracts.rest

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method '{{#attached "Event" this}}{{#outgoingRelations}}{{#target}}{{#if aggregate}}{{controllerInfo.method}}{{/if}}'
        url ('/{{#attached "Event" this}}{{#outgoingRelations}}{{#target}}{{#if aggregate}}{{aggregate.namePlural}}{{#checkExtendVerbType controller.method controllerInfo.apiPath}}{{/checkExtendVerbType}}{{/if}}{{/target}}{{/outgoingRelations}}{{/attached}}')
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
            {{#attached "Event" this}}
            {{#outgoingRelations}}
            {{#target}}
            {{#examples}}
            {{#then}}
            {{#each value}}
                {{@key}}: {{this}},
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
            jsonPath('$.{{camelCase @key}}', byRegex(nonEmpty()).as{{#setExampleType @key this ../../../aggregateList ../../../aggregate}}{{/setExampleType}}())
            {{/each}}
            {{/then}}
            {{/examples}}
            {{/target}}
            {{/outgoingRelations}}
            {{/attached}}
        }
        headers {
            contentType(applicationJsonUtf8())
        }
    }
}

<script src="path/to/helpers.js"></script>