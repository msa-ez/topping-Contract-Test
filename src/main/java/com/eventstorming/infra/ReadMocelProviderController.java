forEach: View
fileName: {{aggregate.namePascalCase}}Controller.java
path: {{boundedContext.name}}/{{options.packagePath}}/infra
except: {{#checkExample examples}}{{/checkExample}}
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

import {{options.package}}.domain.{{aggregate.namePascalCase}}ControllerRepository;

@RestController
public class {{aggregate.namePascalCase}}Controller {

    @Autowired
    {{aggregate.namePascalCase}}Repository {{aggregate.nameCamelCase}}Repository;

    @GetMapping("/{{aggregate.namePlural}}/search/findBy{{#if useDefaultUri}}{{queryOption.apiPath}}{{else}}{{namePascalCase}}{{/if}}")
    public ResponseEntity<List<{{aggregate.namePascalCase}}>> {{aggregate.nameCamelCase}}StockCheck() {
        Iterable<{{aggregate.namePascalCase}}> {{aggregate.nameCamelCase}}Iterable = {{aggregate.nameCamelCase}}Repository.findAll();
        List<{{aggregate.namePascalCase}}> {{aggregate.namePlural}} = StreamSupport.stream({{aggregate.nameCamelCase}}Iterable.spliterator(), false).collect(Collectors.toList());
        return ResponseEntity.ok().body({{aggregate.namePlural}});
    }
}
<function>
    window.$HandleBars.registerHelper('checkExample', function (examples) {
        if(examples){
            return false;
        }else{
            return true;
        }
    })
</function>
