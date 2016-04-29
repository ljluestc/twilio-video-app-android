package com.twilio.conversations;

/**
 * ConversationListener interface defines a set of callbacks for events related to a
 * {@link Conversation}.
 */
public interface ConversationListener {
    /**
     * This method notifies the listener when participant has connected to the conversation.
     *
     * @param conversation The conversation.
     * @param participant The participant.
     */
    void onParticipantConnected(Conversation conversation, Participant participant);

    /**
     * This method notifies the listener when a participant was unable to connect to the
     * conversation.
     *
     * @param conversation The conversation.
     * @param participant The participant.
     * @param exception Exception encountered in adding participant to conversation.
     *                  <p>The error codes returned correspond to the following scenarios:
     *                  <ol>
     *                      <li>{@link TwilioConversations#CONVERSATION_REJECTED} returned when
     *                      participant rejects an invite.</li>
     *                      <li>{@link TwilioConversations#CONVERSATION_IGNORED} returned when
     *                      participant ignores an invite</li>
     *                      <li>{@link TwilioConversations#CONVERSATION_FAILED} returned when
     *                      participant rejects an invite to an existing conversation</li>
     *                  </ol>
     */
    void onFailedToConnectParticipant(Conversation conversation,
                                      Participant participant,
                                      TwilioConversationsException exception);

    /**
     * This method notifies the listener when a participant has disconnected from a conversation
     * by request or due to an error.
     *
     * @param conversation The conversation.
     * @param participant The participant.
     */
    void onParticipantDisconnected(Conversation conversation, Participant participant);

    /**
     * This method notifies the listener when the conversation has ended.
     *
     * @param conversation The conversation
     * @param exception Exception (if any) encountered when conversation ends.
     */
    void onConversationEnded(Conversation conversation, TwilioConversationsException exception);

}
