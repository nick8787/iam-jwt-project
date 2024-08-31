package spring.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import spring.security.jwt.IamServiceApplication;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith({MockitoExtension.class, SpringExtension.class})
@SpringBootTest(classes = IamServiceApplication.class)
@ActiveProfiles("test")
public class PostControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

//    @Test
//    @SneakyThrows
//    @WithMockUser(username = "admin", roles = {"ADMIN"})
//    public void testGetPostAsAdmin_ShouldReturn_200_Ok() {
//        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//        mockMvc.perform(get("/posts/1"))
//                .andExpect(status().isOk());
//    }
}
