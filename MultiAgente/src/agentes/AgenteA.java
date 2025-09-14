package agentes;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import java.util.Random;

public class AgenteA extends Agent {
    @Override
    protected void setup() {
        System.out.println(getLocalName() + " (Cliente) iniciado.");

        // Cliente pide un producto (puedes cambiar laptop por otro)
        String producto = "laptop";
        ACLMessage pedido = new ACLMessage(ACLMessage.REQUEST);
        pedido.addReceiver(new AID("AgenteB", AID.ISLOCALNAME));
        pedido.setContent("Quiero comprar una " + producto);
        send(pedido);
        System.out.println(getLocalName() + ": pedido enviado a Vendedor -> " + producto);

        // Esperar respuesta del vendedor
        ACLMessage respuesta = blockingReceive();
        if (respuesta != null && respuesta.getPerformative() == ACLMessage.INFORM) {
            System.out.println(getLocalName() + ": oferta recibida -> " + respuesta.getContent());

            // Decidir si aceptar o rechazar
            boolean acepta = new Random().nextBoolean(); // 50% de probabilidad
            if (acepta) {
                System.out.println(getLocalName() + ": acepto la oferta.");

                // Mandar al repartidor
                ACLMessage confirmar = new ACLMessage(ACLMessage.REQUEST);
                confirmar.addReceiver(new AID("AgenteC", AID.ISLOCALNAME));
                confirmar.setContent("Entrega de " + producto + " al cliente");
                send(confirmar);
                System.out.println(getLocalName() + ": pedido confirmado, enviado al repartidor.");
            } else {
                System.out.println(getLocalName() + ": rechazo la oferta, muy caro.");
            }
        }

        doDelete();
    }
}
