package uni.fmi.masters.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uni.fmi.masters.models.RecipeMetadata;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserPreferenceRequestDTO {
    private String username;
    private List<RecipeMetadata> favoriteRecipes;
}
