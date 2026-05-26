# 📚 Guia de l'Estudiant: Aplicacions Web Dinàmiques i Gestió de Dades (Lab 4)

Aquest projecte (Lab 4) és una evolució incremental del Lab 3. Manté l'arquitectura moderna i fluida d'una Single Page Application (SPA) mitjançant crides asíncrones al servidor (AJAX), però hi afegeix la complexitat de gestionar múltiples models de dades interconnectats per construir una xarxa social completa.

## 🚀 1. De MVC Tradicional a Aplicació d'una Sola Pàgina (SPA)

En els primers laboratoris, cada interacció de l'usuari provocava que el navegador demanés una pàgina HTML completa al servidor. Això interromp l'experiència de l'usuari i és poc eficient.

En aquest Lab 4, consolidem l'enfocament **SPA (Single Page Application)**:
*   **`index.html`**: És l'única pàgina que el navegador carrega realment. Actua com a esquelet o "frame" principal.
*   **Contenidors Dinàmics**: Dins d'aquest esquelet tenim divisions (`<div id="navigation">`, `<div id="content">`) que s'omplen i es buiden dinàmicament.
*   **MVC Inalterat**: És molt important entendre que, encara que la interfície sigui dinàmica, **el servidor segueix el patró MVC al 100%**. Cada crida AJAX és una petició que gestiona un Controlador (Servlet), que parla amb el Model a través de les classes de Servei i retorna una Vista (JSP/HTML).

> **Important**: Les vistes que retorna el servidor ja no són pàgines completes (amb `<html>`, `<head>`, etc.), sinó **fragments d'HTML** dissenyats per ser inserits dins d'un `<div>`.

## ⚡ 2. El Cor de l'Aplicació: AJAX amb jQuery

Per gestionar les crides asíncrones i la càrrega dinàmica, fem servir la llibreria **jQuery**, que simplifica enormement la sintaxi de Javascript per interactuar amb el servidor.

### 📥 L'avantatge del mètode `.load()`
Al carregar la pàgina, inicialitzem l'esquelet:
```javascript
$('#navigation').load("Menu"); // Carrega el fragment del menú
$('#content').load("Welcome"); // Carrega el fragment del contingut inicial
```
El mètode `.load()` de jQuery és especialment potent per dues raons:
1.  **Inserció Automàtica**: Fa la petició GET i injecta el resultat directament al DOM.
2.  **Execució de Scripts**: A diferència de mètodes natius com `innerHTML`, **`.load()` detecta i executa automàticament qualsevol etiqueta `<script>`** que vingui dins del fragment HTML. Això permet que cada fragment carregat pugui tenir la seva pròpia lògica Javascript sense haver de carregar-la tota a l'inici.

### 🔗 Captura d'Events i Crides Asíncrones (`App.bindEvents`)
Dins del client (a `index.html`), hem definit tota la lògica necessària per fer les crides asíncrones agrupant-la en una funció central, `App.bindEvents()`. Aquesta funció s'encarrega d'interceptar qualsevol acció de l'usuari (fer clic a un enllaç, enviar un formulari, prémer el botó d'afegir un tweet o de "Follow/Unfollow") per evitar que el navegador recarregui la pàgina i, en el seu lloc, fer la petició AJAX corresponent.

Aquesta manera de programar s'emmarca dins de dos patrons de disseny molt comuns en el desenvolupament frontend:
*   **Patró Mòdul (Module Pattern)**: Tota la lògica de l'aplicació s'encapsula sota un únic objecte global `App` (ex: `App.init`, `App.bindEvents`). Això manté el codi organitzat i evita conflictes de variables.
*   **Patró de Delegació d'Events (Event Delegation Pattern)**: En lloc d'assignar un event `onclick` a cada botó de manera individual, assignem un únic "escoltador" genèric al `document` sencer, filtrant per classe: 
    ```javascript
    $(document).on("click", ".menu", function (event) { ... });
    ```
    Aquest patró és **imprescindible** en les aplicacions SPA, ja que els fragments d'HTML (com els nous tweets o perfils) s'insereixen dinàmicament *després* d'haver carregat la pàgina base. La delegació d'events garanteix que qualsevol element afegit a posteriori també respongui als clics.

## 🧩 3. Arquitectura del Servidor: Ampliació del Model de Dades

L'estructura interna de l'aplicació Java es manté robusta i organitzada mitjançant els patrons **Service** i **Repository**. En aquest Lab 4, el nucli bàsic de programació es construeix al voltant de dos models principals que interactuen entre ells:

