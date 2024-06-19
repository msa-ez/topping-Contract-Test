path: src/test/java/com/example/template
except: {{#attached "Event" this}}{{#incomingRelations}}{{#source}}{{#checkIncoming examples}}{{/checkIncoming}}{{/source}}{{/incomingRelations}}{{/attached}}
---
package com.example.template;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import industrialaccident.domain.SickLeave;
import industrialaccident.domain.SickLeaveRepository;


@TestConfiguration
public class TestDataConfig {

    @Bean
    public CommandLineRunner initData(SickLeaveRepository repository) {
        return args -> {
            SickLeave sickLeave = new SickLeave();
            sickLeave.setAccessmentId(1L);
            sickLeave.setAccidentId(1L);
            sickLeave.setBusinessCode("bc_1");
            sickLeave.setEmployeeId("user01");
            sickLeave.setPeriod(10);
            sickLeave.setStatus("휴업급여 요청됨");;
      
            repository.save(sickLeave);
        };
    }
}

<function>
    window.$HandleBars.registerHelper('checkIncoming', function (examples) {
        if(!examples) return true;
    })
</function>