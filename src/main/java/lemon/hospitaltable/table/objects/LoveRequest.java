package lemon.hospitaltable.table.objects;

public record LoveRequest (
                           Integer doctorId,
                           byte rating,
                           String comment
){
}
