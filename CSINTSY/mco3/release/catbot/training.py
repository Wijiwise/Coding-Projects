import random
import time
from typing import Dict
import numpy as np
import pygame
from utility import play_q_table
from cat_env import make_env
#############################################################################
# TODO: YOU MAY ADD ADDITIONAL IMPORTS OR FUNCTIONS HERE.                   #
#############################################################################

"""Get the Manhattan distance between catBot and the cat"""
def get_man_dist(state: int) -> int:
    cat_x = (state // 1000) % 10
    cat_y = (state // 100) % 10
    bot_x = (state // 10) % 10
    bot_y = state % 10
    return abs(cat_x - bot_x) + abs(cat_y - bot_y)
#############################################################################
# END OF YOUR CODE. DO NOT MODIFY ANYTHING BEYOND THIS LINE.                #
#############################################################################

def train_bot(cat_name, render: int = -1):
    env = make_env(cat_type=cat_name)
    
    # Initialize Q-table with all possible states (0-9999)
    # Initially, all action values are zero.
    q_table: Dict[int, np.ndarray] = {
        state: np.zeros(env.action_space.n) for state in range(10000)
    }

    # Training hyperparameters
    episodes = 5000 # Training is capped at 5000 episodes for this project
    
    #############################################################################
    # TODO: YOU MAY DECLARE OTHER VARIABLES AND PERFORM INITIALIZATIONS HERE.   #
    #############################################################################
    # Hint: You may want to declare variables for the hyperparameters of the    #
    # training process such as learning rate, exploration rate, etc.            #
    #############################################################################
    learning_rate = 0.1
    discount_factor = 0.99
    
    exploration_rate = 1.0
    max_exploration_rate = 1.0
    min_exploration_rate = 0.01
    decay_rate = 0.001
    max_steps_per_episode = 60
    #############################################################################
    # END OF YOUR CODE. DO NOT MODIFY ANYTHING BEYOND THIS LINE.                #
    #############################################################################
    
    for ep in range(1, episodes + 1):
        ##############################################################################
        # TODO: IMPLEMENT THE Q-LEARNING TRAINING LOOP HERE.                         #
        ##############################################################################
        # Hint: These are the general steps you must implement for each episode.     #
        # 1. Reset the environment to start a new episode.                           #
        # 2. Decide whether to explore or exploit.                                   #
        # 3. Take the action and observe the next state.                             #
        # 4. Since this environment doesn't give rewards, compute reward manually    #
        # 5. Update the Q-table accordingly based on agent's rewards.                #
        ############################################################################## 
        
        current_state, info = env.reset()
        done = False
        steps = 0
        
        while not done and steps < max_steps_per_episode:
            
            # 2. Decide whether to explore or exploit
            if random.random() < exploration_rate:
                action = env.action_space.sample()  # Explore: select a random action
            else:
                # Exploit: select the action with max Q-value (greedy)
                # Check if multiple actions have the same max Q-value and pick one randomly
                max_q = np.max(q_table[current_state])
                best_actions = np.where(q_table[current_state] == max_q)[0]
                action = random.choice(best_actions)
                
            distance_bef = get_man_dist(current_state)
            
            # 3. Take the action and observe the next state
            next_state, _, terminated, truncated, _ = env.step(action)
            
            is_done = terminated or truncated
            
            # 4. Compute reward manually
            reward = 0.0
            
            if is_done and terminated:
                reward = 10.0 # Large positive reward for catching the cat
            else:
                distance_aft = get_man_dist(next_state)
                
                reward = -0.1 # Base penalty to encourage faster play
                if distance_aft < distance_bef:
                    reward += 1.0 # Positive reward for moving closer
                elif distance_aft > distance_bef:
                    reward -= 1.0 # Negative reward for moving farther
            
            # 5. Update the Q-table
            old_q_value = q_table[current_state][action]
            
            # Calculate the maximum expected future reward (max_a' Q(s', a'))
            next_max = np.max(q_table[next_state])
            
            # Q-Target = R + gamma * max_a' Q(s', a') * (1 - is_done)
            # (1 - is_done) ensures next_max is zero if the episode terminated
            q_target = reward + discount_factor * next_max * (1 - is_done)
            
            # New Q-Value = old_q_value + learning_rate * (Q_Target - old_q_value)
            q_table[current_state][action] = old_q_value + learning_rate * (q_target - old_q_value)

            current_state = next_state
            steps += 1 # Update step counter
        
        # Decay exploration rate (epsilon)
        exploration_rate = min_exploration_rate + (max_exploration_rate - min_exploration_rate) * np.exp(-decay_rate * ep)

        #############################################################################
        # END OF YOUR CODE. DO NOT MODIFY ANYTHING BEYOND THIS LINE.                #
        #############################################################################

        # If rendering is enabled, play an episode every 'render' episodes
        if render != -1 and (ep == 1 or ep % render == 0):
            viz_env = make_env(cat_type=cat_name)
            play_q_table(viz_env, q_table, max_steps=100, move_delay=0.02, window_title=f"{cat_name}: Training Episode {ep}/{episodes}")
            print('episode', ep)

    return q_table