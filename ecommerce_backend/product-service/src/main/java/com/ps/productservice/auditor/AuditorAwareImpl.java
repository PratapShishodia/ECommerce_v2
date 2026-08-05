package com.ps.productservice.auditor;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Configuration("auditorAwareImpl")
public class AuditorAwareImpl implements AuditorAware {
    @Override
    public Optional getCurrentAuditor() {
//        return Optional.of(ApplicationUtility.getLoggedInUser());
        return Optional.of("SELLER");
    }
}
