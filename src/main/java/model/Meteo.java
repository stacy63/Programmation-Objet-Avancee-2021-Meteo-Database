package model;

import com.google.gson.annotations.SerializedName;

public class Meteo {

    private String ville;

    @SerializedName("temp")
    public double actual_temp;

    @SerializedName("temp_max")
    public double temp_max;

    @SerializedName("temp_min")
    public double temp_min;

    @SerializedName("humidity")
    public int humidity_rate;

    /** CONSTRUCTEURS **/

    public Meteo(){
    }

    public Meteo(String ville, double act, double max, double min, int hum){
        this.ville = ville;
        actual_temp = act;
        temp_max = max;
        temp_min = min;
        humidity_rate = hum;
    }

    /** GETTER **/

    public String getVille(){ return ville; }

    public double getActualTemp(){ return actual_temp; }

    public double getTempMax(){ return temp_max; }

    public double getTempMin(){ return temp_min; }

    public int getHumidityRate(){ return humidity_rate; }

    /** SETTER **/

    public void setVille(String ville){ this.ville = ville; }

    public void setActualTemp(double actual_temp){ this.actual_temp = actual_temp; }

    public void setTempMax(double temp_max){ this.temp_max = temp_max; }

    public void setTempMin(double temp_min){ this.temp_min = temp_min; }

    public void setHumidityRate(int humidity_rate){ this.humidity_rate = humidity_rate; }

    /** AFFICHAGE **/

    @Override
    public String toString(){
        return new String("Ville : " + this.getVille() + ", température actuelle: " + this.getActualTemp() +
                ", température maximum : " + this.getTempMax() + ", température minimum :" + this.getTempMin() +
                ", taux d'humidité :" + this.getHumidityRate() + ".");
    }

}
