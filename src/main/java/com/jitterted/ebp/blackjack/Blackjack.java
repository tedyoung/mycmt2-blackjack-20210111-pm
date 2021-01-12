package com.jitterted.ebp.blackjack;

import com.jitterted.ebp.blackjack.adapter.in.console.ConsoleGame;
import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.Game;

public class Blackjack {
  public static void main(String[] args) {
    Deck deck = new Deck();
    Game game = new Game(deck);
    ConsoleGame consoleGame = new ConsoleGame(game);
    consoleGame.start();
  }
}
