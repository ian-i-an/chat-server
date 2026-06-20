package com.example.chatserver.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
@RequiredArgsConstructor
public class AsyncConfig {

    @Bean(name = "ChatSendTaskExecutor")
    public Executor chatSendTaskExecutor() {
        return createExecutor("chat-send", 20, 200, 40);
    }


    private ThreadPoolTaskExecutor createExecutor(String prefix, int core, int queue, int max) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 기본 스레드 숫자
        executor.setCorePoolSize(core);
        // 기본 스레드 숫자 넘어설 때 대기 공간 크기
        executor.setQueueCapacity(queue);
        // 대기 공간 꽉 차면 늘어나는 스레드 최대 숫자
        executor.setMaxPoolSize(max);

        executor.setThreadNamePrefix(prefix);

        // 메인 스레드를 사용한다는 의미
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        executor.initialize();
        return executor;
    }
}