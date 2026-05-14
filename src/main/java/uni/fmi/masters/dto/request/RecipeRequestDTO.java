package uni.fmi.masters.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RecipeRequestDTO {
    private String recipeIri;
    private String name;
    private String imageUrl;
}
