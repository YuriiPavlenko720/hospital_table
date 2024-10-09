package lemon.hospitaltable.table.controllers;

import lemon.hospitaltable.table.objects.*;
import lemon.hospitaltable.table.objects.loveResponses.AverageRatingResponse;
import lemon.hospitaltable.table.objects.loveResponses.DoctorRatingResponse;
import lemon.hospitaltable.table.objects.loveResponses.PatientRatingResponse;
import lemon.hospitaltable.table.services.LovesService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/loves")
public class LovesController {

    private final LovesService lovesService;

    @PostMapping
    @Secured({"ROLE_ADMIN", "ROLE_PATIENT"})
    public ResponseEntity<Love> addLove(
            @RequestBody LoveRequest loveRequest,
            @AuthenticationPrincipal OidcUser principal //getting of current user
    ) {
        Love newLove = lovesService.save(loveRequest, principal); //sending current user to service
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newLove.id())
                .toUri();
        return ResponseEntity.created(location).body(newLove);
    }

    @DeleteMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        lovesService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_PATIENT"})
    public ResponseEntity<Love> changeLove(
            @PathVariable Long id,
            @RequestBody LoveRequest updatedLoveRequest,
            @AuthenticationPrincipal OidcUser principal
    ) {
        Love updatedLove = lovesService.changeLove(id, updatedLoveRequest, principal);
        return ResponseEntity.ok(updatedLove);
    }

    @GetMapping("/patient")
    @Secured({"ROLE_ADMIN", "ROLE_PATIENT"})
    public ResponseEntity<List<Love>> getLovesByPatient(@AuthenticationPrincipal OidcUser principal) {
        List<Love> loves = lovesService.getLovesByPatient(principal);
        return ResponseEntity.ok(loves);
    }

    @GetMapping("/doctor/{id}/average")
    public ResponseEntity<AverageRatingResponse> getAverageRatingByDoctor(@PathVariable Integer id) {
        AverageRatingResponse averageRating = lovesService.getAverageRatingByDoctorId(id);
        return ResponseEntity.ok(averageRating);
    }

    @GetMapping("/doctors/top-bottom")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<List<DoctorRatingResponse>> getTopAndBottomDoctors() {
        List<DoctorRatingResponse> doctors = lovesService.getTopAndBottomDoctors();
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/patients/top")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<List<PatientRatingResponse>> getTopPatients() {
        List<PatientRatingResponse> patients = lovesService.getTopPatients();
        return ResponseEntity.ok(patients);
    }
}
