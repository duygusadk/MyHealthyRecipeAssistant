package uni.fmi.masters.models;

import jakarta.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeMetadata extends BaseEntity{
    private String recipeIri;
    private String name;
    private String imageUrl;
}
