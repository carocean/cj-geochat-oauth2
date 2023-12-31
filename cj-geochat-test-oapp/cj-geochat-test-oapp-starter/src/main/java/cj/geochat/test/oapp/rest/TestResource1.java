package cj.geochat.test.oapp.rest;

import cj.geochat.ability.api.annotation.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Tag(description = "测试方法权限",name = "测试方法权限")
@Slf4j
public class TestResource1 {

    @GetMapping(path = "/hello")
    @PreAuthorize("hasRole('general_users')")
//    @PreAuthorize("hasAuthority('SCOPE_message.read')")
    @Operation(summary = "是否有权",description = "是否有权 desc")
    @ApiResult
    @ApiResponses({@ApiResponse(responseCode = "2000", description = "ok"),
            @ApiResponse(responseCode = "2001", description = "fuck")})
    public String demo(@Parameter(description = "字串") String test) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("authentication: {}", authentication);
        return "hello:"+authentication.getPrincipal();
    }
}
