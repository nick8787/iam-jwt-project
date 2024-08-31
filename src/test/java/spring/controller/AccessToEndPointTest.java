//package spring.controller;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.hibernate.Hibernate;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.ResultMatcher;
//import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.method.HandlerMethod;
//import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
//import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
//import spring.security.jwt.IamServiceApplication;
//import spring.security.jwt.model.entities.Role;
//import spring.security.jwt.model.entities.User;
//import spring.security.jwt.repositories.RoleRepository;
//import spring.security.jwt.repositories.UserRepository;
//import spring.security.jwt.security.JwtTokenUtils;
//
//import javax.activation.UnsupportedDataTypeException;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@SpringBootTest(classes = {IamServiceApplication.class})
//@AutoConfigureMockMvc
//@TestPropertySource(locations = "classpath:application-test.yml")
//@ExtendWith({MockitoExtension.class, SpringExtension.class})
//public class AccessToEndPointTest {
//
//    private static final String ACCESS_DENIED = "Access Denied";
//
//    @Autowired
//    private MockMvc mvc;
//
//    @Autowired
//    private JwtTokenUtils jwtTokenUtils;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private RoleRepository roleRepository;
//
//    @Autowired
//    private RequestMappingHandlerMapping requestMappingHandlerMapping;
//
//    private final ObjectMapper objectMapper = new ObjectMapper();
//    private String superAdminJwt = null;
//
//    @BeforeAll
//    @Transactional
//    void setUp() {
//        beforeAllValidateComponentsAreNotNull();
//        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        authorizeSuperAdmin();
//    }
//
//    private void beforeAllValidateComponentsAreNotNull() {
//        Assertions.assertNotNull(mvc);
//        Assertions.assertNotNull(objectMapper);
//        Assertions.assertNotNull(jwtTokenUtils);
//        Assertions.assertNotNull(userRepository);
//        Assertions.assertNotNull(roleRepository);
//    }
//
//    @Transactional
//    private void authorizeSuperAdmin() {
//        User user = userRepository.findById(1).orElseThrow(() -> new IllegalArgumentException("Invalid user or password"));
//        Hibernate.initialize(user.getRoles()); // Явная инициализация коллекции ролей
//        this.superAdminJwt = jwtTokenUtils.generateToken(user);
//    }
//
//    @Test
//    void accessWithoutTokenWithBadHeader_notOK_401() throws Exception {
//        MvcResult requestResult = mvc.perform(MockMvcRequestBuilders
//                        .post("/posts")
//                        .header("Bad-Header", "BAD_HEADER"))
//                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
//                .andReturn();
//
//        MockHttpServletResponse response = requestResult.getResponse();
//        Assertions.assertNull(response.getErrorMessage());
//    }
//
//    @Test
//    void accessWithoutHeaderWithBadToken_notOK_401() throws Exception {
//        MvcResult requestResult = mvc.perform(MockMvcRequestBuilders
//                        .post("/posts")
//                        .header(HttpHeaders.AUTHORIZATION, "BAD_TOKEN"))
//                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
//                .andReturn();
//
//        MockHttpServletResponse response = requestResult.getResponse();
//        Assertions.assertNull(response.getErrorMessage());
//    }
//
//    @Test
//    void accessWithoutHeaderWithGoodToken_notOK_401() throws Exception {
//        MvcResult requestResult = mvc.perform(MockMvcRequestBuilders
//                        .post("/posts")
//                        .header(HttpHeaders.AUTHORIZATION, superAdminJwt))
//                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
//                .andReturn();
//
//        MockHttpServletResponse response = requestResult.getResponse();
//        Assertions.assertNull(response.getErrorMessage());
//    }
//
//    @Test
//    void testAuthorizedEndPointsWithoutToken_notOK_403() throws Exception {
//        List<String> listOfAuthorizedControllers = List.of(
//                "PostController",
//                "CommentController"
//        );
//
//        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
//        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
//            RequestMappingInfo requestMapping = entry.getKey();
//            if (listOfAuthorizedControllers.contains(entry.getValue().getBeanType().getSimpleName())) {
//                Set<RequestMethod> requestMethods = requestMapping.getMethodsCondition().getMethods();
//                for (RequestMethod method : requestMethods) {
//                    for (String path : requestMapping.getPatternValues()) {
//                        requestWithoutJwt_NotOK_403(method, path.replaceAll("\\{id}", "1"));
//                    }
//                }
//            }
//        }
//    }
//
//    private void requestWithoutJwt_NotOK_403(RequestMethod method, String endPoint) throws Exception {
//        MockHttpServletRequestBuilder builder;
//        switch (method) {
//            case POST:
//                builder = MockMvcRequestBuilders.post(endPoint);
//                break;
//            case GET:
//                builder = MockMvcRequestBuilders.get(endPoint);
//                break;
//            case PUT:
//                builder = MockMvcRequestBuilders.put(endPoint);
//                break;
//            case DELETE:
//                builder = MockMvcRequestBuilders.delete(endPoint);
//                break;
//            default:
//                throw new UnsupportedDataTypeException();
//        }
//
//        MvcResult requestResult = mvc.perform(builder)
//                .andExpect(MockMvcResultMatchers.status().isForbidden())
//                .andReturn();
//        MockHttpServletResponse response = requestResult.getResponse();
//        Assertions.assertEquals(AccessToEndPointTest.ACCESS_DENIED, response.getErrorMessage());
//    }
//
//    private void testAccess(String endPoint, String jwt, String body, String errorMessage, ResultMatcher matcher) throws Exception {
//        MvcResult requestResult = mvc.perform(MockMvcRequestBuilders
//                        .post(endPoint)
//                        .header(HttpHeaders.AUTHORIZATION, jwt)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(body)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(matcher)
//                .andReturn();
//
//        MockHttpServletResponse response = requestResult.getResponse();
//        Assertions.assertEquals(errorMessage, response.getErrorMessage());
//    }
//}
