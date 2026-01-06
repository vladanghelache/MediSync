package com.medisync.MediSync.controller;

import com.medisync.MediSync.dto.AllergyCreateDto;
import com.medisync.MediSync.dto.AllergyDto;
import com.medisync.MediSync.service.AllergyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/allergies")
@RequiredArgsConstructor
public class AllergyController {
    private final AllergyService allergyService;

    @GetMapping
    public ResponseEntity<List<AllergyDto>> getAllAllergies() {
        return ResponseEntity.ok(allergyService.getAllAllergies());
    }

    @GetMapping("/{allergyId}")
    public ResponseEntity<AllergyDto> getAllergy(@PathVariable Long allergyId) {
        return ResponseEntity.ok(allergyService.getAllergy(allergyId));
    }

    @PostMapping
    public ResponseEntity<AllergyDto> createAllergy(@Valid @RequestBody AllergyCreateDto allergyCreateDto) {
        return new ResponseEntity<>(allergyService.createAllergy(allergyCreateDto), HttpStatus.CREATED);
    }

    @PutMapping("/{allergyId}")
    public ResponseEntity<AllergyDto> updateAllergy(@PathVariable Long allergyId,
                                                    @Valid @RequestBody AllergyCreateDto allergyCreateDto) {
        return ResponseEntity.ok(allergyService.updateAllergy(allergyId, allergyCreateDto));
    }

    @DeleteMapping("/{allergyId}")
    public ResponseEntity<Void> deleteAllergy(@PathVariable Long allergyId) {
        allergyService.deleteAllergy(allergyId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
