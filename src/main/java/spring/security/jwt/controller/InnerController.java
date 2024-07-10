package spring.security.jwt.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.security.jwt.config.SwaggerInnerKey;
import spring.security.jwt.model.constants.ApiLogMessage;
import spring.security.jwt.utils.ApiUtils;

@Slf4j
@RestController
@RequestMapping("/iamServiceInner")
@RequiredArgsConstructor
public class InnerController {

    @SwaggerInnerKey
    @GetMapping("/healthCheck")
    public ResponseEntity<Void> healthCheck() {
        log.trace(ApiLogMessage.NAME_OF_CURRENT_METHOD.getValue(), ApiUtils.getMethodName());

        return ResponseEntity.ok().build();
    }
}
