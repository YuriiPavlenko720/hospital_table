package lemon.hospitaltable.table.objects;

import lombok.Getter;

@Getter
public class WardRequest {
    private Integer id;
    private Integer level;
    private String name;
    private Integer departmentId;
    private Integer capacity;
    private Integer taken;
    private Integer free;
}
