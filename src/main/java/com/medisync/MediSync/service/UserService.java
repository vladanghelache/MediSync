package com.medisync.MediSync.service;

import com.medisync.MediSync.dto.UserDto;
import com.medisync.MediSync.exception.ResourceNotFoundException;
import com.medisync.MediSync.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto getUser(Long userId){
        return UserDto.mapToDto(userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id=" + userId + " not found")));
    }
}
