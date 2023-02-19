package cn.huangxulin.learning.sqllog.controller;

import cn.huangxulin.learning.sqllog.entity.User;
import cn.huangxulin.learning.sqllog.service.UserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author hxl
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AtomicLong idGen = new AtomicLong(1000);

    @GetMapping("/list")
    public IPage<User> list() {
        Page<User> page = Page.of(2, 3);
        return userService.page(page);
    }

    @GetMapping("/add")
    public void add() {
        User user = new User();
        long id = idGen.incrementAndGet();
        user.setId(id);
        user.setName("user" + id);
        user.setAge(ThreadLocalRandom.current().nextInt(10, 100));
        user.setEmail("user" + id + "@example.com");

        user.setTestDate(new Date());
        user.setTestLocalDate(LocalDate.now());
        user.setTestLocalTime(LocalTime.now());

        userService.save(user);
    }
}
