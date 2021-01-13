package com.jitterted.ebp.blackjack.domain;

// Domain Entity
public class Game {

  private final Deck deck;

  private final Hand dealerHand = new Hand();
  private final Hand playerHand = new Hand();
  private boolean playerDone;

  public Game(Deck deck) {
    this.deck = deck;
  }

  public void initialDeal() {
    dealRoundOfCards();
    dealRoundOfCards();
  }

  private void dealRoundOfCards() {
    // why: players first because this is the rule
    playerHand.drawFrom(deck);
    dealerHand.drawFrom(deck);
  }

  public GameOutcome determineOutcome() {
    if (playerHand.isBlackjack()) {
      return GameOutcome.PLAYER_WINS_BLACKJACK;
    } else if (playerHand.isBusted()) {
      return GameOutcome.PLAYER_BUSTS;
    } else if (dealerHand.isBusted()) {
      return GameOutcome.DEALER_BUSTS;
    } else if (playerHand.beats(dealerHand)) {
      return GameOutcome.PLAYER_BEATS_DEALER;
    } else if (playerHand.pushes(dealerHand)) {
      return GameOutcome.PUSHES;
    } else {
      return GameOutcome.PLAYER_LOSES;
    }
  }

  private void dealerTurn() {
    // Dealer makes its choice automatically based on a simple heuristic (<=16, hit, 17>stand)
    if (!playerHand.isBusted()) {
      while (dealerHand.dealerMustDrawCard()) {
        dealerHand.drawFrom(deck);
      }
    }
  }

  public Hand dealerHand() {
    return dealerHand;
  }

  public Hand playerHand() {
    return playerHand;
  }

  public void playerHits() {
//    if (isPlayerDone()) {
//      throw new IllegalStateException();
//    }
    playerHand.drawFrom(deck);
    playerDone = playerHand.isBusted();
  }

  public void playerStands() {
    playerDone = true;
    dealerTurn();
  }

  public boolean isPlayerDone() {
    return playerDone;
  }
}
