package batch.spring.demo.job.JobListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * docs: 배치 작업 실행 전, 후 로그 추가를 위한 리스너
 * run: --spring.batch.job.names=loggerListenerJob
 * <p>
 * 핵심: 배치 작업 실행 전, 후 리스너를 통해 로그를 관리할 수 있다.
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class JobListenerConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job loggerListenerJob(Step loggerListenerStep) {
        return jobBuilderFactory.get("loggerListenerJob")
                .incrementer(new RunIdIncrementer())
                .listener(new JobLoggerListener())
                .start(loggerListenerStep)
                .build();
    }

    @JobScope
    @Bean
    public Step loggerListenerStep() {
        return stepBuilderFactory.get("loggerListenerStep")
                .tasklet(loggerListenerTasklet())
                .build();
    }

    @StepScope
    @Bean
    public Tasklet loggerListenerTasklet() {
        return (contribution, chunkContext) -> {
            log.info("loggerListener Tasklet");
            return RepeatStatus.FINISHED;
//                throw new Exception("실패~!!!!!!!!!!!1");
        };
    }
}