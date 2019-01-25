package wei.endpoint;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserInfoResource {

    @GetMapping("/user/me")
    public Principal getUserInfo(Principal principal) {
        return principal;
    }
}
