package lemon.hospitaltable.table.objects.security;

public record UserRequest(
        String username,
        String email,
        Short roleId
) {
}
