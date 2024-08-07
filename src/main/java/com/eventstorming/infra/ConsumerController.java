forEach: Aggregate
fileName: {{namePascalCase}}Controller.java
path: {{boundedContext.name}}/{{options.packagePath}}/infra
---
package {{options.package}}.infra;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import {{options.package}}.domain.{{namePascalCase}};
import {{options.package}}.domain.{{namePascalCase}}Repository;
{{#attached "Command" this}}
{{#if isRestRepository}}
{{else}}
import {{options.package}}.domain.{{namePascalCase}}Command;
{{/if}}
{{/attached}}
{{#attached "View" this}}
{{#if queryOption.useDefaultUri}}
{{else}}
import {{options.package}}.domain.{{namePascalCase}}Query;
{{/if}}
{{/attached}}

@RestController
public class {{namePascalCase}}Controller {

    @Autowired
    {{namePascalCase}}Repository {{nameCamelCase}}Repository;

    @Autowired
    private RestTemplate restTemplate;

    {{#boundedContext}}
    {{#if relationCommandInfo}}
    {{#relationCommandInfo}}
    {{#if targetAggregate}}
    {{else}}
    @Value("${api.url.{{#wrapRight commandValue.aggregate.nameCamelCase}}{{/wrapRight}}")
    private String apiUrl;
    {{/if}}
    {{/relationCommandInfo}}
    {{/if}}
    {{/boundedContext}}
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
    @RequestMapping(value = "/{{../namePlural}}{{#if controllerInfo.apiPath}}{{controllerInfo.apiPath}}{{/if}}",
        method = RequestMethod.{{controllerInfo.method}},
        produces = "application/json;charset=UTF-8")
    public {{../namePascalCase}} {{nameCamelCase}}(HttpServletRequest request, HttpServletResponse response, 
        {{#if fieldDescriptors}}@RequestBody {{namePascalCase}}Command {{nameCamelCase}}Command{{/if}}) throws Exception {
        System.out.println("##### /{{aggregate.nameCamelCase}}/{{nameCamelCase}}  called #####");
        {{aggregate.namePascalCase}} {{aggregate.nameCamelCase}} = new {{aggregate.namePascalCase}}();
        {{aggregate.nameCamelCase}}.{{nameCamelCase}}({{#if fieldDescriptors}}{{nameCamelCase}}Command{{/if}});
        {{aggregate.nameCamelCase}}Repository.save({{aggregate.nameCamelCase}});
        return {{aggregate.nameCamelCase}};
    }
    {{/checkMethod}}    
    {{/isRestRepository}}
    {{/commands}}
    {{/if}}

    {{#attached "Event" this}}
    {{#outgoingRelations}}
    {{#target}}
    {{#if aggregate}}
    {{#attached "Aggregate" this}}
    @Value("${api.url.{{#wrapRight nameCamelCase}}{{/wrapRight}}")
    private String apiUrl;
    {{/attached}}
    {{/if}}
    {{/target}}
    {{/outgoingRelations}}
    {{/attached}}
    
    {{#attached "Event" this}}
    {{#outgoingRelations}}
    {{#target}}
    {{#if aggregate}}
    @GetMapping("/{{../../../nameCamelCase}}/validate{{#attached "Aggregate" this}}{{namePascalCase}}/{id}")
    public ResponseEntity<String> {{nameCamelCase}}StockCheck(@PathVariable(value = "{{keyFieldDescriptor.nameCamelCase}}") {{../../../keyFieldDescriptor.className}} {{../../../keyFieldDescriptor.nameCamelCase}}) {
    
        String {{../../../nameCamelCase}}Url = apiUrl + "/{{../../../namePlural}}/" + {{../../../keyFieldDescriptor.nameCamelCase}};
    
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
    
        ResponseEntity<String> {{../../../nameCamelCase}}Entity = restTemplate.exchange({{../../../nameCamelCase}}Url, HttpMethod.GET, entity, String.class);
    
        return {{../../../nameCamelCase}}Entity;
    }
    {{/attached}}
    {{/if}}
    {{/target}}
    {{/outgoingRelations}}
    {{/attached}}

    {{#attached "View" this}}
    {{#if queryOption.useDefaultUri}}
    {{else}}
    @GetMapping(path = "/{{../namePlural}}/search/findBy{{#changeUpper queryOption.apiPath}}{{/changeUpper}}/{{#queryParameters}}{{#if isKey}}{{#wrap nameCamelCase}}{{/wrap}}{{/if}}{{/queryParameters}}")
    public {{../namePascalCase}} {{queryOption.apiPath}}{{#queryParameters}}{{#if isKey}}(@PathVariable("{{nameCamelCase}}"){{className}} {{nameCamelCase}}{{/if}}{{/queryParameters}}, {{namePascalCase}}Query {{nameCamelCase}}Query) {
        return {{../nameCamelCase}}Repository.findBy{{queryOption.apiPath}}({{#queryParameters}}{{#if isKey}}{{nameCamelCase}}, {{else}}{{../nameCamelCase}}Query.get{{namePascalCase}}(){{#unless @last}},{{/unless}}{{/if}}{{/queryParameters}});
    }
    {{/if}}
    {{/attached}}

    {{#attached "Command" this}}
    {{#if incomingRelations}}
    {{#if controllerInfo.apiPath}}
    {{else}}
    @GetMapping("/{{namePlural}}/{id}")
    public ResponseEntity<{{namePascalCase}}> {{nameCamelCase}}StockCheck(@PathVariable(value = "id") Long id) {
        return {{nameCamelCase}}Repository.findById(id)
            .map({{nameCamelCase}} -> ResponseEntity.ok().body({{nameCamelCase}}))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
    {{/if}}
    {{/if}}
    {{/attached}}
}
<function>
    window.$HandleBars.registerHelper('checkExample', function (example, type) {
        if(example && type != "Policy"){
            return false;
        }else{
            return true;
        }
    })

    window.$HandleBars.registerHelper('wrapRight', function (name) {
        return name + "}";
    })
    window.$HandleBars.registerHelper('checkExample', function (example, type) {
        if(example && type != "Policy"){
            return false;
        }else{
            return true;
        }
    })
    window.$HandleBars.registerHelper('wrap', function (name) {
        return "{" + name + "}";
    })
</function>
