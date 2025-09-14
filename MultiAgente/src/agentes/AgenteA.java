package agentes;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import java.util.Random;

public class AgenteA extends Agent {
    @Override
    protected void setup() {
        System.out.println(getLocalName() + " (Cliente) iniciado.");

        String producto = "laptop";
        ACLMessage pedido = new ACLMessage(ACLMessage.REQUEST);
        pedido.addReceiver(new AID("AgenteB", AID.ISLOCALNAME));
        pedido.setContent("Quiero comprar una " + producto);
        send(pedido);
        System.out.println(getLocalName() + ": pedido enviado a Vendedor -> " + producto);

        // Esperar oferta del vendedor
        ACLMessage oferta = blockingReceive();
        if (oferta != null && oferta.getPerformative() == ACLMessage.INFORM) {
            System.out.println(getLocalName() + ": oferta recibida -> " + oferta.getContent());

            // Decisión de aceptar o rechazar
            boolean acepta = new Random().nextBoolean();
            ACLMessage respuesta = oferta.createReply();

            if (acepta) {
                respuesta.setPerformative(ACLMessage.CONFIRM);
                respuesta.setContent("Acepto la oferta de la " + producto);
                System.out.println(getLocalName() + ": acepto la oferta.");
            } else {
                respuesta.setPerformative(ACLMessage.DISCONFIRM);
                respuesta.setContent("Rechazo la oferta, muy caro.");
                System.out.println(getLocalName() + ": rechazo la oferta.");
            }

            send(respuesta);
        }

        doDelete();
    }
}
