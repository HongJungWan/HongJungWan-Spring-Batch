package batch.spring.job.db_data_read_write;

import batch.spring.core.domain.accounts.Accounts;
import batch.spring.core.domain.accounts.AccountsRepository;
import batch.spring.core.domain.orders.Orders;
import batch.spring.core.domain.orders.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Collections;

/**
 * desc: 주문 테이블 -> 정산 테이블 [데이터 이관]
 * run: --job.name=trMigrationJob
 * <p>
 * 핵심:
 */
@Configuration
@RequiredArgsConstructor
public class TrMigrationConfig {
    private final OrdersRepository ordersRepository;

    private final AccountsRepository accountsRepository;

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job trMigrationJob(Step trMigrationStep) {
        return jobBuilderFactory.get("trMigrationJob")
                .incrementer(new RunIdIncrementer())
                .start(trMigrationStep)
                .build();
    }

    @JobScope
    @Bean
    public Step trMigrationStep(
            ItemReader trOrdersReader,
            ItemProcessor trOrderProcessor,
            ItemWriter trOrdersWriter) {

        return stepBuilderFactory.get("trMigrationStep")
                .<Orders, Accounts>chunk(5)
                .reader(trOrdersReader)
                .processor(trOrderProcessor)
                .writer(trOrdersWriter)
                .build();
    }

    @StepScope
    @Bean
    public ItemWriter<Accounts> trOrdersWriter() {
        return items -> write(items);
    }

    @StepScope
    @Bean
    public ItemProcessor<Orders, Accounts> trOrderProcessor() {
        return item -> new Accounts(item);
    }

    @StepScope
    @Bean
    public RepositoryItemReader<Orders> trOrdersReader() {
        return new RepositoryItemReaderBuilder<Orders>()
                .name("trOrdersReader")
                .repository(ordersRepository)
                .methodName("findAll")
                .pageSize(5)
                .arguments(Arrays.asList())
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    private void write(List<? extends Accounts> items) {
        for (Accounts item : items) {
            accountsRepository.save(item);
        }
    }
}