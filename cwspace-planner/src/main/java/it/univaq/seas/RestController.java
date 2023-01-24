package it.univaq.seas;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author gianlucarea
 */
@org.springframework.web.bind.annotation.RestController
public class RestController {
    private boolean DOCKERIZE = true;

    @GetMapping("/")
    public String greeting() {
        return "Root";
    }

    @PostMapping("/message")
    public String message(@RequestBody String body) {
        System.out.println(body);
        return body;
    }

    @PostMapping("/energyConsuptionAdaptation")
    public void consumptionAdaptation(@RequestBody String message) {
        System.out.println(message);
    }

    @PostMapping("/status")
    public void status(@RequestBody String message) {

        System.out.println(message);


    }
}
