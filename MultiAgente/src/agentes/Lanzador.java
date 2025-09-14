package agentes;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class Lanzador {
    public static void main(String[] args) {
        try {
            Runtime rt = Runtime.instance();
            Profile p = new ProfileImpl();
            AgentContainer mc = rt.createMainContainer(p);

            AgentController a = mc.createNewAgent("AgenteA", AgenteA.class.getName(), null);
            AgentController b = mc.createNewAgent("AgenteB", AgenteB.class.getName(), null);
            AgentController c = mc.createNewAgent("AgenteC", AgenteC.class.getName(), null);

            b.start();
            c.start();
            a.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

