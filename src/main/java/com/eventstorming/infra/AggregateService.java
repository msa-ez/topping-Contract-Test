forEach: Aggregate
fileName: {{namePascalCase}}Service.java
except: {{#attached "Event" this}}{{#incomingRelations}}{{#checkIncoming source}}{{/checkIncoming}}{{/incomingRelations}}{{/attached}}
---
package {{options.package}}.infra;

import {{options.package}}.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.tomcat.jni.Proc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class {{namePascalCase}}Service {

    @StreamListener(KafkaProcessor.INPUT)
    public void on{{#attached "Event" this}}{{namePascalCase}}{{/attached}}(@Payload String message) {
        System.out.println("##### listener : " + message);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        {{#attached "Event" this}}{{namePascalCase}} {{nameCamelCase}} = null;
        try {
            {{nameCamelCase}} = objectMapper.readValue(message, {{namePascalCase}}.class);

            /**
             * 주문이 발생시, 수량을 줄인다.
             */
            if( {{nameCamelCase}}.isMe()){
        {{/attached}}
                Optional<{{namePascalCase}}> {{nameCamelCase}}Optional = {{nameCamelCase}}Repository.findById({{#attached "Event" this}}{{nameCamelCase}}{{/attached}}.getId());
                {{namePascalCase}} {{nameCamelCase}} = {{nameCamelCase}}Optional.get();
                {{nameCamelCase}}.setStock({{nameCamelCase}}.getStock() - {{#attached "Event" this}}{{nameCamelCase}}{{/attached}}.getQty());

                {{nameCamelCase}}Repository.save({{nameCamelCase}});

            }

            /**
             * 주문 취소시, 수량을 늘인다
             */
            if( orderPlaced.getEventType().equals(OrderCancelled.class.getSimpleName())){
                Optional<Product> productOptional = productRepository.findById(orderPlaced.getProductId());
                Product product = productOptional.get();
                product.setStock(product.getStock() + orderPlaced.getQuantity());

                productRepository.save(product);
            }

        }catch (Exception e){

        }
    }

    @Autowired
    ProductRepository productRepository;

    /**
     * 상품 조회
     */
    public Product getProductById(Long id){

        Optional<Product> productOptional = productRepository.findById(id);
        Product product = productOptional.get();

        return product;
    }

    public Product save(String data){
        ObjectMapper mapper = new ObjectMapper();
        Product product = null;
        try {
            product = mapper.readValue(data, Product.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<ProductOption> productOptions = product.getProductOptions();
        for(ProductOption p : productOptions){
            p.setProduct(product);
        }

        return productRepository.save(product);
    }

    public String testMsg(String data) {
        String serializedJson = null;

        if (data == null) {
            Product product = new Product();
            product.setId(1L);
            product.setName("TEST");
            product.setPrice(10000);
            product.setStock(10);
            product.setImageUrl("/test.jpg");

            ProductChanged productChanged = new ProductChanged(product);
            productChanged.setEventType("ProductChanged");
            
            serializedJson = productChanged.toJson();
        }

        return serializedJson;
    }
}
<function>
    window.$HandleBars.registerHelper('checkIncoming', function (source) {
        if(source.type == 'Policy' && source.examples){
            return false;
        } 
        return true;
    })
</function>
