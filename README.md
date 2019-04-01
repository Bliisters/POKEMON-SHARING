# POKEPUTZ-SHARING

*package fr.ensibs.pokeputz.authentification:* Service d'authentification tel que décrit dans le projet initial, essentiellement RMI
*package fr.ensibs.pokeputz.publisher:* Service de publication d'annonces par le client sur le serveur, essentiellement JMS et RMI (JMS pour publier, RMI pour inscrire dans la BDD et appeller le JMS)
*package fr.ensibs.pokeputz.sharing:* Service d'échange de poképutz tel que décrit dans le projet initial, essentiellement Javaspace
*package fr.ensibs.pokeputz.common:* Les interfaces communes a tous les deux projets, genre interface Poképutz, etc...


**UNE REGLE:** On ne créée pas de classe ou interface inutile. Oui yaura sûrement une classe 'Poképutz', mais tant qu'on ne l'utilise pas je ne veux pas la voir dans les packages en mode 'Voilà on en aura besoin je sais pas quoi mettre dedans mlais j'ai fait un truc': On créée les classes dont on aura besoin QUAND ON EN AURA BESOIN
