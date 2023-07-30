package cj.geochat.iapp.test.rest;

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
@RequestMapping("/public/v1")
@Tag(description = "测试开放api", name = "开放api")
@Slf4j
public class PublicResource {
    @GetMapping(path = "/hello")
    @Operation(summary = "喂", description = "测试公共方法")
    @ApiResult
    @ApiResponses({@ApiResponse(responseCode = "2000", description = "ok"), @ApiResponse(responseCode = "2001", description = "fuck")})
    public String test(@Parameter(description = "字串") String test) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return String.format("这是公共方法的访问，权限必为空：%s, 方法参数 %s", authentication, test);
    }
}
