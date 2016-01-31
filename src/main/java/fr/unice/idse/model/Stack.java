package fr.unice.idse.model;

import java.util.ArrayList;

public class Stack {
	private ArrayList<Card> stack;
	
	/**
	 * Constructeur du talon
	 */
	public Stack(){
		stack = new ArrayList<Card>();
	}
	
	/**
	 * Getter du talon
	 * @return
	 */
	public ArrayList<Card> getStack(){
		return stack;
	}
	
	/**
	 * Setter du talon
	 * @param stack
	 */
	public void setStack(ArrayList<Card> stack){
		this.stack = stack;
	}
	
	/**
	 * Permet de récupérer la carte supérieure du talon
	 * @return
	 */
	public Card topCard(){
		int size = stack.size();
		if(size != 0){
			return stack.get(size-1);
		}
		return null;
	}
	
	/**
	 * Permet de vider le talon
	 */
	public void clearStack(){
		stack.clear();
	}
	
	/**
	 * Permet d'ajouter une carte dans le talon
	 * @param c
	 */
	public void addCard(Card c){
		stack.add(c);
	}
	
	/**
	 * Initialise le talon en retirant la carte supérieure de la pioche
	 * et en la posant dans le talon
	 * @param deck
	 */
	public void initStack(Deck deck){
		Card deckTopCard = deck.topCard();
		addCard(deckTopCard);
		deck.removeCard(deckTopCard);
	}
	
	/**
	 * Permet de changer la couleur de la carte supérieure du talon
	 * @param color
	 */
	public void changeColor(Color color){
		topCard().setColor(color);
	}
	
	/**
	 * toString du talon
	 */
	public String toString() {
		String result="\nStack :\n\n";
		for(Card card : stack)
		{
			result+="- "+card+"\n";
		}
		return result;
	}
}