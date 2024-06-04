package lemon.hospitaltable.table.objects;

import java.util.Map;

public record DepartmentsOccupancyStats(Map<Integer, Integer> freeByWard, double occupancyRate) {

}
