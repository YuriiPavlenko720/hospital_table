package lemon.hospitaltable.table.objects;

public record WardRequest(
        Integer level,
        String name,
        Integer departmentId,
        Integer capacity
) {
}
