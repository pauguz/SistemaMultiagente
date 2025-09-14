package agentes;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

import java.util.Random;

public class Cliente extends Agent {
    @Override
    protected void setup() {
        System.out.println(getLocalName() + " (Cliente) iniciado.");

        addBehaviour(new Behaviour() {
            private boolean terminado = false;

            @Override
            public void action() {
                try {
                    // 1. Buscar Vendedor
                    DFAgentDescription templateVendedor = new DFAgentDescription();
                    ServiceDescription sdV = new ServiceDescription();
                    sdV.setType("venta-productos");
                    templateVendedor.addServices(sdV);

                    DFAgentDescription[] vendedores = DFService.search(myAgent, templateVendedor);

                    if (vendedores.length == 0) {
                        System.out.println(getLocalName() + ": no se encontró ningún vendedor, reintentando...");
                        block(2000); // esperar 2 segundos antes de reintentar
                        return;
                    }

                    AID vendedor = vendedores[0].getName();
                    String producto = "laptop";

                    ACLMessage pedido = new ACLMessage(ACLMessage.REQUEST);
                    pedido.addReceiver(vendedor);
                    pedido.setContent("Quiero comprar una " + producto);
                    send(pedido);
                    System.out.println(getLocalName() + ": pedido enviado a " + vendedor.getLocalName());

                    // 2. Esperar respuesta del vendedor
                    ACLMessage respuesta = blockingReceive();
                    if (respuesta != null && respuesta.getPerformative() == ACLMessage.INFORM) {
                        System.out.println(getLocalName() + ": oferta recibida -> " + respuesta.getContent());

                        boolean acepta = new Random().nextBoolean();
                        if (acepta) {
                            System.out.println(getLocalName() + ": acepto la oferta.");

                            // 3. Buscar Repartidor
                            DFAgentDescription templateRepartidor = new DFAgentDescription();
                            ServiceDescription sdR = new ServiceDescription();
                            sdR.setType("servicio-reparto");
                            templateRepartidor.addServices(sdR);

                            DFAgentDescription[] repartidores = DFService.search(myAgent, templateRepartidor);

                            if (repartidores.length == 0) {
                                System.out.println(getLocalName() + ": no se encontró ningún repartidor, reintentando...");
                                block(2000);
                                return;
                            }

                            AID repartidor = repartidores[0].getName();

                            ACLMessage confirmar = new ACLMessage(ACLMessage.CONFIRM);
                            confirmar.addReceiver(vendedor);
                            confirmar.setContent("Acepto la oferta de la " + producto);
                            send(confirmar);
                            System.out.println(getLocalName() + ": confirmación enviada al Vendedor.");

                        } else {
                            System.out.println(getLocalName() + ": rechazo la oferta, muy caro.");
                        }
                    }

                    terminado = true;

                } catch (FIPAException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public boolean done() {
                return terminado;
            }
        });
    }
}