*   **Model `User`**: Representa els usuaris del sistema.
    *   **Servei (`UserService`)**: Lògica de negoci com el registre, la validació manual, l'autenticació (login) i la gestió de relacions (inclou els mètodes específics per fer **`follow`** i **`unfollow`** a altres usuaris).
    *   **Repositori (`UserRepository`)**: Únic responsable de la persistència en la base de dades (lectura i escriptura d'usuaris, així com emmagatzemar a la base de dades qui segueix a qui).
*   **Model `Tweet`**: Representa els missatges publicats pels usuaris.
    *   **Servei (`TweetService`)**: Lògica de negoci per publicar, eliminar i recuperar els tweets pertinents (per exemple, per compondre el "Timeline").
    *   **Repositori (`TweetRepository`)**: Únic responsable de la persistència dels tweets a la base de dades, vinculant-los al seu autor.

Aquesta separació clara permet que els Controladors només hagin de cridar els mètodes dels Serveis (ex: `tweetService.addTweet(...)`) sense haver de preocupar-se en absolut de com es guarden les dades realment.

## 🔐 4. Gestió d'Estat i Sessions

En una SPA, necessitem que el servidor recordi qui som mentre naveguem pels diferents fragments i realitzem accions com publicar un tweet o seguir algú.

> **Revisió important**: Cal comprovar sempre les validacions de sessió i la lògica de control abans de donar per acabat el desenvolupament. Assegureu-vos que cada servlet valida correctament que l'usuari està loguejat, que la sessió no és null i que només es permeten les accions autoritzades.

*   **Creació de la Sessió**: En el Servlet corresponent al Login, quan les credencials són vàlides, es crea una sessió i es guarda l'objecte `User` actiu en aquesta.
*   **Configuració de la Cookie de Sessió**: A `src/main/java/epaw/lab4/util/SessionConfigListener.java` hi ha un listener amb `@WebListener` que configura la cookie `JSESSIONID` per ser persistent durant 1 hora i marca la cookie com `HttpOnly` per millorar la seguretat.
*   **Contextualització**: Gràcies a les **Cookies** (gestionades transparentment pel navegador), cada crida AJAX (ja sigui carregar el menú, el timeline o afegir un tweet) viatja amb l'identificador de sessió. El servidor recupera l'usuari actiu i adapta la lògica (ex: un usuari només pot esborrar els seus propis tweets) i la vista (ex: mostrar opcions diferents).

## ✅ 5. Validació i Resposta

Mantenim la seguretat i l'experiència d'usuari validant en dos nivells:
1.  **Al Client (JS)**: Validació HTML5 / Javascript (com la *Constraint Validation API*) per donar feedback instantani a l'usuari abans d'enviar les dades al servidor.
2.  **Al Servidor (Java)**: Dins la capa de **Servei**, es validen de nou les dades (backend). Si es troben errors, els Controladors retornen el fragment corresponent junt amb la llista de problemes per mostrar-los asíncronament a l'usuari.

---

## 🎯 Objectius del Lab 4

L'objectiu d'aquesta pràctica és ajudar els estudiants a veure com poden implementar funcionalitats clau per a l'assoliment del projecte de l'assignatura, creant una aplicació SPA totalment funcional basada en els models de dades `User` i `Tweet`.

### 🛠️ Funcionalitats a implementar i consolidar:

1.  **Sistema d'Usuaris (Consolidació)**: Mantenir l'arquitectura del login, registre i visualització del perfil, funcionant 100% de manera asíncrona mitjançant AJAX.
2.  **Publicació i Gestió de Tweets**:
    *   Permetre a l'usuari loguejat publicar nous tweets, utilitzant crides asíncrones de l'estil `$.ajax` o Fetch per enviar les dades al servidor i refrescar part de la pantalla.
    *   Permetre eliminar els tweets propis.
3.  **Timeline i Vistes de Llistat**: Construir i mostrar llistats de missatges (com el "Timeline"). Els tweets es carreguen utilitzant el `TweetService` en un controlador per després ser renderitzats a través d'un fragment de vista JSP.
4.  **Sistema de Seguidors**: Implementar la lògica i la interfície per seguir i deixar de seguir altres usuaris (`Follow`/`Unfollow`), actualitzant l'estat dels botons i les relacions a la base de dades sense refrescar la pàgina.
5.  **Interfície i Menús Dinàmics**: Fer que el menú i les accions visibles variïn segons el rol de l'usuari (Autenticat vs. No Autenticat) modificant el contingut en conseqüència.

Tots els conceptes implementats en aquesta evolució us donaran la base necessària per enfrontar-vos amb garanties al desenvolupament del **Projecte Final** de l'assignatura.

> **Criteri d'avaluació**: Per aprovar l'assignatura cal implementar totes les funcionalitats obligatòries definides a l'enunciat (6 punts sobre 10). Per obtenir més nota, cal afegir 3 funcionalitats opcionals: `likes`, `comentaris` i una tercera opció lliure que cal acordar amb el professor (1 punt cada una). Finalment, la memòria s'avaluarà també amb 1 punt.
