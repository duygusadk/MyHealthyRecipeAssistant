package uni.fmi.masters.jade;

import com.fasterxml.jackson.databind.ObjectMapper;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.gateway.GatewayAgent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClientAgent extends GatewayAgent {

    private String searchedCondition;
    private List<AID> recipeAgents;
    @Override
    protected void setup() {
        super.setup();
        System.out.println("UserAgent " + getAID().getName() + " е готов за работа през Gateway.");
    }

    // Този метод се вика от SpringBoot контролера чрез JadeGateway.execute
    @Override
    protected void processCommand(Object command) {
        if (command instanceof String) {
             searchedCondition = (String) command;

            // Намираме DietAgent и стартираме поведението за търсене
            addBehaviour(theRecipeQuest);
        } else {
            // Ако командата е друг тип (например за добавяне на рецепта)
            super.processCommand(command);
        }
    }




    private OneShotBehaviour theRecipeQuest = new OneShotBehaviour() {
        @Override
        public void action() {
            DFAgentDescription agentDesc = new DFAgentDescription();
            ServiceDescription servDesc = new ServiceDescription();

            servDesc.setType("recipe-provider");
            agentDesc.addServices(servDesc);

            try {
                DFAgentDescription[] descriptions = DFService.search(myAgent, agentDesc);
                recipeAgents = new ArrayList<AID>();

                for(DFAgentDescription desc : descriptions) {
                    recipeAgents.add(desc.getName());
                }

                if(!recipeAgents.isEmpty()) {
                    addBehaviour(new SearchForThePerfectRecipe());
                } else {
                    System.out.println("No recipe agents found.");
                }
            } catch (FIPAException e) {
                System.out.println(e.getMessage());
            }
        }
    };

    private class SearchForThePerfectRecipe extends Behaviour {
        int step = 0;
        MessageTemplate mt;
        HashMap<AID, Recipe[]> results = new HashMap<>();
        int replyCount = 0;

        @Override
        public void action() {
            switch(step) {
                case 0:
                    ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
                    for(AID agent : recipeAgents) {
                        cfp.addReceiver(agent);
                    }
                    cfp.setContent(searchedCondition);
                    cfp.setConversationId("recipe-dialog");
                    cfp.setReplyWith(System.currentTimeMillis() + "");

                    mt = MessageTemplate.and(
                            MessageTemplate.MatchConversationId("recipe-dialog"),
                            MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));

                    send(cfp);
                    step++;
                    break;

                case 1:
                    ACLMessage reply = receive(mt);
                    if(reply != null) {
                        replyCount++;
                        if(reply.getPerformative() == ACLMessage.PROPOSE) {
                            ObjectMapper mapper = new ObjectMapper();
                            try {
                                results.put(reply.getSender(),
                                        mapper.readValue(reply.getContent(), Recipe[].class));
                            } catch (IOException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                        if(replyCount >= recipeAgents.size()) {
                            step++;
                        }
                    }
                    break;

                case 2:
                    ObjectMapper mapper = new ObjectMapper();
                    try {

                        String finalJson = mapper.writeValueAsString(results.values());
                        releaseCommand(finalJson);
                    } catch (Exception e) {
                        releaseCommand("Error processing results");
                    }
                    step++;
                    break;
            }
        }

        @Override
        public boolean done() {
            return step == 3;
        }
    }
}
