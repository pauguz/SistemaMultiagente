package agentes;

import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class Lanzador {
    public static void main(String[] args) {
        try {
            Runtime rt = Runtime.instance();
            Profile p = new ProfileImpl();
            AgentContainer mainContainer = rt.createMainContainer(p);

            AgentController a = mainContainer.createNewAgent("AgenteA", "agentes.AgenteA", null);
            AgentController b = mainContainer.createNewAgent("AgenteB", "agentes.AgenteB", null);
            AgentController c = mainContainer.createNewAgent("AgenteC", "agentes.AgenteC", null);

            a.start();
            b.start();
            c.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
