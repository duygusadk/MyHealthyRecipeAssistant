package uni.fmi.masters.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class RecipeResponseDTO {
    private Long id;
    private String recipeIri;
    private String name;
    private String imageUrl;
}
