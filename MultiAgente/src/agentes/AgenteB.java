package agentes;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import java.util.HashMap;
import java.util.Map;

public class AgenteB extends Agent {
    private Map<String, Integer> catalogo = new HashMap<>();

    @Override
    protected void setup() {
        System.out.println(getLocalName() + " (Vendedor) iniciado.");

        // Catálogo de productos
        catalogo.put("laptop", 1000);
        catalogo.put("telefono", 600);
        catalogo.put("tablet", 400);

        // Esperar pedidos
        while (true) {
            ACLMessage pedido = blockingReceive();
            if (pedido != null && pedido.getPerformative() == ACLMessage.REQUEST) {
                System.out.println(getLocalName() + ": pedido recibido -> " + pedido.getContent());

                // Determinar el producto solicitado
                String contenido = pedido.getContent().toLowerCase();
                String productoEncontrado = null;

                for (String producto : catalogo.keySet()) {
                    if (contenido.contains(producto)) {
                        productoEncontrado = producto;
                        break;
                    }
                }

                ACLMessage respuesta = new ACLMessage(ACLMessage.INFORM);
                respuesta.addReceiver(pedido.getSender());

                if (productoEncontrado != null) {
                    int precio = catalogo.get(productoEncontrado);
                    respuesta.setContent("El precio de la " + productoEncontrado + " es " + precio + " USD");
                } else {
                    respuesta.setContent("Producto no disponible.");
                }

                send(respuesta);
                System.out.println(getLocalName() + ": respuesta enviada -> " + respuesta.getContent());
            }
        }
    }
}
