forEach: Aggregate
fileName: {{#boundedContext}}{{#relationCommandInfo}}{{#if targetAggregate}}{{targetAggregate.aggregate.namePascalCase}}{{else}}{{commandValue.aggregate.namePascalCase}}{{/if}}{{/relationCommandInfo}}{{/boundedContext}}Controller.java
path: {{#boundedContext}}{{#relationCommandInfo}}{{#if targetAggregate}}{{targetAggregate.boundedContext.nameCamelCase}}{{else}}{{commandValue.boundedContext.nameCamelCase}}{{/if}}{{/relationCommandInfo}}{{/boundedContext}}/{{options.packagePath}}/infra
except: {{#boundedContext}}{{#checkControllerType relationCommandInfo targetAggregate}}{{/checkControllerType}}{{/boundedContext}}
---
package {{options.package}}.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import {{options.package}}.domain.{{#boundedContext}}{{#relationCommandInfo}}{{#if targetAggregate}}{{targetAggregate.aggregate.namePascalCase}}{{else}}{{commandValue.aggregate.namePascalCase}}{{/if}}{{/relationCommandInfo}}{{/boundedContext}}ControllerRepository;

@RestController
public class {{#boundedContext}}{{#relationCommandInfo}}{{#if targetAggregate}}{{targetAggregate.aggregate.namePascalCase}}{{else}}{{commandValue.aggregate.namePascalCase}}{{/if}}{{/relationCommandInfo}}{{/boundedContext}}Controller {

    @Autowired
    {{#boundedContext}}{{#relationCommandInfo}}{{#if targetAggregate}}{{targetAggregate.aggregate.namePascalCase}}{{else}}{{commandValue.aggregate.namePascalCase}}{{/if}}{{/relationCommandInfo}}{{/boundedContext}}ControllerRepository {{#boundedContext}}{{#relationCommandInfo}}{{#if targetAggregate}}{{targetAggregate.aggregate.nameCamelCase}}{{else}}{{commandValue.aggregate.nameCamelCase}}{{/if}}{{/relationCommandInfo}}{{/boundedContext}}ControllerRepository;

    {{#if commands}}
    {{#commands}}
    {{#isRestRepository}}
    {{/isRestRepository}}

    {{^isRestRepository}}
    {{#checkMethod controllerInfo.method}}
    @RequestMapping(value = "{{../namePlural}}/{id}/{{controllerInfo.apiPath}}",
        method = RequestMethod.{{controllerInfo.method}},
        produces = "application/json;charset=UTF-8")
    public {{../namePascalCase}} {{nameCamelCase}}(@PathVariable(value = "id") {{../keyFieldDescriptor.className}} id, {{#if (hasFields fieldDescriptors)}}@RequestBody {{namePascalCase}}Command {{nameCamelCase}}Command, {{/if}}HttpServletRequest request, HttpServletResponse response) throws Exception {
            System.out.println("##### /{{../nameCamelCase}}/{{nameCamelCase}}  called #####");
            Optional<{{../namePascalCase}}> optional{{../namePascalCase}} = {{../nameCamelCase}}Repository.findById(id);
            
            optional{{../namePascalCase}}.orElseThrow(()-> new Exception("No Entity Found"));
            {{../namePascalCase}} {{../nameCamelCase}} = optional{{../namePascalCase}}.get();
            {{../nameCamelCase}}.{{nameCamelCase}}({{#if (hasFields fieldDescriptors)}}{{nameCamelCase}}Command{{/if}});
            
            {{../nameCamelCase}}Repository.{{#methodConvert controllerInfo.method}}{{/methodConvert}}({{../nameCamelCase}});
            return {{../nameCamelCase}};
            
    }
    
    {{#each ../aggregateRoot.entities.relations}}
    {{#if (isGeneralization targetElement.namePascalCase ../../namePascalCase relationType)}}
    @RequestMapping(value = "{{#toURL sourceElement.nameCamelCase}}{{/toURL}}/{id}/{{../controllerInfo.apiPath}}",
            method = RequestMethod.{{../controllerInfo.method}},
            produces = "application/json;charset=UTF-8")
    public {{../../namePascalCase}} {{../nameCamelCase}}{{sourceElement.namePascalCase}}(
        @PathVariable(value = "id") {{../../keyFieldDescriptor.className}} id, {{#if (hasFields ../fieldDescriptors)}}@RequestBody {{../namePascalCase}}Command {{../nameCamelCase}}Command, {{/if}}HttpServletRequest request, HttpServletResponse response) throws Exception {
            return {{../nameCamelCase}}(id, {{#if (hasFields ../fieldDescriptors)}}{{../nameCamelCase}}Command,{{/if}} request, response);
    }
    {{/if}}
    {{/each}}

    {{/checkMethod}}

    {{^checkMethod controllerInfo.method}}
    @RequestMapping(value = "{{../namePlural}}/{{controllerInfo.apiPath}}",
            method = RequestMethod.{{controllerInfo.method}},
            produces = "application/json;charset=UTF-8")
    public {{../namePascalCase}} {{nameCamelCase}}(HttpServletRequest request, HttpServletResponse response, 
        {{#if fieldDescriptors}}@RequestBody {{aggregate.namePascalCase}} {{aggregate.nameCamelCase}}{{/if}}) throws Exception {
            System.out.println("##### /{{aggregate.nameCamelCase}}/{{nameCamelCase}}  called #####");
            {{aggregate.nameCamelCase}}.{{nameCamelCase}}({{#if fieldDescriptors}}{{nameCamelCase}}command{{/if}});
            {{aggregate.nameCamelCase}}Repository.save({{aggregate.nameCamelCase}});
            return {{aggregate.nameCamelCase}};
    }
    {{/checkMethod}}    
    {{/isRestRepository}}
    {{/commands}}
    {{/if}}

    {{#attached "Command" this}}
    {{#if controllerInfo.apiPath}}
    {{else}}
    @GetMapping("/{{namePlural}}/{id}")
    public ResponseEntity<{{namePascalCase}}> {{nameCamelCase}}StockCheck(@PathVariable(value = "id") Long id) {
        return {{nameCamelCase}}Repository.findById(id)
            .map({{nameCamelCase}} -> ResponseEntity.ok().body({{nameCamelCase}}))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
    {{/if}}
    {{/attached}}

    {{#attached "Command" this}}
    {{#outgoingRelations}}
    {{#target}}
    @GetMapping("/{{aggregate.namePlural}}/search/findBy{{#if useDefaultUri}}{{queryOption.apiPath}}{{else}}{{namePascalCase}}{{/if}}")
    public ResponseEntity<List<{{aggregate.namePascalCase}}>> {{aggregate.nameCamelCase}}StockCheck() {
        Iterable<{{aggregate.namePascalCase}}> {{aggregate.nameCamelCase}}Iterable = {{aggregate.nameCamelCase}}Repository.findAll();
        List<{{aggregate.namePascalCase}}> {{aggregate.namePlural}} = StreamSupport.stream({{aggregate.nameCamelCase}}Iterable.spliterator(), false).collect(Collectors.toList());
        return ResponseEntity.ok().body({{aggregate.namePlural}});
    }
    {{/target}}
    {{/outgoingRelations}}
    {{/attached}}
}
<function>
    window.$HandleBars.registerHelper('checkControllerType', function (relationCommandInfo, targetAggregate) {
        if(relationCommandInfo || targetAggregate){
            return false;
        }else{
            return true;
        }
    })
    window.$HandleBars.registerHelper('checkMethod', function (method, options) {
        if(method.endsWith("PUT") || method.endsWith("DELETE")){
            return options.fn(this);
        } else {
            return options.inverse(this);
        }
    });
</function>
