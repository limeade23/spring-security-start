package example.jwtstart.controller;

import example.jwtstart.model.request.JoinRequest;
import example.jwtstart.model.response.ApiResponse;
import example.jwtstart.service.JoinService;
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
