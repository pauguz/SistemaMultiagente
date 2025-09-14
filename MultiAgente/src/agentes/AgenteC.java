package agentes;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;

public class AgenteC extends Agent {
    @Override
    protected void setup() {
        System.out.println(getLocalName() + " (Repartidor) iniciado.");

        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                ACLMessage entrega = blockingReceive();
                if (entrega != null && entrega.getPerformative() == ACLMessage.REQUEST) {
                    System.out.println(getLocalName() + ": recibido -> " + entrega.getContent());

                    // Necesitamos una copia final para usar dentro del WakerBehaviour
                    final ACLMessage msg = entrega;

                    // Simula tiempo de entrega sin bloquear el thread principal del agente
                    addBehaviour(new WakerBehaviour(myAgent, 3000) {
                        @Override
                        protected void onWake() {
                            System.out.println(getAgent().getLocalName() + ": entrega completada al cliente.");

                            ACLMessage confirm = msg.createReply();
                            confirm.setPerformative(ACLMessage.INFORM);
                            confirm.setContent("Entrega completada para: " + msg.getContent());
                            myAgent.send(confirm);
                        }
                    });
                } else {
                    block();
                }
            }
        });
    }
}


