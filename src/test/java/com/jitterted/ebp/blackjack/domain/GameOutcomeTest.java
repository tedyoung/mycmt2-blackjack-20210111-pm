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
        .isEqualTo(GameOutcome.PLAYER_BUSTS);
  }

  @Test
  public void playerBeatsDealer() throws Exception {
    Deck stubDeck = new StubDeck(Rank.QUEEN, Rank.EIGHT,
                                 Rank.TEN, Rank.JACK);
    Game game = new Game(stubDeck);

    game.initialDeal();
    game.playerStands();

    assertThat(game.determineOutcome())
        .isEqualTo(GameOutcome.PLAYER_BEATS_DEALER);
  }

  @Test
  public void playerWinsBlackjack() throws Exception {
    Deck stubDeck = new StubDeck(Rank.ACE, Rank.EIGHT,
                                 Rank.TEN, Rank.JACK);
    Game game = new Game(stubDeck);

    game.initialDeal();

    assertThat(game.determineOutcome())
        .isEqualTo(GameOutcome.PLAYER_WINS_BLACKJACK);
  }

}