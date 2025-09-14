package agentes;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class AgenteA extends Agent {
    @Override
    protected void setup() {
        System.out.println(getLocalName() + " iniciado.");

        // Enviar mensaje a B y C
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new jade.core.AID("AgenteB", jade.core.AID.ISLOCALNAME));
        msg.addReceiver(new jade.core.AID("AgenteC", jade.core.AID.ISLOCALNAME));
        msg.setContent("Hola, soy " + getLocalName());
        send(msg);

        doDelete(); // Termina después de enviar
    }
}
