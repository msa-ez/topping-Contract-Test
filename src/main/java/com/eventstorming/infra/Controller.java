forEach: Aggregate
fileName: {{namePascalCase}}Controller.java
path: {{boundedContext.name}}/{{options.packagePath}}/infra
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
import org.springframework.web.client.RestTemplate;

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
    @Value("${api.url.{{#wrapRight targetAggregate.aggregate.nameCamelCase}}}{{/wrapRight}}"){{else}}@Value("${api.url.{{#wrapRight commandValue.aggregate.nameCamelCase}}}{{/wrapRight}}")
    {{/if}}
    {{/relationCommandInfo}
    {{/boundedContext}}
    private String apiUrl;

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
    @RequestMapping(value = "{{../namePlural}}{{controllerInfo.apiPath}}",
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

    {{#attached "Event" this}}
    {{#outgoingRelations}}
    {{#target}}
    {{#attached "Aggregate" this}}
    @Value("${api.url.{{#wrapRight nameCamelCase}}{{/wrapRight}}")
    private String apiUrl;
    {{/attached}}
    {{/target}}
    {{/outgoingRelations}}
    {{/attached}}
    
    // consumer
    {{#attached "Event" this}}
    {{#outgoingRelations}}
    {{#target}}
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
    {{/target}}
    {{/outgoingRelations}}
    {{/attached}}

    
    {{#attached "Command" this}}
    {{#outgoingRelations}}
    {{#target}}
    {{#if queryParameters}}
    @GetMapping("/{{../../../nameCamelCase}}/validate{{aggregate.namePascalCase}}/search/findBy{{#if useDefaultUri}}{{queryOption.apiPath}}{{else}}{{namePascalCase}}{{/if}}")
    public ResponseEntity<String> {{aggregate.nameCamelCase}}StockCheck() {
        String {{aggregate.nameCamelCase}}Url = apiUrl + "/{{aggregate.namePlural}}/search/findBy{{#if useDefaultUri}}{{queryOption.apiPath}}{{else}}{{namePascalCase}}{{/if}}";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        
        ResponseEntity<String> {{aggregate.nameCamelCase}}Entity = restTemplate.exchange({{aggregate.nameCamelCase}}Url, HttpMethod.GET, entity, String.class);
        
        return {{aggregate.nameCamelCase}}Entity;
    }
    {{/if}}
    {{/target}}
    {{/outgoingRelations}}
    {{/attached}}
    
    // Provider
    {{#attached "View" this}}
    {{#if incomingRelations}}
    @GetMapping("/{{aggregate.namePlural}}/search/findBy{{#if useDefaultUri}}{{queryOption.apiPath}}{{else}}{{namePascalCase}}{{/if}}")
    public ResponseEntity<List<{{aggregate.namePascalCase}}>> {{aggregate.nameCamelCase}}StockCheck() {
        Iterable<{{aggregate.namePascalCase}}> {{aggregate.nameCamelCase}}Iterable = {{aggregate.nameCamelCase}}Repository.findAll();
        List<{{aggregate.namePascalCase}}> {{aggregate.namePlural}} = StreamSupport.stream({{aggregate.nameCamelCase}}Iterable.spliterator(), false).collect(Collectors.toList());
        return ResponseEntity.ok().body({{aggregate.namePlural}});
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
</function>
