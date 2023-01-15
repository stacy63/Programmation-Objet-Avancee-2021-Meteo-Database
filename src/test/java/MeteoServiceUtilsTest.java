import junit.framework.TestCase;
import model.Meteo;
import org.junit.Assert;
import org.junit.Test;
import utils.MeteoServiceUtils;

public class MeteoServiceUtilsTest extends TestCase {

    private MeteoServiceUtils meteoService;
    String ville;
    String json1, json2, json3, json4;

    protected void setUp() throws Exception {
        super.setUp();
        meteoService = new MeteoServiceUtils();
        ville = "Clermont-Ferrand".toUpperCase();

        /** Mock JSON **/
        //Cas correct
        json1 = new String("{\"coord\":{\"lon\":3,\"lat\":45.6667},\"weather\":[{\"id\":800,\"main\":\"Clear\"," +
                "\"description\":\"clear sky\",\"icon\":\"01d\"}],\"base\":\"stations\",\"main\":{\"temp\":9.63,\"feels_like\":8.34," +
                "\"temp_min\":9.63,\"temp_max\":9.63,\"pressure\":1025,\"humidity\":58},\"visibility\":10000,\"wind\":{\"speed\":2.57,\"deg\":10}," +
                "\"clouds\":{\"all\":0},\"dt\":1635004806,\"sys\":{\"type\":1,\"id\":6496,\"country\":\"FR\",\"sunrise\":1634969709," +
                "\"sunset\":1635007750},\"timezone\":7200,\"id\":3024634,\"name\":\"Arrondissement de Clermont-Ferrand\",\"cod\":200}");
        //Cas temp min manquant
        json2 = new String("{\"coord\":{\"lon\":3,\"lat\":45.6667},\"weather\":[{\"id\":800,\"main\":\"Clear\"," +
                "\"description\":\"clear sky\",\"icon\":\"01d\"}],\"base\":\"stations\",\"main\":{\"temp\":15,\"feels_like\":8.34," +
                "\"temp_max\":15,\"pressure\":1025,\"humidity\":62},\"visibility\":10000,\"wind\":{\"speed\":2.57,\"deg\":10}," +
                "\"clouds\":{\"all\":0},\"dt\":1635004806,\"sys\":{\"type\":1,\"id\":6496,\"country\":\"FR\",\"sunrise\":1634969709," +
                "\"sunset\":1635007750},\"timezone\":7200,\"id\":3024634,\"name\":\"Arrondissement de Clermont-Ferrand\",\"cod\":200}");
        //Cas section main manquante
        json3 = new String("{\"coord\":{\"lon\":3,\"lat\":45.6667},\"weather\":[{\"id\":800,\"main\":\"Clear\"," +
                "\"description\":\"clear sky\",\"icon\":\"01d\"}],\"base\":\"stations\",\"visibility\":10000,\"wind\":{\"speed\":2.57,\"deg\":10}," +
                "\"clouds\":{\"all\":0},\"dt\":1635004806,\"sys\":{\"type\":1,\"id\":6496,\"country\":\"FR\",\"sunrise\":1634969709," +
                "\"sunset\":1635007750},\"timezone\":7200,\"id\":3024634,\"name\":\"Arrondissement de Clermont-Ferrand\",\"cod\":200}");
        //Cas json vide
        json4 = new String("{}");
    }
    protected void tearDown() throws Exception {
        super.tearDown();
        meteoService = null;
        ville = null;
        json1 = null;
        json2 = null;
        json3 = null;
        json4 = null;
    }

    @Test
    public void testDeserialization(){

        /** TEST JSON CORRECT **/
        Meteo meteo1 = meteoService.deserialization(json1,ville);

        Assert.assertTrue(meteo1.getVille().equals(ville));
        Assert.assertTrue(meteo1.getActualTemp()==9.63);
        Assert.assertTrue(meteo1.getTempMin()==9.63);
        Assert.assertTrue(meteo1.getTempMax()==9.63);
        Assert.assertTrue(meteo1.getHumidityRate()==58);

        /** TEST JSON TEMPMIN MANQUANT **/
        Meteo meteo2 = meteoService.deserialization(json2,ville);

        Assert.assertTrue(meteo2.getVille().equals(ville));
        Assert.assertTrue(meteo2.getActualTemp()==15);
        Assert.assertTrue(meteo2.getTempMin()== 0);
        Assert.assertTrue(meteo2.getTempMax()==15);
        Assert.assertTrue(meteo2.getHumidityRate()==62);

        /** TEST JSON MAIN MANQUANT **/
        Meteo meteo3 = meteoService.deserialization(json3,ville);

        Assert.assertTrue(meteo3.getVille()==null);
        Assert.assertTrue(meteo3.getActualTemp()==0);
        Assert.assertTrue(meteo3.getTempMin()==0);
        Assert.assertTrue(meteo3.getTempMax()==0);
        Assert.assertTrue(meteo3.getHumidityRate()==0);

        /** TEST JSON VIDE **/
        Meteo meteo4 = meteoService.deserialization(json4,ville);

        Assert.assertTrue(meteo4.getVille()==null);
        Assert.assertTrue(meteo4.getActualTemp()==0);
        Assert.assertTrue(meteo4.getTempMin()==0);
        Assert.assertTrue(meteo4.getTempMax()==0);
        Assert.assertTrue(meteo4.getHumidityRate()==0);
    }

}
