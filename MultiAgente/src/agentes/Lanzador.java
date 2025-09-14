package agentes;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class Lanzador {
    public static void main(String[] args) {
        try {
            // Inicializar la plataforma JADE
            Runtime rt = Runtime.instance();
            Profile p = new ProfileImpl();
            p.setParameter(Profile.MAIN, "true");   // Contenedor principal
            p.setParameter(Profile.GUI, "true");    // GUI de JADE para ver los agentes
            AgentContainer mainContainer = rt.createMainContainer(p);

            // Crear agentes
            AgentController cliente = mainContainer.createNewAgent("AgenteA", "agentes.Cliente", null);
            AgentController vendedor = mainContainer.createNewAgent("AgenteB", "agentes.Vendedor", null);
            AgentController repartidor = mainContainer.createNewAgent("AgenteC", "agentes.Repartidor", null);

            // Iniciar agentes
            cliente.start();
            vendedor.start();
            repartidor.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}