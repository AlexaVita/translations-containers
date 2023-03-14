package com.studentProject.controllers;

import com.studentProject.entities.Role;
import com.studentProject.services.UserService;
import org.apache.catalina.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@Import(SecurityConfig.class)
@WebMvcTest(MainController.class)
class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Test
    public void shouldAllowAccessForAnonymousUserLogin() throws Exception {

        this.mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login")).andDo(print());
    }

    @Test
    @WithMockCustomUser
    public void shouldRedirectUserToChaptersLogin() throws Exception {

        this.mockMvc.perform(get("/login"))
                .andExpect(view().name("redirect:/chapters")).andDo(print());
    }

    @Test
    @WithMockCustomUser(role = Role.ADMIN)
    public void shouldRedirectAdminToChaptersLogin() throws Exception {

        this.mockMvc.perform(get("/login"))
                .andExpect(view().name("redirect:/chapters")).andDo(print());
    }

    @Test
    public void shouldAllowAccessForAnonymousUserRegistration() throws Exception {

        this.mockMvc.perform(get("/registration"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration")).andDo(print());
    }

    @Test
    @WithMockCustomUser
    public void shouldRedirectUserToChaptersRegistration() throws Exception {

        this.mockMvc.perform(get("/registration"))
                .andExpect(view().name("redirect:/chapters")).andDo(print());
    }

    @Test
    @WithMockCustomUser(role = Role.ADMIN)
    public void shouldRedirectAdminToChaptersRegistration() throws Exception {

        this.mockMvc.perform(get("/registration"))
                .andExpect(view().name("redirect:/chapters")).andDo(print());
    }


    @Test
    @WithMockCustomUser
    public void shouldDenyAuthenticatedUserLogin() throws Exception {

        this.mockMvc
                .perform(post("/login"))
                .andExpect(status().isForbidden())
                .andDo(print());

    }

    @Test
    @WithMockCustomUser(role = Role.ADMIN)
    public void shouldDenyAuthenticatedAdminLogin() throws Exception {

        this.mockMvc
                .perform(post("/login"))
                .andExpect(status().isForbidden())
                .andDo(print());

    }

    @Test
    @WithMockCustomUser
    public void shouldDenyAuthenticatedUserRegistration() throws Exception {

        this.mockMvc
                .perform(post("/registration"))
                .andExpect(status().isForbidden())
                .andDo(print());

    }

    @Test
    @WithMockCustomUser(role = Role.ADMIN)
    public void shouldDenyAuthenticatedAdminRegistration() throws Exception {

        this.mockMvc
                .perform(post("/registration"))
                .andExpect(status().isForbidden())
                .andDo(print());

    }

}