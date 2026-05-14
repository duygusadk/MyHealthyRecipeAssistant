package uni.fmi.masters.jade;

import java.io.Serializable;
import java.util.List;

public class Recipe implements Serializable {
    private String id;
    private String name;
    private List<String> suitableFor;
    private List<String> ingredients;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<String> getIngredients(){return ingredients;}
    public void setIngredients(List<String> ingredients) {this.ingredients = ingredients;}
    public List<String> getSuitableFor() {
        return suitableFor;
    }

    public void setSuitableFor(List<String> suitableFor) {
        this.suitableFor = suitableFor;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Рецепта: ").append(name).append(" (ID: ").append(id).append(")\n");
        sb.append("Съставки: ").append(String.join(", ", ingredients));
        return sb.toString();

    }
}
