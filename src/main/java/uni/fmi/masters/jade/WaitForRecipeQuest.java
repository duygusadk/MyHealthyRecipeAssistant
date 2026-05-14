package uni.fmi.masters.jade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jade.core.Agent;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;

public class WaitForRecipeQuest extends CyclicBehaviour {
    RecipeOntology ontology;

    public WaitForRecipeQuest(RecipeOntology ontology, Agent agent){
        this.ontology = ontology;
        myAgent = agent;
    }

    @Override
    public void action() {

        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
        ACLMessage msg = myAgent.receive(mt);

        if(msg != null) {

            String searchKey = msg.getContent();
            ACLMessage reply = msg.createReply();

            System.out.println("Търсене на рецепти за състояние: " + searchKey);


            ArrayList<Recipe> foundRecipes=  ontology.getRecipesByCondition(searchKey);

            if(!foundRecipes.isEmpty()) {
                System.out.println("We found some recipes...");
                reply.setPerformative(ACLMessage.PROPOSE);

                ObjectMapper mapper = new ObjectMapper();
                try {
                    reply.setContent(mapper.writeValueAsString(foundRecipes));
                    reply.setLanguage("JSON");
                } catch (JsonProcessingException e) {
                    System.out.println(e.getMessage());
                }

            } else {
                reply.setPerformative(ACLMessage.INFORM_REF);
                reply.setContent("No recipes found for: " + searchKey);
                System.out.println("Няма намерени резултати.!");
            }

            myAgent.send(reply);
        }
    }
}
