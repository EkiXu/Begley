package com.github.megatronking.netbare.ip;

import androidx.annotation.NonNull;

import com.github.megatronking.netbare.gateway.InterceptorFactory;

public interface IpInterceptorFactory extends InterceptorFactory {
    @NonNull
    @Override
    IpInterceptor create();
}
