package com.medisync.MediSync.controller;

import com.medisync.MediSync.dto.ChangePasswordDto;
import com.medisync.MediSync.dto.UserDto;
import com.medisync.MediSync.entity.User;
import com.medisync.MediSync.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @PatchMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @RequestBody @Valid ChangePasswordDto request,
            @AuthenticationPrincipal User currentUser
    ) {
        userService.changePassword(request, currentUser.getId());
        return ResponseEntity.ok().build();
    }
}
