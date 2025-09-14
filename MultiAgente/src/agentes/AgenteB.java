package agentes;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class AgenteB extends Agent {
    @Override
    protected void setup() {
        System.out.println(getLocalName() + " (Vendedor) iniciado.");

        while (true) {
            ACLMessage mensaje = blockingReceive();

            if (mensaje != null) {
                switch (mensaje.getPerformative()) {
                    case ACLMessage.REQUEST:
                        // Cliente pide un producto
                        System.out.println(getLocalName() + ": pedido recibido -> " + mensaje.getContent());

                        ACLMessage oferta = mensaje.createReply();
                        oferta.setPerformative(ACLMessage.INFORM);
                        oferta.setContent("Tengo la laptop a 800 USD");
                        send(oferta);
                        System.out.println(getLocalName() + ": oferta enviada al cliente.");
                        break;

                    case ACLMessage.CONFIRM:
                        // Cliente acepta la compra → llamar al repartidor
                        System.out.println(getLocalName() + ": cliente aceptó -> " + mensaje.getContent());

                        ACLMessage ordenEntrega = new ACLMessage(ACLMessage.REQUEST);
                        ordenEntrega.addReceiver(new AID("AgenteC", AID.ISLOCALNAME));
                        ordenEntrega.setContent("Entrega de laptop al cliente");
                        send(ordenEntrega);
                        System.out.println(getLocalName() + ": orden enviada al repartidor.");
                        break;

                    case ACLMessage.DISCONFIRM:
                        // Cliente rechaza
                        System.out.println(getLocalName() + ": cliente rechazó la oferta.");
                        break;
                }
            }
        }
    }
}
