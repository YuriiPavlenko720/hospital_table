package lemon.hospitaltable.table.objects;

import lombok.Getter;

@Getter
public class WardRequest {
    private Integer level;
    private String name;
    private Integer departmentId;
    private Integer capacity;
}
