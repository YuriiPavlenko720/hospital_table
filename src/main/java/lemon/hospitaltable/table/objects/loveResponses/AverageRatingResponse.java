package lemon.hospitaltable.table.objects.loveResponses;

import lemon.hospitaltable.table.objects.Love;
import java.util.List;

public record AverageRatingResponse(double averageRating, List<Love> comments) {

}
