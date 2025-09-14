package agentes;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class AgenteC extends Agent {
    @Override
    protected void setup() {
        System.out.println(getLocalName() + " (Repartidor) iniciado.");

        while (true) {
            ACLMessage mensaje = blockingReceive();

            if (mensaje != null && mensaje.getPerformative() == ACLMessage.REQUEST) {
                System.out.println(getLocalName() + ": recibido -> " + mensaje.getContent());
                System.out.println(getLocalName() + ": en camino con la entrega...");
            }
        }
    }
}

