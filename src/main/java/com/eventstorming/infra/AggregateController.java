forEach: Aggregate
fileName: {{namePascalCase}}Controller.java
path: {{boundedContext.name}}/{{{options.packagePath}}}/infra
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
    @Value("${api.url.{{nameCamelCase}}")
    private String apiUrl;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/order/validate{{namePascalCase}}/{productId}")
    public ResponseEntity<String> {{nameCamelCase}}StockCheck(@PathVariable(value = "productId") Long productId) {
    
        String {{nameCamelCase}}Url = apiUrl + "/{{nameCamelCase}}/" + productId;
    
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
}
---
<function>
    window.$HandleBars.registerHelper('checkOutgoing', function (relation) {
        if(!relation) return true;
    })
</function>
