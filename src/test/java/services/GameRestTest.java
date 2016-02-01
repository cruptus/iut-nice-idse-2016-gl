package services;


import fr.unice.idse.constante.Config;
import fr.unice.idse.model.*;
import fr.unice.idse.services.GameRest;
import org.codehaus.jettison.json.*;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GameRestTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(GameRest.class);
    }

    Model model;
    Game game;

    @Before
    public void init(){
        model = Model.getInstance();
        model.setGames(new ArrayList<Game>());
        model.addGame(model.createPlayer("toto"),"tata", 4);
        game = model.findGameByName("tata");
    }

    @Test
    public void retourneFalseSiLaPartieExisteMaisPasCommencer() throws JSONException{
        Response response = target("/game/tata/gamestate").request().get();
        assertEquals(200, response.getStatus());
        JSONObject json = new JSONObject(response.readEntity(String.class));
        assertEquals(false, json.getBoolean("state"));
    }

    @Test
    public void retourneUneErreur404SiPartieNexistePas() {
        Response response = target("/game/sdsdsdss/gamestate").request().get();
        assertEquals(404, response.getStatus());
    }

    @Test
    public void ajouteUnJoueurInexistantDansUnePartie() throws JSONException{
        String json = "{_token: '"+ Config._token+"', pseudo: 'titi'}";
        Entity<String> jsonEntity = Entity.entity(json, MediaType.APPLICATION_JSON);
        Response response = target("/game/tata/addplayer").request().post(jsonEntity);

        assertEquals(200, response.getStatus());
        JSONObject jsonresponse = new JSONObject(response.readEntity(String.class));
        assertTrue(jsonresponse.getBoolean("status"));
    }

    @Test
    public void ajouterUnJoueurExistantDansUnePartie() throws JSONException{
        String json = "{_token: '"+ Config._token+"', pseudo: 'toto'}";
        Entity<String> jsonEntity = Entity.entity(json, MediaType.APPLICATION_JSON);
        Response response = target("/game/tata/addplayer").request().post(jsonEntity);

        assertEquals(405, response.getStatus());
    }
    
    @Test
    public void retourneLeJoueurActuelDeLaPartie() throws JSONException{
    	// Init the game
    	game.addPlayer(model.createPlayer("marcel"));
    	game.addPlayer(model.createPlayer("chris"));
    	game.addPlayer(model.createPlayer("maurice"));
    	game.start();
    	game.getPlayers().get(1).setTurn(true);
    	
    	// Test the methods
        Response response = target("/game/tata/actualplayer").request().get();
        JSONObject json = new JSONObject(response.readEntity(String.class));

        // Assert
        assertEquals(200, response.getStatus());
        assertEquals("marcel", json.getString("pseudo"));
    }
    
    @Test
    public void retourneErreur401SiLaPartieEstPasDemarre() throws JSONException{    	
    	// Test the methods
        Response response = target("/game/tata/actualplayer").request().get();
        JSONObject json = new JSONObject(response.readEntity(String.class));

        // Assert
        assertEquals(401, response.getStatus());
        assertEquals("Game has not begin", json.getString("error"));
    }
    
    @Test
    public void retourneErreur422SiCeNEstLeTourDAucunJoueur() throws JSONException{  
       	// Init the game
    	game.addPlayer(model.createPlayer("marcel"));
    	game.addPlayer(model.createPlayer("chris"));
    	game.addPlayer(model.createPlayer("maurice"));
    	game.start();
    	
    	// Test the methods
        Response response = target("/game/tata/actualplayer").request().get();
        JSONObject json = new JSONObject(response.readEntity(String.class));

        // Assert
        assertEquals(422, response.getStatus());
        assertEquals("No current player has been set", json.getString("error"));
    }

    @Test
    public void lancerUnePartieSansTousLesJoueurs() throws JSONException{
        String json = "{_token: '"+Config._token+"', pseudo: 'toto'}";
        Entity<String> jsonEntity = Entity.entity(json, MediaType.APPLICATION_JSON);
        Response response = target("/game/tata/begingame").request().post(jsonEntity);

        assertEquals(500, response.getStatus());
        assertEquals("Game not tucked", response.readEntity(String.class));
    }

    @Test
    public void lancerUnePartieQuiADejaCommencer() throws JSONException{
        for(int i = 0; i < 3; i++)
            model.addPlayerToGame("tata", model.createPlayer("azert"+i));
        model.findGameByName("tata").start();

        String json = "{_token: '"+Config._token+"', pseudo: 'toto'}";
        Entity<String> jsonEntity = Entity.entity(json, MediaType.APPLICATION_JSON);
        Response response = target("/game/tata/begingame").request().post(jsonEntity);

        assertEquals(500, response.getStatus());
        assertEquals("Game started", response.readEntity(String.class));
    }

    @Test
    public void lancerUnePartieAvecTousLesJoueurs() throws JSONException{
        for(int i = 0; i < 3; i++)
            model.addPlayerToGame("tata", model.createPlayer("azert"+i));

        String json = "{_token: '"+Config._token+"', pseudo: 'toto'}";
        Entity<String> jsonEntity = Entity.entity(json, MediaType.APPLICATION_JSON);
        Response response = target("/game/tata/begingame").request().post(jsonEntity);

        assertEquals(200, response.getStatus());
        JSONObject jsonresponse = new JSONObject(response.readEntity(String.class));
        assertTrue(jsonresponse.getBoolean("status"));
    }


}
