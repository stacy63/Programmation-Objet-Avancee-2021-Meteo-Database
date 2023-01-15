package utils;

import model.Meteo;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Locale;

public class MeteoDBUtils {

    /** #######################################
     *              CREATION
     *  ###################################### **/

    //Créé une table contenant le bon nombre de colonnes pour pouvoir stocker les données de la météo
    public static void createMeteoTable(Connection connection, Statement stmt) throws SQLException {
        //VARCHAR économise de l’espace si la longueur des données peut être grande et
        //la taille du texte très fluctuante. Or plus on économise l’espace meilleures seront les performances .
        //Le nom de lieu le plus long comporte 163 caractères (sans compter les espaces)
        String sql = "CREATE TABLE IF NOT EXISTS METEO" +
                "(VILLE VARCHAR(190) PRIMARY KEY NOT NULL," +
                " ACTUAL_TEMP DOUBLE, " +
                " TEMP_MAX DOUBLE, " +
                " TEMP_MIN DOUBLE, " +
                " HUMIDITY_RATE INTEGER, " +
                " DATE_TIME TIMESTAMP," +
                " FOREIGN KEY (VILLE) REFERENCES VILLE (NOM))";
        stmt.executeUpdate(sql);
        stmt.close();
    }

    //Créé une table contenant le noms des villes dont la météo a été cherchée
    public static void createVilleTable(Connection connection, Statement stmt) throws SQLException {
        //VARCHAR économise de l’espace si la longueur des données peut être grande et
        //la taille du texte très fluctuante. Or plus on économise l’espace meilleures seront les performances .
        //Le nom de lieu le plus long comporte 163 caractères (sans compter les espaces)
        String sql = "CREATE TABLE IF NOT EXISTS VILLE" +
                "(NOM VARCHAR(190) PRIMARY KEY NOT NULL," +
                " DATE_TIME TIMESTAMP);";
        stmt.executeUpdate(sql);
        stmt.close();
    }

    /** #######################################
     *              INSERTION
     *  ###################################### **/

    public static void insertVille(Connection co, String ville) throws SQLException{

        co.setAutoCommit(false);

        //Verification ville pas déjà insérée car sinon erreur : violation contrainte unique
        if(!AlreadyExistInVILLE(co, ville)){
            String sql = "INSERT INTO VILLE (NOM, DATE_TIME) " +
                    "VALUES (?, ?);";
            PreparedStatement ps = co.prepareStatement(sql);
            ps.setString(1, ville);
            ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();

            co.commit();
            ps.close();
        } else {
            PreparedStatement ps = co.prepareStatement("UPDATE VILLE set DATE_TIME = ? where NOM = ?;");
            ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            ps.setString(2, ville);
            ps.executeUpdate();

            co.commit();
            ps.close();
        }
    }

    public static void insertMeteo(Connection co, Meteo meteo) throws SQLException{

        //Vérification que la ville existe déjà dans la table VILLE car ville de METEO est une foreign key sur nom de la table VILLE.
        if(AlreadyExistInVILLE(co, meteo.getVille())){
            co.setAutoCommit(false);

            PreparedStatement ps = null;
            //Si la ville est déjà présente on maj les données: UPDATE
            if(AlreadyExistInMETEO(co, meteo.getVille())){

                String sql = "UPDATE METEO set ACTUAL_TEMP = ? , TEMP_MAX = ? , TEMP_MIN = ? , HUMIDITY_RATE = ? , DATE_TIME = ? where VILLE = ?; ) " +
                        "VALUES (?, ?, ?, ?, ?, ? );";
                ps = co.prepareStatement(sql);
                ps.setDouble(1, meteo.getActualTemp());
                ps.setDouble(2, meteo.getTempMax());
                ps.setDouble(3, meteo.getTempMin());
                ps.setInt(4, meteo.getHumidityRate());
                ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                ps.setString(6, meteo.getVille());
            } else {
                //Sinon on insère les données
                String sql = "INSERT INTO METEO (VILLE,ACTUAL_TEMP,TEMP_MAX,TEMP_MIN,HUMIDITY_RATE,DATE_TIME) " +
                        "VALUES (?, ?, ?, ?, ?, ? );";
                ps = co.prepareStatement(sql);
                ps.setString(1, meteo.getVille());
                ps.setDouble(2, meteo.getActualTemp());
                ps.setDouble(3, meteo.getTempMax());
                ps.setDouble(4, meteo.getTempMin());
                ps.setInt(5, meteo.getHumidityRate());
                ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            }
            ps.executeUpdate();

            co.commit();
            ps.close();
        }
    }

