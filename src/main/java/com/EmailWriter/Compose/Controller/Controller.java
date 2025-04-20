    package com.EmailWriter.Compose.Controller;

    import com.EmailWriter.Compose.Entity.EmailEntry;
    import com.EmailWriter.Compose.Service.EmailReplyService;
    import lombok.AllArgsConstructor;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestBody;
    import org.springframework.web.bind.annotation.RequestMapping;

    import org.springframework.web.bind.annotation.RestController;

    import java.security.Provider;

    @RequestMapping("/api/Email")

    @RestController
    @AllArgsConstructor

    public class Controller {
        private  final EmailReplyService emailGeneratorService;

        @PostMapping("/generate")
        public ResponseEntity<String> generateemail(@RequestBody EmailEntry emailrequest){
            String response =  emailGeneratorService.generateReply(emailrequest);
            return ResponseEntity.ok(response);

        }

    }
