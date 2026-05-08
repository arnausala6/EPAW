# 📚 Guia de l'Estudiant: Aplicacions Web Dinàmiques amb AJAX i jQuery

Aquest projecte evoluciona el model MVC tradicional cap a una arquitectura més moderna i fluida, eliminant les recàrregues completes de pàgina mitjançant crides asíncrones al servidor (AJAX).

## 🚀 1. De MVC Tradicional a Aplicació d'una Sola Pàgina (SPA)

En el laboratori anterior, cada interacció de l'usuari (clicar un enllaç, enviar un formulari) provocava que el navegador demanés una pàgina HTML completa al servidor. Això interromp l'experiència de l'usuari i és poc eficient.

En aquest Lab 3, utilitzem un enfocament tipus **SPA (Single Page Application)**:
*   **`index.html`**: És l'única pàgina que el navegador carrega realment. Actua com a esquelet o "frame" principal.
*   **Contenidors Dinàmics**: Dins d'aquest esquelet tenim divisions (`<div id="navigation">`, `<div id="content">`) que s'omplen i es buiden dinàmicament.
*   **MVC Inalterat**: És molt important entendre que, encara que la interfície sigui dinàmica, **el servidor segueix el patró MVC al 100%**. Cada crida AJAX és una petició que gestiona un Controlador, que parla amb un Model a través de la seva classe de Servei i retorna una Vista.

> **Important**: Les vistes que retorna el servidor ja no són pàgines completes (amb `<html>`, `<head>`, etc.), sinó **fragments d'HTML** dissenyats per ser inserits dins d'un `<div>`.

## ⚡ 2. El Cor de l'Aplicació: AJAX amb jQuery

Per gestionar les crides asíncrones, fem servir la llibreria **jQuery**, que simplifica enormement la sintaxi de Javascript per interactuar amb el servidor.

### 📥 L'avantatge del mètode `.load()`
Al carregar la pàgina (`App.init`), inicialitzem l'esquelet:
```javascript
$('#navigation').load("Menu"); // Carrega el fragment del menú
$('#content').load("Content");   // Carrega el fragment del contingut inicial
```
El mètode `.load()` de jQuery és especialment potent per dues raons:
1.  **Inserció Automàtica**: Fa la petició GET i injecta el resultat directament al DOM.
2.  **Execució de Scripts**: A diferència de mètodes natius com `innerHTML`, **`.load()` detecta i executa automàticament qualsevol etiqueta `<script>`** que vingui dins del fragment HTML. Això permet que cada fragment carregat pugui tenir la seva pròpia lògica Javascript sense haver de carregar-la tota a l'inici.

### 🔗 Captura d'Events de Navegació
Per evitar que els enllaços recarreguin la pàgina sencera, "segrestem" el clic i el convertim en una crida AJAX:
```javascript
$(document).on("click", ".menu", function (event) {
    event.preventDefault(); // Atura la navegació real
    $('#content').load($(this).attr('href'), function () {
        $('#navigation').load("Menu"); // Recarrega el menú per actualitzar l'estat (ex: Login/Logout)
    });
});
```

## 🧩 3. Arquitectura del Servidor: MVC + Repository/Service

L'estructura interna del Java es manté robusta i organitzada:

*   **Controladors (Servlets)**: Reben la petició AJAX, gestionen els paràmetres i decideixen quin fragment HTML (la vista) retornar.
*   **Model (Repository/Service)**: 
    *   **Servei (`UserService`)**: Conté la lògica de negoci i les **validacions manuals**. Aquí és on comprovem si els camps són buits, si les contrasenyes segueixen el patró correcte o si l'usuari ja existeix.
    *   **Repositori (`UserRepository`)**: S'encarrega exclusivament de la persistència en la base de dades SQL.
*   **Vistes (JSP/HTML)**: En lloc de generar documents complets, generen només el codi HTML necessari per al fragment (ex: el formulari, el missatge de benvinguda).

## 🔐 4. Gestió d'Estat i Sessions

A diferència del laboratori anterior, on cada petició era independent, en una SPA necessitem que el servidor recordi qui som mentre naveguem pels diferents fragments.

*   **Creació de la Sessió**: En el mètode `doPost` de `Login.java`, quan les credencials són vàlides, creem una sessió amb `request.getSession()`.
*   **Scope de Sessió**: Guardem l'objecte `User` en la sessió: `session.setAttribute("user", user)`.
*   **Persistència**: Gràcies a les **Cookies** (gestionades automàticament pel navegador i el servidor), cada vegada que fem un `$('#content').load(...)`, el servidor sap quin usuari està fent la petició i pot personalitzar la vista (ex: mostrar el nom o la foto de l'usuari).

## ✅ 5. Validació en Dos Passos

Mantenim la seguretat de l'aplicació validant en ambdós costats:

1.  **Al Client (JS)**: `LoginValidation.js` i `RegisterValidation.js` utilitzen la **Constraint Validation API** per donar feedback instantani a l'usuari (marges vermells/verds) abans d'enviar les dades.
2.  **Al Servidor (Java)**: Dins de la capa de **Servei**, realitzem una validació manual camp per camp. Si trobem errors, retornem el fragment del formulari amb la llista d'errors perquè l'usuari els pugui corregir.

---

## 🎯 Objectius del Lab 3

L'objectiu final d'aquest laboratori és integrar tota la funcionalitat de registre anterior en una arquitectura d'aplicació web dinàmica funcional basada en el disseny del **Seminari 2**.

### 🛠️ Reptes a assolir:

1.  **Migració al model SPA**: Heu d'adaptar el formulari de registre que vau programar al Lab 2 per a que funcioni dins del model SPA d'aquest template (usant AJAX per a l'enviament i càrrega de fragments).
2.  **Implementació de Casos d'Ús**: Heu de reflectir tots els casos d'ús identificats per als diferents rols del vostre diagrama:
    *   **Usuari Anònim**: (ex: Login, Registre).
    *   **Usuari Registrat**: (ex: Perfil personal, Timeline, Seguir/Deixar de seguir usuaris, Publicar missatges).
    *   **Administrador**: (ex: Gestió d'usuaris).
3.  **Adaptació de Menús**: El menú dinàmic ha de mostrar opcions diferents segons el rol de l'usuari que hagi iniciat sessió.
4.  **Vistes "Fantasma" (Mockups)**: Per a aquells casos d'ús que encara no tenen lògica de backend, l'objectiu és que en clicar l'opció es carregui un fragment HTML que serveixi de **placeholder**.

> [!IMPORTANT]
> Tota la interacció entre aquestes seccions s'ha de fer mitjançant AJAX (sense recarregar la pàgina sencera), utilitzant el mètode `.load()` de jQuery per a la navegació i `$.ajax` per a l'enviament de dades.

> [!TIP]
> Si voleu provar una alternativa moderna al mètode `$.ajax` de jQuery, reviseu el fitxer `index_fetch.html` on s'utilitza la API nativa de Javascript **Fetch**. Recordeu que amb Fetch cal gestionar manualment l'execució dels scripts injectats.
