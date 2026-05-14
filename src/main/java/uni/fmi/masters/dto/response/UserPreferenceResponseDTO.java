package uni.fmi.masters.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uni.fmi.masters.models.RecipeMetadata;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserPreferenceResponseDTO {
    private Long id;
    private String username;
    private List<RecipeMetadata> favoriteRecipes;
}
