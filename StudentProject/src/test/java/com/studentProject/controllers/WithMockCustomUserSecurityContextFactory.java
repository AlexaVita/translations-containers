package com.studentProject.controllers;

import com.studentProject.entities.Role;
import com.studentProject.entities.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory
        implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        User principal =
                new User(customUser.username(), customUser.password(), true, customUser.role());
        Authentication auth =
                new UsernamePasswordAuthenticationToken(principal, customUser.password(), principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}