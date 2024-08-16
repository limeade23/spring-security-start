package start.security.controller;

import start.security.model.request.JoinRequest;
import start.security.model.response.ApiResponse;
import start.security.service.JoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/join")
public class JoinController {

    private final JoinService joinService;

    @PostMapping
    public ResponseEntity join(@RequestBody JoinRequest request) {
        return ApiResponse.ok(joinService.join(request));
    }

}
