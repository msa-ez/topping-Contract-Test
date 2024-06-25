forEach: Aggregate
fileName: {{namePascalCase}}Controller.java
path: {{boundedContext.name}}/{{options.packagePath}}/infra
except: {{#attached "Event" this}}{{#checkOutgoing outgoingRelations}}{{/checkOutgoing}}{{/attached}}
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

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/{{nameCamelCase}}/validate{{#attached "Event" this}}{{#outgoingRelations}}{{#target}}{{#attached "Aggregate" this}}{{namePascalCase}}/{id}")
    public ResponseEntity<String> {{nameCamelCase}}StockCheck(@PathVariable(value = "{{keyFieldDescriptor.nameCamelCase}}") {{keyFieldDescriptor.className}} {{keyFieldDescriptor.nameCamelCase}}) {
    
        String {{nameCamelCase}}Url = apiUrl + "/{{namePlural}}/" + {{keyFieldDescriptor.nameCamelCase}};
    
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
    
        ResponseEntity<String> {{nameCamelCase}}Entity = restTemplate.exchange({{nameCamelCase}}Url, HttpMethod.GET, entity, String.class);
    
        return {{nameCamelCase}}Entity;
    }
    {{/attached}}
    {{/target}}
    {{/outgoingRelations}}
    {{/attached}}

    {{#attached "Event" this}}
    {{#outgoingRelations}}
    {{#target}}
    {{/target}}
    {{/outgoingRelations}}
    {{/attached}}
}
<function>
    window.$HandleBars.registerHelper('checkOutgoing', function (relation) {
        if(!relation) return true;
    })

    window.$HandleBars.registerHelper('wrapRight', function (name) {
        return name + "}";
    })
</function>
