package blog.surapong.example.shedlock.api.controller;

import net.javacrumbs.shedlock.core.LockConfiguration;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.core.SimpleLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;

@RestController
public class TestLockController {

    private static final Logger logger = LoggerFactory.getLogger(TestLockController.class);

    @Autowired
    private LockProvider lockProvider;

    @GetMapping("/test-lock")
    public String testLock() throws Exception {

        final String LOCK_NAME = "TEST-LOCK-NAME";
        Duration lockAtMost = Duration.ofSeconds(60);
        Duration lockAtLeast = Duration.ZERO;
        LockConfiguration lockConfiguration = new LockConfiguration(
                Instant.now(), LOCK_NAME, lockAtMost, lockAtLeast );

        logger.info("Begin get lock");
        SimpleLock lock = lockProvider.lock(lockConfiguration).orElse(null);
        logger.info("End get lock");

        if (lock == null) {
            logger.info("Can not get lock");
            return "Case : can not get lock";
        } else {
            logger.info("Get lock and begin do task");
            try {
                Thread.sleep(10L * 1000L);
                logger.info("Do task complete");
            } finally {
                lock.unlock();
                logger.info("unlock complete");
            }
            return "Case : task complete";
        }
    }

}
