package uni.fmi.masters.jade;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

public class RecipeAgent extends Agent {
    private RecipeOntology recipeOntology;

    @Override
    protected void setup() {


        recipeOntology = new RecipeOntology();

        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();

        dfd.setName(getAID());
        sd.setType("recipe-provider");
        sd.setName("The-Healthy-Recipe-Expert");

        dfd.addServices(sd);

        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            System.out.println(e.getMessage());
        }


        addBehaviour(new WaitForRecipeQuest(recipeOntology, this));
    }
}
