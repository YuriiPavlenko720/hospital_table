package lemon.hospitaltable.table.services;

import lemon.hospitaltable.table.objects.security.Role;
import lemon.hospitaltable.table.objects.security.User;
import lemon.hospitaltable.table.objects.security.UserRequest;
import lemon.hospitaltable.table.repositories.RolesRepositoryInterface;
import lemon.hospitaltable.table.repositories.UsersRepositoryInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Service
public class UsersService {

    private final UsersRepositoryInterface usersRepository;
    private final RolesRepositoryInterface rolesRepository;


    public User save(UserRequest userRequest) {

        //checking existence of a user with the given email
        Optional<User> existingUser = usersRepository.findByEmail(userRequest.email());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("A user with email " + userRequest.email() + " already exists.");
        }

        //checking role existence
        Role role = rolesRepository.findById(userRequest.roleId())
                .orElseThrow(() -> new IllegalArgumentException("Role ID " + userRequest.roleId() + " not found."));

        //creating of the user
        return usersRepository.save(new User(
                null,
                userRequest.username(),
                userRequest.email(),
                userRequest.roleId()
        ));
    }


    public void deleteById(Long id) {
        //checking existence of the aim user
        User user = usersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User ID " + id + " not found."));

        //checking if the user is the last ADMIN
        if (user.roleId() == 1) {
            long count = usersRepository.countByRoleId((short) 1);
            if (count <= 1) {
                throw new IllegalStateException("Cannot delete the last ADMIN. Add another ADMIN and try again.");
            }
        }

        //deleting of the aim user
        usersRepository.deleteById(id);
    }


    public void renameById(Long id, String newUsername) {
        User user = usersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User ID " + id + " not found."));
        usersRepository.save(user.withUsername(newUsername));
    }


    public void changeEmailById(Long id, String newEmail) {
        //checking existence and getting of the aim user
        User user = usersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User ID " + id + " not found."));

        //checking existence of a user with the given email
        Optional<User> existingUser = usersRepository.findByEmail(newEmail);
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("A user with email " + newEmail + " already exists.");
        }

        //saving user with new email
        usersRepository.save(user.withEmail(newEmail));
    }


    public void changeRoleById(Long id, Short newRoleId) {
        //checking existence and getting of the aim user
        User user = usersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User ID " + id + " not found."));

        //checking role existence
        Role role = rolesRepository.findById(newRoleId)
                .orElseThrow(() -> new IllegalArgumentException("Role ID " + newRoleId + " not found."));

        //checking if the user is the last ADMIN
        if (user.roleId() == 1) {
            long count = usersRepository.countByRoleId((short) 1);
            if (count <= 1) {
                throw new IllegalStateException("Cannot delete the last ADMIN. Add another ADMIN and try again.");
            }
        }

        //saving user with new role
        usersRepository.save(user.withRoleId(newRoleId));
    }


    public Optional<User> findById(Long id) {
        return usersRepository.findById(id);
    }


    public List<User> findUsers(String username, Short roleId) {
        if (username != null && !username.isEmpty()) {
            return usersRepository.findByUsername(username);
        } else if (roleId != null) {
            return usersRepository.findByRoleId(roleId);
        } else {
            return StreamSupport.stream(usersRepository.findAll().spliterator(), false)
                    .collect(Collectors.toList());
        }
    }
}
