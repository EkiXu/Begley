package com.github.megatronking.netbare.ip;

import com.github.megatronking.netbare.gateway.Interceptor;
import com.github.megatronking.netbare.gateway.Request;
import com.github.megatronking.netbare.gateway.RequestChain;
import com.github.megatronking.netbare.gateway.Response;
import com.github.megatronking.netbare.gateway.ResponseChain;

public interface IpInterceptor extends Interceptor<Request, RequestChain,
        Response, ResponseChain> {
}
