package start.security.controller;

import start.security.model.response.MainResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/main")
public class MainController {

    @GetMapping
    public ResponseEntity index(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var response = MainResponse.builder()
            .username(authentication.getName())
            .role(authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse(null))
            .build();

        return ResponseEntity.ok(response);
//        return ResponseEntity.ok("main");
    }

}
