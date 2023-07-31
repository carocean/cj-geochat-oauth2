package cj.geochat.test.middle.rest;


import cj.geochat.ability.api.annotation.ApiResult;
import cj.geochat.test.middle.ITestResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "测试",description = "测试")
public class TestResource implements ITestResource {
    @GetMapping("/test1")
    @ApiResult
    @Operation(summary = "测试1")
    @ApiResponses({@ApiResponse(responseCode = "2000", description = "ok"),
            @ApiResponse(responseCode = "2001", description = "fuck")})
    @Override
    public String test1(@Parameter(description = "参数") String str) {
        return "test1::" + str;
    }

    @GetMapping("/test2")
    @ApiResult
    @Operation(summary = "测试2")
    @ApiResponses({@ApiResponse(responseCode = "2000", description = "ok"),
            @ApiResponse(responseCode = "2001", description = "fuck")})
    @Override
    public String test2(@Parameter(description = "参数") String str) {
        return "test2::" + str;
    }
}
