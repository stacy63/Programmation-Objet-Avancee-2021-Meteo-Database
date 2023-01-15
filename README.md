# Objectif
Il consiste à utiliser la base de donnée pour stocker chaque données météo consultée. Cela permet une consultation extrêmement rapide en local plutôt qu’à travers le réseau ou même hors connexion.
La météo dans la ville spécifiée en paramètre de l’application est récupérée via un WS disponible sur internet : OpenWeatherMap. Le programme affiche la ville en question, la température actuelle dans la ville, la température maximale, la température minimale et l’humidité. Il s'agit également de prendre en main un concept fondamental dans le d'eveloppement moderne, l’intégration d’une API réseau RESTFull. Pour cela, il faut faire un appel HTTP et ”parser” les données renvoyées en réponse.

# Projet
Il s'agit d'un projet maven. La dépendance à la librairie gson-2.8.2 a été ajoutée dans le pom, ainsi que la librairie junit-4.13 et la librairie d'SQLite qui est un moteur de base de
donnée compact permettant de stocker des données le plus simplement du monde : dans un seul fichier. Grâce à l’interface JDBC, ce dernier s’utilise comme n’importe moteur de base (Postgres, MySql,...). 
openjdk-17 est utilisé pour ce projet.

# Détails 
## model
Meteo représente l'objet meteo contenant un attribut ville, température actuelle, température max, température min et l'humidité. La présence de l'annotation @SerializedName sur certains des attributs permet d'associer les données présentes dans le json correspondant au nom donné à l'attribut. Elle possède un constructeur par défaut et des getter et setter sur les attributs. Elle possède également sa propre méthode d'affichage. 
## utils
Contient toutes les méthodes permettant de déserialiser un json obtenu via un appel réseau en un objet meteo. La méthode getMeteo est la méthode principal qui permet de faire cela via l'appel des autres méthodes. La méthode readStream permet de créer une chaine de caractère contenant l'ensemble du json lu retourné par l'appel réseau. Enfin, la méthode deserialization permet de créer l'objet meteo à partir de la chaîne de caractère représentant le json récupéré. 
Contient également toutes les méthodes permettant de créer les tables de la base de données, d'insérer des données dans ces tables, d'affichage, de tri en fonction des villes ou de la température, et enfin une méthode de rarfaichissement des données dans le cas ou celles-ci sont présente en base depuis plus de 10 minutes par rapport à l'heure actuelle.
## application 
Contient la séquence du programme et qui effectue la récupération de la météo via le nom de la ville passée en paramètre et la gestion de ces informations en base de données. 
## test 
Contient les tests sur la méthode de désérialisation.
