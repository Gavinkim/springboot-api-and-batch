package com.gavinkim.batch.jobs;

import com.gavinkim.model.coupon.Coupon;
import com.gavinkim.model.coupon.CouponService;
import com.gavinkim.type.CouponStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@StepScope
public class ExpiredCouponUpdateTasklet implements Tasklet {

    @Value("#{jobParameters[targetDate]}")
    private String targetDate;

    private final CouponService couponService;

    @Autowired
    public ExpiredCouponUpdateTasklet(CouponService couponService) {
        this.couponService = couponService;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        LocalDate now = LocalDate.now();
        Optional<List<Coupon>> optCoupon = couponService.getExpiredCoupon(now);
        if(optCoupon.isPresent()){
            List<Coupon> couponList = optCoupon.get().stream().collect(Collectors.toList());
            couponList.stream().forEach(c->c.setStatus(CouponStatus.EXPIRED));
            couponService.saveAll(couponList);
            log.info("[!] >>>> 만료된 쿠폰 {} 개 처리되었습니다.",couponList.size());
            return RepeatStatus.FINISHED;
        }else{
            log.info("[!] >>>> 만료된 쿠폰이 존재 하지 않습니다.");
            return RepeatStatus.FINISHED;
        }

    }
}
