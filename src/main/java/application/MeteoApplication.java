package application;

import model.Meteo;
import utils.MeteoDBUtils;
import utils.MeteoServiceUtils;

import java.io.File;
import java.sql.*;

public class MeteoApplication {

    public static void main (String[] argv) {

        if(argv.length>0){

            /** #######################################
             *           RECUPERATION METEO
             *  ###################################### **/

            String ville = argv[0];
            Meteo meteo = MeteoServiceUtils.getMeteo(ville);
            if(meteo.getVille() == null) {
                System.out.println("/!/ Mauvaise écriture du nom de la ville /!/");
            } else {

                /** #######################################
                 *           GESTION METEO BDD
                 *  ###################################### **/

                Connection connection = null;

                try{

                    //Connexion
                    Class.forName("org.sqlite.JDBC");
                    connection = DriverManager.getConnection("jdbc:sqlite:meteo.db");
                    Statement stmt = connection.createStatement();

                    //Creation des tables si pas encore existantes
                    MeteoDBUtils.createVilleTable(connection, stmt);
                    MeteoDBUtils.createMeteoTable(connection, stmt);

                    //Insertion BDD de la meteo consultée
                    MeteoDBUtils.insertVille(connection, meteo.getVille());
                    MeteoDBUtils.insertMeteo(connection, meteo);

                    //Affichage BDD
                    MeteoDBUtils.afficherDonnesBDD(connection);
                    MeteoDBUtils.afficherBDD(stmt);

                    //Tris
                    MeteoDBUtils.triParVille(stmt);
                    MeteoDBUtils.triParTempérature(stmt);

                    //Rafraichissement
                    MeteoDBUtils.majDonneesExpirees(connection);
                    MeteoDBUtils.afficherBDD(stmt);

                } catch (ClassNotFoundException e){
                    System.out.println("Driver non trouvé");
                } catch (SQLException e){
                    System.out.println("A database access error occurs:");
                    System.out.println(e);
                } finally {
                    try{
                        connection.close();
                    } catch (SQLException e) {
                        System.out.println("A database access error occurs");
                    }
                }
            }
        } else {
            System.out.println("/!/ Entrez une ville en argument de ligne de commande /!/");
        }

    }
}
