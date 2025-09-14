package agentes;

import jade.core.Agent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import java.util.Random;

public class Cliente extends Agent {
    @Override
    protected void setup() {
        System.out.println(getLocalName() + " (Cliente) iniciado.");

        String producto = "laptop";

        try {
            // Buscar vendedores en el DF
            DFAgentDescription template = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType("venta-productos");
            template.addServices(sd);

            DFAgentDescription[] result = DFService.search(this, template);

            if (result.length > 0) {
                AID vendedor = result[0].getName();

                ACLMessage pedido = new ACLMessage(ACLMessage.REQUEST);
                pedido.addReceiver(vendedor);
                pedido.setContent("Quiero comprar una " + producto);
                send(pedido);
                System.out.println(getLocalName() + ": pedido enviado a Vendedor -> " + producto);

                // Esperar oferta
                ACLMessage respuesta = blockingReceive();
                if (respuesta != null && respuesta.getPerformative() == ACLMessage.INFORM) {
                    System.out.println(getLocalName() + ": oferta recibida -> " + respuesta.getContent());

                    // Decisión aleatoria
                    boolean acepta = new Random().nextBoolean();
                    if (acepta) {
                        System.out.println(getLocalName() + ": acepto la oferta.");

                        ACLMessage confirm = new ACLMessage(ACLMessage.CONFIRM);
                        confirm.addReceiver(vendedor);
                        confirm.setContent("Acepto la oferta de la " + producto);
                        send(confirm);
                        System.out.println(getLocalName() + ": confirmación enviada al vendedor.");
                    } else {
                        System.out.println(getLocalName() + ": rechazo la oferta, muy caro.");
                    }
                }
            } else {
                System.out.println(getLocalName() + ": no se encontró ningún vendedor en las páginas amarillas.");
            }
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        doDelete();
    }
}
