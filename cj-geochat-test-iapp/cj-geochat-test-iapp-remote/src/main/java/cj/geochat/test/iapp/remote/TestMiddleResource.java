package cj.geochat.test.iapp.remote;

import cj.geochat.test.middle.ITestResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(contextId = "testMiddleResource", value = "cj-geochat-middle-test", url = "${app.test.feign.adapter.docker.uc.url:}")
public interface TestMiddleResource extends ITestResource {
    //如果Fegin代理的是get请求，那么必须在feign微服务的请求参数带上@RequestParam 或@RequestBody
    //否则不论是否指定为method = RequestMethod.GET，都会发起POST请求
    @RequestMapping(value = "/api/v1/test1", method = RequestMethod.GET)
    @Override
    String test1(@RequestParam String str);

    ///如果Fegin代理的是get请求，那么必须在feign微服务的请求参数带上@RequestParam 或@RequestBody
    //否则不论是否指定为method = RequestMethod.GET，都会发起POST请求
    @RequestMapping(value = "/api/v1/test2", method = RequestMethod.GET)
    @Override
    String test2(@RequestParam String str);
}
