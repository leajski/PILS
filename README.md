## Projet PILS - Projet Innovant Lean Startup - TC 2021

# Dysco : une application pour se déconnecter en groupe

L'application Dysco vise à lutter contre l'addiction aux smartphones en proposant un jeu de groupe ludique pour se déconnecter.

Connectez-vous à plusieurs, déterminez un gage pour le perdant et lancez une partie Dysco. Ne touchez plus à votre téléphone de la soirée: chaque utilisation d'une application ou consultation d'une notification vous fait perdre des points. A la fin de votre soirée, quittez la partie, le dernier du classement doit effectuer le gage.

# Lancer le serveur

Depuis la machine hébergeante, dans le dossier Server/

```
javac server.java
java server
```

Le debugger se situe dans Server/test/. Pour le lancer, depuis le dossier Server/ :

```
javac test/debugger.java
java test/debugger
```

Dans le debugger, écrivez les message comme ils devraient être envoyés (format: PROTOCOLE:ATTRIBUTS).
Voir la [Documentation](https://docs.google.com/document/d/1Q3eJROtHBd83r9aKT_371ztg3jXKyD29RDiFgIepqk4.


# Lancer l'application

Ouvrir le dossier Disconnect dans Android Studio.
Compilez et lancez l'application sur l'émulateur de téléphone, ou sur votre propre smartphone si vous le pouvez (Android).

