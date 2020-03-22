package vizzyy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import vizzyy.service.LoggingService;
import vizzyy.service.S3ResourceService;

@RestController
@RequestMapping(value = "/door")
@PreAuthorize("hasAnyAuthority('ROLE_POWER', 'ROLE_ADMIN')")
public class DoorController {

    @Autowired
    LoggingService loggingService;

    @Autowired
    RestTemplate restTemplate;

    private boolean isDoorOpen = false;

    private static String ddns = (String) S3ResourceService.loadFileFromS3("vizzyy", "credentials/ddns.url").toArray()[0];

    @RequestMapping(value = "/open")
    public String open() {
        String entry = "User BLEEP BLOOP opened door at BLAH BLAH BLAH time.";
        loggingService.addEntry("Calling /door/open?entry=");
        String res = restTemplate.getForObject(ddns + ":9000/open?entry="+entry, String.class);
        isDoorOpen = true;
        return res;
    }

    @RequestMapping(value = "/close")
    public String close() {
        String entry = "User BLEEP BLOOP closed door at BLAH BLAH BLAH time.";
        loggingService.addEntry("Calling /door/close?entry=");
        String res = restTemplate.getForObject(ddns + ":9000/close?entry="+entry, String.class);
        isDoorOpen = false;
        return res;
    }

    @RequestMapping(value = "/state", method = RequestMethod.POST)
    public void setState(@RequestParam boolean state){
        loggingService.addEntry("Set door state - door is opened: "+ state);
        isDoorOpen = state;
    }

    @RequestMapping(value = "/state", method = RequestMethod.GET)
    public boolean getState(){
        loggingService.addEntry("Get door state - door is opened: "+isDoorOpen);
        return isDoorOpen;
    }

}
