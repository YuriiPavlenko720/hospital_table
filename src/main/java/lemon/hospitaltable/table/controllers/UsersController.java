package lemon.hospitaltable.table.controllers;

import lemon.hospitaltable.table.objects.security.Role;
import lemon.hospitaltable.table.objects.security.User;
import lemon.hospitaltable.table.objects.security.UserRequest;
import lemon.hospitaltable.table.repositories.RolesRepositoryInterface;
import lemon.hospitaltable.table.services.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UsersController {
    private final UsersService usersService;
    private final RolesRepositoryInterface rolesRepository;

    @PostMapping
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<User> addUser(@RequestBody UserRequest userRequest) {
        User newUser = usersService.save(userRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newUser.id())
                .toUri();
        return ResponseEntity.created(location).body(newUser);
    }

    @DeleteMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        usersService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change_username")
    @Secured({"ROLE_ADMIN"})
    public void renameById(@PathVariable Long id, String newUsername) {
        usersService.renameById(id, newUsername);
    }

    @PostMapping("/{id}/change_email")
    @Secured({"ROLE_ADMIN"})
    public void changeEmailById(@PathVariable Long id, String newEmail) {
        usersService.changeEmailById(id, newEmail);
    }

    @PostMapping("/{id}/change_role")
    @Secured({"ROLE_ADMIN"})
    public void changeRoleById(@PathVariable Long id, Short newRoleId) {
        usersService.changeRoleById(id, newRoleId);
    }


    @GetMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public Optional<User> getUser(@PathVariable Long id) {
        return usersService.findById(id);
    }

    @GetMapping("/find")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<?> findUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Short roleId
    ) {
        List<User> users = usersService.findUsers(username, roleId);
        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(users);
        }
    }

    @GetMapping("/roles/{id}")
    @Secured({"ROLE_ADMIN"})
    public Optional<Role> getRole(@PathVariable Short id) {
        return rolesRepository.findById(id);
    }

    @GetMapping("/roles")
    @Secured({"ROLE_ADMIN"})
    public Iterable<Role> findAll() {
        return rolesRepository.findAll();
    }
}

