package agentes;

import jade.core.Agent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

public class Vendedor extends Agent {
    @Override
    protected void setup() {
        System.out.println(getLocalName() + " (Vendedor) iniciado.");

        // Registrar en las páginas amarillas
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());

        ServiceDescription sd = new ServiceDescription();
        sd.setType("venta-productos");
        sd.setName("venta-laptops");
        dfd.addServices(sd);

        try {
            DFService.register(this, dfd);
            System.out.println(getLocalName() + ": registrado en las páginas amarillas con servicio 'venta-productos'.");
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        // Ciclo para recibir pedidos
        new Thread(() -> {
            while (true) {
                ACLMessage msg = blockingReceive();
                if (msg != null && msg.getPerformative() == ACLMessage.REQUEST) {
                    System.out.println(getLocalName() + ": pedido recibido -> " + msg.getContent());

                    ACLMessage oferta = msg.createReply();
                    oferta.setPerformative(ACLMessage.INFORM);
                    oferta.setContent("Tengo la laptop a 800 USD");
                    send(oferta);
                    System.out.println(getLocalName() + ": oferta enviada al cliente.");
                } else if (msg != null && msg.getPerformative() == ACLMessage.CONFIRM) {
                    System.out.println(getLocalName() + ": cliente aceptó -> " + msg.getContent());

                    // Buscar repartidor en el DF
                    DFAgentDescription template = new DFAgentDescription();
                    ServiceDescription sd2 = new ServiceDescription();
                    sd2.setType("servicio-reparto");
                    template.addServices(sd2);
                    try {
                        DFAgentDescription[] result = DFService.search(this, template);
                        if (result.length > 0) {
                            AID repartidor = result[0].getName();
                            ACLMessage orden = new ACLMessage(ACLMessage.REQUEST);
                            orden.addReceiver(repartidor);
                            orden.setContent("Entrega de laptop al cliente");
                            send(orden);
                            System.out.println(getLocalName() + ": orden enviada al repartidor.");
                        } else {
                            System.out.println(getLocalName() + ": no se encontró repartidor en las páginas amarillas.");
                        }
                    } catch (FIPAException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
