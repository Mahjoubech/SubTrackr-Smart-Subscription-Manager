import dao.AbonnementDao;
import dao.PaiementDao;
import service.AbonementService;
import service.PaimentService;
import service.RapportService;
import ui.AbonnementMenu;
import ui.PaiementMenu;
import ui.RapportMenu;


public class Main {
    public static void main(String[] args) {
        AbonnementDao dao = new AbonnementDao();
        AbonementService service = new AbonementService(dao);
        PaiementDao pdao = new PaiementDao();
        PaimentService  pservice = new PaimentService(pdao);
        AbonnementMenu.initService(service);
        PaiementMenu.initServices(pservice , service);
        // You probably did this already
        AbonnementMenu.initPaiementService(pservice);
        RapportService rapportService = new RapportService(pdao);
        RapportMenu.initServices(rapportService, service);
        ui.Menu.afiche();
    }
}