    //Vérifie si la ville est déjà présente dans la table VILLE
    private static boolean AlreadyExistInVILLE(Connection connection, String ville) throws SQLException{

        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(" SELECT NOM FROM VILLE;");

        boolean present = false;
        while(rs.next()){
            if(rs.getString("NOM").equals(ville)){
                present = true;
            }
        }
        st.close();
        return present;
    }

    //Vérifie si les données pour une ville sont déjà présentes dans la table METEO
    private static boolean AlreadyExistInMETEO(Connection connection, String ville) throws SQLException{

        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(" SELECT VILLE FROM METEO;");

        boolean present = false;
        while(rs.next()){
            if(rs.getString(1).equals(ville)){
                present = true;
            }
        }
        st.close();

        return present;
    }

    /** #######################################
     *              AFFICHAGE
     *  ###################################### **/

    public static void afficherBDD(Statement st) throws SQLException{
        afficherEveryVille(st);
        afficherEveryMeteo(st);
    }
    public static void afficherEveryMeteo(Statement stmt) throws SQLException{

        String requete = "SELECT * FROM METEO" ;
        ResultSet rs = stmt.executeQuery (requete);

        System.out.println("##################################################");
        System.out.println("                    METEO");
        System.out.println("##################################################\n");

        System.out.println("_________________________________________________________________");
        while(rs.next()){
            String ville = rs.getString("VILLE");
            Double act = rs.getDouble(2);
            Double max = rs.getDouble(3);
            Double min = rs.getDouble(4);
            int hum = rs.getInt(5);
            Timestamp horo = rs.getTimestamp(6);
            System.out.println(ville + " | " + act + " | " + max + " | " + min + " | " + hum + " | " + horo);
        }
        System.out.println("_________________________________________________________________\n");

        rs.close();
    }

    public static void afficherEveryVille(Statement stmt) throws SQLException{

        String requete = "SELECT * FROM VILLE" ;
        ResultSet rs = stmt.executeQuery (requete);

        System.out.println("##################################################");
        System.out.println("                    VILLE");
        System.out.println("##################################################\n");

        System.out.println("_________________________________________________________________");
        while(rs.next()){
            String ville = rs.getString("NOM");
            Timestamp horo = rs.getTimestamp(2);
            System.out.println(ville + " | " + horo);
        }
        System.out.println("_________________________________________________________________\n");

        rs.close();
    }

    public static void afficherDonnesBDD(Connection connection) throws SQLException{

        System.out.println("----------------- BDD -----------------");
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet rs = metaData.getTables(null, null, "%", null);

        while (rs.next()){
            System.out.println(rs.getString(3));
        }
        System.out.println("\n");
    }

    /** #######################################
     *                  TRI
     *  ###################################### **/

    public static void triParVille(Statement st) throws SQLException{

        ResultSet rs = st.executeQuery("SELECT NOM FROM VILLE ORDER BY NOM");

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");

        System.out.println("----------------- Tri par ville -----------------");

        while (rs.next()){
            System.out.println(rs.getString("NOM"));
        }
        System.out.println("\n");
    }

    public static void triParTempérature(Statement st) throws SQLException{

        ResultSet rs = st.executeQuery("SELECT ACTUAL_TEMP FROM METEO ORDER BY ACTUAL_TEMP");

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");

        System.out.println("----------------- Tri par température -----------------");

        while (rs.next()){
            System.out.println(rs.getString("ACTUAL_TEMP"));
        }
        System.out.println("\n");
    }

    /** #######################################
     *                EXPIRATION
     *  ###################################### **/

    public static void majDonneesExpirees(Connection connection) throws SQLException{

        System.out.println("________________________ Rafraichissement données ________________________");

        //Données expirees si antérieures à 10 minutes à partir de l'heure actuelle
        String[] deleteData = {"DELETE FROM METEO WHERE DATE_TIME < ?;", "DELETE FROM VILLE WHERE DATE_TIME < ?;"};
        PreparedStatement ps = null;
        for(String sql : deleteData){
            ps = connection.prepareStatement(sql);
            ps.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now().minusMinutes(10)));
            ps.executeUpdate();
        }
        ps.close();
    }

}