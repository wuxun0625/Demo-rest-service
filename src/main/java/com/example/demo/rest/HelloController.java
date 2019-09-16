package com.example.demo.rest;

import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController<E> {
//	@RequestMapping("/hello/{name}")
//    public User index(@PathVariable String name) {
//        //System.out.println(2);
//		User user = new User();
//		user.setName(name);
//        return user;
//    }
    private AtomicLong getCount = new AtomicLong();
    private AtomicLong postCount = new AtomicLong();
    public final static Logger logger = LoggerFactory.getLogger(HelloController.class);

    @RequestMapping(value = "/getAllUsers", method = RequestMethod.GET)
    @ResponseBody()
    public List<User> index(HttpServletRequest request) {
        // System.out.println(2);
        getCount.incrementAndGet();
        
        logger.info(" request total get count = " + getCount);
        Enumeration<String> en = request.getHeaderNames();
        while (en.hasMoreElements()) {
            String name = en.nextElement();
            logger.info("head key = " + name + "    value = " + request.getHeader(name));
        }
        List<User> list = new CopyOnWriteArrayList<User>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setName("user" + i);
            user.setAge(7 + i);
            list.add(user);
        }
        return list;
    }
    
    @RequestMapping(value = "/clearCount", method = RequestMethod.PUT)
    @ResponseBody()
    public String clearCount() {
        // System.out.println(2);
        getCount = new AtomicLong();
        postCount = new AtomicLong();
        return "Count cleared!";
    }

    @RequestMapping(value = "/saveUser", method = RequestMethod.POST)
    // public void saveUser(HttpServletRequest request) {
    public void saveUser(@Valid @RequestBody Object o1, HttpServletRequest request) {
        // System.out.println("request head:"+ request.getHeader("test").toString() + "
        // body:");
        postCount.incrementAndGet();
        logger.info(" request total post count = " + postCount);
        Enumeration<String> i = request.getHeaderNames();
        while (i.hasMoreElements()) {
            String name = i.nextElement();
            logger.info("head key = " + name + "    value = " + request.getHeader(name));
        }
        logger.info("  body:" + o1.toString());
    }
}

class User {
    private String name;
    private int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
