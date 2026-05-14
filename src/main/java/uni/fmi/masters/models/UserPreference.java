package uni.fmi.masters.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class UserPreference extends BaseEntity{

    @Column(unique = true)
    private String username;

    @ManyToMany
    @JoinTable(
            name = "user_favorites",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_iri")
    )
    private List<RecipeMetadata> favoriteRecipes;
}
