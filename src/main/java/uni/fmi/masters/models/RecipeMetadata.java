package uni.fmi.masters.models;

import jakarta.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RecipeMetadata extends BaseEntity{
    private String recipeIri;
    private String name;
    private String imageUrl;
}
