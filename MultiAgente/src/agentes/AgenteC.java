package agentes;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class AgenteC extends Agent {
    @Override
    protected void setup() {
        System.out.println(getLocalName() + " (Repartidor) iniciado.");

        while (true) {
            ACLMessage entrega = blockingReceive();
            if (entrega != null && entrega.getPerformative() == ACLMessage.REQUEST) {
                System.out.println(getLocalName() + ": recibido -> " + entrega.getContent());

                try {
                    // Simular tiempo de entrega
                    System.out.println(getLocalName() + ": en camino con el pedido...");
                    Thread.sleep(3000); // 3 segundos
                    System.out.println(getLocalName() + ": entrega completada al cliente.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
