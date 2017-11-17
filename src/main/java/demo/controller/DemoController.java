package demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import demo.domain.DemoService;

@RestController
public class DemoController {

    private final DemoService demoService;

    public DemoController(DemoService demoService) {
        this.demoService = demoService;
    }

    @GetMapping("/foo")
    public String foo(@RequestParam(required = false) String q) {
        return demoService.getFoo(q);
    }

    @GetMapping("/bar")
    public String bar(@RequestParam(required = false) String q) {
        return demoService.getBar(q);
    }
}
