package com.jitterted.ebp.blackjack.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class GameOutcomeTest {

  @Test
  public void playerGoesBustAndLoses() throws Exception {
    Deck stubDeck = new StubDeck(Rank.QUEEN, Rank.EIGHT,
                                 Rank.TEN, Rank.FOUR,
                                 Rank.THREE);
    Game game = new Game(stubDeck);

    game.initialDeal();
    game.playerHits();

    assertThat(game.determineOutcome())
        .isEqualTo("You Busted, so you lose.  💸");
  }

  @Test
  public void playerBeatsDealer() throws Exception {
    Deck stubDeck = new StubDeck(Rank.QUEEN, Rank.EIGHT,
                                 Rank.TEN, Rank.JACK);
    Game game = new Game(stubDeck);

    game.initialDeal();
    game.playerStands();
    game.dealerTurn();

    assertThat(game.determineOutcome())
        .isEqualTo("You beat the Dealer! 💵");
  }

}