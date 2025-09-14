package agentes;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

public class Repartidor extends Agent {
    @Override
    protected void setup() {
        System.out.println(getLocalName() + " (Repartidor) iniciado.");

        // Registrar servicio de reparto en el DF
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());

        ServiceDescription sd = new ServiceDescription();
        sd.setType("servicio-reparto");
        sd.setName("entrega-productos");
        dfd.addServices(sd);

        try {
            DFService.register(this, dfd);
            System.out.println(getLocalName() + ": registrado en las páginas amarillas con servicio 'servicio-reparto'.");
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        // Ciclo para recibir entregas
        new Thread(() -> {
            while (true) {
                ACLMessage msg = blockingReceive();
                if (msg != null && msg.getPerformative() == ACLMessage.REQUEST) {
                    System.out.println(getLocalName() + ": recibido -> " + msg.getContent());
                    try {
                        System.out.println(getLocalName() + ": en camino con el pedido...");
                        Thread.sleep(3000);
                        System.out.println(getLocalName() + ": entrega completada al cliente.");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
