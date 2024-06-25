forEach: Aggregate
except: {{#attached "Event" this}}{{#incomingRelations}}{{#checkIncoming source}}{{/checkIncoming}}{{/incomingRelations}}{{/attached}}
---
package com.example.template;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifier;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MimeTypeUtils;
import {{options.package}}.{{namePascalCase}}Application;
import {{options.package}}.infra.{{namePascalCase}}Controller;

import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class) 
@SpringBootTest(classes = {{namePascalCase}}Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureMessageVerifier
public abstract class MessagingBase {

    @Autowired
    {{namePascalCase}}Controller {{nameCamelCase}}Controller;

    @Autowired
    // Message interface to verify Contracts between services.
    MessageVerifier messaging;

    @Before
    public void setup() {
        // any remaining messages on the "eventTopic" channel are cleared
        // makes that each test starts with a clean slate
        this.messaging.receive("eventTopic", 100, TimeUnit.MILLISECONDS);
    }

    public void productChanged() {
        String json = this.{{nameCamelCase}}Controller.{{nameCamelCase}}TestMsg(null);

        this.messaging.send(MessageBuilder
                .withPayload(json)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build(), "eventTopic");
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