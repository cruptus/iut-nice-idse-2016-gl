package fr.unice.idse.db;

import java.sql.SQLException;
import java.util.ArrayList;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataBaseUser extends DataBaseOrigin {
	private Logger logger = LoggerFactory.getLogger(DataBaseUser.class);

	public int getIdUserWithPseudo(String pseudo) {
		String query = "SELECT u_id FROM users WHERE u_pseudo = ?";
		if (executeSQL(query, pseudo))
			try {
				return rs.getInt(1);
			} catch (SQLException e) {
			}
		return 0;
	}
	
	public String getPseudoWithEmail(String email) {
		String query = "SELECT u_pseudo FROM users WHERE u_email = ?";
		if (executeSQL(query, email))
			try {
				return rs.getString("u_pseudo");
			} catch (SQLException e) {
			}
		return null;
	}

	/**
	 * Vérification de login et renvoie les informations nécessaires s'il existe
	 * @param email String
	 * @param password	String
     * @return JSONObject
     */
	public JSONObject verifLogin(String email, String password) {
		JSONObject jsonObject = new JSONObject();
		String query = "SELECT u_pseudo, u_statut, u_banned FROM users WHERE u_email = ? AND u_password = ?";
		try {
			if (executeSQL(query, email, password)) {
				jsonObject.put("pseudo", rs.getString(1));
				jsonObject.put("rang", rs.getInt(2));
				jsonObject.put("banned", rs.getBoolean(3));
			}
		} catch (JSONException e) {
			logger.error(e.getMessage(), e.getCause());
		} catch (SQLException e) {
			logger.error(e.getMessage(), e.getCause());
		}
		return jsonObject;
	}

	public JSONObject allUser(){
		JSONObject jsonObject = new JSONObject();
		ArrayList<JSONObject> players = new ArrayList<>();
		String query = "SELECT u_id, u_email, u_pseudo, u_statut, u_banned FROM users";
		try{
			if(executeSQL(query)){
				do{
					JSONObject tmp = new JSONObject();
					tmp.put("id", rs.getInt(1));
					tmp.put("email", rs.getString(2));
					tmp.put("pseudo", rs.getString(3));
					tmp.put("role", rs.getInt(4));
					tmp.put("banned", rs.getBoolean(5));
					players.add(tmp);
				}while (rs.next());
				jsonObject.put("users", players);
			}
		} catch (JSONException e) {
			logger.error(e.getMessage(), e.getCause());
		} catch (SQLException e) {
			logger.error(e.getMessage(), e.getCause());
		}
		return jsonObject;
	}

	public int getRang(String pseudo){
		String query = "SELECT u_statut FROM users WHERE u_pseudo = ?";
		try {
			if(executeSQL(query, pseudo)){
				return rs.getInt(1);
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return -1;
	}

	
	public boolean userLoginIsCorrect(String email, String password) {
		String query = "SELECT u_email, u_password FROM users WHERE u_email = ? AND u_password = ?";
		return (executeSQL(query, email, password));
	}

	public boolean ifUserAlreadyExistPseudoEmail(String pseudo, String email) {
		String query = "SELECT u_pseudo, u_email FROM users WHERE u_pseudo = ? OR u_email = ?";
		return (executeSQL(query, pseudo, email));
	}

	public boolean ifUserAlreadyExistPseudo(String pseudo) {
		String query = "SELECT u_pseudo FROM users WHERE u_pseudo = ?";
		return (executeSQL(query, pseudo));
	}

	public boolean ifUserAlreadyExistEmail(String email) {
		String query = "SELECT u_pseudo FROM users WHERE u_pseudo = ?";
		return (executeSQL(query, email));
	}

	// 1 bot 2 guest 3 member 4 admin
	public boolean addUser(String pseudo, String email, String password, int statut) {
		if (!ifUserAlreadyExistPseudoEmail(pseudo, email) && isSafeStatut(statut)) {
			String query = "INSERT INTO users (u_pseudo, u_email, u_password, u_statut) VALUES (?, ?, ?, ?)";
			if (executeSQL(query, pseudo, email, password, statut))
				return true;
		}
		return false;
	}

	public boolean deleteUserWithPseudo(String pseudo) {
		if (ifUserAlreadyExistPseudo(pseudo)) {
			String query = "DELETE FROM users WHERE u_pseudo = ?";
			if (executeSQL(query, pseudo))
				return true;
		}
		return false;
	}
	
	public boolean updateUserEmail(String oldEmail, String password, String newEmail) {
		if (userLoginIsCorrect(oldEmail, password) && !ifUserAlreadyExistEmail(newEmail)) {
			String query = "UPDATE users SET u_email = ? WHERE u_email = ?";
			if (executeSQL(query, newEmail, oldEmail))
				if (userLoginIsCorrect(newEmail, password))
					return true;
		}
		return false;
	}

	public boolean updateUserPseudo(String email, String password, String newPseudo) {
		if (userLoginIsCorrect(email, password) && !ifUserAlreadyExistPseudo(newPseudo)) {
			String query = "UPDATE users SET u_pseudo = ? WHERE u_email = ?";
			if (executeSQL(query, newPseudo, email))
				if (getPseudoWithEmail(email).equals(newPseudo))
					return true;
		}
		return false;
	}

	public boolean updateUserPassword(String email, String oldPassword, String newPassword) {
		if (userLoginIsCorrect(email, oldPassword)) {
			String query = "UPDATE users SET u_password = ? WHERE u_email = ?";
			if (executeSQL(query, newPassword, email))
				if (userLoginIsCorrect(email, newPassword))
					return true;
		}
		return false;
	}

	public boolean updateUserBanned(String email, String password, int newBanned) {
		if (userLoginIsCorrect(email, password) && isSafeBanned(newBanned)) {
			String query = "UPDATE users SET u_banned = ? WHERE u_email = ?";
			if (executeSQL(query, newBanned, email))
				return true;
		}
		return false;
	}
}
