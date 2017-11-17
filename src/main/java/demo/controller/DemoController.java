package demo.controller;

import demo.domain.DemoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/clear")
    public String clear() {
        demoService.clearCache();
        return "Cleared!";
    }
}
