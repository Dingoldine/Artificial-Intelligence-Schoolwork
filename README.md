# Artificial-Intelligence-Schoolwork
HIdden Markov Models, Baum-Welsh Algorithm,  Game Theory,  Mini-Max Algorithm for N-dimensonal Tic-Tac-Toe

HHM0
Imagine that we have an estimate of the current state distribution, i.e. we know which of the coins we have used at the last time step with a certain probability. In Figure 2.1, we are located at time step 7 and know the probability of using coin one or two at the previous time step. This knowledge is represented in a row vector, in which each row represents the probability to be in a certain state, here the coin we use, and the sum of all entries is 1. Given this knowledge, we can compute the probability of observing each observation, here head or tail. First, we need to multiply the transition matrix with our current estimate of states.
