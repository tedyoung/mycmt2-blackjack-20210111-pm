package com.jitterted.ebp.blackjack.adapter.in.web;

import com.jitterted.ebp.blackjack.domain.Card;
import com.jitterted.ebp.blackjack.domain.Deck;
import com.jitterted.ebp.blackjack.domain.Game;
import com.jitterted.ebp.blackjack.domain.GameOutcome;
import com.jitterted.ebp.blackjack.domain.Rank;
import com.jitterted.ebp.blackjack.domain.StubDeck;
import com.jitterted.ebp.blackjack.domain.Suit;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class BlackjackControllerTest {

  @Test
  public void startGameThenInitialCardsAreDealt() throws Exception {
    Game game = new Game(new Deck());
    BlackjackController blackjackController = new BlackjackController(game);

    blackjackController.startGame();

    assertThat(game.playerHand().cards())
        .hasSize(2);
  }

  @Test
  public void gameViewPopulatesViewModel() throws Exception {
    // GIVEN
    Deck stubDeck = new StubDeck(List.of(new Card(Suit.DIAMONDS, Rank.TEN),
                                     new Card(Suit.HEARTS, Rank.TWO),
                                     new Card(Suit.DIAMONDS, Rank.KING),
                                     new Card(Suit.CLUBS, Rank.THREE)));

    Game game = new Game(stubDeck);
    BlackjackController blackjackController = new BlackjackController(game);
    blackjackController.startGame();

    Model model = new ConcurrentModel();
    blackjackController.viewGame(model);

    GameView gameView = (GameView) model.getAttribute("gameView");

    assertThat(gameView.getDealerCards())
        .containsExactly("2♥", "3♣");

    assertThat(gameView.getPlayerCards())
        .containsExactly("10♦", "K♦");
  }

  @Test
  public void hitCommandDealsAdditionalCardToPlayer() throws Exception {
    Deck stubDeck = new StubDeck(Rank.TEN, Rank.TWO,
                                 Rank.EIGHT, Rank.FIVE,
                                 Rank.TWO);
    Game game = new Game(stubDeck);
    BlackjackController blackjackController = new BlackjackController(game);
    blackjackController.startGame();

    String viewName = blackjackController.hitCommand();

    assertThat(viewName)
        .isEqualTo("redirect:/game");

    assertThat(game.playerHand().cards())
        .hasSize(3);
  }

  @Test
  public void whenPlayerIsDoneAfterBustingRedirectToDonePage() throws Exception {
    Deck stubDeck = new StubDeck(Rank.TEN, Rank.TWO,
                                 Rank.EIGHT, Rank.FIVE,
                                 Rank.FIVE);
    Game game = new Game(stubDeck);
    BlackjackController blackjackController = new BlackjackController(game);
    blackjackController.startGame();

    String viewName = blackjackController.hitCommand();

    assertThat(viewName)
        .isEqualTo("redirect:/done");
  }

  @Test
  public void donePageShowsFinalGameViewWithOutcome() throws Exception {
    Game game = new Game(new Deck());
    BlackjackController blackjackController = new BlackjackController(game);
    blackjackController.startGame();

    Model model = new ConcurrentModel();
    blackjackController.viewDone(model);

    assertThat(model.containsAttribute("gameView"))
        .isTrue();

    String outcome = (String) model.getAttribute("outcome");

    assertThat(outcome)
        .isNotBlank();
  }
  
  @Test
  public void standResultsInGamePlayerIsDone() throws Exception {
    Game game = new Game(new Deck());
    BlackjackController blackjackController = new BlackjackController(game);
    blackjackController.startGame();

    String redirect = blackjackController.standCommand();

    assertThat(game.isPlayerDone())
        .isTrue();

    assertThat(redirect)
        .isEqualTo("redirect:/done");
  }

  @Test
  public void standResultsInPlayerBeatsDealerWhoDrewAdditionalCard() throws Exception {
    Deck stubDeck = new StubDeck(Rank.TEN, Rank.QUEEN,
                                 Rank.EIGHT, Rank.FIVE,
                                 Rank.SIX);
    Game game = new Game(stubDeck);
    BlackjackController blackjackController = new BlackjackController(game);
    blackjackController.startGame();

    blackjackController.standCommand();

    assertThat(game.dealerHand().cards())
        .hasSize(3);

    assertThat(game.determineOutcome())
        .isEqualTo(GameOutcome.PLAYER_LOSES);

  }
}