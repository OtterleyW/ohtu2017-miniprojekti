package ohtu.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DefaultControllerTest {

    @Autowired
    private WebApplicationContext webAppContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void paasivuStatusOkJaVinkitLoytyyModelista() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("kirjat"))
                .andExpect(model().attributeExists("videot"))
                .andExpect(model().attributeExists("podcastit"));
    }

    @Test
    public void hakuunKelpaamatonSyoteOhjaaTakaisinPaasivulle() throws Exception {
        mockMvc.perform(post("/")
                .param("hakusana", "    "))
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void hakuKelvollinenSyoteVinkitLoytyyModelista() throws Exception {
        mockMvc.perform(post("/")
                .param("hakusana", "testi"))
                .andExpect(model().attributeExists("kirjat"))
                .andExpect(model().attributeExists("videot"))
                .andExpect(model().attributeExists("podcastit"));
    }
}
