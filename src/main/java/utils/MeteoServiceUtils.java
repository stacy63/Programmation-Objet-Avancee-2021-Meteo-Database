package utils;

import com.google.gson.Gson;
import model.Meteo;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MeteoServiceUtils {

    public static Meteo getMeteo(String nom_ville){

        HttpURLConnection urlConnection = null;
        BufferedReader br;
        String json_meteo = "";
        try {
            URL url = new  URL("https://api.openweathermap.org/data/2.5/weather?q=" + nom_ville + "&appid=47631728a917d56bffde4b26e7e461e3&units=metric");
            urlConnection = (HttpURLConnection) url.openConnection();
            //On définit un flux d'entrée dans lequel on va récupérer les infos méteo qu'on va ensuite aller lire
            //getInputStream: Returns an input stream that reads from this open connection.
            try{
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                json_meteo = readStream(in);
            } catch (IOException e){
                System.out.println("Aucune correspondance de la ville trouvée");
            }
            //OU result = new String(in.readAllBytes(),StandardCharsets.UTF_8);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }finally {
            if (urlConnection  != null){
                urlConnection.disconnect ();
            }
        }
        return deserialization(json_meteo, nom_ville);
    }

    private static String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }

    //Transformer un json en objet Meteo
    public static Meteo deserialization(String json_meteo, String nom_ville){

        Meteo meteo = new Meteo();

        if(!json_meteo.isEmpty()){
            Pattern pattern = Pattern.compile("\\\"main\\\":(\\{.*\\}),\\\"visibility\\\"");
            Matcher matcher = pattern.matcher(json_meteo);
            if(matcher.find()) {
                String meteo_str = matcher.group(1);
                Gson gson = new Gson();
                meteo = gson.fromJson(meteo_str, Meteo.class);
                //toUpperCase pour éviter des des doublons en bdd à cause des maj/min et donc assurer l'unicité
                meteo.setVille(nom_ville.toUpperCase());
            }
        }
        return meteo;
    }

}
